package SK.gnome.capabilities;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Properties;
import SK.gnome.capabilities.Capability;
import SK.gnome.morena.MorenaImage;

public abstract class Capabilities implements ActionListener {

    protected ArrayList<Capability> allCapabilities;

    protected Properties properties;

    protected String sourceName;

    protected String sourceValidFileName;

    protected boolean restartAfterPreview;

    protected int sleepTimeBeforeRestart;

    protected Component centerPanel;

    protected Frame frame;

    protected CapabilityDialog dialogWindow;

    public static boolean debug = Boolean.valueOf(System.getProperty("morena.capabilities.debug", "false"));

    public abstract MorenaImage getPreviewImage() throws Exception;

    public abstract String getSourceName();

    public ArrayList<Capability> getCapabilities() {
        return allCapabilities;
    }

    public String getSourceValidFileName() {
        return sourceValidFileName;
    }

    public Capability getCapability(String name) {
        String temp;
        for (Capability capability : allCapabilities) {
            temp = capability.getName();
            if ((null != temp) && (temp.equals(name))) return capability;
        }
        return null;
    }

    public boolean isRestartAfterPreview() {
        return restartAfterPreview;
    }

    public void setRestartAfterPreview(boolean restartAfterPreview) {
        this.restartAfterPreview = restartAfterPreview;
    }

    public int getSleepTimeBeforeRestart() {
        return sleepTimeBeforeRestart;
    }

    public void setSleepTimeBeforeRestart(int sleepTimeAfterRestart) {
        this.sleepTimeBeforeRestart = sleepTimeAfterRestart;
    }

    public String makeFileNameValid(String string) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            Character ch = string.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) s.append(ch);
        }
        return s.toString();
    }
}
