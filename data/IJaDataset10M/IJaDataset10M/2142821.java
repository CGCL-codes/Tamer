package net.sf.beanreports.jasper2.proxy;

import java.lang.annotation.Annotation;
import net.sf.beanreports.jasper.annotation.ClassReportInfo;
import net.sf.beanreports.jasper.proxy.ClassReportInfoProxy;
import net.sf.beanreports.jasper2.annotation.ReportCenterInfo;
import net.sf.beanreports.jasper2.annotation.ReportBeforeInfo;

@ReportCenterInfo
public class ReportCenterInfoProxy extends BaseReportInfoProxy implements ReportCenterInfo {

    public ReportCenterInfoProxy() {
        this(ReportCenterInfoProxy.class.getAnnotation(ReportCenterInfo.class));
    }

    public ReportCenterInfoProxy(ReportCenterInfo other) {
        this.annotationType = other.annotationType();
        this.value = other.value();
        this.width = other.width();
        this.font = other.font();
        this.fontWeight = other.fontWeight();
        this.breakAfter = other.breakAfter();
        this.showInCompact = other.showInCompact();
        this.showInCompactHeader = other.showInCompactHeader();
    }
}
