package com.gestioni.adoc.aps.system.services.itext;

import java.io.File;
import java.io.FileOutputStream;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.AbstractService;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.gestioni.adoc.aps.system.AdocSystemConstants;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BarcodePDF417;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class ITextManager extends AbstractService implements IiTextManager {

    @Override
    public void init() throws Exception {
        ApsSystemUtils.getLogger().info(this.getClass().getName() + " ready");
    }

    @Override
    public String stringToPdf(String segnatura) throws Exception {
        String pathFileOut = null;
        IMOperation all = new IMOperation();
        try {
            all.addImage();
            all.gravity("South");
            all.annotate(null, null, 0, 4, segnatura);
            all.addImage();
            pathFileOut = this.creoPathOut("segnatura", ".png");
            String[] images = new String[] { this.getResourceDiskTempRootFolder() + "segnatura/segnatura_null.png", pathFileOut };
            ConvertCmd convert = new ConvertCmd();
            convert.run(all, (Object[]) images);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "stringToPdf");
            throw new ApsSystemException("Error in stringToPdf ", t);
        }
        return pathFileOut;
    }

    public String barcode128ToPdf(String segnatura, String pathInputFile) throws Exception {
        String pathFileOut = null;
        try {
            pathFileOut = this.getResourceDiskTempRootFolder() + "segnatura" + System.currentTimeMillis() + "_barcode128." + AdocSystemConstants.FILE_EXTENSION_PDF;
            PdfReader reader = new PdfReader(pathInputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathFileOut));
            PdfContentByte over = stamper.getOverContent(1);
            Barcode128 code128 = new Barcode128();
            code128.setCode(segnatura);
            code128.setStartStopText(false);
            Image img = code128.createImageWithBarcode(over, null, null);
            img.setAbsolutePosition(20, 0);
            over.addImage(img);
            stamper.close();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "barcode128ToPdf");
            throw new ApsSystemException("Error in barcode128ToPdf ", t);
        }
        return pathFileOut;
    }

    public String pdf417ToPdf(String segnatura, String pathInputFile) throws Exception {
        String pathFileOut = null;
        try {
            pathFileOut = this.getResourceDiskTempRootFolder() + "segnatura" + System.currentTimeMillis() + "_pdf417." + AdocSystemConstants.FILE_EXTENSION_PDF;
            PdfReader reader = new PdfReader(pathInputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathFileOut));
            PdfContentByte over = stamper.getOverContent(1);
            BarcodePDF417 pdf417 = new BarcodePDF417();
            pdf417.setText(segnatura);
            Image img = pdf417.getImage();
            img.scalePercent(150);
            img.setAbsolutePosition(40, 10);
            over.addImage(img);
            stamper.close();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "pdf417ToPdf");
            throw new ApsSystemException("Error in pdf417ToPdf ", t);
        }
        return pathFileOut;
    }

    public String classicHorSignatureToPdf(String segnatura, String pathInputFile) throws Exception {
        String pathFileOut = null;
        try {
            pathFileOut = this.getResourceDiskTempRootFolder() + "segnatura" + System.currentTimeMillis() + "_classic." + AdocSystemConstants.FILE_EXTENSION_PDF;
            PdfReader reader = new PdfReader(pathInputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathFileOut));
            PdfContentByte over = stamper.getOverContent(1);
            Font font = new Font(Font.NORMAL, 12);
            BaseFont bf = font.getCalculatedBaseFont(false);
            over.setFontAndSize(bf, 13);
            over.setTextMatrix(10, 10);
            over.showText(segnatura);
            stamper.close();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "classicHorSignatureToPdf");
            throw new ApsSystemException("Error in classicHorSignatureToPdf ", t);
        }
        return pathFileOut;
    }

    private String creoPathOut(String name, String fileType) {
        return this.getResourceDiskTempRootFolder() + name + System.currentTimeMillis() + fileType;
    }

    @Override
    public String waterMark(String pathFileIn, String segnatura) throws Exception {
        String pathFileOut = null;
        String pathOut;
        File file = new File(pathFileIn);
        try {
            if (!file.exists()) throw new ApsSystemException("File " + file.getPath() + "doesn't exist");
            PdfReader reader;
            reader = new PdfReader(file.getPath());
            pathOut = this.stringToPdf(segnatura);
            Image img = Image.getInstance(pathOut);
            pathFileOut = this.creoPathOut("watermarked", "." + AdocSystemConstants.FILE_EXTENSION_PDF);
            File watermarked = new File(pathFileOut);
            PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(watermarked));
            PdfContentByte under;
            img.setAbsolutePosition(0, 0);
            under = stamp.getUnderContent(1);
            under.addImage(img);
            stamp.close();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "waterMark");
            throw new ApsSystemException("Error creating waterMark ", t);
        }
        return pathFileOut;
    }

    /**
	 * @param resourceDiskTempRootFolder the resourceDiskTempRootFolder to set
	 */
    public void setResourceDiskTempRootFolder(String resourceDiskTempRootFolder) {
        this._resourceDiskTempRootFolder = resourceDiskTempRootFolder;
    }

    /**
	 * @return the resourceDiskTempRootFolder
	 */
    protected String getResourceDiskTempRootFolder() {
        return _resourceDiskTempRootFolder;
    }

    /**
	 * @param configManager the configManager to set
	 */
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }

    /**
	 * @return the configManager
	 */
    public ConfigInterface getConfigManager() {
        return _configManager;
    }

    private ConfigInterface _configManager;

    private String _resourceDiskTempRootFolder;
}
