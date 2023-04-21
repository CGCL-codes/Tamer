package org.zoolu.sdp;

import org.zoolu.sdp.*;
import org.zoolu.tools.Parser;
import java.util.Vector;

/** Class SdpParser extends class Parser for parsing of SDP strings.
 */
class SdpParser extends Parser {

    /** Creates a SdpParser based on String <i>s</i> */
    public SdpParser(String strString) {
        super(strString);
    }

    /** Creates a SdpParser based on String <i>s</i> and starting from position <i>i</i> */
    public SdpParser(String strString, int nPosition) {
        super(strString, nPosition);
    }

    /** Returns the first SdpField.
	 *  The SDP value terminates with the end of the String or with the first CR or LF char.
	 *  @return the first SdpField, or null if no SdpField is recognized. */
    public SdpField parseSdpField() {
        int nBegin = m_nIndex;
        while (nBegin >= 0 && nBegin < m_strString.length() - 1 && m_strString.charAt(nBegin + 1) != '=') {
            nBegin = m_strString.indexOf("\n", nBegin);
        }
        if (nBegin < 0) {
            return null;
        }
        char type = m_strString.charAt(nBegin);
        nBegin += 2;
        int end = m_strString.length();
        int CR = m_strString.indexOf('\r', nBegin);
        if (CR > 0 && CR < end) {
            end = CR;
        }
        int LF = m_strString.indexOf('\n', nBegin);
        if (LF > 0 && LF < end) {
            end = LF;
        }
        String value = m_strString.substring(nBegin, end).trim();
        if (value == null) {
            return null;
        }
        setPos(end);
        goToNextLine();
        return new SdpField(type, value);
    }

    /** Returns the first SdpField of type <i>type</i>.
	 *  The SDP value terminates with the end of the String or with the first CR or LF char.
	 *  @return the first SdpField, or null if no <i>type</i> SdpField is found. */
    public SdpField parseSdpField(char type) {
        int begin = 0;
        if (!m_strString.startsWith(type + "=", m_nIndex)) {
            begin = m_strString.indexOf("\n" + type + "=", m_nIndex);
            if (begin < 0) {
                return null;
            }
            m_nIndex = begin + 1;
        }
        return parseSdpField();
    }

    /** Returns the first OriginField.
	 *  @return the first OriginField, or null if no OriginField is found. */
    public OriginField parseOriginField() {
        SdpField sf = parseSdpField('o');
        if (sf != null) {
            return new OriginField(sf);
        } else {
            return null;
        }
    }

    /** Returns the first MediaField.
	 *  @return the first MediaField, or null if no MediaField is found. */
    public MediaField parseMediaField() {
        SdpField sf = parseSdpField('m');
        if (sf != null) {
            return new MediaField(sf);
        } else {
            return null;
        }
    }

    /** Returns the first ConnectionField.
	 *  @return the first ConnectionField, or null if no ConnectionField is found. */
    public ConnectionField parseConnectionField() {
        SdpField sf = parseSdpField('c');
        if (sf != null) {
            return new ConnectionField(sf);
        } else {
            return null;
        }
    }

    /** Returns the first SessionNameField.
	 *  @return the first SessionNameField, or null if no SessionNameField is found. */
    public SessionNameField parseSessionNameField() {
        SdpField sf = parseSdpField('s');
        if (sf != null) {
            return new SessionNameField(sf);
        } else {
            return null;
        }
    }

    /** Returns the first TimeField.
	 *  @return the first TimeField, or null if no TimeField is found. */
    public TimeField parseTimeField() {
        SdpField sf = parseSdpField('t');
        if (sf != null) {
            return new TimeField(sf);
        } else {
            return null;
        }
    }

    /** Returns the first AttributeField.
	 *  @return the first AttributeField, or null if no AttributeField is found. */
    public AttributeField parseAttributeField() {
        SdpField sf = parseSdpField('a');
        if (sf != null) {
            return new AttributeField(sf);
        } else {
            return null;
        }
    }

    /** Returns the first MediaDescriptor.
	 *  @return the first MediaDescriptor, or null if no MediaDescriptor is found. */
    public MediaDescriptor parseMediaDescriptor() {
        MediaField mediaField = parseMediaField();
        if (mediaField == null) return null;
        int begin = m_nIndex;
        int end = m_strString.indexOf("\nm", begin);
        if (end < 0) end = m_strString.length(); else end++;
        m_nIndex = end;
        SdpParser parser = new SdpParser(m_strString.substring(begin, end));
        ConnectionField connectionField = parser.parseConnectionField();
        Vector<AttributeField> vecAtributeField = new Vector<AttributeField>();
        AttributeField attributeField = parser.parseAttributeField();
        while (attributeField != null) {
            vecAtributeField.addElement(attributeField);
            attributeField = parser.parseAttributeField();
        }
        return new MediaDescriptor(mediaField, connectionField, vecAtributeField);
    }
}
