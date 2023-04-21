package vademecum.protocol;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vademecum.core.experiment.ExperimentNode;
import vademecum.outline.Outline;
import vademecum.outline.OutlineComp;
import vademecum.protocol.ProtModel.ProtModelCom;
import vademecum.protocol.ProtView.ProtViewListener;
import vademecum.protocol.io.LongHashMap;
import vademecum.protocol.resultitem.ResItem;

/**
 * This classes coordinates the relationship between ProtModel and ProtView. An
 * Instance of this class shall be created for every new Expertice. Every
 * contact with the Protocol Window have to pass this class.
 *
 */
public class Protocol implements TreeSelectionListener {

    private static final Log log = LogFactory.getLog(Protocol.class);

    private ProtModel model;

    private ProtView view;

    /**
	 * Creates a new Protocol (initialize)
	 * @param pcl An IProtocolChangeListener to report changes of the protocol
	 */
    public Protocol(IProtocolChangeListener pcl) {
        IInnerProtocolChangeListener ipcl = new InnerProtocolChangeListener(pcl, this);
        ProtView view = new ProtView();
        ProtViewListener ul = view.new ProtViewListener();
        this.view = view;
        this.model = new ProtModel(null, null, null, ul, ipcl);
    }

    /**
	 * Creates a new Protocol (load)
	 * @param toload The Properties toload (for each Component in the Protocol)
	 * @param entable A table containing a mapping: Number -> ExperimentNode
	 * @param folder the savefolder
	 * @param pcl an IProtocolChangeListener to react on protocolchanges
	 */
    public Protocol(Properties[] toload, LongHashMap<Long, ExperimentNode> entable, File folder, IProtocolChangeListener pcl) {
        IInnerProtocolChangeListener ipcl = new InnerProtocolChangeListener(pcl, this);
        ProtView view = new ProtView();
        ProtViewListener ul = view.new ProtViewListener();
        this.view = view;
        this.model = new ProtModel(toload, entable, folder, ul, ipcl);
    }

    /**
	 * Add a Resultblock to the Protocol
	 *
	 * @param resText
	 *            The noneditable Resulttext
	 * @param resItemsdatapoint
	 *            The Elements of the Resultpane to add
	 */
    public void addResultBlock(String resText, ResItem[] resItems, ExperimentNode en) {
        log.debug("call addresultblock for " + en);
        model.addNewResultBlock(resText, resItems, en);
    }

    /**
	 * Add a ResultItem to the protocol
	 * @param item the ResultItem to add
	 * @param en ExperimentNode this ResultItem belongs to
	 */
    public void addResultItem(ResItem item, ExperimentNode en) {
        model.addResultItem(item, en);
    }

    /**
	 * Add a ProgressPane to the protocol
	 * @param taskbar the taskbar to start the statusbar
	 * @param path the path of the DataNavigation
	 */
    public void addProgressPane(JPanel taskbar, TreePath path) {
        model.addProgressPane(taskbar, path);
    }

    /**
	 * Delete a ResultBlock
	 * @param en ExperimentNode of the ResultBlock to delete
	 */
    public void deleteResultBlock(ExperimentNode en) {
        model.delete(en);
    }

    /**
	 * This function scrolls the Protocol to the specified Component.
	 *
	 * @param pos
	 *            The Component in Protocol to scroll to
	 */
    public void goToComp(JComponent jc) {
        Point p = jc.getParent().getLocation();
        Rectangle rect = new Rectangle(p.x, p.y, 0, 0);
        view.move(rect);
    }

    /**
	 * This function scrolls the Protocol to Component specified by ExperimentNode.
	 *
	 * @param en
	 *            The ExperimentNode of the target ResultPane
	 */
    public void goToComp(ExperimentNode en) {
        ProtModelCom pmc = model.findProtComponent(en);
        if (pmc != null) {
            ProtComponent pc = pmc.getPc();
            Point p = pc.element.getParent().getLocation();
            Rectangle rect = new Rectangle(p.x, p.y, 0, 0);
            view.move(rect);
        }
    }

    /**
	 * Returns the View of the protocol for the central gui managment
	 * @return A JPanel representing the View (without ScrollPane)
	 */
    public JPanel setupProtAlternative() {
        return view.getAlternativeView();
    }

    /**
	 * This method allows Communication with the DataNav Selecting Nodes in the
	 * Data Nav Tree will cause the protocol to jump at the specified position
	 *
	 * @param arg0
	 */
    public void valueChanged(TreeSelectionEvent arg0) {
        TreePath path = arg0.getPath();
        Object o = path.getLastPathComponent();
        if (o instanceof ExperimentNode) {
            ExperimentNode en = (ExperimentNode) o;
            this.goToComp(en);
        } else if (o instanceof OutlineComp) {
            OutlineComp oc = (OutlineComp) o;
            this.goToComp(oc.getElement());
        }
    }

    /**
	 * Adds a SelectListener to the protocol
	 * @param sl the selectlistner to add
	 */
    public void addSelectListener(ISelectListener sl) {
        model.addSelectListener(sl);
    }

    /**
	 * Returns the View of the Outline. Placed here
	 * cause the Outline depends totally on the Protocol
	 * @return
	 */
    public Outline getOutline() {
        return view.getOutline();
    }

    /**
	 * This method is called to save a Protocol to Properties
	 *
	 * @return the Properties created in the save process
	 */
    public Properties[] save(File folder) {
        return model.save(folder);
    }

    /**
	 * The InnerProtocolChangeListener to report changes of
	 * the protocol to the ProtocolChangeListener
	 * 
	 *
	 */
    class InnerProtocolChangeListener implements IInnerProtocolChangeListener {

        private IProtocolChangeListener pcl;

        private Protocol protocol;

        public InnerProtocolChangeListener(IProtocolChangeListener pcl, Protocol protocol) {
            this.pcl = pcl;
            this.protocol = protocol;
        }

        public void changed(ProtocolChangeEvent pce) {
        }

        public void changedInner() {
            pcl.changed(new ProtocolChangeEvent(protocol));
        }
    }
}
