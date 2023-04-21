package com.bluebrim.system.client;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import com.bluebrim.gui.client.*;
import com.bluebrim.xml.shared.*;

/**
 * @version $Id: CoSystemUIUtilities.java,v 1.1.1.1 2005/01/01 21:32:20 goran Exp $
 */
public class CoSystemUIUtilities {

    public static Map m_defaultDirectories = new HashMap();

    private CoSystemUIUtilities() {
        super();
    }

    private static JFileChooser getXmlFileChooser() {
        JFileChooser xmlFileChooser = new JFileChooser();
        FileFilter filter = new FileFilter() {

            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String name = file.getName().toLowerCase();
                return name.endsWith(".xml");
            }

            public String getDescription() {
                return "XML";
            }
        };
        xmlFileChooser.setFileFilter(filter);
        return xmlFileChooser;
    }

    /**
	 * Loads an XML file.
	 *
	 * @param file The file containing the XML data.
	 *
	 * @param putInUncommitedObjects Whether to automatically persist the object(s)
	 * created from the XML file.  Set to <code>true</code> unless you know what you
	 * are doing.
	 *
	 * @return A model that has been constructed from the loaded XML file.
	 */
    private static Object loadXml(File file, final CoXmlContext context) {
        Object model = null;
        try {
            model = CoXmlFileUtilities.loadXml(file, context);
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            CoGUI.warn("Load failed: " + t.toString());
        }
        return model;
    }

    public static Object selectAndLoadXml(CoFrame parent, CoXmlContext context, boolean putInUncommitedObjects) {
        return selectAndLoadXml("XML", parent, context);
    }

    public static void saveXml(File file, final CoXmlContext context, final CoXmlEnabledIF topObject) {
        try {
            CoXmlFileUtilities.saveXml(file, context, topObject);
        } catch (Throwable t) {
            CoGUI.warn("Save failed: " + t.toString());
        }
    }

    public static Object selectAndLoadXml(String loadDescription, CoFrame parent, CoXmlContext context) {
        Object loadedObject;
        File file = selectForLoadXml(loadDescription, parent);
        JFrame messageFrame = createMessageFrame("Loading...", "Loading " + loadDescription + ", please wait...");
        try {
            context.setUrl(file.toURL());
        } catch (MalformedURLException mue) {
            CoGUI.warn("Couldn't create URL from file " + file);
        }
        messageFrame.setVisible(true);
        messageFrame.update(messageFrame.getGraphics());
        loadedObject = loadXml(file, context);
        messageFrame.setVisible(false);
        return loadedObject;
    }

    /**
	 * @return A JFrame centered on screen with the title <code>title</code>,
	 * containing the text <code>text</code>.  To see the frame on screen,
	 * call its <code>setVisible()</code> method.
	 */
    public static JFrame createMessageFrame(String title, String text) {
        JFrame frame = new JFrame(title);
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setSize((int) (frame.getWidth() + 20), (int) (frame.getHeight() + 20));
        Rectangle bounds = CoGUI.centerOnScreen(frame.getWidth(), frame.getHeight());
        frame.setLocation(bounds.x, bounds.y);
        return frame;
    }

    private static void saveXml(File file, final CoXmlContext context, final CoXmlExportEnabledIF topObject) {
        try {
            CoXmlFileUtilities.saveXml(file, context, topObject);
        } catch (Throwable t) {
            CoGUI.warn("Save failed: " + t.toString());
        }
    }

    /**
	 * Opens a file requester asking for the name of an XML file, then saves
	 * the <code>topObject</code> to that file.
	 * 
	 * @param parent The <code>CoFrame</code> that the save file dialog will use
	 * as its parent.
	 * 
	 * @param topObject The topmost of the nodes to be saved in the XML file.
	 */
    public static void selectAndSaveXml(String saveDescription, CoFrame parent, CoXmlContext context, CoXmlExportEnabledIF topObject) {
        JFileChooser chooser = getXmlFileChooser();
        chooser.setDialogTitle("Save " + saveDescription);
        chooser.setSelectedFile((File) (m_defaultDirectories.get(saveDescription)));
        chooser.setPreferredSize(new Dimension(600, 600));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        m_defaultDirectories.put(saveDescription, chooser.getSelectedFile());
        JFrame messageFrame = createMessageFrame("Saving...", "Saving " + saveDescription + ", please wait...");
        File file = chooser.getSelectedFile();
        if (file != null) {
            try {
                context.setUrl(file.toURL());
            } catch (MalformedURLException mue) {
                CoGUI.warn("Couldn't create URL from file " + file);
            }
            messageFrame.setVisible(true);
            messageFrame.update(messageFrame.getGraphics());
            saveXml(file, context, topObject);
            messageFrame.setVisible(false);
        }
    }

    /**
	 * Opens a file requester asking for the name of an XML file, then saves
	 * the <code>topObject</code> to that file.
	 * 
	 * @param parent The <code>CoFrame</code> that the save file dialog will use
	 * as its parent.
	 * 
	 * @param topObject The topmost of the nodes to be saved in the XML file.
	 */
    public static void selectAndSaveXml(CoFrame parent, CoXmlContext context, CoXmlExportEnabledIF topObject) {
        selectAndSaveXml("XML", parent, context, topObject);
    }

    public static File selectForLoadXml(String loadDescription, CoFrame parent) {
        JFileChooser chooser = getXmlFileChooser();
        chooser.setDialogTitle("Load " + loadDescription);
        chooser.setSelectedFile((File) (m_defaultDirectories.get(loadDescription)));
        chooser.setPreferredSize(new Dimension(600, 600));
        if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        m_defaultDirectories.put(loadDescription, chooser.getSelectedFile());
        File file = chooser.getSelectedFile();
        return file;
    }
}
