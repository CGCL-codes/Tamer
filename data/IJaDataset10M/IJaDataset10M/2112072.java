package testMIDlet1.cases;

import mobi.ilabs.common.MIDletContext;
import mobi.ilabs.common.Debug;
import mobi.ilabs.gui.TabbedCanvas;
import mobi.ilabs.common.ImageUtil;
import javax.microedition.lcdui.Image;

/**
* @author �ystein Myhre
*/
public class TabTestCanvas extends TabbedCanvas implements Debug.Constants {

    private static final Image contactsIcon = getIcon("/testMIDlet1/resources/tabTest/icons/Contacts.png");

    private static final Image bloggerIcon = getIcon("/testMIDlet1/resources/tabTest/icons/BlogView.png");

    private static final Image explorerIcon = getIcon("/testMIDlet1/resources/tabTest/icons/FolderExplorer.png");

    private static final Image cameraIcon = getIcon("/testMIDlet1/resources/tabTest/icons/Camera.png");

    private static final Image microfonIcon = getIcon("/testMIDlet1/resources/tabTest/icons/Microfon.png");

    private static final Image chatIcon = getIcon("/testMIDlet1/resources/tabTest/icons/Chat.png");

    private static final Image settingsIcon = getIcon("/testMIDlet1/resources/tabTest/icons/Settings.png");

    private static Image getIcon(String res) {
        Image image = null;
        try {
            image = Image.createImage(res);
        } catch (java.io.IOException ex) {
            if (DEBUG) Debug.EXCEPTION("TabTestCanvas.getIcon: " + res, ex);
            image = null;
        }
        if (image != null) image = ImageUtil.createScaledImage(image, 22, tabHeight - 8);
        return (image);
    }

    public TabTestCanvas() {
    }

    public void show() {
        this.addTab(new TestPanel(this, 0, contactsIcon, "Contacts"));
        this.addTab(new TestPanel(this, 1, chatIcon, "Chat"));
        this.addTab(new TestPanel(this, 2, bloggerIcon, "Mine Blogger"));
        this.addTab(new TestPanel(this, 3, cameraIcon, "Camera"));
        this.addTab(new TestPanel(this, 4, explorerIcon, "Vis Fram"));
        this.addTab(new TestPanel(this, 5, microfonIcon, "Audio Opptak"));
        this.addTab(new TestPanel(this, 6, settingsIcon, "Settings"));
        MIDletContext.setCurrent(this);
    }
}
