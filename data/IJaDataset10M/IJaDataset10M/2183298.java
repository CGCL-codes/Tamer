package org.biomage.BioAssay;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;

/**
 *  The archetypal bioAssayCreation event, whereby biomaterials are 
 *  hybridized to an array.
 *  
 */
public class Hybridization extends BioAssayCreation implements Serializable {

    /**
     *  Default constructor.
     *  
     */
    public Hybridization() {
        super();
    }

    public Hybridization(Attributes atts) {
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
        out.write("<Hybridization");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</Hybridization>");
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
    }

    public String getModelClassName() {
        return new String("Hybridization");
    }
}
