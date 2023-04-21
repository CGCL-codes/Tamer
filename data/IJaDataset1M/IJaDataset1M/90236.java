package com.ondelette.servlet.webforum;

import java.io.File;
import java.io.Reader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateField;

/** A utility for making Lucene Documents from a File. */
public class FileDocument {

    /** Makes a document for a File.
    <p>
    The document has three fields:
    <ul>
    <li><code>path</code>--containing the pathname of the file, as a stored,
    tokenized field;
    <li><code>modified</code>--containing the last modified date of the file as
    a keyword field as encoded by <a
    href="lucene.document.DateField.html">DateField</a>; and
    <li><code>contents</code>--containing the full contents of the file, as a
    Reader field;
    */
    public static Document Document(File f, String subject, String author) throws java.io.FileNotFoundException {
        Document doc = new Document();
        if (subject != null) doc.add(Field.Text("subject", subject)); else doc.add(Field.Text("subject", ""));
        if (f.exists()) {
            FileInputStream is = new FileInputStream(f);
            Reader reader = new BufferedReader(new InputStreamReader(is));
            doc.add(Field.Text("message", reader));
        } else doc.add(Field.Text("message", ""));
        if (author != null) doc.add(Field.Keyword("author", author)); else doc.add(Field.Keyword("author", ""));
        doc.add(Field.Keyword("modified", DateField.timeToString(f.lastModified())));
        return doc;
    }

    public static Document Document(String subject, String message, String path, String author, String lastModified) {
        Document doc = new Document();
        if (subject != null) doc.add(Field.Text("subject", subject)); else doc.add(Field.Text("subject", ""));
        if (message != null) doc.add(Field.Text("message", message)); else doc.add(Field.Text("message", ""));
        if (path != null) doc.add(Field.Text("path", path)); else doc.add(Field.Text("path", ""));
        if (author != null) doc.add(Field.Keyword("author", author)); else doc.add(Field.Keyword("author", ""));
        if (lastModified == null) {
            Date now = new Date();
            doc.add(Field.Keyword("lastModified", DateField.timeToString(now.getTime())));
        } else {
            doc.add(Field.Keyword("lastModified", lastModified));
        }
        return doc;
    }

    public static Document Document(String subject, String message, String path, String author) {
        Document doc = new Document();
        if (subject != null) doc.add(Field.Text("subject", subject)); else doc.add(Field.Text("subject", ""));
        if (message != null) doc.add(Field.Text("message", message)); else doc.add(Field.Text("message", ""));
        if (path != null) doc.add(Field.Text("path", path)); else doc.add(Field.Text("path", ""));
        if (author != null) doc.add(Field.Keyword("author", author)); else doc.add(Field.Keyword("author", ""));
        Date now = new Date();
        doc.add(Field.Keyword("lastModified", DateField.timeToString(now.getTime())));
        return doc;
    }

    private FileDocument() {
    }
}
