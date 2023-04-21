package royere.cwi.layout;

import royere.cwi.structure.Graph;
import royere.cwi.structure.CycleChecker;
import royere.cwi.structure.BiHashMap;
import royere.cwi.framework.TriggerModule;
import royere.cwi.view.ControlViewMessage;
import royere.cwi.view.ControlViewMessageHandler;
import royere.cwi.view.ViewMessage;
import royere.cwi.view.ViewMessageHandler;
import royere.cwi.framework.Keys;
import royere.cwi.util.RoyereError;
import royere.cwi.util.PropertyMismatch;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import java.util.HashMap;

/**
 * This class acts as the top module class for Layout.
 * It implements all the event-handling methods declared
 * of the Module class. As such, it receives messages
 * from the GraphManager and reacts by assigning a Layout 
 * to the Graph as it is passed through the pipeline.
 *
 * @see royere.cwi.framework.Module
 * @see royere.cwi.framework.TriggerModule
 * @see ViewMessageHandler
 * @see Layout
 * @author yugen
 */
public class LayoutManager extends TriggerModule implements LayoutMessageHandler {

    /** Debug object.  Logs data to various channels. */
    private static Logger logger = Logger.getLogger("royere.cwi.layout.LayoutManager");

    /**
   * For now, the manager is assumed to deal with a single instance of a layout.
   */
    protected Layout theLayout = new CompoundLayout();

    protected BiHashMap laidOutGraphs;

    protected BiHashMap layoutMap = new BiHashMap();

    public static HashMap layouts = new HashMap();

    protected static final String LAIDOUT = "AlreadyLaidOut";

    public HashMap getLayouts() {
        return layouts;
    }

    /**
   * Interpret the given LayoutMessage.  If the LayoutMessage 
   * specifies a primitive Layout, wrap it in a CompoundLayout.
   *
   * @see Layout
   * @see CompoundLayout
   * @see LayoutMessage
   */
    public void handleLayoutMessage(LayoutMessage message) {
        logger.log(Priority.DEBUG, "handleLayoutMessage()");
        Graph graph = message.getGraph();
        String layoutName = message.getLayoutName();
        if (graph == null) {
            logger.log(Priority.DEBUG, "handleLayoutMessage(): Got a null Graph object");
            ViewMessage vm = new ViewMessage(this, null, theLayout);
            vm.setFileName(message.getFileName());
            sendMessage(vm);
            return;
        }
        logger.log(Priority.DEBUG, "handleLayoutMessage(): " + "Received graph: '" + graph + "'" + " with " + graph.getNumberOfNodes() + " nodes and" + " with " + graph.getNumberOfEdges() + " edges and" + " layoutName '" + layoutName + "'");
        Layout superLayout = null;
        if (layouts.get(graph) == null) {
            theLayout = new CompoundLayout();
        }
        if (message.getPrecludeLayout() && layouts.get(graph) != null) {
            superLayout = (Layout) layouts.get(graph);
        } else {
            if (layoutName == null) {
                superLayout = LayoutFactory.selectLayout(graph);
                layoutName = superLayout.getName();
                if (layoutMap.get(layoutName, graph) != null) {
                    superLayout = (Layout) layoutMap.get(layoutName, graph);
                }
                layoutMap.put(layoutName, graph, superLayout);
            } else {
                try {
                    superLayout = (Layout) layoutMap.get(layoutName, graph);
                    if (superLayout == null) {
                        superLayout = LayoutFactory.createLayout(layoutName);
                        layoutMap.put(layoutName, graph, superLayout);
                    }
                } catch (Exception e) {
                    logger.log(Priority.ERROR, "handleLayoutMessage(): " + "Invalid layout name '" + layoutName + "' arrived at the LayoutManager");
                }
            }
        }
        layouts.put(graph, superLayout);
        layoutName = superLayout.getName();
        layoutMap.put(layoutName, graph, superLayout);
        logger.log(Priority.DEBUG, "handleLayoutMessage(): Using layout " + superLayout);
        if (superLayout instanceof CompoundLayout) {
            theLayout = superLayout;
        } else {
            ((CompoundLayout) theLayout).assignSuperLayout(graph, superLayout);
        }
        layoutMap.put(theLayout.getName(), graph, theLayout);
        if (!laidOut(graph, superLayout) && !message.getPrecludeLayout()) {
            logger.log(Priority.DEBUG, "handleLayoutMessage(): Re-layout...");
            try {
                theLayout.position(graph);
            } catch (PropertyMismatch pm) {
                logger.log(Priority.ERROR, pm);
                RoyereError.print("A system error has occurred -- see error log for details.");
                theLayout = null;
            }
        } else {
            logger.log(Priority.DEBUG, "handleLayoutMessage(): Bypassing re-layout...");
            theLayout.setGraph(graph);
            theLayout.refreshTraversals();
        }
        ViewMessage vm = new ViewMessage(this, graph, theLayout);
        vm.setFileName(message.getFileName());
        logger.log(Priority.DEBUG, "handleLayoutMessage(): theLayout = '" + theLayout + "', graph = '" + graph + "'");
        sendMessage(vm);
    }

    public void addViewMessageHandler(ViewMessageHandler handler) {
        addMessageHandler(handler, "royere.cwi.view.ViewMessage");
    }

    public void removeViewMessageHandler(ViewMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.view.ViewMessage");
    }

    public void addControlViewMessageHandler(ControlViewMessageHandler handler) {
        addMessageHandler(handler, "royere.cwi.view.ControlViewMessage");
    }

    public void removeControlViewMessageHandler(ControlViewMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.view.ControlViewMessage");
    }

    /**
   * Tests whether a graph has already been laid out
   * with the given layout.
   */
    protected boolean laidOut(Graph graph, Layout layout) {
        if (laidOutGraphs == null) {
            laidOutGraphs = new BiHashMap();
            laidOutGraphs.put(graph, layout, LAIDOUT);
            return false;
        } else {
            return (laidOutGraphs.get(graph, layout) != null);
        }
    }
}
