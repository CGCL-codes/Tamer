package uk.ac.essex.ia.imageview;

import org.apache.log4j.Category;
import uk.ac.essex.common.gui.ApplicationFileImpl;
import uk.ac.essex.common.lang.LanguageManager;
import uk.ac.essex.ia.lang.LanguageConstants;
import uk.ac.essex.ia.lang.LanguageManagerImpl;
import uk.ac.essex.ia.media.HistogramOp;
import javax.media.jai.Histogram;
import java.awt.image.RenderedImage;
import java.io.File;

/**
 * An Image file wraps all the data about an open image in {@link ImageViewer}.
 *
 * @author Laurence Smith
 * @see ImageData
 *
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class ImageFile extends ApplicationFileImpl<ImageData> {

    /** The log4j logger */
    private Category category = Category.getInstance(ImageFile.class);

    private boolean loadHistogram = true, loadStatistics = false;

    private LanguageManager translator;

    /**
     * This takes a rendered image and (WILL) consult the pref manager
     * as to whether to load the statistics and the histogram.
     */
    public ImageFile(RenderedImage renderedImage) {
        ImageData imageData = new ImageData(renderedImage);
        if (loadHistogram) {
            category.debug("Loading histogram...");
            imageData.setHistogram(HistogramOp.getHistogram(renderedImage));
        }
        if (loadStatistics) {
            category.debug("Loading stats...");
        }
        init();
        setModified(true);
        setData(imageData);
    }

    /**
     *
     */
    public ImageFile(RenderedImage renderedImage, File file) {
        this(renderedImage);
        setFile(file);
        setModified(false);
    }

    /**
     * Initialise the data
     *
     * @param renderedImage - The source image data
     * @param histogram - The images histogram
     * @param file - The file associated with the image
     * @see #setFile
     * @see #setHistogram
     */
    public ImageFile(RenderedImage renderedImage, Histogram histogram, File file) {
        this.file = file;
        ImageData imageData = new ImageData(renderedImage);
        imageData.setHistogram(histogram);
        setData(imageData);
        init();
    }

    /** @param histogram - The images histogram */
    public void setHistogram(Histogram histogram) {
        getData().setHistogram(histogram);
    }

    /** @return the image data */
    public RenderedImage getImage() {
        return getData().getSource();
    }

    /** @return the images histogram */
    public Histogram getHistogram() {
        return getData().getHistogram();
    }

    /** */
    public boolean hasHistogram() {
        return getData().hasHistogram();
    }

    public String getStatistics() {
        return getData().getStatistics();
    }

    /**
     *
     */
    private void init() {
        translator = LanguageManagerImpl.getInstance();
        name = translator.get(LanguageConstants.UNTITLED);
    }
}
