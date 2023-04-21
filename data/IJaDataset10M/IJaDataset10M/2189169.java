package net.narusas.cafelibrary.serial;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.narusas.cafelibrary.Book;
import net.narusas.cafelibrary.BookList;
import net.narusas.cafelibrary.Borrower;
import net.narusas.cafelibrary.Config;
import net.narusas.cafelibrary.Library;
import net.narusas.cafelibrary.LibrarySerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLSerializer implements LibrarySerializer {

    public byte[] serialize(Library lib) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        makeLibrary(doc, lib);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t;
        try {
            t = tf.newTransformer();
            t.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(bout, Config.CHARSET)));
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
        return bout.toByteArray();
    }

    private void makeLibrary(Document doc, Library lib) {
        Element root = doc.createElement("library");
        doc.appendChild(root);
        Element books = doc.createElement("books");
        root.appendChild(books);
        makeBooks(books, doc, lib);
        Element booklists = doc.createElement("booklists");
        root.appendChild(booklists);
        makeBookLists(booklists, doc, lib);
        Element borrowers = doc.createElement("borrowers");
        root.appendChild(borrowers);
        makeBorrowers(borrowers, doc, lib);
    }

    private void makeBooks(Element books, Document doc, Library lib) {
        for (int i = 0; i < lib.getBookSize(); i++) {
            Book book = lib.get(i);
            makeBook(books, doc, book);
        }
    }

    private void makeBook(Element books, Document doc, Book book) {
        Element bookNode = doc.createElement("book");
        bookNode.setAttribute("id", String.valueOf(book.getId()));
        bookNode.setAttribute("author", book.getAuthor());
        bookNode.setAttribute("category", book.getCategory());
        bookNode.setAttribute("coverLargeUrl", book.getCoverLargeUrl());
        bookNode.setAttribute("coverSmallUrl", book.getCoverSmallUrl());
        bookNode.setAttribute("description", book.getDescription());
        bookNode.setAttribute("isbn", book.getIsbn());
        bookNode.setAttribute("notes", book.getNotes());
        bookNode.setAttribute("originalPrice", book.getOriginalPrice());
        bookNode.setAttribute("publisher", book.getPublisher());
        bookNode.setAttribute("salePrice", book.getSalePrice());
        bookNode.setAttribute("title", book.getTitle());
        bookNode.setAttribute("translator", book.getTranslator());
        bookNode.setAttribute("publishDate", book.getPublishDate() == null ? null : String.valueOf(book.getPublishDate().getTime()));
        bookNode.setAttribute("purchaseDate", book.getPurchaseDate() == null ? null : String.valueOf(book.getPurchaseDate().getTime()));
        bookNode.setAttribute("favorite", String.valueOf(book.getFavorite()));
        books.appendChild(bookNode);
    }

    private void makeBookLists(Element booklists, Document doc, Library lib) {
        for (int i = 0; i < lib.sizeOfBookLists(); i++) {
            BookList bList = lib.getBookList(i);
            makeBooksList(bList, booklists, doc);
        }
    }

    private void makeBooksList(BookList list, Element booklists, Document doc) {
        Element bListNode = doc.createElement("booklist");
        bListNode.setAttribute("id", String.valueOf(list.getId()));
        bListNode.setAttribute("name", list.getName());
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            buf.append(list.get(i).getId()).append(",");
        }
        bListNode.setAttribute("books", buf.toString());
        booklists.appendChild(bListNode);
    }

    private void makeBorrowers(Element borrowers, Document doc, Library lib) {
        for (int i = 0; i < lib.sizeOfBorrowers(); i++) {
            Borrower borrower = lib.getBorrower(i);
            makeBorrower(borrower, borrowers, doc);
        }
    }

    private void makeBorrower(Borrower borrower, Element borrowers, Document doc) {
        Element borrowerNode = doc.createElement("borrower");
        borrowerNode.setAttribute("id", String.valueOf(borrower.getId()));
        borrowerNode.setAttribute("name", borrower.getName());
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < borrower.size(); i++) {
            buf.append(borrower.get(i).getId()).append(",");
        }
        borrowerNode.setAttribute("books", buf.toString());
        borrowers.appendChild(borrowerNode);
    }
}
