package net.sourceforge.ecldbtool.design.action;

import java.sql.Connection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ResourceAction;
import net.sourceforge.ecldbtool.design.MessagesDesign;
import net.sourceforge.ecldbtool.connect.ConnectionManager;
import net.sourceforge.ecldbtool.design.view.DatabaseModelEditor;
import net.sourceforge.ecldbtool.model.io.IMetadataReader;

public class ActionModelImportFromDB extends ResourceAction {

    DatabaseModelEditor editor;

    public void setEditor(DatabaseModelEditor editor) {
        this.editor = editor;
    }

    public ActionModelImportFromDB(DatabaseModelEditor editor) {
        super(MessagesDesign.getResourceBundle(), "model.import.");
        this.editor = editor;
        update();
    }

    public void run() {
        Connection con = ConnectionManager.getConnectionProfile(editor.getEditorSite().getShell()).getConnection();
        if (con != null) {
            try {
                IMetadataReader metadataReader = ConnectionManager.getConnectionProfile(editor.getEditorSite().getShell()).getDriverDescriptor().getMetadataReader();
                metadataReader.setConnection(con);
                metadataReader.fillDatabaseModel(editor.getModel());
                editor.refresh();
                editor.setDirty(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        setEnabled(true);
        setChecked(false);
    }

    public void setActiveEditor(IEditorPart part) {
        update();
        if (part instanceof DatabaseModelEditor) setEditor((DatabaseModelEditor) part);
    }
}
