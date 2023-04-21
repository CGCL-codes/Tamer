package com.mscg.jID3tags.objects.frames.contents;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.mscg.jID3tags.util.Costants.PictureType;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2FrameContentImage implements ID3v2FrameContent {

    protected StringEncodingType encoding;

    protected String mimeType;

    protected PictureType pictureType;

    protected String description;

    protected byte pictureData[];

    public ID3v2FrameContentImage() {
    }

    public ID3v2FrameContentImage(StringEncodingType encoding, String mimeType, PictureType pictureType, String description, byte[] pictureData) {
        setEncoding(encoding);
        setMimeType(mimeType);
        setPictureType(pictureType);
        setDescription(description);
        setPictureData(pictureData);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StringEncodingType getEncoding() {
        return encoding;
    }

    public void setEncoding(StringEncodingType encoding) {
        this.encoding = encoding;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    public void setPictureData(InputStream stream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Util.copyStream(stream, bos, 1024);
        bos.flush();
        setPictureData(bos.toByteArray());
    }

    public void setPictureData(File file) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            setPictureData(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void setPictureData(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            setPictureData(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public PictureType getPictureType() {
        return pictureType;
    }

    public void setPictureType(PictureType pictureType) {
        this.pictureType = pictureType;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write(encoding.toByte());
            bos.write(Util.encodeString(getMimeType(), getEncoding()));
            bos.write(0);
            bos.write(getPictureType().toByte());
            bos.write(Util.encodeString(getDescription(), getEncoding()));
            bos.write(0);
            bos.write(getPictureData());
            bos.flush();
        } catch (Exception e) {
            return new byte[0];
        }
        return bos.toByteArray();
    }

    public int getLength() {
        return getBytes().length;
    }

    @Override
    public String toString() {
        return "image mime type: " + getMimeType() + ", image type: " + getPictureType().toString() + ", description: " + getDescription() + ", binary data length: " + getPictureData().length;
    }
}
