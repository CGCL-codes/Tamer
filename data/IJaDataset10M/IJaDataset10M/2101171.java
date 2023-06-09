package org.neuroph.netbeans.ide.imageexplorer;

import java.io.File;
import java.util.Collection;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.netbeans.ide.CurrentProject;
import org.neuroph.netbeans.ide.project.NeurophProject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.neuroph.netbeans.ide.imageexplorer//PicturesExplorer//EN", autostore = false)
@TopComponent.Description(preferredID = "ImageExplorerTopComponent", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.neuroph.netbeans.ide.imageexplorer.ImageExplorerTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(displayName = "#CTL_PicturesExplorerAction", preferredID = "ImageExplorerTopComponent")
public final class ImageExplorerTopComponent extends TopComponent implements LookupListener, ExplorerManager.Provider {

    ExplorerManager mgr = new ExplorerManager();

    private String username;

    private FileObject root;

    private DataObject dataObject;

    private Node rootnode;

    public ImageExplorerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ImageExplorerTopComponent.class, "CTL_PicturesExplorerTopComponent"));
        setToolTipText(NbBundle.getMessage(ImageExplorerTopComponent.class, "HINT_PicturesExplorerTopComponent"));
        ((BeanTreeView) jScrollPane1).setRootVisible(false);
        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(mgr));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(mgr));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(mgr));
        map.put("delete", ExplorerUtils.actionDelete(mgr, true));
    }

    private void initComponents() {
        jScrollPane1 = new BeanTreeView();
        setLayout(new java.awt.BorderLayout());
        jScrollPane1.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane1ComponentShown(evt);
            }
        });
        jScrollPane1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyPressed(evt);
            }
        });
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }

    private void jScrollPane1ComponentShown(java.awt.event.ComponentEvent evt) {
    }

    private void jScrollPane1KeyPressed(java.awt.event.KeyEvent evt) {
    }

    private javax.swing.JScrollPane jScrollPane1;

    private Result<NeurophProject> result;

    private NeurophProject selectedNP;

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(NeurophProject.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    public void setData() {
        File file = new File((CurrentProject.getInstance().getCurrentProject().getProjectDirectory()).getPath() + "/Images");
        if (file.exists() && file != null) {
            root = FileUtil.toFileObject(file);
            if (root != null) {
                try {
                    ((BeanTreeView) jScrollPane1).setRootVisible(true);
                    dataObject = DataObject.find(root);
                    rootnode = dataObject.getNodeDelegate();
                    mgr.setRootContext(rootnode);
                } catch (DataObjectNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            if (file.mkdir()) {
                root = FileUtil.toFileObject(file);
                if (root != null) {
                    try {
                        ((BeanTreeView) jScrollPane1).setRootVisible(true);
                        dataObject = DataObject.find(root);
                        rootnode = dataObject.getNodeDelegate();
                        mgr.setRootContext(rootnode);
                    } catch (DataObjectNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void resultChanged(LookupEvent le) {
        Lookup.Result localresult = (Result) le.getSource();
        Collection<Object> coll = localresult.allInstances();
        if (!coll.isEmpty()) {
            setData();
        }
    }
}
