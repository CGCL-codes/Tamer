package celtrum.libmanage.tests;

import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.*;
import org.xml.sax.SAXException;
import celtrum.libmanage.books.*;
import celtrum.libmanage.xml.*;

public class BookTestXML {

    IBookReader reader;

    IBookLibrary books;

    @Before
    public void loadXML() throws ParserConfigurationException {
        reader = new BookReader();
    }

    @After
    public void end() {
        reader = null;
        books = null;
    }

    @Test
    public void testCorrect() throws XPathExpressionException, IOException, SAXException, InvalidXmlFormatException {
        books = reader.readXML("\\test\\celtrum\\libmanage\\tests\\Books.xml");
        IBook book = books.getBooks().elementAt(0);
        String expectedTitle = "Harry Plotter and the Coordinates of Phoenix";
        String expectedAuthor = "Rowling,JP";
        String expectedDDS = "357";
        Assert.assertEquals("Incorrect Title", expectedTitle, book.getInfo(BookInfo.TITLE));
        Assert.assertEquals("Incorrect Author", expectedAuthor, book.getInfo(BookInfo.AUTHOR));
        Assert.assertEquals("Incorrect DDS", expectedDDS, book.getInfo(BookInfo.DDS));
        Assert.assertEquals("Incorrect Second Author", "Rowling,JP", books.getBooks().elementAt(1).getInfo(BookInfo.AUTHOR));
    }

    @Test
    public void testSearch() throws XPathExpressionException, IOException, SAXException, InvalidXmlFormatException {
        books = reader.readXML("\\test\\celtrum\\libmanage\\tests\\Books.xml");
        IBookSearch search = new BookSearch();
        search.addInfo(BookInfo.TITLE, "Mysterious");
        search.addInfo(BookInfo.AUTHOR, "Snicket,Bah");
        search.addInfo(BookInfo.DDS, "2");
        Vector<IBook> result = books.search(search);
        Assert.assertEquals("954", result.elementAt(0).getInfo(BookInfo.DDS));
    }

    @Test(expected = InvalidXmlFormatException.class)
    public void testIncorrect() throws XPathExpressionException, IOException, SAXException, InvalidXmlFormatException {
        reader.readXML("\\test\\celtrum\\libmanage\\tests\\Invalid.xml");
    }
}
