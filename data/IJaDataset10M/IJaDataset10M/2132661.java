package org.webdocwf.util.loader.graphicaleditor.gui.actions;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.webdocwf.util.loader.graphicaleditor.gui.MainFrame;
import org.webdocwf.util.loader.graphicaleditor.gui.TransformationOrderDialog;
import org.webdocwf.util.loader.graphicaleditor.gui.graph.GEGraph;
import org.webdocwf.util.loader.graphicaleditor.gui.graph.TransformationClassGraphCell;
import org.webdocwf.util.loader.graphicaleditor.model.Transformation;

/**
 * @author Adnan Veseli
 *
 */
public class TransformationOrderAction extends GenericAction implements PropertyChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = -9200742863859381199L;

    /**
     * The short descriptive name defaults to undo, undoManager can provide more
     * descriptive text
     */
    public static final String NAME_VALUE = "Transformation Order";

    /** The mnemonic keycode = KeyEvent.VK_X */
    public static final int MNEMONIC_CODE = KeyEvent.VK_O;

    /** The long description text of this action */
    public static final String LONG_DESCRIPTION_VALUE = "Order of the input and output columns of a Transformation";

    /** The keystroke key-code = java.awt.event.KeyEevnt.VK_Z */
    public static final int KEY_STROKE_CODE = KeyEvent.VK_O;

    /** The keystroke modifier mask = Event.CTRL_MASK */
    public static final int KEY_STROKE_MODIFIERS = Event.CTRL_MASK;

    /** The icon for the toolbar */
    public static final Icon SMALL_ICON_VALUE = new ImageIcon(ClassLoader.getSystemResource("org/webdocwf/util/loader/graphicaleditor/gui/resources/transformationorder.png"));

    /** Short description for tool-tip */
    public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;

    private Transformation transformation = null;

    private TransformationOrderDialog diag = null;

    /**
     * @return Returns the addtable.
     */
    private TransformationOrderDialog getDiag() {
        if (diag == null) {
            diag = new TransformationOrderDialog(getFrame());
        }
        return diag;
    }

    public TransformationOrderAction(MainFrame frame) {
        super(frame);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KEY_STROKE_CODE, KEY_STROKE_MODIFIERS);
        putValue(NAME, NAME_VALUE);
        putValue(ACCELERATOR_KEY, keyStroke);
        putValue(MNEMONIC_KEY, new Integer(MNEMONIC_CODE));
        putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
        putValue(SMALL_ICON, SMALL_ICON_VALUE);
        putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        getDiag().setTransformation(transformation);
        getDiag().setVisible(true);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        GEGraph graph = getFrame().getEditor().getCurrentGraph();
        if (graph != null) {
            graph.addGraphSelectionListener(new GraphSelectionListener() {

                public void valueChanged(GraphSelectionEvent arg0) {
                    if (arg0.getCell() instanceof TransformationClassGraphCell) {
                        transformation = (Transformation) ((TransformationClassGraphCell) arg0.getCell()).getXmlElement();
                        setEnabled(true);
                    } else {
                        transformation = null;
                        setEnabled(false);
                    }
                }
            });
        } else {
            setEnabled(false);
        }
    }
}
