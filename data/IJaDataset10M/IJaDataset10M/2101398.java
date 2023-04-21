package fido.servlets;

import java.security.Principal;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import fido.db.SystemPropertiesTable;
import fido.util.Version;

public class Index extends HttpServlet {

    private SystemPropertiesTable sysProps;

    private void dataManipulationTable(PrintWriter out) {
        out.println("<P>");
        out.println("<TABLE WIDTH=100% CELLSPACING=0 CELLPADDING=4 BORDER=0>");
        out.println("<TR>");
        out.println("<TD CLASS=SubHeadingBar> <FONT CLASS=SubHeadingFont>Data Manipulation</FONT></TD>");
        out.println("</TR><TR><TD>");
        out.println("<A HREF=adjPrepTable>Adjective Preposition Table</A><BR>");
        out.println("<A HREF=advPrepTable>Adverb Preposition Table</A><BR>");
        out.println("<A HREF=articleTable>Article Table</A><BR>");
        out.println("<A HREF=attributeTable>Attribute Table</A><BR>");
        out.println("<A HREF=attrCatTable>Attribute Category Table</A><BR>");
        out.println("<A HREF=classLinkTable>Class Link Type Table</A><BR>");
        out.println("<A HREF=dictionaryTable>Dictionary Table</A><BR>");
        out.println("<A HREF=frameSlotTable>Frame Slot Table</A><BR>");
        out.println("<A HREF=grammarLinkTable>Grammar Link Table</A><BR>");
        out.println("<A HREF=instructionTable>Instruction Table</A><BR>");
        out.println("<A HREF=morphologyTable>Morphology Table</A><BR>");
        out.println("<A HREF=objectTable>Object Table</A><BR>");
        out.println("<A HREF=pronounTable>Pronoun Table</A><BR>");
        out.println("<A HREF=properNounTable>Proper Noun Table</A><BR>");
        out.println("<A HREF=questionWordTable>Question Word Table</A><BR>");
        out.println("<A HREF=transactionTable>Transaction Table</A><BR>");
        out.println("<A HREF=verbTable>Verb Table</A><BR>");
        out.println("<A HREF=verbTransactionTable>Verb Transaction Table</A><BR>");
        out.println("<A HREF=webServiceTable>Web Services Table</A><BR>");
        out.println("<A HREF=wordClassTable>Word Classification Table</A><BR>");
        out.println("</TD></TR></TABLE>");
    }

    private void applicationTable(PrintWriter out) {
        out.println("<TABLE WIDTH=100% CELLSPACING=0 CELLPADDING=4 BORDER=0>");
        out.println("<TR>");
        out.println("<TD CLASS=SubHeadingBar> <FONT CLASS=SubHeadingFont>Applications</FONT></TD>");
        out.println("</TR><TR><TD>");
        if (sysProps.productionLevel() == false) out.println("<A HREF=developer/art>Automated Regression Test</A><BR>");
        out.println("<A HREF=frontend>Fido FrontEnd Entry</A><BR>");
        if (sysProps.productionLevel() == false) out.println("<A HREF=debugfrontend>Debug Fido FrontEnd</A><BR>");
        out.println("<A HREF=help>Fido Help System</A><BR>");
        out.println("</TD></TR></TABLE>");
    }

    private void testDataTable(PrintWriter out) {
        out.println("<TABLE WIDTH=100% CELLSPACING=0 CELLPADDING=4 BORDER=0>");
        out.println("<TR>");
        out.println("<TD CLASS=SubHeadingBar> <FONT CLASS=SubHeadingFont>Test Data</FONT></TD>");
        out.println("</TR><TR><TD>");
        out.println("<A HREF=developer/clearData>Clear Data</A> - loads a minimal data set.<BR>");
        out.println("<A HREF=developer/loadTestData>Load Test Data</A> - loads data in all tables to play with<P>");
        out.println("These links load data for the tutorials on the <A HREF=http://fido.sourceforge.net/tutorials/>Fido</A> web site<P>");
        out.println("<A HREF=developer/tutorial1Data>Load Tutorial 1 Data</A><BR>");
        out.println("<A HREF=developer/tutorial2Data>Load Tutorial 2 Data</A><BR>");
        out.println("<A HREF=developer/tutorial3Data>Load Tutorial 3 Data</A>");
        out.println("</TD></TR></TABLE>");
    }

    private void adminTable(PrintWriter out) {
        out.println("<TABLE WIDTH=100% CELLSPACING=0 CELLPADDING=4 BORDER=0>");
        out.println("<TR>");
        out.println("<TD CLASS=SubHeadingBar> <FONT CLASS=SubHeadingFont>Admin</FONT></TD>");
        out.println("</TR><TR><TD>");
        out.println("<A HREF=admin/userTable>User Administration</A><BR>");
        out.println("</TD></TR></TABLE>");
    }

    private void infoTable(PrintWriter out) {
        out.println("<TABLE WIDTH=100% CELLSPACING=0 CELLPADDING=4 BORDER=0>");
        out.println("<TR><TD CLASS=SubHeadingBar> <FONT CLASS=SubHeadingFont>Version Info</FONT></TD>");
        out.println("</TR><TR><TD>");
        out.println("<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=0>");
        out.println("<TR><TD>Fido Software Version</TD><TD>&nbsp;:&nbsp;</TD><TD>" + sysProps.softwareVersion() + "</TD></TR>");
        out.println("<TR><TD>Software Compile Date</TD><TD>&nbsp;:&nbsp;</TD><TD>" + Version.getCompileDate() + "</TD></TR>");
        out.println("<TR><TD>Database Schema Version</TD><TD>&nbsp;:&nbsp;</TD><TD>" + sysProps.dbSchemaVersion() + "</TD></TR>");
        out.println("<TR><TD>Database Data Version</TD><TD>&nbsp;:&nbsp;</TD><TD>" + sysProps.dbDataVersion() + "</TD></TR>");
        out.println("<TR><TD>Database Vendor</TD><TD>&nbsp;:&nbsp;</TD><TD>" + sysProps.dbVendor() + "</TD></TR>");
        out.println("</TABLE>");
        out.println("</TD></TR></TABLE>");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String context = request.getContextPath();
        sysProps = new SystemPropertiesTable();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Fido Jumpstart Page</TITLE>");
        out.println("<LINK REL=stylesheet HREF=" + context + "/Borderless.css>");
        out.println("</HEAD>");
        out.println("<BODY>");
        String[][] path = { { "Home", null } };
        FidoServlet.header(out, path, null, context);
        out.println("<TABLE WIDTH=100%>");
        out.println("<TR><TD VALIGN=top ROWSPAN=4>");
        dataManipulationTable(out);
        out.println("</TD><TD VALIGN=top>");
        applicationTable(out);
        out.println("</TD></TR>");
        if (sysProps.productionLevel() == false) {
            out.println("<TR><TD>");
            testDataTable(out);
            out.println("</TD></TR>");
        }
        out.println("<TR><TD>");
        adminTable(out);
        out.println("</TD></TR>");
        out.println("<TR><TD>");
        infoTable(out);
        out.println("</TD></TR>");
        out.println("</TABLE>");
        FidoServlet.footer(out);
        out.println("</BODY>");
        out.println("</HTML>");
    }
}
