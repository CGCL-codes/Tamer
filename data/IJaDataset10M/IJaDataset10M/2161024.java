package org.devtools.wiki.translators;

import java.util.Enumeration;
import java.util.Properties;
import org.devtools.webtrans.BadResourceException;
import org.devtools.webtrans.ContentDependencies;
import org.devtools.webtrans.PersistenceException;
import org.devtools.webtrans.StringChanges;
import org.devtools.webtrans.Translator;
import org.devtools.webtrans.TranslatorLog;
import org.devtools.webtrans.TranslatorServices;
import org.devtools.webtrans.WebContent;

/**
 * Picks up all of the mixed-case words in the page as on-site links.
 * If the mixed-case word is not a page of the current type, searches
 * other types.  If it is not a page of any type, inserts an editme
 * (?) after the name.  Only considers pages that have a type in the
 * list OnsiteLinkTypes.
 *
 * @author rus@devtools.org
 *
 **/
public class OnsiteLinkTranslator implements Translator {

    /** 
		 * The services for this doc type
		 **/
    private TranslatorServices srv;

    /**			
		 * Constructs a new onsite link detector.  We'll need access to
		 * the Persister in order to find out if pages exist.
		 **/
    public OnsiteLinkTranslator() {
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
        try {
            String doc = content.getContent();
            int count = 0, loc = 0, len = doc.length();
            while (loc < len) {
                while (loc < len && !Character.isUpperCase(doc.charAt(loc))) ++loc;
                int wordStart = loc++;
                while (loc < len && (Character.isLowerCase(doc.charAt(loc)) || Character.isDigit(doc.charAt(loc)))) ++loc;
                if (loc >= len || !Character.isUpperCase(doc.charAt(loc)) || loc == wordStart + 1) continue; else ++loc;
                while (loc < len && (Character.isUpperCase(doc.charAt(loc)) || Character.isLowerCase(doc.charAt(loc)) || Character.isDigit(doc.charAt(loc)))) ++loc;
                while (wordStart > 0 && Character.isUpperCase(doc.charAt(wordStart - 1))) --wordStart;
                if (!c.isDeleted(wordStart, loc - wordStart)) {
                    ++count;
                    String link = doc.substring(wordStart, loc);
                    boolean done = false;
                    D.msg("detail", "Picking up link \"" + link + "\"");
                    Enumeration e = srv.getMultiProperty("OnsiteLinkTypes");
                    if (!e.hasMoreElements()) e = srv.getTypes();
                    while (e.hasMoreElements()) {
                        TranslatorServices psrv = srv.getServicesForType((String) e.nextElement());
                        String URL = psrv.getBaseURL() + link;
                        D.msg("detail", "Checking service " + psrv + " for URL \"" + URL + "\"");
                        ContentDependencies deps = psrv.getContentDependencies();
                        deps.addCreateDependency(content.getURL(), URL);
                        deps.addDeleteDependency(content.getURL(), URL);
                        if (psrv.getPersister().isContent(URL)) {
                            D.msg("detail", URL + " is a page.");
                            srv.getLinkManager().link(c, log, wordStart, link, URL);
                            done = true;
                            break;
                        } else {
                            D.msg("detail", URL + " is NOT a page.");
                        }
                    }
                    if (!done) {
                        D.msg("detail", link + " is NOT a page of any kind.");
                        srv.getLinkManager().linkNewPage(c, log, wordStart, link);
                    }
                }
            }
            log.addDetail("" + count + " Words converted to on-site links.");
        } catch (PersistenceException pe) {
            log.addError("Fatal: could not access: " + content.getTitle() + ": " + pe.getMessage());
        } catch (BadResourceException bre) {
            log.addError("Fatal: bad URL (OnsiteTranslator start " + "parameters were probably bad.)" + bre.getMessage());
        }
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
        return "OnsiteLinkTranslator";
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
        return "Mixed case words such as \"HelloWorld\" and \"ANewPage\" will be \n" + "converted to on-site links to other Wiki pages.\n" + "If a page doesn't exist, a ? link will appear after\n" + "it, which links to an editor for the page.  Pages with\n" + "names like \"Index\" or \"page\" will not be considered links\n" + "because they are not mixed case.";
    }

    private org.devtools.util.debug.Debugger D = new org.devtools.util.debug.Debugger("OnsiteLinkTranslator");

    {
        D.setDisplayGroups(true);
        D.enable("*");
    }
}
