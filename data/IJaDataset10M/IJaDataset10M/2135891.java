package net.sf.joafip.meminspector.service;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.joafip.meminspector.entity.NodeForObject;
import com.thoughtworks.xstream.XStream;

public class ShowObjectTree extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2296015048339810387L;

    private final String fileName;

    public static void main(final String[] args) {
        ShowObjectTree showObjectTree;
        try {
            showObjectTree = new ShowObjectTree(args[0]);
            showObjectTree.run();
        } catch (MemInspectorException e) {
            e.printStackTrace();
        }
    }

    public ShowObjectTree(final String fileName) throws MemInspectorException {
        super(new BorderLayout());
        this.fileName = fileName;
        final JTreeForNodeForObject tree = new JTreeForNodeForObject(getRootNode());
        final JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void run() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    createAndShowGUI();
                } catch (MemInspectorException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createAndShowGUI() throws MemInspectorException {
        final JFrame frame = new JFrame("Show object tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setOpaque(true);
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    private final NodeForObject getRootNode() throws MemInspectorException {
        final XStream stream = new XStream();
        FileInputStream inputStream = null;
        final NodeForObject fromXML;
        try {
            inputStream = new FileInputStream(fileName);
            fromXML = (NodeForObject) stream.fromXML(inputStream);
        } catch (FileNotFoundException exception) {
            throw new MemInspectorException(exception);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException exception) {
                }
            }
        }
        return fromXML;
    }
}
