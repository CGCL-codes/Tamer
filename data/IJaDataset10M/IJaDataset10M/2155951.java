package org.openimmunizationsoftware.dqa.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.Application;
import org.openimmunizationsoftware.dqa.db.model.IssueFound;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.manager.KeyedSettingManager;
import org.openimmunizationsoftware.dqa.manager.OrganizationManager;
import org.openimmunizationsoftware.dqa.parse.PrintBean;
import org.openimmunizationsoftware.dqa.parse.VaccinationUpdateParserHL7;
import org.openimmunizationsoftware.dqa.validate.Validator;

public class ValidationDocumentationServlet extends HttpServlet {

    private static final String EXAMPLE = "MSH|^~\\&|TCH ImmSys|Nathan's Dev Laptop|TxImmTrac|TxDSHS|20110510095220||VXU^V04|1305039140421.100000004|P|2.4|\r" + "PID|||200005643^^^^PI~7833822^^^^MA~453533099^^^^SS||CHEIN^LE^A^JR^DR^^L~ALAST^AFIRST^AMIDDLE^AJR^ADR^A|DOG|20080606|M||2106-3^White^HL70005|102 N BOW WOW TERRACE^APT 103^ROUND ROCK^TX^73377^US^P^COUNTY||^PRN^^^^512^7999399|||||||||ETHNICITY|BIRTH PLACE|Y|5||||20110604|Y|\r" + "PD1|||\r" + "NK1|1|CHEIN^LASSIE^THE|MTH^Mother^HL70063|\r" + "NK1|2|CHEIN^JACQUES^LE|FTH^Father^HL70063|\r" + "PV1||R|\r" + "RXA|0|999|20080606|20080606|90707^MMR^CPT^03^MMR^CVX|999|||01^Historical information - source unspecified^NIP001|\r" + "RXR|ROUTE|SITE|\r" + "RXA|0|999|20080815|20080815|116^^CVX^90680^Rotavirus^C4|999|||01^Historical information - source unspecified^NIP001|\r" + "RXA|0|999|20080815|20080815|110^DTaP-Hep B-IPV^CVX^90723^DTaP/Hep B/IPV^C4|999|||01^Historical information - source unspecified^NIP001|\r" + "RXA|0|999|20081007|20081007|110^DTaP-Hep B-IPV^CVX^90723^DTaP/Hep B/IPV^C4|999|||00^New immunization record^NIP001||^^^West Houston^^^^^12606 West Houston Center Blvd.^^Houston^TX^77082^US||||D80993X||SKB^GlaxoSmithKline^MVX|\r" + "RXA|0|999|20090710|20090710|03^MMR^CVX^90707^MMR^C4|999|||00^New immunization record^NIP001||^^^West Houston^^^^^12606 West Houston Center Blvd.^^Houston^TX^77082^US||||XX993NA||SKB^GlaxoSmithKline^MVX|\r";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String messageText = req.getParameter("messageText");
        if (messageText == null || messageText.equals("")) {
            messageText = EXAMPLE;
        }
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>DQA Validation</title>");
        out.println("    <script>");
        out.println("      function toggleLayer(whichLayer) ");
        out.println("      {");
        out.println("        var elem, vis;");
        out.println("        if (document.getElementById) ");
        out.println("          elem = document.getElementById(whichLayer);");
        out.println("        else if (document.all) ");
        out.println("          elem = document.all[whichLayer] ");
        out.println("        else if (document.layers) ");
        out.println("          elem = document.layers[whichLayer]");
        out.println("        vis = elem.style;");
        out.println("        if (vis.display == '' && elem.offsetWidth != undefined && elem.offsetHeight != undefined) ");
        out.println("          vis.display = (elem.offsetWidth != 0 && elem.offsetHeight != 0) ? 'block' : 'none';");
        out.println("        vis.display = (vis.display == '' || vis.display == 'block') ? 'none' : 'block';");
        out.println("      }");
        out.println("    </script>");
        printCss(out);
        out.println("  </head>");
        out.println("  <body>");
        Application app = KeyedSettingManager.getApplication();
        out.println("    <h1>DQA Validation for " + app.getApplicationLabel() + " " + app.getApplicationType() + "</h1>");
        out.println("    <form action=\"validationDocumentation\" method=\"GET\">");
        out.println("      <textarea name=\"messageText\" cols=\"120\" rows=\"15\" wrap=\"off\">" + messageText + "</textarea>");
        out.println("      <input type=\"submit\" name=\"submit\" value=\"validate\">");
        out.println("    </form>");
        SessionFactory factory = OrganizationManager.getSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            SubmitterProfile profile = (SubmitterProfile) session.get(SubmitterProfile.class, SubmitterProfile.TEST_HL7);
            profile.initPotentialIssueStatus(session);
            VaccinationUpdateParserHL7 parser = new VaccinationUpdateParserHL7(profile);
            MessageReceived messageReceived = new MessageReceived();
            messageReceived.setProfile(profile);
            messageReceived.setRequestText(messageText);
            out.println("<h2>Step 1: Parse Message</h2>");
            parser.createVaccinationUpdateMessage(messageReceived);
            printIsssuesIdentified(out, messageReceived, "1");
            if (!messageReceived.hasErrors()) {
                out.println("<h2>Step 2: Validate Message</h2>");
                setupShowHidDiv(out, "Validating", "validating");
                Validator validator = new Validator(profile, session);
                validator.setDocumentOut(out);
                validator.validateVaccinationUpdateMessage(messageReceived, null);
                out.println("</div>");
                printIsssuesIdentified(out, messageReceived, "2");
            }
            out.println("<h2>Step 3: Create Ack Message</h2>");
            String ackMessage = parser.makeAckMessage(messageReceived);
            setupShowHidDiv(out, "Ack Message", "ackMessage");
            out.println("<pre>" + ackMessage + "</pre>");
            out.println("</div>");
        } finally {
            tx.rollback();
            session.close();
        }
        out.println("  </body>");
        out.println("</html>");
        out.close();
    }

    private void printIsssuesIdentified(PrintWriter out, MessageReceived messageReceived, String step) {
        setupShowHidDiv(out, "Message Contents", "messageContents" + step);
        out.println("<pre>");
        try {
            PrintBean.print(messageReceived, out);
        } catch (Throwable t) {
            t.printStackTrace(out);
        }
        out.println("</pre>");
        out.println("</div>");
        List<IssueFound> issuesFound = messageReceived.getIssuesFound();
        setupShowHidDiv(out, "Issues Identified", "issuesIdentified" + step);
        if (issuesFound.size() == 0) {
            out.println("<p><em>No issues found</em></p>");
        } else {
            boolean first = true;
            for (IssueFound issueFound : issuesFound) {
                if (issueFound.isError()) {
                    if (first) {
                        out.println("<p>Errors:</p>");
                        out.println("<ol>");
                        first = false;
                    }
                    printIssueFound(issueFound, out);
                }
            }
            if (!first) {
                out.println("</ol>");
            }
            first = true;
            for (IssueFound issueFound : issuesFound) {
                if (issueFound.isWarn()) {
                    if (first) {
                        out.println("<p>Warnings:</p>");
                        out.println("<ol>");
                        first = false;
                    }
                    printIssueFound(issueFound, out);
                }
            }
            if (!first) {
                out.println("</ol>");
            }
            first = true;
            for (IssueFound issueFound : issuesFound) {
                if (issueFound.isSkip()) {
                    if (first) {
                        out.println("<p>Skip:</p>");
                        out.println("<ol>");
                        first = false;
                    }
                    printIssueFound(issueFound, out);
                }
            }
            if (!first) {
                out.println("</ol>");
            }
        }
        out.println("</div>");
    }

    private void setupShowHidDiv(PrintWriter out, String sectionLabel, String sectionName) {
        out.println("<h3><a href=\"javascript:toggleLayer('" + sectionName + "');\" title=\"Show/Hide\">" + sectionLabel + " +/-</a></h3>");
        out.println("<div id=\"" + sectionName + "\" style=\"display:none\">");
    }

    private void printIssueFound(IssueFound issueFound, PrintWriter out) {
        out.print("<li>");
        out.print(issueFound.getDisplayText());
        out.print("</li>");
    }

    private static final String VERY_LIGHT = "#FFFFFF";

    private static final String LIGHT = "#CCDDDD";

    private static final String MEDIUM_LIGHT = " #AAC4C4;";

    private static final String MEDIUM = "#93AAAB";

    private static final String DARK = "#749749";

    private static final String BLACK = "#000000";

    private static final String RED = "#CE3100";

    private static final String BLUE = "#0031CE";

    private void printCss(PrintWriter out) {
        out.println("    <style><!--");
        out.println("      body {font-family: Tahoma, Geneva, Sans-serif}");
        out.println("      p {width:700px; color:" + DARK + "; background:" + VERY_LIGHT + "; padding:6px; border-style:none; border-width:1px; border-color:" + LIGHT + "}");
        out.println("      h1 {color:" + BLACK + "; font-size:2.0em;}");
        out.println("      h2 {color:" + BLACK + "; font-size:2.0em; page-break-before:always;}");
        out.println("      h3 {color:" + BLACK + "; font-size:1.2em;}");
        out.println("      table {background:" + LIGHT + "; border-style:solid; border-width:1; border-color:" + MEDIUM + "; border-collapse:collapse}");
        out.println("      th {background:" + MEDIUM + "; font-size:0.8em; color:" + BLACK + "; border-style:none; padding-left:5px; padding-right:5px;}");
        out.println("      td {border-style:solid; border-width:1; border-color:" + MEDIUM + ";margin:0px; padding-left:5px; padding-right:5px;}");
        out.println("      .score {font-size:1.5em;}");
        out.println("      .alert {}");
        out.println("      .highlight {background: " + MEDIUM_LIGHT + ";}");
        out.println("      .excellent {color:" + BLUE + ";}");
        out.println("      .good {color:" + BLUE + ";}");
        out.println("      .poor {color:" + RED + ";}");
        out.println("      .problem {color:" + RED + ";}");
        out.println("      a:link {text-decoration:none; color:" + BLACK + "}");
        out.println("      a:visited {text-decoration:none; color:" + BLACK + "}");
        out.println("      a:hover {text-decoration:none; color:" + BLACK + "} ");
        out.println("      a:active {text-decoration:none; color:" + BLACK + "} ");
        out.println("      a.tooltip span {display:none; padding:2px 3px; margin-left:8px; width:130px;}");
        out.println("      a.tooltip:hover span{display:inline; position:absolute; background:" + LIGHT + "; border:1px solid " + DARK + "; color:" + DARK + "}");
        out.println("    --></style>");
    }
}
