package net.sourceforge.ondex.ovtk2lite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import net.sourceforge.ondex.core.Attribute;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXGraphMetaData;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.memory.MemoryONDEXGraph;
import net.sourceforge.ondex.ovtk2.config.Config;
import net.sourceforge.ondex.ovtk2.graph.ONDEXEdgeArrows;
import net.sourceforge.ondex.ovtk2.graph.ONDEXEdgeColors;
import net.sourceforge.ondex.ovtk2.graph.ONDEXEdgeLabels;
import net.sourceforge.ondex.ovtk2.graph.ONDEXEdgeShapes;
import net.sourceforge.ondex.ovtk2.graph.ONDEXEdgeStrokes;
import net.sourceforge.ondex.ovtk2.graph.ONDEXJUNGGraph;
import net.sourceforge.ondex.ovtk2.graph.ONDEXNodeDrawPaint;
import net.sourceforge.ondex.ovtk2.graph.ONDEXNodeFillPaint;
import net.sourceforge.ondex.ovtk2.graph.ONDEXNodeLabels;
import net.sourceforge.ondex.ovtk2.graph.ONDEXNodeShapes;
import net.sourceforge.ondex.ovtk2.io.OXLImport;
import net.sourceforge.ondex.ovtk2.io.WebserviceImport;
import net.sourceforge.ondex.ovtk2.layout.ConceptClassCircleLayout;
import net.sourceforge.ondex.ovtk2.layout.GEMLayout;
import net.sourceforge.ondex.ovtk2.layout.OVTK2Layouter;
import net.sourceforge.ondex.ovtk2.ui.contentsdisplay.ContentsDisplay;
import net.sourceforge.ondex.ovtk2.ui.toolbars.MenuGraphSearchBox;
import net.sourceforge.ondex.ovtk2lite.search.SearchBoxAction;
import net.sourceforge.ondex.webservice.client.WSGraph;
import net.sourceforge.ondex.webservice.client.WebserviceException_Exception;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * Wrap loading of data files into worker thread.
 * 
 * @author taubertj
 * 
 */
public class InitGraphWorker extends SwingWorker<Boolean, Void> {

    private final Main main;

    private GraphZoomScrollPane scrollPane;

    public InitGraphWorker(Main main) {
        this.main = main;
    }

    @Override
    protected Boolean doInBackground() {
        URL url = null;
        WSGraph wsgraph = null;
        if (main.getParameter("webservice") != null) {
            try {
                url = new URL(main.getParameter("webservice"));
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(main, e.getMessage(), "Error while connecting to webservice.", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (url != null) {
                try {
                    for (WSGraph g : WebserviceImport.getGraphs(url)) {
                        if (g.getName().getValue().equalsIgnoreCase(main.getParameter("graphname"))) {
                            wsgraph = g;
                            break;
                        }
                    }
                } catch (WebserviceException_Exception e) {
                    JOptionPane.showMessageDialog(main, e.getMessage(), "Error while connecting to webservice.", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        if (main.getParameter("ondex.dir") == null || main.getParameter("ovtk.dir") == null) {
            JOptionPane.showMessageDialog(main, "ondex.dir or ovtk.dir not set!");
            return false;
        }
        main.getStatusLabel().setText("Initialising config...");
        net.sourceforge.ondex.config.Config.ondexDir = main.getParameter("ondex.dir");
        net.sourceforge.ondex.ovtk2.config.Config.ovtkDir = main.getParameter("ovtk.dir");
        try {
            net.sourceforge.ondex.config.Config.loadConfig();
            net.sourceforge.ondex.ovtk2.config.Config.loadConfig(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(main, e.getMessage(), "Error while initialising config.", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        LiteMenuBar menuBar = new LiteMenuBar(main);
        main.setJMenuBar(menuBar);
        main.getStatusLabel().setText("Starting import...");
        ONDEXGraph aog = new MemoryONDEXGraph("ONDEX Graph");
        String filename = main.getParameter("filename");
        if (filename != null) {
            try {
                OXLImport imp = new OXLImport(aog, filename, true);
                imp.start();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(main, e.getMessage(), "Error while loading from filename.", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if (url != null && wsgraph != null) {
            try {
                new WebserviceImport(aog, url, wsgraph);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(main, e.getMessage(), "Error while loading from webservice.", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (Main.DEBUG) {
            System.out.println("----- Configuration ONDEX: -----");
            Object[] keys = net.sourceforge.ondex.config.Config.properties.keySet().toArray();
            Arrays.sort(keys);
            for (Object key : keys) {
                System.out.println(key + " = " + net.sourceforge.ondex.config.Config.properties.get(key));
            }
            System.out.println("----- Configuration OVTK2: -----");
            keys = net.sourceforge.ondex.ovtk2.config.Config.config.keySet().toArray();
            Arrays.sort(keys);
            for (Object key : keys) {
                System.out.println(key + " = " + net.sourceforge.ondex.ovtk2.config.Config.config.get(key));
            }
        }
        main.getStatusLabel().setText("Constructing viewer...");
        if (aog.getConcepts().size() > 0) System.out.println("Loading of " + filename != null ? filename : wsgraph.getName() + " successful.");
        ContentsDisplay contentsDisplay = new ContentsDisplay(aog, new ActivatedHyperlinkListener(menuBar.getContentsDisplayFrame()), menuBar.getContentsDisplayFrame());
        menuBar.setContentsDisplay(contentsDisplay);
        ONDEXJUNGGraph graph = new ONDEXJUNGGraph(aog);
        main.setONDEXJUNGGraph(graph);
        if (aog.getConcepts().size() > 2000 || aog.getRelations().size() > 2000) {
            int option = JOptionPane.showConfirmDialog(main, "The network does contain more than 2000 nodes or edges." + "\nIt is recommend to not display all, but use the search function for information extraction." + "\nDo you wish to start with an empty / partial display?", "Network size warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.NO_OPTION) graph.setEverythingVisible();
        }
        VisualizationViewer<ONDEXConcept, ONDEXRelation> visviewer = new VisualizationViewer<ONDEXConcept, ONDEXRelation>(new ConceptClassCircleLayout(null, graph), new Dimension(800, 600));
        main.setVisualizationViewer(visviewer);
        visviewer.setBackground(Color.white);
        visviewer.setDoubleBuffered(true);
        visviewer.addKeyListener(new LiteKeyListener(main));
        main.getStatusLabel().setText("Setting graph layout...");
        loadVisibility(graph);
        if (main.getParameter("layout") != null) {
            String className = main.getParameter("layout");
            try {
                Class<?> clazz = Class.forName(className);
                Constructor<?> constr = clazz.getConstructor(VisualizationViewer.class, ONDEXJUNGGraph.class);
                main.setLayouter((OVTK2Layouter) constr.newInstance(visviewer, graph));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(main, e.getMessage(), "Error while setting graph layout. Reverting to default.", JOptionPane.WARNING_MESSAGE);
                main.setLayouter(new GEMLayout(visviewer, graph));
            }
        } else {
            main.setLayouter(new GEMLayout(visviewer, graph));
        }
        JScrollPane scroll = new JScrollPane(main.getLayouter().getOptionPanel());
        scroll.setPreferredSize(new Dimension(280, 210));
        JButton button = new JButton(Config.language.getProperty("Options.Relayout"));
        button.addActionListener(new LiteActionListener(main));
        JFrame frame = menuBar.getLayoutOptionsFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        frame.getContentPane().add(button, BorderLayout.SOUTH);
        frame.pack();
        main.getStatusLabel().setText("Configuring appearance...");
        visviewer.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);
        ONDEXNodeLabels nodeLabels = new ONDEXNodeLabels(true);
        main.setNodeLabels(nodeLabels);
        ONDEXEdgeLabels edgeLabels = new ONDEXEdgeLabels();
        main.setEdgeLabels(edgeLabels);
        ONDEXNodeShapes nodeShapes = new ONDEXNodeShapes();
        main.setNodeShapes(nodeShapes);
        ONDEXEdgeShapes edgeShapes = new ONDEXEdgeShapes();
        main.setEdgeShapes(edgeShapes);
        ONDEXNodeFillPaint nodeColors = new ONDEXNodeFillPaint(visviewer.getPickedVertexState());
        main.setNodeColors(nodeColors);
        ONDEXNodeDrawPaint nodeDrawPaint = new ONDEXNodeDrawPaint();
        main.setNodeDrawPaint(nodeDrawPaint);
        ONDEXEdgeColors edgeColors = new ONDEXEdgeColors(visviewer.getPickedEdgeState());
        main.setEdgeColors(edgeColors);
        ONDEXEdgeStrokes edgeStrokes = new ONDEXEdgeStrokes();
        main.setEdgeStrokes(edgeStrokes);
        ONDEXEdgeArrows edgeArrows = new ONDEXEdgeArrows();
        main.setEdgeArrows(edgeArrows);
        Map<?, ?> temp = visviewer.getRenderingHints();
        Iterator<?> it = temp.keySet().iterator();
        while (it.hasNext()) {
            RenderingHints.Key key = (RenderingHints.Key) it.next();
            main.getHints().put(key, temp.get(key));
        }
        main.getHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        visviewer.setRenderingHints(main.getHints());
        DefaultModalGraphMouse<ONDEXConcept, ONDEXRelation> graphMouse = new LiteDefaultModalGraphMouse(main);
        ((LiteDefaultModalGraphMouse) graphMouse).getOVTK2PickingMousePlugin().addPickingListener(contentsDisplay);
        visviewer.setGraphMouse(graphMouse);
        visviewer.addKeyListener(graphMouse.getModeKeyListener());
        main.configure();
        main.getStatusLabel().setText("Adding to applet...");
        scrollPane = new GraphZoomScrollPane(visviewer);
        JMenuBar menu = new JMenuBar();
        menu.add(graphMouse.getModeMenu());
        scrollPane.setCorner(menu);
        graphMouse.setMode(Mode.PICKING);
        SearchBoxAction actionListener = new SearchBoxAction(main);
        MenuGraphSearchBox searchBox = new MenuGraphSearchBox();
        searchBox.updateRestrictions(main);
        searchBox.addActionListener(actionListener);
        main.setSearchBox(searchBox);
        return true;
    }

    /**
	 * Sets visibility according to attributes
	 * 
	 * @param graph
	 */
    private void loadVisibility(ONDEXJUNGGraph graph) {
        ONDEXGraphMetaData meta = graph.getMetaData();
        AttributeName attrVisible;
        if ((attrVisible = meta.getAttributeName("visible")) != null) {
            for (ONDEXConcept c : graph.getConcepts()) {
                Attribute attribute = c.getAttribute(attrVisible);
                if (attribute != null) graph.setVisibility(c, ((Boolean) attribute.getValue()).booleanValue()); else graph.setVisibility(c, false);
            }
            for (ONDEXRelation r : graph.getRelations()) {
                Attribute attribute = r.getAttribute(attrVisible);
                if (attribute != null) graph.setVisibility(r, ((Boolean) attribute.getValue()).booleanValue()); else graph.setVisibility(r, false);
            }
        }
    }

    public void done() {
        try {
            if (get()) {
                main.getContentPane().removeAll();
                main.setSize(scrollPane.getPreferredSize());
                main.setLayout(new BorderLayout());
                main.add(scrollPane, BorderLayout.CENTER);
                main.add(main.getSearchBox(), BorderLayout.SOUTH);
                main.getSearchBox().setVisible(false);
                main.validate();
                main.setUseEntitySizes(true);
                main.loadAppearance();
                main.center();
                main.invalidate();
                main.validate();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
