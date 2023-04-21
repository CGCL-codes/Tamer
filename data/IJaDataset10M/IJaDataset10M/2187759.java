package net.sf.xpontus.parsers;

import java.util.HashMap;
import java.util.Stack;
import javax.swing.tree.DefaultMutableTreeNode;
import net.sf.xpontus.view.XPontusWindow;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;

public interface XMLParserTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int XMLDECL = 4;

    int DOCTYPE = 5;

    int EMPTYTAG = 6;

    int STARTTAG = 7;

    int ENDTAG = 8;

    int CDATABLOCK = 9;

    int PCDATA = 10;

    int COMMENT = 11;

    int PI = 12;

    int INTERNAL_DTD = 13;

    int PI_OR_XMLDECL = 14;

    int COMMENT_DATA = 15;

    int PCDATA_DATA = 16;

    int INTERNAL_CDATA_DATA = 17;

    int ATTR = 18;

    int STRING_NO_QUOTE = 19;

    int STRING = 20;

    int NAME = 21;

    int NAMECHAR = 22;

    int DIGIT = 23;

    int LETTER = 24;

    int WS = 25;

    int ESC = 26;

    int NL = 27;
}
