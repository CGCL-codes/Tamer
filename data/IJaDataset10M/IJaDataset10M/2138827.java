package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.SceneGraphStructureNodeType;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * DOCTYPE.java
 * Created on April 12, 2008
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class DOCTYPE extends SceneGraphStructureNodeType {

    private String version;

    public DOCTYPE() {
    }

    @Override
    public String getElementName() {
        return DOCTYPE_ELNAME;
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return DOCTYPECustomizer.class;
    }

    @Override
    public void initialize() {
        version = DOCTYPE_ATTR_VERSION_DFLT;
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr = root.getAttribute(DOCTYPE_ATTR_VERSION_NAME);
        if (attr != null) version = attr.getValue();
    }

    @Override
    public String createAttributes() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE X3D PUBLIC \"ISO//Web3D//DTD X3D ");
        sb.append(version);
        sb.append("//EN\" \"http://www.web3d.org/specifications/x3d-");
        sb.append(version);
        sb.append(".dtd\">");
        return sb.toString();
    }

    private String xmlHdrRegEx = "<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>";

    private String xmlHdr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private String dtdHdr = "<!DOCTYPE X3D ";

    private String dtdHdr1 = "PUBLIC ";

    private String dtdFtr = ">";

    private String dtdNameElement0 = "\"ISO//Web3D//DTD X3D ";

    private String dtdNameElement1 = "//EN\"";

    private String dtdLocationElement0 = "\"http://www.web3d.org/specifications/x3d-";

    private String dtdLocationElement1 = ".dtd\"";

    private String regexPattern = xmlHdrRegEx + "\\s*" + dtdHdr + ".*" + "\"" + ".*" + "\"" + "\\s*" + "\"" + ".*" + "\"" + "\\s*" + dtdFtr;

    /**
   * This gets called after a good insert when things have settled down.  Here we try to make sure the DTD spec is the same as our chosen version.
   * The schema reference gets changed above, so try for the xml and dtd headers here.
   * @param jTextComponent 
   */
    @Override
    public void postInsert(JTextComponent jTextComponent) {
        String completeText = jTextComponent.getText();
        String s = completeText;
        int size = completeText.length();
        if (size > 255) {
            s = s.substring(0, 256);
            size = 255;
        }
        String newHeader = xmlHdr + linesep + dtdHdr + dtdHdr1;
        version = getVersion();
        if (version.equals("3.0") || version.equals("3.1") || version.equals("3.2") || version.equals("3.3")) ; else {
            System.err.println("Bad X3D version='" + version + "' found by DOCTYPE.java: must be 3.0, 3.1, 3.2, or 3.3");
            return;
        }
        newHeader = newHeader + dtdNameElement0 + version + dtdNameElement1 + " " + dtdLocationElement0 + version + dtdLocationElement1 + dtdFtr;
        String[] sa = s.split(regexPattern);
        if (sa == null || sa.length < 2) {
            System.err.println("No DTD header match found by DOCTYPE.java");
            return;
        }
        jTextComponent.setText(completeText.replaceFirst(regexPattern, newHeader));
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String newVersion) {
        version = newVersion;
    }
}
