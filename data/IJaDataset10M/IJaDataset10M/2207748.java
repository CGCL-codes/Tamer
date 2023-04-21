package net.sf.beanreports.jasper.proxy;

import java.lang.annotation.Annotation;
import net.sf.beanreports.jasper.annotation.ClassReportInfo;

/**
 * BaseReportInfoProxy for the ReportInfo annotation
 */
@SuppressWarnings("all")
@ClassReportInfo
public class ClassReportInfoProxy implements ClassReportInfo {

    public Class<? extends Annotation> annotationType;

    public String elementTitle;

    public boolean oneItemPerReportPage;

    public String orientation;

    /**
     * create a instance populated with the default values as defined in the annotation
     */
    public ClassReportInfoProxy() {
        this(ClassReportInfoProxy.class.getAnnotation(ClassReportInfo.class));
    }

    /**
     * copy constructor
     */
    public ClassReportInfoProxy(ClassReportInfo other) {
        super();
        annotationType = other.annotationType();
        this.elementTitle = other.label();
        this.oneItemPerReportPage = other.oneItemPerReportPage();
    }

    public Class<? extends Annotation> annotationType() {
        return annotationType;
    }

    public String label() {
        return elementTitle;
    }

    public boolean oneItemPerReportPage() {
        return oneItemPerReportPage;
    }

    public String orientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return "ReportInfoAdapter [ elementTitle=" + elementTitle + ", oneItemPerReportPage=" + oneItemPerReportPage + ", showInReport=" + " orientation=" + orientation + "]";
    }
}
