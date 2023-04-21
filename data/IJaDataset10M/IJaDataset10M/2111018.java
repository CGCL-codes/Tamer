package org.travelfusion.xmlclient.impl.transport;

import static org.travelfusion.xmlclient.util.TfXSPIUtil.ensureStreamSupportsMark;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.travelfusion.xmlclient.util.TfXCommon;
import org.travelfusion.xmlclient.xobject.XAttributes;
import org.travelfusion.xmlclient.xobject.XRequest;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * An interceptor for handling logging of TripPlannerXML requests and responses.
 * <p>
 * Looks for the request attribute {@link TfXCommon#XATTR_LOG_FOR}. If it is found, requests and responses are logged at
 * INFO level. Alternatively, if DEBUG level is enabled, all requests and responses are logged.
 * 
 * @author Jesse McLaughlin (nzjess@gmail.com)
 */
public class LoggingTransportInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingTransportInterceptor.class);

    private Logger logger;

    /**
   * <code>bind(Key.get(Logger.class, Names.named("TransportInterceptorLogger"))).toInstance(...)</code>
   */
    @Inject(optional = true)
    public void setLogger(@Named("TransportInterceptorLogger") Logger logger) {
        this.logger = logger;
    }

    public InputStream invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        XRequest request = (XRequest) args[0];
        String requestString = (String) args[1];
        Logger logger = this.logger;
        if (logger == null) logger = log;
        boolean debug = logger.isDebugEnabled();
        String logFor = null;
        if (request instanceof XAttributes) {
            logFor = ((XAttributes) request).getAttribute(TfXCommon.XATTR_LOG_FOR);
        }
        if (debug || (logger.isInfoEnabled() && logFor != null)) {
            StringBuilder buf = new StringBuilder(requestString.length() + 16 + (logFor != null ? logFor.length() : 0));
            build(buf, "XML REQUEST", logFor);
            buf.append(requestString);
            log(logger, buf.toString(), debug);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            buf.setLength(0);
            build(buf, "XML RESPONSE", logFor);
            byte[] prebytes = buf.toString().getBytes("UTF-8");
            out.write(prebytes);
            InputStream responseStream = null;
            try {
                responseStream = ensureStreamSupportsMark((InputStream) invocation.proceed());
                for (int b; ((b = responseStream.read()) != -1); ) {
                    out.write(b);
                }
            } finally {
                if (responseStream != null) responseStream.close();
            }
            byte[] bytes = out.toByteArray();
            log(logger, new String(bytes, "UTF-8"), debug);
            return new ByteArrayInputStream(bytes, prebytes.length, bytes.length);
        } else {
            return (InputStream) invocation.proceed();
        }
    }

    private void build(StringBuilder buf, String pre, String logFor) {
        buf.append(pre);
        if (logFor != null) buf.append(": ").append(logFor);
        buf.append("\n");
    }

    private static void log(Logger logger, String message, boolean debug) {
        if (debug) {
            logger.debug(message);
        } else {
            logger.info(message);
        }
    }
}
