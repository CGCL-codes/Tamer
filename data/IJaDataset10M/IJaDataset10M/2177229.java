package com.sun.corba.se.impl.encoding;

import java.util.Map;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.UnmappableCharacterException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;

/**
 * Collection of classes, interfaces, and factory methods for
 * CORBA code set conversion.
 *
 * This is mainly used to shield other code from the sun.io
 * converters which might change, as well as provide some basic
 * translation from conversion to CORBA error exceptions.  Some
 * extra work is required here to facilitate the way CORBA
 * says it uses UTF-16 as of the 00-11-03 spec.
 *
 * REVISIT - Since the nio.Charset and nio.Charset.Encoder/Decoder
 *           use NIO ByteBuffer and NIO CharBuffer, the interaction
 *           and interface between this class and the CDR streams
 *           should be looked at more closely for optimizations to
 *           avoid unnecessary copying of data between char[] &
 *           CharBuffer and byte[] & ByteBuffer, especially
 *           DirectByteBuffers.
 *
 */
public class CodeSetConversion {

    /**
     * Abstraction for char to byte conversion.
     *
     * Must be used in the proper sequence:
     *
     * 1)  convert
     * 2)  Optional getNumBytes and/or getAlignment (if necessary)
     * 3)  getBytes (see warning)
     */
    public abstract static class CTBConverter {

        public abstract void convert(char chToConvert);

        public abstract void convert(String strToConvert);

        public abstract int getNumBytes();

        public abstract float getMaxBytesPerChar();

        public abstract boolean isFixedWidthEncoding();

        public abstract int getAlignment();

        public abstract byte[] getBytes();
    }

    /**
     * Abstraction for byte to char conversion.
     */
    public abstract static class BTCConverter {

        public abstract boolean isFixedWidthEncoding();

        public abstract int getFixedCharWidth();

        public abstract int getNumChars();

        public abstract char[] getChars(byte[] bytes, int offset, int length);
    }

    /**
     * Implementation of CTBConverter which uses a nio.Charset.CharsetEncoder
     * to do the real work.  Handles translation of exceptions to the 
     * appropriate CORBA versions.
     */
    private class JavaCTBConverter extends CTBConverter {

        private ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_ENCODING);

        private OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);

        private CharsetEncoder ctb;

        private int alignment;

        private char[] chars = null;

        private int numBytes = 0;

        private int numChars = 0;

        private ByteBuffer buffer;

        private OSFCodeSetRegistry.Entry codeset;

        public JavaCTBConverter(OSFCodeSetRegistry.Entry codeset, int alignmentForEncoding) {
            try {
                ctb = cache.getCharToByteConverter(codeset.getName());
                if (ctb == null) {
                    Charset tmpCharset = Charset.forName(codeset.getName());
                    ctb = tmpCharset.newEncoder();
                    cache.setConverter(codeset.getName(), ctb);
                }
            } catch (IllegalCharsetNameException icne) {
                throw wrapper.invalidCtbConverterName(icne, codeset.getName());
            } catch (UnsupportedCharsetException ucne) {
                throw wrapper.invalidCtbConverterName(ucne, codeset.getName());
            }
            this.codeset = codeset;
            alignment = alignmentForEncoding;
        }

        public final float getMaxBytesPerChar() {
            return ctb.maxBytesPerChar();
        }

        public void convert(char chToConvert) {
            if (chars == null) chars = new char[1];
            chars[0] = chToConvert;
            numChars = 1;
            convertCharArray();
        }

        public void convert(String strToConvert) {
            if (chars == null || chars.length < strToConvert.length()) chars = new char[strToConvert.length()];
            numChars = strToConvert.length();
            strToConvert.getChars(0, numChars, chars, 0);
            convertCharArray();
        }

        public final int getNumBytes() {
            return numBytes;
        }

        public final int getAlignment() {
            return alignment;
        }

        public final boolean isFixedWidthEncoding() {
            return codeset.isFixedWidth();
        }

        public byte[] getBytes() {
            return buffer.array();
        }

        private void convertCharArray() {
            try {
                buffer = ctb.encode(CharBuffer.wrap(chars, 0, numChars));
                numBytes = buffer.limit();
            } catch (IllegalStateException ise) {
                throw wrapper.ctbConverterFailure(ise);
            } catch (MalformedInputException mie) {
                throw wrapper.badUnicodePair(mie);
            } catch (UnmappableCharacterException uce) {
                throw omgWrapper.charNotInCodeset(uce);
            } catch (CharacterCodingException cce) {
                throw wrapper.ctbConverterFailure(cce);
            }
        }
    }

    /**
     * Special UTF16 converter which can either always write a BOM
     * or use a specified byte order without one.
     */
    private class UTF16CTBConverter extends JavaCTBConverter {

        public UTF16CTBConverter() {
            super(OSFCodeSetRegistry.UTF_16, 2);
        }

        public UTF16CTBConverter(boolean littleEndian) {
            super(littleEndian ? OSFCodeSetRegistry.UTF_16LE : OSFCodeSetRegistry.UTF_16BE, 2);
        }
    }

    /**
     * Implementation of BTCConverter which uses a sun.io.ByteToCharConverter
     * for the real work.  Handles translation of exceptions to the 
     * appropriate CORBA versions.
     */
    private class JavaBTCConverter extends BTCConverter {

        private ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_ENCODING);

        private OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);

        protected CharsetDecoder btc;

        private char[] buffer;

        private int resultingNumChars;

        private OSFCodeSetRegistry.Entry codeset;

        public JavaBTCConverter(OSFCodeSetRegistry.Entry codeset) {
            btc = this.getConverter(codeset.getName());
            this.codeset = codeset;
        }

        public final boolean isFixedWidthEncoding() {
            return codeset.isFixedWidth();
        }

        public final int getFixedCharWidth() {
            return codeset.getMaxBytesPerChar();
        }

        public final int getNumChars() {
            return resultingNumChars;
        }

        public char[] getChars(byte[] bytes, int offset, int numBytes) {
            try {
                ByteBuffer byteBuf = ByteBuffer.wrap(bytes, offset, numBytes);
                CharBuffer charBuf = btc.decode(byteBuf);
                resultingNumChars = charBuf.limit();
                if (charBuf.limit() == charBuf.capacity()) {
                    buffer = charBuf.array();
                } else {
                    buffer = new char[charBuf.limit()];
                    charBuf.get(buffer, 0, charBuf.limit()).position(0);
                }
                return buffer;
            } catch (IllegalStateException ile) {
                throw wrapper.btcConverterFailure(ile);
            } catch (MalformedInputException mie) {
                throw wrapper.badUnicodePair(mie);
            } catch (UnmappableCharacterException uce) {
                throw omgWrapper.charNotInCodeset(uce);
            } catch (CharacterCodingException cce) {
                throw wrapper.btcConverterFailure(cce);
            }
        }

        /**
         * Utility method to find a CharsetDecoder in the
         * cache or create a new one if necessary.  Throws an
         * INTERNAL if the code set is unknown.
         */
        protected CharsetDecoder getConverter(String javaCodeSetName) {
            CharsetDecoder result = null;
            try {
                result = cache.getByteToCharConverter(javaCodeSetName);
                if (result == null) {
                    Charset tmpCharset = Charset.forName(javaCodeSetName);
                    result = tmpCharset.newDecoder();
                    cache.setConverter(javaCodeSetName, result);
                }
            } catch (IllegalCharsetNameException icne) {
                throw wrapper.invalidBtcConverterName(icne, javaCodeSetName);
            }
            return result;
        }
    }

    /**
     * Special converter for UTF16 since it's required to optionally
     * support a byte order marker while the internal Java converters
     * either require it or require that it isn't there.
     *
     * The solution is to check for the byte order marker, and if we
     * need to do something differently, switch internal converters.
     */
    private class UTF16BTCConverter extends JavaBTCConverter {

        private boolean defaultToLittleEndian;

        private boolean converterUsesBOM = true;

        private static final char UTF16_BE_MARKER = (char) 0xfeff;

        private static final char UTF16_LE_MARKER = (char) 0xfffe;

        public UTF16BTCConverter(boolean defaultToLittleEndian) {
            super(OSFCodeSetRegistry.UTF_16);
            this.defaultToLittleEndian = defaultToLittleEndian;
        }

        public char[] getChars(byte[] bytes, int offset, int numBytes) {
            if (hasUTF16ByteOrderMarker(bytes, offset, numBytes)) {
                if (!converterUsesBOM) switchToConverter(OSFCodeSetRegistry.UTF_16);
                converterUsesBOM = true;
                return super.getChars(bytes, offset, numBytes);
            } else {
                if (converterUsesBOM) {
                    if (defaultToLittleEndian) switchToConverter(OSFCodeSetRegistry.UTF_16LE); else switchToConverter(OSFCodeSetRegistry.UTF_16BE);
                    converterUsesBOM = false;
                }
                return super.getChars(bytes, offset, numBytes);
            }
        }

        /**
         * Utility method for determining if a UTF-16 byte order marker is present.
         */
        private boolean hasUTF16ByteOrderMarker(byte[] array, int offset, int length) {
            if (length >= 4) {
                int b1 = array[offset] & 0x00FF;
                int b2 = array[offset + 1] & 0x00FF;
                char marker = (char) ((b1 << 8) | (b2 << 0));
                return (marker == UTF16_BE_MARKER || marker == UTF16_LE_MARKER);
            } else return false;
        }

        /**
         * The current solution for dealing with UTF-16 in CORBA
         * is that if our sun.io converter requires byte order markers,
         * and then we see a CORBA wstring/wchar without them, we 
         * switch to the sun.io converter that doesn't require them.
         */
        private void switchToConverter(OSFCodeSetRegistry.Entry newCodeSet) {
            btc = super.getConverter(newCodeSet.getName());
        }
    }

    /**
     * CTB converter factory for single byte or variable length encodings.
     */
    public CTBConverter getCTBConverter(OSFCodeSetRegistry.Entry codeset) {
        int alignment = (!codeset.isFixedWidth() ? 1 : codeset.getMaxBytesPerChar());
        return new JavaCTBConverter(codeset, alignment);
    }

    /**
     * CTB converter factory for multibyte (mainly fixed) encodings.
     *
     * Because of the awkwardness with byte order markers and the possibility of 
     * using UCS-2, you must specify both the endianness of the stream as well as 
     * whether or not to use byte order markers if applicable.  UCS-2 has no byte 
     * order markers.  UTF-16 has optional markers.
     *
     * If you select useByteOrderMarkers, there is no guarantee that the encoding
     * will use the endianness specified.
     *
     */
    public CTBConverter getCTBConverter(OSFCodeSetRegistry.Entry codeset, boolean littleEndian, boolean useByteOrderMarkers) {
        if (codeset == OSFCodeSetRegistry.UCS_2) return new UTF16CTBConverter(littleEndian);
        if (codeset == OSFCodeSetRegistry.UTF_16) {
            if (useByteOrderMarkers) return new UTF16CTBConverter(); else return new UTF16CTBConverter(littleEndian);
        }
        int alignment = (!codeset.isFixedWidth() ? 1 : codeset.getMaxBytesPerChar());
        return new JavaCTBConverter(codeset, alignment);
    }

    /**
     * BTCConverter factory for single byte or variable width encodings.
     */
    public BTCConverter getBTCConverter(OSFCodeSetRegistry.Entry codeset) {
        return new JavaBTCConverter(codeset);
    }

    /**
     * BTCConverter factory for fixed width multibyte encodings.
     */
    public BTCConverter getBTCConverter(OSFCodeSetRegistry.Entry codeset, boolean defaultToLittleEndian) {
        if (codeset == OSFCodeSetRegistry.UTF_16 || codeset == OSFCodeSetRegistry.UCS_2) {
            return new UTF16BTCConverter(defaultToLittleEndian);
        } else {
            return new JavaBTCConverter(codeset);
        }
    }

    /** 
     * Follows the code set negotiation algorithm in CORBA formal 99-10-07 13.7.2.
     *
     * Returns the proper negotiated OSF character encoding number or
     * CodeSetConversion.FALLBACK_CODESET.
     */
    private int selectEncoding(CodeSetComponentInfo.CodeSetComponent client, CodeSetComponentInfo.CodeSetComponent server) {
        int serverNative = server.nativeCodeSet;
        if (serverNative == 0) {
            if (server.conversionCodeSets.length > 0) serverNative = server.conversionCodeSets[0]; else return CodeSetConversion.FALLBACK_CODESET;
        }
        if (client.nativeCodeSet == serverNative) {
            return serverNative;
        }
        for (int i = 0; i < client.conversionCodeSets.length; i++) {
            if (serverNative == client.conversionCodeSets[i]) {
                return serverNative;
            }
        }
        for (int i = 0; i < server.conversionCodeSets.length; i++) {
            if (client.nativeCodeSet == server.conversionCodeSets[i]) {
                return client.nativeCodeSet;
            }
        }
        for (int i = 0; i < server.conversionCodeSets.length; i++) {
            for (int y = 0; y < client.conversionCodeSets.length; y++) {
                if (server.conversionCodeSets[i] == client.conversionCodeSets[y]) {
                    return server.conversionCodeSets[i];
                }
            }
        }
        return CodeSetConversion.FALLBACK_CODESET;
    }

    /**
     * Perform the code set negotiation algorithm and come up with
     * the two encodings to use.
     */
    public CodeSetComponentInfo.CodeSetContext negotiate(CodeSetComponentInfo client, CodeSetComponentInfo server) {
        int charData = selectEncoding(client.getCharComponent(), server.getCharComponent());
        if (charData == CodeSetConversion.FALLBACK_CODESET) {
            charData = OSFCodeSetRegistry.UTF_8.getNumber();
        }
        int wcharData = selectEncoding(client.getWCharComponent(), server.getWCharComponent());
        if (wcharData == CodeSetConversion.FALLBACK_CODESET) {
            wcharData = OSFCodeSetRegistry.UTF_16.getNumber();
        }
        return new CodeSetComponentInfo.CodeSetContext(charData, wcharData);
    }

    private CodeSetConversion() {
    }

    private static class CodeSetConversionHolder {

        static final CodeSetConversion csc = new CodeSetConversion();
    }

    /**
     * CodeSetConversion is a singleton, and this is the access point.
     */
    public static final CodeSetConversion impl() {
        return CodeSetConversionHolder.csc;
    }

    private static CodeSetConversion implementation;

    private static final int FALLBACK_CODESET = 0;

    private CodeSetCache cache = new CodeSetCache();
}
