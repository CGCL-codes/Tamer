package it.enricod.jcontextfree.engine.model.impl;

import it.enricod.jcontextfree.engine.model.FigureEnum;
import it.enricod.jcontextfree.engine.model.FigureRegion;
import it.enricod.jcontextfree.engine.model.IFigure;
import it.enricod.jcontextfree.engine.model.IFigureParameters;
import it.enricod.jcontextfree.engine.model.IFigureRegionCalculator;

public class Figure implements IFigure {

    private FigureEnum type;

    private IFigureParameters parameters = new it.enricod.jcontextfree.engine.model.impl.FigureParameters();

    public Figure(FigureEnum type) {
        this.type = type;
    }

    public FigureEnum getType() {
        return type;
    }

    public IFigureParameters getParameters() {
        return parameters;
    }

    public FigureRegion getFigureRegion() {
        return getAreaCalculator().calculateRegion(parameters);
    }

    private IFigureRegionCalculator getAreaCalculator() {
        if (type.equals(FigureEnum.CIRCLE)) {
            return new CircleRegionCalculator();
        } else if (type.equals(FigureEnum.SQUARE)) {
            return new SquareRegionCalculator();
        }
        throw new IllegalArgumentException("figura sconosciuta");
    }

    public IFigure translate(double dx, double dy) {
        Figure result = new Figure(type);
        result.getParameters().getTransform().setScale(this.getParameters().getTransform().getScale());
        result.getParameters().getTransform().setRotation(this.getParameters().getTransform().getRotation());
        result.getParameters().getTransform().setX(this.getParameters().getTransform().getX() + dx);
        result.getParameters().getTransform().setY(this.getParameters().getTransform().getY() + dy);
        result.getParameters().getRelative().setScale(parameters.getRelative().getScale());
        result.getParameters().getRelative().setX(this.getParameters().getRelative().getX());
        result.getParameters().getRelative().setY(this.getParameters().getRelative().getY());
        result.getParameters().getRelative().setRotation(this.getParameters().getRelative().getRotation());
        return result;
    }

    public IFigure scale(double factor) {
        Figure result = new Figure(type);
        result.getParameters().getRelative().setScale(this.getParameters().getRelative().getScale());
        result.getParameters().getTransform().setScale(this.getParameters().getTransform().getScale() * factor);
        result.getParameters().getRelative().setX(this.getParameters().getRelative().getX() * factor);
        result.getParameters().getRelative().setY(this.getParameters().getRelative().getY() * factor);
        result.getParameters().getTransform().setX(this.getParameters().getTransform().getX() * factor);
        result.getParameters().getTransform().setY(this.getParameters().getTransform().getY() * factor);
        result.getParameters().getTransform().setRotation(this.getParameters().getTransform().getRotation());
        result.getParameters().getRelative().setRotation(this.getParameters().getRelative().getRotation());
        return result;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getType().getName());
        buf.append(", p(").append(getParameters().getRelative().getX()).append(",").append(getParameters().getRelative().getY()).append(")");
        buf.append(", s=").append(getParameters().getRelative().getScale());
        buf.append(", r=").append(getParameters().getRelative().getRotation());
        buf.append(", cp(").append(getParameters().getTransform().getX()).append(",").append(getParameters().getTransform().getY()).append(")");
        buf.append(", cs=").append(getParameters().getTransform().getScale());
        buf.append(", cr=").append(getParameters().getTransform().getRotation());
        return buf.toString();
    }

    public IFigureParameters getAbsoluteParameters() {
        return null;
    }

    public IFigureParameters getRelativeParameters() {
        return null;
    }

    public IFigureParameters getTransformParameters() {
        return null;
    }
}
