package com.groovytagger.mp3.tags.id3v2.frames.textinfo;

import com.groovytagger.mp3.tags.abstracts.ID3Visitor;
import com.groovytagger.mp3.tags.id3v2.abstracts.ID3V2FrameTextInfo;
import com.groovytagger.mp3.tags.io.TextEncoding;
import java.io.InputStream;

/**
 * @author paul
 *
 * Text frame containing a free-form description of the recording date(s) of this track.
 */
public class TRDATextInformationID3V2Frame extends ID3V2FrameTextInfo {

    private String m_sRecordingDates = null;

    /** Constructor.
   *
   * @param sRecordingDates the recording date(s) of this track
   */
    public TRDATextInformationID3V2Frame(String sRecordingDates) {
        super(sRecordingDates);
        m_sRecordingDates = sRecordingDates;
    }

    public TRDATextInformationID3V2Frame(InputStream oIS) throws Exception {
        super(oIS);
        m_sRecordingDates = m_sInformation;
    }

    public void accept(ID3Visitor oID3Visitor) {
        oID3Visitor.visitTRDATextInformationID3V2Frame(this);
    }

    /** Set the recording date(s) of this track.
   *
   * @param sRecordingDates the recording date(s) of this track
   */
    public void setRecordingDates(String sRecordingDates) {
        m_sRecordingDates = sRecordingDates;
        m_oTextEncoding = TextEncoding.getDefaultTextEncoding();
        m_sInformation = sRecordingDates;
    }

    /** Get the recording date(s) of this track.
   *
   * @return the recording date(s) of this track
   */
    public String getRecordingDates() {
        return m_sRecordingDates;
    }

    public byte[] getFrameId() {
        return "TRDA".getBytes();
    }

    public String toString() {
        return "Recording dates: [" + m_sInformation + "]";
    }

    public boolean equals(Object oOther) {
        if ((oOther == null) || (!(oOther instanceof TRDATextInformationID3V2Frame))) {
            return false;
        }
        TRDATextInformationID3V2Frame oOtherTRDA = (TRDATextInformationID3V2Frame) oOther;
        return (m_sRecordingDates.equals(oOtherTRDA.m_sRecordingDates) && m_oTextEncoding.equals(oOtherTRDA.m_oTextEncoding) && m_sInformation.equals(oOtherTRDA.m_sInformation));
    }
}
