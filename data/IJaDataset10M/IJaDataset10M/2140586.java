package net.java.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the MIP6-Agent-Info grouped AVP type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.45  MIP6-Agent-Info
 * 
 * The MIP6-Agent-InfoAVP is of type Grouped and is defined in IETF RFC 5447 [26]. This AVP shall
 * contain the identity of the PDN-GW. This AVP is used to convey the identity of the PDN-GW 
 * between the MME/SGSN and the HSS regardless of the specific mobility protocol used (GTP or PMIPv6). 
 * The identity of PDN-GW is either an IP address transported in MIP-Home-Agent-Address or an FQDN 
 * transported in MIP-Home-Agent-Host. FQDN shall be used if known to the MME/SGSN/HSS.
 * 
 * AVP format
 * MIP6-Agent-Info ::=  < AVP Header: 486 >
 *                    *2[ MIP-Home-Agent-Address ]
 *                      [ MIP-Home-Agent-Host ]
 *                      [ MIP6-Home-Link-Prefix ]
 *                     *[ AVP ]
 * 
 * Within the MIP6-Agent-Info AVP, if static address allocation is used, there may be either:
 * - an IPv4 address or an IPv6 address of the PGW contained in one MIP-Home-Agent-Address AVP;
 * - both IPv4 address and IPv6 address of the PGW contained in two MIP-Home-Agent-Address AVPs.
 * 
 * The AVP MIP6-Home-Link-Prefix is not used in S6a/S6d, but it is included here to reflect the
 * complete IETF definition of the grouped AVP.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface MIP6AgentInfoAvp extends GroupedAvp {

    public boolean hasMIPHomeAgentAddress();

    public Address getMIPHomeAgentAddress();

    public void setMIPHomeAgentAddress(Address address);

    public boolean hasMIPHomeAgentHost();

    public MIPHomeAgentHostAvp getMIPHomeAgentHost();

    public void setMIPHomeAgentHost(MIPHomeAgentHostAvp hah);

    public boolean hasMIP6HomeLinkPrefix();

    public byte[] getMIP6HomeLinkPrefix();

    public void setMIP6HomeLinkPrefix(byte[] prefix);
}
