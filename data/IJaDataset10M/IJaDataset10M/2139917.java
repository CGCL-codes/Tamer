package org.devtools.wiki.translators;

import java.util.Properties;
import java.util.Vector;
import org.devtools.util.MultiProperties;
import org.devtools.webtrans.StringChanges;
import org.devtools.webtrans.Translator;
import org.devtools.webtrans.TranslatorLog;
import org.devtools.webtrans.TranslatorServices;
import org.devtools.webtrans.WebContent;

/**
 * Allows the user to specify arbitrary Wiki syntax in their page by
 * first looking for [userdef ] widgets and then applying the syntax
 * they specify to the page.
 *
 * @author rus@devtools.org
 *
 **/
public class UserDefinedTranslator implements Translator {

    /** 
		 * The site's translation services
		 **/
    protected TranslatorServices srv;

    private static final String START_MARK_PROP = "smark";

    private static final String END_MARK_PROP = "emark";

    private static final String START_SUB_PROP = "ssub";

    private static final String END_SUB_PROP = "esub";

    private class Subber extends BeginAndEndTranslator {

        Subber(String a, String b, String c, String d) {
            super(a, b, c, d);
            if (a == null || b == null || c == null || d == null) throw new NullPointerException();
        }

        public String getName() {
            return "";
        }

        public String getUsage() {
            return "";
        }

        public void init(TranslatorServices srv) {
        }
    }

    private class UserdefCollector extends WidgetTranslator {

        UserdefCollector() {
            super("userdef");
        }

        public Vector subs = new Vector();

        public String getName() {
            return "";
        }

        public String getUsage() {
            return "";
        }

        public String handleWidget(MultiProperties options, TranslatorLog log) {
            try {
                subs.addElement(new Subber(options.getProperty(START_MARK_PROP), options.getProperty(END_MARK_PROP), options.getProperty(START_SUB_PROP), options.getProperty(END_SUB_PROP)));
            } catch (NullPointerException npe) {
                log.addWarning("User-defined translation syntax ignored.");
            }
            return "";
        }
    }

    /**			
		 *
		 **/
    public UserDefinedTranslator() {
    }

    /** 
		 * Returns the name of the translator.  This should be something
		 * descriptive and human-readable that uniquely indicates what the
		 * translator does.  This name will be included in error logging
		 * if this translator has a conflict with another one, throws an   
		 * exception, etc.
		 *
		 * @return The name of this translator
		 **/
    public String getName() {
        return "UserDefinedTranslator";
    }

    /** 
		 * Returns some HTML representing a description of what text this
		 * translator processes.  This will be shown to the user so that
		 * they can determine how to write their documents.  (If this
		 * translator has no usage or if you wish it to be invisible to
		 * the user, return null.  This Translator will not be included in
		 * the usage list.)
		 *
		 * @return The usage for this translator, as HTML.  Return null to
		 * hide this translator from the user.  (The translator will still
		 * be called on each document even if it is hidden.)
		 **/
    public String getUsage() {
        return "You can place syntax specifiers onto your page like this:\n" + "[userdef startmark=RRR endmark=RRR startsub=&ltFONT COLOR=#FF0000&gt endsub=&lt/FONT&gt]\n" + "Then any occurrances of RRR will be replaced with the tags you wanted.";
    }

    /** 
		 * A constructor-like initialization method.  The
		 * TranslatorManager will call this method exactly once, before
		 * any calls to translate().  The Translator may choose to do
		 * nothing during this method, or it may choose to save the
		 * reference to the provided TranslatorServices, which provide
		 * getters for commonly needed information such as the site's
		 * Persister, its base URL, the location of Special Pages such as
		 * the front page, search page, edit page, putter page,
		 * etc.<BR><BR>
		 *
		 * This method replaces a parametric constructor, so that
		 * Translators can be created using the getInstance() method on
		 * class Class, which requires a no-arg constructor.
		 *
		 * @param srv The services provided to this Translator.
		 *
		 * @see org.devtools.webtrans.TranslatorServices
		 **/
    public void init(TranslatorServices srv) {
        this.srv = srv;
    }

    /** 
		 * Translates the given document in some way, returning the
		 * changes that should be made to the document to complete the
		 * translation.  This method should not throw exceptions even if
		 * it is handed a document that it cannot deal with; translators
		 * should instead quietly fail (for similar reasons to why web
		 * browsers are so error-tolerant.)<BR><BR>
		 *
		 * If, however, the translator needs to give feedback to the
		 * caller, it can do so by adding a message into the
		 * TranslatorLog.  In most implementations, I would expect these
		 * strings to be human-readable, so that they could be presented
		 * to an end-user to help them revise their document.<BR><BR>
		 *
		 * Take a look at the TranslatorLog interface for a list of the
		 * information that a Translator is expected to furnish.
		 *
		 * @param c The changes object to which changes should be added.
		 *
		 * @param doc The document to translate.
		 *
		 * @param log A log to contain any errors, warnings,
		 * notifications, or feedback that the translators want to pass
		 * back to the requestor.  These notifications can then be
		 * ignored, logged, or shown to the user during a post-edit
		 * proof-reading phase.  (You could use this to implement
		 * spell-checking.)
		 *
		 * @return The changes that the translator would like to make to
		 * the given document.  Translators should never return null, even
		 * if they make no changes to the document.  Return an empty
		 * StringChanges instead.
		 *
		 * @see TranslatorLog
		 **/
    public void translate(StringChanges c, WebContent content, TranslatorLog log, Properties query) {
        UserdefCollector col = new UserdefCollector();
        col.init(srv);
        col.translate(c, content, log, query);
        for (int i = col.subs.size() - 1; i >= 0; --i) {
            Subber s = (Subber) col.subs.elementAt(i);
            s.init(srv);
            s.translate(c, content, log, query);
        }
    }
}
