package org.biomage.BioAssayData;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;

/**
 *  Transformed class to associate external data to the BioAssayDataCube
 *  
 */
public class DataExternal implements Serializable {

    /**
     *  The format of the external file, whitespace delimited, tab 
     *  delimited, netcdf, etc...
     *  
     */
    private String dataFormat;

    /**
     *  Location for documentation on the data format
     *  
     */
    private String dataFormatInfoURI;

    /**
     *  The name and location of the file containing the external data
     *  
     */
    private String filenameURI;

    /**
     *  Default constructor.
     *  
     */
    public DataExternal() {
    }

    public DataExternal(Attributes atts) {
        {
            int nIndex = atts.getIndex("", "dataFormat");
            if (nIndex != -1) {
                dataFormat = atts.getValue(nIndex);
            }
        }
        {
            int nIndex = atts.getIndex("", "dataFormatInfoURI");
            if (nIndex != -1) {
                dataFormatInfoURI = atts.getValue(nIndex);
            }
        }
        {
            int nIndex = atts.getIndex("", "filenameURI");
            if (nIndex != -1) {
                filenameURI = atts.getValue(nIndex);
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
        out.write("<DataExternal");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</DataExternal>");
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
        if (dataFormat != null) {
            out.write(" dataFormat=\"" + dataFormat + "\"");
        }
        if (dataFormatInfoURI != null) {
            out.write(" dataFormatInfoURI=\"" + dataFormatInfoURI + "\"");
        }
        if (filenameURI != null) {
            out.write(" filenameURI=\"" + filenameURI + "\"");
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
    }

    /**
     *  Set method for dataFormat
     *  
     *  @param value to set
     *  
     *  
     */
    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    /**
     *  Get method for dataFormat
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public String getDataFormat() {
        return dataFormat;
    }

    /**
     *  Set method for dataFormatInfoURI
     *  
     *  @param value to set
     *  
     *  
     */
    public void setDataFormatInfoURI(String dataFormatInfoURI) {
        this.dataFormatInfoURI = dataFormatInfoURI;
    }

    /**
     *  Get method for dataFormatInfoURI
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public String getDataFormatInfoURI() {
        return dataFormatInfoURI;
    }

    /**
     *  Set method for filenameURI
     *  
     *  @param value to set
     *  
     *  
     */
    public void setFilenameURI(String filenameURI) {
        this.filenameURI = filenameURI;
    }

    /**
     *  Get method for filenameURI
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public String getFilenameURI() {
        return filenameURI;
    }

    public String getModelClassName() {
        return new String("DataExternal");
    }
}
