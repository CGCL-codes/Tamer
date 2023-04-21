package beans;

import java.util.TimerTask;
import javax.servlet.ServletContext;

public class SiteMetricSerialize extends TimerTask {

    ServletContext servletcontext;

    SiteMetrics metric;

    public SiteMetricSerialize(ServletContext sc) {
        servletcontext = sc;
    }

    public void run() {
        metric = (SiteMetrics) servletcontext.getAttribute("metrics");
        if (metric == null) System.out.println("tt :  still null"); else System.out.println("tt : ha we have the value : " + metric.HitCount);
    }
}
