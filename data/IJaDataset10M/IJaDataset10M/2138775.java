package org.openiaml.iacleaner;

import java.io.IOException;
import org.openiaml.iacleaner.inline.InlineStringReader;
import org.openiaml.iacleaner.inline.InlineStringWriter;

/**
 * Handles the cleaning of XML content. This doesn't actually
 * clean XML at all, but it allows for PHP content to be parsed
 * mid-stream.
 * 
 * <p>It is only safe to format white space in two scenarios: when cleaning
 * PHP code, or when processing the contents of &lt;tags&gt;. Another scenario
 * that is not implemented yet is XML comments.
 * 
 * @author Jevon
 *
 */
public class InlineXmlCleaner {

    private IAInlineCleaner inline;

    public InlineXmlCleaner(IAInlineCleaner inline) {
        this.inline = inline;
    }

    public IAInlineCleaner getInline() {
        return inline;
    }

    /**
	 * Clean up XML code; essentially, it just allows
	 * passbacks to PHP if necessary, otherwise outputs
	 * XML character-for-character.
	 * 
	 * @param reader
	 * @param writer
	 * @throws IOException 
	 * @throws CleanerException 
	 */
    public void cleanXmlBlock(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        writer.enableWordwrap(false);
        boolean inTag = false;
        boolean inString = false;
        while (true) {
            String next5 = reader.readAhead(5);
            if (next5 != null && next5.equals("<?php")) {
                boolean oldWordWrap = writer.canWordWrap();
                writer.enableWordwrap(true);
                getInline().cleanPhpBlock(reader, writer);
                writer.enableWordwrap(oldWordWrap);
            } else {
                int c = reader.read();
                if (c == -1) {
                    break;
                } else if (!inTag && c == '<') {
                    inTag = true;
                    writer.enableWordwrap(true);
                } else if (inTag && c == '>') {
                    inTag = false;
                    writer.enableWordwrap(false);
                } else if (inTag && !inString && c == '"') {
                    inString = true;
                    writer.enableWordwrap(false);
                } else if (inTag && inString && c == '"') {
                    inString = false;
                    writer.enableWordwrap(true);
                }
                writer.write(c);
            }
        }
        writer.enableWordwrap(true);
    }
}
