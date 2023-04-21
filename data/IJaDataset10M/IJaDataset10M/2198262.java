package org.filedoc.formatreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains information about a file format definition.
 * 
 * @author Ed Webb
 * @version 1.0 5-May-2004
 * @since 1.0
 */
public class FormatAttributes {

    /**
     * The name of the format 
     */
    private String formatName;

    /**
	 * the company, organization or individual who created the format
	 */
    private String originator;

    /**
	 * The date the specification was released
	 */
    private String specificationDate;

    /**
     * The initial block that the Interpreter should start from
     */
    private String initialBlock;

    /**
     * A description of this file format
     */
    private String description;

    /**
     * The byte order of the file format
     */
    private String byteOrder;

    /** 
	 * A list of common filename extensions associated with this format 
	 */
    private ArrayList extensions = new ArrayList();

    /**
     * Returns the name of this file format
     * 
     * @return the name of this file format
     */
    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getSpecificationDate() {
        return specificationDate;
    }

    public void setSpecificationDate(String specificationDate) {
        this.specificationDate = specificationDate;
    }

    public String getInitialBlock() {
        return initialBlock;
    }

    public void setInitialBlock(String initialBlock) {
        this.initialBlock = initialBlock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getByteOrder() {
        return byteOrder;
    }

    public void setByteOrder(String byteOrder) {
        this.byteOrder = byteOrder;
    }

    public String[] getExtensions() {
        String[] exts = new String[extensions.size()];
        exts = (String[]) extensions.toArray(exts);
        return exts;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = (ArrayList) Arrays.asList(extensions);
    }

    public void addExtension(String extension) {
        this.extensions.add(extension);
    }

    public int matchExtension(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (int i = 0; i < this.extensions.size(); i++) {
            String extension = (String) this.extensions.get(i);
            if (-1 == extension.indexOf("?") && -1 == extension.indexOf("*")) {
                if (ext.equals(extension)) {
                    return 0;
                }
            }
        }
        for (int i = 0; i < this.extensions.size(); i++) {
            String extension = (String) this.extensions.get(i);
            if ((0 <= extension.indexOf("?") || 0 <= extension.indexOf("*")) && !"*".equals(extension)) {
                extension = "^" + extension.replaceAll("\\?", ".{1}").replaceAll("\\*", ".*") + "$";
                Pattern pat = Pattern.compile(extension);
                Matcher match = pat.matcher(ext);
                if (match.matches()) {
                    return 1;
                }
            }
        }
        for (int i = 0; i < this.extensions.size(); i++) {
            String extension = (String) this.extensions.get(i);
            if ("*".equals(extension)) {
                return 2;
            }
        }
        return 3;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Format Attributes [");
        sb.append("Name=");
        sb.append(this.formatName);
        sb.append(", Originator=");
        sb.append(this.originator);
        sb.append(", Spec Date=");
        sb.append(this.specificationDate);
        sb.append(", Initial Block=");
        sb.append(this.initialBlock);
        sb.append(", Extensions=");
        sb.append(this.extensions.toString());
        sb.append("]");
        return sb.toString();
    }
}
