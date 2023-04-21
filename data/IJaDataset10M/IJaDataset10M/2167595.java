package org.biomage.HigherLevelAnalysis;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;
import org.biomage.Interface.HasDesignElementDimension;
import org.biomage.Interface.HasQuantitationDimension;
import org.biomage.Interface.HasBioAssayDimension;
import org.biomage.BioAssayData.BioAssayDimension;
import org.biomage.BioAssayData.DesignElementDimension;
import org.biomage.BioAssayData.QuantitationTypeDimension;
import org.biomage.Common.Describable;

/**
 *  The contents of a node for any or all of the three Dimensions.  If a 
 *  node only contained genes just the DesignElementDimension would be 
 *  defined.
 *  
 */
public class NodeContents extends Describable implements Serializable, HasDesignElementDimension, HasQuantitationDimension, HasBioAssayDimension {

    /**
     *  The relevant BioAssays for this NodeContents from the 
     *  BioAssayData.
     *  
     */
    protected BioAssayDimension bioAssayDimension;

    /**
     *  The relevant DesignElements for this NodeContents from the 
     *  BioAssayData.
     *  
     */
    protected DesignElementDimension designElementDimension;

    /**
     *  The relevant QuantitationTypes for this NodeContents from the 
     *  BioAssayData.
     *  
     */
    protected QuantitationTypeDimension quantitationDimension;

    /**
     *  Default constructor.
     *  
     */
    public NodeContents() {
        super();
    }

    public NodeContents(Attributes atts) {
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
        out.write("<NodeContents");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</NodeContents>");
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
        if (bioAssayDimension != null) {
            out.write("<BioAssayDimension_assnref>");
            out.write("<" + bioAssayDimension.getModelClassName() + "_ref identifier=\"" + bioAssayDimension.getIdentifier() + "\"/>");
            out.write("</BioAssayDimension_assnref>");
        }
        if (designElementDimension != null) {
            out.write("<DesignElementDimension_assnref>");
            out.write("<" + designElementDimension.getModelClassName() + "_ref identifier=\"" + designElementDimension.getIdentifier() + "\"/>");
            out.write("</DesignElementDimension_assnref>");
        }
        if (quantitationDimension != null) {
            out.write("<QuantitationDimension_assnref>");
            out.write("<" + quantitationDimension.getModelClassName() + "_ref identifier=\"" + quantitationDimension.getIdentifier() + "\"/>");
            out.write("</QuantitationDimension_assnref>");
        }
    }

    public String getModelClassName() {
        return new String("NodeContents");
    }

    /**
     *  Set method for bioAssayDimension
     *  
     *  @param value to set
     *  
     *  
     */
    public void setBioAssayDimension(BioAssayDimension bioAssayDimension) {
        this.bioAssayDimension = bioAssayDimension;
    }

    /**
     *  Get method for bioAssayDimension
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public BioAssayDimension getBioAssayDimension() {
        return bioAssayDimension;
    }

    /**
     *  Set method for designElementDimension
     *  
     *  @param value to set
     *  
     *  
     */
    public void setDesignElementDimension(DesignElementDimension designElementDimension) {
        this.designElementDimension = designElementDimension;
    }

    /**
     *  Get method for designElementDimension
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public DesignElementDimension getDesignElementDimension() {
        return designElementDimension;
    }

    /**
     *  Set method for quantitationDimension
     *  
     *  @param value to set
     *  
     *  
     */
    public void setQuantitationDimension(QuantitationTypeDimension quantitationDimension) {
        this.quantitationDimension = quantitationDimension;
    }

    /**
     *  Get method for quantitationDimension
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public QuantitationTypeDimension getQuantitationDimension() {
        return quantitationDimension;
    }
}
