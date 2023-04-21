package org.modss.facilitator.port.report;

import msys.net.html.Area;
import msys.net.html.Center;
import msys.net.html.Color;
import msys.net.html.Component;
import msys.net.html.Document;
import msys.net.html.Font;
import msys.net.html.Heading;
import msys.net.html.HorizontalRule;
import msys.net.html.LineBreak;
import msys.net.html.Link;
import msys.net.html.LiteralText;
import msys.net.html.Paragraph;
import msys.net.html.Text;
import org.modss.facilitator.shared.resource.ResourceProvider;
import org.modss.facilitator.shared.singleton.Singleton;
import org.swzoo.log2.core.LogFactory;
import org.swzoo.log2.core.LogTools;
import org.swzoo.log2.core.Logger;
import java.util.StringTokenizer;

/**
 * This class contains global HTML setting information for reporting.
 */
public class ReportConfig {

    /** Size of headings for each category in the report. */
    public static int headingSize;

    /** Size of sub-headings for each category in the report. */
    public static int subHeadingSize;

    /** Color for the page title heading. */
    public static String pageTitleColor;

    /** Color for headings. */
    public static String headingColor;

    /** Color for sub headings. */
    public static String subHeadingColor;

    /** Color for sub headings. */
    public static String warningColor;

    /** Default document background color. */
    public static java.awt.Color backgroundColor;

    /** Title for the page. */
    public static String pageTitle;

    /** Title for the document page. */
    public static String documentTitle;

    /** Size of footers for each document in the report. */
    public static int footerSize;

    /** HTML whitespace */
    static String whitespace;

    static ReportConfig instance = null;

    public static ReportConfig getInstance() {
        if (instance == null) instance = new ReportConfig();
        return instance;
    }

    private ReportConfig() {
        headingSize = resources.getIntProperty("dss.report.html.heading.size", 2);
        subHeadingSize = resources.getIntProperty("dss.report.html.sub.heading.size", 3);
        pageTitleColor = resources.getProperty("dss.report.html.page.title.colour", "00CC00");
        headingColor = resources.getProperty("dss.report.html.heading.colour", "CC0000");
        subHeadingColor = resources.getProperty("dss.report.html.sub.heading.colour", "0088CC");
        warningColor = resources.getProperty("dss.report.html.warning.colour", "FF0000");
        backgroundColor = resources.getColorProperty("dss.report.html.background.colour", new java.awt.Color(0xFFFFFF));
        pageTitle = resources.getProperty("dss.report.html.page.title.text", "DECISION SUPPORT SYSTEM");
        whitespace = resources.getProperty("dss.report.html.whitespace", "&nbsp;");
        documentTitle = resources.getProperty("dss.report.html.page.document.title.text", "FACILITATOR");
        footerSize = resources.getIntProperty("dss.report.html.footer.size", 2);
    }

    /**
     * Provide an indexed HTML file name based on an index.
     *
     * @param index the index
     * @return a URL string (eg. "12.html");
     */
    public String getIndexHTML(int index) {
        return index + ".html";
    }

    /**
     * Provide an HTML file name based on a provided name.
     *
     * @param name the name
     * @return a URL string (eg. "both.html");
     */
    public String getNameHTML(String name) {
        return name + ".html";
    }

    /**
     * Provide an indexed GIF name based on an index.
     *
     * @param index the index
     * @return a GIF string (eg. "2.gif");
     */
    public String getIndexGIF(int index) {
        return index + ".gif";
    }

    /**
     * Provide an GIF name based on a provided name.
     *
     * @param name the provided name
     * @return a GIF string (eg. "scoregraph.gif");
     */
    public String getNameGIF(String name) {
        return name + ".gif";
    }

    /**
     * Create a document.
     * 
     * @param title the top level title
     * @param description a description which sits below the title
     * @param guts the main body of the document.
     * @returns an document component.
     */
    public Document createDocument(String title, String description, Component guts) {
        if (description == null && description.equals("")) description = null;
        String footerURL = resources.getProperty("dss.report.html.footer.url", "http://products.leapstream.com.au/facilitator/");
        String footerText = resources.getProperty("dss.report.html.footer.text", "Facilitator");
        Document doc = new Document();
        Link link = new Link(footerURL, footerText);
        Area color = new Area();
        Center top = new Center();
        Font font = new Font();
        color.setTag("font");
        color.setTagOptions("color=\"#" + pageTitleColor + "\"");
        color.add(new Heading(headingSize, title, true));
        top.add(color);
        if (description != null) {
            top.add(new LineBreak());
            top.add(ReportConfig.getInstance().createHeading(description));
        }
        doc.add(top);
        doc.setBackgroundColor(new Color(backgroundColor.getRGB()));
        doc.setTitle(documentTitle);
        doc.add(new HorizontalRule());
        doc.add(new Paragraph());
        doc.add(guts);
        doc.add(new Paragraph());
        doc.add(new HorizontalRule());
        font.setSize(footerSize);
        font.add(link);
        doc.add(font);
        return doc;
    }

    /**
     * Create a heading.  This creates a heading of the default
     * size and color.
     *
     * @param text the text to include in the heading.
     */
    public Component createHeading(String text) {
        Area color = new Area();
        color.setTag("font");
        color.setTagOptions("color=\"#" + ReportConfig.headingColor + "\"");
        color.add(new Heading(ReportConfig.headingSize, text));
        return color;
    }

    /**
     * Create a sub-heading.  This creates a sub heading of the default
     * size and color.
     *
     * @param text the text to include in the heading.
     */
    public Component createSubHeading(String text) {
        Area color = new Area();
        color.setTag("font");
        color.setTagOptions("color=\"#" + ReportConfig.subHeadingColor + "\"");
        color.add(new Heading(ReportConfig.subHeadingSize, text));
        return color;
    }

    /**
     * Convert a multi line string (ie separated by "\n"s) into HTML.
     *
     * @param area the html area to add the result HTML to
     * @param lines the string to be broken into multiple lines
     */
    public void multiLineToHTML(Area area, String lines) {
        if (lines == null) {
            return;
        }
        String delimiter = "\n";
        StringTokenizer t = new StringTokenizer(lines, delimiter, true);
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            LogTools.info(logger, "Token = [" + token + "]");
            if (token.equals(delimiter)) {
                area.add(new LiteralText(whitespace));
                area.add(new LineBreak());
            } else {
                area.add(new Text(token));
            }
        }
    }

    /**
     * Runs are invalid message.
     */
    public Component runsInvalid() {
        String invalidMessage = resources.getProperty("dss.report.html.runs.invalid.blurb", "THE FOLLOWING RUNS ARE NOT IN SYNC WITH THE CURRENT LIST OF\n" + "BASE CRITERIA, ALTERNATIVES AND MATRIX CELL VALUES.");
        Area color = new Area();
        color.add(new Paragraph());
        color.setTag("font");
        color.setTagOptions("color=\"#" + ReportConfig.warningColor + "\"");
        Area bold = new Area();
        bold.setTag("b");
        bold.add(new Paragraph());
        bold.add(new Text(invalidMessage));
        bold.add(new Paragraph());
        color.add(bold);
        color.add(new Paragraph());
        return color;
    }

    /**
     * Run is invalid message.
     */
    public Component runInvalid() {
        String invalidMessage = resources.getProperty("dss.report.html.run.invalid.blurb", "THIS RUN IS INVALID");
        Area color = new Area();
        color.add(new Paragraph());
        color.setTag("font");
        color.setTagOptions("color=\"#" + ReportConfig.warningColor + "\"");
        Area bold = new Area();
        bold.setTag("b");
        bold.add(new Paragraph());
        bold.add(new Text(invalidMessage));
        bold.add(new Paragraph());
        color.add(bold);
        color.add(new Paragraph());
        return color;
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();
}
