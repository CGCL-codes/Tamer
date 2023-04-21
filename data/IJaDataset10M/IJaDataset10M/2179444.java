package net.sf.vietpad.converter;

import org.unicode.Normalizer;

public abstract class Converter {

    static final String SERIF = "Times New Roman";

    static final String SANS_SERIF = "Arial";

    static Normalizer composer;

    /**
     *  Converts legacy text to Unicode. To be implemented by subclass.
     */
    public abstract String convert(String source, boolean html);

    /**
     *  Multiple String replacement.
     *
     *@param  text     Text to be performed on
     *@param  pattern  Find text
     *@param  replace  Replace text
     *@return          Result text
     */
    String replaceString(String text, final String[] pattern, final String[] replace) {
        int startIndex;
        int foundIndex;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < pattern.length; i++) {
            startIndex = 0;
            result.setLength(0);
            while ((foundIndex = text.indexOf(pattern[i], startIndex)) >= 0) {
                result.append(text.substring(startIndex, foundIndex));
                result.append(replace[i]);
                startIndex = foundIndex + pattern[i].length();
            }
            result.append(text.substring(startIndex));
            text = result.toString();
        }
        return text;
    }

    /**
     *  Changes HTML meta tag for charset to UTF-8.
     */
    String prepareMetaTag(String str) {
        return str.replaceAll("(?i)charset=(?:iso-8859-1|windows-1252|windows-1258|us-ascii|x-user-defined)", "").replaceAll("(?i)<meta http-equiv=\"?Content-Type\"? content=\"text/html;\\s*\">\\n?", "").replaceAll("(?i)<head>", "<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
    }

    /**
     *  Translates Character entity references to corresponding Cp1252 characters.
     */
    String htmlToANSI(String str) {
        final String[] extended_ansi_html = { "&trade;", "&#8209;", "&nbsp;", "&iexcl;", "&cent;", "&pound;", "&curren;", "&yen;", "&brvbar;", "&sect;", "&uml;", "&copy;", "&ordf;", "&laquo;", "&not;", "&shy;", "&reg;", "&macr;", "&deg;", "&plusmn;", "&sup2;", "&sup3;", "&acute;", "&micro;", "&para;", "&middot;", "&cedil;", "&sup1;", "&ordm;", "&raquo;", "&frac14;", "&frac12;", "&frac34;", "&iquest;", "&Agrave;", "&Aacute;", "&Acirc;", "&Atilde;", "&Auml;", "&Aring;", "&AElig;", "&Ccedil;", "&Egrave;", "&Eacute;", "&Ecirc;", "&Euml;", "&Igrave;", "&Iacute;", "&Icirc;", "&Iuml;", "&ETH;", "&Ntilde;", "&Ograve;", "&Oacute;", "&Ocirc;", "&Otilde;", "&Ouml;", "&times;", "&Oslash;", "&Ugrave;", "&Uacute;", "&Ucirc;", "&Uuml;", "&Yacute;", "&THORN;", "&szlig;", "&agrave;", "&aacute;", "&acirc;", "&atilde;", "&auml;", "&aring;", "&aelig;", "&ccedil;", "&egrave;", "&eacute;", "&ecirc;", "&euml;", "&igrave;", "&iacute;", "&icirc;", "&iuml;", "&eth;", "&ntilde;", "&ograve;", "&oacute;", "&ocirc;", "&otilde;", "&ouml;", "&divide;", "&oslash;", "&ugrave;", "&uacute;", "&ucirc;", "&uuml;", "&yacute;", "&thorn;", "&yuml;" };
        final String[] extended_ansi = { "", "‑", " ", "¡", "¢", "£", "¤", "¥", "¦", "§", "¨", "©", "ª", "«", "¬", "­", "®", "¯", "°", "±", "²", "³", "´", "µ", "¶", "·", "¸", "¹", "º", "»", "¼", "½", "¾", "¿", "À", "Á", "Â", "Ã", "Ä", "Å", "Æ", "Ç", "È", "É", "Ê", "Ë", "Ì", "Í", "Î", "Ï", "Ð", "Ñ", "Ò", "Ó", "Ô", "Õ", "Ö", "×", "Ø", "Ù", "Ú", "Û", "Ü", "Ý", "Þ", "ß", "à", "á", "â", "ã", "ä", "å", "æ", "ç", "è", "é", "ê", "ë", "ì", "í", "î", "ï", "ð", "ñ", "ò", "ó", "ô", "õ", "ö", "÷", "ø", "ù", "ú", "û", "ü", "ý", "þ", "ÿ" };
        return replaceString(str, extended_ansi_html, extended_ansi);
    }

    /**
     *  Converts Numeric Character References and Unicode escape sequences to Unicode.
     */
    String convertNCR(String str) {
        final String[] NCRs = { "&#x", "&#", "\\u", "U+", "#x", "#" };
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < NCRs.length; i++) {
            int radix;
            int foundIndex;
            int startIndex = 0;
            final int STR_LENGTH = str.length();
            final String NCR = NCRs[i];
            final int NCR_LENGTH = NCR.length();
            if (NCR == "&#" || NCR == "#") {
                radix = 10;
            } else {
                radix = 16;
            }
            while (startIndex < STR_LENGTH) {
                foundIndex = str.indexOf(NCR, startIndex);
                if (foundIndex == -1) {
                    result.append(str.substring(startIndex));
                    break;
                }
                result.append(str.substring(startIndex, foundIndex));
                if (NCR == "\\u" || NCR == "U+") {
                    startIndex = foundIndex + 6;
                    if (startIndex > str.length()) startIndex = -1;
                } else {
                    startIndex = str.indexOf(";", foundIndex);
                }
                if (startIndex == -1) {
                    result.append(str.substring(foundIndex));
                    break;
                }
                String tok = str.substring(foundIndex + NCR_LENGTH, startIndex);
                try {
                    result.append((char) Integer.parseInt(tok, radix));
                } catch (NumberFormatException nfe) {
                    try {
                        if (NCR == "\\u" || NCR == "U+") {
                            result.append(NCR + tok);
                        } else {
                            result.append(NCR + tok + str.charAt(startIndex));
                        }
                    } catch (StringIndexOutOfBoundsException sioobe) {
                        result.append(NCR + tok);
                    }
                }
                if (NCR != "\\u" && NCR != "U+") {
                    startIndex++;
                }
            }
            str = result.toString();
            result.setLength(0);
        }
        return str;
    }

    /**
     * Converts Cp1252 characters in - range to pure hex.
     * This method is required for VISCII and VPS because these encodings
     * utilize characters in this range.
     */
    String cp1252ToHex(String str) {
        final char[] cha = { '€', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', 'Ž', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', 'ž', 'Ÿ' };
        final char[] hex = { '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '' };
        for (int i = 0; i < hex.length; i++) {
            str = str.replace(cha[i], hex[i]);
        }
        return str;
    }

    /**
     *  Unicode Composite-to-Unicode Precomposed conversion (NFD -> NFC).
     */
    String compositeToPrecomposed(String str) {
        if (composer == null) {
            composer = new Normalizer(Normalizer.C, false);
        }
        return composer.normalize(str);
    }

    /**
     * Converts HTML.
     */
    String convertHTML(String str) {
        return replaceFont(prepareMetaTag(convertNCR(htmlToANSI(str))));
    }

    /**
     * Replaces fonts.
     */
    String replaceFont(String str) {
        return str;
    }
}
