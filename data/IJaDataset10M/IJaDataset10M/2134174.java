package com.lowagie.text.pdf.codec.postscript;

public interface PACommand {

    public void execute(PAContext context) throws PainterException;
}
