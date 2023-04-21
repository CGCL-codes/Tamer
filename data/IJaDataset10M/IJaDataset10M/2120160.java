package com.groovytagger.mp3.tags.id3v2.frames.textinfo;

import com.groovytagger.mp3.tags.abstracts.ID3Visitor;
import com.groovytagger.mp3.tags.id3v2.abstracts.ID3V2FrameTextInfo;
import com.groovytagger.mp3.tags.io.TextEncoding;
import java.io.InputStream;

/**
 * @author paul
 *
 * Text frame containing the subtitle, or a description refinement, for the track.
 */
public class TIT3TextInformationID3V2Frame extends ID3V2FrameTextInfo {

    private String m_sSubtitle = null;

    /** Constructor.
   *
   * @param sSubtitle the subtitle, or a description refinement, for the track
   */
    public TIT3TextInformationID3V2Frame(String sSubtitle) {
        super(sSubtitle);
        m_sSubtitle = sSubtitle;
    }

    public TIT3TextInformationID3V2Frame(InputStream oIS) throws Exception {
        super(oIS);
        m_sSubtitle = m_sInformation;
    }

    public void accept(ID3Visitor oID3Visitor) {
        oID3Visitor.visitTIT3TextInformationID3V2Frame(this);
    }

    /** Set the subtitle, or a description refinement, for the track.
   *
   * @param sSubtitle the subtitle, or a description refinement, for the track
   */
    public void setSubtitle(String sSubtitle) {
        m_sSubtitle = sSubtitle;
        m_oTextEncoding = TextEncoding.getDefaultTextEncoding();
        m_sInformation = sSubtitle;
    }

    /** Get the subtitle, or description refinement, for the track.
   *
   * @return the subtitle or description refinement
   */
    public String getSubtitle() {
        return m_sSubtitle;
    }

    public byte[] getFrameId() {
        return "TIT3".getBytes();
    }

    public String toString() {
        return "Subtitle/Description refinement: [" + m_sInformation + "]";
    }

    public boolean equals(Object oOther) {
        if ((oOther == null) || (!(oOther instanceof TIT3TextInformationID3V2Frame))) {
            return false;
        }
        TIT3TextInformationID3V2Frame oOtherTIT3 = (TIT3TextInformationID3V2Frame) oOther;
        return (m_sSubtitle.equals(oOtherTIT3.m_sSubtitle) && m_oTextEncoding.equals(oOtherTIT3.m_oTextEncoding) && m_sInformation.equals(oOtherTIT3.m_sInformation));
    }
}
