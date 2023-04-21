package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Transformer parses input for tags with the namespace "jutil" and executes some transformation on them.</br>
 * At present there are the following operations included:<br/>
 * <ul>
 * <li>transform a date in a given format optional in a language given</li>
 * <li>calculate a new date with a difference in days given</li>
 * </ul>
 * 
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:transformer name="jutil" src="de.juwimm.cms.cocoon.transformation.UtilTransformer"/&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * Just put code like this in your pipeline:
 * <pre>
 * &lt;map:transform type="jutil"/&gt;
 * </pre>
 * </p>
 * <p>The input must be something like this:</br>
 * <pre>
 * &lt;jutil:dateDiff xmlns:jutil="http://juwimm.net/conquest/util/1.0" dateFormat="yyyyMMdd" lang="german"&gt;
 *     &lt;jutil:date diff="-14" dateFormat="yyyyMMdd"&gt;20050324&lt;/jutil:date&gt;
 * &lt;/jutil:dateDiff&gt;
 * </pre>
 * Instead of giving a string containing the source-date, you can use the tag "jutil:currentDate"
 * <pre>
 * &lt;jutil:date diff="-14" dateFormat="yyyyMMdd"&gt;&lt;jutil:currentDate/&gt;&lt;jutil:date&gt;
 * </pre>
 * In this case the attribute "dateFormat" must be "yyyyMMdd".
 * </p>
 * <p>
 * All formats must look like described in class SimpleDateFormat.<br/>
 * All language-information must match the language-definitions in class Locale.
 * @see java.text.SimpleDateFormat
 * @see java.util.Locale
 * </p>
 * <p><h5>Result:</h5>
 * The output will look like this:</br>
 * <pre>
 * &lt;jutil:dateDiff xmlns:jutil="http://juwimm.net/conquest/util/1.0" dateFormat="yyyyMMdd" lang="german"&gt;
 *     &lt;date dateFormat="yyyyMMdd"&gt;20050310&lt;date&gt;
 * &lt;jutil:dateDiff&gt;
 * </pre>
 * </p>
 * <p>
 * If you just want to format a date, you can leave-out the attribute "diff".<br/>If you leave-out the attribute
 * "dateFormat" the format "yyyyMMdd" will be used. The "lang"-attribute is optional.
 * </p>
 * 
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $$Id: UtilTransformer.java 8 2009-02-15 08:54:54Z skulawik $$
 * @since ConQuest 1.66
 */
public class UtilTransformer extends AbstractTransformer implements Recyclable {

    private static Logger log = Logger.getLogger(UtilTransformer.class);

    public static final String NAME_SPACE = "jutil";

    private String resultFormat = null;

    private String resultLanguage = null;

    private int diff = 0;

    private String sourceFormat = null;

    private String lastElement = null;

    public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
        this.resultFormat = null;
        this.resultLanguage = null;
        this.diff = 0;
        this.sourceFormat = null;
        this.lastElement = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        this.lastElement = qName;
        if (qName.startsWith(UtilTransformer.NAME_SPACE)) {
            if (localName.equalsIgnoreCase("dateDiff")) {
                this.resultFormat = attrs.getValue("dateFormat");
                if (log.isDebugEnabled()) log.debug("found resultFormat: " + this.resultFormat);
                this.resultLanguage = attrs.getValue("lang");
                if (log.isDebugEnabled()) log.debug("found resultLanguage: " + this.resultLanguage);
                contentHandler.startElement(uri, localName, qName, attrs);
            } else if (localName.equalsIgnoreCase("date")) {
                String d = null;
                d = attrs.getValue("diff");
                if (d != null) {
                    this.diff = Integer.parseInt(attrs.getValue("diff"));
                } else {
                    diff = 0;
                }
                this.sourceFormat = attrs.getValue("dateFormat");
                AttributesImpl newAttr = new AttributesImpl();
                newAttr.addAttribute("", "dateFormat", "dateFormat", "CDATA", this.resultFormat);
                contentHandler.startElement("", localName, localName, newAttr);
            } else if (localName.equalsIgnoreCase("currentDate")) {
                SimpleDateFormat sdf = new SimpleDateFormat(this.sourceFormat);
                Date d = new Date();
                String resultDate = this.transformDate(sdf.format(d));
                contentHandler.characters(resultDate.toCharArray(), 0, resultDate.length());
            }
        } else {
            contentHandler.startElement(uri, localName, qName, attrs);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.lastElement.equalsIgnoreCase(UtilTransformer.NAME_SPACE + ":date")) {
            String date = new String(ch, start, length);
            String resultDate = this.transformDate(date);
            this.lastElement = "";
            contentHandler.characters(resultDate.toCharArray(), 0, resultDate.length());
        } else {
            this.lastElement = "";
            contentHandler.characters(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(UtilTransformer.NAME_SPACE + ":date")) {
            contentHandler.endElement("", localName, localName);
        } else if (!qName.equalsIgnoreCase(UtilTransformer.NAME_SPACE + ":currentDate")) {
            contentHandler.endElement(uri, localName, qName);
        }
    }

    @Override
    public void recycle() {
        if (log.isDebugEnabled()) log.debug("begin recycle");
    }

    private String transformDate(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat(this.sourceFormat);
        try {
            Date sourceDate = sdf.parse(input, new ParsePosition(0));
            Calendar cal = Calendar.getInstance();
            if (sourceDate != null) {
                cal.setTime(sourceDate);
                sdf = null;
                if (this.diff != 0) {
                    cal.add(Calendar.DAY_OF_YEAR, this.diff);
                }
                if (this.resultFormat != null && !"".equalsIgnoreCase(this.resultFormat)) {
                    if (this.resultLanguage != null && !"".equalsIgnoreCase(this.resultLanguage)) {
                        sdf = new SimpleDateFormat(this.resultFormat, new Locale(this.resultLanguage));
                    } else {
                        sdf = new SimpleDateFormat(this.resultFormat);
                    }
                } else {
                    sdf = new SimpleDateFormat("yyyyMMdd");
                }
                return sdf.format(cal.getTime());
            }
            return input;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }
}
