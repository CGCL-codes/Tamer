package org.blue.star.common;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;
import org.blue.star.base.utils;

public class BluePatternParser extends PatternParser {

    private class SystemTimePatternConverter extends PatternConverter {

        SystemTimePatternConverter(FormattingInfo formattingInfo) {
            super(formattingInfo);
        }

        public String convert(LoggingEvent event) {
            return String.valueOf(utils.currentTimeInSeconds());
        }
    }

    public BluePatternParser(String pattern) {
        super(pattern);
    }

    public void finalizeConverter(char c) {
        if (c == 'N') {
            addConverter(new SystemTimePatternConverter(formattingInfo));
            currentLiteral.setLength(0);
        } else {
            super.finalizeConverter(c);
        }
    }
}
