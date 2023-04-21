package net.rptools.maptool.client;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import net.rptools.maptool.client.swing.ImagePanel;
import net.rptools.maptool.model.AssetGroup;

public class AssetPanel extends JComponent {

    private AssetTree assetTree;

    private ImagePanel imagePanel;

    public AssetPanel() {
        assetTree = new AssetTree(this);
        imagePanel = new ImagePanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setTopComponent(new JScrollPane(assetTree));
        splitPane.setBottomComponent(new JScrollPane(imagePanel));
        splitPane.setDividerLocation(100);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, splitPane);
    }

    public void addAssetRoot(AssetGroup assetGroup) {
        assetTree.addRootGroup(assetGroup);
    }

    public void setAssetGroup(AssetGroup assetGroup) {
        imagePanel.setModel(new AssetGroupImagePanelModel(assetGroup));
    }
}
