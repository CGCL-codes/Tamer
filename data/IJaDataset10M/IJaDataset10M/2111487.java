package com.sptci.cms.admin.view.dialog;

import static com.sptci.echo.Application.getApplication;
import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.list.EnumListModel;
import com.sptci.cms.admin.view.NodeSelectionComponent;
import com.sptci.cms.admin.view.FileUploadComponent;
import com.sptci.cms.admin.model.ImportBehaviour;
import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.SelectField;
import nextapp.echo.app.SplitPane;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP;
import nextapp.echo.app.layout.GridLayoutData;
import javax.jcr.Node;

/**
 * A dialogue used to prompt user for an XML export from a repository
 * that is to be imported into the current repository.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-24
 * @version $Id: NodeImportDialog.java 28 2010-02-02 20:08:42Z spt $
 */
public class NodeImportDialog extends WindowPane {

    private static final long serialVersionUID = 1l;

    private Label pathLabel;

    @Constraints(value = Constraints.Value.NOT_NULL)
    @ActionListener(value = "com.sptci.jcr.webui.listener.DeleteNodeListener")
    private NodeSelectionComponent path;

    private Label behaviourLabel;

    private SelectField behaviour;

    private FileUploadComponent upload;

    @ActionListener(value = "com.sptci.cms.admin.listener.ImportListener")
    private Button importButton;

    public NodeImportDialog(final Node node) {
        new ViewInitialiser<NodeImportDialog>(this).init();
        path = new NodeSelectionComponent(node);
    }

    @Override
    public void init() {
        removeAll();
        super.init();
        final SplitPane pane = new SplitPane(ORIENTATION_VERTICAL_BOTTOM_TOP);
        pane.setAutoPositioned(true);
        createButtons(pane);
        createGrid(pane);
        getApplication().setFocusedComponent(path);
        add(pane);
    }

    private void createGrid(final SplitPane pane) {
        final Grid grid = new Grid();
        grid.add(pathLabel);
        grid.add(path);
        grid.add(behaviourLabel);
        grid.add(createBehaviour());
        upload = new FileUploadComponent();
        final GridLayoutData layout = new GridLayoutData();
        layout.setColumnSpan(2);
        upload.setLayoutData(layout);
        grid.add(upload);
        pane.add(grid);
    }

    private Component createBehaviour() {
        behaviour = new com.sptci.echo.list.SelectField<EnumListModel<ImportBehaviour>>();
        behaviour.setModel(new EnumListModel<ImportBehaviour>(ImportBehaviour.class));
        return behaviour;
    }

    private void createButtons(final SplitPane pane) {
        final Grid row = new Grid();
        row.add(importButton);
        pane.add(row);
    }

    public String getPath() {
        return path.getPath();
    }

    public FileUploadComponent getUpload() {
        return upload;
    }

    public Node getNode() {
        return path.getNode();
    }

    public ImportBehaviour getBehaviour() {
        return (ImportBehaviour) behaviour.getSelectedItem();
    }
}
