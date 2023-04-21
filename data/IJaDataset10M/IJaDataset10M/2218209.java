package protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;

/**
 * A basic packet. A packet can do 4 things: 
 * 1. Initialzed from parameters.
 * 2. Give you the parameters 
 * 3. Initialzed from deserialization buffer.
 * 4. Serialzied
 * 
 * The parameter of the basic packet is it type, and maybe an accosiated
 * connection.
 */
public final class Packet {

    private final Element packet;

    static TransformerFactory transformer = TransformerFactory.newInstance();

    public Element getXmlElement() {
        return packet;
    }

    public Packet(byte[] buffer) throws InvalidPacketException {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ZipInputStream zis = new ZipInputStream(bais);
        ZipEntry entry;
        try {
            entry = zis.getNextEntry();
        } catch (IOException e1) {
            throw new InvalidPacketException("Decompressing failed");
        }
        assert entry.getName() == "data";
        try {
            this.packet = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(zis).getDocumentElement();
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to deserialize xml");
        }
    }

    /**
     * Constructor from parameters. It is protected, as this Packet should not 
     * be created from parameters.
     * @param packetType
     */
    public Packet(final Element packet) {
        this.packet = packet;
    }

    /**
     * @return A serialzede packet.
     * @throws TransformerFactoryConfigurationError 
     * @throws  
     */
    public byte[] toBuffer() throws InvalidPacketException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ZipOutputStream zo = new ZipOutputStream(bo);
        try {
            zo.putNextEntry(new ZipEntry("data"));
        } catch (IOException e1) {
            throw new InvalidPacketException("Compressing failed");
        }
        Source source = new DOMSource(this.packet);
        Result result = new StreamResult(zo);
        Transformer xformer;
        try {
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to serialize xml");
        }
        try {
            zo.close();
        } catch (IOException e) {
            throw new InvalidPacketException("Failed to compress xml");
        }
        return bo.toByteArray();
    }

    @Override
    public String toString() {
        return "XML packet: \n" + packet;
    }
}
