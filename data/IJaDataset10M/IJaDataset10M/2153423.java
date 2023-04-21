package icm.unicore.plugins.dbbrowser.nucleotide;

import java.io.*;
import java.util.Enumeration;
import javax.swing.text.html.*;
import icm.unicore.plugins.dbbrowser.*;

/** Parses response from Nucl server (many results) for all necessary information.
 *
 * @author Michal Wronski (wrona@mat.uni.torun.pl)
 * @version 1.0
 */
public class NuclListParser {

    private NuclParsedList response;

    private String urlString;

    /** Creates new NuclListParser
	 */
    public NuclListParser() {
    }

    /** Parses input taken from <I>reader</I>. Return results in <I>response</I>.
	 * This method sets <I>base=urlString</I> in the response header (necessary to load images).
	 */
    public void parse(Reader reader, NuclParsedList response, String urlString) {
        this.response = response;
        this.urlString = urlString;
        ParserGetter kit = new ParserGetter();
        HTMLEditorKit.Parser parser = kit.getParser();
        HTMLEditorKit.ParserCallback callback = new NuclResponseCallback();
        try {
            response.add("<html>");
            parser.parse(reader, callback, true);
            response.add("</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ParserGetter extends HTMLEditorKit {

        /** We have to make it public
		 */
        public HTMLEditorKit.Parser getParser() {
            return super.getParser();
        }
    }

    private class NuclResponseCallback extends HTMLEditorKit.ParserCallback {

        int tables = 0;

        StringBuffer output = new StringBuffer();

        boolean startheader = false;

        boolean startrighttable = false;

        boolean viewcomment = false;

        boolean startspan = false;

        boolean startinfo = false;

        boolean startitems = false;

        NuclResponseCallback() {
        }

        private String toString(javax.swing.text.MutableAttributeSet attributes) {
            String res = "";
            Object attr;
            for (Enumeration e = attributes.getAttributeNames(); e.hasMoreElements(); ) {
                attr = e.nextElement();
                res = res + attr.toString() + "=\"" + attributes.getAttribute(attr).toString() + "\" ";
            }
            return res;
        }

        /** Invoked when start tag is found.
		 */
        public void handleStartTag(HTML.Tag tag, javax.swing.text.MutableAttributeSet attributes, int position) {
            if (tag == HTML.Tag.HEAD) {
                startheader = true;
            } else if (tag == HTML.Tag.BODY) {
                javax.swing.text.MutableAttributeSet attr = attributes;
                attr.removeAttribute(HTML.Attribute.BACKGROUND);
                output.append("<" + tag.toString() + " " + toString(attr) + ">");
                response.add(output);
            } else if (tag == HTML.Tag.TABLE) {
                if (viewcomment) tables++;
            } else if (tag == HTML.Tag.SPAN) {
                startspan = true;
            } else if (tag == HTML.Tag.FORM) {
                String url = (String) attributes.getAttribute(HTML.Attribute.ACTION);
                url = url.substring(0, url.indexOf('?')) + "?CMD=Pager&DB=nucleotide";
                response.setCgiUrl(url);
                response.setMethod((((String) attributes.getAttribute(HTML.Attribute.METHOD)).toLowerCase().compareTo("get") == 0) ? FormPoster.GET : FormPoster.POST);
            }
            if ((startheader || startrighttable) && (!startspan)) {
                attributes.removeAttribute(HTML.Attribute.CONTENT);
                output.append("<" + tag.toString() + " " + toString(attributes) + ">");
            }
        }

        /** Invoked when text is found.
		 */
        public void handleText(char[] data, int pos) {
            String str = new String(data);
            if ((startheader || startrighttable) && (!startspan)) output.append(str); else if (startinfo) {
                response.setTotalPages(str);
                startinfo = false;
            } else if (startitems && str.startsWith("Items")) {
                response.setItemsDisplayed(str);
                startitems = false;
            }
        }

        /** Invoked when comment is found.
		 */
        public void handleComment(char[] data, int pos) {
            String str = (new String(data)).trim();
            if (str.compareToIgnoreCase("-- Pager -- (page header) -- end -----") == 0) {
                startrighttable = true;
            } else if (str.compareToIgnoreCase("-- Pager -- (page footer) -- begin ---") == 0) {
                startrighttable = false;
                response.add(output);
                startitems = true;
            }
        }

        /** Invoked when simple tag is found.
		 */
        public void handleSimpleTag(HTML.Tag tag, javax.swing.text.MutableAttributeSet attributes, int position) {
            if (tag == HTML.Tag.INPUT) {
                String name = (String) attributes.getAttribute(HTML.Attribute.NAME);
                if (name == null) name = "";
                String type = (String) attributes.getAttribute(HTML.Attribute.TYPE);
                if (type == null) type = "";
                String val = (String) attributes.getAttribute(HTML.Attribute.VALUE);
                if (val == null) val = "";
                if (name.compareTo("textpage") == 0) {
                    startinfo = true;
                    response.setPage(val);
                }
                if (name.compareTo("term") == 0) response.addHiddenQuery(name, val);
                if (type.compareToIgnoreCase("hidden") == 0 && !name.startsWith("inputpage")) {
                    response.addHiddenQuery(name, val);
                }
            }
            if (tag == HTML.Tag.SPAN) {
                startspan = !startspan;
            } else if (startrighttable && (tag == HTML.Tag.INPUT)) {
                String name = (String) attributes.getAttribute(HTML.Attribute.NAME);
                if ((name != null) && (name.compareToIgnoreCase("Display") == 0)) {
                    startrighttable = false;
                }
            }
            if (startheader && (tag != HTML.Tag.BASE)) {
                attributes.removeAttribute(HTML.Attribute.CONTENT);
                output.append("<" + tag.toString() + " " + toString(attributes) + ">");
            }
            if (startrighttable && (tag != HTML.Tag.INPUT) && (!startspan) && (tag != HTML.Tag.SPAN)) if (tag == HTML.Tag.BR) output.append(" "); else output.append("<" + tag.toString() + " " + toString(attributes) + ">");
        }

        /** Invoked when end tag is found.
		 */
        public void handleEndTag(HTML.Tag tag, int position) {
            if (tag == HTML.Tag.HEAD) {
                output.append("<base href=\"" + urlString + "\">");
                output.append("</" + tag.toString() + ">");
                if (startheader) {
                    response.add(output);
                }
                startheader = false;
                output = new StringBuffer();
            } else if (tag == HTML.Tag.BODY) {
                response.add("</" + tag.toString() + ">");
            }
            if ((startrighttable || startheader) && (!startspan)) output.append("</" + tag.toString() + ">");
            if (tag == HTML.Tag.TABLE) {
            } else if (tag == HTML.Tag.SPAN) startspan = false;
        }
    }
}
