package papertoolkit.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import javax.media.jai.TiledImage;
import org.jibble.epsgraphics.EpsGraphics2D;
import papertoolkit.paper.Region;
import papertoolkit.paper.Sheet;
import papertoolkit.pattern.TiledPattern;
import papertoolkit.pattern.TiledPatternGenerator;
import papertoolkit.pattern.coordinates.PatternToSheetMapping;
import papertoolkit.pattern.coordinates.conversion.TiledPatternCoordinateConverter;
import papertoolkit.pattern.output.PDFPatternGenerator;
import papertoolkit.pattern.output.PostscriptPatternGenerator;
import papertoolkit.units.Pixels;
import papertoolkit.units.Points;
import papertoolkit.units.Units;
import papertoolkit.units.coordinates.Coordinates;
import papertoolkit.util.DebugUtils;
import papertoolkit.util.MathUtils;
import papertoolkit.util.files.FileUtils;
import papertoolkit.util.graphics.GraphicsUtils;
import papertoolkit.util.graphics.ImageUtils;
import papertoolkit.util.graphics.JAIUtils;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * <p>
 * This class will render a Sheet into a JPEG, PDF, or Java2D graphics context.
 * </p>
 * <p>
 * For individual regions, it will use specific region renderers (e.g., ImageRenderer, PolygonRenderer, and
 * TextRenderer).
 * </p>
 * <p>
 * <span class="BSDLicense"> This software is distributed under the <a
 * href="http://hci.stanford.edu/research/copyright.txt">BSD License</a>. </span>
 * </p>
 * 
 * @author <a href="http://graphics.stanford.edu/~ronyeh">Ron B Yeh</a> (ronyeh(AT)cs.stanford.edu)
 */
public class SheetRenderer {

    /**
	 * Generates pattern for this sheet.
	 */
    private TiledPatternGenerator generator;

    /**
	 * Allows us to save the pattern info to the same directory as the most recently rendered pdf or ps.
	 */
    private File mostRecentlyRenderedFile;

    /**
	 * What color should we render the pattern in?
	 */
    private Color patternColor = Color.BLACK;

    /**
	 * You can make the pattern bigger or smaller depending on your printer... 0 == default. - --> smaller, +
	 * --> bigger. Each unit corresponds to two font points.
	 */
    private int patternDotSizeAdjustment = 0;

    /**
	 * Populate this only when we render the pattern (renderToPDF). After we render to pdf, we can save the
	 * information to a file, for so that we can run the application in the future without rendering more
	 * pattern.
	 */
    private PatternToSheetMapping patternInformation;

    /**
	 * By default, any active regions will be overlaid with pattern (unique to at least this sheet, unless
	 * otherwise specified).
	 */
    protected boolean renderActiveRegionsWithPattern = true;

    /**
	 * The sheet we are to render.
	 */
    protected Sheet sheet;

    /**
	 * Create a new TiledPatternGenerator for this Sheet.
	 * 
	 * @param s
	 */
    public SheetRenderer(Sheet s) {
        this(s, new TiledPatternGenerator());
    }

    /**
	 * Feel free to share TiledPatternGenerator between Sheets. That way, you can get unique pattern across
	 * multiple Sheets.
	 * 
	 * @param s
	 * @param patternGenerator
	 */
    public SheetRenderer(Sheet s, TiledPatternGenerator patternGenerator) {
        sheet = s;
        patternInformation = sheet.getPatternToSheetMapping();
        generator = patternGenerator;
    }

    /**
	 * @return
	 */
    public PatternToSheetMapping getPatternInformation() {
        return patternInformation;
    }

    /**
	 * We will render pattern when outputting PDFs. Rendering pattern to screen is a waste of time, since dots
	 * are not resolvable on screen. Perhaps for screen display (i.e., anything < 600 dpi), we should render
	 * pattern as a faint dotted overlay?
	 * 
	 * WARNING: Does not work for multiple sheets, as we will get the same pattern....
	 * 
	 * @param cb
	 *            a content layer returned by iText
	 * 
	 */
    private void renderPatternToPDF(PdfContentByte cb) {
        final List<Region> regions = sheet.getRegions();
        final PDFPatternGenerator pgen = new PDFPatternGenerator(cb, sheet.getWidth(), sheet.getHeight());
        pgen.setPatternColor(patternColor);
        pgen.adjustPatternSize(patternDotSizeAdjustment);
        for (Region r : regions) {
            if (!r.isActive()) {
                continue;
            }
            Coordinates regionOffset = sheet.getRegionOffset(r);
            final Units scaledWidth = r.getWidth();
            final Units scaledHeight = r.getHeight();
            final TiledPattern pattern = generator.getPattern(scaledWidth, scaledHeight);
            pgen.renderPattern(pattern, Units.add(r.getOriginX(), regionOffset.getX()), Units.add(r.getOriginY(), regionOffset.getY()));
            final TiledPatternCoordinateConverter tiledPatternInRegion = (TiledPatternCoordinateConverter) patternInformation.getPatternBoundsOfRegion(r);
            tiledPatternInRegion.setPatternInformationByReadingItFrom(pattern);
        }
    }

    /**
	 * @param file
	 */
    private String renderPatternToPostScript() {
        Units width = sheet.getWidth();
        Units height = sheet.getHeight();
        final PostscriptPatternGenerator pgen = new PostscriptPatternGenerator(width, height, generator);
        final List<Region> regions = sheet.getRegions();
        for (Region r : regions) {
            final TiledPatternCoordinateConverter patternCoordinateConverter = (TiledPatternCoordinateConverter) patternInformation.getPatternBoundsOfRegion(r);
            Coordinates regionLocation = sheet.getRegionLocationRelativeToSheet(r);
            if (patternCoordinateConverter == null) {
                DebugUtils.println("Null Converter. Is this region not active? " + r);
                continue;
            }
            patternCoordinateConverter.setPatternInformationByReadingItFrom(pgen.getPattern(), regionLocation, r.getWidth(), r.getHeight());
        }
        return pgen.getPostscriptPattern();
    }

    /**
	 * We assume the g2d is big enough for us to draw this Sheet to.
	 * 
	 * By default, the transforms works at 72 dots per inch. Scale the transform beforehand if you would like
	 * better or worse rendering.
	 * 
	 * This only renders the regions and region content, but not the pattern.
	 * 
	 * @param g2d
	 */
    public void renderToG2D(Graphics2D g2d) {
        g2d.setRenderingHints(GraphicsUtils.getBestRenderingHints());
        final List<Region> regions = sheet.getRegions();
        for (Region r : regions) {
            final AffineTransform currTransform = new AffineTransform(g2d.getTransform());
            final Coordinates regionOffset = sheet.getRegionOffset(r);
            final double xOffsetPts = regionOffset.getX().getValueInPoints();
            final double yOffsetPts = regionOffset.getY().getValueInPoints();
            g2d.translate((int) xOffsetPts, (int) yOffsetPts);
            r.getRenderer().renderToG2D(g2d);
            g2d.setTransform(currTransform);
        }
    }

    /**
	 * Use the default pixels per inch. Specified in our configuration file.
	 * 
	 * @param file
	 */
    public void renderToJPEG(File file) {
        renderToJPEG(file, Pixels.ONE);
    }

    /**
	 * @param destJPEGFile
	 * @param destUnits
	 *            Converts the graphics2D object into a new coordinate space based on the destination units'
	 *            pixels per inch. This is for the purposes of rendering the document to screen, where
	 *            Graphics2D's default 72ppi isn't always the right way to do it.
	 */
    public void renderToJPEG(File destJPEGFile, Pixels destUnits) {
        final Units width = sheet.getWidth();
        final Units height = sheet.getHeight();
        final double scale = Points.ONE.getScalarMultipleToConvertTo(destUnits);
        final int w = MathUtils.rint(width.getValueIn(destUnits));
        final int h = MathUtils.rint(height.getValueIn(destUnits));
        final TiledImage image = JAIUtils.createWritableBufferWithoutAlpha(w, h);
        final Graphics2D graphics2D = image.createGraphics();
        graphics2D.setRenderingHints(GraphicsUtils.getBestRenderingHints());
        graphics2D.setTransform(AffineTransform.getScaleInstance(scale, scale));
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, w, h);
        renderToG2D(graphics2D);
        graphics2D.dispose();
        ImageUtils.writeImageToJPEG(image.getAsBufferedImage(), destJPEGFile);
    }

    /**
	 * Uses the iText package to render a PDF file from scratch. iText is nice because we can write to a
	 * Graphics2D context. Alternatively, we can use PDF-like commands.
	 * 
	 * @param destPDFFile
	 */
    public void renderToPDF(File destPDFFile) {
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(destPDFFile);
            final Rectangle pageSize = new Rectangle(0, 0, (int) Math.round(sheet.getWidth().getValueInPoints()), (int) Math.round(sheet.getHeight().getValueInPoints()));
            final Document doc = new Document(pageSize, 0, 0, 0, 0);
            final PdfWriter writer = PdfWriter.getInstance(doc, fileOutputStream);
            doc.open();
            final PdfContentByte topLayer = writer.getDirectContent();
            final PdfContentByte bottomLayer = writer.getDirectContentUnder();
            renderToPDFContentLayers(destPDFFile, topLayer, bottomLayer);
            doc.close();
            savePatternInformation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param destPDFFile
	 * @param topLayer
	 * @param bottomLayer
	 */
    protected void renderToPDFContentLayers(File destPDFFile, PdfContentByte topLayer, PdfContentByte bottomLayer) {
        mostRecentlyRenderedFile = destPDFFile;
        final Units width = sheet.getWidth();
        final Units height = sheet.getHeight();
        final float wPoints = (float) width.getValueInPoints();
        final float hPoints = (float) height.getValueInPoints();
        final Graphics2D g2dOver = topLayer.createGraphicsShapes(wPoints, hPoints);
        renderToG2D(g2dOver);
        g2dOver.dispose();
        if (renderActiveRegionsWithPattern) {
            renderPatternToPDF(topLayer);
        }
    }

    /**
	 * Uses Jibble and some EPS Hacking to create a PostScript file!
	 * 
	 * @param file
	 */
    public void renderToPostScript(File file) {
        mostRecentlyRenderedFile = file;
        final EpsGraphics2D g2d = new EpsGraphics2D("PostScript Render");
        g2d.setStroke(new BasicStroke(0.1f));
        g2d.drawRect(0, 0, (int) sheet.getWidth().getValueInPoints(), (int) sheet.getHeight().getValueInPoints());
        renderToG2D(g2d);
        String graphicsPostscript = g2d.toString();
        if (renderActiveRegionsWithPattern) {
            String patternPostscript = renderPatternToPostScript();
            graphicsPostscript = graphicsPostscript.replaceAll("(?s)%.*EndComments", "");
            graphicsPostscript = graphicsPostscript.replaceAll("(?s)showpage.*EOF", "");
            String output = patternPostscript.replace("__INSERT_SHEET_POSTSCRIPT_HERE__", graphicsPostscript);
            FileUtils.writeStringToFile(output, file);
        } else {
            FileUtils.writeStringToFile(graphicsPostscript, file);
        }
        savePatternInformation();
    }

    /**
	 * This saves an xml file with the same name/path, but different extension as the most-recently rendered
	 * PDF file.
	 */
    public void savePatternInformation() {
        if (mostRecentlyRenderedFile == null) {
            System.err.println("SheetRenderer: We cannot save the pattern information " + "without a destination file. Please render a PDF or PS first " + "so we know where to put the pattern configuration file!");
        } else {
            File parentDir = mostRecentlyRenderedFile.getParentFile();
            String fileName = mostRecentlyRenderedFile.getName();
            if (fileName.contains(".pdf")) {
                fileName = fileName.replace(".pdf", ".patternInfo.xml");
            } else if (fileName.contains(".ps")) {
                fileName = fileName.replace(".ps", ".patternInfo.xml");
            } else {
                fileName = fileName + ".patternInfo.xml";
            }
            File destFile = new File(parentDir, fileName);
            savePatternInformation(destFile);
        }
    }

    /**
	 * After Rendering Pattern, we now know the particulars of the pattern coordinates for each region. Save
	 * that information to disk.
	 * 
	 * @param patternInfoFile
	 */
    public void savePatternInformation(File patternInfoFile) {
        patternInformation.saveConfigurationToXML(patternInfoFile);
    }

    /**
	 * This is really just for debugging, as you want BLACK pattern in general.
	 * 
	 * @param pColor
	 */
    public void setPatternColor(Color pColor) {
        patternColor = pColor;
    }

    /**
	 * Useful for when rendering many sheets at a time. This can guarantee that the pattern is unique across
	 * sheets. If you want to reset the pattern, or pick a particular sheet, you may, by interacting with the
	 * generator object.
	 * 
	 * @param tiledPatternGenerator
	 */
    public void setPatternGenerator(TiledPatternGenerator tiledPatternGenerator) {
        generator = tiledPatternGenerator;
    }

    /**
	 * Used for debugging. If you set this to false, then we will not render any pattern at all.
	 * 
	 * @param activeWithPattern
	 */
    public void setRenderActiveRegionsWithPattern(boolean activeWithPattern) {
        renderActiveRegionsWithPattern = activeWithPattern;
    }

    /**
	 * Call this one or more times before rendering. It's a hint to the renderer.
	 */
    public void useLargerPatternDots() {
        patternDotSizeAdjustment++;
    }

    /**
	 * Call this one or more times before rendering. It's a hint to the renderer.
	 */
    public void useSmallerPatternDots() {
        patternDotSizeAdjustment--;
    }
}
