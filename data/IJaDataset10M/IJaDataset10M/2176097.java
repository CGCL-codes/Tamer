package org.nwn.prog.gui.plugins.res;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.nwn.common.service.Loader;
import org.nwn.prog.gui.DataHolder;
import org.nwn.prog.gui.GFFEditorApplication;
import org.nwn.prog.gui.plugins.ApplicationTabContainerController;
import org.nwn.prog.gui.plugins.ApplicationTabController;
import org.nwn.prog.gui.wrapper.NamedObject;
import org.nwn.prog.gui.wrapper.ReflectedJTable;
import org.nwn.resource.generic.Resource;
import org.nwn.resource.generic.ResourceContainer;
import org.nwn.resource.generic.ResourceContainerException;
import org.nwn.resource.generic.SimpleResourceTypeMap;

/**
 * @author Niels "Moon" Thykier
 *
 */
public class ResourceContainerTab extends JPanel implements ApplicationTabContainerController, ActionListener {

    /**
	 *
	 */
    private static final long serialVersionUID = 953843371027005310L;

    public static final String[] mimes = new String[] { "nwn/erf", GFFEditorApplication.DIR_MIMETYPE };

    private GFFEditorApplication app;

    private DataHolder data;

    private JMenuItem[] items;

    private ReflectedJTable table;

    private ResourceContainerTableModel model;

    private SimpleResourceTypeMap resMap;

    private DefaultMutableTreeNode creatable;

    private RCKeyListener keyListener;

    public ResourceContainerTab() {
        super(new BorderLayout());
        items = new JMenuItem[6];
        items[0] = new JMenu("Import ...");
        JMenuItem iFile = new JMenuItem("File/Folder", KeyEvent.VK_F);
        JMenuItem iTab = new JMenuItem("Tab", KeyEvent.VK_T);
        iFile.addActionListener(this);
        iFile.setActionCommand("f");
        iFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
        iTab.addActionListener(this);
        iTab.setActionCommand("t");
        iTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
        items[0].add(iFile);
        items[0].add(iTab);
        items[0].setMnemonic('I');
        items[1] = new JMenuItem("Export file", KeyEvent.VK_E);
        items[1].addActionListener(this);
        items[1].setActionCommand("e");
        items[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        items[2] = null;
        items[3] = new JMenuItem("Open Resource", KeyEvent.VK_O);
        items[3].addActionListener(this);
        items[3].setActionCommand("o");
        items[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
        items[5] = new JMenuItem("Remove Resource", KeyEvent.VK_R);
        items[5].addActionListener(this);
        items[5].setActionCommand("r");
        items[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
        table = new ReflectedJTable();
        this.add(new JScrollPane(table));
        creatable = new DefaultMutableTreeNode(new NamedObject("NWN Containers", this), true);
        List<ResourceContainer> containers = Loader.findProviders(ResourceContainer.class);
        for (ResourceContainer con : containers) {
            if (con.supportsMutability()) {
                creatable.add(new DefaultMutableTreeNode(new NamedObject((String) con.getParameter(ResourceContainer.CONTAINER_PARAM_FILETYPE), con), false));
            }
        }
    }

    public ResourceContainerTab(GFFEditorApplication app, ResourceContainer con, File file) {
        this();
        this.app = app;
        this.data = new DataHolder(file);
        resMap = app.getResourceTypeMap();
        model = new ResourceContainerTableModel(con, resMap);
        model.setResourceContainerTab(this);
        table.setModel(model);
        table.useAutoSorter();
        model.updateColumnNames(table);
        this.setName(data.getName());
    }

    public void open(GFFEditorApplication appli, DataHolder holder) throws IOException {
        this.app = appli;
        this.data = holder;
        File file = holder.getFile();
        if (file == null) {
            throw new IOException("Can only open files!");
        }
        ResourceContainer con = Loader.findFileHandler(ResourceContainer.class, file);
        if (con == null) {
            throw new IOException("No handler for: " + data.getName());
        }
        resMap = app.getResourceTypeMap();
        con.setResourceTypeMap(resMap);
        con.readContainer(file, true);
        finishBuild(con);
    }

    protected void finishBuild(ResourceContainer con) {
        model = new ResourceContainerTableModel(con, resMap);
        model.setResourceContainerTab(this);
        table.setModel(model);
        table.useAutoSorter();
        model.updateColumnNames(table);
        table.setDefaultRenderer(Object.class, model.getRenderer());
        keyListener = new RCKeyListener(this, table, model);
        table.addKeyListener(keyListener);
        setName(data != null ? data.getName() : "Untitled");
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTabTitle() {
        return getName();
    }

    public void save() throws IOException {
        try {
            model.getResourceContainer().write();
        } catch (ResourceContainerException e) {
            throw new IOException(e.toString());
        }
    }

    public boolean close() {
        boolean toReturn = true;
        if (model.isDirty()) {
            int res = JOptionPane.showConfirmDialog(this, "Do you wish to save before closing?", getName() + " has been modified.", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch(res) {
                case JOptionPane.YES_OPTION:
                    toReturn = app.handleSave(this, false);
                case JOptionPane.NO_OPTION:
                    break;
                default:
                    toReturn = false;
                    break;
            }
        }
        if (toReturn) {
            try {
                model.getResourceContainer().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }

    public void updateMenu(JMenu menu) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                menu.add(items[i]);
            } else {
                menu.addSeparator();
            }
        }
    }

    protected void importContainer(ResourceContainer resCon, File loc, boolean closeContainer) throws IOException {
        ImportDialog dia = new ImportDialog(app, this, resCon, loc, closeContainer);
        dia.setVisible(true);
    }

    protected void openJointTab(ResourceContainerTab other) {
        ApplicationTabController tab = new ResourceContainerJointTab(app, this, other);
        app.addTab(tab, true);
    }

    public ResourceContainerTableModel getTableModel() {
        return model;
    }

    public void actionPerformed(ActionEvent evt) {
        int[] rows;
        ResourceContainer con = model.getResourceContainer();
        switch(evt.getActionCommand().charAt(0)) {
            case 't':
                if (!con.isMutable()) {
                    JOptionPane.showMessageDialog(this, "This handler cannot modify the archive." + "\nThe archive may be read-only or the handler may a read-only handler.", "Cannot import", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<ResourceContainerTab> tabs = new LinkedList<ResourceContainerTab>();
                for (int i = 0; i < app.getTabCount(); i++) {
                    ApplicationTabController tab = app.getTab(i);
                    if (tab instanceof ResourceContainerTab && !tab.equals(this)) {
                        tabs.add((ResourceContainerTab) tab);
                    }
                }
                if (tabs.size() > 0) {
                    ResourceContainerTab chosen = (ResourceContainerTab) JOptionPane.showInputDialog(this, "Please choose a tab", "Select Tab", JOptionPane.QUESTION_MESSAGE, null, tabs.toArray(new ResourceContainerTab[tabs.size()]), tabs.get(0));
                    if (chosen != null) {
                        try {
                            openJointTab(chosen);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, e.toString(), "Error importing: " + chosen.getName(), JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 'f':
                if (!con.isMutable()) {
                    JOptionPane.showMessageDialog(this, "This handler cannot modify the archive." + "\nThe archive may be read-only or the handler may a read-only handler.", "Cannot import", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                File importFile = app.selectFile("Import", JFileChooser.FILES_AND_DIRECTORIES, (FileFilter[]) null);
                if (!importFile.exists() || !importFile.canRead()) {
                    JOptionPane.showMessageDialog(this, "Cannot import " + importFile + "\nIt either does not exist or cannot be read.", importFile + " is not readable.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DataInputStream in = null;
                try {
                    ResourceContainer resCon = Loader.findFileHandler(ResourceContainer.class, importFile);
                    if (resCon != null) {
                        resCon.setResourceTypeMap(resMap);
                        resCon.readContainer(importFile);
                        importContainer(resCon, importFile, true);
                    } else {
                        Resource res = resMap.makeResource(importFile);
                        byte[] buffer;
                        if (res.getType() == SimpleResourceTypeMap.RESOURCE_TYPE_UNKNOWN || res.getType() == SimpleResourceTypeMap.RESOURCE_TYPE_INVALID) {
                            JOptionPane.showMessageDialog(this, "Cannot import " + importFile + "\nIts file type is not a known NWN resource or is invalid.", importFile + " is not recognised.", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (con.hasResource(res)) {
                            int answer = JOptionPane.showConfirmDialog(this, res + " already exists. Override?", "Override?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                            if (answer != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }
                        buffer = new byte[(int) importFile.length()];
                        in = new DataInputStream(new FileInputStream(importFile));
                        in.readFully(buffer);
                        con.addResource(res, buffer);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.toString(), "Error importing: " + importFile, JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 'e':
                rows = table.getSelectedRows();
                if (rows != null && rows.length > 0) {
                    File dir = app.selectFile("Export", JFileChooser.DIRECTORIES_ONLY, (FileFilter[]) null);
                    int override = 0;
                    if (dir.exists() && !dir.isDirectory()) {
                        JOptionPane.showMessageDialog(this, "Cannot export to " + dir + "\nIt is not a folder.", dir + " is not a folder", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (!dir.exists() && !dir.mkdirs()) {
                            JOptionPane.showMessageDialog(this, "Cannot export to " + dir + "\nUnable to create the dir.", "Could not create: " + dir, JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        con.cache();
                        OutputStream out = null;
                        File outFile = null;
                        try {
                            for (int i = 0; i < rows.length; i++) {
                                Resource res = model.getResource(table.rowIndexToModel(rows[i]));
                                outFile = new File(dir, res.getFullName());
                                if (outFile.exists()) {
                                    if (override < 0) {
                                        continue;
                                    } else if (override == 0) {
                                        int choice = JOptionPane.showOptionDialog(this, outFile + " already exists", "Override " + res.getFullName() + "?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "Yes to all", "No", "No to all", "Cancel" }, "Yes");
                                        switch(choice) {
                                            case 1:
                                                override = 1;
                                            case 0:
                                                break;
                                            case 3:
                                                override = -1;
                                            case 2:
                                                continue;
                                            case 4:
                                                return;
                                        }
                                    }
                                }
                                byte[] buffer = con.getResourceAsBytes(res);
                                out = new FileOutputStream(outFile);
                                out.write(buffer);
                            }
                            JOptionPane.showMessageDialog(this, "Resources successfully extracted to " + dir, "Success.", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(this, e.toString(), "Could not write " + (outFile != null ? outFile : "file"), JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        con.decache();
                    }
                }
                break;
            case 'o':
                openResource();
                break;
            case 'r':
                removeSelectedResources(null);
                break;
        }
    }

    protected void removeSelectedResources(ReflectedJTable jTable) {
        ResourceContainer con = model.getResourceContainer();
        if (jTable == null) {
            jTable = table;
        }
        int[] rows;
        if (!con.isMutable()) {
            JOptionPane.showMessageDialog(this, "This handler cannot modify the archive." + "\nThe archive may be read-only or the handler may a read-only handler.", "Cannot remove resources", JOptionPane.ERROR_MESSAGE);
            return;
        }
        rows = jTable.getSelectedRows();
        if (rows != null && rows.length > 0) {
            try {
                List<Resource> resources = new ArrayList<Resource>(rows.length);
                for (int i = 0; i < rows.length; i++) {
                    resources.add(model.getResource(jTable.rowIndexToModel(rows[i])));
                }
                for (Resource r : resources) {
                    con.removeResource(r);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Could not remove all the resources." + "\n" + e.toString(), "Error while removing resources", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getPluginName() {
        return "ResourceContainer Plugin";
    }

    public void visibilityChange(boolean newState) {
    }

    public String[] getMimeTypes() {
        return mimes;
    }

    public void updateResource(Object key, byte[] newValue) throws IOException {
        try {
            model.getResourceContainer().addResource((Resource) key, newValue);
        } catch (ResourceContainerException e) {
            throw new IOException(e.toString());
        }
    }

    public InputStream getResourceStream(Object key) throws IOException {
        return this.model.getResourceContainer().getResource((Resource) key);
    }

    public void updateIsDirty(boolean isDirty) {
        String name = (data != null ? data.getName() : "Untitled");
        if (isDirty) {
            setName("* " + name);
        } else {
            setName(name);
        }
        app.updateName(this, null, isDirty);
    }

    public void create(GFFEditorApplication appli, Object type) {
        this.app = appli;
        this.resMap = app.getResourceTypeMap();
        if (type instanceof NamedObject) {
            type = ((NamedObject) type).getObject();
        }
        ResourceContainer con = (ResourceContainer) type;
        con.setResourceTypeMap(resMap);
        con.createNewContainer();
        finishBuild(con);
    }

    public MutableTreeNode getCreatableTypes() {
        return creatable;
    }

    public int accepts(String... mimeArray) {
        return ALL_MIMES_ACCEPTED;
    }

    public Object addResource(String name, String mime) {
        Resource res = resMap.makeResource(name);
        return res;
    }

    public boolean hasResource(String name) {
        Resource res;
        try {
            res = resMap.makeResource(name);
        } catch (RuntimeException e) {
            return false;
        }
        return model.hasResource(res);
    }

    public boolean openResource() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return false;
        }
        return openResource(model.getResource(table.rowIndexToModel(row)));
    }

    public boolean openResource(String name) {
        Resource res;
        try {
            res = resMap.makeResource(name);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Could not find " + name, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return openResource(res);
    }

    private boolean openResource(Resource res) {
        String mime = resMap.getResourceMimeType(res.getType());
        ApplicationTabController tab = app.getHandler(mime.split("\\s*+,\\s*+"));
        if (tab != null) {
            try {
                tab.open(app, new DataHolder(this, res));
                app.addTab(tab, true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.toString(), "Could not read: " + res.getResref(), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean canSaveAs() {
        return data == null || model.getResourceContainer().supportsRelocation();
    }

    public void setNewDataHolder(DataHolder holder) throws IOException {
        if (holder.getFile() == null) {
            throw new IOException("Can only save to files.");
        }
        data = holder;
        model.getResourceContainer().setContainerLocation(holder.getFile());
    }

    public DataHolder getDataHolder() {
        return data;
    }

    public String getPluginVersion() {
        return "0.0.1";
    }
}
