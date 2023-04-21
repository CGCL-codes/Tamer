package org.owasp.orizon.twilight;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Vector;
import org.owasp.orizon.core.CommonUI;
import org.owasp.orizon.core.Cons;
import org.owasp.orizon.core.Engine;
import org.owasp.orizon.dusk.parser.QUIT;
import org.owasp.orizon.library.Rule;
import org.owasp.orizon.mirage.Call;
import org.owasp.orizon.report.Reportable;
import org.owasp.orizon.twilight.parser.CRAWL;
import org.owasp.orizon.twilight.parser.Node;
import org.owasp.orizon.twilight.parser.ParseException;
import org.owasp.orizon.twilight.parser.TwilightParser;

/**
 * @author thesp0nge
 *
 */
public class Twilight extends Engine {

    public static final int twilightStartErrno = 300;

    public static final int twilightNotFeed = twilightStartErrno + 1;

    public static final int twilightNoIdentifiers = twilightStartErrno + 2;

    private final String PROMPT = "(twilight) $ ";

    private Vector<Call> identifiers;

    private Vector<String> dangerousKeywords;

    public Twilight() {
        super();
        dangerousKeywords = new Vector<String>();
        identifiers = new Vector<Call>();
    }

    public Twilight(String name) {
        this();
        setAppName(name);
    }

    @Override
    public boolean exec(String command) {
        try {
            resetTimer();
            startTimer();
            ByteArrayInputStream bis = new ByteArrayInputStream(command.getBytes("UTF-8"));
            TwilightParser tP = new TwilightParser(new InputStreamReader(bis));
            tP.Command();
            stopTimer();
            return process(tP.rootNode());
        } catch (UnsupportedEncodingException e) {
        } catch (ParseException e) {
            debug("exec(): " + e.getMessage());
            return false;
        }
        return false;
    }

    public boolean setIdentifiers(Vector<Call> i) {
        if ((i == null) || (i.size() == 0)) {
            errno = twilightNoIdentifiers;
            status = O_E_ERROR;
            setErrorString("no identifiers were loaded from source code.");
            return false;
        }
        identifiers = i;
        return true;
    }

    private String removeChar(String s, char c) {
        String r = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r += s.charAt(i);
        }
        return r;
    }

    @Override
    public boolean init() {
        if (identifiers.size() == 0) {
            errno = twilightNoIdentifiers;
            status = O_E_ERROR;
            setErrorString("no identifiers were loaded from source code.");
            return false;
        }
        if (rules == null) {
            errno = twilightNotFeed;
            status = O_E_ERROR;
            setErrorString("no rules were provided. I need to be feed before init()");
            return false;
        }
        for (Rule r : rules) {
            String subj = r.retrieve("subj");
            String verb = r.retrieve("verb");
            String value = r.retrieve("value");
            if ("keyword".equals(subj) && "found".equals(verb)) dangerousKeywords.add(removeChar(value, '\"'));
        }
        return true;
    }

    @Override
    public boolean pause() {
        return false;
    }

    @Override
    protected boolean process(Object n) {
        Node root = (Node) n;
        if (root instanceof QUIT) return false;
        if (root instanceof CRAWL) {
        }
        return true;
    }

    @Override
    public boolean shell() {
        boolean ret = true;
        boolean quit = false;
        String PS1 = PROMPT;
        while (!quit) {
            try {
                System.out.println(PS1);
                TwilightParser tP = new TwilightParser(new InputStreamReader(System.in));
                tP.Command();
                if (!process((Node) tP.rootNode())) quit = true;
            } catch (ParseException e) {
                debug("invalid command. " + e.getMessage());
            }
        }
        return ret;
    }

    /**
	 * Starts the Twilight engine
	 * 
	 * @return <i>true</i> if the Twilight engine can be started, or <i>false</i>
	 *         otherwise.
	 */
    public boolean start() {
        if (!init()) return false;
        int ii = 1;
        for (Call i : identifiers) {
            for (String k : dangerousKeywords) {
                if (i.getName().equals(k)) {
                    String s = getProperties().get(Cons.OC_FRAMEWORK_OPTION_SNIPPET_DELTA);
                    int delta = 0;
                    try {
                        if (s == null) delta = 3; else delta = new Integer(s).intValue();
                    } catch (NumberFormatException e) {
                        debug("an unexpected property value has been found for snippet-delta: " + s + "\nReverting to the default value (3).");
                        delta = 3;
                    }
                    Reportable r = new Reportable();
                    r.setEngineName("twilight");
                    r.setErrorMessage("found potential dangerous keyword during crawling (" + k + ")");
                    r.setSeverity("low");
                    r.setSourceFileName(i.getFilename());
                    r.setLineNo(i.getLineNo());
                    r.setStats(getStats());
                    if (!r.setSnippet(delta)) debug("an error occured while trying to collect the source code snippet.");
                    addFault(r);
                }
            }
            ii++;
        }
        return true;
    }

    @Override
    public boolean stop() {
        return false;
    }
}
