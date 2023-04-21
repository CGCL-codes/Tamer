package cai.flow.struct;

public class Scheme_DataNode extends Scheme_Data {

    public String ip;

    public Scheme_DataNode(String RouterIP, long Flows, long Missed, long dPkts, long dOctets, String ip) {
        super(RouterIP, Flows, Missed, dPkts, dOctets);
        this.ip = ip;
    }
}
