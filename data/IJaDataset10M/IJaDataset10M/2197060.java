package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.AbstractFullBox;
import java.nio.ByteBuffer;

/**
 * The copyright box contains a copyright declaration which applies to the entire presentation, when contained
 * within the MovieBox, or, when contained in a track, to that entire track. There may be multple boxes using
 * different language codes.
 *
 * @see MovieBox
 * @see TrackBox
 */
public class CopyrightBox extends AbstractFullBox {

    public static final String TYPE = "cprt";

    private String language;

    private String copyright;

    public CopyrightBox() {
        super(TYPE);
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    protected long getContentSize() {
        return 7 + Utf8.utf8StringLengthInBytes(copyright);
    }

    @Override
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        language = IsoTypeReader.readIso639(content);
        copyright = IsoTypeReader.readString(content);
    }

    @Override
    protected void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeIso639(byteBuffer, language);
        byteBuffer.put(Utf8.convert(copyright));
        byteBuffer.put((byte) 0);
    }

    public String toString() {
        return "CopyrightBox[language=" + getLanguage() + ";copyright=" + getCopyright() + "]";
    }
}
