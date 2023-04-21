package de.blitzcoder.collide.gui;

import de.blitzcoder.collide.util.Log;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.FilterOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author blitzcoder
 */
public class DebugDocument extends Document {

    private JTextArea textArea;

    private static DebugDocument instance = new DebugDocument();

    public static DebugDocument getInstance() {
        return instance;
    }

    private DebugDocument() {
        setLayout(new GridLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea));
        Log.addListener(new Log.LogListener() {

            @Override
            public void log(final String txt) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        textArea.append(txt);
                    }
                });
            }
        });
    }

    @Override
    public String getTitle() {
        return "Debug";
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon(de.blitzcoder.collide.icons.Icon.load("icon.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }

    @Override
    public String getToolTip() {
        return "DebugLog";
    }

    @Override
    public void focus() {
    }

    @Override
    public boolean equalsFile(File file) {
        return false;
    }
}
