package org.yuanheng.cookcc.codegen.xml;

import java.io.PrintWriter;
import org.yuanheng.cookcc.doc.GrammarDoc;
import org.yuanheng.cookcc.doc.ParserDoc;
import org.yuanheng.cookcc.doc.RhsDoc;
import org.yuanheng.cookcc.doc.TypeDoc;

/**
 * @author Heng Yuan
 * @version $Id: XmlParserOutput.java 451 2008-11-01 19:41:27Z superduperhengyuan $
 */
class XmlParserOutput {

    private void printRhs(RhsDoc rhs, PrintWriter p) {
        p.println("\t\t\t<rhs>" + Utils.translate(rhs.getTerms()) + "</rhs>");
        String action = rhs.getAction();
        if (action != null && action.length() > 0) p.println("\t\t\t<action>" + Utils.translate(rhs.getAction()) + "</action>");
    }

    private void printGrammar(GrammarDoc grammar, PrintWriter p) {
        p.println("\t\t<grammar rule=\"" + grammar.getRule() + "\">");
        for (RhsDoc rhs : grammar.getRhs()) printRhs(rhs, p);
        p.println("\t\t</grammar>");
    }

    private void printType(TypeDoc type, PrintWriter p) {
        p.print("\t\t<type format=\"" + type.getFormat().toPattern() + "\"><![CDATA[");
        for (String t : type.getSymbols()) p.print(" " + t);
        p.println(" ]]></type>");
    }

    public void printParserDoc(ParserDoc doc, PrintWriter p) {
        if (doc == null) return;
        p.println("\t<parser" + (doc.getStart() == null ? "" : (" start=\"" + doc.getStart() + "\"")) + (doc.getParseError() == true ? "" : " parseerror=\"false\"") + (doc.getRecovery() == true ? "" : " recovery=\"false\"") + ">");
        for (TypeDoc type : doc.getTypes()) printType(type, p);
        for (GrammarDoc grammar : doc.getGrammars()) printGrammar(grammar, p);
        p.println("\t</parser>");
    }
}
