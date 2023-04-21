package com.googlecode.fivehundred.svg;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;

/**
 * Creates a raster image from an SVG document.
 * 
 * @author Nathan C Jones
 */
public class SVGRasterizer {

    /**
	 * Create a rasterised image from the SVG file at the given URL.
	 * 
	 * @param svgURL the location of the SVG file to rasterize.
	 * @param width the size of the image to generate from the SVG.
	 * @return an image generated from SVG with the given width.
	 * @throws SVGRasteriserException if the SVG image could not be rasterized.
	 */
    public Image createImage(URL svgURL, float width) {
        TranscoderInput input = new TranscoderInput(svgURL.toString());
        Rasterizer r = new Rasterizer();
        r.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(width));
        try {
            r.transcode(input, null);
        } catch (TranscoderException e) {
            throw new SVGRasteriserException(e);
        }
        return r.raster;
    }

    /**
	 * An image transcoder that stores the resulting image.
	 */
    private static class Rasterizer extends ImageTranscoder {

        private Image raster;

        public BufferedImage createImage(int w, int h) {
            return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }

        public void writeImage(BufferedImage img, TranscoderOutput output) throws TranscoderException {
            raster = img;
        }
    }
}
