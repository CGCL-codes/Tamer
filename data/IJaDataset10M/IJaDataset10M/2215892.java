package net.sf.opencet.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import net.sf.opencet.inout.IOManager;
import net.sf.opencet.release.AppOptions;

/**
 * 
 */
public class HelpFrame extends GenericFrame {

    private JEditorPane pContents;

    public HelpFrame() {
        try {
            setSize(800, 600);
            setTitle(StringManager.get(StringManager.HELP_TITLE));
            pContents = new JEditorPane();
            pContents.setEditable(false);
            pContents.setContentType("text/html");
            URL helpURL = HelpFrame.class.getResource(AppOptions.HELP);
            if (helpURL != null) {
                try {
                    pContents.setPage(helpURL);
                } catch (IOException e) {
                    System.err.println("Attempted to read a bad URL: " + helpURL);
                }
            } else {
                System.err.println("Couldn't find file: User Guide.html");
            }
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(new JScrollPane(pContents));
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause() + " / " + e.getMessage());
        }
    }

    protected void closingAction() {
        this.dispose();
    }
}
