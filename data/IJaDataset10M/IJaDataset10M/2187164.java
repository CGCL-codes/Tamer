package com.entelience.objects.net;

/**
 * Bean for IANA Protocol Port (see schema net/e_iana_* tables)
 */
public class SimpleNetPort implements java.io.Serializable {

    protected Integer protocolId;

    protected Integer portId;

    protected Integer portNumber;

    public SimpleNetPort() {
    }

    public SimpleNetPort(Integer _portId, Integer _protocolId, Integer _portNumber) {
        protocolId = _protocolId;
        portId = _portId;
        portNumber = _portNumber;
    }

    public void setProtocolId(Integer _protocolId) {
        protocolId = _protocolId;
    }

    public Integer getProtocolId() {
        return protocolId;
    }

    public void setPortId(Integer _portId) {
        portId = _portId;
    }

    public void setPortNumber(Integer _portNumber) {
        portNumber = _portNumber;
    }

    public Integer getPortId() {
        return portId;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof com.entelience.objects.net.SimpleNetPort) {
            SimpleNetPort snp = (SimpleNetPort) o;
            if (((protocolId == null && snp.protocolId == null) || (protocolId != null && protocolId.compareTo(snp.protocolId) == 0)) && ((portId == null && snp.portId == null) || (portId != null && portId.compareTo(snp.portId) == 0)) && ((portNumber == null && snp.portNumber == null) || (portNumber != null && portNumber.compareTo(snp.portNumber) == 0))) return true;
        }
        return false;
    }
}
