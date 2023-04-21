package progl;

public interface Shape {

    static final int POINTS = (1 << 4) | 0;

    static final int LINES = (1 << 5) | 0;

    static final int LINE_STRIP = (1 << 5) | 1;

    static final int LINE_LOOP = (1 << 5) | 2;

    static final int TRIANGLES = (1 << 6) | 0;

    static final int TRIANGLE_STRIP = (1 << 6) | 1;

    static final int TRIANGLE_FAN = (1 << 6) | 2;

    static final int QUADS = (1 << 7) | 0;

    static final int QUAD_STRIP = (1 << 7) | 1;

    static final int POLYGON = (1 << 8) | 0;
}
