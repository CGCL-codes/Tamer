package org.semanticweb.mmm.mr3.sample;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import org.semanticweb.mmm.mr3.plugin.*;
import com.hp.hpl.jena.rdf.model.*;

/**
 * @author takeshi morita print RDF Model
 */
public class GetRDFModelSample extends MR3Plugin {

    private JTree classTree;

    private JTree propertyTree;

    private JTextArea textArea;

    private JFrame srcFrame;

    public GetRDFModelSample() {
        classTree = new JTree();
        JScrollPane classTreeScroll = new JScrollPane(classTree);
        classTreeScroll.setMinimumSize(new Dimension(150, 150));
        classTreeScroll.setBorder(BorderFactory.createTitledBorder("Class Tree"));
        propertyTree = new JTree();
        JScrollPane propertyTreeScroll = new JScrollPane(propertyTree);
        propertyTreeScroll.setBorder(BorderFactory.createTitledBorder("Property Tree"));
        textArea = new JTextArea(5, 10);
        JScrollPane textAreaScroll = new JScrollPane(textArea);
        textAreaScroll.setBorder(BorderFactory.createTitledBorder("RDF/XML"));
        srcFrame = new JFrame("Sample Plugin 2");
        srcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JSplitPane treeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, classTreeScroll, propertyTreeScroll);
        treeSplitPane.setDividerLocation(0.5);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeSplitPane, textAreaScroll);
        splitPane.setDividerLocation(0.3);
        srcFrame.getContentPane().add(splitPane);
        srcFrame.setBounds(new Rectangle(50, 50, 500, 400));
    }

    public void exec() {
        try {
            Model rdfModel = getRDFModel();
            Writer out = new StringWriter();
            rdfModel.write(new PrintWriter(out));
            textArea.setText(out.toString());
        } catch (RDFException e) {
            e.printStackTrace();
        }
        classTree.setModel(getClassTreeModel());
        propertyTree.setModel(getPropertyTreeModel());
        srcFrame.setVisible(true);
    }
}
