package net.sf.doolin.gui.util;

import javax.annotation.PostConstruct;
import javax.swing.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sets the look and feel of the application at startup.
 * 
 * @author Damien Coraboeuf
 */
public class GUILF {

    private static final Logger log = LoggerFactory.getLogger(GUILF.class);

    private boolean useSystem;

    private String lfClassName;

    /**
	 * Run.
	 */
    @PostConstruct
    public void run() {
        try {
            if (useSystem) {
                lfClassName = UIManager.getSystemLookAndFeelClassName();
            }
            log.debug("Setting L&F " + lfClassName);
            UIManager.setLookAndFeel(lfClassName);
        } catch (Exception e) {
            log.error("Cannot set the look and feel", e);
        }
    }

    /**
	 * Returns the lfClassName.
	 * 
	 * @return String
	 */
    public String getLfClassName() {
        return lfClassName;
    }

    /**
	 * Sets the lfClassName.
	 * 
	 * @param lfClassName
	 *            String
	 */
    public void setLfClassName(String lfClassName) {
        this.lfClassName = lfClassName;
    }

    /**
	 * Checks if is use system.
	 * 
	 * @return true, if is use system
	 */
    public boolean isUseSystem() {
        return useSystem;
    }

    /**
	 * Sets the use system.
	 * 
	 * @param useSystem
	 *            the new use system
	 */
    public void setUseSystem(boolean useSystem) {
        this.useSystem = useSystem;
    }
}
