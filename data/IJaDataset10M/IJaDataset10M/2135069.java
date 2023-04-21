package com.objectwave.sourceParser;

import java.io.StringReader;
import java.io.Reader;
import java.io.IOException;

/**  
*  This class happens to look exactly like package def, except it is
*  for imports.
*     @author Dave Hoag
*/
public class ImportDefParser extends PackageDefParser {

    /**
	* Finish parsing the import. Go all of the way until the ';'.
	*/
    public void finishParsing(StringBuffer buf, String[] identifier, Reader rdr) throws IOException {
        StringBuffer identifierBuf = SourceCodeReader.nextJavaIdentifier(buf, rdr);
        int cInt;
        cInt = rdr.read();
        if (cInt < 0) return;
        char c = (char) cInt;
        if (c == '*') {
            buf.append(c);
            identifierBuf.append(c);
            cInt = rdr.read();
            if (cInt < 0) return;
            c = (char) cInt;
        }
        while (c != ';') {
            buf.append(c);
            cInt = rdr.read();
            if (cInt < 0) return;
            c = (char) cInt;
        }
        buf.append(c);
        theObject.setIdentifier(identifierBuf.toString());
        theObject.setFullText(buf.toString());
        theObject.setAsImport();
    }

    /**
	* When parsing source code we never know what ClassElement will be next, so we ask
	* all possible class elements isMyArea(String) for one of them to say yes.  Once one
	* of the parser acknowledges ownership, we hand off the parsing routine to that
	* class parser. This is my area if the identifier = 'import'
	* @see #finishParsing
	*/
    public boolean isMyArea(String identifier) {
        return identifier.equals("import");
    }

    /**
	* Test routine.
	*/
    public static void main(String[] argv) {
        try {
            StringReader rdr = new StringReader("import my.test.*;");
            StringBuffer sum = new StringBuffer();
            String ident = SourceCodeReader.nextJavaIdentifier(sum, rdr).toString();
            ImportDefParser def = new ImportDefParser();
            if (def.isMyArea(ident)) def.finishParsing(sum, new String[0], rdr);
            System.out.println(sum);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
