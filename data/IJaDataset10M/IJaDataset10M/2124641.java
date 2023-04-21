package com.sun.pisces;

/**
 * The <code>Flattener</code> class rewrites a general path, which
 * may include curved segments, into one containing only linear
 * segments suitable for sending to a <code>LineSink</code>.
 *
 * <p> Curved segments specified by <code>quadTo</code> and
 * <code>curveTo</code> commands will be subdivided into two pieces.
 * When the control points of a segment lie sufficiently close
 * togther, such that <code>max(x_i) - min(x_i) < flatness</code> and
 * <code>max(y_i) - min(y_i) < flatness</code> for a user-supplied
 * <code>flatness</code> parameter, a <code>lineTo</code> command is
 * emitted between the first and last points of the curve.
 */
public class Flattener extends PathSink {

    public static final long MAX_CHORD_LENGTH_SQ = 16L * 16L * 65536L * 65536L;

    public static final long MIN_CHORD_LENGTH_SQ = 65536L * 65536L / (2L * 2L);

    public static final int LG_FLATNESS = 0;

    public static final int FLATNESS_SQ_SHIFT = 2 * (-LG_FLATNESS);

    LineSink output;

    int flatness, flatnessSq;

    int x0, y0;

    /**
     * Empty constructor.  <code>setOutput</code> and
     * <code>setFlatness</code> must be called prior to calling any
     * other methods.
     */
    public Flattener() {
    }

    /**
     * Constructs a <code>Flattener</code> that will rewrite any
     * incoming <code>quadTo</code> and <code>curveTo</code> commands
     * into a series of <code>lineTo</code> commands with maximum X
     * and Y extents no larger than the supplied <code>flatness</code>
     * value.  The flat segments will be sent as commands to the given
     * <code>LineSink</code>.
     *
     * @param output a <code>LineSink</code> to which commands
     * should be sent.
     * @param flatness the maximum extent of a subdivided output line
     * segment, in S15.16 format.
     */
    public Flattener(LineSink output, int flatness) {
        setOutput(output);
        setFlatness(flatness);
    }

    /**
     * Sets the output <code>LineSink</code> of this
     * <code>Flattener</code>.
     *
     * @param output an output <code>LineSink</code>.
     */
    public void setOutput(LineSink output) {
        this.output = output;
    }

    /**
     * Sets the desired output flatness for this <code>Flattener</code>.
     *
     * @param flatness the maximum extent of a subdivided output line
     * segment, in S15.16 format.
     */
    public void setFlatness(int flatness) {
        this.flatness = flatness;
        this.flatnessSq = (int) ((long) flatness * flatness >> 16);
    }

    public void moveTo(int x0, int y0) {
        output.moveTo(x0, y0);
        this.x0 = x0;
        this.y0 = y0;
    }

    public void lineJoin() {
        output.lineJoin();
    }

    public void lineTo(int x1, int y1) {
        output.lineJoin();
        output.lineTo(x1, y1);
        this.x0 = x1;
        this.y0 = y1;
    }

    public void quadTo(int x1, int y1, int x2, int y2) {
        output.lineJoin();
        quadToHelper(x1, y1, x2, y2);
    }

    private boolean flatEnough(int x0, int y0, int x1, int y1, int x2, int y2) {
        long dx = (long) x2 - (long) x0;
        long dy = (long) y2 - (long) y0;
        long denom2 = dx * dx + dy * dy;
        if (denom2 > MAX_CHORD_LENGTH_SQ) {
            return false;
        }
        if (denom2 < MIN_CHORD_LENGTH_SQ) {
            int minx = Math.min(Math.min(x0, x1), x2);
            int miny = Math.min(Math.min(y0, y1), y2);
            int maxx = Math.max(Math.max(x0, x1), x2);
            int maxy = Math.max(Math.max(y0, y1), y2);
            long dx1 = (long) maxx - (long) minx;
            long dy1 = (long) maxy - (long) miny;
            long l2 = dx1 * dx1 + dy1 * dy1;
            if (l2 < MIN_CHORD_LENGTH_SQ) {
                return true;
            }
        }
        long num = -dy * x1 + dx * y1 + ((long) x0 * y2 - (long) x2 * y0);
        num >>= 16;
        long numsq = num * num;
        long df2 = denom2 * flatnessSq >> 16;
        return numsq < df2;
    }

    private void quadToHelper(int x1, int y1, int x2, int y2) {
        if (flatEnough(x0, y0, x1, y1, x2, y2)) {
            output.lineTo(x1, y1);
            output.lineTo(x2, y2);
        } else {
            long lx1 = (long) x1;
            long ly1 = (long) y1;
            long x01 = x0 + lx1;
            long y01 = y0 + ly1;
            long x12 = lx1 + x2;
            long y12 = ly1 + y2;
            long x012 = x01 + x12;
            long y012 = y01 + y12;
            quadToHelper((int) (x01 >> 1), (int) (y01 >> 1), (int) (x012 >> 2), (int) (y012 >> 2));
            quadToHelper((int) (x12 >> 1), (int) (y12 >> 1), x2, y2);
        }
        this.x0 = x2;
        this.y0 = y2;
    }

    public void cubicTo(int x1, int y1, int x2, int y2, int x3, int y3) {
        output.lineJoin();
        cubicToHelper(x1, y1, x2, y2, x3, y3);
    }

    private boolean flatEnough(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3) {
        long dx = (long) x3 - (long) x0;
        long dy = (long) y3 - (long) y0;
        long denom2 = dx * dx + dy * dy;
        if (denom2 > MAX_CHORD_LENGTH_SQ) {
            return false;
        }
        if (denom2 < MIN_CHORD_LENGTH_SQ) {
            int minx = Math.min(Math.min(Math.min(x0, x1), x2), x3);
            int miny = Math.min(Math.min(Math.min(y0, y1), y2), y3);
            int maxx = Math.max(Math.max(Math.max(x0, x1), x2), x3);
            int maxy = Math.max(Math.max(Math.max(y0, y1), y2), y3);
            long dx1 = (long) maxx - (long) minx;
            long dy1 = (long) maxy - (long) miny;
            long l2 = dx1 * dx1 + dy1 * dy1;
            if (l2 < MIN_CHORD_LENGTH_SQ) {
                return true;
            }
        }
        long df2 = denom2 * flatnessSq >> 16;
        long cross = (long) x0 * y3 - (long) x3 * y0;
        long num1 = dx * y1 - dy * x1 + cross;
        num1 >>= 16;
        long num1sq = num1 * num1;
        if (num1sq > df2) {
            return false;
        }
        long num2 = dx * y2 - dy * x2 + cross;
        num2 >>= 16;
        long num2sq = num2 * num2;
        return num2sq < df2;
    }

    private void cubicToHelper(int x1, int y1, int x2, int y2, int x3, int y3) {
        if (flatEnough(x0, y0, x1, y1, x2, y2, x3, y3)) {
            output.lineTo(x1, y1);
            output.lineTo(x2, y2);
            output.lineTo(x3, y3);
        } else {
            long lx1 = (long) x1;
            long ly1 = (long) y1;
            long lx2 = (long) x2;
            long ly2 = (long) y2;
            long x01 = x0 + lx1;
            long y01 = y0 + ly1;
            long x12 = lx1 + lx2;
            long y12 = ly1 + ly2;
            long x23 = lx2 + x3;
            long y23 = ly2 + y3;
            long x012 = x01 + x12;
            long y012 = y01 + y12;
            long x123 = x12 + x23;
            long y123 = y12 + y23;
            long x0123 = x012 + x123;
            long y0123 = y012 + y123;
            cubicToHelper((int) (x01 >> 1), (int) (y01 >> 1), (int) (x012 >> 2), (int) (y012 >> 2), (int) (x0123 >> 3), (int) (y0123 >> 3));
            cubicToHelper((int) (x123 >> 2), (int) (y123 >> 2), (int) (x23 >> 1), (int) (y23 >> 1), x3, y3);
        }
        this.x0 = x3;
        this.y0 = y3;
    }

    public void close() {
        output.lineJoin();
        output.close();
    }

    public void end() {
        output.end();
    }
}
