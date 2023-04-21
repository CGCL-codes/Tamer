package server.resources.necessities;

import server.resources.Resource;

public abstract class Necessity extends Resource {

    public Necessity() {
        super();
    }

    public Necessity(double amount) {
        super(amount);
    }
}
