package org.biomage.BioAssay;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;
import org.biomage.Interface.HasPhysicalBioAssay;
import org.biomage.Interface.HasTarget;
import org.biomage.BioEvent.BioEvent;

/**
 *  The event which records the process by which PhysicalBioAssays are 
 *  processed (typically washing, blocking, etc...).
 *  
 */
public class BioAssayTreatment extends BioEvent implements Serializable, HasPhysicalBioAssay, HasTarget {

    /**
     *  The set of treatments undergone by this PhysicalBioAssay.
     *  
     */
    protected PhysicalBioAssay physicalBioAssay;

    /**
     *  The PhysicalBioAssay that was treated.
     *  
     */
    protected PhysicalBioAssay target;

    /**
     *  Default constructor.
     *  
     */
    public BioAssayTreatment() {
        super();
    }

    public BioAssayTreatment(Attributes atts) {
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
        out.write("<BioAssayTreatment");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</BioAssayTreatment>");
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
        if (physicalBioAssay != null) {
            out.write("<PhysicalBioAssay_assnref>");
            out.write("<" + physicalBioAssay.getModelClassName() + "_ref identifier=\"" + physicalBioAssay.getIdentifier() + "\"/>");
            out.write("</PhysicalBioAssay_assnref>");
        }
        if (target != null) {
            out.write("<Target_assnref>");
            out.write("<" + target.getModelClassName() + "_ref identifier=\"" + target.getIdentifier() + "\"/>");
            out.write("</Target_assnref>");
        }
    }

    public String getModelClassName() {
        return new String("BioAssayTreatment");
    }

    /**
     *  Set method for physicalBioAssay
     *  
     *  @param value to set
     *  
     *  
     */
    public void setPhysicalBioAssay(PhysicalBioAssay physicalBioAssay) {
        this.physicalBioAssay = physicalBioAssay;
    }

    /**
     *  Get method for physicalBioAssay
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public PhysicalBioAssay getPhysicalBioAssay() {
        return physicalBioAssay;
    }

    /**
     *  Set method for target
     *  
     *  @param value to set
     *  
     *  
     */
    public void setTarget(PhysicalBioAssay target) {
        this.target = target;
    }

    /**
     *  Get method for target
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public PhysicalBioAssay getTarget() {
        return target;
    }
}
