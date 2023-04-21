package org.geonetwork.domain.ebrim.test.utilities.csw202.exception;

import org.geonetwork.domain.csw202.exception.Exception;
import org.geonetwork.domain.csw202.exception.ExceptionReport;

public class ExceptionReportFactory {

    public static synchronized ExceptionReport create() {
        ExceptionReport exceptionReport = new ExceptionReport();
        exceptionReport.setVersion("1.0.0");
        org.geonetwork.domain.csw202.exception.Exception exception = new org.geonetwork.domain.csw202.exception.Exception();
        exception.setExceptionCode(Exception.WRS_INVALIDREQUEST);
        exception.addException(org.geonetwork.domain.csw202.exception.Exception.codeDescriptionMap.get(Exception.WRS_INVALIDREQUEST));
        exception.addException("ows:ExceptionText1");
        exceptionReport.addException(exception);
        return exceptionReport;
    }
}
