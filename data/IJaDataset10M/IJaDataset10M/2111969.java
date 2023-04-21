package org.jfree.report.modules.factories.data.base;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDataFactory;
import org.jfree.util.Configuration;
import org.jfree.xmlns.parser.AbstractXmlResourceFactory;

/**
 * Creation-Date: 08.04.2006, 14:25:23
 *
 * @author Thomas Morgner
 */
public class ReportDataFactoryXmlResourceFactory extends AbstractXmlResourceFactory {

    public ReportDataFactoryXmlResourceFactory() {
    }

    protected Configuration getConfiguration() {
        return JFreeReportBoot.getInstance().getGlobalConfig();
    }

    public Class getFactoryType() {
        return ReportDataFactory.class;
    }
}
