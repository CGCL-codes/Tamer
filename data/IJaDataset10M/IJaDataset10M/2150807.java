package org.retro.gis.util;

import org.retro.scheme.Scheme;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * The BotProcessThread, mainly the Alpha-Thread needs an system for
 * parsing strings as they come in, this class is used for parsing incoming
 * messages and spitting out a scheme parsed translation.
 * 
 * @author Berlin Brown
 *
 */
public class NaturalSchemeParser {

    private static final String _endl = "\n";

    private Scheme _scheme;

    private ArrayList saveWordTypes = new ArrayList();

    /**
	 * Define word types, use the Vector to sort through the set.
	 */
    private CacheSimpleGreetings simpleGreetings = new CacheSimpleGreetings();

    private CacheArticles simpleArts = new CacheArticles();

    private CacheSimpleNouns simpleNouns = new CacheSimpleNouns();

    private CachePronouns simplePronouns = new CachePronouns();

    private CacheVerbs simpleVerbs = new CacheVerbs();

    private CacheAdjectives simpleAdjs = new CacheAdjectives();

    private CacheAdverbs simpleAdverbs = new CacheAdverbs();

    private CachePrepositions simplePreps = new CachePrepositions();

    private CacheNames simpleNames = new CacheNames();

    private CacheMath simpleMath = new CacheMath();

    private CacheOrdinal simpleOrds = new CacheOrdinal();

    private CacheCardinal simpleCards = new CacheCardinal();

    private CacheTech simpleTech = new CacheTech();

    private CacheTechSlang simpleSlang = new CacheTechSlang();

    private CachePunctuation simplePunc = new CachePunctuation();

    private CacheSimpleResults simpleResults = new CacheSimpleResults();

    public NaturalSchemeParser(Scheme s) {
        _scheme = s;
    }

    public void loadMap() {
        saveWordTypes.add(simpleGreetings);
        saveWordTypes.add(simpleArts);
        saveWordTypes.add(simpleNouns);
        saveWordTypes.add(simplePronouns);
        saveWordTypes.add(simpleVerbs);
        saveWordTypes.add(simpleAdjs);
        saveWordTypes.add(simpleAdverbs);
        saveWordTypes.add(simplePreps);
        saveWordTypes.add(simpleNames);
        saveWordTypes.add(simpleMath);
        saveWordTypes.add(simpleOrds);
        saveWordTypes.add(simpleCards);
        saveWordTypes.add(simpleTech);
        saveWordTypes.add(simpleSlang);
        saveWordTypes.add(simplePunc);
    }

    public StringBuffer postAddWordTypes() {
        StringBuffer evalBuf = new StringBuffer();
        for (Iterator _ix = saveWordTypes.iterator(); _ix.hasNext(); ) {
            evalBuf.append(addSchemeWordList((CacheWordList) _ix.next()));
        }
        return evalBuf;
    }

    /**
	 * Part of the natural language parsing of the incoming message.
	 */
    private String addSchemeWordList(CacheWordList _wordlist) {
        StringBuffer _sbuf = new StringBuffer();
        _sbuf.append("(define " + _wordlist.getListName() + " '(" + _wordlist.defaultList() + "))" + _endl);
        _sbuf.append("(define (" + _wordlist.getFindName() + " Items)" + _endl);
        _sbuf.append("    (find-element Items list-" + _wordlist.getWordType() + "))" + _endl);
        return _sbuf.toString();
    }

    /**
	 * Part of the natural language parsing of the incoming message.
	 */
    private void mixSchemeResults(CacheWordList _resultlist) {
        Iterator iter = saveWordTypes.iterator();
        while (iter.hasNext()) {
            String res = null;
            CacheWordList _wordlist = (CacheWordList) iter.next();
            res = _scheme.getStringInput("(length " + _wordlist.getResultName() + ")" + _endl);
            int _len = Integer.parseInt(res.trim());
            if (_len > 0) {
                for (int _i = 0; _i < _len; _i++) {
                    res = _scheme.getStringInput("(list-ref '(" + _wordlist.getSchemeList() + ") " + _i + ")" + _endl);
                    if (!res.equals("#f")) {
                        res = _scheme.getStringInput("(set-list-ref " + _resultlist.getResultName() + " " + _i + " '" + res + ")");
                    }
                }
            }
        }
    }

    /**
	 * Part of the natural language parsing of the incoming message.
	 */
    private void schemeProcessMsg(CacheWordList _wordlist, String _msg) {
        String res = null;
        res = _scheme.getStringInput("(length " + _wordlist.getResultName() + ")" + _endl);
        int _len = Integer.parseInt(res.trim());
        if (_len > 0) {
            for (int _i = 0; _i < _len; _i++) {
                res = _scheme.getStringInput("(list-ref " + _wordlist.getResultName() + " " + _i + ")" + _endl);
                String listRef = _scheme.getStringInput("(list-ref (" + _msg + ") " + _i + ")");
                if (res.equals("#t")) {
                    _wordlist.enqueue(listRef, Boolean.TRUE);
                } else {
                    _wordlist.enqueue(listRef, Boolean.FALSE);
                }
            }
            _wordlist.save();
            _wordlist.clearQueue();
        }
    }

    private String addSchemeMapResults(CacheWordList _wordlist, String _msg) {
        StringBuffer _schemebuf = new StringBuffer();
        _schemebuf.append("(map " + _wordlist.getFindName() + " (" + _msg + "))");
        _schemebuf.append("(define " + _wordlist.getResultName() + " (map " + _wordlist.getFindName() + " (" + _msg + ")))");
        return _schemebuf.toString();
    }

    public void processIncomingMessage(String _incomingMsg) {
        StringBuffer _schemebuf = null;
        _schemebuf = new StringBuffer();
        _schemebuf.append("(define (in-msg) " + _endl);
        _schemebuf.append("    (list " + SchemeUtil.buildListString(_incomingMsg) + "))" + _endl);
        _schemebuf.append("(length (in-msg))");
        for (Iterator _iterate = saveWordTypes.iterator(); _iterate.hasNext(); ) {
            _schemebuf.append(addSchemeMapResults((CacheWordList) _iterate.next(), "in-msg") + _endl);
        }
        _schemebuf.append("(define " + simpleResults.getResultName() + " (list-copy " + simpleNouns.getResultName() + "))");
        _scheme.runEval(_schemebuf.toString());
        for (Iterator __iterate = saveWordTypes.iterator(); __iterate.hasNext(); ) {
            schemeProcessMsg((CacheWordList) __iterate.next(), "in-msg");
        }
        mixSchemeResults(simpleResults);
        schemeProcessMsg((CacheWordList) simpleResults, "in-msg");
    }

    public String getResultString() {
        return _scheme.getStringInput(simpleResults.getResultName());
    }
}
