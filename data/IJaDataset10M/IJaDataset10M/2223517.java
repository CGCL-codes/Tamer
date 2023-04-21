package com.db4o.nb.db4ofiletype;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.nb.api.Db4oDatabase;
import com.db4o.nb.api.Db4oProvider;
import com.db4o.nb.api.Db4oServer;
import com.db4o.nb.api.QueryData;
import com.db4o.nb.api.exception.Db4oProviderException;
import com.db4o.nb.dialogs.Db4oNewDatabaseDialog;
import com.db4o.nb.dialogs.EnterMessageDialog;
import com.db4o.nb.editor.Db4oDataEditor;
import com.db4o.nb.editor.nodes.Db4oDataNode;
import com.db4o.nb.editor.nodes.QueryNode;
import com.db4o.nb.util.DialogUtil;
import com.db4o.nb.util.PreferencesUtil;
import com.db4o.tools.Defragment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import org.openide.ErrorManager;
import org.openide.actions.DeleteAction;
import org.openide.actions.PropertiesAction;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Node that represents a db4o database in the nevaigator view.
 * 
 * @author klevgert
 */
public class Db4oDatabaseNode extends DataNode {

    private static final String IMAGE_ICON_BASE = "icons/database.gif";

    private static final String IMAGE_EXECUTING = "icons/field.gif";

    private static final String IMAGE_EXEC = "icons/execute.gif";

    private static final String IMAGE_CONNECTED = "icons/connected.gif";

    private Db4oDatabase database;

    private Db4oServer server;

    private Db4oDataEditor editor;

    private InputOutput io;

    /** 
   * Creates a new instance of Db4oDatabaseNode 
   */
    public Db4oDatabaseNode(DataObject dataObject, Db4oDatabase database) {
        super(dataObject, new Db4oDatabaseNodeChildren(((Db4oProvider) dataObject.getCookie(Db4oProvider.class)).getQueries(), dataObject));
        io = IOProvider.getDefault().getIO("Db4o Output", false);
        this.database = database;
        this.database.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("connected")) {
                    if (((Boolean) evt.getNewValue()).booleanValue() == true) io.getOut().println("connected to " + Db4oDatabaseNode.this.database.getName()); else io.getOut().println("disconnected from " + Db4oDatabaseNode.this.database.getName());
                    fireIconChange();
                }
            }
        });
        this.server = ((Db4oProvider) dataObject.getCookie(Db4oProvider.class)).getServerHost();
        this.server.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("state")) {
                    fireIconChange();
                }
            }
        });
        this.setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    public void destroy() throws IOException {
        super.destroy();
        if (this.server != null) {
            this.server.stop(false);
        }
    }

    /**
   * Gets the context dependent image
   */
    public Image getIcon(int type) {
        Image orig = super.getIcon(type);
        if (this.server != null) {
            if (this.server.getState() == Db4oServer.STARTING || this.server.getState() == Db4oServer.STOPPING) {
                orig = overlayImage(orig, IMAGE_EXECUTING);
            } else if (this.server.getState() == Db4oServer.STARTED) {
                orig = overlayImage(orig, IMAGE_EXEC);
            }
        }
        if (this.database != null && this.database.isConnected()) {
            orig = overlayImage(orig, IMAGE_CONNECTED);
        }
        return orig;
    }

    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    /**
   * Returns the preferred action that will be executed on double click.
   * @return preferred node action
   */
    public Action getPreferredAction() {
        Action retValue = this.getActions(true)[0];
        return retValue;
    }

    /**
   * Gets the action that could be performed on the node.
   * @param b   flag indicating if the menu is a popup 
   * @return Array of actions to execute on the node
   */
    public Action[] getActions(boolean b) {
        Action[] actions = new Action[] { new OpenEditorAction(), null, new StartServerAction(), new StopServerAction(), null, new SendMessageAction(), null, SystemAction.get(DeleteAction.class), null, new DefragmentAction(), new BackupAction(), null, SystemAction.get(PropertiesAction.class) };
        return actions;
    }

    /**
   * Gets the file path and name associated with the node.
   * This might be null if the connection is a server connection
   * @return file name or null if a server connection.
   */
    public String getFileName() {
        return database.getFile();
    }

    public void reloadChildren() {
    }

    /**
   * Creates a properties sheet for this node.
   * @return Property sheet.
   */
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = sheet.createPropertiesSet();
        try {
            createNameProperty(set);
            createFileProperty(set);
            createHostProperty(set);
            createPortProperty(set);
            createStateProperty(set);
            createAllowVersionUpdateProperty(set);
        } catch (Exception ex) {
            ErrorManager.getDefault().notify(ex);
        }
        sheet.put(set);
        return sheet;
    }

    private void createPortProperty(final Sheet.Set set) {
        if (this.database.getPort() != null) {
            String displayName = NbBundle.getMessage(this.getClass(), "Port");
            Property portProp = new PropertySupport.ReadOnly("port", String.class, displayName, null) {

                public Object getValue() {
                    return Db4oDatabaseNode.this.database.getPort();
                }
            };
            portProp.setName("port");
            set.put(portProp);
        }
    }

    private void createHostProperty(final Sheet.Set set) {
        if (this.database.getHost() != null) {
            String displayName = NbBundle.getMessage(this.getClass(), "Host");
            Property hostProp = new PropertySupport.ReadOnly("host", String.class, displayName, null) {

                public Object getValue() {
                    return Db4oDatabaseNode.this.database.getHost();
                }
            };
            hostProp.setName("host");
            set.put(hostProp);
        }
    }

    private void createFileProperty(final Sheet.Set set) {
        if (this.database.getFile() != null) {
            String displayName = NbBundle.getMessage(this.getClass(), "File");
            Property fileProp = new PropertySupport.ReadOnly("file", String.class, displayName, null) {

                public Object getValue() {
                    return Db4oDatabaseNode.this.database.getFile();
                }
            };
            fileProp.setName("name");
            set.put(fileProp);
        }
    }

    private void createNameProperty(final Sheet.Set set) {
        if (this.database.getFile() != null) {
            String displayName = NbBundle.getMessage(this.getClass(), "Name");
            Property nameProp = new PropertySupport.ReadWrite("name", String.class, displayName, null) {

                public Object getValue() {
                    return Db4oDatabaseNode.this.database.getFile();
                }

                public void setValue(Object value) {
                    DataObject dataObj = Db4oDatabaseNode.this.getDataObject();
                    dataObj.setModified(true);
                    Db4oDatabaseNode.this.reloadChildren();
                }
            };
            nameProp.setName("name");
            set.put(nameProp);
        }
    }

    /**
   * creates the db4o database state property
   */
    private void createStateProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "State");
        Property stateProp = new PropertySupport.ReadOnly("state", String.class, displayName, null) {

            public Object getValue() {
                String ret = "";
                if (server.isStarted()) {
                    ret = "Hosted";
                } else {
                    ret = (Db4oDatabaseNode.this.getObjectContainer() == null || Db4oDatabaseNode.this.getObjectContainer().ext().isClosed()) ? "Closed" : "Open";
                }
                return ret;
            }
        };
        stateProp.setName("state");
        set.put(stateProp);
    }

    private void createReadOnlyProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "ReadOnly");
        Property roProp = new PropertySupport.ReadWrite("readOnly", Boolean.class, displayName, null) {

            public Object getValue() {
                return Boolean.valueOf(Db4oDatabaseNode.this.database.isReadOnly());
            }

            public void setValue(Object value) {
                Boolean ro = (Boolean) value;
                Db4oDatabaseNode.this.database.setReadOnly(ro.booleanValue());
                DataObject dataObj = Db4oDatabaseNode.this.getDataObject();
                dataObj.setModified(true);
            }
        };
        roProp.setName("readOnly");
        set.put(roProp);
    }

    private void createAllowVersionUpdateProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "AllowVersionUpdate");
        Property versionUpdateProp = new PropertySupport.ReadWrite("allowVersionUpdate", Boolean.class, displayName, null) {

            public Object getValue() {
                return Boolean.valueOf(Db4oDatabaseNode.this.database.isAllowVersionUpdate());
            }

            public void setValue(Object value) {
                Boolean ro = (Boolean) value;
                Db4oDatabaseNode.this.database.setAllowVersionUpdate(ro.booleanValue());
                DataObject dataObj = Db4oDatabaseNode.this.getDataObject();
                dataObj.setModified(true);
            }
        };
        versionUpdateProp.setName("allowVersionUpdate");
        set.put(versionUpdateProp);
    }

    public static class Db4oDatabaseNodeChildren extends Children.Keys {

        private List queryList;

        private DataObject dataObj;

        /** Creates a new instance of Db4oQueryNodeChildren */
        public Db4oDatabaseNodeChildren(List ql, DataObject dataObj) {
            this.queryList = ql;
            this.dataObj = dataObj;
        }

        protected Node[] createNodes(Object object) {
            QueryNode result = new QueryNode((QueryData) object, dataObj);
            return new Node[] { result };
        }

        protected void addNotify() {
            setKeys(queryList.toArray());
        }
    }

    /**
   * Action to open the db4o database represented by this node in 
   * the db4o data editor. 
   */
    private class OpenEditorAction extends AbstractAction {

        public OpenEditorAction() {
            this.putValue(NAME, "Open Data Editor");
            this.putValue(DEFAULT, new Boolean(true));
        }

        public void actionPerformed(ActionEvent e) {
            if (server != null && server.isStarted()) {
                String errmsg = NbBundle.getMessage(OpenEditorAction.class, "AlreadyRunningAsServer");
                DialogUtil.showErrorMessage(errmsg);
                return;
            }
            if (editor != null && editor.isOpened()) {
                editor.setVisible(true);
                editor.requestVisible();
            } else {
                if (Db4oDatabaseNode.this.database.isConfigured() == false) {
                    configureConnection();
                }
                try {
                    editor = new Db4oDataEditor();
                    if (editor != null) {
                        AbstractNode node = new Db4oDataNode(Db4oDatabaseNode.this.getDataObject());
                        String suffix = NbBundle.getMessage(OpenEditorAction.class, "Content");
                        editor.setDisplayName(editor.getDisplayName() + " - " + suffix);
                        editor.getExplorerManager().setRootContext(node);
                        editor.open();
                        editor.requestActive();
                        Db4oDatabaseNode.this.fireIconChange();
                    }
                } catch (DatabaseFileLockedException ex) {
                    String message = NbBundle.getMessage(this.getClass(), "CannotConnectToDatabaseWithArg", ex.getMessage());
                    DialogUtil.showErrorMessage(message);
                } catch (Db4oProviderException ex) {
                    String message = NbBundle.getMessage(this.getClass(), "CannotConnectToDatabase");
                    DialogUtil.showErrorMessage(message);
                }
            }
        }
    }

    /**
   * Action to open the db4o database represented by this node in 
   * the db4o data editor. 
   */
    private class StartServerAction extends AbstractAction {

        private static final String IMAGE_START = "icons/start.gif";

        private static final String IMAGE_STOP = "icons/stop.gif";

        public StartServerAction() {
            String name = NbBundle.getMessage(OpenEditorAction.class, "StartAsServer");
            this.putValue(NAME, name);
            this.putValue(IMAGE_ICON_BASE, IMAGE_START);
            this.putValue(PROP_DISPLAY_NAME, name);
        }

        public void actionPerformed(ActionEvent e) {
            Db4oDatabase database = Db4oDatabaseNode.this.database;
            if (database.isConfigured() == false) {
                configureConnection();
            }
            if (database.isConnected()) {
                String errmsg = NbBundle.getMessage(OpenEditorAction.class, "DatabaseAlreadyOpen");
                DialogUtil.showErrorMessage(errmsg);
                return;
            }
            if (server == null) {
                String errmsg = NbBundle.getMessage(OpenEditorAction.class, "AlreadyRunningAsServer");
                DialogUtil.showErrorMessage(errmsg);
                return;
            }
            if (!server.isStarted()) {
                server.start();
            } else {
                String errmsg = NbBundle.getMessage(OpenEditorAction.class, "ServerAlreadyRunning");
                DialogUtil.showErrorMessage(errmsg);
            }
        }
    }

    /**
   * Action to open the db4o database represented by this node in 
   * the db4o data editor. 
   */
    private class StopServerAction extends AbstractAction {

        private static final String IMAGE_STOP = "icons/stop.gif";

        public StopServerAction() {
            String name = NbBundle.getMessage(OpenEditorAction.class, "StopServer");
            this.putValue(NAME, name);
            this.putValue(PROP_DISPLAY_NAME, name);
            this.putValue(Action.SMALL_ICON, IMAGE_STOP);
        }

        public void actionPerformed(ActionEvent e) {
            Db4oServer server = Db4oDatabaseNode.this.server;
            if (server != null && server.isStarted()) {
                server.stop(false);
            }
        }
    }

    /**
   * Action to commit all changes made so far explicitly. 
   */
    private class SendMessageAction extends AbstractAction {

        public SendMessageAction() {
            String name = NbBundle.getMessage(this.getClass(), "SendMessage");
            this.putValue(NAME, name);
        }

        public void actionPerformed(ActionEvent e) {
            if (database.isConnected()) {
                EnterMessageDialog dlg = new EnterMessageDialog(new javax.swing.JFrame(), true);
                dlg.setVisible(true);
                if (dlg.getReturnStatus() == EnterMessageDialog.RET_CANCEL) {
                    return;
                }
                try {
                    database.sendMessage(dlg.getMessageText());
                } catch (Exception exc) {
                    String errmsg = NbBundle.getMessage(SendMessageAction.class, "CannotSendMessageToServer", exc.getMessage());
                    DialogUtil.showErrorMessage(errmsg);
                }
            } else {
                String errmsg = NbBundle.getMessage(SendMessageAction.class, "NotConnectedToADatabase");
                DialogUtil.showErrorMessage(errmsg);
            }
        }
    }

    private class DefragmentAction extends AbstractAction {

        public DefragmentAction() {
            String name = NbBundle.getMessage(DefragmentAction.class, "Defragment");
            this.putValue(NAME, name);
        }

        public void actionPerformed(ActionEvent e) {
            Db4oDatabase db = Db4oDatabaseNode.this.database;
            if (db.getFile() != null && db.getFile().length() > 0) {
                String message = NbBundle.getMessage(OpenEditorAction.class, "confirmDefragmentation");
                if (DialogUtil.confirmYesNo(message).equals(DialogUtil.YES)) {
                    if (db.isConnected()) {
                        db.disconnect();
                    }
                    String file = db.getFile();
                    boolean deleteFlag = PreferencesUtil.isDeleteOnDefragmentation();
                    new Defragment().run(file, deleteFlag);
                }
            } else {
                String msg = "Defragmentation can only be done on db4o file connections";
                DialogUtil.showErrorMessage(msg);
            }
        }
    }

    private class BackupAction extends AbstractAction {

        public BackupAction() {
            String name = NbBundle.getMessage(BackupAction.class, "Backup");
            this.putValue(NAME, name);
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(".");
            int status = fileChooser.showOpenDialog(null);
            if (status == JFileChooser.CANCEL_OPTION) {
                return;
            }
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();
            try {
                ObjectContainer objectContainer = Db4oDatabaseNode.this.getObjectContainer();
                objectContainer.ext().backup(fileName);
            } catch (IOException ex) {
                DialogUtil.showErrorMessage(ex.getMessage());
            }
        }
    }

    private ObjectContainer getObjectContainer() throws DatabaseFileLockedException {
        Db4oProvider provider = getDb4oProvider();
        ObjectContainer objectContainer = provider.getDatabase().getObjectContainer();
        return objectContainer;
    }

    private Db4oProvider getDb4oProvider() {
        DataObject dataObj = this.getDataObject();
        Db4oProvider provider = (Db4oProvider) dataObj.getCookie(Db4oProvider.class);
        if (provider == null) {
            ErrorManager.getDefault().log("Severe: No db4o provider found");
        }
        return provider;
    }

    /**
   * calls the dialog to configure the connection
   * @return true if configured, otherwise false
   */
    private boolean configureConnection() {
        Db4oNewDatabaseDialog ndd = new Db4oNewDatabaseDialog(null, true);
        ndd.setVisible(true);
        if (ndd.getReturnStatus() == Db4oNewDatabaseDialog.RET_OK) {
            Db4oDatabase db = this.database;
            db.reset();
            db.setName(ndd.getName());
            if (ndd.getDatabaseType() == 0) {
                db.setFile(ndd.getFile());
            } else {
                db.setHost(ndd.getHost());
                db.setPort("" + ndd.getPort());
                db.setUser(ndd.getUser());
                db.setPassword(new String(ndd.getPassword()));
            }
            Db4oProvider provider = getDb4oProvider();
            provider.save();
            return true;
        } else {
            return false;
        }
    }

    /**
   * Merges the original images with a second one.
   */
    private Image overlayImage(final Image orig, String overlay) {
        Image runBadge = Utilities.loadImage(overlay);
        Image newImage = Utilities.mergeImages(orig, runBadge, 5, 5);
        return newImage;
    }
}
