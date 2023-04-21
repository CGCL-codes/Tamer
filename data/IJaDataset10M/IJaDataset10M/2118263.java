package net.sourceforge.plantuml.jcckit;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.imageio.ImageIO;
import jcckit.GraphicsPlotCanvas;
import jcckit.data.DataPlot;
import jcckit.util.ConfigParameters;
import jcckit.util.PropertiesBasedConfigData;
import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;

public class PSystemJcckit extends AbstractPSystem {

    private final PropertiesBasedConfigData prop;

    private final int width;

    private final int height;

    public PSystemJcckit(Properties p, int width, int height) {
        this.width = width;
        this.height = height;
        prop = new PropertiesBasedConfigData(p);
    }

    public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ConfigParameters config = new ConfigParameters(prop);
        GraphicsPlotCanvas plotCanvas = new GraphicsPlotCanvas(config, image);
        plotCanvas.connect(DataPlot.create(config));
        plotCanvas.paint();
        ImageIO.write(image, "png", os);
    }

    public String getDescription() {
        return "(JCCKit)";
    }
}
