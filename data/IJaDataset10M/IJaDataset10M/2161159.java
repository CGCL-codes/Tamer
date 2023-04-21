package net.sf.jradius.freeradius;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.sf.jradius.packet.RadiusFormat;
import net.sf.jradius.packet.RadiusPacket;
import net.sf.jradius.packet.attribute.RadiusAttribute;
import net.sf.jradius.packet.attribute.value.AttributeValue;

/**
 * Packs and Unpacks Radius Packets and Attributes for the FreeRADIUS Server.
 *
 * @author David Bird
 */
public class FreeRadiusFormat extends RadiusFormat {

    private static final FreeRadiusFormat staticFormat = new FreeRadiusFormat();

    /**
     * @see net.sf.jradius.packet.RadiusFormat#setAttributeBytes(net.sf.jradius.packet.RadiusPacket, byte[])
     */
    public static void setAttributeBytes(RadiusPacket p, byte[] bAttributes) {
        int attributesLength = bAttributes.length;
        if (attributesLength > 0) {
            staticFormat.unpackAttributes(p.getAttributes(), bAttributes, attributesLength);
        }
    }

    /**
     * @see net.sf.jradius.packet.RadiusFormat#packHeader(java.io.OutputStream, net.sf.jradius.packet.RadiusPacket, byte[])
     */
    public void packHeader(OutputStream out, RadiusPacket p, byte[] attributeBytes) throws IOException {
        writeUnsignedByte(out, p.getCode());
        writeUnsignedByte(out, p.getIdentifier());
        writeUnsignedInt(out, attributeBytes == null ? 0 : attributeBytes.length);
    }

    /**
     * @see net.sf.jradius.packet.RadiusFormat#packHeader(java.io.OutputStream, net.sf.jradius.packet.attribute.RadiusAttribute)
     */
    public void packHeader(OutputStream out, RadiusAttribute a) throws IOException {
        AttributeValue attributeValue = a.getValue();
        writeUnsignedInt(out, a.getFormattedType());
        writeUnsignedInt(out, attributeValue.getLength());
        writeUnsignedInt(out, a.getAttributeOp());
    }

    /**
     * @see net.sf.jradius.packet.RadiusFormat#unpackAttributeHeader(java.io.InputStream, net.sf.jradius.packet.RadiusFormat.AttributeParseContext)
     */
    protected int unpackAttributeHeader(InputStream in, AttributeParseContext ctx) throws IOException {
        ctx.attributeType = (int) readUnsignedInt(in);
        ctx.attributeLength = (int) readUnsignedInt(in);
        ctx.attributeOp = (int) readUnsignedInt(in);
        if (ctx.attributeType > (1 << 16)) {
            ctx.vendorNumber = (ctx.attributeType >> 16) & 0xffff;
            ctx.attributeType &= 0xffff;
        }
        return 12;
    }
}
