package org.jomc.logging.it;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.fail;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
public class LoggerTest {

    /** Creates a new {@code LoggerTest} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    public LoggerTest() {
        super();
    }

    /**
     * Tests the {@link org.jomc.logging.Logger#isInfoEnabled() isXxxEnabled()} methods to not throw any exceptions.
     *
     * @throws Exception if testing fails.
     */
    @Test
    public void testIsEnabled() throws Exception {
        if (this.getLogger() != null) {
            this.getLogger().isDebugEnabled();
            this.getLogger().isErrorEnabled();
            this.getLogger().isFatalEnabled();
            this.getLogger().isInfoEnabled();
            this.getLogger().isTraceEnabled();
            this.getLogger().isWarnEnabled();
        } else {
            fail(this.getTestImplementationNotFoundMessage(this.getLocale()));
        }
    }

    /**
     * Tests the various logger methods to not throw any exceptions.
     *
     * @throws Exception if testing fails.
     */
    @Test
    public void testLog() throws Exception {
        if (this.getLogger() != null) {
            this.getLogger().debug("TEST");
            this.getLogger().debug(new Exception());
            this.getLogger().debug("TEST", new Exception());
            this.getLogger().error("TEST");
            this.getLogger().error(new Exception());
            this.getLogger().error("TEST", new Exception());
            this.getLogger().fatal("TEST");
            this.getLogger().fatal(new Exception());
            this.getLogger().fatal("TEST", new Exception());
            this.getLogger().info("TEST");
            this.getLogger().info(new Exception());
            this.getLogger().info("TEST", new Exception());
            this.getLogger().trace("TEST");
            this.getLogger().trace(new Exception());
            this.getLogger().trace("TEST", new Exception());
            this.getLogger().warn("TEST");
            this.getLogger().warn(new Exception());
            this.getLogger().warn("TEST", new Exception());
        } else {
            fail(this.getTestImplementationNotFoundMessage(this.getLocale()));
        }
    }

    /**
     * Test runner entry point.
     * <p>This method sets up the JDK's {@code LogManager} with properties found at class path location
     * {@code "/logging.properties"} and executes {@link JUnitCore#main} passing the given arguments with this classes
     * name prepended.</p>
     *
     * @param args Command line arguments.
     */
    public static void main(final String... args) {
        try {
            final URL loggingProperties = LoggerTest.class.getResource("/logging.properties");
            if (loggingProperties != null) {
                final InputStream in = loggingProperties.openStream();
                LogManager.getLogManager().readConfiguration(in);
                in.close();
            }
            final List<String> l = new ArrayList<String>(Arrays.asList(args));
            l.add(0, LoggerTest.class.getName());
            JUnitCore.main(l.toArray(new String[l.size()]));
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Gets the {@code <Locale>} dependency.
     * <p>
     *   This method returns the {@code <default>} object of the {@code <java.util.Locale>} specification at specification level 1.1.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <Locale>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private java.util.Locale getLocale() {
        final java.util.Locale _d = (java.util.Locale) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "Locale");
        assert _d != null : "'Locale' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <Logger>} dependency.
     * <p>
     *   This method returns any available object of the {@code <org.jomc.logging.Logger>} specification at specification level 1.0.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <p><strong>Properties:</strong>
     *   <table border="1" width="100%" cellpadding="3" cellspacing="0">
     *     <tr class="TableSubHeadingColor">
     *       <th align="left" scope="col" nowrap><b>Name</b></th>
     *       <th align="left" scope="col" nowrap><b>Type</b></th>
     *       <th align="left" scope="col" nowrap><b>Documentation</b></th>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>{@code <name>}</td>
     *       <td align="left" valign="top" nowrap>{@code java.lang.String}</td>
     *       <td align="left" valign="top"></td>
     *     </tr>
     *   </table>
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return Implementation tests are performed with.
     * {@code null} if no object is available.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private org.jomc.logging.Logger getLogger() {
        return (org.jomc.logging.Logger) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "Logger");
    }

    /**
     * Gets the text of the {@code <Test Implementation Not Found Message>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @return The text of the {@code <Test Implementation Not Found Message>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private String getTestImplementationNotFoundMessage(final java.util.Locale locale) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "Test Implementation Not Found Message", locale);
        assert _m != null : "'Test Implementation Not Found Message' message not found.";
        return _m;
    }
}
