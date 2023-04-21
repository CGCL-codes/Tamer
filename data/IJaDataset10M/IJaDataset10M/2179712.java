package com.okrasz.elvis.elvisnet.taglib;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import org.w3c.dom.Element;
import com.okrasz.elvis.elvisnet.RemoteBookCache;

/**
 *  Generated tag class.
 */
public class PagesArrayTag_1 extends BodyTagSupport {

    /** property declaration for tag attribute: guid.
     *
     */
    private String guid;

    /** property declaration for tag attribute: from.
     *
     */
    private int pagesFrom;

    /** property declaration for tag attribute: to.
     *
     */
    private int pagesTo;

    /**
     * The book that is going to be displayed
     */
    Element book = null;

    ;

    /** 
     * Tranformer from book to pages array
     */
    Transformer xslt = null;

    public PagesArrayTag_1() {
        super();
    }

    /**
     *  
     * Fill in this method to perform other operations from doStartTag().
     * 
     */
    public void otherDoStartTagOperations() {
        book = RemoteBookCache.get(guid, pageContext.getSession());
        if (book != null) {
            StringBuffer xsltPath = new StringBuffer(pageContext.getServletContext().getInitParameter("installDir"));
            xsltPath.append("xsl/elvisnetBook_").append(book.getAttribute("version"));
            xsltPath.append("_to_pagesArray.xsl");
            TransformerFactory tFactory = TransformerFactory.newInstance();
            try {
                xslt = tFactory.newTransformer(new StreamSource(xsltPath.toString()));
                xslt.setParameter("from", new Integer(pagesFrom));
                xslt.setParameter("to", new Integer(pagesTo));
            } catch (javax.xml.transform.TransformerConfigurationException e) {
                e.printStackTrace();
                book = null;
            }
        }
    }

    /**
     *  
     * Fill in this method to determine if the tag body should be evaluated
     * Called from doStartTag().
     * 
     */
    public boolean theBodyShouldBeEvaluated() {
        return true;
    }

    /**
     *  
     * Fill in this method to perform other operations from doEndTag().
     * 
     */
    public void otherDoEndTagOperations() {
        Element book = null;
        ;
        Transformer xslt = null;
    }

    /**
     *  
     * Fill in this method to determine if the rest of the JSP page
     * should be generated after this tag is finished.
     * Called from doEndTag().
     * 
     */
    public boolean shouldEvaluateRestOfPageAfterEndTag() {
        return true;
    }

    /** .
     *
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_INCLUDE if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     *
     */
    public int doStartTag() throws JspException, JspException {
        otherDoStartTagOperations();
        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }

    /** .
     *
     *
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     *
     */
    public int doEndTag() throws JspException, JspException {
        otherDoEndTagOperations();
        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String value) {
        guid = value;
    }

    public int getFrom() {
        return pagesFrom;
    }

    public void setFrom(int value) {
        pagesFrom = value;
    }

    public int getTo() {
        return pagesTo;
    }

    public void setTo(int value) {
        pagesTo = value;
    }

    /** .
     * Fill in this method to process the body content of the tag.
     * You only need to do this if the tag's BodyContent property
     * is set to "JSP" or "tagdependent."
     * If the tag's bodyContent is set to "empty," then this method
     * will not be called.
     *
     *
     */
    public void writeTagBodyContent(JspWriter out, BodyContent bodyContent) throws IOException {
        System.out.println("PagesArrayTag: writeTag");
        if (book != null) {
            try {
                java.io.StringWriter sw = new java.io.StringWriter();
                xslt.transform(new DOMSource(book), new StreamResult(sw));
                out.println(sw.toString());
                System.out.println(sw.toString());
            } catch (javax.xml.transform.TransformerException e) {
                e.printStackTrace();
                book = null;
            }
        }
        if (book == null) for (int i = 0; i <= pagesTo - pagesFrom; i++) {
            out.print("pagesArray[");
            out.print(i);
            out.print("]=\"\";");
        }
        bodyContent.writeOut(out);
        bodyContent.clearBody();
    }

    /** .
     *
     * Handles exception from processing the body content.
     *
     *
     */
    public void handleBodyContentException(Exception ex) throws JspException {
        throw new JspException("error in PagesArrayTag: " + ex);
    }

    /** .
     *
     *
     * This method is called after the JSP engine processes the body content of the tag.
     * @return EVAL_BODY_AGAIN if the JSP engine should evaluate the tag body again, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     *
     */
    public int doAfterBody() throws JspException {
        try {
            JspWriter out = getPreviousOut();
            BodyContent bodyContent = getBodyContent();
            writeTagBodyContent(out, bodyContent);
        } catch (Exception ex) {
            handleBodyContentException(ex);
        }
        if (theBodyShouldBeEvaluatedAgain()) {
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    /**
     * Fill in this method to determine if the tag body should be evaluated
     * again after evaluating the body.
     * Use this method to create an iterating tag.
     * Called from doAfterBody().
     *
     *
     */
    public boolean theBodyShouldBeEvaluatedAgain() {
        return false;
    }
}
