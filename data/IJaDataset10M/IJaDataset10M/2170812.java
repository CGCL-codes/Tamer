package net.sourceforge.pmd.cpd;

import net.sourceforge.pmd.ast.JavaCharStream;
import net.sourceforge.pmd.ast.JavaParserTokenManager;
import java.io.Reader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

public class JavaTokensTokenizer implements Tokenizer {

    public void tokenize(TokenList tokens, Reader input) throws IOException {
        List lines = new ArrayList();
        StringBuffer sb = new StringBuffer();
        LineNumberReader r = new LineNumberReader(input);
        String currentLine = null;
        while ((currentLine = r.readLine()) != null) {
            lines.add(currentLine);
            sb.append(currentLine);
            sb.append(System.getProperty("line.separator"));
        }
        tokens.setCode(lines);
        JavaCharStream javaStream = new JavaCharStream(new StringReader(sb.toString()));
        JavaParserTokenManager tokenMgr = new JavaParserTokenManager(javaStream);
        net.sourceforge.pmd.ast.Token currToken = tokenMgr.getNextToken();
        while (currToken.image != "") {
            if (!currToken.image.equals(";")) {
                tokens.add(new TokenEntry(currToken.image, tokens.size(), tokens.getID(), currToken.beginLine));
            }
            currToken = tokenMgr.getNextToken();
        }
    }
}
