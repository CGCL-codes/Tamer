package gis.vizualization.gui;

import gis.utilz.GISConstants;
import java.io.File;
import java.util.NoSuchElementException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.io.GraphMLReader;
import prefuse.data.io.GraphMLWriter;

public class GraphEditing {

    private Graph currentGraph;

    private GisProject mainApplication;

    private GisProject getMainApplication() {
        return mainApplication;
    }

    private void setMainApplication(GisProject mainApplication) {
        this.mainApplication = mainApplication;
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }

    public void setCurrentGraph(Graph currentGraph) {
        this.currentGraph = currentGraph;
    }

    public GraphEditing(Graph g) {
        this.setCurrentGraph(g);
    }

    public GraphEditing(GisProject g) {
        this.setMainApplication(g);
    }

    public boolean AddNode(String sNewNodeValue) {
        Node nodeTest;
        boolean ifExist = true;
        boolean bSucess = false;
        try {
            nodeTest = (Node) currentGraph.tuples(ExpressionParser.predicate(GISConstants.NodeDataName + " == '" + sNewNodeValue + "'")).next();
        } catch (NoSuchElementException e) {
            ifExist = false;
        }
        if (sNewNodeValue.equals("")) {
            JOptionPane.showMessageDialog(null, "String given is empty !!!", "Warning", JOptionPane.WARNING_MESSAGE);
            System.out.println("Podany zosta pusty String !!!");
        } else if (!ifExist) {
            Node newNode = currentGraph.addNode();
            newNode.setString(GISConstants.NodeDataName, sNewNodeValue);
            bSucess = true;
        } else {
            System.out.println("Taki wezel juz istnieje !");
            JOptionPane.showMessageDialog(null, "Node already exists !!!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return bSucess;
    }

    public boolean AddEdege(String sNewEdgeValue, String sNodeStart, String sNodeEnd) {
        Node nodeStart;
        Node nodeEnd;
        boolean bSucess = false;
        try {
            nodeStart = (Node) currentGraph.tuples(ExpressionParser.predicate(GISConstants.NodeDataName + " == '" + sNodeStart + "'")).next();
            nodeEnd = (Node) currentGraph.tuples(ExpressionParser.predicate(GISConstants.NodeDataName + " == '" + sNodeEnd + "'")).next();
            try {
                Integer.parseInt(sNewEdgeValue);
            } catch (NumberFormatException e) {
                System.out.println("Waga nie jest liczb�");
                JOptionPane.showMessageDialog(null, "Edge weight should be a digit !!!", "Warning", JOptionPane.WARNING_MESSAGE);
                return bSucess;
            }
            if (currentGraph.getEdge(nodeStart, nodeEnd) == null) {
                Edge newEdge = currentGraph.addEdge(nodeStart, nodeEnd);
                newEdge.setString(GISConstants.EdgeValDataName, sNewEdgeValue);
                newEdge.setString(GISConstants.ChosenDataName, GISConstants.ChosenFalse);
                bSucess = true;
            } else {
                System.out.println("Taka krawedz juz istnieje !");
                JOptionPane.showMessageDialog(null, "Edge already exist !!!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Given node(s) dont exist !!!", "Warning", JOptionPane.WARNING_MESSAGE);
            System.out.println("Nie ma takiego wezla poczatkowego/koncowego !");
        }
        return bSucess;
    }

    public boolean LoadGraphXML() {
        boolean bSuccess = false;
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new XmlFileFilter());
        chooser.setApproveButtonText("Load Graph");
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName() + chooser.getSelectedFile().getPath());
            Graph g = null;
            String datafile = chooser.getSelectedFile().getPath();
            try {
                g = new GraphMLReader().readGraph(datafile);
                this.setCurrentGraph(g);
                bSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("B��d podczas wczytywania pliku !!!");
                JOptionPane.showMessageDialog(null, "Error while opening file !!!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        return bSuccess;
    }

    public boolean SaveGraphXML() {
        boolean bSuccess = false;
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new XmlFileFilter());
        chooser.setApproveButtonText("Save Graph");
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to save this file: " + chooser.getSelectedFile().getName() + chooser.getSelectedFile().getPath());
            String datafile = null;
            String extension = this.getExtension(chooser.getSelectedFile());
            if (extension.equals("xml")) {
                datafile = chooser.getSelectedFile().getPath();
                System.out.println("nie dodaje konc�wki: " + datafile);
            } else if (extension.equals("")) {
                datafile = chooser.getSelectedFile().getPath() + ".xml";
                System.out.println("dodaje konc�wke: " + datafile);
            } else {
                System.out.println("Niepoprawne rozszerzenie !!");
                JOptionPane.showMessageDialog(null, "Inproper file extension !!!", "Warning", JOptionPane.WARNING_MESSAGE);
                return bSuccess;
            }
            try {
                File outFile = new File(datafile);
                Graph gSave = this.getCurrentGraph();
                GraphMLWriter writer = new GraphMLWriter();
                writer.writeGraph(gSave, outFile);
                bSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("B��d podczas zapisywania pliku !!!");
                JOptionPane.showMessageDialog(null, "Error while saving file !!!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        return bSuccess;
    }

    private String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
