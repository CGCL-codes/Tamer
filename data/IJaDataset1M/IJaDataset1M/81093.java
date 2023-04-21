package org.yaoqiang.graph.editor.swing;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import org.yaoqiang.graph.action.GraphActions;
import org.yaoqiang.graph.editor.action.EditorActions;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

/**
 * MainToolBar
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class MainToolBar extends JToolBar {

    private static final long serialVersionUID = -8015443128436394471L;

    private boolean ignoreZoomChange = false;

    public MainToolBar(final BaseEditor editor, int orientation) {
        super(orientation);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), getBorder()));
        setFloatable(false);
        populateFileToolbar(editor);
        addSeparator();
        add(editor.bind(mxResources.get("pageSetup"), GraphActions.getAction(GraphActions.PAGE_SETUP), "/org/yaoqiang/graph/editor/images/page_setup.png"));
        add(editor.bind(mxResources.get("print"), GraphActions.getAction(GraphActions.PRINT), "/org/yaoqiang/graph/editor/images/printer.png"));
        addSeparator();
        add(editor.bind(mxResources.get("cut"), TransferHandler.getCutAction(), "/org/yaoqiang/graph/editor/images/cut.png"));
        add(editor.bind(mxResources.get("copy"), TransferHandler.getCopyAction(), "/org/yaoqiang/graph/editor/images/copy.png"));
        add(editor.bind(mxResources.get("paste"), TransferHandler.getPasteAction(), "/org/yaoqiang/graph/editor/images/paste.png"));
        addSeparator();
        add(editor.bind(mxResources.get("delete"), GraphActions.getAction(GraphActions.DELETE), "/org/yaoqiang/graph/editor/images/delete.png"));
        addSeparator();
        add(editor.bind(mxResources.get("undo"), EditorActions.getAction(EditorActions.UNDO), "/org/yaoqiang/graph/editor/images/undo.png"));
        add(editor.bind(mxResources.get("redo"), EditorActions.getAction(EditorActions.REDO), "/org/yaoqiang/graph/editor/images/redo.png"));
        addSeparator();
        add(editor.bind(mxResources.get("grid"), EditorActions.getAction(EditorActions.GRID), "/org/yaoqiang/graph/editor/images/grid.png"));
        add(editor.bind(mxResources.get("zoomIn"), GraphActions.getAction(GraphActions.ZOOM_IN), "/org/yaoqiang/graph/editor/images/zoom_in.png"));
        add(editor.bind(mxResources.get("actualSize"), GraphActions.getAction(GraphActions.ZOOM_ACTUAL), "/org/yaoqiang/graph/editor/images/zoomactual.png"));
        add(editor.bind(mxResources.get("zoomOut"), GraphActions.getAction(GraphActions.ZOOM_OUT), "/org/yaoqiang/graph/editor/images/zoom_out.png"));
        add(editor.bind(mxResources.get("page"), GraphActions.getAction(GraphActions.ZOOM_FIT_PAGE), "/org/yaoqiang/graph/editor/images/zoomfitpage.gif"));
        add(editor.bind(mxResources.get("width"), GraphActions.getAction(GraphActions.ZOOM_FIT_WIDTH), "/org/yaoqiang/graph/editor/images/zoomfitwidth.gif"));
        final mxGraphView view = editor.getGraphComponent().getGraph().getView();
        final JComboBox zoomCombo = new JComboBox(new Object[] { "400%", "200%", "150%", "100%", "75%", "50%" });
        zoomCombo.setEditable(true);
        zoomCombo.setMinimumSize(new Dimension(65, 0));
        zoomCombo.setPreferredSize(new Dimension(65, 26));
        zoomCombo.setMaximumSize(new Dimension(65, 100));
        zoomCombo.setMaximumRowCount(9);
        add(zoomCombo);
        mxIEventListener scaleTracker = new mxIEventListener() {

            public void invoke(Object sender, mxEventObject evt) {
                ignoreZoomChange = true;
                try {
                    zoomCombo.setSelectedItem((int) Math.round(100 * view.getScale()) + "%");
                } finally {
                    ignoreZoomChange = false;
                }
            }
        };
        view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
        view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE, scaleTracker);
        scaleTracker.invoke(null, null);
        zoomCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mxGraphComponent graphComponent = editor.getGraphComponent();
                if (!ignoreZoomChange) {
                    String zoom = zoomCombo.getSelectedItem().toString();
                    if (zoom.equals(mxResources.get("page"))) {
                        graphComponent.setPageVisible(true);
                        graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
                    } else if (zoom.equals(mxResources.get("width"))) {
                        graphComponent.setPageVisible(true);
                        graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
                    } else {
                        try {
                            zoom = zoom.replace("%", "");
                            double scale = Math.min(16, Math.max(0.01, Double.parseDouble(zoom) / 100));
                            graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(editor, ex.getMessage());
                        }
                    }
                }
            }
        });
        addSeparator();
        add(editor.bind(mxResources.get("bold"), GraphActions.getFontStyleAction(mxConstants.FONT_BOLD), "/org/yaoqiang/graph/editor/images/bold.gif"));
        add(editor.bind(mxResources.get("italic"), GraphActions.getFontStyleAction(mxConstants.FONT_ITALIC), "/org/yaoqiang/graph/editor/images/italic.gif"));
        addSeparator();
        add(editor.bind(mxResources.get("left"), GraphActions.getKeyValueAction(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT), "/org/yaoqiang/graph/editor/images/left.gif"));
        add(editor.bind(mxResources.get("center"), GraphActions.getKeyValueAction(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER), "/org/yaoqiang/graph/editor/images/center.gif"));
        add(editor.bind(mxResources.get("right"), GraphActions.getKeyValueAction(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_RIGHT), "/org/yaoqiang/graph/editor/images/right.gif"));
        addSeparator();
        add(editor.bind(mxResources.get("FontColor"), GraphActions.getColorAction(mxResources.get("fontColor"), mxConstants.STYLE_FONTCOLOR), "/org/yaoqiang/graph/editor/images/fontcolor.gif"));
        add(editor.bind(mxResources.get("Stroke"), GraphActions.getColorAction(mxResources.get("stroke"), mxConstants.STYLE_STROKECOLOR), "/org/yaoqiang/graph/editor/images/linecolor.gif"));
        add(editor.bind(mxResources.get("FillColor"), GraphActions.getColorAction(mxResources.get("fillColor"), mxConstants.STYLE_FILLCOLOR), "/org/yaoqiang/graph/editor/images/fillcolor.gif"));
        addSeparator();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        List<String> fonts = new ArrayList<String>();
        fonts.addAll(Arrays.asList(new String[] { this.getFont().getFamily(), "Arial", "Helvetica", "Verdana", "Times New Roman", "Courier New", "-" }));
        fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));
        final JComboBox fontCombo = new JComboBox(fonts.toArray());
        fontCombo.setEditable(true);
        fontCombo.setMinimumSize(new Dimension(150, 0));
        fontCombo.setPreferredSize(new Dimension(150, 26));
        fontCombo.setMaximumSize(new Dimension(150, 100));
        add(fontCombo);
        fontCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String font = fontCombo.getSelectedItem().toString();
                if (font != null && !font.equals("-")) {
                    mxGraph graph = editor.getGraphComponent().getGraph();
                    graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, font);
                }
            }
        });
        final JComboBox sizeCombo = new JComboBox(new Object[] { "6pt", "8pt", "9pt", "10pt", "11pt", "12pt", "14pt", "18pt", "24pt", "30pt", "36pt", "48pt", "60pt" });
        sizeCombo.setEditable(true);
        sizeCombo.setMinimumSize(new Dimension(65, 0));
        sizeCombo.setPreferredSize(new Dimension(65, 26));
        sizeCombo.setMaximumSize(new Dimension(65, 100));
        add(sizeCombo);
        sizeCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mxGraph graph = editor.getGraphComponent().getGraph();
                graph.setCellStyles(mxConstants.STYLE_FONTSIZE, sizeCombo.getSelectedItem().toString().replace("pt", ""));
            }
        });
    }

    protected void populateFileToolbar(BaseEditor editor) {
        add(editor.bind(mxResources.get("new"), EditorActions.getAction(EditorActions.NEW), "/org/yaoqiang/graph/editor/images/new.png"));
        add(editor.bind(mxResources.get("openFile"), EditorActions.getAction(EditorActions.OPEN), "/org/yaoqiang/graph/editor/images/open.png"));
        add(editor.bind(mxResources.get("reload"), EditorActions.getAction(EditorActions.RELOAD), "/org/yaoqiang/graph/editor/images/reload.png"));
        add(editor.bind(mxResources.get("save"), EditorActions.getSaveAction(), "/org/yaoqiang/graph/editor/images/save.png"));
        add(editor.bind(mxResources.get("saveAs"), EditorActions.getSaveAsAction(), "/org/yaoqiang/graph/editor/images/save_as.png"));
        add(editor.bind(mxResources.get("saveAsPNG"), EditorActions.getSaveAsPNG(), "/org/yaoqiang/graph/editor/images/save_as_png.png"));
    }
}
