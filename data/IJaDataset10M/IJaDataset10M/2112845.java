package equilibrium.commons.report.export;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PNGReportExporter extends AbstractReportExporter {

    private static final Log logger = LogFactory.getLog(PNGReportExporter.class);

    private float pageToImageZoom = 1.28f;

    public PNGReportExporter(boolean moveTableOfContent) {
        super(moveTableOfContent);
    }

    public PNGReportExporter(boolean shouldContentTablePagesBeMoved, int precedingPagesCount) {
        super(shouldContentTablePagesBeMoved, precedingPagesCount);
    }

    protected void exportReportSpecificaly(JasperPrint jasperPrint, File file) {
        logger.info("Exporting report to the PNG file");
        List pages = jasperPrint.getPages();
        int pageNumber = pages.size();
        Image singlePageImage = null;
        String fileName;
        String folderName = file.getName();
        String folderPath = file.getPath();
        for (int i = 0; i < pageNumber; ++i) {
            try {
                singlePageImage = JasperPrintManager.printPageToImage(jasperPrint, i, pageToImageZoom);
            } catch (JRException e) {
                logger.fatal("Cannot export report page to image.", e);
                throw new ReportExporterException("Cannot export report page to image.", e);
            }
            fileName = prepareFileName(folderName, i, pageNumber);
            saveImageToFile(singlePageImage, folderPath, fileName);
        }
    }

    protected String prepareFileName(String fileName, int pageIndex, int pageNumber) {
        int numberOfDigits = countFileNameNumberLength(pageNumber);
        String imageFileName = new String(fileName);
        String format = "%0" + numberOfDigits + "d";
        String formatedPageNumber = String.format(format, pageIndex);
        imageFileName += "_" + formatedPageNumber + ".png";
        return imageFileName;
    }

    protected int countFileNameNumberLength(int pageNumber) {
        int temp = pageNumber;
        int digits = 0;
        while ((temp % 10) > 0 || (temp >= 10)) {
            ++digits;
            temp = temp / 10;
        }
        return digits;
    }

    private void saveImageToFile(Image singlePageImage, String folderName, String fileName) {
        logger.debug("Saving page to file: '" + folderName + File.separator + fileName);
        File parentDirectory = new File(folderName);
        parentDirectory.mkdir();
        File imageFile = new File(folderName + File.separator + fileName);
        try {
            ImageIO.write((RenderedImage) singlePageImage, "png", imageFile);
        } catch (IOException e) {
            logger.fatal("Cannot export report page image into PNG file", e);
            throw new ReportExporterException("Cannot export report page image into PNG file", e);
        }
    }

    public float getZoom() {
        return pageToImageZoom;
    }

    public void setZoom(float zoom) {
        this.pageToImageZoom = zoom;
    }
}
