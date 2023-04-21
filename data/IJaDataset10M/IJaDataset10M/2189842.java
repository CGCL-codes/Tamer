package edu.kds.circuit.types;

import java.io.Serializable;
import edu.kds.circuit.CComponent;
import edu.kds.circuit.Circuit;

public class CompositeTypeCircuit implements Serializable {

    private static final long serialVersionUID = 4712921761942353108L;

    private final Circuit innerCircuit;

    public CompositeTypeCircuit(CComponent innerComponent, Circuit circuit) {
        innerCircuit = circuit;
        innerCircuit.add(innerComponent);
    }

    public Circuit getInnerCircuit() {
        return innerCircuit;
    }
}
