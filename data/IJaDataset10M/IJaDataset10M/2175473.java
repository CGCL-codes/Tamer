package logic.Math;

public final class Vector2D {

    public long x = 0;

    public long y = 0;

    public final Vector2D set(final long x, final long y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public final Vector2D set(final Vector2D v) {
        return set(v.x, v.y);
    }

    public final Vector2D add(final Vector2D v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public final Vector2D sub(final Vector2D v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public final Vector2D rotate(final int deg) {
        final long x = this.x;
        final long y = this.y;
        final long sin = Math.sin(deg);
        final long cos = Math.cos(deg);
        this.x = (x * cos - y * sin) / Math.accuracy;
        this.y = (x * sin + y * cos) / Math.accuracy;
        return this;
    }

    public final Vector2D rotate90() {
        return set(-y, x);
    }

    public final long getProjection(final Vector2D v) {
        return (x * v.x + y * v.y) / getLength();
    }

    public final Vector2D interpolate(final Vector2D v, final long milliFactor) {
        x += (v.x - x) * milliFactor / Math.accuracy;
        y += (v.y - y) * milliFactor / Math.accuracy;
        return this;
    }

    public final Vector2D negate() {
        x = -x;
        y = -y;
        return this;
    }

    public final Vector2D mul(final long l) {
        x *= l;
        y *= l;
        return this;
    }

    public final Vector2D div(final long l) {
        x /= l;
        y /= l;
        return this;
    }

    public final long getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public final Vector2D setLength(final long l) {
        long d = getLength();
        return mul(l).div(d);
    }

    public Vector2D(final long x, final long y) {
        set(x, y);
    }

    public Vector2D(final Vector2D v) {
        set(v);
    }

    public Vector2D() {
    }
}
