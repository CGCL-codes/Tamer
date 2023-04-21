package org.riant.simplemms.mm7;

import org._3gpp.ftp.specs.archive._23_series._23_140.schema.rel_6_mm7_1_4.SenderIDType;
import org._3gpp.ftp.specs.archive._23_series._23_140.schema.rel_6_mm7_1_4.AddressType;

/**
 *
 * @author jmills
 */
public class SenderInformation {

    private String m_sourceAddress;

    private String m_vasId;

    private String m_vaspId;

    public void setSourceAddress(String addr) {
        m_sourceAddress = addr;
    }

    public void setVasId(String vas) {
        m_vasId = vas;
    }

    public void setVaspId(String vasp) {
        m_vaspId = vasp;
    }

    public String getSourceAddress() {
        return m_sourceAddress;
    }

    public String getVasId() {
        return m_vasId;
    }

    public String getVaspId() {
        return m_vaspId;
    }

    /**
     * Utility Method which converts the sender information to a generated class type.
     * @return SenderIDType
     */
    public SenderIDType convertToSenderIdType() {
        SenderIDType sid = new SenderIDType();
        AddressType addr = new AddressType();
        AddressType.ShortCode sc = new AddressType.ShortCode();
        sc.setValue(getSourceAddress());
        sc.setDisplayOnly(false);
        addr.setShortCode(sc);
        sid.setSenderAddress(addr);
        sid.setVASID(getVasId());
        sid.setVASPID(getVaspId());
        return sid;
    }
}
