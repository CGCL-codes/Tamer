package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.*;
import java.io.*;
import java.util.*;

/**
 *  Module for analysis of content as an ASCII stream.
 */
public class AsciiModule extends ModuleBase {

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/
    private static final String NAME = "ASCII-hul";

    private static final String RELEASE = "1.3";

    private static final int[] DATE = { 2006, 9, 5 };

    private static final String[] FORMAT = { "ASCII", "US-ASCII", "ANSI X3.4", "ISO 646" };

    private static final String COVERAGE = null;

    private static final String[] MIMETYPE = { "text/plain; charset=US-ASCII" };

    private static final String WELLFORMED = "An ASCII object is well-formed " + "if each byte is between 0x00 and 0x7F";

    private static final String VALIDITY = null;

    private static final String REPINFO = "Additional representation information includes: line ending and control characters";

    private static final String NOTE = null;

    private static final String RIGHTS = "Copyright 2003-2007 by JSTOR and " + "the President and Fellows of Harvard College. " + "Released under the GNU Lesser General Public License.";

    private static final int CR = 0x0d;

    private static final int LF = 0x0a;

    private static final String controlCharMnemonics[] = { "NUL (0x00)", "SOH (0x01)", "STX (0x02)", "ETX (0x03)", "EOT (0x04)", "ENQ (0x05)", "ACK (0x06)", "BEL (0x07)", "BS (0x08)", "TAB (0x09)", "LF (0x0A)", "VT (0x0B)", "FF (0x0C)", "CR (0x0D)", "SO (0x0E)", "SI (0x0F)", "DLE (0x10)", "DC1 (0x11)", "DC2 (0x12)", "DC3 (0x13)", "DC4 (0x14)", "NAK (0x15)", "SYN (0x16)", "ETB (0x17)", "CAN (0x18)", "EM (0x19)", "SUB (0x1A)", "ESC (0x1B)", "FS (0x1C)", "GS (0x1D)", "RS (0x1E)", "US (0x1F)" };

    protected ChecksumInputStream _cstream;

    protected DataInputStream _dstream;

    protected boolean _lineEndCR;

    protected boolean _lineEndLF;

    protected boolean _lineEndCRLF;

    protected int _prevChar;

    protected Map _controlCharMap;

    protected boolean _withTextMD = false;

    protected TextMDMetadata _textMD;

    /**
     *  Creates an AsciiModule.
     */
    public AsciiModule() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED, VALIDITY, REPINFO, NOTE, RIGHTS, false);
        Agent agent = new Agent("Harvard University Library", AgentType.EDUCATIONAL);
        agent.setAddress("Office for Information Systems, " + "90 Mt. Auburn St., " + "Cambridge, MA 02138");
        agent.setTelephone("+1 (617) 495-3724");
        agent.setEmail("jhove-support@hulmail.harvard.edu");
        _vendor = agent;
        Document doc = new Document("Information technology -- ISO 7-bit " + "coded character set for information " + "interchange", DocumentType.STANDARD);
        agent = new Agent("ISO", AgentType.STANDARD);
        agent.setAddress("1, rue de Varembe, Casa postale 56, " + "CH-1211, Geneva 20, Switzerland");
        agent.setTelephone("+41 22 749 01 11");
        agent.setFax("+41 22 733 34 30");
        agent.setEmail("iso@iso.ch");
        agent.setWeb("http://www.iso.org/");
        doc.setPublisher(agent);
        doc.setDate("1991");
        doc.setIdentifier(new Identifier("ISO/IEC 646:1991", IdentifierType.ISO));
        _specification.add(doc);
        doc = new Document("Information Systems -- Coded Character Sets " + "7-Bit American National Standard Code for " + "Information Interchange (7-Bit ASCII)", DocumentType.STANDARD);
        agent = new Agent("ANSI", AgentType.STANDARD);
        agent.setAddress("1819 L Street, NW, Washington, DC 20036");
        agent.setTelephone("+1 (202) 293-8020");
        agent.setFax("+1 (202) 293-9287");
        agent.setEmail("info@ansi.org");
        agent.setWeb("http://www.ansi.org/");
        doc.setPublisher(agent);
        doc.setDate("1986-12-30");
        doc.setIdentifier(new Identifier("ANSI X3.4-1986", IdentifierType.ANSI));
        _specification.add(doc);
        doc = new Document("7-Bit coded Character Set", DocumentType.STANDARD);
        doc.setEdition("6th");
        doc.setDate("1991-12");
        agent = new Agent("ECMA", AgentType.STANDARD);
        agent.setAddress("114 Rue du Rhone, CH-1204 Geneva, Switzerland");
        agent.setTelephone("+41 22 849.60.00");
        agent.setFax("+41 22 849.60.01");
        agent.setEmail("helpdesk@ecma.ch");
        agent.setWeb("http://www.ecma-international.org/");
        doc.setPublisher(agent);
        doc.setIdentifier(new Identifier("ECMA-6", IdentifierType.ECMA));
        doc.setIdentifier(new Identifier("http://www.ecma-international." + "org/publications/files/ecma-st/" + "Ecma-006.pdf", IdentifierType.URL));
        _specification.add(doc);
    }

    /**
     *   Parse the content of a stream digital object and store the
     *   results in RepInfo.
     */
    public final int parse(InputStream stream, RepInfo info, int parseIndex) throws IOException {
        if (_defaultParams != null) {
            Iterator iter = _defaultParams.iterator();
            while (iter.hasNext()) {
                String param = (String) iter.next();
                if (param.toLowerCase().equals("withtextmd=true")) {
                    _withTextMD = true;
                }
            }
        }
        initParse();
        info.setModule(this);
        _lineEndCR = false;
        _lineEndLF = false;
        _lineEndCRLF = false;
        _prevChar = 0;
        _controlCharMap = new HashMap();
        _textMD = new TextMDMetadata();
        boolean printableChars = false;
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);
        Checksummer ckSummer = null;
        if (_je != null && _je.getChecksumFlag() && info.getChecksum().size() == 0) {
            ckSummer = new Checksummer();
            _cstream = new ChecksumInputStream(stream, ckSummer);
            _dstream = getBufferedDataStream(_cstream, _je != null ? _je.getBufferSize() : 0);
        } else {
            _dstream = getBufferedDataStream(stream, _je != null ? _je.getBufferSize() : 0);
        }
        boolean eof = false;
        _nByte = 0;
        while (!eof) {
            try {
                int ch = readUnsignedByte(_dstream, this);
                if (ch > 0x7f) {
                    ErrorMessage error = new ErrorMessage("Invalid character", "Character = " + ((char) ch) + " (0x" + Integer.toHexString(ch) + ")", _nByte - 1);
                    info.setMessage(error);
                    info.setWellFormed(RepInfo.FALSE);
                    return 0;
                }
                if (ch < 0X20 && ch != 0X0D && ch != 0X0A) {
                    _controlCharMap.put(new Integer(ch), controlCharMnemonics[ch]);
                } else if (ch == 0X7F) {
                    _controlCharMap.put(new Integer(ch), "DEL (0x7F)");
                }
                checkLineEnd(ch);
                if (0x20 <= ch && ch <= 0x7e) {
                    printableChars = true;
                }
                _prevChar = ch;
            } catch (EOFException e) {
                eof = true;
                checkLineEnd(0);
            }
        }
        if (ckSummer != null) {
            info.setSize(_cstream.getNBytes());
            info.setChecksum(new Checksum(ckSummer.getCRC32(), ChecksumType.CRC32));
            String value = ckSummer.getMD5();
            if (value != null) {
                info.setChecksum(new Checksum(value, ChecksumType.MD5));
            }
            if ((value = ckSummer.getSHA1()) != null) {
                info.setChecksum(new Checksum(value, ChecksumType.SHA1));
            }
        }
        if (_nByte == 0) {
            info.setMessage(new ErrorMessage("Zero-length file"));
            info.setWellFormed(RepInfo.FALSE);
            return 0;
        }
        _textMD.setCharset(TextMDMetadata.CHARSET_ASCII);
        _textMD.setByte_order(_bigEndian ? TextMDMetadata.BYTE_ORDER_BIG : TextMDMetadata.BYTE_ORDER_LITTLE);
        _textMD.setByte_size("8");
        _textMD.setCharacter_size("1");
        List metadataList = new ArrayList(2);
        if (_lineEndCR || _lineEndLF || _lineEndCRLF) {
            ArrayList propArray = new ArrayList(3);
            if (_lineEndCR) {
                propArray.add("CR");
                _textMD.setLinebreak(TextMDMetadata.LINEBREAK_CR);
            }
            if (_lineEndLF) {
                propArray.add("LF");
                _textMD.setLinebreak(TextMDMetadata.LINEBREAK_LF);
            }
            if (_lineEndCRLF) {
                propArray.add("CRLF");
                _textMD.setLinebreak(TextMDMetadata.LINEBREAK_CRLF);
            }
            Property property = new Property("LineEndings", PropertyType.STRING, PropertyArity.LIST, propArray);
            metadataList.add(property);
        }
        if (!_controlCharMap.isEmpty()) {
            LinkedList propList = new LinkedList();
            String mnem;
            for (int i = 0; i < 0X20; i++) {
                mnem = (String) _controlCharMap.get(new Integer(i));
                if (mnem != null) {
                    propList.add(mnem);
                }
            }
            mnem = (String) _controlCharMap.get(new Integer(0X7F));
            if (mnem != null) {
                propList.add(mnem);
            }
            Property property = new Property("ControlCharacters", PropertyType.STRING, PropertyArity.LIST, propList);
            metadataList.add(property);
        }
        if (_withTextMD) {
            Property property = new Property("TextMDMetadata", PropertyType.TEXTMDMETADATA, PropertyArity.SCALAR, _textMD);
            metadataList.add(property);
        }
        if (metadataList.size() > 0) {
            info.setProperty(new Property("ASCIIMetadata", PropertyType.PROPERTY, PropertyArity.LIST, metadataList));
        }
        if (!printableChars) {
            info.setMessage(new InfoMessage("No printable characters"));
        }
        return 0;
    }

    /**
     *  Check if the digital object conforms to this Module's
     *  internal signature information.
     *  An ASCII file has no "signature," so in cases like this we just
     *  check the beginning of the file as a plausible guess. This really
     *  proves nothing, since a text file could have a single accented
     *  character dozens of kilobytes into it. But oh well.
     *
     *   @param file      A File object for the object being parsed
     *   @param stream    An InputStream, positioned at its beginning,
     *                    which is generated from the object to be parsed
     *   @param info      A fresh RepInfo object which will be modified
     *                    to reflect the results of the test
     */
    public void checkSignatures(File file, InputStream stream, RepInfo info) throws IOException {
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);
        info.setModule(this);
        JhoveBase jb = getBase();
        int sigBytes = jb.getSigBytes();
        int bytesRead = 0;
        boolean eof = false;
        DataInputStream dstream = new DataInputStream(stream);
        while (!eof && bytesRead < sigBytes) {
            try {
                int ch = readUnsignedByte(dstream, this);
                ++bytesRead;
                if (ch > 0x7f) {
                    info.setWellFormed(false);
                    return;
                }
            } catch (EOFException e) {
                eof = true;
            }
        }
        if (bytesRead == 0) {
            info.setWellFormed(false);
            return;
        }
        info.setSigMatch(_name);
    }

    protected void checkLineEnd(int ch) {
        if (ch == LF) {
            if (_prevChar == CR) {
                _lineEndCRLF = true;
            } else {
                _lineEndLF = true;
            }
        } else if (_prevChar == CR) {
            _lineEndCR = true;
        }
    }
}
