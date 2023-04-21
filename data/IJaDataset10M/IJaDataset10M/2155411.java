package com.framedobjects.dashwell.biz;

import wsl.mdn.mdnmsgsvr.MdnSmpp;
import ie.omk.smpp.Connection;

public class MdnSmppServerObject {

    private int id;

    private String number;

    private String host;

    private String port;

    private String username;

    private String password;

    private String sourceNpi;

    private String sourceTon;

    private String destNpi;

    private String destTon;

    private String bindNpi;

    private String bindTon;

    private String type;

    private int useTlv;

    private int useAddrRange;

    private int interval;

    private long startTime;

    private Connection smppConnection;

    public MdnSmppServerObject(int id, String number, String host, String port, String username, String password, String sourceNpi, String sourceTon, String destNpi, String destTon, String bindNpi, String bindTon, String type, int useTlv, int interval, Connection smppConnection, int useAddrRange) {
        this.id = id;
        this.number = number;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.sourceNpi = sourceNpi;
        this.sourceTon = sourceTon;
        this.destNpi = destNpi;
        this.destTon = destTon;
        this.bindNpi = bindNpi;
        this.bindTon = bindTon;
        this.type = type;
        this.useTlv = useTlv;
        this.useAddrRange = useAddrRange;
        this.interval = interval;
        this.smppConnection = smppConnection;
    }

    public MdnSmpp getMdnSmsGateway() {
        MdnSmpp smsGatewaySetting = new MdnSmpp();
        smsGatewaySetting.setId(id);
        smsGatewaySetting.setNumber(number);
        smsGatewaySetting.setHost(host);
        smsGatewaySetting.setPort(port);
        smsGatewaySetting.setUsername(username);
        smsGatewaySetting.setPassword(password);
        smsGatewaySetting.setSourceNPI(sourceNpi);
        smsGatewaySetting.setSourceTON(sourceTon);
        smsGatewaySetting.setDestNPI(destNpi);
        smsGatewaySetting.setDestTON(destTon);
        smsGatewaySetting.setDestNPI(bindNpi);
        smsGatewaySetting.setDestTON(bindTon);
        smsGatewaySetting.setType(type);
        smsGatewaySetting.setUseTlv(useTlv);
        smsGatewaySetting.setUseTlv(useAddrRange);
        smsGatewaySetting.setInterval(interval);
        return smsGatewaySetting;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSourceNPI() {
        return sourceNpi;
    }

    public void setSourceNPI(String sourceNpi) {
        this.sourceNpi = sourceNpi;
    }

    public String getSourceTON() {
        return sourceTon;
    }

    public void setSourceTON(String sourceTon) {
        this.sourceTon = sourceTon;
    }

    public String getDestNPI() {
        return destNpi;
    }

    public void setDestNPI(String destNpi) {
        this.destNpi = destNpi;
    }

    public String getDestTON() {
        return destTon;
    }

    public void setDestTON(String destTon) {
        this.destTon = destTon;
    }

    public String getBindNPI() {
        return bindNpi;
    }

    public void setBindNPI(String bindNpi) {
        this.bindNpi = bindNpi;
    }

    public String getBindTON() {
        return bindTon;
    }

    public void setBindTON(String bindTon) {
        this.bindTon = bindTon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        ;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getUseTlv() {
        return useTlv;
    }

    public void setUseTlv(int useTlv) {
        this.useTlv = useTlv;
    }

    public int getUseAddrRange() {
        return useAddrRange;
    }

    public void setUseAddrRange(int useAddrRange) {
        this.useAddrRange = useAddrRange;
    }

    public Connection getSmppConnection() {
        return smppConnection;
    }

    public void setSmppConnection(Connection smppConnection) {
        this.smppConnection = smppConnection;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
