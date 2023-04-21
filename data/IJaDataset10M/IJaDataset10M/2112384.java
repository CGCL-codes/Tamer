package za.co.data.util;

import org.jgraph.graph.*;
import za.co.data.framework.ClassUtils;
import za.co.data.framework.modler.ModlerUI;
import za.co.data.framework.ui.components.MappingEdge;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author darryl
 */
public class ModlerTools {

    /**
     * Create jGraph cells for specified classes.
     *
     * @param persistentClasses
     * @return
     */
    public static Map<Class, DefaultGraphCell> createCells(Collection<String> persistentClasses) {
        int lastX = 10;
        int lastY = 10;
        Map<Class, DefaultGraphCell> cellClassMap = new HashMap<Class, DefaultGraphCell>();
        for (String s : persistentClasses) {
            try {
                Class tableClass = ClassUtils.loadClass(s);
                DefaultGraphCell cell = new DefaultGraphCell(tableClass.getSimpleName());
                cell.getAttributes().applyValue("cell.class", tableClass);
                GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(lastX, lastX, 200, 250));
                lastX += 110;
                lastY += 50;
                cellClassMap.put(tableClass, cell);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ModlerTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cellClassMap;
    }

    /**
     * Sets the component's location to center on screen
     *
     * @param component
     */
    public static void centerOnScreen(Component component, boolean fullScreen) {
        Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
        if (fullScreen) {
            component.setSize(sc);
        }
        Dimension wi = component.getSize();
        component.setLocation((sc.width - wi.width) / 2, (sc.height - wi.height) / 2);
    }

    /**
     * Returns a list of connectors by determinig the relationship between member fields in
     * the keySet of the specified map.
     * It sets the source and target of the connections by checking which class references
     * which.
     *
     * @param cellClassMap
     * @return
     */
    public static List<Edge> mapCellRelations(Map<Class, DefaultGraphCell> cellClassMap) {
        List<Edge> edges = new ArrayList<Edge>();
        for (Class clazz : cellClassMap.keySet()) {
            try {
                List<Class> mappedClasses = ClassUtils.getMappedClasses(clazz);
                for (Class targetClass : mappedClasses) {
                    if (!cellClassMap.containsKey(targetClass) || !cellClassMap.containsKey(clazz)) {
                        continue;
                    }
                    MappingEdge edge = new MappingEdge();
                    edge.setSource(cellClassMap.get(clazz).addPort());
                    edge.setTarget(cellClassMap.get(targetClass).addPort());
                    edges.add(edge);
                }
            } catch (Exception ex) {
                Logger.getLogger(ModlerTools.class.getName()).log(Level.INFO, "Error in class relation mapping", ex);
            }
        }
        return edges;
    }

    /**
     * Returns all connections to and from this cell.
     *
     * @param cell
     * @return
     */
    public static Object[] getConnectors(DefaultGraphCell cell) {
        List connectors = new ArrayList();
        for (Object child : cell.getChildren()) {
            if (child instanceof DefaultPort) {
                connectors.addAll(((DefaultPort) child).getEdges());
            }
        }
        return connectors.toArray();
    }

    /**
     * @param cell
     * @param outConnectors
     * @return
     */
    public static String getLinkedTableNames(DefaultGraphCell cell, boolean outConnectors) {
        StringBuffer buff = new StringBuffer();
        List<DefaultGraphCell> cells = getLinkedTables(cell, outConnectors);
        for (DefaultGraphCell referencedCell : cells) {
            buff.append(referencedCell.getUserObject().toString() + "\n");
        }
        return buff.toString();
    }

    /**
     * @param cell
     * @param outConnectors
     * @return
     */
    private static List<DefaultGraphCell> getLinkedTables(DefaultGraphCell cell, boolean outConnectors) {
        List<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
        Object[] edges = getConnectors(cell);
        for (Object edge : edges) {
            if (outConnectors) {
                cells.add((DefaultGraphCell) (((DefaultPort) ((DefaultEdge) edge).getTarget()).getParent()));
            } else {
                cells.add((DefaultGraphCell) (((DefaultPort) ((DefaultEdge) edge).getSource()).getParent()));
            }
        }
        cells.remove(cell);
        return cells;
    }

    /**
     * Loads cell/class layout file.
     * This file is saved in the ~/.dataSiloPrefrences.properties
     * file with key layout.file
     *
     * @param modler
     */
    public static void loadLayout(ModlerUI modler) {
        BufferedReader br = null;
        System.out.println("LAYOUT FILE " + modler.getProperties().getProperty("layout.file"));
        if (modler.getProperties() != null && modler.getProperties().getProperty("layout.file") != null && modler != null) {
            try {
                File layoutFile = new File(modler.getProperties().getProperty("layout.file"));
                modler.setLayoutFile(layoutFile);
                if (layoutFile.exists()) {
                    br = new BufferedReader(new FileReader(layoutFile));
                    Map<DefaultGraphCell, AttributeMap> cellLayouts = new HashMap<DefaultGraphCell, AttributeMap>();
                    String line = br.readLine();
                    line = br.readLine();
                    line = br.readLine();
                    while (line != null) {
                        try {
                            String[] lineSplit = line.split(",");
                            if (lineSplit.length != 3) {
                                line = br.readLine();
                                continue;
                            }
                            DefaultGraphCell cell = ModlerUI.CLASS_VERTEX_MAPPING.get(ClassUtils.loadClass(lineSplit[0]));
                            AttributeMap attributes = (AttributeMap) cell.getAttributes().clone();
                            Rectangle2D bounds = GraphConstants.getBounds(attributes);
                            bounds.setRect(Double.parseDouble(lineSplit[1].substring(2)), Double.parseDouble(lineSplit[2].substring(2)), bounds.getWidth(), bounds.getHeight());
                            GraphConstants.setBounds(attributes, bounds);
                            cellLayouts.put(cell, attributes);
                        } catch (ClassNotFoundException e) {
                        }
                        line = br.readLine();
                    }
                    ModlerUI.getGraph().getGraphLayoutCache().edit(cellLayouts);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ModlerTools.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ModlerTools.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (br != null) try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ModlerTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Save the layout of the cells in the main project.
     *
     * @param modler
     */
    public static void saveLayout(ModlerUI modler) {
        JFileChooser saveTo = new JFileChooser(System.getProperty("user.home"));
        if (modler.getLayoutFile() == null) {
            saveTo.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (f.getName().endsWith("slo") || f.getName().indexOf(".") < 0) {
                        return true;
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return "Silo layout file";
                }
            });
            saveTo.showSaveDialog(modler);
            File saveToFile = saveTo.getSelectedFile();
            if (!saveToFile.getPath().endsWith(".slo")) {
                modler.setLayoutFile(new File(saveTo.getSelectedFile().getPath() + ".slo"));
            } else {
                modler.setLayoutFile(saveTo.getSelectedFile());
            }
        }
        FileOutputStream fos = null;
        try {
            StringBuffer fileBuffer = new StringBuffer();
            fileBuffer.append("# Written by DataSiloModler on " + Calendar.getInstance().getTime());
            fileBuffer.append("\n# Do not edit.");
            for (Class clazz : ModlerUI.CLASS_VERTEX_MAPPING.keySet()) {
                DefaultGraphCell cell = ModlerUI.CLASS_VERTEX_MAPPING.get(clazz);
                Rectangle2D bounds = GraphConstants.getBounds(cell.getAttributes());
                fileBuffer.append("\n" + clazz.getName() + ",x=" + bounds.getX() + ",y=" + bounds.getY());
            }
            fileBuffer.append("\n");
            fos = new FileOutputStream(modler.getLayoutFile(), false);
            fos.write(fileBuffer.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModlerTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModlerTools.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(ModlerTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @param modler
     * @throws java.io.IOException
     */
    public static void saveProject(ModlerUI modler) throws IOException {
        Properties dataSiloProperties = modler.getProperties();
        if (modler.getLayoutFile() == null) {
            saveLayout(modler);
        }
        dataSiloProperties.setProperty("layout.file", modler.getLayoutFile().getPath());
        dataSiloProperties.store(new FileOutputStream(modler.getDataSiloPref(), false), "Generated by DataSiloModler. Do not edit ");
    }
}
