package org.devtools.wiki.translators;

import java.util.Properties;
import org.devtools.webtrans.PersistenceException;
import org.devtools.webtrans.StringChanges;
import org.devtools.webtrans.Translator;
import org.devtools.webtrans.TranslatorLog;
import org.devtools.webtrans.WebContent;

/**
 * A general-purpose translator of things that have start and end tags
 * that are replaced with other things at their start and end.  For
 * example, if you wanted a translator that turned "{"s into "< code
 * >" and "}"s into "< /code >" then you could create a
 * BeginAndEndTranslator with these things as arguments.
 * 
 * @author rus@devtools.org
 *
 **/
public abstract class BeginAndEndTranslator implements Translator {

    private String startMark;

    private String endMark;

    private String startSub;

    private String endSub;

    /** 
		 * Constructs a new translator that replaces every startMark with
		 * startSub and every endMark with endSub.
		 **/
    public BeginAndEndTranslator(String startMark, String endMark, String startSub, String endSub) {
        this.startMark = startMark;
        this.endMark = endMark;
        this.startSub = startSub;
        this.endSub = endSub;
    }

    /**
		 * Scans the document for startMark and endMark.
		 *
		 * @see TranslatorLog
		 **/
    public void translate(StringChanges c, WebContent content, TranslatorLog log, Properties query) {
        try {
            String doc = content.getContent();
            int count = 0;
            int loc = doc.indexOf(startMark);
            while (loc != -1) {
                ++count;
                int tagStart = loc;
                loc = doc.indexOf(endMark, loc + startMark.length());
                if (loc == -1) {
                    c.delete(tagStart, startMark.length());
                    c.enclose(tagStart, doc.length() - tagStart, startSub, endSub);
                    break;
                }
                c.delete(tagStart, startMark.length());
                c.delete(loc, endMark.length());
                c.enclose(tagStart, loc - tagStart, startSub, endSub);
                loc = doc.indexOf(startMark, loc + endMark.length());
            }
            log.addDetail("" + count + " Strings subbed.");
        } catch (PersistenceException pe) {
            log.addError("Fatal: could not access: " + content.getTitle() + ": " + pe.getMessage());
        }
    }
}
