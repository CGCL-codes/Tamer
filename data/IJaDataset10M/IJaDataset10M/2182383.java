package org.openscience.cdk.io.cml;

import java.util.Hashtable;
import javax.vecmath.Vector3d;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.dict.DictRef;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.Atom;
import org.openscience.cdk.interfaces.AtomContainer;
import org.openscience.cdk.interfaces.Bond;
import org.openscience.cdk.interfaces.ChemFile;
import org.openscience.cdk.interfaces.ChemModel;
import org.openscience.cdk.interfaces.ChemObjectBuilder;
import org.openscience.cdk.interfaces.ChemObjectChangeEvent;
import org.openscience.cdk.interfaces.ChemObjectListener;
import org.openscience.cdk.interfaces.ChemSequence;
import org.openscience.cdk.interfaces.Crystal;
import org.openscience.cdk.interfaces.Molecule;
import org.openscience.cdk.interfaces.PseudoAtom;
import org.openscience.cdk.interfaces.Reaction;
import org.openscience.cdk.interfaces.Ring;
import org.openscience.cdk.interfaces.RingSet;
import org.openscience.cdk.interfaces.SetOfMolecules;
import org.openscience.cdk.interfaces.SetOfReactions;
import org.openscience.cdk.io.cml.cdopi.CDOAcceptedObjects;
import org.openscience.cdk.io.cml.cdopi.CDOInterface;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.tools.DeAromatizationTool;
import org.openscience.cdk.tools.LoggingTool;

/**
 * CDO object needed as interface with the JCFL library for reading CML
 * encoded data.
 *
 * @cdk.module io
 * 
 * @author Egon Willighagen <egonw@sci.kun.nl>
 */
public class ChemFileCDO implements ChemFile, CDOInterface {

    private ChemFile currentChemFile;

    private AtomContainer currentMolecule;

    private SetOfMolecules currentSetOfMolecules;

    private ChemModel currentChemModel;

    private ChemSequence currentChemSequence;

    private SetOfReactions currentSetOfReactions;

    private Reaction currentReaction;

    private Atom currentAtom;

    private Hashtable atomEnumeration;

    private int numberOfAtoms = 0;

    private int bond_a1;

    private int bond_a2;

    private double bond_order;

    private int bond_stereo;

    private String bond_id;

    private double crystal_axis_x;

    private double crystal_axis_y;

    private double crystal_axis_z;

    private double aromaticOrder = 1.5;

    protected LoggingTool logger;

    /**
     * Basic contructor
     */
    public ChemFileCDO(ChemFile file) {
        logger = new LoggingTool(this);
        currentChemFile = file;
        currentChemSequence = file.getBuilder().newChemSequence();
        currentChemModel = file.getBuilder().newChemModel();
        currentSetOfMolecules = file.getBuilder().newSetOfMolecules();
        currentSetOfReactions = null;
        currentReaction = null;
        currentMolecule = file.getBuilder().newMolecule();
        atomEnumeration = new Hashtable();
    }

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void startDocument() {
        logger.info("New CDO Object");
        currentChemSequence = currentChemFile.getBuilder().newChemSequence();
        currentChemModel = currentChemFile.getBuilder().newChemModel();
        currentSetOfMolecules = currentChemFile.getBuilder().newSetOfMolecules();
        currentMolecule = currentChemFile.getBuilder().newMolecule();
        atomEnumeration = new Hashtable();
    }

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void endDocument() {
        logger.debug("Closing document");
        if (currentSetOfReactions != null && currentSetOfReactions.getReactionCount() == 0 && currentReaction != null) {
            logger.debug("Adding reaction to SetOfReactions");
            currentSetOfReactions.addReaction(currentReaction);
        }
        if (currentSetOfReactions != null && currentChemModel.getSetOfReactions() == null) {
            logger.debug("Adding SOR to ChemModel");
            currentChemModel.setSetOfReactions(currentSetOfReactions);
        }
        if (currentSetOfMolecules != null && currentSetOfMolecules.getMoleculeCount() != 0) {
            logger.debug("Adding reaction to SetOfMolecules");
            currentChemModel.setSetOfMolecules(currentSetOfMolecules);
        }
        if (currentChemSequence.getChemModelCount() == 0) {
            logger.debug("Adding ChemModel to ChemSequence");
            currentChemSequence.addChemModel(currentChemModel);
        }
        if (getChemSequenceCount() == 0) {
            addChemSequence(currentChemSequence);
        }
        logger.info("End CDO Object");
        logger.info("Number of sequences:", getChemSequenceCount());
    }

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void setDocumentProperty(String type, String value) {
    }

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void startObject(String objectType) {
        logger.debug("START:" + objectType);
        if (objectType.equals("Molecule")) {
            if (currentChemModel == null) currentChemModel = currentChemFile.getBuilder().newChemModel();
            if (currentSetOfMolecules == null) currentSetOfMolecules = currentChemFile.getBuilder().newSetOfMolecules();
            currentMolecule = currentChemFile.getBuilder().newMolecule();
        } else if (objectType.equals("Atom")) {
            currentAtom = currentChemFile.getBuilder().newAtom("H");
            logger.debug("Atom # " + numberOfAtoms);
            numberOfAtoms++;
        } else if (objectType.equals("Bond")) {
            bond_id = null;
            bond_stereo = -99;
        } else if (objectType.equals("Animation")) {
            currentChemSequence = currentChemFile.getBuilder().newChemSequence();
        } else if (objectType.equals("Frame")) {
            currentChemModel = currentChemFile.getBuilder().newChemModel();
        } else if (objectType.equals("SetOfMolecules")) {
            currentSetOfMolecules = currentChemFile.getBuilder().newSetOfMolecules();
            currentMolecule = currentChemFile.getBuilder().newMolecule();
        } else if (objectType.equals("Crystal")) {
            currentMolecule = currentChemFile.getBuilder().newCrystal(currentMolecule);
        } else if (objectType.equals("a-axis") || objectType.equals("b-axis") || objectType.equals("c-axis")) {
            crystal_axis_x = 0.0;
            crystal_axis_y = 0.0;
            crystal_axis_z = 0.0;
        } else if (objectType.equals("SetOfReactions")) {
            currentSetOfReactions = currentChemFile.getBuilder().newSetOfReactions();
        } else if (objectType.equals("Reaction")) {
            if (currentSetOfReactions == null) startObject("SetOfReactions");
            currentReaction = currentChemFile.getBuilder().newReaction();
        } else if (objectType.equals("Reactant")) {
            if (currentReaction == null) startObject("Reaction");
            currentMolecule = currentChemFile.getBuilder().newMolecule();
        } else if (objectType.equals("Product")) {
            if (currentReaction == null) startObject("Reaction");
            currentMolecule = currentChemFile.getBuilder().newMolecule();
        }
    }

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void endObject(String objectType) {
        logger.debug("END: " + objectType);
        if (objectType.equals("Molecule")) {
            if (currentMolecule instanceof Molecule) {
                logger.debug("Adding molecule to set");
                currentSetOfMolecules.addMolecule((Molecule) currentMolecule);
                logger.debug("#mols in set: " + currentSetOfMolecules.getMoleculeCount());
            } else if (currentMolecule instanceof Crystal) {
                logger.debug("Adding crystal to chemModel");
                currentChemModel.setCrystal((Crystal) currentMolecule);
                currentChemSequence.addChemModel(currentChemModel);
            }
        } else if (objectType.equals("SetOfMolecules")) {
            currentChemModel.setSetOfMolecules(currentSetOfMolecules);
            currentChemSequence.addChemModel(currentChemModel);
        } else if (objectType.equals("Frame")) {
        } else if (objectType.equals("Animation")) {
            addChemSequence(currentChemSequence);
            logger.info("This file has " + getChemSequenceCount() + " sequence(s).");
        } else if (objectType.equals("Atom")) {
            currentMolecule.addAtom(currentAtom);
        } else if (objectType.equals("Bond")) {
            logger.debug("Bond(" + bond_id + "): " + bond_a1 + ", " + bond_a2 + ", " + bond_order);
            if (bond_a1 > currentMolecule.getAtomCount() || bond_a2 > currentMolecule.getAtomCount()) {
                logger.error("Cannot add bond between at least one non-existant atom: " + bond_a1 + " and " + bond_a2);
            } else {
                org.openscience.cdk.interfaces.Atom a1 = currentMolecule.getAtomAt(bond_a1);
                org.openscience.cdk.interfaces.Atom a2 = currentMolecule.getAtomAt(bond_a2);
                Bond b = currentChemFile.getBuilder().newBond(a1, a2, bond_order);
                if (bond_id != null) b.setID(bond_id);
                if (bond_stereo != -99) {
                    b.setStereo(bond_stereo);
                    System.err.println(bond_stereo + " stereo");
                }
                if (bond_order == CDKConstants.BONDORDER_AROMATIC) {
                    b.setFlag(CDKConstants.ISAROMATIC, true);
                    b.setOrder(aromaticOrder);
                }
                currentMolecule.addBond(b);
            }
        } else if (objectType.equals("a-axis")) {
            if (currentMolecule instanceof Crystal) {
                Crystal current = (Crystal) currentMolecule;
                current.setA(new Vector3d(crystal_axis_x, crystal_axis_y, crystal_axis_z));
            } else {
                logger.warn("Current object is not a crystal");
            }
        } else if (objectType.equals("b-axis")) {
            if (currentMolecule instanceof Crystal) {
                Crystal current = (Crystal) currentMolecule;
                current.setB(new Vector3d(crystal_axis_x, crystal_axis_y, crystal_axis_z));
            } else {
                logger.warn("Current object is not a crystal");
            }
        } else if (objectType.equals("c-axis")) {
            if (currentMolecule instanceof Crystal) {
                Crystal current = (Crystal) currentMolecule;
                current.setC(new Vector3d(crystal_axis_x, crystal_axis_y, crystal_axis_z));
            } else {
                logger.warn("Current object is not a crystal");
            }
        } else if (objectType.equals("SetOfReactions")) {
            currentChemModel.setSetOfReactions(currentSetOfReactions);
            currentChemSequence.addChemModel(currentChemModel);
        } else if (objectType.equals("Reaction")) {
            logger.debug("Adding reaction to SOR");
            currentSetOfReactions.addReaction(currentReaction);
        } else if (objectType.equals("Reactant")) {
            currentReaction.addReactant((Molecule) currentMolecule);
        } else if (objectType.equals("Product")) {
            currentReaction.addProduct((Molecule) currentMolecule);
        } else if (objectType.equals("Crystal")) {
            logger.debug("Crystal: " + currentMolecule);
        }
    }

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void setObjectProperty(String objectType, String propertyType, String propertyValue) {
        logger.debug("objectType: " + objectType);
        logger.debug("propType: " + propertyType);
        logger.debug("property: " + propertyValue);
        if (objectType == null) {
            logger.error("Cannot add property for null object");
            return;
        }
        if (propertyType == null) {
            logger.error("Cannot add property for null property type");
            return;
        }
        if (propertyValue == null) {
            logger.warn("Will not add null property");
            return;
        }
        if (objectType.equals("Molecule")) {
            if (propertyType.equals("id")) {
                currentMolecule.setID(propertyValue);
            } else if (propertyType.equals("inchi")) {
                currentMolecule.setProperty("iupac.nist.chemical.identifier", propertyValue);
            } else if (propertyType.equals("pdb:residueName")) {
                currentMolecule.setProperty(new DictRef(propertyType, propertyValue), propertyValue);
            } else if (propertyType.equals("pdb:oneLetterCode")) {
                currentMolecule.setProperty(new DictRef(propertyType, propertyValue), propertyValue);
            } else if (propertyType.equals("pdb:id")) {
                currentMolecule.setProperty(new DictRef(propertyType, propertyValue), propertyValue);
            } else {
                logger.warn("Not adding molecule property!");
            }
        } else if (objectType.equals("PseudoAtom")) {
            if (propertyType.equals("label")) {
                if (!(currentAtom instanceof PseudoAtom)) {
                    currentAtom = currentChemFile.getBuilder().newPseudoAtom(currentAtom);
                }
                ((PseudoAtom) currentAtom).setLabel(propertyValue);
            }
        } else if (objectType.equals("Atom")) {
            if (propertyType.equals("type")) {
                if (propertyValue.equals("R") && !(currentAtom instanceof PseudoAtom)) {
                    currentAtom = currentChemFile.getBuilder().newPseudoAtom(currentAtom);
                }
                currentAtom.setSymbol(propertyValue);
            } else if (propertyType.equals("x2")) {
                currentAtom.setX2d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("y2")) {
                currentAtom.setY2d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("x3")) {
                currentAtom.setX3d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("y3")) {
                currentAtom.setY3d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("z3")) {
                currentAtom.setZ3d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("xFract")) {
                currentAtom.setFractX3d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("yFract")) {
                currentAtom.setFractY3d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("zFract")) {
                currentAtom.setFractZ3d(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("formalCharge")) {
                currentAtom.setFormalCharge(new Integer(propertyValue).intValue());
            } else if (propertyType.equals("charge") || propertyType.equals("partialCharge")) {
                currentAtom.setCharge(new Double(propertyValue).doubleValue());
            } else if (propertyType.equals("hydrogenCount")) {
                currentAtom.setHydrogenCount(new Integer(propertyValue).intValue());
            } else if (propertyType.equals("dictRef")) {
                currentAtom.setProperty("org.openscience.cdk.dict", propertyValue);
            } else if (propertyType.equals("atomicNumber")) {
                currentAtom.setAtomicNumber(Integer.parseInt(propertyValue));
            } else if (propertyType.equals("massNumber")) {
                currentAtom.setMassNumber((new Double(propertyValue)).intValue());
            } else if (propertyType.equals("spinMultiplicity")) {
                int unpairedElectrons = new Integer(propertyValue).intValue() - 1;
                for (int i = 0; i < unpairedElectrons; i++) {
                    currentMolecule.addElectronContainer(currentChemFile.getBuilder().newSingleElectron(currentAtom));
                }
            } else if (propertyType.equals("id")) {
                logger.debug("id: ", propertyValue);
                currentAtom.setID(propertyValue);
                atomEnumeration.put(propertyValue, new Integer(numberOfAtoms));
            }
        } else if (objectType.equals("Bond")) {
            if (propertyType.equals("atom1")) {
                bond_a1 = new Integer(propertyValue).intValue();
            } else if (propertyType.equals("atom2")) {
                bond_a2 = new Integer(propertyValue).intValue();
            } else if (propertyType.equals("id")) {
                logger.debug("id: " + propertyValue);
                bond_id = propertyValue;
            } else if (propertyType.equals("order")) {
                try {
                    bond_order = Double.parseDouble(propertyValue);
                } catch (Exception e) {
                    logger.error("Cannot convert to double: " + propertyValue);
                    bond_order = 1.0;
                }
            } else if (propertyType.equals("stereo")) {
                if (propertyValue.equals("H")) {
                    bond_stereo = CDKConstants.STEREO_BOND_DOWN;
                } else if (propertyValue.equals("W")) {
                    bond_stereo = CDKConstants.STEREO_BOND_UP;
                }
            }
        } else if (objectType.equals("Reaction")) {
            if (propertyType.equals("id")) {
                currentReaction.setID(propertyValue);
            }
        } else if (objectType.equals("SetOfReactions")) {
            if (propertyType.equals("id")) {
                currentSetOfReactions.setID(propertyValue);
            }
        } else if (objectType.equals("Reactant")) {
            if (propertyType.equals("id")) {
                currentMolecule.setID(propertyValue);
            }
        } else if (objectType.equals("Product")) {
            if (propertyType.equals("id")) {
                currentMolecule.setID(propertyValue);
            }
        } else if (objectType.equals("Crystal")) {
            if (currentMolecule instanceof Crystal) {
                Crystal current = (Crystal) currentMolecule;
                if (propertyType.equals("spacegroup")) {
                    logger.debug("Setting crystal spacegroup to: " + propertyValue);
                    current.setSpaceGroup(propertyValue);
                } else if (propertyType.equals("z")) {
                    try {
                        logger.debug("Setting z to: " + propertyValue);
                        current.setZ(Integer.parseInt(propertyValue));
                    } catch (NumberFormatException exception) {
                        logger.error("Error in format of Z value");
                    }
                }
            } else {
                logger.warn("Cannot add crystal cell parameters to a non " + "Crystal class!");
            }
        } else if (objectType.equals("a-axis") || objectType.equals("b-axis") || objectType.equals("c-axis")) {
            if (currentMolecule instanceof Crystal) {
                logger.debug("Setting axis (" + objectType + "): " + propertyValue);
                if (propertyType.equals("x")) {
                    crystal_axis_x = Double.parseDouble(propertyValue);
                } else if (propertyType.equals("y")) {
                    crystal_axis_y = Double.parseDouble(propertyValue);
                } else if (propertyType.equals("z")) {
                    crystal_axis_z = Double.parseDouble(propertyValue);
                }
            } else {
                logger.warn("Cannot add crystal cell parameters to a non " + "Crystal class!");
            }
        }
        logger.debug("Object property set...");
    }

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public CDOAcceptedObjects acceptObjects() {
        CDOAcceptedObjects objects = new CDOAcceptedObjects();
        objects.add("Molecule");
        objects.add("Fragment");
        objects.add("Atom");
        objects.add("Bond");
        objects.add("Animation");
        objects.add("Frame");
        objects.add("Crystal");
        objects.add("a-axis");
        objects.add("b-axis");
        objects.add("c-axis");
        objects.add("SetOfReactions");
        objects.add("Reactions");
        objects.add("Reactant");
        objects.add("Product");
        return objects;
    }

    public void addChemSequence(org.openscience.cdk.interfaces.ChemSequence chemSequence) {
        currentChemFile.addChemSequence(chemSequence);
    }

    public org.openscience.cdk.interfaces.ChemSequence[] getChemSequences() {
        return currentChemFile.getChemSequences();
    }

    public org.openscience.cdk.interfaces.ChemSequence getChemSequence(int number) {
        return currentChemFile.getChemSequence(number);
    }

    public int getChemSequenceCount() {
        return currentChemFile.getChemSequenceCount();
    }

    public void addListener(ChemObjectListener col) {
        currentChemFile.addListener(col);
    }

    public int getListenerCount() {
        return currentChemFile.getListenerCount();
    }

    public void removeListener(ChemObjectListener col) {
        currentChemFile.removeListener(col);
    }

    public void notifyChanged() {
        currentChemFile.notifyChanged();
    }

    public void notifyChanged(ChemObjectChangeEvent evt) {
        currentChemFile.notifyChanged(evt);
    }

    public void setProperty(Object description, Object property) {
        currentChemFile.setProperty(description, property);
    }

    public void removeProperty(Object description) {
        currentChemFile.removeProperty(description);
    }

    public Object getProperty(Object description) {
        return currentChemFile.getProperty(description);
    }

    public Hashtable getProperties() {
        return currentChemFile.getProperties();
    }

    public String getID() {
        return currentChemFile.getID();
    }

    public void setID(String identifier) {
        currentChemFile.setID(identifier);
    }

    public void setFlag(int flag_type, boolean flag_value) {
        currentChemFile.setFlag(flag_type, flag_value);
    }

    public boolean getFlag(int flag_type) {
        return currentChemFile.getFlag(flag_type);
    }

    public void setProperties(Hashtable properties) {
        currentChemFile.setProperties(properties);
    }

    public void setFlags(boolean[] flagsNew) {
        currentChemFile.setFlags(flagsNew);
    }

    public boolean[] getFlags() {
        return currentChemFile.getFlags();
    }

    public Object clone() {
        return currentChemFile.clone();
    }

    public ChemObjectBuilder getBuilder() {
        return currentChemFile.getBuilder();
    }

    /**
	 * return the aromaticValue value
	 * 
	 * @return aromaticValue 
	 */
    public double getAromaticOrder() {
        return aromaticOrder;
    }

    /**
	 * Sets the aromaticOrder value. This is the bond order aromatic bonds will get, default 1.5
	 * 
	 * @param aromaticOrder
	 */
    public void setAromaticOrder(double aromaticOrder) {
        this.aromaticOrder = aromaticOrder;
    }
}
