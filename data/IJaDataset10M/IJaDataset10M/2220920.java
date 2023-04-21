package com.intel.gpe.client2.expert.workfloweditor;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import javax.swing.Icon;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import com.intel.gpe.client2.common.clientwrapper.ClientWrapper;
import com.intel.gpe.client2.expert.workfloweditor.cell.GridBeanCell;
import com.intel.gpe.client2.expert.workfloweditor.cell.RectangleCell;
import com.intel.gpe.client2.expert.workfloweditor.panels.WorkflowEditorPanel;
import com.intel.gpe.client2.expert.workfloweditor.router.EdgeRouterDSD;
import com.intel.gpe.client2.expert.workfloweditor.router.EdgeRouterSD;
import com.intel.gpe.clients.api.TargetSystemClient;
import com.intel.gpe.clients.api.workflow.Action;
import com.intel.gpe.clients.api.workflow.GPEWorkflowJob;
import com.intel.gpe.clients.api.workflow.PartnerLink;
import com.intel.gpe.clients.api.workflow.Sequence;
import com.intel.gpe.clients.api.workflow.Switch;
import com.intel.gpe.clients.api.workflow.Throw;
import com.intel.gpe.clients.api.workflow.WorkflowConstants;
import com.intel.util.xml.Namespaces;

/**
 * @version $Id: IfThenElementView.java,v 1.10 2007/02/27 11:22:10 vashorin Exp $
 * @author Valery Shorin
 */
public class IfThenElementView extends GridBeanElementView<IfThenElement> {

    GridBeanCell<IfThenElementView> cell;

    WorkflowView thenView;

    WorkflowView elseView;

    DefaultEdge edgeThen;

    DefaultEdge edgeElse;

    DefaultEdge edgeAfterThen;

    DefaultEdge edgeAfterElse;

    RectangleCell<IfThenElementView> connector;

    private static Icon yesArrowIcon = loadIcon("images/yes-arrow.gif");

    private static Icon noArrowIcon = loadIcon("images/no-arrow.gif");

    public IfThenElementView(WorkflowElementView<?> parent, IfThenElement element, WorkflowEditorPanel editor) {
        super(parent, element, editor);
        if (element.getWorkflowThen() == null) {
            thenView = new WorkflowView(this, editor);
        } else {
            thenView = new WorkflowView(this, element.getWorkflowThen(), editor);
        }
        getElement().setWorkflowThen(thenView.getElement());
        if (element.getWorkflowElse() == null) {
            elseView = new WorkflowView(this, editor);
        } else {
            elseView = new WorkflowView(this, element.getWorkflowElse(), editor);
        }
        getElement().setWorkflowElse(elseView.getElement());
        cell = new GridBeanCell<IfThenElementView>(this);
        Editor.createPort(cell, "ThenPort", 0, GraphConstants.PERMILLE / 3);
        Editor.createPort(cell, "ElsePort", GraphConstants.PERMILLE, GraphConstants.PERMILLE / 3);
        cells.add(cell);
        DefaultPort input = Editor.createPort(cell, "Input", GraphConstants.PERMILLE / 2, 0);
        setInputPort(input);
        edgeThen = new DefaultEdge();
        cells.add(edgeThen);
        edgeElse = new DefaultEdge();
        cells.add(edgeElse);
        connector = new RectangleCell<IfThenElementView>(this);
        connector.setPopupEnabled(false);
        GraphConstants.setGradientColor(connector.getAttributes(), Color.GRAY);
        GraphConstants.setOpaque(connector.getAttributes(), true);
        Editor.createPort(connector, "Input", GraphConstants.PERMILLE / 2, 0);
        DefaultPort connectorOutput = Editor.createPort(connector, "Output", GraphConstants.PERMILLE / 2, GraphConstants.PERMILLE);
        setOutputPort(connectorOutput);
        edgeAfterThen = new DefaultEdge();
        cells.add(edgeAfterThen);
        edgeAfterElse = new DefaultEdge();
        cells.add(edgeAfterElse);
        cells.add(connector);
        paintCells();
    }

    public IfThenElementView(WorkflowElementView<?> parent, WorkflowEditorPanel editor) {
        this(parent, new IfThenElement(), editor);
    }

    @Override
    public int getLeftWidth() {
        return thenView.getWidth() + 2;
    }

    @Override
    public int getRightWidth() {
        return elseView.getWidth() + 2;
    }

    @Override
    public int getHeight() {
        return Math.max(thenView.getHeight(), elseView.getHeight()) + 4;
    }

    @Override
    void build(int x, int y) {
        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double((x) * SINGLE_ELEMENT_WIDTH, y * SINGLE_ELEMENT_HEIGHT, SINGLE_ELEMENT_WIDTH * 4, SINGLE_ELEMENT_HEIGHT * 2));
        getEditor().getEditor().getGraphLayoutCache().editCell(cell, cell.getAttributes());
        thenView.build(x - thenView.getRightWidth() - 2, y + 3);
        elseView.build(x + elseView.getLeftWidth() + 2, y + 3);
        GraphConstants.setBounds(connector.getAttributes(), new Rectangle2D.Double((x) * SINGLE_ELEMENT_WIDTH + SINGLE_ELEMENT_WIDTH * 2, (y + Math.max(thenView.getHeight(), elseView.getHeight()) + 3.5) * SINGLE_ELEMENT_HEIGHT + SINGLE_ELEMENT_HEIGHT / 2, 0, 0));
        Editor.connect(edgeThen, (DefaultPort) cell.getChildAt(0), thenView.getInputPort(), new EdgeRouterSD());
        Editor.connect(edgeElse, (DefaultPort) cell.getChildAt(1), elseView.getInputPort(), new EdgeRouterSD());
        Editor.connect(edgeAfterThen, thenView.getOutputPort(), (DefaultPort) connector.getChildAt(0), new EdgeRouterDSD(), GraphConstants.ARROW_CIRCLE);
        Editor.connect(edgeAfterElse, elseView.getOutputPort(), (DefaultPort) connector.getChildAt(0), new EdgeRouterDSD(), GraphConstants.ARROW_CIRCLE);
        repaintCells();
    }

    @Override
    void removeCells() {
        super.removeCells();
        thenView.removeCells();
        elseView.removeCells();
    }

    @Override
    public Icon getLeftIcon() {
        return yesArrowIcon;
    }

    @Override
    public Icon getRightIcon() {
        return noArrowIcon;
    }

    @Override
    public void checkStatuses() throws Exception {
        super.checkStatuses();
        thenView.checkStatuses();
        elseView.checkStatuses();
    }

    @Override
    public boolean populateVariableList(VariableList vars, WorkflowElementView<?> forElement) {
        if ((WorkflowElementView) this == forElement) {
            return true;
        }
        if (!super.populateVariableList(vars, forElement)) {
            VariableList varsThen = new VariableList();
            VariableList varsElse = new VariableList();
            if (thenView.populateVariableList(varsThen, forElement)) {
                vars.merge(varsThen);
                return true;
            }
            if (elseView.populateVariableList(varsElse, forElement)) {
                vars.merge(varsElse);
                return true;
            }
            varsThen.and(varsElse);
            vars.merge(varsThen);
        }
        return false;
    }

    @Override
    public Action getAction(Namespaces namespaces, PartnerLink spoolStorage, GPEWorkflowJob workflowJob, ClientWrapper<TargetSystemClient, ?> workflowTargetSystem, Calendar terminationTime) throws Exception {
        String bpws = namespaces.getPrefix(WorkflowConstants.Workflow.BPWS_NAMESPACE);
        Sequence sequence = new Sequence();
        Action gbAction = super.getAction(namespaces, spoolStorage, workflowJob, workflowTargetSystem, terminationTime);
        Action thenAction = thenView.getAction(namespaces, spoolStorage, workflowJob, workflowTargetSystem, terminationTime);
        Action elseAction = elseView.getAction(namespaces, spoolStorage, workflowJob, workflowTargetSystem, terminationTime);
        if (gbAction == null) {
            if (thenAction == null) {
                return null;
            }
            sequence.addAction(thenAction);
            return sequence;
        }
        sequence.addAction(gbAction);
        if (thenAction == null) {
            thenAction = new Sequence();
        }
        if (elseAction == null) {
            elseAction = new Sequence();
        }
        Switch.Case successfulCase = new Switch.Case(bpws + ":getVariableData('" + getElement().getStatusVariable().getName() + "','" + getElement().getStatusVariable().getPart() + "')/child::node()[1]/child::text() = 'SUCCESSFUL'", thenAction);
        Switch.Case failedCase = new Switch.Case(bpws + ":getVariableData('" + getElement().getStatusVariable().getName() + "','" + getElement().getStatusVariable().getPart() + "')/child::node()[1]/child::text() = 'FAILED'", elseAction);
        Action throwElem = new Throw(WorkflowElement.WORKFLOW_EXECUTION_FAULT);
        Action switchAction = new Switch(new Switch.Case[] { successfulCase, failedCase }, throwElem);
        sequence.addAction(switchAction);
        return sequence;
    }
}
