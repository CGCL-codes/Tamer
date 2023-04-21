package com.groovytagger.mp3.tags.id3v2.frames.textinfo;

import com.groovytagger.mp3.tags.abstracts.ID3Visitor;
import com.groovytagger.mp3.tags.id3v2.abstracts.ID3V2FrameTextInfo;
import com.groovytagger.mp3.tags.io.TextEncoding;
import java.io.InputStream;

/**
 * @author paul
 *
 * Text frame containing a description of the software and/or hardware settings and/or encoders used to encode
 * the track which is being tagged.
 */
public class TSSETextInformationID3V2Frame extends ID3V2FrameTextInfo {

    private String m_sHardwareSoftwareSettings = null;

    /** Constructor.
   *
   * @param sHardwareSoftwareSettings the hardware and/or software settings and/or encoders used to encode
   *        the track which is being tagged
   */
    public TSSETextInformationID3V2Frame(String sHardwareSoftwareSettings) {
        super(sHardwareSoftwareSettings);
        m_sHardwareSoftwareSettings = sHardwareSoftwareSettings;
    }

    public TSSETextInformationID3V2Frame(InputStream oIS) throws Exception {
        super(oIS);
        m_sHardwareSoftwareSettings = m_sInformation;
    }

    public void accept(ID3Visitor oID3Visitor) {
        oID3Visitor.visitTSSETextInformationID3V2Frame(this);
    }

    /** Set the hardware and/or software settings and/or encoders used to encode the track
   *  which is being tagged.
   *
   * @param sHardwareSoftwareSettings the hardware and/or software settings and/or encoders used to encode
   *        the track which is being tagged
   */
    public void setHardwareSoftwareSettings(String sHardwareSoftwareSettings) {
        m_sHardwareSoftwareSettings = sHardwareSoftwareSettings;
        m_oTextEncoding = TextEncoding.getDefaultTextEncoding();
        m_sInformation = sHardwareSoftwareSettings;
    }

    /** Get the hardware and/or software settings and/or encoders used to encode the track
   *  which was tagged.
   *
   * @return the hardware and/or software settings and/or encoders used to encode this track
   */
    public String getHardwareSoftwareSettings() {
        return m_sHardwareSoftwareSettings;
    }

    public byte[] getFrameId() {
        return "TSSE".getBytes();
    }

    public String toString() {
        return "Software/Hardware and settings used for encoding: [" + m_sInformation + "]";
    }

    public boolean equals(Object oOther) {
        if ((oOther == null) || (!(oOther instanceof TSSETextInformationID3V2Frame))) {
            return false;
        }
        TSSETextInformationID3V2Frame oOtherTSSE = (TSSETextInformationID3V2Frame) oOther;
        return (m_sHardwareSoftwareSettings.equals(oOtherTSSE.m_sHardwareSoftwareSettings) && m_oTextEncoding.equals(oOtherTSSE.m_oTextEncoding) && m_sInformation.equals(oOtherTSSE.m_sInformation));
    }
}
