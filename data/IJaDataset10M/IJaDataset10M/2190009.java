package com.skruk.elvis.doc;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;
import java.io.PrintWriter;
import java.io.IOException;

/**
 *  Klasa odpowiadająca znacznikowi <code><i>doc</i>:script</code> z TagLib <tt>doc</tt> -
 *  odpowiedzialna za generowanie znacznika łączącego z arkuszem skryptów np. JavaScript  
 */
public class ScriptTag extends TagSupport {

    /** 
		 * Nazwa alternatywnego (X)HTMLa, który może zostać wygenerowany przez znacznik
     */
    private String view = null;

    /**
		 * Określa odnośnik do arkusza skryptów 
		 */
    private String href;

    /** 
		 * Określa język arkusza skryptów
     */
    private String lang = "JavaScript";

    /** 
		 * Nadrzędny znacznik otaczający <code><i>doc</i>:head</code>
		*/
    private HeadTag headTag_parent = null;

    public ScriptTag() {
        super();
    }

    /**
     *	Metoda odpowiedzialna za generowanie początku znacznika  
     */
    public void otherDoStartTagOperations() {
        StringBuffer patternName = new StringBuffer(this.getView()).append("js_link");
        String[] values = new String[2];
        values[0] = this.getHref();
        values[1] = this.getLang();
        CustomWriter cw = CustomWriter.getInstance(patternName.toString(), values);
        try {
            this.pageContext.getOut().write(cw.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ;
    }

    /**
     *	Określa czy ma zostać wygenerowana zawartość znacznika  
     *  @return Czy ma zostać wygenerowana zawartość znacznika?
     */
    public boolean theBodyShouldBeEvaluated() {
        return true;
    }

    /**
     *	Metoda generuje końcową część znacznika 
     */
    public void otherDoEndTagOperations() {
        this.view = null;
        this.lang = "JavaScript";
    }

    /**
     *	Określa czy ma zostać wygenerowana reszta strony po znaczniku  
     *  @return Czy ma zostać wygenerowana reszta strony po znaczniku ?
     */
    public boolean shouldEvaluateRestOfPageAfterEndTag() {
        return true;
    }

    /** .//GEN-BEGIN:doStartTag
     *
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_INCLUDE if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doStartTag() throws JspException, JspException {
        if (headTag_parent == null) {
            headTag_parent = (HeadTag) findAncestorWithClass(this, HeadTag.class);
        }
        otherDoStartTagOperations();
        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    /** .//GEN-BEGIN:doEndTag
     *
     *
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
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

    /** 
			************************* metody get i set dostępu do pół znacznika *************************** 
			*/
    public String getView() {
        if (this.view == null) this.view = this.headTag_parent.getView();
        return view;
    }

    public void setView(String value) {
        view = value;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String value) {
        href = value;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String value) {
        lang = value;
    }
}
