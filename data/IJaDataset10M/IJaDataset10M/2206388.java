package org.biomage.Array;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;
import org.biomage.Interface.HasArrayDesign;
import org.biomage.Interface.HasArrayGroup;
import org.biomage.Interface.HasInformation;
import org.biomage.Interface.HasArrayManufactureDeviations;
import org.biomage.ArrayDesign.ArrayDesign;
import org.biomage.Common.Identifiable;

/**
 *  The physical substrate along with its features and their annotation
 *  
 */
public class Array extends Identifiable implements Serializable, HasArrayDesign, HasArrayGroup, HasInformation, HasArrayManufactureDeviations {

    /**
     *  An identifying string, e.g. a barcode.
     *  
     */
    String arrayIdentifier;

    /**
     *  This can indicate the x position on a slide, chip, etc. of the 
     *  first Feature and is usually specified relative to the fiducial.
     *  
     */
    float arrayXOrigin;

    /**
     *  This can indicate the y position on a slide, chip, etc. of the 
     *  first Feature and is usually specified relative to the fiducial.
     *  
     */
    float arrayYOrigin;

    /**
     *  What the array origin is relative to, e.g. upper left corner, 
     *  fiducial, etc.
     *  
     */
    String originRelativeTo;

    /**
     *  The association of a physical array with its array design.
     *  
     */
    protected ArrayDesign arrayDesign;

    /**
     *  Association between the manufactured array and the information on 
     *  that manufacture.
     *  
     */
    protected ArrayManufacture information;

    /**
     *  Association between an ArrayGroup and its Arrays, typically the 
     *  ArrayGroup will represent a slide and the Arrays will be the 
     *  manufactured so that they may be hybridized separately on that 
     *  slide.
     *  
     */
    protected ArrayGroup arrayGroup;

    /**
     *  Association to classes to describe deviations from the 
     *  ArrayDesign.
     *  
     */
    protected ArrayManufactureDeviations_list arrayManufactureDeviations = new ArrayManufactureDeviations_list();

    /**
     *  Default constructor.
     *  
     */
    public Array() {
        super();
    }

    public Array(Attributes atts) {
        super(atts);
        {
            int nIndex = atts.getIndex("", "arrayIdentifier");
            if (nIndex != -1) {
                arrayIdentifier = atts.getValue(nIndex);
            }
        }
        {
            int nIndex = atts.getIndex("", "arrayXOrigin");
            if (nIndex != -1) {
                arrayXOrigin = Float.parseFloat(atts.getValue(nIndex));
            }
        }
        {
            int nIndex = atts.getIndex("", "arrayYOrigin");
            if (nIndex != -1) {
                arrayYOrigin = Float.parseFloat(atts.getValue(nIndex));
            }
        }
        {
            int nIndex = atts.getIndex("", "originRelativeTo");
            if (nIndex != -1) {
                originRelativeTo = atts.getValue(nIndex);
            }
        }
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
        out.write("<Array");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</Array>");
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
        if (arrayIdentifier != null) {
            out.write(" arrayIdentifier=\"" + arrayIdentifier + "\"");
        }
        out.write(" arrayXOrigin=\"" + arrayXOrigin + "\"");
        out.write(" arrayYOrigin=\"" + arrayYOrigin + "\"");
        if (originRelativeTo != null) {
            out.write(" originRelativeTo=\"" + originRelativeTo + "\"");
        }
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
        if (arrayDesign != null) {
            out.write("<ArrayDesign_assnref>");
            out.write("<" + arrayDesign.getModelClassName() + "_ref identifier=\"" + arrayDesign.getIdentifier() + "\"/>");
            out.write("</ArrayDesign_assnref>");
        }
        if (information != null) {
            out.write("<Information_assnref>");
            out.write("<" + information.getModelClassName() + "_ref identifier=\"" + information.getIdentifier() + "\"/>");
            out.write("</Information_assnref>");
        }
        if (arrayGroup != null) {
            out.write("<ArrayGroup_assnref>");
            out.write("<" + arrayGroup.getModelClassName() + "_ref identifier=\"" + arrayGroup.getIdentifier() + "\"/>");
            out.write("</ArrayGroup_assnref>");
        }
        if (arrayManufactureDeviations.size() > 0) {
            out.write("<ArrayManufactureDeviations_assnlist>");
            for (int i = 0; i < arrayManufactureDeviations.size(); i++) {
                ((ArrayManufactureDeviation) arrayManufactureDeviations.elementAt(i)).writeMAGEML(out);
            }
            out.write("</ArrayManufactureDeviations_assnlist>");
        }
    }

    /**
     *  Set method for arrayIdentifier
     *  
     *  @param value to set
     *  
     *  
     */
    public void setArrayIdentifier(String arrayIdentifier) {
        this.arrayIdentifier = arrayIdentifier;
    }

    /**
     *  Get method for arrayIdentifier
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public String getArrayIdentifier() {
        return arrayIdentifier;
    }

    /**
     *  Set method for arrayXOrigin
     *  
     *  @param value to set
     *  
     *  
     */
    public void setArrayXOrigin(float arrayXOrigin) {
        this.arrayXOrigin = arrayXOrigin;
    }

    /**
     *  Get method for arrayXOrigin
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public float getArrayXOrigin() {
        return arrayXOrigin;
    }

    /**
     *  Set method for arrayYOrigin
     *  
     *  @param value to set
     *  
     *  
     */
    public void setArrayYOrigin(float arrayYOrigin) {
        this.arrayYOrigin = arrayYOrigin;
    }

    /**
     *  Get method for arrayYOrigin
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public float getArrayYOrigin() {
        return arrayYOrigin;
    }

    /**
     *  Set method for originRelativeTo
     *  
     *  @param value to set
     *  
     *  
     */
    public void setOriginRelativeTo(String originRelativeTo) {
        this.originRelativeTo = originRelativeTo;
    }

    /**
     *  Get method for originRelativeTo
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public String getOriginRelativeTo() {
        return originRelativeTo;
    }

    public String getModelClassName() {
        return new String("Array");
    }

    /**
     *  Set method for arrayDesign
     *  
     *  @param value to set
     *  
     *  
     */
    public void setArrayDesign(ArrayDesign arrayDesign) {
        this.arrayDesign = arrayDesign;
    }

    /**
     *  Get method for arrayDesign
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public ArrayDesign getArrayDesign() {
        return arrayDesign;
    }

    /**
     *  Set method for information
     *  
     *  @param value to set
     *  
     *  
     */
    public void setInformation(ArrayManufacture information) {
        this.information = information;
    }

    /**
     *  Get method for information
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public ArrayManufacture getInformation() {
        return information;
    }

    /**
     *  Set method for arrayGroup
     *  
     *  @param value to set
     *  
     *  
     */
    public void setArrayGroup(ArrayGroup arrayGroup) {
        this.arrayGroup = arrayGroup;
    }

    /**
     *  Get method for arrayGroup
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public ArrayGroup getArrayGroup() {
        return arrayGroup;
    }

    /**
     *  Set method for arrayManufactureDeviations
     *  
     *  @param value to set
     *  
     *  
     */
    public void setArrayManufactureDeviations(ArrayManufactureDeviations_list arrayManufactureDeviations) {
        this.arrayManufactureDeviations = arrayManufactureDeviations;
    }

    /**
     *  Get method for arrayManufactureDeviations
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public ArrayManufactureDeviations_list getArrayManufactureDeviations() {
        return arrayManufactureDeviations;
    }

    /**
     *  Method to add ArrayManufactureDeviation to 
     *  ArrayManufactureDeviations_list
     *  
     */
    public void addToArrayManufactureDeviations(ArrayManufactureDeviation arrayManufactureDeviation) {
        this.arrayManufactureDeviations.add(arrayManufactureDeviation);
    }

    /**
     *  Method to add ArrayManufactureDeviation at position to 
     *  ArrayManufactureDeviations_list
     *  
     */
    public void addToArrayManufactureDeviations(int position, ArrayManufactureDeviation arrayManufactureDeviation) {
        this.arrayManufactureDeviations.add(position, arrayManufactureDeviation);
    }

    /**
     *  Method to get ArrayManufactureDeviation from 
     *  ArrayManufactureDeviations_list
     *  
     */
    public ArrayManufactureDeviation getFromArrayManufactureDeviations(int position) {
        return (ArrayManufactureDeviation) this.arrayManufactureDeviations.get(position);
    }

    /**
     *  Method to remove by position from ArrayManufactureDeviations_list
     *  
     */
    public void removeElementAtFromArrayManufactureDeviations(int position) {
        this.arrayManufactureDeviations.removeElementAt(position);
    }

    /**
     *  Method to remove first ArrayManufactureDeviation from 
     *  ArrayManufactureDeviations_list
     *  
     */
    public void removeFromArrayManufactureDeviations(ArrayManufactureDeviation arrayManufactureDeviation) {
        this.arrayManufactureDeviations.remove(arrayManufactureDeviation);
    }
}
