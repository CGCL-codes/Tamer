package it.cspnet.jpa.mapping.testsupport;

import it.cspnet.jpa.mapping.Broker;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class BrokerBeta extends Broker {

    @ManyToOne
    protected Beta beta;

    public BrokerBeta() {
        super();
    }

    public Beta getBeta() {
        return beta;
    }
}
