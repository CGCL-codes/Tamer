package com.groovytagger.mp3.tags.id3v2.frames.textinfo;

import com.groovytagger.mp3.tags.abstracts.ID3Visitor;
import com.groovytagger.mp3.tags.id3v2.abstracts.ID3V2FrameTextInfo;
import com.groovytagger.mp3.tags.io.TextEncoding;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author paul
 *
 * Text frame containing the original artist(s) or performer(s) of the original recording in this track.
 */
public class TOPETextInformationID3V2Frame extends ID3V2FrameTextInfo {

    private String[] m_asOriginalPerformer = null;

    /** Constructor.
   *
   * @param sOriginalPerformer the original artist or performer for this track
   */
    public TOPETextInformationID3V2Frame(String sOriginalPerformer) {
        super(sOriginalPerformer);
        m_asOriginalPerformer = getPerformers(sOriginalPerformer);
    }

    /** Constructor.
   *
   * @param asOriginalPerformer the original artist(s) or performer(s) for this track
   */
    public TOPETextInformationID3V2Frame(String[] asOriginalPerformer) {
        super("");
        StringBuffer sbPerformers = new StringBuffer();
        for (int i = 0; i < asOriginalPerformer.length; i++) {
            sbPerformers.append(asOriginalPerformer[i] + "/");
        }
        sbPerformers.deleteCharAt(sbPerformers.length() - 1);
        m_sInformation = sbPerformers.toString();
        m_asOriginalPerformer = getPerformers(m_sInformation);
    }

    public TOPETextInformationID3V2Frame(InputStream oIS) throws Exception {
        super(oIS);
        m_asOriginalPerformer = getPerformers(m_sInformation);
    }

    public void accept(ID3Visitor oID3Visitor) {
        oID3Visitor.visitTOPETextInformationID3V2Frame(this);
    }

    /** Set the original author(s) or performer(s) for the recording in this track.
   *  Multiple performers can optionally be set with this method by separating them
   *  with a slash "/" character.
   *
   * @param sOriginalPerformer the original author or performer for this track
   */
    public void setOriginalPerformer(String sOriginalPerformer) {
        m_oTextEncoding = TextEncoding.getDefaultTextEncoding();
        m_sInformation = sOriginalPerformer;
        m_asOriginalPerformer = getPerformers(sOriginalPerformer);
    }

    /** Set the original author(s) or performer(s) for the recording in this track.
   *
   * @param asOriginalPerformer the original author(s) or performer(s) for this track
   */
    public void setOriginalPerformers(String[] asOriginalPerformer) {
        StringBuffer sbPerformers = new StringBuffer();
        for (int i = 0; i < asOriginalPerformer.length; i++) {
            sbPerformers.append(asOriginalPerformer[i] + "/");
        }
        sbPerformers.deleteCharAt(sbPerformers.length() - 1);
        m_oTextEncoding = TextEncoding.getDefaultTextEncoding();
        m_sInformation = sbPerformers.toString();
        m_asOriginalPerformer = getPerformers(m_sInformation);
    }

    /** Get the original author(s) or performer(s) for this track
   *
   * @return the original author(s) or performer(s) for this track
   */
    public String[] getOriginalPerformers() {
        return m_asOriginalPerformer;
    }

    public byte[] getFrameId() {
        return "TOPE".getBytes();
    }

    public String toString() {
        return "Original artist(s)/performer(s): [" + m_sInformation + "]";
    }

    /** Split a string containing potentially several distinct values (forward-slash separated) into
   *  an array of Strings, one value per String.
   *
   * @param sValue value to be separated
   * @return an array of values
   */
    private String[] getPerformers(String sValue) {
        String[] asPerformer = sValue.split("/");
        return asPerformer;
    }

    public boolean equals(Object oOther) {
        if ((oOther == null) || (!(oOther instanceof TOPETextInformationID3V2Frame))) {
            return false;
        }
        TOPETextInformationID3V2Frame oOtherTOPE = (TOPETextInformationID3V2Frame) oOther;
        return (m_sInformation.equals(oOtherTOPE.m_sInformation) && m_oTextEncoding.equals(oOtherTOPE.m_oTextEncoding) && Arrays.equals(m_asOriginalPerformer, oOtherTOPE.m_asOriginalPerformer));
    }
}
