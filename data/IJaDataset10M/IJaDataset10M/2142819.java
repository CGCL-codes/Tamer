package model;

import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;
import org.w3c.dom.Document;

/**
 *
 * @author mariano
 */
public class Revision extends xml.Xmlizable {

    private int revision;

    private Date date;

    private Vector<Author> authors;

    private String description;

    /** Creates a new instance of Revision */
    public Revision() {
    }

    public Revision(Date fecha, Vector<Author> autores, String descripcion, int revision) {
        this.revision = revision;
        this.date = fecha;
        this.authors = autores;
        this.description = descripcion;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Vector<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Vector<Author> authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public xml.Coleccion toXmlColeccion() {
        xml.Coleccion col = new xml.Coleccion("revision");
        col.addNodo("revision-number", "" + revision);
        col.addNodo("revision-date", "" + DateFormat.getDateInstance().format(this.date));
        col.addNodo("revision-description", this.description);
        xml.Coleccion col1 = new xml.Coleccion("revision-authors");
        for (Author author : authors) {
            col1.addItem(author.toXmlColeccion());
        }
        col.addItem(col1);
        return col;
    }

    public static Revision fromXml(String str) {
        xml.Item col = xml.Item.fromXml(str);
        return parse(col);
    }

    public static Revision fromXml(Document doc) {
        xml.Item col = xml.Item.fromXml(doc.getFirstChild());
        return parse(col);
    }

    private static Revision parse(xml.Item col) {
        if (!col.isColeccion() || col.getName() != "revision") return null;
        xml.Coleccion c = (xml.Coleccion) col;
        xml.Nodo number = c.getNodo(0, "revision-number");
        xml.Nodo date = c.getNodo(1, "revision-date");
        xml.Nodo description = c.getNodo(2, "revision-description");
        xml.Coleccion authors = c.getColeccion(3, "revision-authors");
        if (number == null || date == null || description == null || authors == null) return null;
        Vector<Author> authorsVector = new Vector<Author>();
        for (int i = 0; i < authors.size(); i++) {
            xml.Coleccion authCol = authors.getColeccion(i, "author");
            if (authCol == null) continue;
            Author author = Author.fromXml(authCol.toString());
            authorsVector.add(author);
        }
        int revisionNumber = 1;
        try {
            revisionNumber = Integer.parseInt(number.getValue());
        } catch (Exception ex) {
        }
        Date revisionDate = new Date();
        try {
            revisionDate = DateFormat.getInstance().parse(date.getValue());
        } catch (Exception ex) {
        }
        return new Revision(revisionDate, authorsVector, description.getValue(), revisionNumber);
    }
}
