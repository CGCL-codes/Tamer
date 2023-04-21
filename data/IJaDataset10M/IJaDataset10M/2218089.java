package org.imogene.web.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

/**
 * Class that permits to handle binary references.
 * 
 * A binary ID could be composed by a token which 
 * include severals information about the referenced
 * binary file. This token is at least composed by the binary ID.
 * It can propose more information separating information by '/', 
 * but it should always start with binary ID.
 * 
 * Sample : Binary_ID/Binary_Name/Binary_Size
 * 
 * @author Medes-IMPS
 *
 */
public class BinaryTools {

    public static String TOKEN_SPERATOR = "/";

    public static String DOWNLOAD_TMPL = "<a href=\"" + GWT.getHostPageBaseURL() + "getbinary?binaryId=%PARAM_ID%\">" + "<img style=\"border:0;\" src=\"" + GWT.getModuleBaseURL() + "images/download_24.png\"></a>";

    public static String LINK_TMPL = GWT.getHostPageBaseURL() + "getbinary?binaryId=";

    public static String LINK_THUMB = GWT.getHostPageBaseURL() + "getbinary?thumbId=";

    /**
	 * Create a binary description object 
	 * from the binary token.
	 * @param token the binary token
	 * @return the binary description object
	 */
    public static BinaryDesc createBinaryDesc(String token) {
        String[] elmts = token.split(TOKEN_SPERATOR);
        if (elmts.length < 1) throw new RuntimeException("invalid binary token"); else {
            BinaryDesc desc = new BinaryDesc();
            desc.setId(elmts[0]);
            if (elmts.length > 1) desc.setName(elmts[1]);
            if (elmts.length > 2) desc.setSize(Integer.parseInt(elmts[2]));
            return desc;
        }
    }

    /**
	 * Create a string representation of 
	 * the binary file size
	 * @param size binary size in bytes
	 * @return string representation of the size
	 */
    public static String getSizeAsString(int size) {
        NumberFormat nf = NumberFormat.getFormat("0.00");
        StringBuffer sizeBuffer = new StringBuffer();
        if (size < 1024) sizeBuffer.append(size + " octets");
        if (size > 1024) {
            float ko = size / 1024.0f;
            if (ko > 1024) {
                float mo = ko / 1024.0f;
                sizeBuffer.append(nf.format(mo) + " Mo");
            } else {
                sizeBuffer.append(nf.format(ko) + " Ko");
            }
        }
        return sizeBuffer.toString();
    }
}
