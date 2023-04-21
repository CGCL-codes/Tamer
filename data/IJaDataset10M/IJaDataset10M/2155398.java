package com.navigator.client.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.navigator.client.ui.catalog.CatalogPanel;
import com.navigator.client.ui.playercollection.PlayerCollectionSelectionPanel;
import com.navigator.client.ui.playercollection.UnitCollectionEditor;
import com.navigator.client.ui.playercollection.UnitCollectionEditorContainer;

/**
 * @author Derek Knuese
 * 
 * Created on May 22, 2006
 */
public class LauncherBar extends JPanel {

    private JFrame mainFrame;

    private JButton catalogButton = new JButton("Catalog");

    private JButton collectionButton = new JButton("Collection");

    private JDialog catalogBrowser;

    private JDialog collectionEditor;

    private UnitCollectionEditorContainer editorContainer;

    public LauncherBar(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridLayout(1, 2));
        add(catalogButton);
        add(collectionButton);
        catalogBrowser = new JDialog(mainFrame, "Catalog Browser", false);
        catalogBrowser.getContentPane().add(new CatalogPanel());
        catalogBrowser.setSize(1000, 700);
        collectionEditor = new JDialog(mainFrame, "Collection Editor", false);
        collectionEditor.getContentPane().add(editorContainer = new UnitCollectionEditorContainer());
        collectionEditor.setSize(1000, 700);
        catalogButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                catalogBrowser.setVisible(true);
                catalogBrowser.requestFocus();
                catalogBrowser.toFront();
            }
        });
        collectionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                collectionEditor.setVisible(true);
                collectionEditor.requestFocus();
                collectionEditor.toFront();
            }
        });
    }
}
