package org.neblipedia.imagen;

import java.io.File;
import org.neblipedia.imagen.svg.SvgBatik;
import org.neblipedia.imagen.svg.SvgImageMagick;
import org.neblipedia.imagen.svg.SvgInkscape;
import org.neblipedia.imagen.svg.SvgRsvg;
import org.neblipedia.wiki.config.WikiParserConfig;
import org.neblipedia.wiki.config.WikiParserConfig.SVG_AP;

public abstract class Svg2Png {

    public static File svg2png(File tmp, File imagenOriginal, int ancho, WikiParserConfig conf) {
        String nombre = imagenOriginal.getName();
        nombre = nombre.substring(0, nombre.lastIndexOf('.'));
        nombre = ancho + "-" + nombre + ".png";
        File imagenProcesada = new File(tmp, nombre);
        if (!imagenProcesada.exists()) {
            try {
                if (conf.getSvgAplicacion() == SVG_AP.BATIK) {
                    System.out.println("batik");
                    new SvgBatik(imagenOriginal, imagenProcesada, ancho);
                } else if (conf.getSvgAplicacion() == SVG_AP.RSVG) {
                    System.out.println("rsvg");
                    new SvgRsvg(imagenOriginal, imagenProcesada, ancho);
                } else if (conf.getSvgAplicacion() == SVG_AP.IMAGEMAGICK) {
                    System.out.println("imagemagick");
                    new SvgImageMagick(conf.getSvgEjecutable(SVG_AP.IMAGEMAGICK), imagenOriginal, imagenProcesada, ancho);
                } else if (conf.getSvgAplicacion() == SVG_AP.INKSCAPE) {
                    System.out.println("inkscape");
                    new SvgInkscape(conf.getSvgEjecutable(SVG_AP.INKSCAPE), imagenOriginal, imagenProcesada, ancho);
                }
                if (imagenProcesada.exists()) {
                    return imagenProcesada;
                }
            } catch (Exception e) {
                imagenProcesada.delete();
                return null;
            }
        } else {
            return imagenProcesada;
        }
        return null;
    }

    public Svg2Png(File svginput, File pngoutput, int ancho) {
    }

    public Svg2Png(String ejecutable, File svginput, File pngoutput, int ancho) {
    }
}
