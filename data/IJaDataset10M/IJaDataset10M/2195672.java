package au.gov.nla.aons.mvc.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;

public class FormatReviewXYToolTipGenerator implements XYToolTipGenerator {

    private String dateFormat = "yyyy/MM/dd hh:ss Z";

    private SimpleDateFormat sdf;

    private List<Calendar> reviewDates;

    public void initialize() {
        sdf = new SimpleDateFormat(dateFormat);
    }

    public String generateToolTip(XYDataset dataset, int series, int item) {
        String dateStr = sdf.format(reviewDates.get(item).getTime());
        if (series == 0) {
            Number value = dataset.getY(series, item);
            return "Community Risk Assessment [" + value + "] performed on " + dateStr;
        } else if (series == 1) {
            Number value = dataset.getY(series, item);
            return "Local Risk Assessment [" + value + "] performed on " + dateStr;
        } else if (series == 2) {
            Number value = dataset.getY(series, item);
            return "Final Risk Assessment [" + value + "] performed on " + dateStr;
        }
        return null;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<Calendar> getReviewDates() {
        return reviewDates;
    }

    public void setReviewDates(List<Calendar> reviewDates) {
        this.reviewDates = reviewDates;
    }
}
