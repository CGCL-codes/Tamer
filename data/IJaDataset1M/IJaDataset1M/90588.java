package org.identifylife.key.editor.gwt.client.modules.features.gui;

import org.identifylife.key.editor.gwt.client.gui.tree.FeatureTreeNode;
import org.identifylife.key.editor.gwt.client.modules.features.FeatureManager;
import org.identifylife.key.editor.gwt.client.modules.features.gui.tree.DatasetTreeNode;
import org.identifylife.key.editor.gwt.shared.model.Dataset;
import org.identifylife.key.editor.gwt.shared.model.Feature;
import org.identifylife.key.editor.gwt.shared.model.ItemType;
import com.allen_sauer.gwt.log.client.Log;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * @author dbarnier
 *
 */
public class MainPanel extends VLayout {

    private static final String DATASET_TEMP = "dataset:unknown";

    private FeatureManager manager;

    private Label statusLabel;

    public MainPanel(FeatureManager manager) {
        this.manager = manager;
        initLayout();
    }

    private void initLayout() {
        VStack moveControls = new VStack(10);
        moveControls.setWidth(32);
        moveControls.setHeight100();
        moveControls.setAlign(VerticalAlignment.CENTER);
        moveControls.setLayoutAlign(Alignment.CENTER);
        TransferImgButton removeButton = new TransferImgButton(TransferImgButton.LEFT);
        removeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selections = manager.getFeatureGrid().getSelection();
                for (ListGridRecord selection : selections) {
                    if (!(selection instanceof FeatureTreeNode)) {
                        continue;
                    }
                    FeatureTreeNode treeNode = (FeatureTreeNode) selection;
                    if (treeNode.getItemType() != ItemType.FEATURE) {
                        continue;
                    }
                    Feature feature = (Feature) treeNode.getItem();
                    if (feature.getMappedTo() == null) {
                        moveUnmappedFeature(treeNode);
                    } else {
                        moveMappedFeature(treeNode);
                    }
                }
            }

            private void moveUnmappedFeature(FeatureTreeNode treeNode) {
                TreeNode dsNode = manager.getDatasetTree().getTreeNode(DATASET_TEMP);
                if (dsNode == null) {
                    Dataset ds = new Dataset(DATASET_TEMP);
                    ds.setLabel("Unknown");
                    dsNode = new DatasetTreeNode(ds);
                    ((DatasetTreeNode) dsNode).setLoaded(true);
                    manager.getDatasetTree().add(dsNode, manager.getDatasetTree().getRoot());
                }
                manager.getDatasetGrid().addFeatureNode(DATASET_TEMP, treeNode);
            }

            private void moveMappedFeature(FeatureTreeNode treeNode) {
                Feature feature = (Feature) treeNode.getItem();
                String datasetId = manager.getDatasetIdFromFeatureId(feature.getMappedTo().getRef());
                TreeNode dsNode = manager.getDatasetTree().getTreeNode(datasetId);
                if (dsNode == null) {
                    Log.warn("Dataset not found: " + datasetId);
                    Dataset ds = new Dataset(DATASET_TEMP);
                    dsNode = new DatasetTreeNode(ds);
                    manager.getDatasetTree().add(dsNode, manager.getDatasetTree().getRoot());
                }
                manager.getDatasetGrid().addFeatureNode(datasetId, treeNode);
            }
        });
        TransferImgButton addButton = new TransferImgButton(TransferImgButton.RIGHT);
        addButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selections = manager.getDatasetGrid().getSelection();
                for (ListGridRecord selection : selections) {
                    if (selection instanceof FeatureTreeNode) {
                        FeatureTreeNode treeNode = (FeatureTreeNode) selection;
                        if (!manager.getDatasetTree().hasTreeNode(treeNode.getItemId())) {
                            continue;
                        }
                        if (treeNode.getItemType() == ItemType.FEATURE) {
                            manager.getFeatureGrid().addFeatureNode(treeNode);
                        }
                    }
                }
            }
        });
        moveControls.addMember(removeButton);
        moveControls.addMember(addButton);
        HLayout content = new HLayout();
        content.setWidth("65%");
        content.setHeight100();
        content.addMember(new PartGridPanel(manager.getDatasetGrid(), "Datasets"));
        content.addMember(moveControls);
        content.addMember(new PartGridPanel(manager.getFeatureGrid(), "Your Features"));
        manager.getDatasetGrid().setWidth100();
        manager.getDatasetGrid().setHeight100();
        manager.getFeatureGrid().setWidth100();
        manager.getFeatureGrid().setHeight100();
        HLayout mainLayout = new HLayout();
        mainLayout.setMargin(5);
        mainLayout.setWidth100();
        mainLayout.setHeight100();
        mainLayout.addMember(content);
        Button cancelButton = new PartButton("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                manager.cancel();
            }
        });
        Button finishButton = new PartButton("Finish");
        finishButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                manager.finish();
            }
        });
        statusLabel = new Label();
        statusLabel.setWidth100();
        statusLabel.setContents("");
        HLayout statusLayout = new HLayout();
        statusLayout.setMargin(5);
        statusLayout.setWidth100();
        statusLayout.setHeight(16);
        statusLayout.setAlign(Alignment.LEFT);
        statusLayout.addMember(statusLabel);
        HLayout buttonLayout = new HLayout();
        buttonLayout.setMargin(5);
        buttonLayout.setWidth100();
        buttonLayout.setHeight(25);
        buttonLayout.setAlign(Alignment.RIGHT);
        buttonLayout.setMembersMargin(5);
        buttonLayout.addMember(cancelButton);
        buttonLayout.addMember(finishButton);
        HLayout padding = new HLayout();
        padding.setHeight(15);
        setWidth100();
        setHeight100();
        setPadding(5);
        addMember(padding);
        addMember(mainLayout);
        addMember(statusLayout);
        addMember(buttonLayout);
    }

    public void setStatusText(String text) {
        statusLabel.setContents(text);
    }

    public static class PartGridPanel extends VLayout {

        private TreeGrid treeGrid;

        private Label titleLabel;

        public PartGridPanel(TreeGrid treeGrid, String title) {
            this.treeGrid = treeGrid;
            initLayout();
            setTitle(title);
        }

        private void initLayout() {
            titleLabel = new Label();
            titleLabel.setAlign(Alignment.CENTER);
            titleLabel.setHeight(10);
            titleLabel.setWidth100();
            setPadding(5);
            setWidth100();
            setHeight100();
            addMember(titleLabel);
            addMember(treeGrid);
        }

        public void setTitle(String title) {
            titleLabel.setContents(title);
        }
    }

    public static class PartButton extends Button {

        public PartButton(String title) {
            setShowRollOver(true);
            setShowDisabled(true);
            setShowDown(true);
            setShowFocused(false);
            setWidth(110);
            setTitle(title);
        }
    }
}
