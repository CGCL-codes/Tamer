package tuner3d.graphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import com.sun.opengl.util.GLUT;
import tuner3d.genome.Cds;
import tuner3d.genome.Region;
import tuner3d.genome.Dot;
import tuner3d.genome.Genome;
import tuner3d.genome.Parameter;
import tuner3d.genome.Rna;
import tuner3d.graphics.Palette;
import tuner3d.ui.GenomeCanvas;
import tuner3d.util.Misc;

public class Painter2D {

    private Genome genome;

    private Parameter parameter;

    private Palette palette;

    private boolean isBigChar = false;

    private GLUT glut;

    public static final double PI2 = Math.PI * 2;

    public Painter2D(Parameter parameter, Palette palette) {
        super();
        this.parameter = parameter;
        this.palette = palette;
    }

    public static void setGlColor(GL gl, Color color) {
        if (color == null) return;
        gl.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public static void setGlColor(GL gl, Color color, int alpha) {
        if (color == null) return;
        gl.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha / 255.0f);
    }

    public static void setGlColor(GL gl, Color color, float alpha) {
        if (color == null) return;
        gl.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
    }

    public Painter2D(Genome genome, Parameter parameter) {
        super();
        this.genome = genome;
        this.parameter = parameter;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public void drawLabel(GL gl) {
        setGlColor(gl, palette.coord_color);
        drawText(gl, genome.getName(), GenomeCanvas.LEFT_ALIGN, 10.0, 10.0, 0.0, 0.0f);
    }

    public void drawCoordinate(GL gl) {
        double x, y, r, a;
        r = parameter.getCoordRadius();
        a = genome.getAngle();
        drawCircle(gl, r);
        float size = genome.getStatistics().getSizeMb();
        for (float size2 = 0.0f; size2 <= size; size2 += parameter.getCoordUnit()) {
            x = r * Math.sin(a);
            y = r * Math.cos(a);
            drawLine2D(gl, r, a, 5);
            drawText(gl, Misc.toDecimal(size2), GenomeCanvas.LEFT_ALIGN, x, y, a, 1);
            a += (0.5f / size) * PI2;
        }
    }

    public void drawHistogram(GL gl) {
        double x, y, r, a;
        r = parameter.getSkewRadius();
        a = genome.getAngle();
        ArrayList<Dot> dots = genome.getGcSkew();
        for (Iterator<Dot> iterator = dots.iterator(); iterator.hasNext(); ) {
            Dot dot = iterator.next();
            if (dot.getVal() > 0) setGlColor(gl, palette.skew_color1, parameter.getSkewAlpha()); else setGlColor(gl, palette.skew_color2, parameter.getSkewAlpha());
            gl.glBegin(GL.GL_LINE_STRIP);
            x = r * Math.sin(a);
            y = r * Math.cos(a);
            gl.glVertex2d(x, y);
            x = (parameter.getSkewScale() * dot.getVal() + r) * Math.sin(a);
            y = (parameter.getSkewScale() * dot.getVal() + r) * Math.cos(a);
            gl.glVertex2d(x, y);
            gl.glEnd();
            a += genome.getSkewStep() * PI2 / genome.getSize();
        }
    }

    public void drawLineChart(GL gl) {
        double x, y, r, a;
        r = parameter.getGcRadius();
        a = genome.getAngle();
        setGlColor(gl, palette.gc_color, parameter.getGcAlpha());
        drawCircle(gl, r);
        gl.glLineWidth(3.0f);
        gl.glBegin(GL.GL_LINE_LOOP);
        ArrayList<Dot> dots = genome.getGcContent();
        float gcContent = genome.getStatistics().getGcContent();
        for (Iterator<Dot> iterator = dots.iterator(); iterator.hasNext(); ) {
            Dot dot = iterator.next();
            x = ((dot.getVal() - gcContent) * parameter.getGcScale() + r) * Math.sin(a);
            y = ((dot.getVal() - gcContent) * parameter.getGcScale() + r) * Math.cos(a);
            gl.glVertex2d(x, y);
            a += genome.getGcStep() * PI2 / genome.getSize();
        }
        gl.glEnd();
        gl.glLineWidth(1.0f);
    }

    public void drawCircle(GL gl, double r) {
        double x, y;
        gl.glBegin(GL.GL_LINE_LOOP);
        for (double angle = 0.0; angle < PI2; angle += 0.1) {
            x = r * Math.sin(angle);
            y = r * Math.cos(angle);
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    private void drawLine2D(GL gl, double r, double angle, float length) {
        double x, y;
        gl.glBegin(GL.GL_LINE_STRIP);
        x = r * Math.sin(angle);
        y = r * Math.cos(angle);
        gl.glVertex2d(x, y);
        x = (r + length) * Math.sin(angle);
        y = (r + length) * Math.cos(angle);
        gl.glVertex2d(x, y);
        gl.glEnd();
    }

    private void drawText(GL gl, String text, boolean moreThanHalf, double x, double y, double angle, float offset) {
        if (moreThanHalf) drawText(gl, text, GenomeCanvas.RIGHT_ALIGN, x, y, angle, offset); else drawText(gl, text, GenomeCanvas.LEFT_ALIGN, x, y, angle, offset);
    }

    private void drawText(GL gl, String text, byte align, double x, double y, double angle, float offset) {
        x += offset * Math.sin(angle);
        y += offset * Math.cos(angle);
        switch(align) {
            case GenomeCanvas.LEFT_ALIGN:
                gl.glRasterPos2d(x, y);
                break;
            case GenomeCanvas.RIGHT_ALIGN:
                gl.glRasterPos2d(x - 2.8 * text.length(), y);
                break;
            case GenomeCanvas.CENTER_ALIGN:
                gl.glRasterPos2d(x - 1.4 * text.length(), y);
                break;
            default:
                break;
        }
        if (isBigChar) glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, text); else glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, text);
    }

    public void drawRegion(GL gl) {
        double x, y, r, a;
        r = parameter.getRegionRadius();
        a = genome.getAngle();
        setGlColor(gl, palette.region_color);
        double amove = 0.02;
        for (Iterator<Region> iterator = genome.getRegion().iterator(); iterator.hasNext(); ) {
            Region region = iterator.next();
            a = region.getBegin() * PI2 / genome.getSize() + genome.getAngle();
            if (parameter.getRegionColor() == Parameter.ORDER_COLOR) setGlColor(gl, Palette.getByOrder(region.getId())); else if (parameter.getRegionColor() == Parameter.ONE_COLOR) setGlColor(gl, palette.region_color);
            double a2 = a;
            double aend = a + region.getLength() * PI2 / genome.getSize();
            do {
                gl.glBegin(GL.GL_QUADS);
                x = r * Math.sin(a2);
                y = r * Math.cos(a2);
                gl.glVertex2d(x, y);
                gl.glVertex2d(x + parameter.getRegionWidth() * Math.sin(a2), y + parameter.getRegionWidth() * Math.cos(a2));
                x = r * Math.sin(a2 + amove);
                y = r * Math.cos(a2 + amove);
                gl.glVertex2d(x + parameter.getRegionWidth() * Math.sin(a2 + amove), y + parameter.getRegionWidth() * Math.cos(a2 + amove));
                gl.glVertex2d(x, y);
                gl.glEnd();
                a2 += amove;
            } while (a2 <= aend);
        }
    }

    public void drawRna(GL gl) {
        double x, y, r, a;
        ArrayList<Rna> rnas = genome.getRna();
        if (rnas.isEmpty()) return;
        r = parameter.getRnaRadius();
        drawCircle(gl, r);
        for (Iterator<Rna> iterator = rnas.iterator(); iterator.hasNext(); ) {
            Rna rna = iterator.next();
            rna.setTypeColor(palette);
            setGlColor(gl, rna.getColor());
            a = rna.getBegin() * PI2 / genome.getSize() + genome.getAngle();
            x = r * Math.sin(a);
            y = r * Math.cos(a);
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex2d(x, y);
            gl.glVertex2d(x + parameter.getRnaWidth() * Math.sin(a), y + parameter.getRnaWidth() * Math.cos(a));
            gl.glEnd();
            setGlColor(gl, palette.coord_color);
            if (rna.isLongMarked()) {
                drawLine2D(gl, r + parameter.getRnaWidth(), a, 30.0f);
                drawText(gl, rna.getLocusTag(), rna.isMoreThanHalf(), x + parameter.getRnaWidth() * Math.sin(a), y + parameter.getRnaWidth() * Math.cos(a), a, 30.0f);
            } else if (rna.isMarked()) {
                drawLine2D(gl, r + parameter.getRnaWidth(), a, 20.0f);
                drawText(gl, rna.getLocusTag(), rna.isMoreThanHalf(), x + parameter.getRnaWidth() * Math.sin(a), y + parameter.getRnaWidth() * Math.cos(a), a, 20.0f);
            }
        }
    }

    public void drawCds(GL gl) {
        double x, y, r, a;
        ArrayList<Cds> cdss = genome.getCds();
        if (cdss.isEmpty()) return;
        r = parameter.getCdsRadius();
        boolean isCdsLine = parameter.isCdsLine() || (genome.getStatistics().getCdsNum() > parameter.getCdsLineLimit());
        setGlColor(gl, palette.cds_color);
        drawCircle(gl, r);
        Color[] cds_length_gradient_colors = Palette.getGradientColors(parameter.getCdsLengthGradientDivision(), palette.cds_length_gradient_start_color, palette.cds_length_gradient_end_color);
        Color[] cds_gc_gradient_colors = Palette.getGradientColors(parameter.getCdsGcGradientDivision(), palette.cds_gc_gradient_start_color, palette.cds_gc_gradient_end_color);
        float max_length = genome.getStatistics().getMaxCdsLength();
        float min_length = genome.getStatistics().getMinCdsLength();
        float length_unit = (max_length - min_length) / parameter.getCdsLengthGradientDivision();
        float max_gc_content = genome.getStatistics().getMaxCdsGcContent();
        float min_gc_content = genome.getStatistics().getMinCdsGcContent();
        float gc_content_unit = (max_gc_content - min_gc_content) / parameter.getCdsGcGradientDivision();
        for (Iterator<Cds> iterator = cdss.iterator(); iterator.hasNext(); ) {
            Cds cds = iterator.next();
            switch(parameter.getCdsColor()) {
                case Parameter.COG_COLOR:
                    setGlColor(gl, palette.getByCog(cds.getCog()));
                    break;
                case Parameter.ONE_COLOR:
                    setGlColor(gl, palette.cds_color);
                    break;
                case Parameter.ORDER_COLOR:
                    setGlColor(gl, Palette.getByOrder(cds.getId()));
                    break;
                case Parameter.LENGTH_COLOR:
                    int length_index = Palette.getGradientLevel(cds.getLength(), min_length, max_length, length_unit, parameter.getCdsGcGradientDivision());
                    setGlColor(gl, cds_length_gradient_colors[length_index]);
                    break;
                case Parameter.GC_COLOR:
                    int gc_index = Palette.getGradientLevel(cds.gcContent(), min_gc_content, max_gc_content, gc_content_unit, parameter.getCdsGcGradientDivision());
                    setGlColor(gl, cds_gc_gradient_colors[gc_index]);
                    break;
                default:
                    break;
            }
            a = cds.getBegin() * PI2 / genome.getSize() + genome.getAngle();
            x = r * Math.sin(a);
            y = r * Math.cos(a);
            if (parameter.isCdsStrand()) {
                if (cds.getStrand()) {
                    if (isCdsLine) gl.glBegin(GL.GL_LINE_STRIP); else gl.glBegin(GL.GL_QUADS);
                    gl.glVertex2d(x, y);
                    gl.glVertex2d(x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a));
                    if (!isCdsLine) {
                        a += cds.getLength() * PI2 / genome.getSize();
                        x = r * Math.sin(a);
                        y = r * Math.cos(a);
                        gl.glVertex2d(x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a));
                        gl.glVertex2d(x, y);
                    }
                    gl.glEnd();
                    setGlColor(gl, palette.coord_color);
                    if (cds.isLongMarked()) {
                        drawLine2D(gl, r + parameter.getCdsWidth(), a, 30.0f);
                        drawText(gl, cds.getLocusTag(), cds.isMoreThanHalf(), x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a), a, 30.0f);
                    } else if (cds.isMarked()) {
                        drawLine2D(gl, r + parameter.getCdsWidth(), a, 20.0f);
                        drawText(gl, cds.getLocusTag(), cds.isMoreThanHalf(), x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a), a, 20.0f);
                    }
                } else {
                    if (isCdsLine) gl.glBegin(GL.GL_LINE_STRIP); else gl.glBegin(GL.GL_QUADS);
                    gl.glVertex2d(x, y);
                    gl.glVertex2d(x - parameter.getCdsWidth() * Math.sin(a), y - parameter.getCdsWidth() * Math.cos(a));
                    if (!isCdsLine) {
                        a += cds.getLength() * PI2 / genome.getSize();
                        x = r * Math.sin(a);
                        y = r * Math.cos(a);
                        gl.glVertex2d(x - parameter.getCdsWidth() * Math.sin(a), y - parameter.getCdsWidth() * Math.cos(a));
                        gl.glVertex2d(x, y);
                    }
                    gl.glEnd();
                    setGlColor(gl, palette.coord_color);
                    if (cds.isLongMarked()) {
                        drawLine2D(gl, r, a, 30.0f);
                        drawText(gl, cds.getLocusTag(), cds.isMoreThanHalf(), x, y, a, 30.0f);
                    } else if (cds.isMarked()) {
                        drawLine2D(gl, r, a, 20.0f);
                        drawText(gl, cds.getLocusTag(), cds.isMoreThanHalf(), x, y, a, 20.0f);
                    }
                }
            } else {
                if (isCdsLine) gl.glBegin(GL.GL_LINE_STRIP); else gl.glBegin(GL.GL_QUADS);
                gl.glVertex2d(x, y);
                gl.glVertex2d(x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a));
                if (!isCdsLine) {
                    a += cds.getLength() * PI2 / genome.getSize();
                    x = r * Math.sin(a);
                    y = r * Math.cos(a);
                    gl.glVertex2d(x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a));
                    gl.glVertex2d(x, y);
                }
                gl.glEnd();
                setGlColor(gl, palette.coord_color);
                if (cds.isLongMarked()) {
                    drawLine2D(gl, r + parameter.getCdsWidth(), a, 30.0f);
                    drawText(gl, cds.getLocusTag(), cds.isMoreThanHalf(), x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a), a, 30.0f);
                } else if (cds.isMarked()) {
                    drawLine2D(gl, r + parameter.getCdsWidth(), a, 20.0f);
                    drawText(gl, cds.getLocusTag(), cds.isMoreThanHalf(), x + parameter.getCdsWidth() * Math.sin(a), y + parameter.getCdsWidth() * Math.cos(a), a, 20.0f);
                }
            }
        }
    }

    public void drawPies(GL gl) {
        double x, y, r, a;
        r = parameter.getPieRadius();
        a = genome.getAngle();
        double amove = 0.02;
        for (Iterator<Region> iterator = genome.getPies().iterator(); iterator.hasNext(); ) {
            Region region = iterator.next();
            a = region.getBegin() * PI2 / genome.getSize() + genome.getAngle();
            setGlColor(gl, region.getColor(), parameter.getPieAlpha());
            double a2 = a;
            double aend = a + region.getLength() * PI2 / genome.getSize();
            gl.glBegin(GL.GL_TRIANGLE_FAN);
            gl.glVertex2d(0.0, 0.0);
            do {
                x = r * Math.sin(a2);
                y = r * Math.cos(a2);
                gl.glVertex2d(x, y);
                a2 += amove;
            } while (a2 <= aend);
            x = r * Math.sin(a2);
            y = r * Math.cos(a2);
            gl.glVertex2d(x, y);
            gl.glEnd();
        }
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public void setGLUT(GLUT glut) {
        this.glut = glut;
    }

    public void setBigChar(boolean isBigChar) {
        this.isBigChar = isBigChar;
    }
}
