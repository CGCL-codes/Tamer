package omoikane.exceptions;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *
 * @author mora
 */
public class CEAppender extends AppenderSkeleton {

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    protected void append(LoggingEvent event) {
        System.out.println(event.getMessage());
        ExceptionWindow ew = new ExceptionWindow();
        ew.getLblTituloError().setText((String) event.getMessage());
        if (event.getThrowableInformation() != null) {
            ew.getTxtExcepcion().setText(Misc.getStackTraceString(event.getThrowableInformation().getThrowable()));
        }
        ew.setVisible(true);
    }
}
