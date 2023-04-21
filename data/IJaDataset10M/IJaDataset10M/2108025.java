package org.biomage.BQS;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;

/**
 *  Allows a reference to an article, book or other publication to be 
 *  specified for searching repositories.
 *  
 */
public class BQS_package implements Serializable {

    /**
     *  Default constructor.
     *  
     */
    public BQS_package() {
    }

    public BQS_package(Attributes atts) {
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
        out.write("<BQS_package");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</BQS_package>");
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
    }

    public String getModelClassName() {
        return new String("BQS_package");
    }
}
