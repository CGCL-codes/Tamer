package gpsmate.io.kml;

import gpsmate.geodata.GeoDataContainer;
import gpsmate.io.GpsExporter;
import gpsmate.utils.Configuration;
import gpsmate.utils.FileTool;
import gpsmate.utils.Logger;
import gpsmate.utils.ZipTool;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.zip.ZipOutputStream;
import javax.swing.JDialog;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * KmlExporter
 * 
 * @author raphael.spreitzer, longdistancewalker
 */
public class KmlExporter extends GpsExporter {

    public static final int KML_TYPE = 0;

    public static final int KMZ_TYPE = 1;

    private int fileType;

    private String filename = null;

    private double iconScale;

    private int lineWidth;

    private short lineOpacity;

    private Color lineColor;

    private Properties language = null;

    private Configuration cfg = null;

    public KmlExporter() {
        cfg = Configuration.getInstance();
        language = cfg.getLanguageConfiguration();
        fileType = KML_TYPE;
        filename = cfg.getProperty("export.default.filename");
        try {
            lineOpacity = Short.parseShort(cfg.getProperty("export.exporters.kml.line.opacity"));
        } catch (Throwable e) {
            lineOpacity = 0x7f;
        }
        try {
            short r = Short.parseShort(cfg.getProperty("export.exporters.kml.line.color.red"));
            short g = Short.parseShort(cfg.getProperty("export.exporters.kml.line.color.green"));
            short b = Short.parseShort(cfg.getProperty("export.exporters.kml.line.color.blue"));
            lineColor = new Color(r, g, b);
        } catch (Throwable e) {
            lineColor = Color.blue;
        }
        double stepIconScale = 0.25;
        try {
            stepIconScale = Double.parseDouble(Configuration.getInstance().getProperty("export.exporters.kml.slider.icon.step"));
        } catch (Throwable e) {
        }
        int initial = 4;
        try {
            initial = Integer.parseInt(Configuration.getInstance().getProperty("export.exporters.kml.slider.icon.initial"));
        } catch (Throwable e) {
        }
        iconScale = stepIconScale * initial;
        initial = 2;
        try {
            initial = Integer.parseInt(Configuration.getInstance().getProperty("export.exporters.kml.slider.line.initial"));
        } catch (Throwable e) {
        }
        int stepLineWidth = 1;
        try {
            stepLineWidth = Integer.parseInt(Configuration.getInstance().getProperty("export.exporters.kml.slider.line.step"));
        } catch (Throwable e) {
        }
        lineWidth = initial * stepLineWidth;
    }

    public boolean isKmz() {
        return fileType == KMZ_TYPE;
    }

    /**
   * @return the lineOpacity
   */
    public short getLineOpacity() {
        return lineOpacity;
    }

    /**
   * @param lineOpacity
   *          the lineOpacity to set
   */
    public void setLineOpacity(short lineOpacity) {
        this.lineOpacity = lineOpacity;
    }

    /**
   * @return the lineColor
   */
    public Color getLineColor() {
        return lineColor;
    }

    /**
   * @param lineColor
   *          the lineColor to set
   */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
   * @return the iconScale
   */
    public double getIconScale() {
        return iconScale;
    }

    /**
   * @param iconScale
   *          the iconScale to set
   */
    public void setIconScale(double iconScale) {
        this.iconScale = iconScale;
    }

    /**
   * @return the lineWidth
   */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
   * @param lineWidth
   *          the lineWidth to set
   */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String file) {
        this.filename = file;
    }

    public void setFileType(int id) {
        this.fileType = id;
    }

    @Override
    public boolean export(GeoDataContainer geoData) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            KmlSkeleton kml = new KmlSkeleton(geoData);
            doc.appendChild(kml.toKmlElement(doc, this));
            String prevFileName = filename;
            if (fileType == KML_TYPE && (FileTool.getExtension(filename) == null || !FileTool.getExtension(filename).equalsIgnoreCase("kml"))) filename += ".kml";
            if (fileType == KMZ_TYPE) filename = cfg.getProperty("export.exporters.kml.temp.path") + System.getProperty("file.separator") + "file.kml";
            File tmpDir = new File("tmp");
            if (!tmpDir.exists()) tmpDir.mkdir();
            FileOutputStream fos = new FileOutputStream(filename);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) (doc.getImplementation()).getFeature("LS", "3.0");
            LSSerializer serializer = domImplementationLS.createLSSerializer();
            serializer.setNewLine(System.getProperty("line.separator"));
            LSOutput lsOutput = domImplementationLS.createLSOutput();
            lsOutput.setCharacterStream(writer);
            serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
            serializer.write(doc, lsOutput);
            writer.close();
            fos.close();
            if (fileType == KMZ_TYPE) {
                SymbolExtracter.extractTo(geoData, cfg.getProperty("export.exporters.kml.temp.path"));
                try {
                    if (!"kmz".equalsIgnoreCase(FileTool.getExtension(prevFileName))) prevFileName += ".kmz";
                    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(prevFileName));
                    ZipTool.zipDirectoryContents(cfg.getProperty("export.exporters.kml.temp.path"), zos);
                    zos.close();
                } catch (Throwable e) {
                    Logger.logException(e);
                }
                filename = prevFileName;
                FileTool.cleanDirectory(cfg.getProperty("export.exporters.kml.temp.path"), true);
            }
            return true;
        } catch (ParserConfigurationException e) {
            Logger.logException(e);
        } catch (FileNotFoundException e) {
            Logger.logException(e);
        } catch (IOException e) {
            Logger.logException(e);
        }
        return false;
    }

    @Override
    public String getDescription() {
        return language.getProperty("export.exporters.kml.description");
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public boolean isExportable(int numberOfSelectedTracks, int numberOfSelectedPlacemarks) {
        return numberOfSelectedTracks > 0 || numberOfSelectedPlacemarks > 0;
    }

    @Override
    public JDialog getConfigurationDialog() {
        KmlExportConfigurationDialog d = new KmlExportConfigurationDialog(null, this);
        return d;
    }
}
