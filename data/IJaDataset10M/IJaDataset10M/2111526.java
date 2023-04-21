package net.sf.excompcel.poi.report.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import net.sf.excompcel.poi.report.ReportTextBase;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.Doctype;
import org.apache.ecs.Document;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.Comment;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Style;
import org.apache.ecs.xhtml.span;
import org.apache.ecs.xhtml.table;
import org.apache.ecs.xhtml.td;
import org.apache.ecs.xhtml.tr;
import org.apache.log4j.Logger;

/**
 * HTML Report File.
 * 
 * @author Detlev Struebig
 * @since v0.5
 *
 */
public class ReportHtml extends ReportTextBase {

    /** Logger. */
    private static Logger log = Logger.getLogger(ReportHtml.class);

    /** The Report Doc File. */
    private Document docReport;

    /** Hold Filename of the temporary Report file. This will delete when save the real Report File. */
    private String reportFilenameTempHold;

    /** DIV Container for DOC File Content. */
    protected Div divContent;

    /** DIV Container for Description of comparison. */
    protected Div divDescription;

    /** DIV Container for DOC File Header. */
    protected Div divHeader;

    /** DIV Container for DOC File Footer. */
    protected Div divFooter;

    protected RowDefinitionHtml currentTabRow = new RowDefinitionHtml();

    /**
	 * Constructor.
	 * @throws {@link IOException}
	 */
    public ReportHtml() throws IOException {
        reportFilenameTempHold = getTempFileName();
        initHtml();
    }

    /**
	 * Constructor.
	 * @param report {@link ReportHtml}
	 * @throws IOException
	 */
    public ReportHtml(ReportHtml report) throws IOException {
        this.divContent = report.divContent;
        this.divDescription = report.divDescription;
        this.divHeader = report.divHeader;
        this.divFooter = report.divFooter;
    }

    /**
	 * Get Report for Content.
	 * @return {@link ReportHtmlContent}
	 */
    public ReportHtmlContent getReportContent() {
        try {
            return new ReportHtmlContent(this);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * Get Report for Footer.
	 * @return {@link ReportHtmlFooter}
	 */
    public ReportHtmlFooter getReportFooter() {
        try {
            return new ReportHtmlFooter(this);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * Get Report for Header.
	 * @return {@link ReportHtmlHeader}
	 */
    public ReportHtmlHeader getReportHeader() {
        try {
            return new ReportHtmlHeader(this);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * Get Report for Description.
	 * @return {@link ReportHtmlDescription}
	 */
    public ReportHtmlDescription getReportDescription() {
        try {
            return new ReportHtmlDescription(this);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * Create the HTML Document.
	 */
    private void initHtml() {
        docReport = new Document().appendTitle(("Text Compare Report " + getDateString()));
        docReport.setDoctype(new Doctype.XHtml10Strict());
        Style style = createCssStyle();
        docReport.appendHead(style);
        divDescription = new Div();
        divDescription.setID("description");
        addDivHead(divDescription, "Description");
        docReport.appendBody(divDescription);
        divHeader = new Div();
        divHeader.setID("header");
        addDivHead(divHeader, "Header");
        docReport.appendBody(divHeader);
        divFooter = new Div();
        divFooter.setID("footer");
        addDivHead(divFooter, "Footer");
        docReport.appendBody(divFooter);
        divContent = new Div();
        divContent.setID("content");
        addDivHead(divContent, "Content");
        docReport.appendBody(divContent);
        docReport.getHtml().setPrettyPrint(true);
    }

    /**
	 * Create the CSS Style for the Report HTML.
	 * @return The CSS {@link Style}.
	 */
    private Style createCssStyle() {
        Element styleParameter = new ElementContainer().addElement("span.delete { color:red; text-decoration:line-through }\n").addElement("span.insert { color:blue; }\n").addElement("div {border-width:1px; border-style:solid; padding:5px;  margin:5px; background-color:rgb(239, 251, 255);}\n").addElement("div#head { background-color: green; padding: 5px; margin: 5px;}\n").addElement("td.mark { background-color:red; }\n");
        Style style = new Style(Style.CSS);
        style.addElement(new Comment().addElement(styleParameter));
        return style;
    }

    /**
	 * Add a Head Div (id='head') Container to an DIV Container.
	 * 
	 * @param div The {@link Div} Container, where to add a Head Div Container. 
	 * @param headline
	 */
    private void addDivHead(Div div, String headline) {
        Div divHead = new Div(headline);
        divHead.setID("head");
        div.addElement(divHead);
    }

    public synchronized String save() throws IOException {
        String filename = getFileName();
        return save(filename);
    }

    /**
	 * Create the Filename for the Report Doc File.
	 * @return Filename for Report Doc File. 
	 */
    private String getFileName() {
        return createFilename("report");
    }

    /**
	 * Create the Temporary Filename for the Report Doc File.
	 * @return Termporary Filename for Report Doc File. 
	 */
    private String getTempFileName() {
        return createFilename("reportTemp");
    }

    /**
	 * Create the Filename for the HTML Report File.
	 * 
	 * @param prefix Prefix of the Filename.
	 * @return the Filename.
	 */
    private String createFilename(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            throw new IllegalArgumentException("The prefix of Report Filename have to be filled out.");
        }
        StringBuffer buf = new StringBuffer();
        buf.append(prefix).append(getDateString()).append(".html");
        return buf.toString();
    }

    public String save(String filename) throws IOException {
        if (StringUtils.isEmpty(filename)) {
            throw new IllegalArgumentException("Filename have to be filled out.");
        }
        PrintStream out = new PrintStream(new FileOutputStream(filename));
        out.println(docReport.toString());
        out.flush();
        out.close();
        if (!new File(reportFilenameTempHold).delete()) {
            log.debug("Temp Report Doc File is not deleted.");
        }
        return new File(filename).getAbsolutePath();
    }

    public void startNewLine() {
        log.debug("startNewLine");
        createTrStart(currentTabRow);
    }

    public void endNewLine() {
        log.debug("endNewLine");
    }

    /**
	 * Add a new Line of Text.
	 * @param text Text Line.
	 */
    public void addNewLine(String text) {
        startNewLine();
        addReportTextEqual(text);
        endNewLine();
    }

    @Override
    public void addReportTextDelete(String text) {
        log.debug("addReportTextDelete:" + text);
        span span = new span(text);
        span.addAttribute("class", "delete");
        RowDefinitionHtml rowdef = getRowDefinition();
        rowdef.getTdContent().addElement(span);
        rowdef.getTdMark().addAttribute("class", "mark");
        rowdef.getTdMark().setTagText("&nbsp;");
    }

    @Override
    public void addReportTextEqual(String text) {
        log.debug("addReportTextEqual:" + text);
        RowDefinitionHtml rowdef = getRowDefinition();
        rowdef.getTdContent().addElement(text);
    }

    @Override
    public void addReportTextInsert(String text) {
        log.debug("addReportTextInsert:" + text);
        span span = new span(text);
        span.addAttribute("class", "insert");
        RowDefinitionHtml rowdef = getRowDefinition();
        rowdef.getTdContent().addElement(span);
        rowdef.getTdMark().addAttribute("class", "mark");
        rowdef.getTdMark().setTagText("&nbsp;");
    }

    /**
	 * 
	 * @param rowDef {@link RowDefinitionHtml}
	 */
    protected void createTrStart(RowDefinitionHtml rowDef) {
        rowDef.setTrCurrent(new tr());
        rowDef.setTdMark(new td());
        rowDef.setTdContent(new td());
        rowDef.getTable().addElement(rowDef.getTrCurrent().addElement(rowDef.getTdMark()).addElement(rowDef.getTdContent()));
    }

    /**
	 * Get current Row definition.
	 * @return {@link RowDefinitionHtml}
	 */
    public RowDefinitionHtml getRowDefinition() {
        return currentTabRow;
    }

    /**
	 * Create a HTML Table for in DIV Container.
	 * @param elemDiv {@link Div}
	 * @return {@link RowDefinitionHtml}
	 */
    protected RowDefinitionHtml createTable(Div elemDiv) {
        RowDefinitionHtml rowdef = new RowDefinitionHtml();
        rowdef.setTable(new table(1));
        elemDiv.addElement(rowdef.getTable());
        return rowdef;
    }
}

/**
 * 
 * @author Detlev Struebig
 * v0.5
 */
class ReportHtmlDescription extends ReportHtml {

    /** Logger. */
    private static Logger log = Logger.getLogger(ReportHtmlDescription.class);

    /**
	 * Constructor.
	 * @param report
	 * @throws IOException
	 */
    public ReportHtmlDescription(ReportHtml report) throws IOException {
        super(report);
        currentTabRow = createTable(divDescription);
        log.debug("Create ReportHtmlDescription.");
    }
}

/**
 * 
 * @author Detlev Struebig
 * v0.5
 */
class ReportHtmlHeader extends ReportHtml {

    /** Logger. */
    private static Logger log = Logger.getLogger(ReportHtmlHeader.class);

    /**
	 * Constructor.
	 * @param report
	 * @throws IOException
	 */
    public ReportHtmlHeader(ReportHtml report) throws IOException {
        super(report);
        currentTabRow = createTable(divHeader);
        log.debug("Create ReportHtmlHeader.");
    }
}

/**
 * 
 * @author Detlev Struebig
 * v0.5
 */
class ReportHtmlFooter extends ReportHtml {

    /** Logger. */
    private static Logger log = Logger.getLogger(ReportHtmlFooter.class);

    /**
	 * Constructor.
	 * @param report
	 * @throws IOException
	 */
    public ReportHtmlFooter(ReportHtml report) throws IOException {
        super(report);
        currentTabRow = createTable(divFooter);
        log.debug("Create ReportHtmlFooter.");
    }
}

/**
 * 
 * @author Detlev Struebig
 * v0.5
 */
class ReportHtmlContent extends ReportHtml {

    /** Logger. */
    private static Logger log = Logger.getLogger(ReportHtmlContent.class);

    /**
	 * Constructor.
	 * @param report
	 * @throws IOException
	 */
    public ReportHtmlContent(ReportHtml report) throws IOException {
        super(report);
        currentTabRow = createTable(divContent);
        log.debug("Create ReportHtmlContent.");
    }
}
