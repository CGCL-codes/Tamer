package cai.flow.struct;

public class Scheme_DataInterfaceMatrix extends Scheme_Data {

    public long input, output;

    public Scheme_DataInterfaceMatrix(String RouterIP, long Flows, long Missed, long dPkts, long dOctets, long input, long output) {
        super(RouterIP, Flows, Missed, dPkts, dOctets);
        this.input = input;
        this.output = output;
    }
}
