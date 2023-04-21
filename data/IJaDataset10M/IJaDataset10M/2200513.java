package org.gwt.joi.client;

import java.util.LinkedList;
import java.util.List;
import org.gwt.joi.shared.File;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Joi implements EntryPoint {

    /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
    private static final String SERVER_ERROR = "An error occurred while " + "attempting to contact the server. Please check your network " + "connection and try again.";

    /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    private final FileServiceAsync fileService = GWT.create(FileService.class);

    DockLayoutPanel p = new DockLayoutPanel(Unit.EM);

    TabSet tabPanel = new TabSet();

    private final Messages messages = GWT.create(Messages.class);

    /**
   * This is the entry point method.
   */
    public void onModuleLoad() {
        HLayout mainLayout = new HLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight100();
        Label navigationLabel = new Label();
        navigationLabel.setContents("Navigation");
        navigationLabel.setAlign(Alignment.CENTER);
        navigationLabel.setOverflow(Overflow.HIDDEN);
        navigationLabel.setShowResizeBar(true);
        final Tree t = new Tree();
        TreeGrid treeGrid = new TreeGrid();
        initFileTree(treeGrid, t);
        treeGrid.setShowResizeBar(true);
        mainLayout.addMember(treeGrid);
        VLayout vLayout = new VLayout();
        vLayout.setWidth("70%");
        tabPanel.setTabBarPosition(Side.TOP);
        tabPanel.setTabBarAlign(Side.LEFT);
        final Label detailsLabel = new Label();
        detailsLabel.setContents("Details");
        detailsLabel.setAlign(Alignment.CENTER);
        detailsLabel.setOverflow(Overflow.HIDDEN);
        detailsLabel.setHeight("20%");
        treeGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

            public void onRecordDoubleClick(RecordDoubleClickEvent event) {
                detailsLabel.setContents(event.getRecord().getAttribute("Path"));
                final String fileName = event.getRecord().getAttribute("Name");
                fileService.getFileContents(event.getRecord().getAttribute("Path"), new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(String result) {
                        Tab t1 = new Tab(fileName);
                        t1.setCanClose(true);
                        Canvas pane = new Canvas();
                        pane.setContents(result);
                        t1.setPane(pane);
                        tabPanel.addTab(t1);
                    }
                });
            }
        });
        vLayout.addMember(tabPanel);
        vLayout.addMember(detailsLabel);
        mainLayout.addMember(vLayout);
        mainLayout.draw();
    }

    public static class FileTreeNode extends TreeNode {

        public FileTreeNode(String name) {
            setName(name);
        }

        public FileTreeNode(String name, String path, FileTreeNode... fileTreeNodes) {
            setName(name);
            setChildren(fileTreeNodes);
            setPath(path);
        }

        private void setPath(String path) {
            setAttribute("Path", path);
        }

        public void setName(String name) {
            setAttribute("Name", name);
        }
    }

    private void initFileTree(final TreeGrid treeGrid, final Tree tree) {
        fileService.getFileSystem(new AsyncCallback<File>() {

            public void onFailure(Throwable caught) {
                System.err.println(caught.getMessage());
            }

            public void onSuccess(File result) {
                TreeGridField field = new TreeGridField("Name", "Tree from local data");
                field.setCanSort(false);
                treeGrid.setFields(field);
                tree.setModelType(TreeModelType.CHILDREN);
                tree.setNameProperty("Name");
                initTree(result, tree);
                treeGrid.setData(tree);
            }
        });
    }

    private void initTree(File result, Tree t) {
        FileTreeNode treeItem = new FileTreeNode(result.getName(), result.getPath(), getChildren(result));
        TreeNode rootNode = new TreeNode("root");
        rootNode.setName("root");
        rootNode.setChildren(new TreeNode[] { treeItem });
        t.setRoot(rootNode);
    }

    private FileTreeNode[] getChildren(File result) {
        List<FileTreeNode> children = new LinkedList<FileTreeNode>();
        if (result.getFileList().size() > 0) {
            for (File file : result.getFileList()) {
                FileTreeNode cItem = new FileTreeNode(file.getName(), file.getPath(), getChildren(file));
                children.add(cItem);
            }
            return children.toArray(new FileTreeNode[0]);
        }
        return null;
    }
}
