package net.sf.ghost4j.example;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.sf.ghost4j.Ghostscript;
import net.sf.ghost4j.GhostscriptException;
import net.sf.ghost4j.display.ImageWriterDisplayCallback;

/**
 * Example showing how to setup a display callback (to interract with the display) for the Ghostscript interpreter.
 * In this example, a simple ImageWriterDisplayCallback is used as callback: it converts page rasters into images and stores them.
 * @author Gilles Grousset (gi.grousset@gmail.com)
 */
public class DisplayCallbackExample {

    public static void main(String[] args) {
        Ghostscript gs = Ghostscript.getInstance();
        ImageWriterDisplayCallback displayCallback = new ImageWriterDisplayCallback();
        gs.setDisplayCallback(displayCallback);
        String[] gsArgs = new String[7];
        gsArgs[0] = "-dQUIET";
        gsArgs[1] = "-dNOPAUSE";
        gsArgs[2] = "-dBATCH";
        gsArgs[3] = "-dSAFER";
        gsArgs[4] = "-sDEVICE=display";
        gsArgs[5] = "-sDisplayHandle=0";
        gsArgs[6] = "-dDisplayFormat=16#804";
        try {
            gs.initialize(gsArgs);
            gs.runFile("input.ps");
            gs.exit();
        } catch (GhostscriptException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        try {
            for (int i = 0; i < displayCallback.getImages().size(); i++) {
                ImageIO.write((RenderedImage) displayCallback.getImages().get(i), "png", new File((i + 1) + ".png"));
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
