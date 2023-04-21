package es.map.sgtic.fw.log4j;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * 
 * @author <a href="mailto:carlos.alonso1@map.es">carlos.alonso1@map.es</a>
 * @version 1.0 Fecha: 16/04/2008
 */
public class DataSourceParser extends PatternParser {

    /**
	 * 
	 */
    protected int maxSizeMessage;

    protected int maxSizeException;

    protected int maxTraceException;

    /**
	 * @param pattern
	 * @param maxSizeMessage
	 * @param maxSizeException
	 * @param maxTraceException
	 */
    public DataSourceParser(String pattern, int maxSizeMessage, int maxSizeException, int maxTraceException) {
        super(pattern);
        this.maxSizeMessage = maxSizeMessage;
        this.maxSizeException = maxSizeException;
        this.maxTraceException = maxTraceException;
    }

    protected void finalizeConverter(char c) {
        PatternConverter pc = null;
        switch(c) {
            case 'e':
                pc = new ExceptionPatternConverter(maxSizeException, maxTraceException);
                addConverter(pc);
                break;
            case 'm':
                pc = new MessagePatternConverter(formattingInfo, maxSizeMessage);
                addConverter(pc);
                break;
            default:
                super.finalizeConverter(c);
                break;
        }
    }

    /**
	 * @author Carlos
	 * 
	 */
    private class MessagePatternConverter extends PatternConverter {

        int maxSizeMessage;

        /**
		 * @param formattingInfo
		 * @param maxSizeMessage
		 */
        public MessagePatternConverter(FormattingInfo formattingInfo, int maxSizeMessage) {
            super(formattingInfo);
            this.maxSizeMessage = maxSizeMessage;
        }

        protected String convert(LoggingEvent event) {
            String message = event.getRenderedMessage();
            if (maxSizeMessage != DataSourceLayout.DEFAULT_MAX_SIZE_EXCEPTION) {
                message = StringUtils.substring(event.getRenderedMessage(), 0, maxSizeMessage);
            }
            return StringEscapeUtils.escapeSql(message);
        }
    }

    /**
	 * @author Carlos
	 * 
	 */
    private class ExceptionPatternConverter extends PatternConverter {

        /**
		 * 
		 */
        static final String TAB = "\t";

        int maxSizeException;

        int maxTraceException;

        /**
		 * @param formattingInfo
		 * @param maxSizeException
		 * @param maxTraceException
		 */
        ExceptionPatternConverter(int maxSizeException, int maxTraceException) {
            super();
            this.maxSizeException = maxSizeException;
            this.maxTraceException = maxTraceException;
        }

        protected String convert(LoggingEvent event) {
            if (event.getThrowableInformation() == null) {
                return StringUtils.EMPTY;
            }
            String exception = convertThrowable(event.getThrowableInformation().getThrowable());
            if (maxSizeException != DataSourceLayout.DEFAULT_MAX_SIZE_EXCEPTION) {
                exception = StringUtils.substring(exception, 0, maxSizeException);
            }
            return StringEscapeUtils.escapeSql(exception);
        }

        /**
		 * @param throwableInfo
		 * @return
		 */
        private String convertThrowable(Throwable throwable) {
            if (throwable == null) {
                return StringUtils.EMPTY;
            }
            StringBuffer out = new StringBuffer();
            out.append(throwable + Layout.LINE_SEP);
            StackTraceElement[] stackTraces = throwable.getStackTrace();
            for (int i = 0; i < stackTraces.length && (maxTraceException == DataSourceLayout.DEFAULT_MAX_TRACE_EXCEPTION || i < maxTraceException); i++) {
                StackTraceElement trace = stackTraces[i];
                out.append(TAB + "at " + trace + Layout.LINE_SEP);
            }
            out.append(convertThrowable(throwable.getCause()));
            return out.toString();
        }
    }
}
