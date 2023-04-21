package foa.bricks.complex;

import java.util.*;
import javax.swing.tree.*;
import foa.elements.Element;
import foa.elements.fo.*;
import foa.elements.processor.*;
import foa.bricks.Brick;
import foa.templates.BrickDirector;
import foa.properties.bricks.BrickDialog;
import foa.properties.bricks.SimpleTableDialog;
import foa.apps.XSLTWriter;

public class SimpleTable extends Brick {

    private BrickDialog currentDialog;

    public static class Maker extends Brick.Maker {

        public Brick make(String brickName, String brickClass, String match, String type, String group) {
            return new SimpleTable(brickName, brickClass, match, type, group);
        }

        public Brick make(String group) {
            return new SimpleTable("", "table", "", group, group);
        }
    }

    private ArrayList tableRows = new ArrayList();

    private ArrayList tableCells = new ArrayList();

    private ArrayList tableColumns = new ArrayList();

    public SimpleTable(String brickName, String brickClass, String match, String group, String type) {
        super(brickName, brickClass, match, group, type);
        this.setAttribute("uses", "");
        this.setAttribute("select", "");
        this.setAttribute("table-layout", "");
        this.setAttribute("width", "");
        this.setAttribute("height", "");
    }

    public SimpleTable() {
        super();
    }

    public void fillSpecificAtts(String tag, Hashtable attributes) {
        if (tag.equals("xsl:apply-templates") && attributes.containsKey("select")) {
            this.setAttribute("select", (String) attributes.get("select"));
        } else if (tag.equals("fo:table-column")) {
            tableColumns.add(new Integer((String) attributes.get("column-number")).intValue() - 1, (String) attributes.get("column-width"));
        } else if (tag.equals("fo:table")) {
            if (attributes.containsKey("width")) {
                this.setAttribute("width", (String) attributes.get("width"));
            }
            if (attributes.containsKey("height")) {
                this.setAttribute("height", (String) attributes.get("height"));
            }
            if (attributes.containsKey("xsl:use-attribute-sets")) {
                this.setAttribute("uses", (String) attributes.get("xsl:use-attribute-sets"));
            }
        }
    }

    public void addChild(Brick child) {
        child.setParent(this);
        if (child.getAttribute("type").equals("table-row")) {
            tableRows.add(child);
        } else if (child.getAttribute("type").equals("table-cell")) {
            tableCells.add(child);
        }
    }

    public int getNumberOfChildren() {
        return (tableRows.size() + tableCells.size());
    }

    public Brick getChild(int number) {
        if (number < tableRows.size()) {
            return (Brick) tableRows.get(number);
        } else {
            return (Brick) tableCells.get(number - tableRows.size());
        }
    }

    public void createBrickDialog(BrickDirector brickDirector) {
        currentDialog = new SimpleTableDialog(brickDirector.getBrickManager().getBrickFrame(), brickDirector, this.getAttributesTable(), tableRows, tableCells, tableColumns);
    }

    public void enableDialog(boolean flag) {
        currentDialog.okButton.setEnabled(flag);
    }

    public BrickDialog getDialog() {
        return null;
    }

    public void updateAttribute(String value, String attName) {
        ((BrickDialog) currentDialog).updateAttribute(attName, value);
    }

    public void updateColumns(ArrayList columns) {
        tableColumns = columns;
    }

    public void updateCells(ArrayList cells) {
        tableCells = cells;
    }

    public void updateRows(ArrayList rows) {
        tableRows = rows;
    }

    private String getTableWidth() {
        String currentColumnWidth;
        String currentUnit;
        String currentValue;
        double totalWidth = 0;
        for (int i = 0; i < tableColumns.size(); i++) {
            currentColumnWidth = (String) tableColumns.get(i);
            currentUnit = currentColumnWidth.substring(currentColumnWidth.length() - 2);
            currentValue = currentColumnWidth.substring(0, currentColumnWidth.length() - 2);
            if (!currentUnit.equals("pt")) {
                currentValue = convertToPoints(currentValue, currentUnit);
            }
            totalWidth += (new Double(currentValue)).doubleValue();
        }
        return totalWidth + "pt";
    }

    public DefaultMutableTreeNode createBrickElement() {
        DefaultMutableTreeNode newElem = new DefaultMutableTreeNode(this.getAttribute("name"));
        for (int i = 0; i < this.getNumberOfChildren(); i++) {
            newElem.add(new DefaultMutableTreeNode((getChild(i).getAttribute("type") + "(" + (this.getAttribute("name"))) + ")"));
        }
        return newElem;
    }

    private String convertToPoints(String value, String unit) {
        double dvalue;
        int assumed_resolution = 1;
        dvalue = Double.valueOf(value).doubleValue();
        if (unit.equals("in")) dvalue = dvalue * 72; else if (unit.equals("cm")) dvalue = dvalue * 28.35; else if (unit.equals("mm")) dvalue = dvalue * 2.84; else if (unit.equals("pc")) dvalue = dvalue * 0.08333; else if (unit.equals("em")) dvalue = dvalue * 10; else if (unit.equals("px")) dvalue = dvalue * assumed_resolution; else {
            dvalue = 0;
            System.err.println("ERROR : unknown length units in " + unit);
        }
        return new Double(dvalue).toString();
    }

    public static Brick.Maker maker() {
        return new SimpleTable.Maker();
    }

    public void writeBrick(XSLTWriter writer) {
        writer.doStart("<xsl:template match=\"" + (String) this.getAttribute("match") + "\" foa:name=\"" + (String) this.getAttribute("name") + "\" foa:class=\"table\" foa:group=\"simple-table\">");
        writer.doStart("<fo:table foa:name=\"" + (String) this.getAttribute("name") + "\" foa:group=\"" + (String) this.getAttribute("group") + "\" table-layout=\"fixed\" width=\"" + getTableWidth() + "\" xsl:use-attribute-sets=\"" + ((String) this.getAttribute("uses")).substring(((String) this.getAttribute("uses")).indexOf(": ") + 1, ((String) this.getAttribute("uses")).length()) + "\">");
        writeColumns(writer);
        writer.doStart("<fo:table-body>");
        if (!((String) this.getAttributesTable().get("select")).equals("")) {
            writer.doBoth("<xsl:apply-templates select=\"" + (String) this.getAttribute("select") + "\"/>");
        } else {
            writer.doBoth("<xsl:apply-templates/>");
        }
        writer.doEnd("</fo:table-body>");
        writer.doEnd("</fo:table>");
        writer.doEnd("</xsl:template>");
        writeChildren(writer);
    }

    private void writeColumns(XSLTWriter writer) {
        for (int i = 0; i < tableColumns.size(); i++) {
            writer.doBoth("<fo:table-column column-number=\"" + (i + 1) + "\" column-width=\"" + (String) tableColumns.get(i) + "\"/>");
        }
    }

    private void writeChildren(XSLTWriter writer) {
        for (int i = 0; i < this.getNumberOfChildren(); i++) {
            (this.getChild(i)).writeBrick(writer);
        }
    }
}
