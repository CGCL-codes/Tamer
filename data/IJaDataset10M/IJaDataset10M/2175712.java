package configuration.report;

import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.annotations.Range;
import org.ytoh.configurations.ui.CheckBox;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Oct 12, 2009
 * Time: 11:58:47 AM
 * To change this template use File | Settings | File Templates.
 */
@Component(name = "Crossvalidation subreport", description = "Crossvalidation subreport configuration")
public class CrossValidationSROldConfig extends BaseSRConfig {

    @Property(name = "Include images", description = "Include/exclude images")
    @CheckBox
    private boolean images = true;

    public boolean isImages() {
        return images;
    }

    public void setImages(boolean images) {
        this.images = images;
    }

    @Property(name = "Boxplot width", description = "Boxplot width in pixels")
    @Range(from = 1, to = 1600)
    private int boxPlotWidth = 400;

    public int getBoxPlotWidth() {
        return boxPlotWidth;
    }

    public void setBoxPlotWidth(int boxPlotWidth) {
        this.boxPlotWidth = boxPlotWidth;
    }

    @Property(name = "Boxplot height", description = "Boxplot height in pixels")
    @Range(from = 1, to = 1600)
    private int boxPlotHeight = 300;

    public int getBoxPlotHeight() {
        return boxPlotHeight;
    }

    public void setBoxPlotHeight(int boxPlotHeight) {
        this.boxPlotHeight = boxPlotHeight;
    }
}
