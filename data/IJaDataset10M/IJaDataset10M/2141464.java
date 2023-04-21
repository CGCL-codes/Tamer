package net.sf.doolin.app.sc.game.type;

import java.math.BigDecimal;

public final class DisplayTemp extends BigDecimal {

    public static final Temp REFERENCE = new Temp("22");

    private static final long serialVersionUID = 1L;

    private final Temp reference;

    private final Temp temp;

    public DisplayTemp(Temp reference, Temp temp) {
        super(REFERENCE.add(temp).subtract(reference).toString());
        this.reference = reference;
        this.temp = temp;
    }

    public Temp getReference() {
        return this.reference;
    }

    public Temp getTemp() {
        return this.temp;
    }
}
