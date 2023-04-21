package com.skruk.elvis.search.indexing;

import org.apache.lucene.index.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import java.util.*;
import java.io.*;
import com.skruk.elvis.beans.*;
import com.skruk.elvis.search.*;
import com.skruk.elvis.search.analysis.*;
import com.skruk.elvis.db.xml.*;
import java.lang.reflect.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;

/**
 *	Klasa odpowiedzialna za indeksowanie zasobów przy pomocy biblioteki Lucene
 */
public class IndexDocument {

    /**
   * Instancja klasy - pattern Singelton
   */
    private static IndexDocument instance = null;

    /**
   * Kontekst aplikacji
   */
    private javax.servlet.ServletContext context = null;

    /**
   * Obiekt analizatora wykorzystywane przez indekser
   */
    private Analyzer analyzer = null;

    /**
   * Czy ma być tworzony index jeżeli nie istnieje
   */
    private final boolean createFlag = false;

    /**
   * Nazwa katalogu z indeksem
   */
    private String indexDir = "index";

    /**
   * Mapowanie elementów i atrybutów opisu zasobu (XML Schema) do ich opisu indeksowania 
   */
    private final HashMap indexFields = new HashMap();

    /**
   * Pomocnicza tablica klas - wykorzystywana przy wywołaniach typu JavaReflection
   */
    private final Class[] as_CLASS = { java.lang.String.class, java.lang.String.class };

    /**
   * Obiekt odpowiedzialny za przekształcenia XSLT
   */
    private javax.xml.transform.Transformer transformer = null;

    /**
   * Obiekt odpowiedzialny za produkcję instancji potrzebnych do przekształcen XSLT
   */
    private javax.xml.transform.TransformerFactory tFactory = null;

    /**
   * Źródło XSL wykorzystywane w przekształceniach XSLT
   */
    private javax.xml.transform.Source xslSource = null;

    /**
   * Konstruktor prywatny - pattern Singelton
	 * @param context Kontekst aplikacji
   */
    private IndexDocument(javax.servlet.ServletContext context) {
        this.context = context;
        this.analyzer = PolishAnalyzer.getInstance(context);
        StringBuffer sb = new StringBuffer(context.getInitParameter("installDir"));
        sb.append(context.getInitParameter("indexPath"));
        this.indexDir = sb.toString();
        java.io.File fIndex = new java.io.File(indexDir + "segments");
        if (!fIndex.exists()) {
            try {
                IndexWriter writer = new IndexWriter(this.indexDir, this.analyzer, true);
                Document indexDoc = new Document();
                writer.addDocument(indexDoc);
                writer.close();
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        }
        ;
        try {
            sb.delete(0, sb.length());
            sb.append(context.getInitParameter("installDir")).append("xml/dtd/book.xsd");
            org.jdom.Document xmlDoc = new org.jdom.input.SAXBuilder().build(new File(sb.toString()));
            sb.delete(0, sb.length());
            sb.append(context.getInitParameter("installDir")).append("xsl/indexProperty.xsl");
            org.jdom.Document xslDoc = new org.jdom.input.SAXBuilder().build(new File(sb.toString()));
            org.jdom.Document indexFieldsDoc = XsltHelper.getInstance(context).transform(xmlDoc, xslDoc, (Map) null);
            Iterator itFields = indexFieldsDoc.getRootElement().getChildren("field").iterator();
            while (itFields.hasNext()) {
                org.jdom.Element el = (org.jdom.Element) itFields.next();
                this.indexFields.put(el.getAttribute("id").getValue(), el.getAttribute("index").getValue());
            }
        } catch (org.jdom.JDOMException ex) {
            ex.printStackTrace();
        }
        try {
            tFactory = javax.xml.transform.TransformerFactory.newInstance();
            xslSource = new javax.xml.transform.stream.StreamSource(new java.io.File(new StringBuffer(context.getInitParameter("installDir")).append("xsl/checkXml.xsl").toString()).toURL().openStream());
            transformer = tFactory.newTransformer(xslSource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
   * Umożliwia dostęp do instacji klasy - pattern Singelton
   */
    public static IndexDocument getInstance(javax.servlet.ServletContext context) {
        if (instance == null) {
            instance = new IndexDocument(context);
        }
        return instance;
    }

    /** 
   * Indeksowanie dokumetu XML w postaci JDOM
	 * @param document Dokument do zaindeksowania
	 * @param uriDoc URI jakie ma zostać skojarzone z danym dokumentem
   */
    public void index(org.jdom.Document document, String uriDoc) throws java.io.IOException {
        IndexWriter writer = new IndexWriter(this.indexDir, this.analyzer, this.createFlag);
        Document indexDoc = this.createDocument(document);
        indexDoc.add(Field.Keyword("uriDoc", uriDoc));
        writer.addDocument(indexDoc);
        if ((writer.docCount() % 10) == 0) {
            writer.optimize();
        }
        writer.close();
    }

    /**
   *  Indeksowanie zawartości bazy wiedzy
	 * @param hmDate Baza wiedzy do zaindeksowania w postaci mapy (predicate, value)
   */
    public void indexRDF(HashMap hmDate) throws java.io.IOException {
        IndexWriter writer = new IndexWriter(this.indexDir, this.analyzer, this.createFlag);
        Document indexRdf = new Document();
        Iterator it = hmDate.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.trim().length() > 0) {
                String value = new String(((String) hmDate.get(key)).getBytes("ISO8859-1"));
                indexRdf.add(Field.Text(key, value));
            }
        }
        ;
        writer.addDocument(indexRdf);
        if ((writer.docCount() % 10) == 0) {
            writer.optimize();
        }
        writer.close();
    }

    /**
   * Tworzenie ideksu dokumentu XML
	 * @param document Dokument XML do zaindeksowania
	 * @return Wynikowy dokument reprezentujący indeksowany dokument XML
   */
    protected Document createDocument(org.jdom.Document document) {
        Document result = new Document();
        StringBuffer buffer = new StringBuffer();
        this.indexElement(document.getRootElement(), result, buffer);
        result.add(Field.UnStored("doc", buffer.toString()));
        return result;
    }

    /**
   * Indeksowanie elementu XML (rekurencyjne)
	 * @param element Element XML do zaindeksowania
	 * @param indexDoc Wynikowy dokument indeksu
	 * @param buffer Buffor do tworzenia całościowego opisu zasobu
   */
    protected void indexElement(org.jdom.Element element, Document indexDoc, StringBuffer buffer) {
        String elName = element.getName();
        StringBuffer sb = new StringBuffer(elName).append("_");
        Iterator itAttr = element.getAttributes().iterator();
        while (itAttr.hasNext()) {
            org.jdom.Attribute attr = (org.jdom.Attribute) itAttr.next();
            String name = attr.getName();
            String value = attr.getValue();
            sb.delete(elName.length() + 1, sb.length()).append(name);
            String type = (String) this.indexFields.get(sb.toString());
            if (value != null && value.length() > 0 && type != null && !type.equals("none") && !type.equals("process")) {
                String[] asField = { name, value };
                try {
                    Method m = org.apache.lucene.document.Field.class.getMethod(type, as_CLASS);
                    Field field = (Field) m.invoke(null, asField);
                    indexDoc.add(field);
                } catch (java.lang.NoSuchMethodException nsme) {
                    nsme.printStackTrace();
                } catch (java.lang.IllegalAccessException iae) {
                    iae.printStackTrace();
                } catch (java.lang.reflect.InvocationTargetException ite) {
                    ite.printStackTrace();
                }
            } else if (value != null && value.length() > 0 && type != null && type.equals("process")) {
                try {
                    this.indexResource(value, buffer);
                } catch (Exception xmlex) {
                    xmlex.printStackTrace();
                }
            }
        }
        itAttr = element.getChildren().iterator();
        while (itAttr.hasNext()) {
            this.indexElement((org.jdom.Element) itAttr.next(), indexDoc, buffer);
        }
    }

    /**
   * Indeksowanie zasobu z bazy danych XML
	 * @param value ID zasobu w bazie XML
	 * @param buffer Buffor do tworzenia całościowego opisu zasobu
   */
    protected StringBuffer indexResource(String value, StringBuffer buffer) throws org.xmldb.api.base.XMLDBException, com.skruk.elvis.db.DbException {
        DbEngine dbe = DbEngine.createInstance(context);
        dbe.loadCollection("/elvis/books");
        XMLResource xmlRes = dbe.getDocument(value);
        if (xmlRes != null) {
            String sXmlDoc = ((String) xmlRes.getContent());
            Reader isDoc = new StringReader(sXmlDoc);
            StringWriter wDoc = new StringWriter();
            try {
                javax.xml.transform.stream.StreamSource xmlSource = new javax.xml.transform.stream.StreamSource(isDoc);
                transformer.transform(xmlSource, new javax.xml.transform.stream.StreamResult(wDoc));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            buffer.append(" ").append(wDoc.getBuffer());
        }
        ;
        return buffer;
    }
}
