package org.biomage.tools.generate_dtd;

import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import org.biomage.tools.generate_classes.CreateFile;
import org.biomage.tools.generate_classes.CreateMAGEFile;
import org.biomage.tools.generate_classes.XMIParseHelpers;
import org.biomage.tools.helpers.StringOutputHelpers;
import org.w3c.dom.*;

/**
 * <b>Description:</b>
 *      Class that is resposible for generating a DTD file
 *      for the classes represented by the list of class nodes
 *      passed into the constructor.
 *
 */
public class WriteDTDMageElement extends WriteDTDElement {

    /**
     * <b>Description:</b>
     *      For outputting the MAGE-OM element to the file.
     */
    StringBuffer mageML = null;

    /**
     * <b>Description:</b>
     *      Map that specifies the order of the packages.  Population of the map
     *      comes from a the parameter packageOrdering resource.
     */
    Map packageOrder = new TreeMap();

    /**
     * <b>Description:</b>
     *      Method to read the XML configuration for the ordering of the packages.
     *
     *<p>
     *  @param packageOrdering: the XML configuration element.
     *<p>
     */
    protected void setPackageOrder(Element packageOrdering) throws Exception {
        NodeList elements = packageOrdering.getElementsByTagName("package");
        for (int i = 0; i < elements.getLength(); i++) {
            packageOrder.put(((Element) elements.item(i)).getAttribute("name") + "_package", new Integer(i));
        }
    }

    /**
     * <b>Description:</b>
     *      Constructor for the DTD file generator.
     *
     *<p>
     *  @param createFile: the class to write to the DTD.
     *<p>
     */
    protected WriteDTDMageElement(CreateFile createFile) throws Exception {
        super(createFile);
    }

    /**
     * <b>Description:</b>
     *     Creates the element declaration for the top-level model.
     *<p>
     *  @param packageOrdering: the XML configuration element for package ordering information.
     *<p>
     */
    protected void createXMLStrings() throws Exception {
        Vector ordered = createFile.getAssociationInfo();
        mageML = new StringBuffer("<!ELEMENT MAGE-ML ((%Identifiable_content;)");
        for (int i = 0; i < ordered.size(); i++) {
            mageML.append("," + StringOutputHelpers.NEWLINE + "\t\t" + ordered.get(i) + "?");
        }
        mageML.append(") >" + StringOutputHelpers.NEWLINE);
        mageML.append("<!ATTLIST MAGE-ML %Identifiable_attrs; >" + StringOutputHelpers.NEWLINE + StringOutputHelpers.NEWLINE);
    }

    /**
     * <b>Description:</b>
     *      Method to write out the body.
     *
     *<p>
     *  @param write: the writer to use.
     *<p>
     */
    protected void writeBody(FileWriter writer) throws Exception {
        String comment = "MAGE-ML" + StringOutputHelpers.NEWLINE + StringOutputHelpers.NEWLINE + "The top-level element that contains the packages.  Each of the package " + "elements contain the lists of independent elements in that package.";
        StringOutputHelpers.writeDTDComment(writer, comment, null, true, true);
        writer.write(mageML.toString());
    }
}
