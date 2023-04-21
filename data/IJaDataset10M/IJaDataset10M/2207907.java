package org.bongolipi.btrans.editor;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.text.StyleConstants;
import org.apache.log4j.Logger;
import org.bongolipi.btrans.core.BParser;
import org.bongolipi.btrans.core.BengaliTransform;
import org.bongolipi.btrans.core.BtransException;
import org.bongolipi.btrans.core.InputConverter;
import org.bongolipi.btrans.core.SpecialGlyph;
import org.bongolipi.btrans.service.Configuration;
import org.bongolipi.util.Data;
import static org.bongolipi.btrans.util.BtransConstants.*;

public class BtransEditorInputProcessor extends BtransEditor implements Runnable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(BtransEditorInputProcessor.class);

    private static Configuration internalOption = Configuration.getConfig(INTERNAL_PROP_FILE);

    private BParser r;

    protected InputConverter currentToBaseSchemeConverter;

    protected InputConverter baseToCurrentSchemeConverter;

    private BengaliTransform bt;

    private BtransEditor editor;

    private char lastTypedChar = '\0';

    private char beforeLastTypedChar = '\0';

    private char twoBeforeLastTypedChar = '\0';

    private String escapedForScheme;

    private String residualForScheme;

    private String currentScheme = "";

    private String indicFontName = "";

    LinkedBlockingQueue<KeyEvent> inQueue = new LinkedBlockingQueue<KeyEvent>();

    BtransEditorInputProcessor(BtransEditor editorInstance, String scheme, String indicFont) {
        this.editor = editorInstance;
        this.currentScheme = scheme;
        this.indicFontName = indicFont;
        init_trans();
    }

    private void init_trans() {
        this.r = new BParser();
        updateBengaliTransform();
        changeScheme(currentScheme);
    }

    private void changeScheme(String schemeName) {
        String scheme = schemeName.toLowerCase();
        currentScheme = scheme;
        if (log.isInfoEnabled()) {
            log.info("Changing scheme to " + scheme);
        }
        escapedForScheme = internalOption.getProperty(COMMON_ESCAPE_FOR_SCHEME_KEY, "") + internalOption.getProperty(scheme + ESCAPE_FOR_SCHEME_KEY_SUFFIX, "");
        residualForScheme = internalOption.getProperty(COMMON_RESIDUAL_FOR_SCHEME_KEY, "") + internalOption.getProperty(scheme + RESIDUAL_FOR_SCHEME_KEY_SUFFIX, "");
        if (!currentScheme.equals(BASE_SCHEME.toLowerCase())) {
            try {
                currentToBaseSchemeConverter = InputConverter.getConverter(currentScheme, BASE_SCHEME);
                baseToCurrentSchemeConverter = InputConverter.getConverter(BASE_SCHEME, currentScheme);
            } catch (BtransException bEx) {
                log.error(bEx);
                showError(bEx.getMessage());
            }
        } else {
            currentToBaseSchemeConverter = null;
            baseToCurrentSchemeConverter = null;
        }
        this.updateDelimSet();
    }

    /**
	 * Update BengaliTransform object. Must be called everytime supported font
	 * change.
	 */
    private void updateBengaliTransform() {
        if (log.isDebugEnabled()) {
            log.debug("instantiating new BengaliTransform object with " + indicFontName);
        }
        try {
            bt = new BengaliTransform(indicFontName);
        } catch (BtransException bEx) {
            log.error(bEx);
            showError("Cannot change font: " + bEx.getMessage());
        }
    }

    protected String indicToInternal(String indicText) {
        String internalForm = bt.getInternalForm(indicText);
        return internalForm;
    }

    protected String internalToIndic(String internalText) {
        Properties iFontProp = Configuration.getFontProperties(indicFontName);
        SpecialGlyph iSpecialGlyph = SpecialGlyph.getSpecialGlyph(indicFontName);
        if (null == iFontProp || null == iSpecialGlyph) {
            log.error("iFontProp or iSpecialGlypg is null");
            return "";
        }
        String[] internalChar = internalText.split("U");
        StringBuilder indic = new StringBuilder();
        String indicChar = null;
        for (int count = 0; count < internalChar.length; count++) {
            indicChar = (String) iFontProp.get(Data.makeUChar(internalChar[count]));
            if (null != indicChar) {
                indic.append(indicChar);
            }
        }
        return iSpecialGlyph.replace_special(indic.toString());
    }

    protected void switchToIndic(String indicFont) {
        this.indicFontName = indicFont;
        if (log.isInfoEnabled()) {
            log.info("Changing font to " + indicFontName);
        }
        editor.isTypingRoman = false;
        updateBengaliTransform();
        updateDelimSet();
        editor.switchToIndic();
        return;
    }

    protected String getRoman(String indicText) {
        String roman = null;
        try {
            roman = bt.getRoman(indicText);
            if (log.isDebugEnabled()) {
                log.debug("indic: " + indicText);
                log.debug("reverse transformed roman (before scheme conversion): " + roman);
            }
            if (!currentScheme.equalsIgnoreCase(BASE_SCHEME)) {
                roman = baseToCurrentSchemeConverter.convert(roman);
            }
        } catch (Exception ex) {
            log.error(ex);
            showError(ex.getMessage());
        }
        return roman;
    }

    /**
	 * Creates the delimiter set from the font's delimiter set and removing 
	 * the escape characters of the scheme. This delim set must be created
	 * everytime the supported font changes or scheme changes.
	 */
    private void updateDelimSet() {
        HashSet<String> s = new HashSet<String>();
        if (UNICODE_FONT_SET.contains(indicFontName)) {
            for (int c = 0; c < DELIM.length(); c++) {
                s.add(DELIM.substring(c, c + 1));
            }
        } else {
            s.addAll(BengaliTransform.UNICODE_DELIM);
        }
        char[] escapedChars = escapedForScheme.toCharArray();
        char[] residualChars = residualForScheme.toCharArray();
        for (int c = 0; c < escapedChars.length; c++) {
            s.remove(String.valueOf(escapedChars[c]));
        }
        for (int c = 0; c < residualChars.length; c++) {
            s.remove(String.valueOf(residualChars[c]));
        }
        editor.delimSet = s;
    }

    void processChunkText(String text, String indicFont) {
        r.registerHandler(editor);
        StringTokenizer st = new StringTokenizer(text, Data.setToString(editor.delimSet), true);
        try {
            String itransRoman = null;
            String token = null;
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (log.isDebugEnabled()) {
                    log.debug("Unicode token before conversion: '" + token + " (" + Data.getEscapedUnicode(token) + ")'");
                }
                if (token.equals(" ")) {
                    editor.acceptTransformedWord(token, indicFontName, true);
                } else {
                    if (null != currentToBaseSchemeConverter) {
                        if (log.isDebugEnabled()) {
                            log.debug("converting to base-scheme");
                        }
                        itransRoman = currentToBaseSchemeConverter.convert(token);
                    } else {
                        itransRoman = token;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("base-scheme roman: " + itransRoman);
                    }
                    r.processToken(itransRoman.toCharArray(), indicFont);
                }
            }
        } catch (Exception e) {
            BtransEditor.showError("Unrecoverable error: " + e.toString());
        } finally {
            r.unRegisterHandler();
        }
    }

    /**
	 * This methods transforms the text and returns transformed text. This is similar to 
	 * <code>processChunkText()</code> method, but this method returns the transformed text 
	 * in a chunk, unlike <code>processChunkText()</code> which calls caller's 
	 * <code>acceptTransformedWord()</code> token-by-token 
	 * @param roman Text to transform
	 * @param indicFont Indic font of the transformed text 
	 * @return
	 */
    String transformChunkText(String roman, String indicFont) {
        r.unRegisterHandler();
        String transformedText = null;
        try {
            if (null != currentToBaseSchemeConverter) {
                roman = currentToBaseSchemeConverter.convert(roman);
            }
            r.parseText(roman, indicFont);
            transformedText = r.getTransformedContent();
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug(Data.getPrintableStackTrace(ex));
            }
            BtransEditor.showError("Unrecoverable error: " + ex.toString());
        } finally {
            r.registerHandler(editor);
        }
        if (log.isDebugEnabled()) {
            log.debug("Transformed chunk text to " + indicFont + "\n" + "roman: " + roman + "\n" + "transformed: " + Data.getEscapedUnicode(transformedText));
        }
        return Data.strip(transformedText, UNULL);
    }

    void processKeyInput(KeyEvent e) {
        char thisKey = e.getKeyChar();
        int keycode = e.getKeyCode();
        if (log.isDebugEnabled()) {
            log.debug("keyTyped: " + thisKey + "(" + keycode + ")");
        }
        if (Character.isDigit(thisKey) && !UNICODE_FONT_SET.contains(indicFontName)) {
            return;
        }
        e.consume();
        editor.dot = editor.caret.getDot();
        StringBuilder reverseIndic = new StringBuilder();
        String indic;
        String roman = "";
        try {
            if (thisKey == KeyEvent.VK_BACK_SPACE) {
                lastTypedChar = beforeLastTypedChar;
                beforeLastTypedChar = '\0';
                return;
            } else if (editor.delimSet.contains(String.valueOf(thisKey))) {
                if (log.isDebugEnabled()) {
                    log.debug("delim typed: '" + Data.getEscapedUnicode(thisKey) + "'");
                }
                if (thisKey == KeyEvent.VK_ENTER) {
                    if (log.isDebugEnabled()) {
                        log.debug("ENTER: doing nothing");
                    }
                    resetCharMemory();
                    return;
                }
                String tDelim = r.processToken(new char[] { thisKey }, indicFontName);
                editor.showWord(tDelim, indicFontName, true);
            } else {
                if (!Data.isPresent(VOWEL, thisKey) && !Data.isPresent(CONSONANT, thisKey) && !Data.isPresent(escapedForScheme, thisKey) && !Data.isPresent(residualForScheme, thisKey) && !Character.isDigit(thisKey)) {
                    if (log.isDebugEnabled()) {
                        log.debug("unhandled key: '" + thisKey + "'. Returning.");
                    }
                    return;
                }
                indic = editor.getCurrentIndic(editor.dot);
                if (log.isDebugEnabled()) {
                    log.debug("current indic: " + indic);
                }
                roman = bt.getRoman(indic);
                if (log.isDebugEnabled()) {
                    log.debug("reverse transformed roman (before scheme conversion): " + roman);
                }
                if (null != baseToCurrentSchemeConverter) {
                    roman = baseToCurrentSchemeConverter.convert(roman);
                }
                if (log.isDebugEnabled()) {
                    log.debug("reverse transformed roman (after): " + roman);
                    log.debug("before last typed: " + twoBeforeLastTypedChar);
                    log.debug("before last typed: " + beforeLastTypedChar);
                    log.debug("last typed: " + lastTypedChar);
                }
                reverseIndic.append(roman);
                if (beforeLastTypedChar == 'a' && (Data.isPresent(escapedForScheme, lastTypedChar) || Data.isPresent(residualForScheme, lastTypedChar)) && roman.endsWith("a")) {
                    roman = (new StringBuilder(roman).append(lastTypedChar)).toString();
                    if (log.isDebugEnabled()) {
                        log.debug("regenerated roman: " + roman);
                    }
                }
                if (lastTypedChar != 'a' && roman.endsWith("a")) {
                    reverseIndic.deleteCharAt(reverseIndic.length() - 1);
                    if (log.isDebugEnabled()) {
                        log.debug("clipped roman: " + reverseIndic.toString());
                    }
                }
                if (Data.isPresent(escapedForScheme, beforeLastTypedChar) && reverseIndic.length() > 1 && reverseIndic.charAt(reverseIndic.length() - 2) != beforeLastTypedChar) {
                    reverseIndic.deleteCharAt(reverseIndic.length() - 1).append(beforeLastTypedChar).append(lastTypedChar);
                }
                if (Data.isPresent(escapedForScheme, lastTypedChar) || Data.isPresent(residualForScheme, lastTypedChar)) {
                    if (Data.isPresent(escapedForScheme, beforeLastTypedChar) || Data.isPresent(residualForScheme, beforeLastTypedChar)) {
                        if (twoBeforeLastTypedChar == 'a' && roman.endsWith("a")) {
                            System.out.println("here");
                            reverseIndic.append(twoBeforeLastTypedChar).append(beforeLastTypedChar).append(lastTypedChar);
                        } else {
                            reverseIndic.append(beforeLastTypedChar).append(lastTypedChar);
                        }
                    } else if (beforeLastTypedChar == 'a' && roman.endsWith("a")) {
                        reverseIndic.append(beforeLastTypedChar).append(lastTypedChar);
                    } else {
                        reverseIndic.append(lastTypedChar);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("regenerated roman: " + reverseIndic.toString());
                    }
                }
                reverseIndic.append(thisKey);
                roman = reverseIndic.toString();
                if (log.isDebugEnabled()) {
                    log.debug("roman: " + roman);
                }
                if (null != currentToBaseSchemeConverter) {
                    roman = currentToBaseSchemeConverter.convert(roman);
                }
                if (log.isDebugEnabled()) {
                    log.debug("roman (after scheme conversion): " + roman);
                }
                if (!Data.isPresent(escapedForScheme, thisKey) && !Data.isPresent(residualForScheme, thisKey)) {
                    String tWord = r.processToken(roman.toCharArray(), indicFontName);
                    tWord = Data.strip(tWord, UNULL);
                    if (log.isDebugEnabled()) {
                        log.debug("After stripping UNULL with '" + Data.getEscapedUnicode("") + "': " + Data.getEscapedUnicode(tWord));
                    }
                    editor.showWord(tWord, indicFontName, false);
                }
            }
        } catch (NullPointerException npEx) {
            log.debug(Data.getPrintableStackTrace(npEx));
            showError("A null pointer error has happenned");
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append(ex);
            log.error("\n" + Data.getPrintableStackTrace(ex));
            showError(sb.toString());
        }
        twoBeforeLastTypedChar = beforeLastTypedChar;
        beforeLastTypedChar = lastTypedChar;
        lastTypedChar = thisKey;
    }

    void resetCharMemory() {
        lastTypedChar = '\0';
        beforeLastTypedChar = '\0';
        twoBeforeLastTypedChar = '\0';
    }

    public void run() {
    }
}
