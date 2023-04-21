package net.sf.intltyper.lib;

import java.io.*;
import java.net.*;

/**
 * Facade class for users of the IntlTyper library. This class manages
 * internal objects like {@link KeySequenceList}s and {@link
 * UnicodeList}s.
 */
public class IntlTyperFacade {

    public static final String VERSION = "0.1";

    private KeySequenceList ksl;

    private URL unicodeListURL, rfc1345URL;

    private UnicodeList ucl;

    private RFC1345List rfc1345;

    /**
     * Creates a new IntlTyperFacade - users might prefer the other constructor.
     *
     * @param ksl The KeySequencsList to user
     * @param unicodeListURL URL to load the unicode list from.
     */
    public IntlTyperFacade(KeySequenceList ksl, URL unicodeListURL) {
        this.ksl = ksl;
        this.unicodeListURL = unicodeListURL;
    }

    public IntlTyperFacade(URL defsURL, File defsOverride, URL unicodeListURL) {
        this(defsURL, defsOverride, unicodeListURL, null);
    }

    /**
     * Creates a new IntlTyperFacade
     *
     * @param defsURL URL to load definitions from
     * @param defsOverride File to load and save definitions
     * @param unicodeListURL URL to load the Unicode list from
     * @param rfc1345URL URL to load the mnemonics from
     */
    public IntlTyperFacade(URL defsURL, File defsOverride, URL unicodeListURL, URL rfc1345URL) {
        this.unicodeListURL = unicodeListURL;
        this.rfc1345URL = rfc1345URL;
        try {
            InputStream in = defsURL.openStream();
            if (in != null) {
                ksl = new KeySequenceList(null, defsURL.openStream());
            } else {
                ksl = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            ksl = null;
        }
        if (defsOverride != null) {
            try {
                ksl = new KeySequenceList(ksl, defsOverride);
            } catch (IOException ex) {
                ksl = new KeySequenceList(ksl);
            }
        } else {
            ksl = new KeySequenceList(ksl);
        }
    }

    /**
     * Returns the KeySequenceList used by this instance.
     */
    protected KeySequenceList getKSL() {
        return ksl;
    }

    /**
     * Builds a new GUIHelper from this facade. Every text component supporting IntlTyper must have its <b>own</b> instance!
     */
    public GUIHelper buildGUIHelper(GUIAdapter ga) {
        return new GUIHelper(this, ga);
    }

    /**
     * Save key sequences created by GUIHelpers of this facade.
     */
    public void saveKeySequences() {
        ksl.save();
    }

    /**
     * Return the UnicodeList used by this facade.
     */
    public UnicodeList getUnicodeList() {
        if (ucl == null) ucl = new UnicodeList(unicodeListURL);
        return ucl;
    }

    public RFC1345List getRFC1345List() {
        if (rfc1345 == null) {
            rfc1345 = new RFC1345List(rfc1345URL);
        }
        return rfc1345;
    }
}
