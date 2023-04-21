package gnu.java.lang.management;

import java.lang.management.OperatingSystemMXBean;
import javax.management.NotCompliantMBeanException;

/**
 * Provides access to information about the underlying operating
 * system.  
 *
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.5
 */
public final class OperatingSystemMXBeanImpl extends BeanImpl implements OperatingSystemMXBean {

    /**
   * Constructs a new <code>OperatingSystemMXBeanImpl</code>.
   *
   * @throws NotCompliantMBeanException if this class doesn't implement
   *                                    the interface or a method appears
   *                                    in the interface that doesn't comply
   *                                    with the naming conventions.
   */
    public OperatingSystemMXBeanImpl() throws NotCompliantMBeanException {
        super(OperatingSystemMXBean.class);
    }

    public String getArch() {
        return System.getProperty("os.arch");
    }

    public int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public String getName() {
        return System.getProperty("os.name");
    }

    public String getVersion() {
        return System.getProperty("os.version");
    }
}
