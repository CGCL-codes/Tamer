package uk.ac.essex.imageview.model;

import uk.ac.essex.common.gui.impl.ApplicationFileImpl;
import javax.media.jai.Histogram;
import java.awt.image.RenderedImage;
import java.io.File;

/**
 * <br>
 * Created Date: 20-Dec-2003<br>
 * <p/>
 * You should have received a copy of Lesser GNU public license with this code.
 * If not please visit <a href="http://www.gnu.org/copyleft/lesser.html">this site </a>
 * 
 * @author Laurence Smith
 */
public class ImageFileImpl extends ApplicationFileImpl implements ImageModel {

    RenderedImage renderedImage;

    Histogram histogram;

    /** Has the histogram been computed ?  */
    boolean hasHistogram;

    public ImageFileImpl() {
    }

    public ImageFileImpl(RenderedImage renderedImage) {
        this.renderedImage = renderedImage;
        setData(renderedImage);
    }

    public ImageFileImpl(RenderedImage renderedImage, File file) {
        super(file);
        this.renderedImage = renderedImage;
        setData(renderedImage);
    }

    public RenderedImage getRenderedImage() {
        return renderedImage;
    }

    public void setRenderedImage(RenderedImage renderedImage) {
        this.renderedImage = renderedImage;
        setData(renderedImage);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
    }

    public boolean hasHistogram() {
        return hasHistogram;
    }
}
