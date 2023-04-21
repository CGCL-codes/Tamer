package ca.uhn.hl7v2.testpanel.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 
 * @author James
 */
public class ImageFactory {

    private static Map<String, ImageIcon> ourIcons = new HashMap<String, ImageIcon>();

    public static ImageIcon getButtonExecute() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/button_execute.png");
    }

    public static ImageIcon getProfile() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/profile.png");
    }

    public static ImageIcon getProfileGroup() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/new_tree.png");
    }

    public static ImageIcon getFile() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/file.png");
    }

    public static ImageIcon getTable() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/table.png");
    }

    public static ImageIcon getNo() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/no.png");
    }

    private static ImageIcon getImageIcon(String theLocation) {
        ImageIcon retVal = ourIcons.get(theLocation);
        if (retVal == null) {
            URL resource = ImageFactory.class.getClassLoader().getResource(theLocation);
            if (resource == null) {
                throw new Error(theLocation);
            }
            retVal = new ImageIcon(resource);
            ourIcons.put(theLocation, retVal);
        }
        return retVal;
    }

    public static ImageIcon getInterfaceOff() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/interface_off.png");
    }

    public static ImageIcon getHapi64() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/hapi_64.png");
    }

    public static ImageIcon getInterfaceOn() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/interface_on.png");
    }

    public static Icon getInterfaceStarting() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/interface_starting.png");
    }

    public static ImageIcon getMessageHl7() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/message_hl7.png");
    }

    public static ImageIcon getMessageIn() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/message_in.png");
    }

    public static ImageIcon getMessageOut() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/message_out.png");
    }

    public static ImageIcon getMessageXml() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/message_xml.png");
    }

    public static ImageIcon getTabLog() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/tab_log.png");
    }

    public static ImageIcon getTest() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/test.png");
    }

    public static ImageIcon getTestFailed() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/test_failed.png");
    }

    public static ImageIcon getTestPassed() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/test_passed.png");
    }

    public static ImageIcon getTestRunning() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/test_running.png");
    }

    public static ImageIcon getTreeBundle() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/tree_bundle.png");
    }

    public static ImageIcon getTreeLeaf() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/tree_leaf.png");
    }

    public static ImageIcon getValFailed() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/val_failed.png");
    }

    public static Icon getValFailedChild() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/val_failed_child.png");
    }

    public static ImageIcon getValPassed() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/val_passed.png");
    }

    public static ImageIcon getValPassedGreen() {
        return getImageIcon("ca/uhn/hl7v2/testpanel/images/val_passed_green.png");
    }
}
