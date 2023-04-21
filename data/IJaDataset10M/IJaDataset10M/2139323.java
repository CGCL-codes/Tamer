package org.openXpertya.print.pdf.text.rtf.document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.openXpertya.print.pdf.text.rtf.RtfElement;

/**
 * The RtfInfoGroup stores information group elements. 
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfInfoGroup extends RtfElement {

    /**
     * Information group starting tag
     */
    private static final byte[] INFO_GROUP = "\\info".getBytes();

    /**
     * The RtfInfoElements that belong to this RtfInfoGroup
     */
    ArrayList infoElements = null;

    /**
     * Constructs a RtfInfoGroup belonging to a RtfDocument
     * 
     * @param doc The RtfDocument this RtfInfoGroup belongs to
     */
    public RtfInfoGroup(RtfDocument doc) {
        super(doc);
        infoElements = new ArrayList();
    }

    /**
     * Adds an RtfInfoElement to the RtfInfoGroup
     * 
     * @param infoElement The RtfInfoElement to add
     */
    public void add(RtfInfoElement infoElement) {
        this.infoElements.add(infoElement);
    }

    /**
     * Writes the RtfInfoGroup and its RtfInfoElement elements.
     * 
     * @return A byte array containing the group and its elements
     */
    public byte[] write() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(OPEN_GROUP);
            result.write(INFO_GROUP);
            for (int i = 0; i < infoElements.size(); i++) {
                RtfInfoElement infoElement = (RtfInfoElement) infoElements.get(i);
                result.write(infoElement.write());
            }
            result.write(CLOSE_GROUP);
            result.write((byte) '\n');
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
}
