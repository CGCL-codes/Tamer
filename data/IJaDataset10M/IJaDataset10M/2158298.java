package org.mcisb.celldesigner.subliminal;

import java.util.*;
import jp.sbi.celldesigner.plugin.*;
import org.sbml.libsbml.*;
import org.mcisb.celldesigner.*;
import org.mcisb.sbml.*;

/**
 * @author Neil Swainston
 */
public class CellDesignerSbmlDocumentBuilder extends SbmlDocumentBuilder {

    /**
	 * 
	 */
    private PluginModel pluginModel = null;

    /**
	 * 
	 */
    private final Map<Object, Integer> speciesIdsToIndices = new HashMap<Object, Integer>();

    /**
	 * 
	 * @param pluginModel
	 * @throws Exception 
	 */
    public void setModel(final PluginModel pluginModel) throws Exception {
        this.pluginModel = pluginModel;
        init();
    }

    @Override
    public void init() throws Exception {
        for (int i = 0; i < pluginModel.getNumSpecies(); i++) {
            final PluginSpecies pluginSpecies = pluginModel.getSpecies(i);
            speciesIdsToIndices.put(getKey(pluginSpecies.getName(), pluginSpecies.getCompartment()), Integer.valueOf(i));
        }
    }

    @Override
    protected void addCompartment(final Compartment compartment) throws Exception {
        final PluginCompartment sbase = CellDesignerUtils.getPluginCompartment(compartment);
        pluginModel.addCompartment(sbase);
        add(sbase);
    }

    @Override
    public void addSpecies(final Species species) throws Exception {
        final PluginSpecies sbase = CellDesignerUtils.getPluginSpecies(species);
        pluginModel.addSpecies(sbase);
        add(sbase);
    }

    @Override
    public void deleteSpecies(final Species species) throws Exception {
        final int index = speciesIdsToIndices.get(getKey(species.getName(), species.getCompartment())).intValue();
        final PluginSpecies sbase = pluginModel.getSpecies(index);
        pluginModel.getListOfSpecies().remove(sbase);
        delete(sbase);
    }

    /**
	 * 
	 * @param speciesName
	 * @param speciesCompartment
	 * @return Object
	 */
    private Object getKey(final String speciesName, final String speciesCompartment) {
        return new ArrayList<String>(Arrays.asList(speciesName, speciesCompartment));
    }

    /**
	 * 
	 * @param sbase
	 */
    private void add(final PluginSBase sbase) {
        support.firePropertyChange(ADDED, null, sbase);
    }

    /**
	 * 
	 * @param sbase
	 */
    private void delete(final PluginSBase sbase) {
        support.firePropertyChange(DELETED, null, sbase);
    }
}
