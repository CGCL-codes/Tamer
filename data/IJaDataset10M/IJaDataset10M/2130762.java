package org.argouml.uml.diagram.ui;

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.TabSpawnable;
import org.argouml.uml.ui.TabModelTarget;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.event.GraphSelectionEvent;
import org.tigris.gef.event.GraphSelectionListener;
import org.tigris.gef.event.ModeChangeEvent;
import org.tigris.gef.event.ModeChangeListener;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.ui.ToolBar;

public class TabDiagram extends TabSpawnable implements TabModelTarget, GraphSelectionListener, ModeChangeListener {

    protected UMLDiagram _target;

    protected JGraph _jgraph;

    protected ButtonGroup _lineModeBG;

    protected boolean _shouldBeEnabled = true;

    protected ToolBar _toolBar;

    public TabDiagram() {
        super("Diagram");
        setLayout(new BorderLayout());
        _jgraph = new JGraph();
        _jgraph.setDrawingSize((612 - 30) * 2, (792 - 55 - 20) * 2);
        Globals.setStatusBar(ProjectBrowser.TheInstance);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        p.add(_jgraph, BorderLayout.CENTER);
        add(p, BorderLayout.CENTER);
        _jgraph.addGraphSelectionListener(this);
        _jgraph.addModeChangeListener(this);
    }

    public Object clone() {
        TabDiagram newPanel = new TabDiagram();
        return newPanel;
    }

    public void setTarget(Object t) {
        setTarget(t, true);
    }

    public void setTarget(Object t, boolean visible) {
        if (t == null) _shouldBeEnabled = false;
        if (t instanceof UMLDiagram) {
            _target = (UMLDiagram) t;
            _shouldBeEnabled = true;
        } else {
            _shouldBeEnabled = false;
            return;
        }
        UMLDiagram d = (UMLDiagram) _target;
        _jgraph.setDiagram(d);
        if (_toolBar != null) {
            setVisible(false);
            remove(_toolBar);
        }
        _toolBar = d.getToolBar();
        add(_toolBar, BorderLayout.NORTH);
        setVisible(visible);
        validate();
    }

    public Object getTarget() {
        return _target;
    }

    public void refresh() {
        setTarget(_target);
    }

    public boolean shouldBeEnabled() {
        return _shouldBeEnabled;
    }

    public JGraph getJGraph() {
        return _jgraph;
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
        getJGraph().setVisible(b);
    }

    public void selectionChanged(GraphSelectionEvent gse) {
        Vector sels = gse.getSelections();
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        if (sels.size() == 1) pb.setDetailsTarget(sels.elementAt(0)); else pb.setDetailsTarget(null);
    }

    public void removeGraphSelectionListener(GraphSelectionListener listener) {
        _jgraph.removeGraphSelectionListener(listener);
    }

    public void modeChange(ModeChangeEvent mce) {
        if (!Globals.getSticky() && Globals.mode() instanceof ModeSelect) _toolBar.unpressAllButtons();
    }

    public void removeModeChangeListener(ModeChangeListener listener) {
        _jgraph.removeModeChangeListener(listener);
    }
}
