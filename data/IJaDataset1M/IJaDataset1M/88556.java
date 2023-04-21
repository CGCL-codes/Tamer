package net.sf.doolin.app.sc.game.dist;

import java.util.concurrent.atomic.AtomicInteger;
import net.sf.doolin.app.sc.game.type.Position;

public abstract class AbstractSpatialDistribution implements SpatialDistribution {

    private static final long serialVersionUID = 1L;

    private double range = 1000;

    private double spacing = 4;

    private double pertubation = 2;

    private double margin = 0.05;

    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final int id;

    private final StandardDistribution pertubationDistribution = new StandardDistribution(-2, 2, 2);

    public AbstractSpatialDistribution() {
        this.id = idGenerator.incrementAndGet();
    }

    protected Position adjustForSpacing(Position p) {
        double x = Math.round(p.x) * this.spacing;
        double y = Math.round(p.y) * this.spacing;
        return new Position(x, y);
    }

    protected double adjustWithinMargin(double d) {
        d = d * (1 - this.margin);
        return d;
    }

    protected Position adjustWithinMargin(Position p) {
        double x = adjustWithinMargin(p.x);
        double y = adjustWithinMargin(p.y);
        return new Position(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpatialDistribution) {
            return getId() == ((SpatialDistribution) obj).getId();
        } else {
            return false;
        }
    }

    public int getId() {
        return this.id;
    }

    public double getMargin() {
        return this.margin;
    }

    public double getPertubation() {
        return this.pertubation;
    }

    public double getRange() {
        return this.range;
    }

    public double getSpacing() {
        return this.spacing;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public double pertubate(double x) {
        x += this.pertubationDistribution.generate();
        return x;
    }

    public Position pertubate(Position p) {
        double x = pertubate(p.x);
        double y = pertubate(p.y);
        return new Position(x, y);
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public void setPertubation(double pertubation) {
        this.pertubationDistribution.setMin(-pertubation);
        this.pertubationDistribution.setMax(pertubation);
        this.pertubation = pertubation;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }
}
