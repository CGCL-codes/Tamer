package Modelo;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pellizard
 */
public class Author {

    private String name;

    private String surname;

    private String email;

    /**
     * Creates a new instance of Author
     */
    public Author() {
        this("", "", "");
    }

    public Author(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String apellido) {
        this.surname = apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getCompleteName() {
        return this.getSurname() + "_" + this.getName();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Node toXml() {
        try {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = fact.newDocumentBuilder();
            Document doc = parser.newDocument();
            return toXml(doc);
        } catch (Exception ex) {
            return null;
        }
    }

    public Node toXml(Document doc) {
        try {
            Node root = doc.createElement("author");
            Node nombre = doc.createElement("name");
            nombre.appendChild(doc.createTextNode(this.name));
            root.appendChild(nombre);
            Node apellido = doc.createElement("surname");
            apellido.appendChild(doc.createTextNode(this.surname));
            root.appendChild(apellido);
            Node email = doc.createElement("email");
            email.appendChild(doc.createTextNode(this.email));
            root.appendChild(email);
            return root;
        } catch (Exception ex) {
            System.err.println("+============================+");
            System.err.println("|        XML Error           |");
            System.err.println("+============================+");
            System.err.println(ex.getClass());
            System.err.println(ex.getMessage());
            System.err.println("+============================+");
            ex.printStackTrace();
            return null;
        }
    }

    public static Author fromXml(String str) {
        ByteArrayInputStream is;
        try {
            is = new ByteArrayInputStream(str.getBytes("UTF8"));
        } catch (Exception e) {
            return null;
        }
        try {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = fact.newDocumentBuilder();
            Document doc = parser.parse(is);
            return fromXml(doc);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Author fromXml(Document doc) {
        String name = "", surname = "", email = null;
        try {
            NodeList nodes = doc.getElementsByTagName("name");
            if (nodes.getLength() == 1) {
                Node node = nodes.item(0);
                name = node.getFirstChild().getNodeValue();
            }
            nodes = doc.getElementsByTagName("surname");
            if (nodes.getLength() == 1) {
                Node node = nodes.item(0);
                surname = node.getFirstChild().getNodeValue();
            }
            nodes = doc.getElementsByTagName("email");
            if (nodes.getLength() == 1) {
                Node node = nodes.item(0);
                try {
                    email = node.getFirstChild().getNodeValue();
                } catch (Exception e) {
                    email = "";
                }
            }
            return new Author(name, surname, email);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
