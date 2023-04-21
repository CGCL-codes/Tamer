package org.jivesoftware.smackx.workgroup.ext.macros;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

/**
 * Macros iq is responsible for handling global and personal macros in the a Live Assistant
 * Workgroup.
 */
public class Macros extends IQ {

    private MacroGroup rootGroup;

    private boolean personal;

    private MacroGroup personalMacroGroup;

    private static ClassLoader cl;

    public static void setClassLoader(ClassLoader cloader) {
        cl = cloader;
    }

    public MacroGroup getRootGroup() {
        return rootGroup;
    }

    public void setRootGroup(MacroGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public boolean isPersonal() {
        return personal;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public MacroGroup getPersonalMacroGroup() {
        return personalMacroGroup;
    }

    public void setPersonalMacroGroup(MacroGroup personalMacroGroup) {
        this.personalMacroGroup = personalMacroGroup;
    }

    /**
     * Element name of the packet extension.
     */
    public static final String ELEMENT_NAME = "macros";

    /**
     * Namespace of the packet extension.
     */
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        if (isPersonal()) {
            buf.append("<personal>true</personal>");
        }
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }

    /**
     * An IQProvider for Macro packets.
     *
     * @author Derek DeMoro
     */
    public static class InternalProvider implements IQProvider {

        public InternalProvider() {
            super();
        }

        public IQ parseIQ(XmlPullParser parser) throws Exception {
            Macros macroGroup = new Macros();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("model")) {
                        String macros = parser.nextText();
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals(ELEMENT_NAME)) {
                        done = true;
                    }
                }
            }
            return macroGroup;
        }
    }
}
