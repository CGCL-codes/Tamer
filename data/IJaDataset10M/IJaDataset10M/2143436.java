package pl.voidsystems.yajf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.voidsystems.yajf.servlet.ComponentStructureException;

/**
 * Component represents html table. It contains header, body and footer.
 */
public class Table extends Component implements IContainer {

    /**
     * Table name, must be locally unique.
     * 
     * @uml.property name="title"
     */
    protected String title = "";

    /**
     * Column span
     * 
     * @uml.property name="colspan"
     */
    protected int colspan = -1;

    /**
     * List of all table body rows.
     */
    protected ArrayList<TableRow> bodyRows = new ArrayList<TableRow>();

    /**
     * List of table header rows
     */
    protected ArrayList<TableRow> headerRows = new ArrayList<TableRow>();

    /**
     * List of table footer rows
     */
    protected ArrayList<TableRow> footerRows = new ArrayList<TableRow>();

    protected HashMap<Integer, String> col_templates = new HashMap<Integer, String>();

    /**
     * @see pl.voidsystems.yajf.components.IComponent#register()
     */
    @Override
    public void register() throws ComponentStructureException {
        super.register();
        for (TableRow row : this.bodyRows) {
            row.registerComponents();
        }
        for (TableRow row : this.headerRows) {
            row.registerComponents();
        }
        for (TableRow row : this.footerRows) {
            row.registerComponents();
        }
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Table name, must be locally unique
     */
    public Table(String name) {
        super(name);
    }

    /**
     * @see pl.voidsystems.yajf.components.IComponent#paintAt(org.w3c.dom.Element)
     */
    @Override
    public Element paintAt(final Element element) throws PaintException {
        final Document doc = element.getOwnerDocument();
        final Element table = doc.createElement("table");
        if (!this.template.equals("") && this.template != null) {
            table.setAttribute("template", this.template);
        }
        final Element header = doc.createElement("header");
        for (final Iterator iter = this.headerRows.iterator(); iter.hasNext(); ) {
            final TableRow row = (TableRow) iter.next();
            row.paintAt(header);
        }
        table.setAttribute("title", this.getTitle());
        int colspan = this.getColspan();
        table.setAttribute("colspan", colspan + "");
        for (int i = 0; i < colspan; i++) {
            Element col = doc.createElement("col");
            String temp = this.col_templates.get(i);
            if (temp != null) {
                col.setAttribute("template", temp);
            }
            table.appendChild(col);
        }
        final Element footer = doc.createElement("footer");
        for (final Iterator iter = this.footerRows.iterator(); iter.hasNext(); ) {
            final TableRow row = (TableRow) iter.next();
            row.paintAt(footer);
        }
        final Element body = doc.createElement("body");
        for (final Iterator iter = this.bodyRows.iterator(); iter.hasNext(); ) {
            final TableRow row = (TableRow) iter.next();
            row.paintAt(body);
        }
        table.appendChild(header);
        table.appendChild(footer);
        table.appendChild(body);
        element.appendChild(table);
        return table;
    }

    /**
     * @see pl.voidsystems.yajf.components.IContainer#getComponents()
     */
    public ComponentArrayList getComponents() {
        final ComponentArrayList result = new ComponentArrayList();
        for (TableRow row : this.headerRows) {
            result.addAll(row.getFlat());
        }
        for (TableRow row : this.footerRows) {
            result.addAll(row.getFlat());
        }
        for (TableRow row : this.bodyRows) {
            result.addAll(row.getFlat());
        }
        return result;
    }

    /**
     * Gets the live list off all table body rows
     * 
     * @return Returns the tableRow.
     */
    public ArrayList<TableRow> getBodyRows() {
        return this.bodyRows;
    }

    /**
     * Adds a row to the table body, also automaticaly sets colspan equal to quantity of rows.
     * 
     * @param row
     *            Row to be added
     */
    public void addBodyRow(final TableRow row) {
        this.bodyRows.add(row);
        this.setColspan(row.size());
    }

    /**
     * Adds a list of rows to the body table. On each element in list it launches <tt>addBodyRow</tt>
     * 
     * @param rows
     *            Rows to be added
     */
    public void setBodyRows(final ArrayList<TableRow> rows) {
        for (final Iterator iter = rows.iterator(); iter.hasNext(); ) {
            final TableRow element = (TableRow) iter.next();
            this.addBodyRow(element);
        }
    }

    /**
     * Getter of the property <tt>headerRows</tt>
     * 
     * @return Returns the headerRows.
     */
    public ArrayList<TableRow> getHeaderRows() {
        return this.headerRows;
    }

    /**
     * Adds a row to the table header
     * 
     * @param row
     *            Row to be added
     */
    public void addHeaderRow(final TableRow row) {
        this.headerRows.add(row);
    }

    /**
     * Adds an array of rows to the table header
     * 
     * @param rows
     *            Rows to be added
     */
    public void setHeaderRows(final ArrayList<TableRow> rows) {
        for (final Iterator iter = rows.iterator(); iter.hasNext(); ) {
            final TableRow element = (TableRow) iter.next();
            this.addHeaderRow(element);
        }
    }

    /**
     * Getter of the property <tt>footerRows</tt>
     * 
     * @return Returns the footerRows.
     */
    public ArrayList<TableRow> getFooterRows() {
        return this.footerRows;
    }

    /**
     * Adds a body row to the table footer
     * 
     * @param row
     *            Row to be added
     */
    public void addFooterRow(final TableRow row) {
        this.footerRows.add(row);
    }

    /**
     * Adds an array of rows to the table footer
     * 
     * @param rows
     *            Rows to be added
     */
    public void setFooterRows(final ArrayList<TableRow> rows) {
        for (final Iterator iter = rows.iterator(); iter.hasNext(); ) {
            final TableRow element = (TableRow) iter.next();
            this.addFooterRow(element);
        }
    }

    /**
     * @see pl.voidsystems.yajf.components.IContainer#remove(java.lang.Object)
     */
    public boolean remove(final Object object) {
        return this.headerRows.remove(object) || this.bodyRows.remove(object) || this.footerRows.remove(object);
    }

    /**
     * Getter of the <tt>title</tt> property.
     * 
     * @return Current title of the table
     * @uml.property name="title"
     */
    @ASynchronized(setter = "setTitle")
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter of the <tt>title</tt> property.
     * 
     * @param title
     *            Sets new table title
     * @uml.property name="title"
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Getter of the <tt>width</tt> property.
     * 
     * @return Width of the table
     * @uml.property name="colspan"
     */
    public int getColspan() {
        if (this.colspan == -1) {
            this.colspan = 1;
            if (this.bodyRows.size() > 0) {
                this.colspan = Math.max(this.colspan, this.bodyRows.get(0).size());
            }
            if (this.headerRows.size() > 0) {
                this.colspan = Math.max(this.colspan, this.headerRows.get(0).size());
            }
            if (this.footerRows.size() > 0) {
                this.colspan = Math.max(this.colspan, this.footerRows.get(0).size());
            }
        }
        return this.colspan;
    }

    /**
     * Sets the current width of the table
     * 
     * @param value
     *            Maximum number of cells in one row in the table
     * @uml.property name="colspan"
     */
    public void setColspan(final int value) {
        this.colspan = value;
    }

    public void setColTemplate(int index, String template) {
        this.col_templates.put(index, template);
    }

    public String getColTemplate(int index) {
        return this.col_templates.get(index);
    }
}
