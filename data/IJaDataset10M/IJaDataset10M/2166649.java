package org.biomage.Experiment;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;
import org.biomage.Interface.HasTopLevelBioAssays;
import org.biomage.Interface.HasExperimentalFactors;
import org.biomage.Interface.HasReplicateDescription;
import org.biomage.Interface.HasQualityControlDescription;
import org.biomage.Interface.HasNormalizationDescription;
import org.biomage.Interface.HasTypes;
import org.biomage.BioAssay.BioAssay;
import org.biomage.Common.Describable;
import org.biomage.Description.Description;
import org.biomage.Description.OntologyEntry;

/**
 *  The ExperimentDesign is the description and collection of 
 *  ExperimentalFactors and the hierarchy of BioAssays to which they 
 *  pertain.
 *  
 */
public class ExperimentDesign extends Describable implements Serializable, HasTopLevelBioAssays, HasExperimentalFactors, HasReplicateDescription, HasQualityControlDescription, HasNormalizationDescription, HasTypes {

    /**
     *  Classification of an experiment.  For example 'normal vs. 
     *  diseased', 'treated vs. untreated', 'time course', 'tiling', etc.
     *  
     */
    protected Types_list types = new Types_list();

    /**
     *  The organization of the BioAssays as specified by the 
     *  ExperimentDesign (TimeCourse, Dosage, etc.)
     *  
     */
    protected TopLevelBioAssays_list topLevelBioAssays = new TopLevelBioAssays_list();

    /**
     *  The description of the factors (TimeCourse, Dosage, etc.) that 
     *  group the BioAssays.
     *  
     */
    protected ExperimentalFactors_list experimentalFactors = new ExperimentalFactors_list();

    /**
     *  Description of the quality control aspects of the Experiment.
     *  
     */
    protected Description qualityControlDescription;

    /**
     *  Description of the normalization strategy of the Experiment.
     *  
     */
    protected Description normalizationDescription;

    /**
     *  Description of the replicate strategy of the Experiment.
     *  
     */
    protected Description replicateDescription;

    /**
     *  Default constructor.
     *  
     */
    public ExperimentDesign() {
        super();
    }

    public ExperimentDesign(Attributes atts) {
        super(atts);
    }

    /**
     *  writeMAGEML
     *  
     *  This method is responsible for assembling the attribute and 
     *  association data into XML. It creates the object tag and then calls 
     *  the writeAttributes and writeAssociation methods.
     *  
     *  
     */
    public void writeMAGEML(Writer out) throws IOException {
        out.write("<ExperimentDesign");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</ExperimentDesign>");
    }

    /**
     *  writeAttributes
     *  
     *  This method is responsible for assembling the attribute data into 
     *  XML. It calls the super method to write out all attributes of this 
     *  class and it's ancestors.
     *  
     *  
     */
    public void writeAttributes(Writer out) throws IOException {
        super.writeAttributes(out);
    }

    /**
     *  writeAssociations
     *  
     *  This method is responsible for assembling the association data 
     *  into XML. It calls the super method to write out all associations of 
     *  this class's ancestors.
     *  
     *  
     */
    public void writeAssociations(Writer out) throws IOException {
        super.writeAssociations(out);
        if (types.size() > 0) {
            out.write("<Types_assnlist>");
            for (int i = 0; i < types.size(); i++) {
                ((OntologyEntry) types.elementAt(i)).writeMAGEML(out);
            }
            out.write("</Types_assnlist>");
        }
        if (topLevelBioAssays.size() > 0) {
            out.write("<TopLevelBioAssays_assnreflist>");
            for (int i = 0; i < topLevelBioAssays.size(); i++) {
                String modelClassName = ((BioAssay) topLevelBioAssays.elementAt(i)).getModelClassName();
                out.write("<" + modelClassName + "_ref identifier=\"" + ((BioAssay) topLevelBioAssays.elementAt(i)).getIdentifier() + "\"/>");
            }
            out.write("</TopLevelBioAssays_assnreflist>");
        }
        if (experimentalFactors.size() > 0) {
            out.write("<ExperimentalFactors_assnlist>");
            for (int i = 0; i < experimentalFactors.size(); i++) {
                ((ExperimentalFactor) experimentalFactors.elementAt(i)).writeMAGEML(out);
            }
            out.write("</ExperimentalFactors_assnlist>");
        }
        if (qualityControlDescription != null) {
            out.write("<QualityControlDescription_assn>");
            qualityControlDescription.writeMAGEML(out);
            out.write("</QualityControlDescription_assn>");
        }
        if (normalizationDescription != null) {
            out.write("<NormalizationDescription_assn>");
            normalizationDescription.writeMAGEML(out);
            out.write("</NormalizationDescription_assn>");
        }
        if (replicateDescription != null) {
            out.write("<ReplicateDescription_assn>");
            replicateDescription.writeMAGEML(out);
            out.write("</ReplicateDescription_assn>");
        }
    }

    public String getModelClassName() {
        return new String("ExperimentDesign");
    }

    /**
     *  Set method for types
     *  
     *  @param value to set
     *  
     *  
     */
    public void setTypes(Types_list types) {
        this.types = types;
    }

    /**
     *  Get method for types
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public Types_list getTypes() {
        return types;
    }

    /**
     *  Method to add OntologyEntry to Types_list
     *  
     */
    public void addToTypes(OntologyEntry ontologyEntry) {
        this.types.add(ontologyEntry);
    }

    /**
     *  Method to add OntologyEntry at position to Types_list
     *  
     */
    public void addToTypes(int position, OntologyEntry ontologyEntry) {
        this.types.add(position, ontologyEntry);
    }

    /**
     *  Method to get OntologyEntry from Types_list
     *  
     */
    public OntologyEntry getFromTypes(int position) {
        return (OntologyEntry) this.types.get(position);
    }

    /**
     *  Method to remove by position from Types_list
     *  
     */
    public void removeElementAtFromTypes(int position) {
        this.types.removeElementAt(position);
    }

    /**
     *  Method to remove first OntologyEntry from Types_list
     *  
     */
    public void removeFromTypes(OntologyEntry ontologyEntry) {
        this.types.remove(ontologyEntry);
    }

    /**
     *  Set method for topLevelBioAssays
     *  
     *  @param value to set
     *  
     *  
     */
    public void setTopLevelBioAssays(TopLevelBioAssays_list topLevelBioAssays) {
        this.topLevelBioAssays = topLevelBioAssays;
    }

    /**
     *  Get method for topLevelBioAssays
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public TopLevelBioAssays_list getTopLevelBioAssays() {
        return topLevelBioAssays;
    }

    /**
     *  Method to add BioAssay to TopLevelBioAssays_list
     *  
     */
    public void addToTopLevelBioAssays(BioAssay bioAssay) {
        this.topLevelBioAssays.add(bioAssay);
    }

    /**
     *  Method to add BioAssay at position to TopLevelBioAssays_list
     *  
     */
    public void addToTopLevelBioAssays(int position, BioAssay bioAssay) {
        this.topLevelBioAssays.add(position, bioAssay);
    }

    /**
     *  Method to get BioAssay from TopLevelBioAssays_list
     *  
     */
    public BioAssay getFromTopLevelBioAssays(int position) {
        return (BioAssay) this.topLevelBioAssays.get(position);
    }

    /**
     *  Method to remove by position from TopLevelBioAssays_list
     *  
     */
    public void removeElementAtFromTopLevelBioAssays(int position) {
        this.topLevelBioAssays.removeElementAt(position);
    }

    /**
     *  Method to remove first BioAssay from TopLevelBioAssays_list
     *  
     */
    public void removeFromTopLevelBioAssays(BioAssay bioAssay) {
        this.topLevelBioAssays.remove(bioAssay);
    }

    /**
     *  Set method for experimentalFactors
     *  
     *  @param value to set
     *  
     *  
     */
    public void setExperimentalFactors(ExperimentalFactors_list experimentalFactors) {
        this.experimentalFactors = experimentalFactors;
    }

    /**
     *  Get method for experimentalFactors
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public ExperimentalFactors_list getExperimentalFactors() {
        return experimentalFactors;
    }

    /**
     *  Method to add ExperimentalFactor to ExperimentalFactors_list
     *  
     */
    public void addToExperimentalFactors(ExperimentalFactor experimentalFactor) {
        this.experimentalFactors.add(experimentalFactor);
    }

    /**
     *  Method to add ExperimentalFactor at position to 
     *  ExperimentalFactors_list
     *  
     */
    public void addToExperimentalFactors(int position, ExperimentalFactor experimentalFactor) {
        this.experimentalFactors.add(position, experimentalFactor);
    }

    /**
     *  Method to get ExperimentalFactor from ExperimentalFactors_list
     *  
     */
    public ExperimentalFactor getFromExperimentalFactors(int position) {
        return (ExperimentalFactor) this.experimentalFactors.get(position);
    }

    /**
     *  Method to remove by position from ExperimentalFactors_list
     *  
     */
    public void removeElementAtFromExperimentalFactors(int position) {
        this.experimentalFactors.removeElementAt(position);
    }

    /**
     *  Method to remove first ExperimentalFactor from 
     *  ExperimentalFactors_list
     *  
     */
    public void removeFromExperimentalFactors(ExperimentalFactor experimentalFactor) {
        this.experimentalFactors.remove(experimentalFactor);
    }

    /**
     *  Set method for qualityControlDescription
     *  
     *  @param value to set
     *  
     *  
     */
    public void setQualityControlDescription(Description qualityControlDescription) {
        this.qualityControlDescription = qualityControlDescription;
    }

    /**
     *  Get method for qualityControlDescription
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public Description getQualityControlDescription() {
        return qualityControlDescription;
    }

    /**
     *  Set method for normalizationDescription
     *  
     *  @param value to set
     *  
     *  
     */
    public void setNormalizationDescription(Description normalizationDescription) {
        this.normalizationDescription = normalizationDescription;
    }

    /**
     *  Get method for normalizationDescription
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public Description getNormalizationDescription() {
        return normalizationDescription;
    }

    /**
     *  Set method for replicateDescription
     *  
     *  @param value to set
     *  
     *  
     */
    public void setReplicateDescription(Description replicateDescription) {
        this.replicateDescription = replicateDescription;
    }

    /**
     *  Get method for replicateDescription
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public Description getReplicateDescription() {
        return replicateDescription;
    }
}
