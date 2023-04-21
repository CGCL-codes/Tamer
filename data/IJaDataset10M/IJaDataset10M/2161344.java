package net.sf.remilama.web.action;

import java.util.List;
import javax.annotation.Resource;
import net.sf.remilama.service.ReviewStatisticsService;
import net.sf.remilama.web.form.ReviewStatisticsForm;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.DoubleConversionUtil;
import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class ReviewStatisticsAction {

    @Resource
    @ActionForm
    protected ReviewStatisticsForm reviewStatisticsForm;

    @Resource
    protected ReviewStatisticsService reviewStatisticsService;

    public List<BeanMap> statisticsList;

    @Execute(validator = false)
    public String index() {
        statisticsList = reviewStatisticsService.findReview();
        for (BeanMap statistics : statisticsList) {
            double time = DoubleConversionUtil.toDouble(statistics.get("reviewTime"));
            double numOfPages = DoubleConversionUtil.toDouble(statistics.get("numOfPages"));
            double commentNum = DoubleConversionUtil.toDouble(statistics.get("commentNum"));
            statistics.put("timeDensity", time / numOfPages);
            statistics.put("commentDensity", commentNum / numOfPages);
        }
        return "index.jsp";
    }
}
