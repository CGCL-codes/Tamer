package com.nokia.ats4.appmodel.grapheditor.swing;

import com.nokia.ats4.appmodel.model.domain.ApplicationModelState;
import com.nokia.ats4.appmodel.model.domain.EntryPoint;
import com.nokia.ats4.appmodel.model.domain.OutGate;
import com.nokia.ats4.appmodel.model.domain.Port;
import com.nokia.ats4.appmodel.model.domain.ExitPoint;
import com.nokia.ats4.appmodel.model.domain.InGate;
import com.nokia.ats4.appmodel.model.domain.StartState;
import com.nokia.ats4.appmodel.model.domain.SubModelState;
import com.nokia.ats4.appmodel.model.domain.SystemState;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

/**
 * GraphCellViewFactory creates customized cell views for the JGraph component.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class GraphCellViewFactory extends DefaultCellViewFactory {

    @Override
    protected VertexView createVertexView(Object obj) {
        VertexView view = null;
        DefaultGraphCell cell = (DefaultGraphCell) obj;
        Object userObject = cell.getUserObject();
        if (userObject instanceof StartState) {
            view = new StartStateView(cell);
        } else if (userObject instanceof InGate) {
            view = new InputGateView(cell);
        } else if (userObject instanceof OutGate) {
            view = new OutputGateView(cell);
        } else if (userObject instanceof EntryPoint) {
            view = new EntryPointView(cell);
        } else if (userObject instanceof ExitPoint) {
            view = new ExitPointView(cell);
        } else if (userObject instanceof SubModelState) {
            view = new SubModelView(cell);
        } else if (userObject instanceof SystemState) {
            view = new SystemStateView(cell);
        } else if (userObject instanceof ApplicationModelState) {
            view = new ApplicationModelView(cell);
        } else {
            view = new VertexView(obj);
        }
        return view;
    }

    @Override
    protected EdgeView createEdgeView(Object object) {
        EdgeView view = null;
        DefaultEdge edge = (DefaultEdge) object;
        Port sp = (Port) ((DefaultPort) edge.getSource()).getUserObject();
        DefaultGraphCell cell = (DefaultGraphCell) ((DefaultPort) edge.getSource()).getParent();
        if (sp.getType() == Port.PortType.OUT_GATE_EXIT && cell.getUserObject() instanceof ApplicationModelState) {
            view = new InterApplicationTransitionView(object);
        } else {
            view = new TransitionView(object);
        }
        return view;
    }

    @Override
    protected PortView createPortView(Object object) {
        PortView view = null;
        DefaultPort dp = (DefaultPort) object;
        Object userObject = dp.getUserObject();
        if (userObject instanceof Port && ((Port) userObject).getOwnerState() instanceof ApplicationModelState) {
            Port port = (Port) userObject;
            if (port.getType() == Port.PortType.IN_GATE_ENTRY) {
                view = new InputPortView(object);
                return view;
            } else if (port.getType() == Port.PortType.OUT_GATE_EXIT) {
                view = new OutputPortView(object);
                return view;
            }
        }
        if (userObject instanceof Port) {
            Port port = (Port) userObject;
            switch(port.getType()) {
                case ENTRY:
                    view = new EntryPortView(object);
                    break;
                case EXIT:
                    view = new ExitPortView(object);
                    break;
                case IN_GATE_ENTRY:
                    view = new EntryPortView(object);
                    break;
                case OUT_GATE_EXIT:
                    view = new ExitPortView(object);
                    break;
                case IN_GATE_EXIT:
                    view = new ExitPortView(object);
                    break;
                case OUT_GATE_ENTRY:
                    view = new EntryPortView(object);
                    break;
                default:
                    view = new PortView(object);
            }
        } else {
            view = new PortView(object);
        }
        return view;
    }
}
