package com.webloq.io.util.mime;

import java.util.List;
import java.nio.charset.UnsupportedCharsetException;
import com.webloq.io.util.BinaryString;

/**
 * A Utility class for encoding and decoding non-ASCII data as a MIME encoded-word.
 * See RFC 2047
 * @author Evan Buswell
 */
class MIMEEncodedWord {

    /**
	 * Encodes s into the MIME encoded-word format.  Treats as single word, regardless of whitespace.
	 * @param s the String to encode.
	 * @return s as a BinaryString encoded in MIME encoded-word format (null ONLY if UTF-8 is unsupported).
	 */
    public static BinaryString encodeWord(String s) {
        try {
            return encodeWord(new BinaryString(s, "UTF-8"));
        } catch (UnsupportedCharsetException e) {
            return null;
        }
    }

    /**
	 * Encodes bString to MIME encoded-word format.  Treats as single word, regardless of whitespace.
	 * @param bString the BinaryString to encode.
	 * @return if bString is all ascii, returns bString, else returns a MIME encoded-word Base64 encoding of bString.
	 */
    public static BinaryString encodeWord(BinaryString bString) {
        for (int i = 0; i < bString.length(); i++) {
            byte b = bString.byteAt(i);
            if (b < 33 || b > 126) {
                return BinaryString.concat(new BinaryString("=?" + bString.getCharset().toLowerCase() + "?B?"), bString.base64encode(), new BinaryString("?="));
            }
        }
        return bString;
    }

    /**
	 * Encodes s according to MIME encoded-word format.
	 * @param s the String to encode.
	 * @return an MIME encoded BinaryString.
	 */
    public static BinaryString encodePhrase(String s) {
        try {
            return encodePhrase(new BinaryString(s, "UTF-8"));
        } catch (UnsupportedCharsetException e) {
            return null;
        }
    }

    /**
	 * Encodes bString according to MIME encoded-word format.
	 * @param bString the BinaryString to encode
	 * @return an encoded BinaryString.
	 */
    public static BinaryString encodePhrase(BinaryString bString) {
        BinaryString ret = new BinaryString(bString.length());
        int wordStart = -1;
        boolean encodeWord = false;
        for (int i = 0; i < bString.length(); i++) {
            byte b = bString.byteAt(i);
            if (BinaryString.isWhitespace(b)) {
                if (wordStart != -1) {
                    if (encodeWord) {
                        ret.append(new BinaryString("=?" + bString.getCharset().toLowerCase() + "?B?"));
                        ret.append(bString.substring(wordStart, i).base64encode());
                        ret.append(new BinaryString("?="));
                    } else {
                        ret.append(bString.substring(wordStart, i));
                    }
                    wordStart = -1;
                    encodeWord = false;
                }
                ret.append(b);
            } else {
                if (wordStart == -1) {
                    wordStart = i;
                }
                if (b < 1) {
                    encodeWord = true;
                }
            }
        }
        if (wordStart != -1) {
            if (encodeWord) {
                ret.append(new BinaryString("=?" + bString.getCharset().toLowerCase() + "?B?"));
                ret.append(bString.substring(wordStart).base64encode());
                ret.append(new BinaryString("?="));
            } else {
                ret.append(bString.substring(wordStart));
            }
        }
        return ret;
    }

    /**
	 * Decodes a MIME encoded-word encoded BinaryString into a UTF-8 encoded BinaryString.
	 * Warning: only supports Base64 encoding, not the more common quoted-printable.
	 * @param s the BinaryString that is in Ascii or base64.
	 * @return the decoded BinaryString.
	 */
    public static BinaryString decodeWord(BinaryString s) {
        int i = 0;
        int lastI = -1;
        BinaryString ret = new BinaryString(s.length());
        try {
            ret.setCharset("UTF-8");
        } catch (UnsupportedCharsetException e) {
        }
        while ((i = s.indexOf(new BinaryString("=?"), i)) != -1) {
            int j = s.indexOf(new BinaryString("?="), i);
            if (lastI != -1) {
                lastI = 0;
            }
            ret.append(s.substring(lastI, i));
            List<BinaryString> eWord = s.substring(i + 2, j).splitInPlace(new BinaryString("?"));
            if (eWord.get(1).equalsIgnoreCase(new BinaryString("B"))) {
                if (eWord.get(0).equalsIgnoreCase(new BinaryString("utf-8"))) {
                    ret.append(eWord.get(2).base64decode());
                } else {
                    try {
                        ret.append(eWord.get(2).base64decode().toString(eWord.get(0).toString()));
                    } catch (UnsupportedCharsetException e) {
                        ret.append(eWord.get(2).base64decode());
                    }
                }
            } else {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
        return ret;
    }
}
