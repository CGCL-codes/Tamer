package net.sourceforge.arcamplayer.gui.visuals.panels.hierarchy;

import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import net.sourceforge.arcamplayer.core.ApplicationCore;
import net.sourceforge.arcamplayer.gui.controllers.MainController;
import net.sourceforge.arcamplayer.gui.datamodels.CollectionTreeNode;
import net.sourceforge.arcamplayer.library.collection.ClassificationComponent;

/**
 * <p>Manejador de transferencias para permitir movimientos entre elementos del JTree
 * de clasificación de listas de reproducción de la biblioteca de medios..</p>
 * @author David Arranz Oveja
 * @author Pelayo Campa González-Nuevo
 */
public class HierarchicalTreeTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    private MainController controller;

    private TreePath sourcepath;

    /**
	 * instancia del registrador de eventos de ejecución. Para propósitos de depuración.
	 */
    private static Logger logger;

    static {
        logger = Logger.getLogger(ApplicationCore.class);
        URL url = ClassLoader.getSystemResource("resources/log4j.properties");
        PropertyConfigurator.configure(url);
    }

    /**
	 * Constructor que recibe el controlador principal.
	 * @param controller controlador.
	 */
    public HierarchicalTreeTransferHandler(MainController controller) {
        this.controller = controller;
    }

    /**
	 * Devuelve el tipo de acciones disponibles.
	 */
    public int getSourceActions(JComponent c) {
        logger.info("getSourceActions: " + c);
        return COPY_OR_MOVE;
    }

    /**
	 * Crea un objeto transferible a partir del nodo seleccionado.
	 */
    protected Transferable createTransferable(JComponent c) {
        if (!(c instanceof JTree)) {
            return null;
        }
        JTree tree = (JTree) c;
        TreePath sourcepath = tree.getSelectionPath();
        this.sourcepath = sourcepath;
        logger.info("createTransferable: " + sourcepath);
        return new StringSelection(c.toString());
    }

    /**
	 * Notifica que se ha comletado la exportación.
	 */
    protected void exportDone(JComponent c, Transferable t, int action) {
        logger.info("exportDone: " + c);
    }

    /**
	 * Comprueba si se puede importar el elemento.
	 */
    public boolean canImport(TransferSupport support) {
        JTree tree = (JTree) support.getComponent();
        TreePath sourcepath = this.sourcepath;
        Point pt = tree.getMousePosition();
        TreePath targetpath = tree.getPathForLocation(pt.x, pt.y);
        if (targetpath == null) {
            return false;
        }
        CollectionTreeNode node = (CollectionTreeNode) targetpath.getLastPathComponent();
        ClassificationComponent cc = node.getUserObject();
        if (cc.isContainer() && targetIsValid(sourcepath, targetpath)) {
            return true;
        }
        return false;
    }

    /**
	 * <p>Indica si un destino es válido.</p>
	 * @param source objeto a mover.
	 * @param target destino.
	 * @return true en caso afirmativo.
	 */
    private boolean targetIsValid(TreePath source, TreePath target) {
        logger.info("-Source: " + source);
        logger.info("-Target: " + target);
        if (isContained(source.getPath(), target.getPath())) {
            return false;
        }
        if (target.getLastPathComponent().equals(source.getParentPath().getLastPathComponent())) {
            return false;
        }
        return true;
    }

    /**
	 * Comprueba si un path está contenido en otro.
	 * @param conted path a comprobar si está contenido en otro.
	 * @param conter contenedor.
	 * @return true en caso afirmativo.
	 */
    private boolean isContained(Object[] conted, Object[] conter) {
        if (conted.length > conter.length) {
            return false;
        }
        for (int i = 0; i < conted.length; i++) {
            if (!conted[i].equals(conter[i])) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Solicita la importación de datos al controlador.
	 */
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        logger.info("importData: IMPORT GRANTED!!!");
        JTree tree = (JTree) support.getComponent();
        Point pt = tree.getMousePosition();
        TreePath targetpath = tree.getPathForLocation(pt.x, pt.y);
        controller.requestMoveClassificationComponent(sourcepath, targetpath);
        return true;
    }
}
