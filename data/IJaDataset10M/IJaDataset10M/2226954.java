package name.schaefer.heiko.twijjer.icons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import name.schaefer.heiko.twijjer.util.Serializable;

/**
 * An ImageData object contains the raw information about a downloaded
 * user icon:
 * 
 * The original representation of the image in an array of bytes
 * (encoded as JPEG or PNG typically)
 *
 * The url the image was retrieved from
 *
 * The resource type, as provided by the providing webserver
 * 
 * This object also sets a "created date" on construction.
 * The Timestamp is intended to be used for cache eviction.
 *
 * @author Heiko Schaefer
 */
class ImageData implements Serializable {

    private final byte[] data;

    private final String type;

    private final String url;

    private Date created;

    ImageData(byte[] data, String type, String url) {
        this.data = data;
        this.type = type;
        this.url = url;
        created = new Date();
    }

    public byte[] getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Date getCreated() {
        return created;
    }

    /**
     * this method is only intended to be used for
     * de-serialization from RecordStore
     *
     * @param created
     */
    void setCreated(Date created) {
        this.created = created;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(getUrl());
        dos.writeUTF(getType());
        dos.writeLong(getCreated().getTime());
        dos.write(getData());
        byte[] b = baos.toByteArray();
        dos.close();
        baos.close();
        return b;
    }

    public static ImageData deserialize(byte[] d) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(d);
        DataInputStream dis = new DataInputStream(bais);
        String url = dis.readUTF();
        String type = dis.readUTF();
        Date created = new Date(dis.readLong());
        byte[] data = new byte[dis.available()];
        dis.readFully(data);
        dis.close();
        bais.close();
        ImageData imageData = new ImageData(data, type, url);
        imageData.setCreated(created);
        return imageData;
    }
}
