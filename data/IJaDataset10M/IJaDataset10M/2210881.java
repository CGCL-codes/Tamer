package org.apache.ws.jaxme.logging;

/** <p>An logger factory creating instances of {@link JavaUtilLogger}.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class Log4jLoggerFactory extends LoggerFactoryImpl {

    public Logger newLogger(String pName) {
        return new Log4jLogger(pName);
    }
}
