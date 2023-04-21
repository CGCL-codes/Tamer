package sc.model;

import higraph.model.interfaces.Payload;

public class PayloadSC implements Payload<PayloadSC> {

    private String name;

    public PayloadSC(String name) {
        this.name = name;
    }

    public PayloadSC copy() {
        return this;
    }
}
