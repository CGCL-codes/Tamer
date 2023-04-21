package net.liveseeds.eye.detailsview;

import net.liveseeds.base.world.World;
import net.liveseeds.errorutil.ErrorUtil;
import net.liveseeds.eye.detailsview.model.LiveSeedNode;
import net.liveseeds.eye.view.EyeFileFilter;
import net.liveseeds.persistence.XMLPersistenceManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class ExportLiveSeedAction extends AbstractAction implements DetailsViewAction {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(ExportLiveSeedAction.class.getName());

    private static final Logger logger = Logger.getLogger(ExportLiveSeedAction.class);

    private JTree tree;

    private final TreeSelectionListenerImpl treeSelectionListener = new TreeSelectionListenerImpl();

    private final XMLPersistenceManager xmlPersistenceManager = new XMLPersistenceManager();

    private final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

    private DocumentBuilder documentBuilder;

    public ExportLiveSeedAction() {
        putValue(Action.SHORT_DESCRIPTION, RESOURCE_BUNDLE.getString("name"));
        putValue(Action.SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/general/Export16.gif")));
        fileChooser.setFileFilter(new EyeFileFilter("xml"));
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            ErrorUtil.showError(e);
        }
    }

    public void init(final JTree tree, final World world) {
        if (this.tree != null) {
            this.tree.removeTreeSelectionListener(treeSelectionListener);
        }
        this.tree = tree;
        this.tree.addTreeSelectionListener(treeSelectionListener);
        updateState();
    }

    public void actionPerformed(final ActionEvent e) {
        try {
            if (tree == null) {
                return;
            }
            if (fileChooser.showSaveDialog(tree) == JFileChooser.APPROVE_OPTION) {
                final Document document = documentBuilder.newDocument();
                xmlPersistenceManager.init(document);
                xmlPersistenceManager.store(((LiveSeedNode) tree.getSelectionPaths()[0].getLastPathComponent()).getLiveSeed());
                final Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".xml")) {
                    path = path + ".xml";
                }
                transformer.transform(new DOMSource(document), new StreamResult(path));
            }
        } catch (Throwable t) {
            ErrorUtil.showError(t);
        }
    }

    private void updateState() {
        final TreePath[] selectionPaths = tree.getSelectionPaths();
        if (selectionPaths == null) {
            setEnabled(false);
            return;
        }
        setEnabled(selectionPaths.length == 1 && selectionPaths[0].getLastPathComponent() instanceof LiveSeedNode);
    }

    private final class TreeSelectionListenerImpl implements TreeSelectionListener {

        public void valueChanged(final TreeSelectionEvent e) {
            updateState();
        }
    }
}
