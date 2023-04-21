package org.fudaa.fudaa.modeleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import com.memoire.bu.BuCheckBox;
import com.memoire.bu.BuVerticalLayout;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluArkSaver;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluXmlReaderHelper;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ctulu.gui.CtuluFileChooserPanel;
import org.fudaa.ebli.calque.BCalquePersistenceGroupe;
import org.fudaa.ebli.calque.BCalqueSaverSingle;
import org.fudaa.fudaa.commun.save.FudaaSaveLib;
import org.fudaa.fudaa.commun.save.FudaaSaveZipLoader;
import org.fudaa.fudaa.modeleur.MdlImplementation.MdlFileFilter;
import org.fudaa.fudaa.sig.FSigLib;
import org.geotools.referencing.cs.DefaultAffineCS;

/**
 * Un modele d'arbre bas� sur un fichier projet, pour la repr�sentation des calques existants dans ce fichier.
 * @author Bertrand Marchand
 * @version $Id: MdlProjectImportTreeModel.java,v 1.1.2.1 2008/05/13 12:10:18 bmarchan Exp $
 */
public class MdlProjectImportTreeModel extends DefaultTreeModel {

    private FudaaSaveZipLoader loader_;

    private static String NAME_ROOT = "data";

    public static class LayerNode extends DefaultMutableTreeNode {

        public String name_;

        public String title_;

        public LayerNode(String _title, String _name) {
            super(_title, true);
            name_ = _name;
            title_ = _title;
        }
    }

    /**
   * @param root
   */
    public MdlProjectImportTreeModel(FudaaSaveZipLoader _loader) {
        super(new LayerNode(NAME_ROOT, "cqRoot"));
        loader_ = _loader;
        if (loader_ != null) buildTree();
    }

    private void buildTree() {
        ((LayerNode) getRoot()).removeAllChildren();
        addLayer(NAME_ROOT + "/", (LayerNode) getRoot());
    }

    private void addLayer(String _parentDir, LayerNode _node) {
        for (String ent : loader_.getEntries(_parentDir)) {
            if (ent.endsWith(CtuluArkSaver.DESC_EXT)) {
                try {
                    CtuluXmlReaderHelper helper = new CtuluXmlReaderHelper(loader_.getReader(_parentDir, ent));
                    String cqTitle = helper.getTextFor("title");
                    String cqName = loader_.getLayerProperty(_parentDir, ent, "name");
                    if (cqName == null) cqName = "_bad_";
                    String entrep = ent.substring(0, ent.lastIndexOf(CtuluArkSaver.DESC_EXT));
                    LayerNode nd = new LayerNode(cqTitle, cqName);
                    _node.add(nd);
                    addLayer(_parentDir + entrep + "/", nd);
                } catch (Exception _exc) {
                }
            }
        }
    }
}
