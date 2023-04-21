package hambo.app.emulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Element;
import hambo.app.base.VirtualPortalPage;
import hambo.config.Config;
import hambo.config.ConfigManager;

/**
 * Page component which displays a WAP emulator. For use in all pages that needs
 * an embedded emulator.
 *
 */
public class EmbeddedEmulator extends VirtualPortalPage {

    private String skinId;

    private String url;

    private String skinWidth;

    private String skinHeight;

    private String skinCodebase;

    private String skinArchive;

    private String skinCode;

    private Map skins = null;

    /**
     * Creates an EmbeddedEmulator with the given skin id.
     *
     * @param skinId  the id of the skin
     * @param url     the URL to visit with the emulator
     */
    public EmbeddedEmulator(String skinId, String url) {
        super("em", "EmbeddedEmulator");
        if (url == null) url = "";
        this.skinId = skinId;
        this.url = url;
        Config config = ConfigManager.getConfig("emulator");
        skinWidth = config.getProperty("skinwidth");
        skinHeight = config.getProperty("skinheight");
        skinCodebase = config.getProperty("skincodebase");
        skinArchive = config.getProperty("skinarchive");
        skinCode = config.getProperty("skincode");
        skins = new HashMap();
        String stringname = "parametername";
        String stringvalue = "parametervalue";
        String tmpname = "";
        String tmpvalue = "";
        boolean stop = false;
        for (int ii = 1; !stop; ii++) {
            tmpname = config.getProperty(stringname + ii);
            tmpvalue = config.getProperty(stringvalue + ii);
            if (tmpname != null && tmpvalue != null) {
                skins.put(tmpname, tmpvalue);
            } else {
                stop = true;
            }
        }
    }

    /**
     * Creates an EmbeddedEmulator with the default skin id.
     *
     * @param url the URL to visit with the emulator
     */
    public EmbeddedEmulator(String url) {
        this("1", url);
    }

    public boolean processData() {
        EmulatorSkinDO edo = new EmulatorSkinDO();
        EmulatorSkin oSkin = (EmulatorSkin) edo.getSkinObject(skinId);
        if (oSkin != null) {
            skinWidth = oSkin.getSkinWidth();
            skinHeight = oSkin.getSkinHeight();
            skinCodebase = oSkin.getCodebase();
            skinArchive = oSkin.getArchive();
            skinCode = oSkin.getCode();
            skins = oSkin.getHashtableNameValue();
        }
        return true;
    }

    public boolean processTemplate() {
        Element appletElement = getElement("applet");
        if (appletElement != null) {
            appletElement.setAttribute("width", skinWidth);
            appletElement.setAttribute("height", skinHeight);
            appletElement.setAttribute("codebase", skinCodebase);
            appletElement.setAttribute("archive", skinArchive);
            appletElement.setAttribute("code", skinCode);
        }
        Element param_template = getElement("paramtemplate");
        if (param_template != null) {
            String theKey = "";
            String theValue = "";
            for (Iterator i = skins.keySet().iterator(); i.hasNext(); ) {
                theKey = ((String) i.next()).trim();
                theValue = ((String) skins.get(theKey)).trim();
                Element newparam = (Element) param_template.cloneNode(true);
                newparam.setAttribute("name", theKey);
                if (theKey.equals("wml-homepage") && !url.equals("")) {
                    newparam.setAttribute("value", url);
                } else {
                    newparam.setAttribute("value", theValue);
                }
                param_template.getParentNode().insertBefore(newparam, param_template);
            }
            param_template.getParentNode().removeChild(param_template);
        }
        return true;
    }
}
