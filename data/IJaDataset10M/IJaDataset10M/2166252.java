package org.akrogen.tkui.usecases.platform.swing.xul.tree;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.akrogen.tkui.core.dom.bindings.IDOMDocumentBindable;
import org.akrogen.tkui.core.platform.IPlatform;
import org.akrogen.tkui.platform.swing.SwingPlatform;

public class SwingXULTreeTest {

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel();
            frame.getContentPane().add(panel);
            panel.setLayout(new GridLayout());
            InputStream source = new FileInputStream(new File("resources/xul/tree/treeWithPopup.xul"));
            IPlatform platform = SwingPlatform.getDefaultPlatform();
            IDOMDocumentBindable document = platform.createDocument(panel, null, null);
            platform.loadDocument(source, document);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
