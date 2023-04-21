package org.berlin.math.demo;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLine;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.PngFilterType;
import ar.com.hjg.pngj.PngWriter;

/**
 * Basic math utilities for google app engine.
 * Render the mandelbrot fractal.
 * 
 * @author bbrown (berlin.brown at gmail.com)
 *
 */
public class ImageDemoMandelbrotServlet extends HttpServlet {

    /**
	 * Serial version id. 
	 */
    private static final long serialVersionUID = 1L;

    public static class Complex {

        private final double re;

        private final double im;

        public Complex(double real, double imag) {
            re = real;
            im = imag;
        }

        public String toString() {
            if (im == 0) return re + "";
            if (re == 0) return im + "i";
            if (im < 0) return re + " - " + (-im) + "i";
            return re + " + " + im + "i";
        }

        public double abs() {
            return Math.hypot(re, im);
        }

        public double phase() {
            return Math.atan2(im, re);
        }

        public Complex plus(Complex b) {
            Complex a = this;
            double real = a.re + b.re;
            double imag = a.im + b.im;
            return new Complex(real, imag);
        }

        public Complex minus(Complex b) {
            Complex a = this;
            double real = a.re - b.re;
            double imag = a.im - b.im;
            return new Complex(real, imag);
        }

        public Complex times(Complex b) {
            Complex a = this;
            double real = a.re * b.re - a.im * b.im;
            double imag = a.re * b.im + a.im * b.re;
            return new Complex(real, imag);
        }

        public Complex times(double alpha) {
            return new Complex(alpha * re, alpha * im);
        }

        public Complex conjugate() {
            return new Complex(re, -im);
        }

        public Complex reciprocal() {
            double scale = re * re + im * im;
            return new Complex(re / scale, -im / scale);
        }

        public double re() {
            return re;
        }

        public double im() {
            return im;
        }

        public Complex divides(Complex b) {
            Complex a = this;
            return a.times(b.reciprocal());
        }

        public Complex exp() {
            return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
        }

        public Complex sin() {
            return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
        }

        public Complex cos() {
            return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
        }

        public Complex tan() {
            return sin().divides(cos());
        }

        public static Complex plus(Complex a, Complex b) {
            double real = a.re + b.re;
            double imag = a.im + b.im;
            Complex sum = new Complex(real, imag);
            return sum;
        }

        public static void test(String[] args) {
            Complex a = new Complex(5.0, 6.0);
            Complex b = new Complex(-3.0, 4.0);
            System.out.println("a            = " + a);
            System.out.println("b            = " + b);
            System.out.println("Re(a)        = " + a.re());
            System.out.println("Im(a)        = " + a.im());
            System.out.println("b + a        = " + b.plus(a));
            System.out.println("a - b        = " + a.minus(b));
            System.out.println("a * b        = " + a.times(b));
            System.out.println("b * a        = " + b.times(a));
            System.out.println("a / b        = " + a.divides(b));
            System.out.println("(a / b) * b  = " + a.divides(b).times(b));
            System.out.println("conj(a)      = " + a.conjugate());
            System.out.println("|a|          = " + a.abs());
            System.out.println("tan(a)       = " + a.tan());
        }
    }

    public static int mandelbrot(final Complex z0, final int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) {
                return t;
            }
            z = z.times(z).plus(z0);
        }
        return max;
    }

    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        final int cols = 380;
        final int rows = 380;
        final int max = 78;
        final int N = cols;
        final OutputStream os = response.getOutputStream();
        final PngWriter png = new PngWriter(os, new ImageInfo(cols, rows, 8, false));
        png.setFilterType(PngFilterType.FILTER_NONE);
        final ImageLine iline1 = new ImageLine(png.imgInfo);
        final double xc = Double.parseDouble("-0.5");
        final double yc = Double.parseDouble("0");
        final double size = Double.parseDouble("2");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final double x0 = xc - size / 2 + size * i / N;
                double y0 = yc - size / 2 + size * j / N;
                Complex z0 = new Complex(x0, y0);
                final int grayx = (max - mandelbrot(z0, max));
                final int gray = (int) Math.min(Math.floor(grayx * 2.0), 254);
                ImageLineHelper.setPixelRGB8(iline1, j, gray, gray, gray);
            }
            iline1.setRown(i);
            png.writeRow(iline1);
        }
        png.end();
    }
}
