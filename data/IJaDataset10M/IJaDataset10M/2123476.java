package com.loribel.tools.web.action;

import java.io.IOException;
import java.io.Writer;
import com.loribel.commons.abstraction.GB_ObjectAction;
import com.loribel.tools.web.tools.AA;

/**
 * Action abstraite avec Writer.
 *  
 * @author Gregory Borelli
 */
public abstract class GBW_HtmlLinkWriterAction implements GB_ObjectAction {

    private Writer writer;

    public GBW_HtmlLinkWriterAction() {
        super();
    }

    public GBW_HtmlLinkWriterAction(Writer a_writer) {
        super();
        writer = a_writer;
    }

    /**
     * Pour faire une action apr�s avoir boucler sur les liens.
     */
    public abstract void doAfter() throws Exception;

    /**
     * Pour faire une action avant de boucler sur les liens.
     */
    public abstract void doBefore() throws Exception;

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer a_writer) {
        writer = a_writer;
    }

    public void writeLn() throws IOException {
        getWriter().write(AA.SL);
    }

    public void writeLn(String a_line) throws IOException {
        getWriter().write(a_line + AA.SL);
    }
}
