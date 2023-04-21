package net.sf.gluent.doc.distillery.indentedtokens;

public interface LineStart {

    void end();

    Tokens line();

    LineStartOrIndentationChange emptyLine();
}
