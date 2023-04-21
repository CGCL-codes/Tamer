package jmri.jmrit.roster.swing;

import jmri.jmrit.roster.*;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jdom.*;

/**
 * Tests for the roster.swing.RosterTableModel class.
 *
 * @author	Bob Jacobsen     Copyright (C) 2009
 * @version     $Revision: 17977 $
 */
public class RosterTableModelTest extends TestCase {

    public void testTableLength() throws Exception {
        RosterTableModel t = new RosterTableModel();
        Assert.assertEquals(NENTRIES, t.getRowCount());
    }

    public void testTableWidth() throws Exception {
        RosterTableModel t = new RosterTableModel();
        Assert.assertEquals(RosterTableModel.NUMCOL, t.getColumnCount());
    }

    public void testColumnName() throws Exception {
        RosterTableModel t = new RosterTableModel();
        Assert.assertEquals("DCC Address", t.getColumnName(RosterTableModel.ADDRESSCOL));
    }

    public void testGetValueAt() {
        RosterTableModel t = new RosterTableModel();
        Assert.assertEquals("id 1", t.getValueAt(0, RosterTableModel.IDCOL));
        Assert.assertEquals(Integer.valueOf(12), t.getValueAt(0, RosterTableModel.ADDRESSCOL));
        Assert.assertEquals("33", t.getValueAt(0, RosterTableModel.DECODERCOL));
        Assert.assertEquals("id 2", t.getValueAt(1, RosterTableModel.IDCOL));
        Assert.assertEquals(Integer.valueOf(13), t.getValueAt(1, RosterTableModel.ADDRESSCOL));
        Assert.assertEquals("34", t.getValueAt(1, RosterTableModel.DECODERCOL));
        Assert.assertEquals("id 3", t.getValueAt(2, RosterTableModel.IDCOL));
        Assert.assertEquals(Integer.valueOf(14), t.getValueAt(2, RosterTableModel.ADDRESSCOL));
        Assert.assertEquals("35", t.getValueAt(2, RosterTableModel.DECODERCOL));
    }

    static int NENTRIES = 3;

    static int NKEYS = 4;

    public void setUp() {
        apps.tests.Log4JFixture.setUp();
        Roster.installNullInstance();
        Element e;
        RosterEntry r;
        e = new org.jdom.Element("locomotive").setAttribute("id", "id 1").setAttribute("fileName", "file here").setAttribute("roadNumber", "431").setAttribute("roadName", "SP").setAttribute("mfg", "Athearn").setAttribute("dccAddress", "1234").addContent(new org.jdom.Element("decoder").setAttribute("family", "91").setAttribute("model", "33")).addContent(new org.jdom.Element("locoaddress").addContent(new org.jdom.Element("dcclocoaddress").setAttribute("number", "12").setAttribute("longaddress", "yes")));
        r = new RosterEntry(e) {

            protected void warnShortLong(String s) {
            }
        };
        Roster.instance().addEntry(r);
        r.putAttribute("key a", "value 1");
        e = new org.jdom.Element("locomotive").setAttribute("id", "id 2").setAttribute("fileName", "file here").setAttribute("roadNumber", "431").setAttribute("roadName", "SP").setAttribute("mfg", "Athearn").addContent(new org.jdom.Element("decoder").setAttribute("family", "91").setAttribute("model", "34")).addContent(new org.jdom.Element("locoaddress").addContent(new org.jdom.Element("dcclocoaddress").setAttribute("number", "13").setAttribute("longaddress", "yes")));
        r = new RosterEntry(e) {

            protected void warnShortLong(String s) {
            }
        };
        Roster.instance().addEntry(r);
        r.putAttribute("key a", "value 11");
        r.putAttribute("key b", "value 12");
        r.putAttribute("key c", "value 13");
        r.putAttribute("key d", "value 14");
        e = new org.jdom.Element("locomotive").setAttribute("id", "id 3").setAttribute("fileName", "file here").setAttribute("roadNumber", "431").setAttribute("roadName", "SP").setAttribute("mfg", "Athearn").addContent(new org.jdom.Element("decoder").setAttribute("family", "91").setAttribute("model", "35")).addContent(new org.jdom.Element("locoaddress").addContent(new org.jdom.Element("dcclocoaddress").setAttribute("number", "14").setAttribute("longaddress", "yes")));
        r = new RosterEntry(e) {

            protected void warnShortLong(String s) {
            }
        };
        Roster.instance().addEntry(r);
    }

    public RosterTableModelTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", RosterTableModelTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RosterTableModelTest.class);
        return suite;
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }
}
