package net.jwiki.engine.example;

import net.jwiki.engine.RendererException;
import net.jwiki.engine.core.WikiEngine;
import net.jwiki.engine.core.extended.ExtendedWikiEngine;
import net.jwiki.engine.core.simple.SimpleWikiEngine;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: Armond Avanes
 */
public abstract class AbstractDemoWiki {

    private static final String WIKI_RESULT_DIR = "target/wiki-result/";

    private static final String EXTENDED_GRAMMAR = "/wiki/test-wiki2.egrm";

    private static final String EXTENDED_XGRAMMAR_OUTPUT = "wiki.xegrm";

    private static final String SIMPLE_GRAMMAR = "/wiki/wiki.grm";

    private static final String SIMPLE_XGRAMMAR_OUTPUT = "wiki.xgrm";

    protected static WikiEngine prepareWikiEngine(String[] args) throws RendererException, IOException {
        String engineType = extractProperty(args, "wiki.engine");
        WikiEngine wikiEngine = null;
        if (engineType != null && engineType.equalsIgnoreCase("extended")) {
            File grammarFile = new File(WIKI_RESULT_DIR, EXTENDED_XGRAMMAR_OUTPUT);
            wikiEngine = new ExtendedWikiEngine(EXTENDED_GRAMMAR);
            grammarFile.getParentFile().mkdirs();
            ((ExtendedWikiEngine) wikiEngine).writeXmlGrammar(new FileOutputStream(grammarFile));
        } else if (engineType == null || engineType.trim().equals("") || engineType.equalsIgnoreCase("simple")) {
            File grammarFile = new File(WIKI_RESULT_DIR, SIMPLE_XGRAMMAR_OUTPUT);
            wikiEngine = new SimpleWikiEngine(SIMPLE_GRAMMAR);
            grammarFile.getParentFile().mkdirs();
            ((SimpleWikiEngine) wikiEngine).writeXmlGrammar(new FileOutputStream(grammarFile));
        } else throw new RendererException("Wiki engine type is invalid!");
        return wikiEngine;
    }

    private static String extractProperty(String[] args, String propKey) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-D" + propKey) && (i + 1 < args.length)) return args[i + 1];
        }
        return null;
    }

    protected static String readFromConsole(String prompt) throws IOException {
        StringBuffer input = new StringBuffer();
        char ch;
        System.out.println();
        System.out.print(prompt);
        while (true) {
            ch = (char) System.in.read();
            if (ch != -1 && ch != '\r' && ch != '\n') input.append(ch); else break;
        }
        return input.toString();
    }

    protected static String extractFirstParameter(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().startsWith("-d")) i++; else return args[i];
        }
        return null;
    }
}
