package it.enricod.jcontextfree.engine.model.impl;

import it.enricod.jcontextfree.engine.model.FigureRegion;
import it.enricod.jcontextfree.engine.model.FigureEnum;
import it.enricod.jcontextfree.engine.model.IFigure;
import it.enricod.jcontextfree.engine.model.IFigureParameters;
import it.enricod.jcontextfree.engine.model.IPaint;
import it.enricod.jcontextfree.engine.model.impl.CircleRegionCalculator;
import it.enricod.jcontextfree.engine.model.impl.Figure;
import it.enricod.jcontextfree.engine.model.impl.Paint;
import it.enricod.jcontextfree.engine.model.impl.SquareRegionCalculator;
import it.enricod.jcontextfree.engine.model.vo.Position2D;
import org.junit.Test;
import org.junit.Assert;
import static java.lang.Math.*;

/**
 * 
 * @author edonelli
 */
public class TestCalculateRegion {

    public static final double EPS = 0.001d;

    @Test
    public void calculateSquareRegion() {
        IFigureParameters params = new it.enricod.jcontextfree.engine.model.impl.FigureParameters();
        params.getRelative().setX(11);
        params.getRelative().setY(20);
        params.getRelative().setScale(24);
        SquareRegionCalculator areaCalculator = new SquareRegionCalculator();
        FigureRegion area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(-1f, area.getP1().getX(), EPS);
        Assert.assertEquals(8, area.getP1().getY(), EPS);
        Assert.assertEquals(23, area.getP2().getX(), EPS);
        Assert.assertEquals(32, area.getP2().getY(), EPS);
    }

    @Test
    public void calculateSquareRegionWithContextParameters() {
        double contextX = 50d;
        double contextY = 30d;
        double shapeX = 10d;
        double shapeY = 20d;
        double size = 24d;
        double halfSize = size / 2;
        FigureParameters params = new FigureParameters();
        params.getRelative().setX(shapeX);
        params.getRelative().setY(shapeY);
        params.getTransform().setX(contextX);
        params.getTransform().setY(contextY);
        params.getRelative().setScale(24);
        SquareRegionCalculator areaCalculator = new SquareRegionCalculator();
        FigureRegion area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(contextX + shapeX - halfSize, area.getP1().getX(), EPS);
        Assert.assertEquals(contextY + shapeY - halfSize, area.getP1().getY(), EPS);
        Assert.assertEquals(contextX + shapeX + halfSize, area.getP2().getX(), EPS);
        Assert.assertEquals(contextY + shapeY + halfSize, area.getP2().getY(), EPS);
    }

    @Test
    public void calculateSquareAreaWithRotation() {
        double x = 200d;
        double s = 400d;
        double l = s / 2;
        FigureParameters params = new FigureParameters();
        params.getRelative().setX(l);
        params.getRelative().setY(l);
        params.getRelative().setScale(s);
        params.getRelative().setRotation(0d);
        SquareRegionCalculator areaCalculator = new SquareRegionCalculator();
        FigureRegion area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(0d, area.getP1().getX(), 0.001f);
        Assert.assertEquals(0d, area.getP1().getY(), 0.001f);
        Assert.assertEquals(400, area.getP2().getX(), 0.001f);
        Assert.assertEquals(400, area.getP2().getY(), 0.001f);
        double alfa = PI / 4;
        params.getRelative().setRotation(PI / 4);
        area = areaCalculator.calculateRegion(params);
        double d = l * sqrt(2) * cos(PI / 4 - alfa);
        Assert.assertEquals(200 - d, area.getP1().getX(), 0.001f);
        Assert.assertEquals(200 - d, area.getP1().getY(), 0.001f);
        Assert.assertEquals(200 + d, area.getP2().getX(), 0.001f);
        Assert.assertEquals(200 + d, area.getP2().getY(), 0.001f);
        alfa = -PI / 4;
        params.getRelative().setRotation(alfa);
        d = l * sqrt(2);
        area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(x - d, area.getP1().getX(), 0.001f);
        Assert.assertEquals(x - d, area.getP1().getY(), 0.001f);
        Assert.assertEquals(x + d, area.getP2().getX(), 0.001f);
        Assert.assertEquals(x + d, area.getP2().getY(), 0.001f);
        alfa = PI / 2;
        params.getRelative().setRotation(alfa);
        d = l * sqrt(2) * cos(PI / 4 - alfa);
        area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(0, area.getP1().getX(), 0.001f);
        Assert.assertEquals(0d, area.getP1().getY(), 0.001f);
        Assert.assertEquals(400, area.getP2().getX(), 0.001f);
        Assert.assertEquals(400, area.getP2().getY(), 0.001f);
    }

    @Test
    public void calculateSquareAreaWithContextParams() {
        double contextX = 5;
        double contextY = 4;
        double x = 1d;
        double y = 0d;
        double s = 1d;
        double hs = s / 2;
        FigureParameters params = new FigureParameters();
        params.getRelative().setX(x);
        params.getRelative().setY(y);
        params.getRelative().setScale(s);
        params.getRelative().setRotation(0);
        params.getTransform().setX(contextX);
        params.getTransform().setY(contextY);
        SquareRegionCalculator areaCalculator = new SquareRegionCalculator();
        FigureRegion area = areaCalculator.calculateRegion(params);
        double absoluteX = contextX * s + (x * s);
        double absoluteY = contextY * s + (y * s);
        Assert.assertEquals(absoluteX - hs, area.getP1().getX(), EPS);
        Assert.assertEquals(absoluteY - hs, area.getP1().getY(), EPS);
        Assert.assertEquals(absoluteX + hs, area.getP2().getX(), EPS);
        Assert.assertEquals(absoluteY + hs, area.getP2().getY(), EPS);
        double alfa = PI / 4;
        params.getRelative().setRotation(alfa);
        double d = hs * sqrt(2);
        area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(absoluteX - d, area.getP1().getX(), EPS);
        Assert.assertEquals(absoluteY - d, area.getP1().getY(), EPS);
        Assert.assertEquals(absoluteX + d, area.getP2().getX(), EPS);
        Assert.assertEquals(absoluteY + d, area.getP2().getY(), EPS);
        alfa = 0;
        double calfa = PI / 4;
        params.getRelative().setRotation(alfa);
        params.getTransform().setRotation(calfa);
        d = hs * sqrt(2);
        area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(absoluteX - d, area.getP1().getX(), EPS);
        Assert.assertEquals(absoluteY - d, area.getP1().getY(), EPS);
        Assert.assertEquals(absoluteX + d, area.getP2().getX(), EPS);
        Assert.assertEquals(absoluteY + d, area.getP2().getY(), EPS);
    }

    @Test
    public void calculateCircleRegion() {
        FigureParameters params = new FigureParameters();
        params.getRelative().setX(11);
        params.getRelative().setY(20);
        params.getRelative().setScale(24);
        CircleRegionCalculator areaCalculator = new CircleRegionCalculator();
        FigureRegion area = areaCalculator.calculateRegion(params);
        Assert.assertEquals(-1f, area.getP1().getX(), EPS);
        Assert.assertEquals(8, area.getP1().getY(), EPS);
        Assert.assertEquals(23, area.getP2().getX(), EPS);
        Assert.assertEquals(32, area.getP2().getY(), EPS);
    }

    @Test
    public void calculateFigureRegion() {
        IFigure figure = new Figure(FigureEnum.SQUARE);
        IFigureParameters params = figure.getParameters();
        params.getRelative().setX(0);
        params.getRelative().setY(0);
        params.getRelative().setScale(200d);
        IPaint paint = new Paint();
        paint.addFigure(figure);
        FigureRegion area = paint.getRegion();
        Assert.assertEquals(-100d, area.getP1().getX(), EPS);
        Assert.assertEquals(-100d, area.getP1().getY(), EPS);
        Assert.assertEquals(100d, area.getP2().getX(), EPS);
        Assert.assertEquals(100d, area.getP2().getY(), EPS);
        figure = new Figure(FigureEnum.SQUARE);
        figure.getParameters().getRelative().setX(0);
        figure.getParameters().getRelative().setY(0);
        figure.getParameters().getRelative().setScale(200);
        figure.getParameters().getRelative().setRotation(Math.PI / 4);
        paint = new Paint();
        paint.addFigure(figure);
        area = paint.getRegion();
        Assert.assertEquals(-100d * Math.sqrt(2), area.getP1().getX(), EPS);
        Assert.assertEquals(-100d * Math.sqrt(2), area.getP1().getY(), EPS);
        Assert.assertEquals(100d * Math.sqrt(2), area.getP2().getX(), EPS);
        Assert.assertEquals(100d * Math.sqrt(2), area.getP2().getY(), EPS);
    }
}
