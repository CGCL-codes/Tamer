package org.apache.harmony.jndi.provider.dns;

import java.util.StringTokenizer;
import org.apache.harmony.jndi.internal.nls.Messages;

/**
 * Represents domain protocol Resource Record
 * 
 * @see RFC 1035
 */
public class ResourceRecord {

    /** a domain name */
    private String name;

    /** resource record type */
    private int rrType;

    /** resource record class */
    private int rrClass;

    /** time to live */
    private long ttl;

    /** resource data itself */
    private Object rData;

    /** empty constructor */
    public ResourceRecord() {
    }

    /**
     * Constructs new ResourceRecord object from given values.
     * 
     * @param name
     *            a domain name
     * @param rrType
     *            resource record type
     * @param rrClass
     *            resource record class
     * @param ttl
     *            time to live
     * @param rdLength
     *            resource data length
     * @param rData
     *            resource data itself
     */
    public ResourceRecord(String name, int rrType, int rrClass, long ttl, Object rData) {
        this.name = name;
        this.rrType = rrType;
        this.rrClass = rrClass;
        this.ttl = ttl;
        this.rData = rData;
    }

    /**
     * Creates the sequence of bytes that represents the current resource
     * record.
     * 
     * @param buffer
     *            the buffer to write the bytes into
     * @param startIdx
     *            starting index
     * @return updated index
     * @throws DomainProtocolException
     *             if something went wrong
     * @throws ArrayIndexOutOfBoundsException
     *             if the buffer border unpredictably encountered
     */
    public int writeBytes(byte[] buffer, int startIdx) throws DomainProtocolException {
        int idx = startIdx;
        if (buffer == null) {
            throw new DomainProtocolException(Messages.getString("jndi.32"));
        }
        idx = ProviderMgr.writeName(name, buffer, idx);
        idx = ProviderMgr.write16Int(rrType, buffer, idx);
        idx = ProviderMgr.write16Int(rrClass, buffer, idx);
        idx = ProviderMgr.write32Int(ttl, buffer, idx);
        if (rrType == ProviderConstants.NS_TYPE || rrType == ProviderConstants.CNAME_TYPE || rrType == ProviderConstants.PTR_TYPE) {
            int idx0 = idx;
            idx += 2;
            idx = ProviderMgr.writeName((String) rData, buffer, idx);
            ProviderMgr.write16Int(idx - 2 - idx0, buffer, idx0);
        } else if (rrType == ProviderConstants.A_TYPE) {
            byte[] ipBytes = ProviderMgr.parseIpStr((String) rData);
            idx = ProviderMgr.write16Int(ipBytes.length, buffer, idx);
            for (byte element : ipBytes) {
                buffer[idx++] = element;
            }
        } else if (rrType == ProviderConstants.SOA_TYPE) {
            StringTokenizer st = new StringTokenizer((String) rData, " ");
            String token;
            int idx0 = idx;
            if (st.countTokens() != 7) {
                throw new DomainProtocolException(Messages.getString("jndi.35"));
            }
            idx += 2;
            token = st.nextToken();
            idx = ProviderMgr.writeName(token, buffer, idx);
            token = st.nextToken();
            idx = ProviderMgr.writeName(token, buffer, idx);
            try {
                for (int i = 0; i < 5; i++) {
                    token = st.nextToken();
                    idx = ProviderMgr.write32Int(Long.parseLong(token), buffer, idx);
                }
            } catch (NumberFormatException e) {
                throw new DomainProtocolException(Messages.getString("jndi.36"), e);
            }
            ProviderMgr.write16Int(idx - 2 - idx0, buffer, idx0);
        } else if (rrType == ProviderConstants.MX_TYPE) {
            StringTokenizer st = new StringTokenizer((String) rData, " ");
            String token;
            int idx0 = idx;
            if (st.countTokens() != 2) {
                throw new DomainProtocolException(Messages.getString("jndi.37"));
            }
            idx += 2;
            token = st.nextToken();
            try {
                ProviderMgr.write16Int(Integer.parseInt(token), buffer, idx);
            } catch (NumberFormatException e) {
                throw new DomainProtocolException(Messages.getString("jndi.38"), e);
            }
            token = st.nextToken();
            idx = ProviderMgr.writeName(token, buffer, idx);
            ProviderMgr.write16Int(idx - 2 - idx0, buffer, idx0);
        } else if (rrType == ProviderConstants.HINFO_TYPE) {
            StringTokenizer st = new StringTokenizer((String) rData, " ");
            String token;
            int idx0 = idx;
            if (st.countTokens() != 2) {
                throw new DomainProtocolException(Messages.getString("jndi.39"));
            }
            idx += 2;
            for (int i = 0; i < 2; i++) {
                token = st.nextToken();
                idx = ProviderMgr.writeCharString(token, buffer, idx);
            }
            ProviderMgr.write16Int(idx - 2 - idx0, buffer, idx0);
        } else if (rrType == ProviderConstants.TXT_TYPE) {
            int idx0 = idx;
            StringTokenizer st = new StringTokenizer((String) rData, " ");
            idx += 2;
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.getBytes().length > 255) {
                    throw new DomainProtocolException(Messages.getString("jndi.3A"));
                }
                idx = ProviderMgr.writeCharString(token, buffer, idx);
            }
            if (idx - 2 - idx0 > 65535) {
                throw new DomainProtocolException(Messages.getString("jndi.3B"));
            }
            ProviderMgr.write16Int(idx - 2 - idx0, buffer, idx0);
        } else if (rrType == ProviderConstants.SRV_TYPE) {
            StringTokenizer st = new StringTokenizer((String) rData, " ");
            String token;
            int idx0 = idx;
            idx += 2;
            if (st.countTokens() != 4) {
                throw new DomainProtocolException(Messages.getString("jndi.3C"));
            }
            try {
                for (int i = 0; i < 3; i++) {
                    token = st.nextToken();
                    idx = ProviderMgr.write16Int(Integer.parseInt(token), buffer, idx);
                }
            } catch (NumberFormatException e) {
                throw new DomainProtocolException(Messages.getString("jndi.3D"), e);
            }
            token = st.nextToken();
            idx = ProviderMgr.writeName(token, buffer, idx);
            ProviderMgr.write16Int(idx - 2 - idx0, buffer, idx0);
        } else {
            byte[] bytes;
            if (!(rData instanceof byte[])) {
                throw new DomainProtocolException(Messages.getString("jndi.3E", rrType));
            }
            bytes = (byte[]) rData;
            idx = ProviderMgr.write16Int(bytes.length, buffer, idx);
            for (byte element : bytes) {
                buffer[idx++] = element;
            }
        }
        return idx;
    }

    /**
     * Parses given sequence of bytes and constructs a resource record from it.
     * 
     * @param mesBytes
     *            the byte array that should be parsed
     * @param startIdx
     *            an index of <code>mesBytes</code> array to start the parsing
     *            at
     * @param resultRR
     *            an object the result of the operation will be stored into
     * @return updated index of <code>mesBytes</code> array
     * @throws DomainProtocolException
     *             if something went wrong
     * @throws ArrayIndexOutOfBoundsException
     *             if the array border unpredictably encountered
     */
    public static int parseRecord(byte[] mesBytes, int startIdx, ResourceRecord resultRR) throws DomainProtocolException {
        int idx = startIdx;
        StringBuffer nameSB = new StringBuffer();
        int rrType;
        int rdLen;
        Object rDat = null;
        if (resultRR == null) {
            throw new NullPointerException(Messages.getString("jndi.3F"));
        }
        idx = ProviderMgr.parseName(mesBytes, idx, nameSB);
        resultRR.setName(ProviderMgr.normalizeName(nameSB.toString()));
        rrType = ProviderMgr.parse16Int(mesBytes, idx);
        resultRR.setRRType(rrType);
        idx += 2;
        resultRR.setRRClass(ProviderMgr.parse16Int(mesBytes, idx));
        idx += 2;
        resultRR.setTtl(ProviderMgr.parse32Int(mesBytes, idx));
        idx += 4;
        rdLen = ProviderMgr.parse16Int(mesBytes, idx);
        idx += 2;
        if (rrType == ProviderConstants.NS_TYPE || rrType == ProviderConstants.CNAME_TYPE || rrType == ProviderConstants.PTR_TYPE) {
            StringBuffer name = new StringBuffer();
            idx = ProviderMgr.parseName(mesBytes, idx, name);
            rDat = ProviderMgr.normalizeName(name.toString());
        } else if (rrType == ProviderConstants.A_TYPE) {
            byte tmpArr[] = new byte[4];
            for (int i = 0; i < 4; i++) {
                tmpArr[i] = mesBytes[idx + i];
            }
            rDat = ProviderMgr.getIpStr(tmpArr);
            idx += 4;
        } else if (rrType == ProviderConstants.MX_TYPE) {
            int preference;
            StringBuffer name = new StringBuffer();
            preference = ProviderMgr.parse16Int(mesBytes, idx);
            idx += 2;
            idx = ProviderMgr.parseName(mesBytes, idx, name);
            rDat = "" + preference + " " + ProviderMgr.normalizeName(name.toString());
        } else if (rrType == ProviderConstants.SOA_TYPE) {
            StringBuffer mName = new StringBuffer();
            StringBuffer rName = new StringBuffer();
            long serial;
            long refresh;
            long retry;
            long expire;
            long minimum;
            idx = ProviderMgr.parseName(mesBytes, idx, mName);
            idx = ProviderMgr.parseName(mesBytes, idx, rName);
            serial = ProviderMgr.parse32Int(mesBytes, idx);
            idx += 4;
            refresh = ProviderMgr.parse32Int(mesBytes, idx);
            idx += 4;
            retry = ProviderMgr.parse32Int(mesBytes, idx);
            idx += 4;
            expire = ProviderMgr.parse32Int(mesBytes, idx);
            idx += 4;
            minimum = ProviderMgr.parse32Int(mesBytes, idx);
            idx += 4;
            rDat = ProviderMgr.normalizeName(mName.toString()) + " " + ProviderMgr.normalizeName(rName.toString()) + " " + serial + " " + refresh + " " + retry + " " + expire + " " + minimum;
        } else if (rrType == ProviderConstants.TXT_TYPE) {
            StringBuilder sbuf = new StringBuilder();
            int idx0 = idx;
            while (true) {
                int len11 = ProviderMgr.parse8Int(mesBytes, idx++);
                if (idx - idx0 + len11 > rdLen) {
                    idx--;
                    break;
                }
                if (sbuf.length() > 0) {
                    sbuf.append(' ');
                }
                sbuf.append(new String(mesBytes, idx, len11));
                idx += len11;
            }
            rDat = sbuf.toString();
        } else if (rrType == ProviderConstants.HINFO_TYPE) {
            StringBuffer res = new StringBuffer();
            idx = ProviderMgr.parseCharString(mesBytes, idx, res);
            res.append(" ");
            idx = ProviderMgr.parseCharString(mesBytes, idx, res);
            rDat = res.toString();
        } else if (rrType == ProviderConstants.SRV_TYPE) {
            int priority;
            int weight;
            int port;
            StringBuffer name = new StringBuffer();
            priority = ProviderMgr.parse16Int(mesBytes, idx);
            idx += 2;
            weight = ProviderMgr.parse16Int(mesBytes, idx);
            idx += 2;
            port = ProviderMgr.parse16Int(mesBytes, idx);
            idx += 2;
            idx = ProviderMgr.parseName(mesBytes, idx, name);
            rDat = "" + priority + " " + weight + " " + port + " " + ProviderMgr.normalizeName(name.toString());
        } else {
            rDat = new byte[rdLen];
            for (int i = 0; i < rdLen; i++) {
                ((byte[]) rDat)[i] = mesBytes[idx++];
            }
        }
        resultRR.setRData(rDat);
        return idx;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" ");
        sb.append(ProviderConstants.rrTypeNames[rrType]);
        sb.append(" ");
        sb.append(rrClass);
        sb.append(" ");
        sb.append("TTL=" + ttl);
        sb.append(" ");
        sb.append(rData.toString());
        return sb.toString();
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the rData.
     */
    public Object getRData() {
        return rData;
    }

    /**
     * @param data
     *            The rData to set.
     */
    public void setRData(Object data) {
        rData = data;
    }

    /**
     * @return Returns the rrClass.
     */
    public int getRRClass() {
        return rrClass;
    }

    /**
     * @param rrClass
     *            The rrClass to set.
     */
    public void setRRClass(int rrClass) {
        this.rrClass = rrClass;
    }

    /**
     * @return Returns the rrType.
     */
    public int getRRType() {
        return rrType;
    }

    /**
     * @param rrType
     *            The rrType to set.
     */
    public void setRRType(int rrType) {
        this.rrType = rrType;
    }

    /**
     * @return Returns the TTL.
     */
    public long getTtl() {
        return ttl;
    }

    /**
     * @param ttl
     *            The TTL to set.
     */
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
