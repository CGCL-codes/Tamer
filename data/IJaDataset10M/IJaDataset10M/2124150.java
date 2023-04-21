package example.nanoc;

import java.util.Enumeration;
import java.util.Hashtable;
import pobs.PControl;
import pobs.PDirective;
import pobs.PParser;
import pobs.parser.PParserPointer;

/**
 * @author Franz-Josef Elmer
 */
public class ParserWeb {

    private static class Trace implements PControl {

        private final String name;

        public Trace(String name) {
            this.name = name;
        }

        public Object modifyState(PDirective directive) {
            System.out.println("Parser '" + name + "' started");
            return null;
        }

        public void reestablishPreviousState(PDirective directive, Object previousState) {
            System.out.println("Parser '" + name + "' finished");
        }
    }

    private final Hashtable nameToParserMap = new Hashtable();

    private final Hashtable unresolvedPointers = new Hashtable();

    private final boolean tracing;

    public ParserWeb(boolean tracing) {
        this.tracing = tracing;
    }

    public PParser get(String name) {
        PParser object = (PParser) nameToParserMap.get(name);
        if (object == null) {
            object = new PParserPointer();
            nameToParserMap.put(name, object);
            unresolvedPointers.put(name, object);
        }
        return object;
    }

    public void set(String name, PParser parser) {
        if ((nameToParserMap.get(name) instanceof PParser) && (unresolvedPointers.get(name) == null)) {
            throw new IllegalArgumentException("Parser '" + name + "' already defined.");
        }
        if (tracing) {
            parser.addControl(new Trace(name));
        }
        nameToParserMap.put(name, parser);
        PParserPointer unresolvedPointer = (PParserPointer) unresolvedPointers.get(name);
        if (unresolvedPointer != null) {
            unresolvedPointer.set(parser);
            unresolvedPointers.remove(name);
        }
    }

    public boolean hasUnresolvedPointers() {
        return unresolvedPointers.size() > 0;
    }

    public String getNamesOfUnresolvedPointers() {
        StringBuffer result = new StringBuffer("{");
        Enumeration keys = unresolvedPointers.keys();
        while (keys.hasMoreElements()) {
            result.append(keys.nextElement());
            if (keys.hasMoreElements()) {
                result.append(", ");
            }
        }
        return new String(result.append("}"));
    }
}
