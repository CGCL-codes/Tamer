package com.emental.mindraider.core.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * An utility for making Lucene Documents from a File.
 * 
 * @author Martin Dvorak
 */
public final class FileDocument {

    /**
	 * Makes a document for a File.
	 * <p>
	 * The document has three fields:
	 * <ul>
	 * <li><code>path</code> containing the pathname of the file, as a
	 * stored, tokenized field;
	 * <li><code>modified</code> containing the last modified date of the
	 * file as a keyword field as encoded by <a
	 * href="lucene.document.DateField.html">DateField</a>; and
	 * <li><code>contents</code> containing the full contents of the file, as
	 * a Reader field;
	 * </ul>
	 */
    public static Document Document(File f, String notebookLabel, String conceptLabel, String conceptUri) throws java.io.FileNotFoundException {
        Document doc = new Document();
        Field field;
        field = new Field("uri", conceptUri, Field.Store.YES, Field.Index.UN_TOKENIZED);
        doc.add(field);
        field = new Field("path", f.getPath(), Field.Store.YES, Field.Index.NO);
        doc.add(field);
        field = new Field("conceptLabel", conceptLabel, Field.Store.YES, Field.Index.TOKENIZED);
        doc.add(field);
        field = new Field("notebookLabel", notebookLabel, Field.Store.YES, Field.Index.NO);
        doc.add(field);
        field = new Field("modified", DateTools.timeToString(f.lastModified(), DateTools.Resolution.SECOND), Field.Store.YES, Field.Index.NO);
        doc.add(field);
        FileInputStream is = new FileInputStream(f);
        Reader reader = new BufferedReader(new InputStreamReader(is));
        field = new Field("contents", reader);
        doc.add(field);
        return doc;
    }

    private FileDocument() {
    }
}
