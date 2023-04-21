package de.startext.mex.extendedinformation;

import org.eclipse.swt.browser.Browser;
import de.startext.mex.model.MexNode;
import de.startext.mex.profile.MexProfileEntry;

/**
 * This class generates a html file which is shown in the user view
 * @author No�l Marx
 *
 */
public class MexExtendedInformationHTMLCreator {

    public static void showExtendedInformation(Browser browser, MexNode mexNode) {
        if (browser == null) {
            return;
        }
        browser.setText("");
        if (mexNode == null) {
            return;
        }
        if (!mexNode.hasProfileElement()) {
            return;
        }
        StringBuffer sBuffer = new StringBuffer();
        MexExtendedInformationContext context = MexExtendedInformationDAO.getMexExtendedInformationForMexNode(mexNode, false);
        if (context == null) {
            return;
        }
        writeContextToBuffer(mexNode.getProfileElement(), sBuffer, context);
        browser.setText(sBuffer.toString());
    }

    public static void showExtendedInformation(Browser browser, MexProfileEntry mexProfileEntry) {
        if (browser == null) {
            return;
        }
        browser.setText("");
        if (mexProfileEntry == null) {
            return;
        }
        StringBuffer sBuffer = new StringBuffer();
        MexExtendedInformationContext context = MexExtendedInformationDAO.getMexExtendedInformationForMexProfileEntry(mexProfileEntry, false);
        if (context == null) {
            return;
        }
        writeContextToBuffer(mexProfileEntry, sBuffer, context);
        browser.setText(sBuffer.toString());
    }

    private static void writeContextToBuffer(MexProfileEntry mexProfileEntry, StringBuffer sBuffer, MexExtendedInformationContext context) {
        writeHeaderToBuffer(sBuffer);
        String minApp = "";
        if (mexProfileEntry.getMinOccurs().equals("0")) {
            minApp = "Optional";
        } else {
            minApp = "Obligatorisch";
        }
        String maxApp = "";
        if (mexProfileEntry.getMaxOccurs().equals("1")) {
            maxApp = "Nicht wiederholbar";
        } else {
            maxApp = "Wiederholbar";
        }
        writeContextAttributeToBuffer(sBuffer, minApp + ";" + maxApp, "Verwendung", 0, true);
        writeContextAttributeToBuffer(sBuffer, context.getAttribute_Name(), "Attribut Name", 1, true);
        writeContextAttributeToBuffer(sBuffer, context.getDefinition(), "Definition", 3, true);
        writeContextAttributeToBuffer(sBuffer, context.getExample(), "Beispiele", 4, false);
        writeContextAttributeToBuffer(sBuffer, context.getCreationNotes(), "Erstellungshinweise", 2, false);
        writeContextAttributeToBuffer(sBuffer, context.getRationale(), "Begr�ndung", 5, false);
        writeContextAttributeToBuffer(sBuffer, context.getUsage_Notes(), "Verwendungshinweise", 6, false);
        writerFooterToBuffer(sBuffer);
    }

    private static void writeContextAttributeToBuffer(StringBuffer buffer, String contextAttribute, String contextAttributName, int id, boolean showFromBeginning) {
        if (contextAttribute == null) {
            return;
        }
        String idString = "div" + id;
        buffer.append("<h2 ");
        buffer.append("onclick=\"divOpenOrClose('");
        buffer.append(idString);
        buffer.append("');\">");
        buffer.append(contextAttributName);
        buffer.append("</h2>");
        buffer.append("<div id=\"");
        buffer.append(idString);
        buffer.append("\" ");
        if (showFromBeginning) {
            buffer.append("style=\"display:block;\">");
        } else {
            buffer.append("style=\"display:none;\">");
        }
        buffer.append(contextAttribute.replace("\r\n", "<br/>"));
        buffer.append("</div>");
    }

    private static void writeHeaderToBuffer(StringBuffer buffer) {
        buffer.append("<html>");
        buffer.append("<head>");
        buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html;\" charset=\"ISO-8859-1\"/>");
        buffer.append("<title>div-open-close</title>");
        buffer.append("<style type=\"text/css\">");
        buffer.append(".gray { background-color:#eee; display: block; padding: 6px; width:150px; cursor:pointer; }");
        buffer.append("</style>");
        buffer.append("<style>h1, h2, h3, h4, h5, h6 {color:#116480} body {background-color:#FCF4E0;margin-top:0.25in;margin-left:0.5in;margin-right:1.0in;margin-bottom:3.0in;font-family: verdana;} a {color:#116480} pre{font-family: verdana;font-size=12pt;}</style>");
        buffer.append("<script type=\"text/javascript\">");
        buffer.append("function divOpenOrClose(divid) {");
        buffer.append("if(document.getElementById(divid).style.display == \"block\"){");
        buffer.append("document.getElementById(divid).style.display=\"none\";");
        buffer.append("}");
        buffer.append("else{");
        buffer.append("document.getElementById(divid).style.display=\"block\";");
        buffer.append("}}");
        buffer.append("</script>");
        buffer.append("</head>");
        buffer.append("<body>");
        buffer.append("<div id=\"alldivs\">");
    }

    private static void writerFooterToBuffer(StringBuffer buffer) {
        buffer.append("</div>  ");
        buffer.append("</body> ");
        buffer.append("</html>");
    }
}
