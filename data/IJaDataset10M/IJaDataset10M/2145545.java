package com.vladium.emma.report;

import com.vladium.emma.report.IReportProperties;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * @author Vlad Roubtsov, (C) 2003
 */
public interface IReportEnums {

    final class TypeAttribute extends EnumeratedAttribute {

        public String[] getValues() {
            return VALUES;
        }

        private static final String[] VALUES = new String[] { "txt", "html", "xml" };
    }

    final class DepthAttribute extends EnumeratedAttribute {

        public String[] getValues() {
            return VALUES;
        }

        private static final String[] VALUES = new String[] { IReportProperties.DEPTH_ALL, IReportProperties.DEPTH_PACKAGE, IReportProperties.DEPTH_SRCFILE, IReportProperties.DEPTH_CLASS, IReportProperties.DEPTH_METHOD };
    }

    final class ViewTypeAttribute extends EnumeratedAttribute {

        public String[] getValues() {
            return VALUES;
        }

        private static final String[] VALUES = new String[] { IReportProperties.SRC_VIEW, IReportProperties.CLS_VIEW };
    }

    static final class UnitsTypeAttribute extends EnumeratedAttribute {

        public String[] getValues() {
            return VALUES;
        }

        private static final String[] VALUES = new String[] { IReportProperties.INSTR_UNITS, IReportProperties.COUNT_UNITS };
    }
}
