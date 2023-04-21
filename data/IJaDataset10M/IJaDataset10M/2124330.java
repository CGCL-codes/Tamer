package org.kablink.teaming.taglib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.portlet.RenderRequest;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.LinkedMap;
import org.kablink.util.servlet.StringServletResponse;
import com.sitescape.team.domain.Statistics;

public class StatisticTag extends BodyTagSupport {

    public static final String ColoredBar = "coloredBar";

    private Map statistic;

    private String style = ColoredBar;

    private Boolean showLabel = true;

    private Boolean labelAll = false;

    private Boolean showLegend = true;

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException {
        try {
            HttpServletRequest httpReq = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse httpRes = (HttpServletResponse) pageContext.getResponse();
            if (this.statistic.get(Statistics.VALUES_LIST) != null) {
                Integer internalTotal = new Integer(0);
                List statisticValuesList = (List) this.statistic.get(Statistics.VALUES_LIST);
                Map statisticValues = (Map) this.statistic.get(Statistics.VALUES);
                Map statisticLabels = (Map) this.statistic.get(Statistics.CAPTIONS);
                Integer total = (Integer) this.statistic.get(Statistics.TOTAL_KEY);
                String label = (String) this.statistic.get(Statistics.ATTRIBUTE_CAPTION);
                Iterator<Integer> iterator = statisticValues.values().iterator();
                while (iterator.hasNext()) {
                    Integer value = iterator.next();
                    if (value != null && value > 0) {
                        internalTotal += value;
                    }
                }
                OrderedMap percentStatistic = new LinkedMap();
                if (internalTotal > 0) {
                    Iterator it = statisticValuesList.iterator();
                    while (it.hasNext()) {
                        Object key = it.next();
                        if (key != null && statisticValues.get(key) != null) {
                            OrderedMap attrValues = new LinkedMap();
                            attrValues.put("percent", ((Integer) statisticValues.get(key) * 100) / internalTotal);
                            attrValues.put("value", (Integer) statisticValues.get(key));
                            attrValues.put("total", total);
                            if (statisticLabels != null) {
                                attrValues.put("label", statisticLabels.get(key));
                            }
                            percentStatistic.put(key, attrValues);
                        }
                    }
                }
                httpReq.setAttribute("percentStatistic", percentStatistic);
                httpReq.setAttribute("statisticLabel", label);
            }
            httpReq.setAttribute("showLabel", this.showLabel);
            httpReq.setAttribute("showLegend", this.showLegend);
            httpReq.setAttribute("labelAll", this.labelAll);
            httpReq.setAttribute("barStyle", this.style);
            String jsp = "/WEB-INF/jsp/tag_jsps/charts/statistic.jsp";
            RequestDispatcher rd = httpReq.getRequestDispatcher(jsp);
            ServletRequest req = pageContext.getRequest();
            StringServletResponse res = new StringServletResponse(httpRes);
            rd.include(req, res);
            pageContext.getOut().print(res.getString().trim());
        } catch (Exception e) {
            throw new JspTagException(e.getLocalizedMessage());
        } finally {
            this.statistic = null;
            this.style = ColoredBar;
            this.showLabel = true;
            this.labelAll = false;
            this.showLegend = true;
        }
        return EVAL_PAGE;
    }

    public void setStatistic(Map statistic) {
        this.statistic = statistic;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setShowLabel(Boolean showLabel) {
        this.showLabel = showLabel;
    }

    public void setShowLegend(Boolean showLegend) {
        this.showLegend = showLegend;
    }

    public void setLabelAll(Boolean labelAll) {
        this.labelAll = labelAll;
    }
}
