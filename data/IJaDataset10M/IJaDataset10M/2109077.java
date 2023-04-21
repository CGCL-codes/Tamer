package org.antlr.codegen;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.tool.Grammar;
import org.antlr.Tool;
import org.antlr.misc.Utils;
import java.io.IOException;

public class ObjCTarget extends Target {

    protected void genRecognizerHeaderFile(Tool tool, CodeGenerator generator, Grammar grammar, StringTemplate headerFileST, String extName) throws IOException {
        generator.write(headerFileST, grammar.name + Grammar.grammarTypeToFileNameSuffix[grammar.type] + extName);
    }

    public String getTargetCharLiteralFromANTLRCharLiteral(CodeGenerator generator, String literal) {
        if (literal.startsWith("'\\u")) {
            literal = "0x" + literal.substring(3, 7);
        } else {
            int c = literal.charAt(1);
            if (c < 32 || c > 127) {
                literal = "0x" + Integer.toHexString(c);
            }
        }
        return literal;
    }

    /** Convert from an ANTLR string literal found in a grammar file to
	*  an equivalent string literal in the target language.  For Java, this
	*  is the translation 'a\n"' -> "a\n\"".  Expect single quotes
	*  around the incoming literal.  Just flip the quotes and replace
	*  double quotes with \"
	*/
    public String getTargetStringLiteralFromANTLRStringLiteral(CodeGenerator generator, String literal) {
        literal = Utils.replace(literal, "\"", "\\\"");
        StringBuffer buf = new StringBuffer(literal);
        buf.setCharAt(0, '"');
        buf.setCharAt(literal.length() - 1, '"');
        buf.insert(0, '@');
        return buf.toString();
    }

    /** If we have a label, prefix it with the recognizer's name */
    public String getTokenTypeAsTargetLabel(CodeGenerator generator, int ttype) {
        String name = generator.grammar.getTokenDisplayName(ttype);
        if (name.charAt(0) == '\'') {
            return String.valueOf(ttype);
        }
        return generator.grammar.name + Grammar.grammarTypeToFileNameSuffix[generator.grammar.type] + "_" + name;
    }

    /** Target must be able to override the labels used for token types. Sometimes also depends on the token text.*/
    public String getTokenTextAndTypeAsTargetLabel(CodeGenerator generator, String text, int tokenType) {
        String name = generator.grammar.getTokenDisplayName(tokenType);
        if (name.charAt(0) == '\'') {
            return String.valueOf(tokenType);
        }
        String textEquivalent = text == null ? name : text;
        if (textEquivalent.charAt(0) >= '0' && textEquivalent.charAt(0) <= '9') {
            return textEquivalent;
        } else {
            return generator.grammar.name + Grammar.grammarTypeToFileNameSuffix[generator.grammar.type] + "_" + textEquivalent;
        }
    }
}
