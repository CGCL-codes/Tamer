package net.nextquestion.xdk;

import org.antlr.runtime.ClassicToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Bridge between StAX and ANTLR 3. Original design by Kunle Odata, modified by Richard Clark.
 * See http://www.antlr.org/wiki/display/ANTLR3/Interfacing+StAX+to+ANTLR
 *
 * @author rdclark
 *         <p/>
 *         Date: Jan 21, 2008
 *         Time: 11:27:51 PM
 */
public class StaxTokenSource implements TokenSource {

    protected XMLStreamReader reader;

    protected Map<String, Integer> string2type = new HashMap<String, Integer>();

    protected Queue<Token> tokens = new LinkedList<Token>();

    public StaxTokenSource(Reader tokenDefinitionsReader, Reader xmlReader) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        this.reader = factory.createXMLStreamReader(xmlReader);
        initMapping(tokenDefinitionsReader);
    }

    public Token nextToken() {
        try {
            while (tokens.isEmpty()) {
                boolean ok = collectToken();
                if (!ok) break;
            }
        } catch (XMLStreamException e) {
        }
        return (tokens.isEmpty()) ? null : tokens.remove();
    }

    /**
     * Gets the next tag or text item (if any) and append one or more tokens to the buffer.
     *
     * @return false if at the end of the stream
     * @throws XMLStreamException for pqrsing failures
     */
    public boolean collectToken() throws XMLStreamException {
        if (!reader.hasNext()) return false;
        int eventType = reader.next();
        String tag;
        switch(eventType) {
            case XMLStreamConstants.START_ELEMENT:
                tag = reader.getLocalName();
                tokens.add(makeElementToken(reader, tag, "_START", "<" + tag));
                int numAttrs = reader.getAttributeCount();
                List<NamedToken> taggedAttributes = new ArrayList<NamedToken>(numAttrs);
                for (int i = 0; i < numAttrs; i++) {
                    tag = reader.getAttributeLocalName(i);
                    String attrName = tag.toUpperCase() + "_ATTR";
                    taggedAttributes.add(new NamedToken(attrName, makeToken(reader, attrName, reader.getAttributeValue(i))));
                }
                Collections.sort(taggedAttributes);
                for (NamedToken entry : taggedAttributes) {
                    tokens.add(entry.getToken());
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                tag = reader.getLocalName();
                tokens.add(makeElementToken(reader, tag, "_END", "/" + tag));
                break;
            case XMLStreamConstants.END_DOCUMENT:
                reader.close();
                return false;
        }
        return true;
    }

    private Token makeElementToken(XMLStreamReader reader, String tag, String suffix, String text) {
        String tokenName = tag.toUpperCase() + suffix;
        return makeToken(reader, tokenName, text);
    }

    private Token makeToken(XMLStreamReader reader, String tokenName, String text) {
        Token token;
        if (string2type.containsKey(tokenName)) {
            token = new ClassicToken(string2type.get(tokenName));
        } else {
            token = new ClassicToken(-1);
            token.setChannel(Token.HIDDEN_CHANNEL);
        }
        token.setText(text);
        Location loc = reader.getLocation();
        token.setLine(loc.getLineNumber());
        token.setCharPositionInLine(loc.getColumnNumber());
        return token;
    }

    /**
     * Parses an ANTLR tokens file to map the token names with their numbers.
     *
     * @param tokenDefinition a stream connected to a token file.
     * @throws IOException if there's a problem reading the definitions.
     */
    public void initMapping(Reader tokenDefinition) throws IOException {
        BufferedReader reader = new BufferedReader(tokenDefinition);
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=");
            String tokenName = parts[0];
            String tokenType = parts[1];
            Integer iType = new Integer(tokenType);
            string2type.put(tokenName, iType);
        }
    }

    private class NamedToken implements Comparable<NamedToken> {

        private String name;

        private Token token;

        public NamedToken(String name, Token token) {
            this.name = name;
            this.token = token;
        }

        public Token getToken() {
            return token;
        }

        public int compareTo(NamedToken other) {
            return name.compareTo(other.name);
        }
    }
}
