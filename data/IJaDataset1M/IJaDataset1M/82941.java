package net.sourceforge.javautil.classloader.boot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.javautil.classloader.impl.ClassContext;

/**
 * An information composite for {@link IEntryPointType}'s so as to provide
 * access to initialization parameters.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: EntryPointConfiguration.java 2580 2010-11-15 03:52:49Z ponderator $
 */
public class EntryPointConfiguration {

    protected final IEntryPointType parent;

    protected final EntryPoint point;

    protected Set<IEntryPointType> executed = new LinkedHashSet<IEntryPointType>();

    protected EntryPointConfiguration(EntryPoint point) {
        this(null, point);
    }

    protected EntryPointConfiguration(IEntryPointType parent, EntryPoint point) {
        this.parent = parent;
        this.point = point;
    }

    public <T extends IEntryPointType> boolean isAlreadyExecuted(Class<T> type) {
        for (IEntryPointType etype : this.executed) {
            if (type.isInstance(etype)) return true;
        }
        return false;
    }

    public void registerExecuted(IEntryPointType type) {
        this.executed.add(type);
    }

    /**
	 * This will not be null when multi-point booting or chained booting 
	 * is being used and the current point is not the root in the chain.
	 * 
	 * @return The parent of this entry point config, otherwise null
	 */
    public IEntryPointType getParent() {
        return parent;
    }

    /**
	 * @return The entry point that generated this configuration.
	 */
    public EntryPoint getPoint() {
        return point;
    }

    /**
	 * @return The main class should be used, or null if not specified
	 */
    public String getMainClass() {
        return (parent == null ? point : parent).getSetting(EntryPoint.MAIN_CLASS_PROPERTY);
    }

    /**
	 * Pass through method to {@link EntryPoint#printHelp(String)}.
	 * 
	 * @param error The message to display
	 */
    public void printHelp(String error) {
        EntryPoint.printHelp(error);
    }
}
