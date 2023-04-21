package edu.indiana.extreme.xbaya.gui;

import edu.indiana.extreme.xbaya.graph.DataPort;
import edu.indiana.extreme.xbaya.graph.Port;
import edu.indiana.extreme.xbaya.graph.gui.GraphCanvas;
import edu.indiana.extreme.xbaya.graph.gui.GraphCanvasEvent;
import edu.indiana.extreme.xbaya.graph.gui.GraphCanvasEvent.GraphCanvasEventType;
import edu.indiana.extreme.xbaya.graph.gui.GraphCanvasListener;
import edu.indiana.extreme.xbaya.util.SwingUtil;
import xsul5.XmlConstants;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author Satoshi Shirasuna
 */
public class PortViewer implements GraphCanvasListener {

    /**
     * The title.
     */
    public static final String TITLE = "Parameters";

    private JPanel panel;

    private JEditorPane outputEditor;

    private JEditorPane inputEditor;

    /**
     * Creates a PortInformation
     */
    public PortViewer() {
        this.panel = new JPanel();
        this.panel.setMinimumSize(SwingUtil.MINIMUM_SIZE);
        this.panel.setPreferredSize(new Dimension(0, 150));
        JPanel inBox = new JPanel(new BorderLayout());
        inBox.setBorder(new TitledBorder(new EtchedBorder(), "Input Parameter"));
        this.inputEditor = createEditorPane();
        JScrollPane inputScrollPane = new JScrollPane(this.inputEditor);
        inputScrollPane.setMinimumSize(SwingUtil.MINIMUM_SIZE);
        inBox.add(inputScrollPane, BorderLayout.CENTER);
        inBox.setMinimumSize(SwingUtil.MINIMUM_SIZE);
        JPanel outBox = new JPanel(new BorderLayout());
        outBox.setBorder(new TitledBorder(new EtchedBorder(), "Output Parameter"));
        this.outputEditor = createEditorPane();
        JScrollPane outScrollPane = new JScrollPane(this.outputEditor);
        outScrollPane.setMinimumSize(SwingUtil.MINIMUM_SIZE);
        outBox.add(outScrollPane, BorderLayout.CENTER);
        outBox.setMinimumSize(SwingUtil.MINIMUM_SIZE);
        this.panel.setLayout(new GridLayout(1, 2));
        this.panel.add(outBox);
        this.panel.add(inBox);
    }

    /**
     * @return The panel
     */
    public JComponent getSwingComponent() {
        return this.panel;
    }

    /**
     * @param port
     */
    public void setOutputPort(Port port) {
        showPortInfo(this.outputEditor, port);
    }

    /**
     * @param port
     */
    public void setInputPort(Port port) {
        showPortInfo(this.inputEditor, port);
    }

    /**
     * @see edu.indiana.extreme.xbaya.graph.gui.GraphCanvasListener#graphCanvasChanged(edu.indiana.extreme.xbaya.graph.gui.GraphCanvasEvent)
     */
    public void graphCanvasChanged(GraphCanvasEvent event) {
        GraphCanvasEventType type = event.getType();
        GraphCanvas graphCanvas = event.getGraphCanvas();
        switch(type) {
            case INPUT_PORT_SELECTED:
                Port inputPort = graphCanvas.getSelectedInputPort();
                setInputPort(inputPort);
                break;
            case OUTPUT_PORT_SELECTED:
                Port outputPort = graphCanvas.getSelectedOutputPort();
                setOutputPort(outputPort);
                break;
            case GRAPH_LOADED:
            case NAME_CHANGED:
            case NODE_SELECTED:
        }
    }

    /**
     * Shows the information of a selected port on the list specified.
     * 
     * @param editor
     * @param port
     */
    private void showPortInfo(JEditorPane editor, Port port) {
        if (port == null) {
            editor.setText("");
        } else {
            StringBuilder buf = new StringBuilder();
            buf.append("<strong>Component: " + port.getNode().getName() + "</strong><br>");
            buf.append("<strong>Port: " + port.getName() + "<br></strong>");
            if (port instanceof DataPort) {
                buf.append("<strong>Type</strong>: " + ((DataPort) port).getType() + "<br>");
            } else {
            }
            buf.append("<strong>Description</strong>: " + port.getComponentPort().getDescription() + "<br>");
            editor.setText(buf.toString());
            editor.setCaretPosition(0);
        }
    }

    private JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setMinimumSize(SwingUtil.MINIMUM_SIZE);
        editorPane.setEditable(false);
        editorPane.setBackground(Color.WHITE);
        editorPane.setContentType(XmlConstants.CONTENT_TYPE_HTML);
        return editorPane;
    }
}
