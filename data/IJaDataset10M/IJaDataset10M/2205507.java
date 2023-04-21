package org.semanticweb.mmm.mr3.actions;

import java.awt.event.*;
import javax.swing.*;
import org.jgraph.graph.*;
import org.semanticweb.mmm.mr3.data.*;
import org.semanticweb.mmm.mr3.data.MR3Constants.*;
import org.semanticweb.mmm.mr3.jgraph.*;
import org.semanticweb.mmm.mr3.ui.*;
import org.semanticweb.mmm.mr3.ui.OntologyPanel.*;
import org.semanticweb.mmm.mr3.util.*;

/**
 * @author takeshi morita
 */
public class EditConceptAction extends AbstractAction {

    private String uri;

    private BasePanel basePanel;

    private RDFSInfo rdfsInfo;

    private GraphCell graphCell;

    private RDFGraph graph;

    private GraphManager gmanager;

    public EditConceptAction(RDFGraph g, GraphManager gm) {
        graph = g;
        gmanager = gm;
    }

    public EditConceptAction(BasePanel bp, RDFGraph g, GraphManager gm) {
        basePanel = bp;
        graph = g;
        gmanager = gm;
    }

    public void setURIString(String uri) {
        this.uri = uri;
    }

    public void setGraphCell(GraphCell gc) {
        graphCell = gc;
    }

    public void setRDFSInfo(RDFSInfo info) {
        rdfsInfo = info;
    }

    private RDFSInfo editConcept() {
        RDFSInfo beforeRDFSInfo = null;
        if (rdfsInfo instanceof ClassInfo) {
            beforeRDFSInfo = new ClassInfo((ClassInfo) rdfsInfo);
        } else if (rdfsInfo instanceof PropertyInfo) {
            beforeRDFSInfo = new PropertyInfo((PropertyInfo) rdfsInfo);
        }
        RDFSInfoMap rdfsInfoMap = gmanager.getCurrentRDFSInfoMap();
        rdfsInfoMap.removeURICellMap(rdfsInfo);
        rdfsInfo.setURI(uri);
        GraphUtilities.resizeRDFSResourceCell(gmanager, rdfsInfo, graphCell);
        rdfsInfoMap.putURICellMap(rdfsInfo, graphCell);
        gmanager.selectChangedRDFCells(rdfsInfo);
        return beforeRDFSInfo;
    }

    private void editWithDialog() {
        if (graphCell != null) {
            uri = basePanel.getURIString();
            if (gmanager.isEmptyURI(uri) || gmanager.isDuplicatedWithDialog(uri, graphCell, graph.getType())) {
                return;
            }
            RDFSInfo beforeRDFSInfo = editConcept();
            rdfsInfo.setMetaClass(basePanel.getMetaClassString());
            if (graph.getType() == GraphType.CLASS) {
                HistoryManager.saveHistory(HistoryType.EDIT_CLASS_WITH_DIAGLOG, beforeRDFSInfo, rdfsInfo);
            } else if (graph.getType() == GraphType.PROPERTY) {
                HistoryManager.saveHistory(HistoryType.EDIT_ONT_PROPERTY_WITH_DIAGLOG, beforeRDFSInfo, rdfsInfo);
            }
            graph.clearSelection();
            graph.setSelectionCell(graphCell);
        }
    }

    private boolean isValidResource(String newRes, String oldRes) {
        return !newRes.equals(oldRes) && !gmanager.isDuplicatedWithDialog(newRes, null, graph.getType());
    }

    public void editWithGraph(String uri, RDFSInfo info, GraphCell cell) {
        rdfsInfo = info;
        graphCell = cell;
        this.uri = uri;
        if (!isValidResource(uri, rdfsInfo.getURIStr())) {
            graph.getGraphLayoutCache().editCell(cell, cell.getAttributes());
            return;
        }
        RDFSInfo beforeInfo = editConcept();
        if (RDFGraph.isRDFSClassCell(graphCell)) {
            HistoryManager.saveHistory(HistoryType.EDIT_CLASS_WITH_GRAPH, beforeInfo, rdfsInfo);
        } else if (RDFGraph.isRDFSPropertyCell(graphCell)) {
            HistoryManager.saveHistory(HistoryType.EDIT_ONT_PROPERTY_WITH_GRAPH, beforeInfo, rdfsInfo);
        }
    }

    public void actionPerformed(ActionEvent e) {
        editWithDialog();
    }
}
