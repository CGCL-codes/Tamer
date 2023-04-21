package net.sf.katta.ec2;

public class Ec2Instance {

    private final String _privateDnsName;

    private final String _publicDnsName;

    public Ec2Instance(String privateDnsName, String publicdnsName) {
        _privateDnsName = privateDnsName;
        _publicDnsName = publicdnsName;
    }

    public String getInternalHost() {
        return _privateDnsName;
    }

    public String getExternalHost() {
        return _publicDnsName;
    }
}
