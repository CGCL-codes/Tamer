package com.jeantessier.diff;

import java.io.*;
import javax.xml.parsers.*;
import junit.framework.*;
import org.apache.oro.text.perl.*;
import org.xml.sax.*;

public class TestListDiffPrinter extends TestCase implements ErrorHandler {

    private static final String SPECIFIC_ENCODING = "iso-latin-1";

    private static final String SPECIFIC_DTD_PREFIX = "./etc";

    private XMLReader reader;

    private Perl5Util perl;

    protected void setUp() throws Exception {
        boolean validate = Boolean.getBoolean("DEPENDENCYFINDER_TESTS_VALIDATE");
        reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        reader.setFeature("http://xml.org/sax/features/validation", validate);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", validate);
        reader.setErrorHandler(this);
        perl = new Perl5Util();
    }

    public void testDefaultDTDPrefix() {
        ListDiffPrinter printer = new ListDiffPrinter();
        String xmlDocument = printer.toString();
        assertTrue(xmlDocument + "Missing DTD", perl.match("/DOCTYPE \\S+ SYSTEM \"(.*)\"/", xmlDocument));
        assertTrue("DTD \"" + perl.group(1) + "\" does not have prefix \"" + ListDiffPrinter.DEFAULT_DTD_PREFIX + "\"", perl.group(1).startsWith(ListDiffPrinter.DEFAULT_DTD_PREFIX));
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
    }

    public void testSpecificDTDPrefix() {
        ListDiffPrinter printer = new ListDiffPrinter(ListDiffPrinter.DEFAULT_ENCODING, SPECIFIC_DTD_PREFIX);
        String xmlDocument = printer.toString();
        assertTrue(xmlDocument + "Missing DTD", perl.match("/DOCTYPE \\S+ SYSTEM \"(.*)\"/", xmlDocument));
        assertTrue("DTD \"" + perl.group(1) + "\" does not have prefix \"./etc\"", perl.group(1).startsWith(SPECIFIC_DTD_PREFIX));
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
    }

    public void testDefaultEncoding() {
        ListDiffPrinter printer = new ListDiffPrinter();
        String xmlDocument = printer.toString();
        assertTrue(xmlDocument + "Missing encoding", perl.match("/encoding=\"([^\"]*)\"/", xmlDocument));
        assertEquals("Encoding", ListDiffPrinter.DEFAULT_ENCODING, perl.group(1));
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
    }

    public void testSpecificEncoding() {
        ListDiffPrinter printer = new ListDiffPrinter(SPECIFIC_ENCODING, ListDiffPrinter.DEFAULT_DTD_PREFIX);
        String xmlDocument = printer.toString();
        assertTrue(xmlDocument + "Missing encoding", perl.match("/encoding=\"([^\"]*)\"/", xmlDocument));
        assertEquals("Encoding", SPECIFIC_ENCODING, perl.group(1));
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
    }

    public void testDefault() {
        ListDiffPrinter printer = new ListDiffPrinter(ListDiffPrinter.DEFAULT_ENCODING, SPECIFIC_DTD_PREFIX);
        printer.remove("java.lang.Object");
        printer.remove("java.lang.Object.Object()");
        printer.remove("java.lang.String");
        printer.remove("java.util");
        printer.remove("java.util.Collection.add(java.lang.Object)");
        printer.remove("java.util.Collection.addAll(java.util.Collection)");
        printer.add("java.lang.Thread");
        printer.add("java.lang.Thread.Thread()");
        printer.add("java.lang.System");
        printer.add("java.io");
        printer.add("java.io.PrintStream.println(java.lang.Object)");
        printer.add("java.io.PrintWriter.println(java.lang.Object)");
        String xmlDocument = printer.toString();
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
        assertTrue("java.lang.Object not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object</line>") != -1);
        assertTrue("java.lang.Object.Object() not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object.Object()</line>") != -1);
        assertTrue("java.lang.String not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.String</line>") != -1);
        assertTrue("java.util not in " + xmlDocument, xmlDocument.indexOf("<line>java.util</line>") != -1);
        assertTrue("java.util.Collection.add(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") != -1);
        assertTrue("java.util.Collection.addAll(java.util.Collection) not in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") != -1);
        assertTrue("java.lang.Thread not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread</line>") != -1);
        assertTrue("java.lang.Thread.Thread() not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread.Thread()</line>") != -1);
        assertTrue("java.lang.System not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.System</line>") != -1);
        assertTrue("java.io not in " + xmlDocument, xmlDocument.indexOf("<line>java.io</line>") != -1);
        assertTrue("java.io.PrintStream.println(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") != -1);
        assertTrue("java.io.PrintWriter.println(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") != -1);
    }

    public void testFullList() {
        ListDiffPrinter printer = new ListDiffPrinter(false, ListDiffPrinter.DEFAULT_ENCODING, SPECIFIC_DTD_PREFIX);
        printer.remove("java.lang.Object");
        printer.remove("java.lang.Object.Object()");
        printer.remove("java.lang.String");
        printer.remove("java.util");
        printer.remove("java.util.Collection.add(java.lang.Object)");
        printer.remove("java.util.Collection.addAll(java.util.Collection)");
        printer.add("java.lang.Thread");
        printer.add("java.lang.Thread.Thread()");
        printer.add("java.lang.System");
        printer.add("java.io");
        printer.add("java.io.PrintStream.println(java.lang.Object)");
        printer.add("java.io.PrintWriter.println(java.lang.Object)");
        String xmlDocument = printer.toString();
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
        assertTrue("java.lang.Object not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object</line>") != -1);
        assertTrue("java.lang.Object.Object() not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object.Object()</line>") != -1);
        assertTrue("java.lang.String not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.String</line>") != -1);
        assertTrue("java.util not in " + xmlDocument, xmlDocument.indexOf("<line>java.util</line>") != -1);
        assertTrue("java.util.Collection.add(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") != -1);
        assertTrue("java.util.Collection.addAll(java.util.Collection) not in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") != -1);
        assertTrue("java.lang.Thread not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread</line>") != -1);
        assertTrue("java.lang.Thread.Thread() not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread.Thread()</line>") != -1);
        assertTrue("java.lang.System not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.System</line>") != -1);
        assertTrue("java.io not in " + xmlDocument, xmlDocument.indexOf("<line>java.io</line>") != -1);
        assertTrue("java.io.PrintStream.println(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") != -1);
        assertTrue("java.io.PrintWriter.println(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") != -1);
    }

    public void testCompressedList() {
        ListDiffPrinter printer = new ListDiffPrinter(true, ListDiffPrinter.DEFAULT_ENCODING, SPECIFIC_DTD_PREFIX);
        printer.remove("java.lang.Object [C]");
        printer.remove("java.lang.Object.Object() [F]");
        printer.remove("java.lang.String [C]");
        printer.remove("java.util [P]");
        printer.remove("java.util.Collection.add(java.lang.Object) [F]");
        printer.remove("java.util.Collection.addAll(java.util.Collection) [F]");
        printer.add("java.lang.Thread [C]");
        printer.add("java.lang.Thread.Thread() [F]");
        printer.add("java.lang.System [C]");
        printer.add("java.io [P]");
        printer.add("java.io.PrintStream.println(java.lang.Object) [F]");
        printer.add("java.io.PrintWriter.println(java.lang.Object) [F]");
        String xmlDocument = printer.toString();
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
        assertTrue("java.lang.Object not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object</line>") != -1);
        assertTrue("java.lang.Object.Object() in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object.Object()</line>") == -1);
        assertTrue("java.lang.String not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.String</line>") != -1);
        assertTrue("java.util not in " + xmlDocument, xmlDocument.indexOf("<line>java.util</line>") != -1);
        assertTrue("java.util.Collection.add(java.lang.Object) in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") == -1);
        assertTrue("java.util.Collection.addAll(java.util.Collection) in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") == -1);
        assertTrue("java.lang.Thread not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread</line>") != -1);
        assertTrue("java.lang.Thread.Thread() in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread.Thread()</line>") == -1);
        assertTrue("java.lang.System not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.System</line>") != -1);
        assertTrue("java.io not in " + xmlDocument, xmlDocument.indexOf("<line>java.io</line>") != -1);
        assertTrue("java.io.PrintStream.println(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") == -1);
        assertTrue("java.io.PrintWriter.println(java.lang.Object) in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") == -1);
    }

    public void testCompressedListWithoutSuffixes() {
        ListDiffPrinter printer = new ListDiffPrinter(true, ListDiffPrinter.DEFAULT_ENCODING, SPECIFIC_DTD_PREFIX);
        printer.remove("java.lang.Object");
        printer.remove("java.lang.Object.Object()");
        printer.remove("java.lang.String");
        printer.remove("java.util");
        printer.remove("java.util.Collection.add(java.lang.Object)");
        printer.remove("java.util.Collection.addAll(java.util.Collection)");
        printer.add("java.lang.Thread");
        printer.add("java.lang.Thread.Thread()");
        printer.add("java.lang.System");
        printer.add("java.io");
        printer.add("java.io.PrintStream.println(java.lang.Object)");
        printer.add("java.io.PrintWriter.println(java.lang.Object)");
        String xmlDocument = printer.toString();
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
        assertTrue("java.lang.Object not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object</line>") != -1);
        assertTrue("java.lang.Object.Object() not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Object.Object()</line>") != -1);
        assertTrue("java.lang.String not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.String</line>") != -1);
        assertTrue("java.util not in " + xmlDocument, xmlDocument.indexOf("<line>java.util</line>") != -1);
        assertTrue("java.util.Collection.add(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") != -1);
        assertTrue("java.util.Collection.addAll(java.util.Collection) not in " + xmlDocument, xmlDocument.indexOf("<line>java.util.Collection.add(java.lang.Object)</line>") != -1);
        assertTrue("java.lang.Thread not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread</line>") != -1);
        assertTrue("java.lang.Thread.Thread() not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.Thread.Thread()</line>") != -1);
        assertTrue("java.lang.System not in " + xmlDocument, xmlDocument.indexOf("<line>java.lang.System</line>") != -1);
        assertTrue("java.io not in " + xmlDocument, xmlDocument.indexOf("<line>java.io</line>") != -1);
        assertTrue("java.io.PrintStream.println(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") != -1);
        assertTrue("java.io.PrintWriter.println(java.lang.Object) not in " + xmlDocument, xmlDocument.indexOf("<line>java.io.PrintStream.println(java.lang.Object)</line>") != -1);
    }

    public void testLegitimateSuffixes() {
        ListDiffPrinter printer = new ListDiffPrinter(true, ListDiffPrinter.DEFAULT_ENCODING, SPECIFIC_DTD_PREFIX);
        printer.remove("removed [P]");
        printer.remove("removed.Removed [C]");
        printer.remove("removed.Removed.Removed() [F]");
        printer.remove("removed.Removed.removed [F]");
        printer.remove("removed.OtherRemoved.OtherRemoved() [F]");
        printer.remove("removed.OtherRemoved.other_removed [F]");
        printer.remove("removedpackage [P]");
        printer.remove("removed.package.internal [P]");
        printer.remove("other.removed.Removed [C]");
        printer.remove("other.removed.Removed.Removed() [F]");
        printer.remove("other.removed.OtherRemoved.OtherRemoved() [F]");
        printer.add("add [P]");
        printer.add("add.Add [C]");
        printer.add("add.Add.Add() [F]");
        printer.add("add.Add.add [F]");
        printer.add("add.OtherAdd.OtherAdd() [F]");
        printer.add("add.OtherAdd.add [F]");
        printer.add("addpackage [P]");
        printer.add("add.package.internal [P]");
        printer.add("other.add.Add [C]");
        printer.add("other.add.Add.Add() [F]");
        printer.add("other.add.OtherAdd.OtherAdd() [F]");
        String xmlDocument = printer.toString();
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
        assertTrue("removed not in " + xmlDocument, xmlDocument.indexOf("<line>removed</line>") != -1);
        assertTrue("removed.Removed in " + xmlDocument, xmlDocument.indexOf("<line>removed.Removed</line>") == -1);
        assertTrue("removed.Removed.Removed() in " + xmlDocument, xmlDocument.indexOf("<line>removed.Removed.Removed()</line>") == -1);
        assertTrue("removed.Removed.removed in " + xmlDocument, xmlDocument.indexOf("<line>removed.Removed.removed</line>") == -1);
        assertTrue("removed.OtherRemoved.OtherRemoved() in " + xmlDocument, xmlDocument.indexOf("<line>removed.OtherRemoved.OtherRemoved()</line>") == -1);
        assertTrue("removed.OtherRemoved.other_removed in " + xmlDocument, xmlDocument.indexOf("<line>removed.OtherRemoved.other_removed</line>") == -1);
        assertTrue("removedpackage not in " + xmlDocument, xmlDocument.indexOf("<line>removedpackage</line>") != -1);
        assertTrue("removed.package.internal not in " + xmlDocument, xmlDocument.indexOf("<line>removed.package.internal</line>") != -1);
        assertTrue("other.removed.Removed not in " + xmlDocument, xmlDocument.indexOf("<line>other.removed.Removed</line>") != -1);
        assertTrue("other.removed.Removed.Removed() in " + xmlDocument, xmlDocument.indexOf("<line>other.removed.Removed.Removed()</line>") == -1);
        assertTrue("other.removed.OtherRemoved.OtherRemoved() not in " + xmlDocument, xmlDocument.indexOf("<line>other.removed.OtherRemoved.OtherRemoved()</line>") != -1);
        assertTrue("add not in " + xmlDocument, xmlDocument.indexOf("<line>add</line>") != -1);
        assertTrue("add.Add in " + xmlDocument, xmlDocument.indexOf("<line>add.Add</line>") == -1);
        assertTrue("add.Add.Add() in " + xmlDocument, xmlDocument.indexOf("<line>add.Add.Add()</line>") == -1);
        assertTrue("add.Add.add in " + xmlDocument, xmlDocument.indexOf("<line>add.Add.add</line>") == -1);
        assertTrue("add.OtherAdd.OtherAdd() in " + xmlDocument, xmlDocument.indexOf("<line>add.OtherAdd.OtherAdd()</line>") == -1);
        assertTrue("add.OtherAdd.other_add in " + xmlDocument, xmlDocument.indexOf("<line>add.OtherAdd.other_add</line>") == -1);
        assertTrue("addpackage not in " + xmlDocument, xmlDocument.indexOf("<line>addpackage</line>") != -1);
        assertTrue("add.package.internal not in " + xmlDocument, xmlDocument.indexOf("<line>add.package.internal</line>") != -1);
        assertTrue("other.add.Add not in " + xmlDocument, xmlDocument.indexOf("<line>other.add.Add</line>") != -1);
        assertTrue("other.add.Add.Add() in " + xmlDocument, xmlDocument.indexOf("<line>other.add.Add.Add()</line>") == -1);
        assertTrue("other.add.OtherAdd.OtherAdd() not in " + xmlDocument, xmlDocument.indexOf("<line>other.add.OtherAdd.OtherAdd()</line>") != -1);
    }

    public void testNoSuffixes() {
        ListDiffPrinter printer = new ListDiffPrinter(true, ListDiffPrinter.DEFAULT_ENCODING, SPECIFIC_DTD_PREFIX);
        printer.remove("removed");
        printer.remove("removed.Removed");
        printer.remove("removed.Removed.Removed()");
        printer.remove("removed.Removed.removed");
        printer.remove("removed.OtherRemoved.OtherRemoved()");
        printer.remove("removed.OtherRemoved.other_removed");
        printer.remove("removedpackage");
        printer.remove("removed.package.internal");
        printer.remove("other.removed.Removed");
        printer.remove("other.removed.Removed.Removed()");
        printer.remove("other.removed.OtherRemoved.OtherRemoved()");
        printer.add("add");
        printer.add("add.Add");
        printer.add("add.Add.Add()");
        printer.add("add.Add.add");
        printer.add("add.OtherAdd.OtherAdd()");
        printer.add("add.OtherAdd.other_add");
        printer.add("addpackage");
        printer.add("add.package.internal");
        printer.add("other.add.Add");
        printer.add("other.add.Add.Add()");
        printer.add("other.add.OtherAdd.OtherAdd()");
        String xmlDocument = printer.toString();
        try {
            reader.parse(new InputSource(new StringReader(xmlDocument)));
        } catch (SAXException ex) {
            fail("Could not parse XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        } catch (IOException ex) {
            fail("Could not read XML Document: " + ex.getMessage() + "\n" + xmlDocument);
        }
        assertTrue("removed not in " + xmlDocument, xmlDocument.indexOf("<line>removed</line>") != -1);
        assertTrue("removed.Removed in " + xmlDocument, xmlDocument.indexOf("<line>removed.Removed</line>") != -1);
        assertTrue("removed.Removed.Removed() in " + xmlDocument, xmlDocument.indexOf("<line>removed.Removed.Removed()</line>") != -1);
        assertTrue("removed.Removed.removed in " + xmlDocument, xmlDocument.indexOf("<line>removed.Removed.removed</line>") != -1);
        assertTrue("removed.OtherRemoved.OtherRemoved() in " + xmlDocument, xmlDocument.indexOf("<line>removed.OtherRemoved.OtherRemoved()</line>") != -1);
        assertTrue("removed.OtherRemoved.other_removed in " + xmlDocument, xmlDocument.indexOf("<line>removed.OtherRemoved.other_removed</line>") != -1);
        assertTrue("removedpackage not in " + xmlDocument, xmlDocument.indexOf("<line>removedpackage</line>") != -1);
        assertTrue("removed.package.internal not in " + xmlDocument, xmlDocument.indexOf("<line>removed.package.internal</line>") != -1);
        assertTrue("other.removed.Removed not in " + xmlDocument, xmlDocument.indexOf("<line>other.removed.Removed</line>") != -1);
        assertTrue("other.removed.Removed.Removed() in " + xmlDocument, xmlDocument.indexOf("<line>other.removed.Removed.Removed()</line>") != -1);
        assertTrue("other.removed.OtherRemoved.OtherRemoved() not in " + xmlDocument, xmlDocument.indexOf("<line>other.removed.OtherRemoved.OtherRemoved()</line>") != -1);
        assertTrue("add not in " + xmlDocument, xmlDocument.indexOf("<line>add</line>") != -1);
        assertTrue("add.Add in " + xmlDocument, xmlDocument.indexOf("<line>add.Add</line>") != -1);
        assertTrue("add.Add.Add() in " + xmlDocument, xmlDocument.indexOf("<line>add.Add.Add()</line>") != -1);
        assertTrue("add.Add.add in " + xmlDocument, xmlDocument.indexOf("<line>add.Add.add</line>") != -1);
        assertTrue("add.OtherAdd.OtherAdd() in " + xmlDocument, xmlDocument.indexOf("<line>add.OtherAdd.OtherAdd()</line>") != -1);
        assertTrue("add.OtherAdd.other_add in " + xmlDocument, xmlDocument.indexOf("<line>add.OtherAdd.other_add</line>") != -1);
        assertTrue("addpackage not in " + xmlDocument, xmlDocument.indexOf("<line>addpackage</line>") != -1);
        assertTrue("add.package.internal not in " + xmlDocument, xmlDocument.indexOf("<line>add.package.internal</line>") != -1);
        assertTrue("other.add.Add not in " + xmlDocument, xmlDocument.indexOf("<line>other.add.Add</line>") != -1);
        assertTrue("other.add.Add.Add() in " + xmlDocument, xmlDocument.indexOf("<line>other.add.Add.Add()</line>") != -1);
        assertTrue("other.add.OtherAdd.OtherAdd() not in " + xmlDocument, xmlDocument.indexOf("<line>other.add.OtherAdd.OtherAdd()</line>") != -1);
    }

    public void error(SAXParseException ex) {
    }

    public void fatalError(SAXParseException ex) {
    }

    public void warning(SAXParseException ex) {
    }
}
