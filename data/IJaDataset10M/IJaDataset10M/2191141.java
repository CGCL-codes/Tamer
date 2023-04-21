package de.fzj.unicore.plugins.cpmd.wizard;

import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.table.DefaultTableModel;

/** 
 *
 * @author Valentina Huber
 * @version 
 */
public abstract class CPMDPanel extends javax.swing.JPanel {

    public static final String TAB = "  ";

    public static final String LFTAB = "\n " + TAB;

    Hashtable xmlList = new Hashtable();

    protected CPMDWizard wizard;

    /** Creates new CPMDPanel */
    public CPMDPanel(String name, CPMDWizard wizard) {
        super();
        this.wizard = wizard;
        setName(name);
    }

    public abstract void validateValues() throws InvalidFormatException;

    public abstract void updateFields();

    public static void validateValue(String description, JTextField tf) throws InvalidFormatException {
        try {
            validateValue(description, tf, Class.forName("java.lang.String"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void validateValue(String description, JTextField tf, Class type) throws InvalidFormatException {
        description = description.trim();
        if (description.endsWith(":")) description = description.substring(0, description.indexOf(":"));
        String className = type.getName();
        int i = className.lastIndexOf(".");
        if (i != -1) className = className.substring(i + 1);
        try {
            String value = tf.getText().trim();
            if (className.equals("int")) Integer.parseInt(value); else if (className.equals("double")) {
                Double.valueOf(value.replace('D', 'E')).doubleValue();
                className = "float";
            } else if (className.equals("String") && value.equals("")) throw new Exception();
        } catch (Exception ex) {
            tf.requestFocus();
            tf.selectAll();
            throw new InvalidFormatException("Please enter " + className + " value in the field \"" + description + "\"");
        }
    }

    public static void validateValue(String description, JTable tbl) throws InvalidFormatException {
        description = description.trim();
        if (description.endsWith(":")) description = description.substring(0, description.indexOf(":"));
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        Vector v = model.getDataVector();
        int row = 0, column = 0;
        for (; row < v.size(); row++) {
            Vector vr = (Vector) v.elementAt(row);
            for (column = 0; column < vr.size(); column++) {
                String className = model.getColumnClass(column).getName();
                int i = className.lastIndexOf(".");
                if (i != -1) className = className.substring(i + 1);
                if (vr.elementAt(column) == null && !(className.equals("Boolean"))) {
                    tbl.changeSelection(row, column, false, false);
                    String columnName = model.getColumnName(column);
                    if (columnName.equals("null")) columnName = String.valueOf(column);
                    if (className.equals("double")) className = "float";
                    throw new InvalidFormatException("Please enter the " + className + " value in the cell (column=\"" + columnName + "\" row=" + row + ") and press <TAB>.");
                }
            }
        }
    }

    /**
   * Adds the specified object to be stored as XML element
   */
    public void addToXmlList(String name, Object obj) {
        xmlList.put(name, obj);
    }

    public Hashtable getXmlList() {
        return xmlList;
    }

    /**
   * Sets the last stored settings.
   */
    public void reset() throws ParserConfigurationException {
        Document lastSettings = wizard.getLastSettings();
        updateComponents(lastSettings);
    }

    /**
   * Set the default settings.
   */
    public void setDefault() throws ParserConfigurationException {
        Document defaultSettings = wizard.getDefaultSettings();
        updateComponents(defaultSettings);
    }

    /**
   * update values of components with the current settings
   */
    public void updateComponents() throws ParserConfigurationException {
        Document currentSettings = wizard.getCurrentSettings();
        updateComponents(currentSettings);
    }

    /**
   * update values of components from the specified XML document
   * and update the dependencies between components
   */
    public void updateComponents(Document document) throws ParserConfigurationException {
        updateValues(document);
        updateFields();
    }

    /**
   * update values of components from the specified XML document
   */
    private void updateValues(Document document) throws ParserConfigurationException {
        Element parent = (Element) document.getElementsByTagName(wizard.getName()).item(0);
        if (parent == null) throw new ParserConfigurationException("XmlElement for " + wizard.getName() + " not found"); else setValue(this, parent);
    }

    public void saveValues() throws IOException, ParserConfigurationException {
        Document lastSettings = wizard.getLastSettings();
        Element parent = (Element) lastSettings.getDocumentElement();
        Element newElement = createXmlElement(this, parent);
        Element oldElement = (Element) parent.getElementsByTagName(getName()).item(0);
        if (oldElement != null) {
            parent.replaceChild(newElement, oldElement);
        } else {
            parent.appendChild(newElement);
        }
    }

    public void saveValue() throws IOException, ParserConfigurationException {
        Document lastSettings = wizard.getLastSettings();
        Element parent = (Element) lastSettings.getDocumentElement();
        saveValue(this, parent);
    }

    public static Element saveValue(Component component, Element parent) throws IOException, DOMException, ParserConfigurationException {
        if (component == null) return null;
        String name = component.getName();
        if (name == null || name.equals("")) name = component.getClass().getName() + "_" + component.hashCode();
        name = java2Xml(name);
        Element oldElement = (Element) parent.getElementsByTagName(name).item(0);
        Element newElement = createXmlElement(component, parent);
        if (oldElement != null) {
            parent.replaceChild(newElement, oldElement);
        } else {
            parent.appendChild(newElement);
        }
        return newElement;
    }

    public static Element appendXmlElement(Component component, Element parent) throws IOException, DOMException, ParserConfigurationException {
        Element element = createXmlElement(component, parent);
        if (element != null) parent.appendChild(element);
        return element;
    }

    public static Element appendXmlElement(String name, Object obj, Element parent) throws IOException, DOMException, ParserConfigurationException {
        Element element = createXmlElement(name, obj, parent);
        if (element != null) parent.appendChild(element);
        return element;
    }

    public static Element createXmlElement(Component component, Element parent) throws IOException, DOMException, ParserConfigurationException {
        if (component == null) return null;
        String name = component.getName();
        if (name == null || name.trim().equals("")) name = component.getClass().getName() + "_" + component.hashCode();
        name = java2Xml(name);
        return createXmlElement(name, component, parent);
    }

    public static Element createXmlElement(String name, Object object, Element parent) throws IOException, DOMException, ParserConfigurationException {
        if (object == null) {
            System.err.println("Object " + name + " is null");
            return null;
        }
        Document document = (Document) parent.getOwnerDocument();
        Element elem = document.createElement(name);
        String cc = java2Xml(object.getClass().getName());
        elem.setAttribute("class", cc);
        if (object instanceof Component && ((Component) object).isEnabled()) elem.setAttribute("isEnabled", "true");
        if (object instanceof CPMDPanel) {
            Hashtable olist = ((CPMDPanel) object).getXmlList();
            for (Enumeration e = olist.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                Object obj = olist.get(key);
                appendXmlElement(key, obj, elem);
            }
        } else if (object instanceof JTextComponent) {
            String text = ((JTextComponent) object).getText().trim();
            if (!text.equals("")) elem.setAttribute("value", java2Xml(text));
        } else if (object instanceof JSlider) {
            int value = ((JSlider) object).getValue();
            elem.setAttribute("value", String.valueOf(value));
        } else if (object instanceof JToggleButton) {
            boolean isSelected = ((JToggleButton) object).isSelected();
            if (isSelected) elem.setAttribute("isSelected", String.valueOf(isSelected));
        } else if (object instanceof JComboBox) {
            String selectedItem = (String) ((JComboBox) object).getSelectedItem();
            elem.setAttribute("value", selectedItem);
        } else if (object instanceof JTable) {
            DefaultTableModel tm = (DefaultTableModel) ((JTable) object).getModel();
            int numRows = tm.getRowCount();
            int numColumns = tm.getColumnCount();
            elem.setAttribute("rows", String.valueOf(numRows));
            elem.setAttribute("columns", String.valueOf(numColumns));
            Element columnNames = document.createElement("columnNames");
            for (int j = 0; j < numColumns; j++) {
                Element columnName = document.createElement("columnName");
                columnName.setAttribute("value", tm.getColumnName(j));
                columnNames.appendChild(columnName);
            }
            elem.appendChild(columnNames);
            Vector vec = tm.getDataVector();
            if (vec.size() > 0) {
                Element data = document.createElement("dataElements");
                for (int j = 0; j < vec.size(); j++) {
                    Vector vr = (Vector) vec.elementAt(j);
                    for (int k = 0; k < vr.size(); k++) {
                        Object obj = vr.elementAt(k);
                        Element dataElement = document.createElement("dataElement");
                        data.appendChild(dataElement);
                        if (obj == null) dataElement.setAttribute("value", "null"); else {
                            dataElement.setAttribute("value", java2Xml(obj.toString().trim()));
                            dataElement.setAttribute("class", java2Xml(obj.getClass().getName()));
                        }
                    }
                }
                elem.appendChild(data);
            }
        } else if (object instanceof Container && ((Container) object).getComponentCount() != 0) {
            Element clist = document.createElement("Subcomponents");
            elem.appendChild(clist);
            Component[] components = ((Container) object).getComponents();
            for (int i = 0; i < components.length; i++) appendXmlElement(components[i], clist);
        } else if (object instanceof Vector) {
            int size = ((Vector) object).size();
            elem.setAttribute("size", String.valueOf(size));
            if (name.equals("atoms")) {
                if (Atom.isotopeIsSelected) elem.setAttribute("isotopeIsSelected", "true");
                if (Atom.changeChargeIsSelected) elem.setAttribute("changeChargeIsSelected", "true");
                if (Atom.movieTypeIsSelected) elem.setAttribute("movieTypeIsSelected", "true");
                if (Atom.generateCoordinatesIsSelected) elem.setAttribute("generateCoordinatesIsSelected", "true");
            }
            if (size != 0) {
                Element clist = document.createElement("Elements");
                elem.appendChild(clist);
                for (int i = 0; i < ((Vector) object).size(); i++) appendXmlElement("Element", ((Vector) object).elementAt(i), clist);
            }
        } else if (object instanceof Atom) {
            Atom atom = (Atom) object;
            elem.setAttribute("ecpname", atom.ecpname);
            if (atom.methodIsSelected) elem.setAttribute("methodIsSelected", "true");
            elem.setAttribute("method", atom.method);
            if (atom.nlccIsSelected) elem.setAttribute("nlccIsSelected", "true");
            if (atom.raggioIsSelected) elem.setAttribute("raggioIsSelected", "true");
            elem.setAttribute("raggio", String.valueOf(atom.raggio));
            if (atom.binaryIsSelected) elem.setAttribute("binaryIsSelected", "true");
            if (atom.newfIsSelected) elem.setAttribute("newfIsSelected", "true");
            if (atom.tpseuIsSelected) elem.setAttribute("tpseuIsSelected", "true");
            if (atom.lmaxIsSelected) elem.setAttribute("lmaxIsSelected", "true");
            elem.setAttribute("lmax", atom.lmax);
            if (atom.locIsSelected) elem.setAttribute("locIsSelected", "true");
            elem.setAttribute("loc", atom.loc);
            if (atom.skipIsSelected) elem.setAttribute("skipIsSelected", "true");
            elem.setAttribute("skip", atom.skip);
            if (atom.coordinates.length != 0) {
                Element clist = document.createElement("Coordinates");
                for (int i = 0; i < atom.coordinates.length; i++) {
                    Element coordElement = document.createElement("Coordinate");
                    coordElement.setAttribute("X", String.valueOf(atom.coordinates[i][0]));
                    coordElement.setAttribute("Y", String.valueOf(atom.coordinates[i][1]));
                    coordElement.setAttribute("Z", String.valueOf(atom.coordinates[i][2]));
                    clist.appendChild(coordElement);
                }
                elem.appendChild(clist);
            }
            if (atom.isotope != null) elem.setAttribute("isotope", String.valueOf(atom.isotope));
            if (atom.charge != null) elem.setAttribute("charge", String.valueOf(atom.charge));
            if (atom.movieType != null) elem.setAttribute("movieType", String.valueOf(atom.movieType));
            if (atom.generateCoordinates != null) elem.setAttribute("generateCoordinates", String.valueOf(atom.generateCoordinates));
            if (atom.davidsonNumber != null) elem.setAttribute("davidsonNumber", String.valueOf(atom.davidsonNumber));
            Element basis = document.createElement("AtomicBasis");
            elem.appendChild(basis);
            basis.setAttribute("type", atom.basis.type);
            basis.setAttribute("filename", atom.basis.filename);
            basis.setAttribute("format", atom.basis.format);
            if (atom.basis.occupationIsSelected) basis.setAttribute("occupationIsSelected", "true");
            if (atom.basis.lvalues.length != 0) {
                Element lvlist = document.createElement("lvalues");
                for (int i = 0; i < atom.basis.lvalues.length; i++) {
                    Element lvElement = document.createElement("lvalue");
                    lvElement.setAttribute("size", String.valueOf(atom.basis.lvalues.length));
                    lvElement.setAttribute("value", String.valueOf(atom.basis.lvalues[i][0]));
                    if (atom.basis.occupationIsSelected) lvElement.setAttribute("occupation", String.valueOf(atom.basis.lvalues[i][1]));
                    lvlist.appendChild(lvElement);
                }
                basis.appendChild(lvlist);
            }
        } else if (object instanceof DummyAtom) {
            DummyAtom dummyAtom = (DummyAtom) object;
            elem.setAttribute("type", dummyAtom.type);
            elem.setAttribute("X", String.valueOf(dummyAtom.coordinates[0]));
            elem.setAttribute("Y", String.valueOf(dummyAtom.coordinates[1]));
            elem.setAttribute("Z", String.valueOf(dummyAtom.coordinates[2]));
            if (dummyAtom.atoms.length != 0) {
                Element alist = document.createElement("atomNumbers");
                alist.setAttribute("size", String.valueOf(dummyAtom.atoms.length));
                for (int i = 0; i < dummyAtom.atoms.length; i++) {
                    Element lElement = document.createElement("atomNumber");
                    lElement.setAttribute("value", String.valueOf(dummyAtom.atoms[i][0]));
                    alist.appendChild(lElement);
                }
                elem.appendChild(alist);
            }
        } else if (object instanceof FixCoordinates) {
            FixCoordinates fixCoordinates = (FixCoordinates) object;
            elem.setAttribute("atomNumber", String.valueOf(fixCoordinates.atomNumber));
            elem.setAttribute("fixX", String.valueOf(fixCoordinates.fixX));
            elem.setAttribute("fixY", String.valueOf(fixCoordinates.fixY));
            elem.setAttribute("fixZ", String.valueOf(fixCoordinates.fixZ));
        } else if (object instanceof Bond) {
            Bond bond = (Bond) object;
            elem.setAttribute("flag", String.valueOf(bond.flag));
            elem.setAttribute("atom1", String.valueOf(bond.atom1));
            elem.setAttribute("atom2", String.valueOf(bond.atom2));
        } else if (object instanceof Velocity) {
            Velocity velocity = (Velocity) object;
            elem.setAttribute("specieNumber", String.valueOf(velocity.specieNumber));
            elem.setAttribute("atomNumber", String.valueOf(velocity.atomNumber));
            elem.setAttribute("vx", String.valueOf(velocity.vx));
            elem.setAttribute("vy", String.valueOf(velocity.vy));
            elem.setAttribute("vz", String.valueOf(velocity.vz));
        } else if (object instanceof AtomConstraint) {
            AtomConstraint constraint = (AtomConstraint) object;
            elem.setAttribute("type", constraint.type);
            elem.setAttribute("n1", String.valueOf(constraint.n1));
            elem.setAttribute("n2", String.valueOf(constraint.n2));
            elem.setAttribute("n3", String.valueOf(constraint.n3));
            elem.setAttribute("n4", String.valueOf(constraint.n4));
            elem.setAttribute("R", String.valueOf(constraint.R));
            elem.setAttribute("O", String.valueOf(constraint.O));
            elem.setAttribute("k", String.valueOf(constraint.k));
            elem.setAttribute("Rc", String.valueOf(constraint.Rc));
            elem.setAttribute("C0", String.valueOf(constraint.C0));
            elem.setAttribute("shoveValue", constraint.shoveValue);
            if (constraint.atoms.length != 0) {
                Element alist = document.createElement("atoms");
                for (int i = 0; i < constraint.atoms.length; i++) {
                    Element lElement = document.createElement("atom");
                    lElement.setAttribute("number", String.valueOf(constraint.atoms[i][0]));
                    alist.appendChild(lElement);
                }
                elem.appendChild(alist);
            }
        } else if (object instanceof Integer || object instanceof Double || object instanceof Float || object instanceof Byte || object instanceof Short || object instanceof String) {
            elem.setAttribute("value", String.valueOf(object));
        }
        return elem;
    }

    /**
   * Sets the state of the specified component and its subcomponents
   * to the values stored in the specified XML-Document
   */
    public static void setValue(Component component, Element parent) throws ParserConfigurationException {
        if (component != null) {
            String name = component.getName();
            if (name == null || name.trim().equals("")) name = component.getClass().getName() + "_" + component.hashCode();
            name = java2Xml(name);
            setValue(name, component, parent);
        }
    }

    /**
   * Sets the state of the specified object
   * to the value stored in the specified XML-Document
   */
    public static void setValue(String name, Object object, Element parent) throws ParserConfigurationException {
        Element elem = (Element) parent.getElementsByTagName(name).item(0);
        if (elem == null) throw new ParserConfigurationException("XmlElement " + name + " not found.");
        try {
            if (object instanceof CPMDPanel) {
                Hashtable xmlList = ((CPMDPanel) object).getXmlList();
                for (Enumeration e = xmlList.keys(); e.hasMoreElements(); ) {
                    String key = (String) e.nextElement();
                    Object obj = xmlList.get(key);
                    setValue(key, obj, elem);
                }
                ((CPMDPanel) object).updateFields();
            } else if (object instanceof JTextComponent) {
                String text = elem.getAttribute("value");
                ((JTextComponent) object).setText(text);
            } else if (object instanceof JSlider) {
                String value = elem.getAttribute("value");
                if (!value.equals("")) ((JSlider) object).setValue(Integer.parseInt(value));
            } else if (object instanceof JToggleButton) {
                boolean isSelected = elem.getAttribute("isSelected").equals("true");
                ((JToggleButton) object).setSelected(isSelected);
            } else if (object instanceof JComboBox) {
                String selectedItem = elem.getAttribute("value");
                if (!selectedItem.equals("")) ((JComboBox) object).setSelectedItem(selectedItem);
            } else if (object instanceof JTable) {
                NodeList nlist = elem.getElementsByTagName("columnName");
                String[] columnNames = new String[nlist.getLength()];
                for (int i = 0; i < columnNames.length; i++) {
                    columnNames[i] = ((Element) nlist.item(i)).getAttribute("value");
                }
                int numRows = Integer.parseInt(elem.getAttribute("rows"));
                int numColumns = Integer.parseInt(elem.getAttribute("columns"));
                Object[][] data = new Object[numRows][numColumns];
                NodeList dataElements = elem.getElementsByTagName("dataElement");
                for (int i = 0; i < dataElements.getLength(); i++) {
                    String value = ((Element) dataElements.item(i)).getAttribute("value");
                    if (value == null || value.equals("null")) continue;
                    int r = i / numColumns;
                    int c = i - r * numColumns;
                    String classType = ((Element) dataElements.item(i)).getAttribute("class");
                    if (classType.equals("java.lang.Integer")) data[r][c] = new Integer(value); else if (classType.equals("java.lang.Boolean")) data[r][c] = new Boolean(value); else if (classType.equals("java.lang.Double")) data[r][c] = new Double(value); else data[r][c] = value;
                }
                DefaultTableModel tm = (DefaultTableModel) ((JTable) object).getModel();
                tm.setDataVector(data, columnNames);
            } else if (object instanceof Container && ((Container) object).getComponentCount() != 0) {
                Component[] components = ((Container) object).getComponents();
                for (int i = 0; i < components.length; i++) {
                    setValue(components[i], parent);
                }
            } else if (object instanceof Vector) {
                if (name.equals("atoms")) {
                    Atom.isotopeIsSelected = (elem.getAttribute("isotopeIsSelected").equals("true"));
                    Atom.changeChargeIsSelected = (elem.getAttribute("changeChargeIsSelected").equals("true"));
                    Atom.movieTypeIsSelected = (elem.getAttribute("movieTypeIsSelected").equals("true"));
                    Atom.generateCoordinatesIsSelected = (elem.getAttribute("generateCoordinatesIsSelected").equals("true"));
                }
                ((Vector) object).removeAllElements();
                NodeList nlist = elem.getElementsByTagName("Element");
                for (int i = 0; i < nlist.getLength(); i++) {
                    Element e = (Element) nlist.item(i);
                    if (name.equals("atoms")) {
                        Atom atom = new Atom();
                        ((Vector) object).add(atom);
                        atom.ecpname = e.getAttribute("ecpname");
                        atom.methodIsSelected = (e.getAttribute("methodIsSelected").equals("true"));
                        atom.method = e.getAttribute("method");
                        atom.nlccIsSelected = (e.getAttribute("nlccIsSelected").equals("true"));
                        atom.raggioIsSelected = (e.getAttribute("raggioIsSelected").equals("true"));
                        try {
                            atom.raggio = Double.valueOf(e.getAttribute("raggio")).doubleValue();
                        } catch (Exception ex) {
                        }
                        atom.binaryIsSelected = (e.getAttribute("binaryIsSelected").equals("true"));
                        atom.newfIsSelected = (e.getAttribute("newfIsSelected").equals("true"));
                        atom.tpseuIsSelected = (e.getAttribute("tpseuIsSelected").equals("true"));
                        atom.lmaxIsSelected = (e.getAttribute("lmaxIsSelected").equals("true"));
                        atom.lmax = e.getAttribute("lmax");
                        atom.locIsSelected = (e.getAttribute("locIsSelected").equals("true"));
                        atom.loc = e.getAttribute("loc");
                        atom.skipIsSelected = (e.getAttribute("skipIsSelected").equals("true"));
                        atom.skip = e.getAttribute("skip");
                        org.w3c.dom.NodeList clist = e.getElementsByTagName("Coordinate");
                        atom.coordinates = new Double[clist.getLength()][3];
                        for (int j = 0; j < atom.coordinates.length; j++) {
                            Element coordElement = (Element) clist.item(j);
                            try {
                                atom.coordinates[j][0] = new Double(coordElement.getAttribute("X"));
                            } catch (Exception ex) {
                            }
                            try {
                                atom.coordinates[j][1] = new Double(coordElement.getAttribute("Y"));
                            } catch (Exception ex) {
                            }
                            try {
                                atom.coordinates[j][2] = new Double(coordElement.getAttribute("Z"));
                            } catch (Exception ex) {
                            }
                        }
                        try {
                            atom.isotope = new Double(e.getAttribute("isotope"));
                        } catch (Exception ex) {
                        }
                        try {
                            atom.charge = new Double(e.getAttribute("charge"));
                        } catch (Exception ex) {
                        }
                        try {
                            atom.movieType = new Integer(e.getAttribute("movieType"));
                        } catch (Exception ex) {
                        }
                        try {
                            atom.generateCoordinates = new Integer(e.getAttribute("generateCoordinates"));
                        } catch (Exception ex) {
                        }
                        try {
                            atom.davidsonNumber = new Integer(e.getAttribute("davidsonNumber"));
                        } catch (Exception ex) {
                        }
                        Element basis = (Element) e.getElementsByTagName("AtomicBasis").item(0);
                        atom.basis.type = basis.getAttribute("type");
                        atom.basis.filename = basis.getAttribute("filename");
                        atom.basis.format = basis.getAttribute("format");
                        atom.basis.occupationIsSelected = (basis.getAttribute("occupationIsSelected").equals("true"));
                        NodeList lvlist = basis.getElementsByTagName("lvalue");
                        int n = (atom.basis.occupationIsSelected ? 2 : 1);
                        atom.basis.lvalues = new Object[lvlist.getLength()][n];
                        for (int j = 0; j < atom.basis.lvalues.length; j++) {
                            Element lvElement = (Element) lvlist.item(j);
                            try {
                                atom.basis.lvalues[j][0] = new Double(lvElement.getAttribute("value"));
                            } catch (Exception ex) {
                            }
                            if (atom.basis.occupationIsSelected) {
                                try {
                                    atom.basis.lvalues[j][1] = new Double(lvElement.getAttribute("occupation"));
                                } catch (Exception ex) {
                                }
                            }
                        }
                    } else if (name.equals("dummyAtoms")) {
                        DummyAtom dummyAtom = new DummyAtom();
                        ((Vector) object).add(dummyAtom);
                        dummyAtom.type = e.getAttribute("type");
                        try {
                            dummyAtom.coordinates[0] = Double.valueOf(e.getAttribute("X")).doubleValue();
                        } catch (Exception ex) {
                        }
                        try {
                            dummyAtom.coordinates[1] = Double.valueOf(e.getAttribute("Y")).doubleValue();
                        } catch (Exception ex) {
                        }
                        try {
                            dummyAtom.coordinates[2] = Double.valueOf(e.getAttribute("Z")).doubleValue();
                        } catch (Exception ex) {
                        }
                        NodeList alist = e.getElementsByTagName("atomNumber");
                        dummyAtom.atoms = new Integer[alist.getLength()][1];
                        for (int j = 0; j < dummyAtom.atoms.length; j++) {
                            Element aElement = (Element) alist.item(j);
                            try {
                                dummyAtom.atoms[j][0] = new Integer(aElement.getAttribute("value"));
                            } catch (Exception ex) {
                            }
                        }
                    } else if (name.equals("bonds")) {
                        Bond bond = new Bond();
                        ((Vector) object).add(bond);
                        try {
                            bond.flag = new Boolean(e.getAttribute("flag"));
                        } catch (Exception ex) {
                        }
                        try {
                            bond.atom1 = new Integer(e.getAttribute("atom1"));
                        } catch (Exception ex) {
                        }
                        try {
                            bond.atom2 = new Integer(e.getAttribute("atom2"));
                        } catch (Exception ex) {
                        }
                    } else if (name.equals("velocities")) {
                        Velocity velocity = new Velocity();
                        ((Vector) object).add(velocity);
                        try {
                            velocity.specieNumber = new Integer(e.getAttribute("specieNumber"));
                        } catch (Exception ex) {
                        }
                        try {
                            velocity.atomNumber = new Integer(e.getAttribute("atomNumber"));
                        } catch (Exception ex) {
                        }
                        try {
                            velocity.vx = new Double(e.getAttribute("vx"));
                        } catch (Exception ex) {
                        }
                        try {
                            velocity.vy = new Double(e.getAttribute("vy"));
                        } catch (Exception ex) {
                        }
                        try {
                            velocity.vz = new Double(e.getAttribute("vz"));
                        } catch (Exception ex) {
                        }
                    } else if (name.equals("fixAtoms")) {
                        try {
                            Integer atomNumber = new Integer(e.getAttribute("value"));
                            ((Vector) object).add(atomNumber);
                        } catch (Exception ex) {
                        }
                    } else if (name.equals("fixCoordinates")) {
                        FixCoordinates fixCoordinates = new FixCoordinates();
                        ((Vector) object).add(fixCoordinates);
                        try {
                            fixCoordinates.atomNumber = new Integer(e.getAttribute("atomNumber"));
                        } catch (Exception ex) {
                        }
                        try {
                            fixCoordinates.fixX = new Boolean(e.getAttribute("fixX"));
                        } catch (Exception ex) {
                        }
                        try {
                            fixCoordinates.fixY = new Boolean(e.getAttribute("fixY"));
                        } catch (Exception ex) {
                        }
                        try {
                            fixCoordinates.fixZ = new Boolean(e.getAttribute("fixZ"));
                        } catch (Exception ex) {
                        }
                    } else if (name.equals("atomConstraints")) {
                        AtomConstraint constraint = new AtomConstraint();
                        ((Vector) object).add(constraint);
                        constraint.type = e.getAttribute("type");
                        try {
                            constraint.n1 = Integer.parseInt(e.getAttribute("n1"));
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.n2 = Integer.parseInt(e.getAttribute("n2"));
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.n3 = Integer.parseInt(e.getAttribute("n3"));
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.n4 = Integer.parseInt(e.getAttribute("n4"));
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.R = Double.valueOf(e.getAttribute("R")).doubleValue();
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.O = Double.valueOf(e.getAttribute("O")).doubleValue();
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.k = Double.valueOf(e.getAttribute("k")).doubleValue();
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.Rc = Double.valueOf(e.getAttribute("Rc")).doubleValue();
                        } catch (Exception ex) {
                        }
                        try {
                            constraint.C0 = Double.valueOf(e.getAttribute("C0")).doubleValue();
                        } catch (Exception ex) {
                        }
                        constraint.shoveValue = e.getAttribute("shoveValue");
                        NodeList alist = e.getElementsByTagName("atom");
                        constraint.atoms = new Integer[alist.getLength()][1];
                        for (int j = 0; j < constraint.atoms.length; j++) {
                            Element aElement = (Element) alist.item(j);
                            try {
                                constraint.atoms[j][0] = new Integer(aElement.getAttribute("number"));
                            } catch (Exception ex) {
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ParserConfigurationException("Failure setting " + name + ": " + e);
        }
    }

    /**
   * Converts the ASCII	characters of the specified string
   * to the XML characters.
   */
    public static String java2Xml(String str) {
        StringBuffer xmlString = new StringBuffer();
        char[] xmlArray = new char[str.length()];
        str.getChars(0, xmlArray.length, xmlArray, 0);
        for (int i = 0; i < xmlArray.length; i++) {
            String s = String.valueOf(xmlArray[i]);
            if ("&".equals(s)) s = "&amp;"; else if ("\'".equals(s)) s = "&apos;"; else if ("<".equals(s)) s = "&lt;"; else if (">".equals(s)) s = "&gt;"; else if ("\"".equals(s)) s = "&quot;"; else if ("$".equals(s) || " ".equals(s)) s = "_";
            xmlString.append(s);
        }
        return xmlString.toString();
    }
}
