package fr.albin.data.model.dao.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import fr.albin.compiler.dynacode.Book;
import fr.albin.data.model.Item;
import fr.albin.data.model.codes.Codes;
import fr.albin.data.model.codes.Codes.Code;

/**
 * TODO create a JDOM and the use an XMLOutputter.
 * @author avigier
 *
 */
public class XmlPersistenceTest extends TestCase {

    public void testMarshallWithBook() {
        Book book = new Book();
        book.setId(1);
        book.setComment("test");
        Codes codes = new Codes();
        List<Code> authors = new ArrayList<Code>();
        Code code = new Code();
        code.setId(1);
        authors.add(code);
        code = new Code();
        code.setId(2);
        authors.add(code);
        codes.getCode().addAll(authors);
        book.setAuthor(codes);
        List<Item> items = new ArrayList<Item>();
        items.add(book);
        XmlPersistence persistence = new XmlPersistence();
        FileWriter writer = null;
        try {
            writer = new FileWriter("tests/tmp/books.xml");
            persistence.marshall(writer, items);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testUnmarshall() {
        XmlPersistence persistence = new XmlPersistence();
        try {
            List<Item> books = persistence.unmarshall(new File("tests/tmp/books.xml"));
            assertEquals(1, books.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testJDom() {
        Element root = new Element("root");
        root.setAttribute("MyAttr", "MyValue");
        Element child = new Element("child1");
        child.setText("Here is my child, it is so funny ;) && and � and � there are weird charcters");
        root.addContent(child);
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        XMLOutputter outputter = new XMLOutputter(format);
        try {
            outputter.output(root, new FileWriter("tests/tmp/outputter.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
