package org.apache.mina.protocol.dns.io.encoder;

/**
 * 3.1.1.  Format
 * 
 *    The RDATA portion of the A6 record contains two or three fields.
 * 
 * <pre>
 *            +-----------+------------------+-------------------+
 *            |Prefix len.|  Address suffix  |    Prefix name    |
 *            | (1 octet) |  (0..16 octets)  |  (0..255 octets)  |
 *            +-----------+------------------+-------------------+
 * </pre>
 * <ul>
 *    <li>  A prefix length, encoded as an eight-bit unsigned integer with
 *       value between 0 and 128 inclusive.</li>
 * 
 *    <li>  An IPv6 address suffix, encoded in network order (high-order octet
 *       first).  There MUST be exactly enough octets in this field to
 *       contain a number of bits equal to 128 minus prefix length, with 0
 *       to 7 leading pad bits to make this field an integral number of
 *       octets.  Pad bits, if present, MUST be set to zero when loading a
 *       zone file and ignored (other than for SIG [DNSSEC] verification)
 *       on reception. </li>
 * 
 *    <li>  The name of the prefix, encoded as a domain name.  By the rules of
 *       [DNSIS], this name MUST NOT be compressed.</li>
 * </ul>
 * 
 *    The domain name component SHALL NOT be present if the prefix length
 *    is zero.  The address suffix component SHALL NOT be present if the
 *    prefix length is 128.
 * 
 *    It is SUGGESTED that an A6 record intended for use as a prefix for
 *    other A6 records have all the insignificant trailing bits in its
 *    address suffix field set to zero.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev: 84 $, $Date: 2008-02-22 11:33:48 -0500 (Fri, 22 Feb 2008) $
 */
public class A6RecordEncoder {
}
