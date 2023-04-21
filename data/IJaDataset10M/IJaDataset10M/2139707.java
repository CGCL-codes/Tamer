package csimage.demo.animate;

public class BouncingBall extends Ball {

    protected IntRange _width;

    protected IntRange _height;

    protected BouncingBall(IntRange width, IntRange height) {
        super();
        _width = width;
        _height = height;
        checkInvariant();
    }

    public BouncingBall(IntRange width, IntRange height, int xStart, int yStart, int radius, int dx, int dy) {
        super(xStart, yStart, radius, dx, dy);
        _width = width;
        _height = height;
        checkInvariant();
    }

    public void progress() {
        checkInvariant();
        translate(_dx, _dy);
        double x = getX();
        double y = getY();
        double radius = getWidth();
        double diam = getDiameter();
        if (x <= _width.getLo()) {
            _dx = -_dx;
            double Xnew = _width.getLo();
            setX(Xnew);
            assert getX() == Xnew : getX() + " == " + Xnew;
        }
        assert _width.getHi() == 500;
        if (x + diam >= _width.getHi()) {
            _dx = -_dx;
            double Xnew = _width.getHi() - diam - 1;
            setX(Xnew);
            assert getX() == Xnew : getX() + " == " + Xnew;
        }
        if (y <= _height.getLo()) {
            _dy = -_dy;
            double Ynew = _height.getLo();
            setY(Ynew);
            assert getY() == Ynew : getY() + " == " + Ynew;
        }
        if (y + diam >= _height.getHi()) {
            _dy = -_dy;
            double Ynew = _height.getHi() - diam - 1;
            setY(Ynew);
            assert getY() == Ynew : getY() + " == " + Ynew;
        }
        checkInvariant();
    }

    public void checkInvariant() {
        int x = (int) getX();
        int y = (int) getY();
        int diam = (int) getDiameter();
        assert _width.contains(x) : _width.asInequality(x) + ", x = " + x + ", _dx = " + _dx;
        assert _height.contains(y) : _height.asInequality(y) + ", y = " + y + ", _dy = " + _dy;
        assert _width.contains(x + diam) : _width.asInequality(x + diam) + ", x + diam = " + (x + diam) + ", _dx = " + _dx + ", diameter = " + diam;
        assert _height.contains(y + diam) : _height.asInequality(y + diam) + ", y + diam = " + (y + diam) + ", _dy = " + _dy + ", diameter = " + diam;
    }
}
