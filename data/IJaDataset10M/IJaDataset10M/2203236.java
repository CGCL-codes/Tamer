package org.apache.mina.protocol.dns.messages;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev: 84 $, $Date: 2008-02-22 11:33:48 -0500 (Fri, 22 Feb 2008) $
 */
public class ResourceRecordModifier {

    private String dnsName;

    private RecordType dnsType;

    private RecordClass dnsClass;

    private int dnsTtl;

    private Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * Returns the {@link ResourceRecord} built by this {@link ResourceRecordModifier}.
     *
     * @return The {@link ResourceRecord}.
     */
    public ResourceRecord getEntry() {
        return new ResourceRecordImpl(dnsName, dnsType, dnsClass, dnsTtl, attributes);
    }

    /**
     * @param dnsName The dnsName to set.
     */
    public void setDnsName(String dnsName) {
        this.dnsName = dnsName;
    }

    /**
     * @param dnsType The dnsType to set.
     */
    public void setDnsType(RecordType dnsType) {
        this.dnsType = dnsType;
    }

    /**
     * @param dnsClass The dnsClass to set.
     */
    public void setDnsClass(RecordClass dnsClass) {
        this.dnsClass = dnsClass;
    }

    /**
     * @param dnsTtl The dnsTtl to set.
     */
    public void setDnsTtl(int dnsTtl) {
        this.dnsTtl = dnsTtl;
    }

    /**
     * @param id The id to set
     * @param value The value to set 
     */
    public void put(String id, String value) {
        attributes.put(id.toLowerCase(), value);
    }
}
