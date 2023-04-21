package org.micthemodel.micWizard.elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.micthemodel.elements.Material;
import org.micthemodel.elements.ModelGrain;
import org.micthemodel.elements.Reactor;
import org.micthemodel.factory.Parameters;

/**
 *
 * @author sbishnoi
 */
public class Powder {

    private String powderFileName;

    private File powderFile;

    private String name;

    private ArrayList<PowderPhase> phases;

    private HashMap<PowderPhase, Material> phaseNewMaterialMap;

    private HashMap<PowderPhase, Material> phaseLibraryMaterialMap;

    private HashMap<Material, PowderPhase> libraryMaterialPhaseMap;

    private HashMap<Material, Material> libraryMaterialToNewMaterialMap;

    private String psdFileName;

    private type powderType;

    private double fraction;

    private ModelGrain libraryModelGrain;

    private ModelGrain newModelGrain;

    /**
     * @return the powderFileName
     */
    public String getPowderFileName() {
        return powderFileName;
    }

    /**
     * @param powderFileName the powderFileName to set
     */
    public void setPowderFileName(String powderFileName) {
        this.powderFileName = powderFileName;
    }

    public enum type {

        main, replacement, addition
    }

    ;

    public Powder() {
        this.phases = new ArrayList<PowderPhase>();
        this.phaseNewMaterialMap = new HashMap<PowderPhase, Material>();
        this.libraryMaterialPhaseMap = new HashMap<Material, PowderPhase>();
        this.libraryMaterialToNewMaterialMap = new HashMap<Material, Material>();
        this.phaseLibraryMaterialMap = new HashMap<PowderPhase, Material>();
    }

    protected boolean createInitialMaterials(Reactor libraryReactor, Reactor newReactor) {
        Iterator<PowderPhase> onPhases = this.phases.iterator();
        while (onPhases.hasNext()) {
            PowderPhase phase = onPhases.next();
            if (!libraryReactor.materialExists(phase.getName())) {
                Parameters.getOut().println("Cannot find material " + phase.getName() + " in the library...");
                return false;
            }
            Material libraryMaterial = libraryReactor.getMaterial(phase.getName());
            if (libraryMaterial.getInitialFraction() <= 0.0) {
                Parameters.getOut().println("The material " + phase.getName() + " is not defined as a reactant and cannot be added...");
                return false;
            }
            if (this.libraryModelGrain == null && !libraryMaterial.isUniversal()) {
                this.libraryModelGrain = libraryMaterial.getModelGrain();
            }
            if (!libraryMaterial.isUniversal() && this.libraryModelGrain != libraryMaterial.getModelGrain()) {
                Parameters.getOut().println("The material " + libraryMaterial + " is not defined in the same " + "powder in the library file.");
                return false;
            }
            this.phaseLibraryMaterialMap.put(phase, libraryMaterial);
            this.libraryMaterialPhaseMap.put(libraryMaterial, phase);
            System.out.println("put " + phase.getName() + " " + this.getName() + " and " + libraryMaterial);
            Material newMaterial;
            if (phase.getDensity() <= 0.0) {
                phase.setDensity(libraryMaterial.getDensity());
            }
            if (libraryMaterial.isUniversal()) {
                if (newReactor.materialExists(phase.getName())) {
                    newMaterial = newReactor.getMaterial(phase.getName());
                    if (this.powderType == type.main) {
                        newMaterial.setDensity(phase.getDensity());
                    }
                } else {
                    newMaterial = new Material(newReactor);
                    newReactor.setName(newMaterial, phase.getName());
                    newMaterial.setDensity(phase.getDensity());
                }
            } else {
                newMaterial = new Material(newReactor);
                newReactor.setName(newMaterial, this.getName() + "." + libraryMaterial.getName());
                newMaterial.setDensity(phase.getDensity());
            }
            this.phaseNewMaterialMap.put(phase, newMaterial);
            this.libraryMaterialToNewMaterialMap.put(libraryMaterial, newMaterial);
            newMaterial.setMaterialClass(libraryMaterial.getMaterialClass());
            newMaterial.setColor(libraryMaterial.getColor(), newReactor);
            newMaterial.setDiscrete(libraryMaterial.isDiscrete());
            newMaterial.setUniversal(libraryMaterial.isUniversal());
            newMaterial.setInner(libraryMaterial.isInner());
            newMaterial.setUserDefinedParameter(libraryMaterial.getUserDefinedParameter());
            newMaterial.setHasVariant(libraryMaterial.hasVariant());
        }
        return true;
    }

    public void add(PowderPhase phase) {
        this.phases.add(phase);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the powderType
     */
    public type getPowderType() {
        return powderType;
    }

    /**
     * @param powderType the powderType to set
     */
    public void setPowderType(type powderType) {
        this.powderType = powderType;
    }

    /**
     * @return the fraction
     */
    public double getFraction() {
        return fraction;
    }

    /**
     * @param fraction the fraction to set
     */
    public void setFraction(double fraction) {
        this.fraction = fraction;
    }

    /**
     * @return the phases
     */
    public ArrayList<PowderPhase> getPhases() {
        return phases;
    }

    public Material getNewMaterial(PowderPhase phase) {
        return this.phaseNewMaterialMap.get(phase);
    }

    public Material getNewMaterial(Material libraryMaterial) {
        return this.libraryMaterialToNewMaterialMap.get(libraryMaterial);
    }

    public Material getLibraryMaterial(PowderPhase phase) {
        return this.phaseLibraryMaterialMap.get(phase);
    }

    public PowderPhase getPhaseForLibraryMaterial(Material material) {
        return this.libraryMaterialPhaseMap.get(material);
    }

    public void add(Material libraryMaterial, Material newMaterial) {
        PowderPhase phase = new PowderPhase();
        phase.setName(libraryMaterial.getName());
        this.libraryMaterialPhaseMap.put(libraryMaterial, phase);
        this.libraryMaterialToNewMaterialMap.put(libraryMaterial, newMaterial);
        this.phaseLibraryMaterialMap.put(phase, libraryMaterial);
        this.phaseNewMaterialMap.put(phase, newMaterial);
        phase.setDensity(libraryMaterial.getDensity());
        phase.setMassFraction(0.0);
    }

    /**
     * @return the psdFile
     */
    public String getPsdFileName() {
        return psdFileName;
    }

    /**
     * @param psdFile the psdFile to set
     */
    public void setPsdFileName(String psdFileName) {
        this.psdFileName = psdFileName;
    }

    /**
     * @return the newModelGrain
     */
    public ModelGrain getNewModelGrain() {
        return newModelGrain;
    }

    /**
     * @param newModelGrain the newModelGrain to set
     */
    public void setNewModelGrain(ModelGrain newModelGrain) {
        this.newModelGrain = newModelGrain;
    }

    /**
     * @return the libraryModelGrain
     */
    public ModelGrain getLibraryModelGrain() {
        return libraryModelGrain;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * @return the powderFile
     */
    public File getPowderFile() {
        return powderFile;
    }

    /**
     * @param powderFile the powderFile to set
     */
    public void setPowderFile(File powderFile) {
        this.powderFile = powderFile;
    }
}
