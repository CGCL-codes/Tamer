package edu.udo.scaffoldhunter.model.dataimport;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.io.MDLV2000Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import edu.udo.scaffoldhunter.gui.util.ProgressListener;
import edu.udo.scaffoldhunter.gui.util.ProgressSupport;
import edu.udo.scaffoldhunter.model.data.Message;
import edu.udo.scaffoldhunter.model.data.MessageListener;
import edu.udo.scaffoldhunter.model.dataimport.ImportJob.SourcePropertyMapping;
import edu.udo.scaffoldhunter.model.db.Dataset;
import edu.udo.scaffoldhunter.model.db.MoleculeNumProperty;
import edu.udo.scaffoldhunter.model.db.MoleculeStringProperty;
import edu.udo.scaffoldhunter.model.db.Property;
import edu.udo.scaffoldhunter.model.db.PropertyDefinition;
import edu.udo.scaffoldhunter.model.util.CanonicalSmilesGenerator;
import edu.udo.scaffoldhunter.model.util.SVGGen;
import edu.udo.scaffoldhunter.model.util.SVGGenResult;

/**
 * An Iterator which can be used to merge an {@link ImportJob} into the
 * database.
 * <p>
 * This iterator returns the smile strings of the molecules read by the import
 * job. Based on that information clients can use the methods
 * {@link #newMolecule} and {@link #mergeInto} to retrieve the new data. This
 * iterator does not interact with the db itselft. Saving or updating data in
 * the database is sole responsibility of a client.
 * 
 * @author Henning Garus
 * @author Michael Hesse
 * 
 */
public class MergeIterator implements Iterator<String> {

    private static final Logger logger = LoggerFactory.getLogger(MergeIterator.class);

    private final ImportJob importjob;

    private final Dataset dataset;

    private final String mergeBy;

    private final boolean mergeByNumeric;

    private final Collection<PropertyDefinition> mergedProperties;

    private final Iterator<Molecule> molIterator;

    private final Set<String> mergedPropertyKeys = Sets.newHashSet();

    private final List<Entry<String, SourcePropertyMapping>> mappings = Lists.newArrayList();

    private final Set<String> insertedMoleculeSMILES = Sets.newHashSet();

    private final StringWriter structureBuffer = new StringWriter();

    private final MDLV2000Writer structureWriter = new MDLV2000Writer(structureBuffer);

    private final List<MessageListener> messageListeners = Lists.newArrayList();

    private final ProgressSupport<Void> progressListeners = new ProgressSupport<Void>();

    private Molecule currentMolecule;

    private Molecule nextMolecule;

    private String currentSmiles;

    private String nextSmiles;

    private int progress = 0;

    /**
     * Create a new Merge Iterator
     * 
     * @param importJob
     *            the import job which should be merged by this iterator
     * @param dataset
     *            the dataset into which the data should be merged
     * @param mergedProperties
     *            the propertyDefinitons for which there may already be
     *            properties in the dataset. Properties associated with other
     *            property definitions encountered in the import job will be
     *            treated as new.
     * @param mergeBy
     *            the source property which is used for merging, if any.
     *            Otherwise <code>null</code> (if merging is done by SMILES)
     * @param mergeByNumeric
     *            should the source property specified in mergeBy be treated as
     *            numeric. If mergeBy is <code>null</code> this value will be
     *            ignored.
     */
    public MergeIterator(ImportJob importJob, Dataset dataset, Collection<PropertyDefinition> mergedProperties, String mergeBy, boolean mergeByNumeric) {
        this.importjob = importJob;
        this.dataset = dataset;
        this.mergeBy = mergeBy;
        this.mergeByNumeric = mergeByNumeric;
        this.molIterator = importJob.getResults().getMolecules().iterator();
        this.mergedProperties = mergedProperties;
        for (Entry<String, SourcePropertyMapping> e : importjob.getPropertyMappings().entrySet()) {
            if (e.getValue() != null && e.getValue().getPropertyDefiniton() != null) mappings.add(e);
            if (mergedProperties.contains(e.getValue().getPropertyDefiniton())) mergedPropertyKeys.add(e.getKey());
        }
        if (mergeBy == null) {
            getNextMolecule();
        }
    }

    /**
     * gets the value of the specified property from the cdk molecule
     * 
     * @param sourceMolecule
     * @param key
     */
    private Double getNumPropertyFromCdk(org.openscience.cdk.Molecule cdkMolecule, String key, String title) {
        Object cdkPropertyValue = cdkMolecule.getProperty(key);
        if (cdkPropertyValue == null) {
            sendMessage(new Message(MergeMessageTypes.PROPERTY_NOT_PRESENT, title, importjob.getPropertyMappings().get(key).getPropertyDefiniton(), importjob));
            return null;
        }
        if (cdkPropertyValue instanceof Number) return (Double) cdkPropertyValue;
        if (cdkPropertyValue instanceof String) {
            Double result = null;
            try {
                result = new Double((String) cdkPropertyValue);
            } catch (NumberFormatException e) {
            }
            if (result == null || Double.isNaN(result) || Double.isInfinite(result)) {
                sendMessage(new Message(MergeMessageTypes.DOUBLE_CONVERSION_ERROR, title, importjob.getPropertyMappings().get(key).getPropertyDefiniton(), importjob));
                return null;
            }
            return result;
        }
        sendMessage(new Message(MergeMessageTypes.DOUBLE_CONVERSION_ERROR, title, importjob.getPropertyMappings().get(key).getPropertyDefiniton(), importjob));
        return null;
    }

    private void mergeProperties(edu.udo.scaffoldhunter.model.db.Molecule shMol, Molecule mol, List<Property> newProperties, List<Property> updatedProperties) {
        for (String propertyKey : mergedPropertyKeys) {
            Object newProp = mol.getProperty(propertyKey);
            if (newProp == null) continue;
            SourcePropertyMapping mapping = importjob.getPropertyMappings().get(propertyKey);
            PropertyDefinition propdef = mapping.getPropertyDefiniton();
            if (propdef.isStringProperty()) {
                MoleculeStringProperty stringProp = shMol.getStringProperties().get(propdef.getId());
                String newValue = (String) mol.getProperty(propertyKey);
                if (stringProp == null) {
                    if (newValue == null) {
                        sendMessage(new Message(MergeMessageTypes.PROPERTY_NOT_PRESENT, shMol.getTitle(), propdef, importjob));
                        continue;
                    }
                    stringProp = new MoleculeStringProperty(propdef, newValue);
                    stringProp.setMolecule(shMol);
                    newProperties.add(stringProp);
                } else {
                    String oldValue = stringProp.getValue();
                    if (newValue == null) {
                        sendMessage(new Message(MergeMessageTypes.PROPERTY_NOT_PRESENT, shMol.getTitle(), propdef, importjob));
                    } else {
                        stringProp.setValue(mapping.getMergeStrategy().apply(oldValue, newValue));
                        updatedProperties.add(stringProp);
                        sendMessage(new Message(mapping.getMergeStrategy().getMessageType(), shMol.getTitle(), propdef, importjob));
                    }
                }
            } else {
                MoleculeNumProperty numProp = shMol.getNumProperties().get(propdef.getId());
                Double newValue = getNumPropertyFromCdk(mol, propertyKey, shMol.getTitle());
                if (newValue != null && mapping.getTransformFunction() != null) {
                    newValue = mapping.getTransformFunction().calculate(newValue);
                }
                if (numProp == null) {
                    if (newValue == null) {
                        continue;
                    }
                    numProp = new MoleculeNumProperty(propdef, newValue);
                    numProp.setMolecule(shMol);
                    newProperties.add(numProp);
                } else {
                    Double oldValue = numProp.getValue();
                    if (newValue == null) {
                        sendMessage(new Message(MergeMessageTypes.PROPERTY_NOT_PRESENT, shMol.getTitle(), propdef, importjob));
                    } else {
                        numProp.setValue(mapping.getMergeStrategy().apply(oldValue, newValue));
                        sendMessage(new Message(mapping.getMergeStrategy().getMessageType(), shMol.getTitle(), propdef, importjob));
                        updatedProperties.add(numProp);
                    }
                }
            }
        }
    }

    private void fillPropertyMaps(edu.udo.scaffoldhunter.model.db.Molecule shMol, Molecule cdkMol, List<Property> newProperties) {
        for (Entry<String, SourcePropertyMapping> e : mappings) {
            PropertyDefinition propdef = e.getValue().getPropertyDefiniton();
            if (cdkMol.getProperty(e.getKey()) == null) {
                sendMessage(new Message(MergeMessageTypes.PROPERTY_NOT_PRESENT, shMol.getTitle(), propdef, importjob));
                continue;
            }
            if (mergedPropertyKeys.contains(e.getKey()) || e.getKey().equals(mergeBy)) {
                continue;
            }
            if (propdef.isStringProperty()) {
                Object property = cdkMol.getProperty(e.getKey());
                if (property == null || !(property instanceof String)) continue;
                MoleculeStringProperty prop = new MoleculeStringProperty(propdef, (String) property);
                prop.setMolecule(shMol);
                shMol.getStringProperties().put(propdef.getId(), prop);
                newProperties.add(prop);
            } else {
                Double value = getNumPropertyFromCdk(cdkMol, e.getKey(), shMol.getTitle());
                if (value == null) continue;
                if (e.getValue().getTransformFunction() != null) value = e.getValue().getTransformFunction().calculate(value);
                MoleculeNumProperty numProp = new MoleculeNumProperty(propdef, value);
                numProp.setMolecule(shMol);
                shMol.getNumProperties().put(propdef.getId(), numProp);
                newProperties.add(numProp);
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (mergeBy == null) {
            return nextMolecule != null;
        } else {
            return molIterator.hasNext();
        }
    }

    /**
     * forward the iterator to the next molecule in the import job.
     * 
     * @return the next molecule's SMILES string
     */
    @Override
    public String next() {
        currentMolecule = nextMolecule;
        currentSmiles = nextSmiles;
        getNextMolecule();
        return currentSmiles;
    }

    /**
     * forward the iterator to the next molecule in the import job.
     * 
     * @return the value of the molecule's <code>mergeBy</code> property either
     *         as a String or as a Double as specified by
     *         <code>mergeByNumeric</code>. Returns <code>null</code> if the
     *         property is undefined for the molecule.
     */
    public Object nextID() {
        currentMolecule = molIterator.next();
        progressListeners.setProgressValue(progress++);
        String value = (String) currentMolecule.getProperty(mergeBy);
        if (mergeByNumeric) {
            try {
                double val = Double.parseDouble(value);
                return val;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return value;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a new Molecule based on the current molecule
     * 
     * @param newProperties
     *            a list to which all added newly created properties are added
     * 
     * @return A new Molecule based on the current molecule, may return 
     *         <code>null</code> in case of errors.
     */
    public edu.udo.scaffoldhunter.model.db.Molecule newMolecule(List<Property> newProperties) {
        logger.trace("newMolecule");
        Map<Integer, MoleculeStringProperty> stringProps = Maps.newHashMap();
        Map<Integer, MoleculeNumProperty> numProps = Maps.newHashMap();
        Object prop = currentMolecule.getProperty(importjob.getTitleProperty());
        String title;
        if (prop != null && prop instanceof String) {
            title = (String) prop;
        } else {
            title = currentSmiles;
            sendMessage(new Message(MergeMessageTypes.NO_TITLE, currentSmiles, null, importjob));
        }
        SVGGenResult svgResult = SVGGen.getSVG(currentMolecule);
        StringWriter strucMol = new StringWriter();
        MDLV2000Writer writer = new MDLV2000Writer(strucMol);
        try {
            writer.write(currentMolecule);
        } catch (CDKException e) {
            sendMessage(new Message(MergeMessageTypes.STRUCTURE_WRITE_ERROR, title, null, importjob));
            return null;
        }
        edu.udo.scaffoldhunter.model.db.Molecule newMolecule = new edu.udo.scaffoldhunter.model.db.Molecule(dataset, stringProps, numProps, title, currentSmiles, svgResult.getSvgString(), svgResult.getHeight(), svgResult.getWidth(), strucMol.toString());
        fillPropertyMaps(newMolecule, currentMolecule, newProperties);
        insertedMoleculeSMILES.add(currentSmiles);
        return newMolecule;
    }

    /**
     * merge the data of the current molecule into the provided molecule
     * 
     * @param dbMol
     *            the molecule into which the data of the current molecule
     *            should be merged.
     * @param newProperties
     *            a list which will be filled with all properties which are
     *            newly created
     * @param updatedProperties
     *            a list which will be filled with all properties which are
     *            updated
     */
    public void mergeInto(edu.udo.scaffoldhunter.model.db.Molecule dbMol, List<Property> newProperties, List<Property> updatedProperties) {
        logger.trace("mergeInto");
        String newTitle = (String) currentMolecule.getProperty(importjob.getTitleProperty());
        dbMol.setTitle(importjob.getTitleMergeStrategy().apply(dbMol.getTitle(), newTitle));
        if (importjob.getStructureMergeStrategy() == MergeStrategy.OVERWRITE) {
            structureBuffer.getBuffer().setLength(0);
            try {
                structureWriter.write(currentMolecule);
                dbMol.setStrucMol(structureWriter.toString());
            } catch (CDKException e) {
                sendMessage(new Message(MergeMessageTypes.STRUCTURE_WRITE_ERROR, dbMol.getTitle(), null, importjob));
            }
        }
        fillPropertyMaps(dbMol, currentMolecule, newProperties);
        mergeProperties(dbMol, currentMolecule, newProperties, updatedProperties);
        insertedMoleculeSMILES.add(currentSmiles);
    }

    /**
     * @return the dataset
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * @return the mergedProperties
     */
    public Collection<PropertyDefinition> getMergedProperties() {
        return mergedProperties;
    }

    /**
     * Add a progress listener to this iterator
     * 
     * @param listener
     */
    public void addProgressListener(ProgressListener<Void> listener) {
        progressListeners.addProgressListener(listener);
    }

    /**
     * remove a progress listener from this iterator
     * 
     * @param listener
     */
    public void removeProgressListener(ProgressListener<Void> listener) {
        progressListeners.removeProgressListener(listener);
    }

    /**
     * add a message listener to this iterator
     * 
     * @param listener
     */
    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    /**
     * remove a message listener from this iterator
     * 
     * @param listener
     */
    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    private void sendMessage(Message message) {
        for (MessageListener l : messageListeners) l.receiveMessage(message);
    }

    /**
     * Retrieves the next "valid" molecule from the plugin's iterator and saves
     * it in nextMolecule.
     * <p>
     * A Molecule is a valid if it has a structure and a SMILES was generated
     * successfully.
     */
    private void getNextMolecule() {
        do {
            if (!molIterator.hasNext()) {
                nextMolecule = null;
                return;
            }
            nextMolecule = molIterator.next();
            progressListeners.setProgressValue(++progress);
            if (nextMolecule.getAtomCount() == 0) {
                Object title = nextMolecule.getProperty(importjob.getTitleProperty());
                String titleString = title != null ? title.toString() : "";
                sendMessage(new Message(MergeMessageTypes.NO_STRUCTURE_ERROR, titleString, null, importjob));
                continue;
            }
            nextSmiles = CanonicalSmilesGenerator.createSMILES(nextMolecule);
            if (nextSmiles.isEmpty()) {
                Object title = nextMolecule.getProperty(importjob.getTitleProperty());
                String titleString = title != null ? title.toString() : "";
                sendMessage(new Message(MergeMessageTypes.SMILES_ERROR, titleString, null, importjob));
                continue;
            }
            if (insertedMoleculeSMILES.contains(nextSmiles) || nextSmiles.equals(currentSmiles)) {
                Object title = nextMolecule.getProperty(importjob.getTitleProperty());
                String titleString = title != null ? title.toString() : "";
                sendMessage(new Message(MergeMessageTypes.SAME_MOLECULE, titleString, null, importjob));
                nextSmiles = null;
            }
        } while (Strings.isNullOrEmpty(nextSmiles));
    }
}
