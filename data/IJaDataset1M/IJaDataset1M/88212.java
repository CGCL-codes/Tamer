package rtjdds.rtps.messages;

import rtjdds.rtps.messages.elements.GuidPrefix;
import rtjdds.rtps.messages.elements.ProtocolVersion;
import rtjdds.rtps.messages.elements.VendorId;
import rtjdds.rtps.portable.CDROutputPacket;
import rtjdds.rtps.types.INFO_SRC;

public class InfoSource extends Submessage {

    protected ProtocolVersion version;

    protected VendorId vendor;

    protected GuidPrefix guidPrefix;

    public InfoSource(ProtocolVersion version, VendorId vendor, GuidPrefix guidPrefix) {
        super(INFO_SRC.value);
        this.version = version;
        this.vendor = vendor;
        this.guidPrefix = guidPrefix;
    }

    protected void writeBody(CDROutputPacket os) {
        os.write_long(0);
        this.version.write(os);
        this.vendor.write(os);
        this.guidPrefix.write(os);
    }
}
