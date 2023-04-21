package net.sourceforge.ondex.ovtk2.annotator.knockout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.ovtk2.annotator.OVTK2Annotator;
import net.sourceforge.ondex.ovtk2.config.Config;
import net.sourceforge.ondex.ovtk2.graph.ONDEXJUNGGraph;
import net.sourceforge.ondex.ovtk2.graph.ONDEXNodeShapes;
import net.sourceforge.ondex.ovtk2.ui.OVTK2Viewer;
import net.sourceforge.ondex.ovtk2.util.SpringUtilities;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import edu.uci.ics.jung.visualization.picking.PickedState;

/**
 * Performs node and edge knock outs to derive influence the connectivity matrix
 * of the graph.
 * 
 * @author taubertj
 * @version 27.03.2008
 */
public class KnockOutAnnotator extends OVTK2Annotator implements ActionListener, ListSelectionListener {

    private static final boolean DEBUG = false;

    private static final long serialVersionUID = 1L;

    private Set<Integer> finishedSP;

    private JTable table;

    private JFormattedTextField minField, maxField;

    private Map<ONDEXConcept, Double> map;

    /**
	 * Annotator has been used
	 */
    private boolean used = false;

    /**
	 * Calculates reachability matrices
	 * 
	 * @param viewer
	 *            OVTK2Viewer
	 */
    public KnockOutAnnotator(OVTK2Viewer viewer) {
        super(viewer);
        setLayout(new SpringLayout());
        map = new HashMap<ONDEXConcept, Double>();
        long start = System.currentTimeMillis();
        Map<Integer, Set<Integer>> neighbors = directNeighbors();
        System.out.println("Neighbors took: " + (System.currentTimeMillis() - start) + " msec.");
        if (DEBUG) System.out.println(neighbors);
        finishedSP = new HashSet<Integer>();
        start = System.currentTimeMillis();
        Map<Integer, Map<Integer, Integer>> matrix = new HashMap<Integer, Map<Integer, Integer>>();
        int diameter = constructMatrix(neighbors, matrix, 0);
        System.out.println("Matrix took: " + (System.currentTimeMillis() - start) + " msec.");
        System.out.println("Diameter: " + diameter);
        int connected = indentifyConnectedComponents(neighbors, matrix, matrix, -1);
        System.out.println("Connected components: " + connected);
        if (DEBUG) System.out.println(matrix);
        String[] columnNames = { "cid", "concept name", "extension", "deletion", "nochange", "score", "diameter", "components" };
        Object[][] data = new Object[matrix.keySet().size()][columnNames.length];
        start = System.currentTimeMillis();
        Integer[] keys = matrix.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            int node = keys[i];
            Map<Integer, Set<Integer>> newneighbors = directNeighbors(neighbors, node);
            if (DEBUG) {
                System.out.print("Knockout: " + node + " ");
                System.out.println(newneighbors);
            }
            Iterator<Integer> itn = matrix.keySet().iterator();
            Set<Integer> influenced = matrix.get(node).keySet();
            while (itn.hasNext()) {
                int key = itn.next();
                if (influenced.contains(key)) finishedSP.remove(key); else finishedSP.add(key);
            }
            Map<Integer, Map<Integer, Integer>> newmatrix = new HashMap<Integer, Map<Integer, Integer>>();
            diameter = constructMatrix(newneighbors, newmatrix, 0);
            if (DEBUG) {
                System.out.println("Diameter: " + diameter);
                System.out.println(newmatrix);
            }
            connected = indentifyConnectedComponents(newneighbors, matrix, newmatrix, node);
            int[] result = compareMatrix(matrix, newmatrix, node);
            if (DEBUG) System.out.println("Extensions: " + result[0] + " Deletions: " + result[1] + " NoChange: " + result[2]);
            ONDEXConcept c = graph.getConcept(Integer.valueOf(node));
            data[i][0] = c.getId();
            if (c.getConceptName() != null) data[i][1] = c.getConceptName().getName(); else data[i][1] = "";
            data[i][2] = Integer.valueOf(result[0]);
            data[i][3] = Integer.valueOf(result[1]);
            data[i][4] = Integer.valueOf(result[2]);
            double score = ((double) (result[0] + result[1])) / ((double) result[0] + result[1] + result[2]);
            map.put(c, score);
            data[i][5] = Double.valueOf(score);
            data[i][6] = Integer.valueOf(diameter);
            data[i][7] = Integer.valueOf(connected);
        }
        System.out.println("KnockOut took: " + (System.currentTimeMillis() - start) + " msec.");
        table = new JTable(new ScoreTableModel(data, columnNames));
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);
        table.setAutoCreateRowSorter(true);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        Comparator<Integer> intComparator = new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                if (o1.equals(o2)) return 0; else if (o1 < o2) return -1; else return 1;
            }
        };
        sorter.setComparator(2, intComparator);
        sorter.setComparator(3, intComparator);
        sorter.setComparator(4, intComparator);
        sorter.setComparator(6, intComparator);
        sorter.setComparator(7, intComparator);
        sorter.setComparator(5, new Comparator<Double>() {

            public int compare(Double o1, Double o2) {
                if (o1.equals(o2)) return 0; else if (o1 < o2) return -1; else return 1;
            }
        });
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        add(scrollPane);
        JPanel sizeConstraints = new JPanel(new SpringLayout());
        sizeConstraints.add(new JLabel("Min Node Size"));
        minField = new JFormattedTextField(10);
        minField.setColumns(5);
        sizeConstraints.add(minField);
        sizeConstraints.add(new JLabel("Max Node Size"));
        maxField = new JFormattedTextField(30);
        maxField.setColumns(5);
        sizeConstraints.add(maxField);
        SpringUtilities.makeCompactGrid(sizeConstraints, sizeConstraints.getComponentCount() / 2, 2, 5, 5, 5, 5);
        add(sizeConstraints);
        JButton exportButton = new JButton("Export results");
        exportButton.addActionListener(this);
        exportButton.setActionCommand("export");
        add(exportButton);
        JButton goButton = new JButton("Annotate Graph");
        goButton.addActionListener(this);
        goButton.setActionCommand("go");
        add(goButton);
        SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, 5, 5, 5, 5);
    }

    @Override
    public String getName() {
        return Config.language.getProperty("Name.Menu.Annotator.KnockOut");
    }

    /**
	 * Propagate table selection to selection of nodes in the graph.
	 */
    public void valueChanged(ListSelectionEvent e) {
        ONDEXJUNGGraph graph = viewer.getONDEXJUNGGraph();
        PickedState<ONDEXConcept> state = viewer.getVisualizationViewer().getPickedVertexState();
        state.clear();
        int[] selection = table.getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            int index = table.convertRowIndexToModel(selection[i]);
            Integer id = (Integer) table.getModel().getValueAt(index, 0);
            ONDEXConcept node = graph.getConcept(id);
            state.pick(node, true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("go")) {
            Integer min = (Integer) minField.getValue();
            Integer max = (Integer) maxField.getValue();
            if (min < 5) {
                min = 5;
                minField.setValue(min);
            }
            if (min > 45) {
                min = 45;
                minField.setValue(min);
            }
            if (max < 5) {
                max = 5;
                maxField.setValue(max);
            }
            if (max > 45) {
                max = 45;
                maxField.setValue(max);
            }
            if (min > max) {
                Integer temp = min;
                min = max;
                max = temp;
                minField.setValue(min);
                maxField.setValue(max);
            }
            ONDEXNodeShapes nodeShapes = viewer.getNodeShapes();
            final Map<ONDEXConcept, Integer> amplifications = resizeNodes(min, max, map);
            nodeShapes.setNodeSizes(new Transformer<ONDEXConcept, Integer>() {

                @Override
                public Integer transform(ONDEXConcept input) {
                    return amplifications.get(input);
                }
            });
            nodeShapes.updateAll();
            used = true;
        } else if (cmd.equals("export")) {
            try {
                String excelFileName = null;
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) return true;
                        String ext = f.getAbsolutePath();
                        if (ext.lastIndexOf('.') > -1) {
                            ext = ext.substring(ext.lastIndexOf('.') + 1, ext.length());
                            if (ext.equals("xls")) return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return ".xls Excel Files";
                    }
                });
                int returnVal = chooser.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    excelFileName = chooser.getSelectedFile().getAbsolutePath();
                }
                if (excelFileName != null) {
                    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    String excelDatabase = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=";
                    excelDatabase += excelFileName.trim() + ";DriverID=790;READONLY=false}";
                    Connection excelCon = DriverManager.getConnection(excelDatabase, "", "");
                    if (!excelCon.isClosed()) System.out.println("Successfully Connected To Excel for " + excelFileName);
                    Statement excelStat;
                    excelStat = excelCon.createStatement();
                    excelStat.executeUpdate("CREATE TABLE knockout (cid NUMBER, concept_name TEXT, " + "extension NUMBER, deletion NUMBER, nochange NUMBER, score NUMBER, diameter NUMBER, components NUMBER)");
                    for (int rowI = 0; rowI < table.getRowCount(); rowI++) {
                        String sqlInsert = "INSERT INTO [knockout$] (cid, concept_name, " + "extension, deletion, nochange, score, diameter, components) VALUES (" + table.getValueAt(rowI, 0) + ", '" + table.getValueAt(rowI, 1) + "'," + table.getValueAt(rowI, 2) + ", " + table.getValueAt(rowI, 3) + ", " + table.getValueAt(rowI, 4) + ", " + table.getValueAt(rowI, 5) + ", " + table.getValueAt(rowI, 6) + ", " + table.getValueAt(rowI, 7) + ")";
                        if (DEBUG) System.out.println(sqlInsert);
                        excelStat.executeUpdate(sqlInsert);
                    }
                    excelStat.close();
                    excelCon.commit();
                    excelCon.close();
                }
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    /**
	 * Calculates amplification of the nodes according to specified values.
	 * 
	 * @param targMin
	 *            minimum node size
	 * @param targMax
	 *            maximum node size
	 * @param map
	 *            Map<ONDEXConcept, Double>
	 * @return Map<ONDEXConcept, Integer>
	 */
    private Map<ONDEXConcept, Integer> resizeNodes(int targMin, int targMax, Map<ONDEXConcept, Double> map) {
        int sizeRange = targMax - targMin;
        Map<ONDEXConcept, Integer> amplification = LazyMap.decorate(new HashMap<ONDEXConcept, Integer>(), new Factory<Integer>() {

            @Override
            public Integer create() {
                return Config.defaultNodeSize;
            }
        });
        for (ONDEXConcept c : map.keySet()) {
            double percentBase = map.get(c);
            if (percentBase == 0) {
                amplification.put(c, (int) Math.floor(targMin + (sizeRange / 2)));
            }
            double width = targMin + (percentBase * sizeRange);
            amplification.put(c, (int) Math.floor(width));
        }
        return amplification;
    }

    /**
	 * Returns total number of connected components.
	 * 
	 * @param neighbors
	 *            neighbors for all nodes
	 * @param matrix
	 *            original reachability matrix
	 * @param newmatrix
	 *            after knocked out matrix
	 * @param node
	 *            knocked out node
	 * @return number of connected components
	 */
    private int indentifyConnectedComponents(Map<Integer, Set<Integer>> neighbors, Map<Integer, Map<Integer, Integer>> matrix, Map<Integer, Map<Integer, Integer>> newmatrix, int node) {
        int components = 0;
        Set<Integer> seen = new HashSet<Integer>();
        if (node > -1 && matrix.containsKey(node)) {
            Iterator<Integer> it = matrix.get(node).keySet().iterator();
            while (it.hasNext()) {
                int n = it.next();
                if (!seen.contains(n)) {
                    seen.add(n);
                    components++;
                    if (newmatrix.containsKey(n)) {
                        Iterator<Integer> itn = newmatrix.get(n).keySet().iterator();
                        while (itn.hasNext()) {
                            int nn = itn.next();
                            seen.add(nn);
                        }
                    }
                }
            }
        } else if (node == -1) {
            Iterator<Integer> it = neighbors.keySet().iterator();
            while (it.hasNext()) {
                int n = it.next();
                if (!seen.contains(n)) {
                    seen.add(n);
                    components++;
                    if (matrix.containsKey(n)) {
                        Iterator<Integer> itn = matrix.get(n).keySet().iterator();
                        while (itn.hasNext()) {
                            int nn = itn.next();
                            seen.add(nn);
                        }
                    }
                }
            }
        }
        return components;
    }

    /**
	 * Returns the comparison of two reachability matrices.
	 * 
	 * @param matrix
	 *            original matrix
	 * @param newmatrix
	 *            changed matrix
	 * @param node
	 *            knocked out node
	 * @return comparison values
	 */
    private int[] compareMatrix(Map<Integer, Map<Integer, Integer>> matrix, Map<Integer, Map<Integer, Integer>> newmatrix, int node) {
        int extensions = 0;
        int deletions = 0;
        int nochange = 0;
        Iterator<Integer> itn = matrix.get(node).keySet().iterator();
        while (itn.hasNext()) {
            int key = itn.next();
            Map<Integer, Integer> original = matrix.get(key);
            Map<Integer, Integer> changed = newmatrix.get(key);
            Iterator<Integer> ito = original.keySet().iterator();
            while (ito.hasNext()) {
                int o = ito.next();
                if (changed == null) {
                    deletions++;
                } else if (!changed.containsKey(o)) {
                    deletions++;
                } else {
                    if (changed.get(o) == original.get(o)) {
                        nochange++;
                    } else {
                        extensions++;
                    }
                }
            }
        }
        return new int[] { extensions, deletions, nochange };
    }

    /**
	 * Copies neighbours except the node specified.
	 * 
	 * @param neighbors
	 *            original set of neighbours
	 * @param node
	 *            knocked out node
	 * @return set of neighbours without knocked out node
	 */
    private Map<Integer, Set<Integer>> directNeighbors(Map<Integer, Set<Integer>> neighbors, int node) {
        Map<Integer, Set<Integer>> newneighbors = new HashMap<Integer, Set<Integer>>();
        Iterator<Integer> it = neighbors.keySet().iterator();
        while (it.hasNext()) {
            int id = it.next();
            if (id != node) {
                Set<Integer> temp = new HashSet<Integer>();
                Iterator<Integer> itn = neighbors.get(id).iterator();
                while (itn.hasNext()) {
                    int n = itn.next();
                    if (n != node) temp.add(n);
                }
                newneighbors.put(id, temp);
            }
        }
        return newneighbors;
    }

    /**
	 * Constructs lists of direct neighbours for each concept.
	 * 
	 * @return set of neighbours for each concept
	 */
    private Map<Integer, Set<Integer>> directNeighbors() {
        Map<Integer, Set<Integer>> neighbors = new HashMap<Integer, Set<Integer>>();
        for (ONDEXConcept c : graph.getConcepts()) {
            Integer cid = c.getId();
            Set<Integer> temp = new HashSet<Integer>();
            neighbors.put(cid, temp);
            for (ONDEXRelation r : graph.getRelationsOfConcept(c)) {
                ONDEXConcept from = r.getFromConcept();
                ONDEXConcept to = r.getToConcept();
                if (from.equals(to)) continue;
                if (from.equals(c)) temp.add(to.getId()); else temp.add(from.getId());
            }
        }
        return neighbors;
    }

    /**
	 * Finds all pairs shortest paths based on concept ids.
	 * 
	 * @param neighbors
	 *            set of neighbours for each concept
	 * @param matrix
	 *            previously empty distance matrix
	 * @param depth
	 *            current recursion depth
	 * @return diameter of graph (length of longest shortest path)
	 */
    private int constructMatrix(Map<Integer, Set<Integer>> neighbors, Map<Integer, Map<Integer, Integer>> matrix, int depth) {
        depth++;
        boolean change = false;
        Iterator<Integer> it = neighbors.keySet().iterator();
        while (it.hasNext()) {
            int cid = it.next();
            Set<Integer> neighborsCid = neighbors.get(cid);
            if (!finishedSP.contains(cid) && neighborsCid.size() > 0) {
                if (!matrix.containsKey(cid)) {
                    Map<Integer, Integer> matrixTemp = new HashMap<Integer, Integer>();
                    matrix.put(cid, matrixTemp);
                    Iterator<Integer> itn = neighborsCid.iterator();
                    while (itn.hasNext()) {
                        int nid = itn.next();
                        matrixTemp.put(nid, depth);
                    }
                    change = true;
                } else {
                    Map<Integer, Integer> matrixCid = matrix.get(cid);
                    boolean localchange = false;
                    for (Integer nid : matrixCid.keySet()) {
                        Iterator<Integer> itnn = neighbors.get(nid).iterator();
                        while (itnn.hasNext()) {
                            int nnid = itnn.next();
                            if (nnid != cid) {
                                if (!matrixCid.containsKey(nnid)) {
                                    matrixCid.put(nnid, depth);
                                    change = true;
                                    localchange = true;
                                }
                            }
                        }
                    }
                    if (!localchange) finishedSP.add(cid);
                }
            }
        }
        if (!change) return 0; else return 1 + constructMatrix(neighbors, matrix, depth);
    }

    /**
	 * Simple model, that is not edit able.
	 * 
	 * @author taubertj
	 * @version 26.03.2008
	 */
    private class ScoreTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        private String[] columnNames = null;

        private Object[][] data = null;

        public ScoreTableModel(Object[][] data, String[] columnNames) {
            this.columnNames = columnNames;
            this.data = data;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    @Override
    public boolean hasBeenUsed() {
        return used;
    }
}
