package org.filedoc.formatreader;

import org.filedoc.interpreter.DataFormat;

/**
 * Represents a Block of data in a Binary File. It contains
 * other DataXxx objects that contain the logic and data of the block.
 * 
 * @author Ed Webb
 * @version 1.0 5-May-2004
 * @since 1.0
 */
public class DataBlockDef extends DataCollection {

    /**
     * The file this block definition has been included from
     */
    private String include = "";

    /**
     * Returns the name of the file this block definition has been included from
     *  
     * @return the name of the file this block definition has been included from
     */
    public String getInclude() {
        return include;
    }

    /**
     * Sets the name of the file this block definition has been included from
     * 
     * @param include the name of the block this item definition has been included from
     */
    public void setInclude(String include) {
        this.include = include;
    }

    public boolean process(DataFormat format) throws FormatProcessException {
        return super.process(format);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Data Block Definition [");
        sb.append(super.toString());
        if (this.include != "") {
            sb.append(", Included=");
            sb.append(this.include);
        }
        sb.append("]");
        return sb.toString();
    }
}
