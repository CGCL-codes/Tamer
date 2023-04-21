package net.za.grasser.duplicate.gui.display;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.za.grasser.duplicate.util.Chars;
import net.za.grasser.duplicate.util.res.Debug;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

/**
 * This class ...
 * 
 * @author <a href="http://sourceforge.net/sendmessage.php?touser=733840">pyropunk at sourceforge dot net</a>
 * @version $Revision: 1.7 $
 */
public class DoubleDisplayFactory {

    /**
   * <code>DISPLAY</code> DoubleDisplayFactory -
   */
    private static final String DISPLAY = "Display";

    /**
   * <code>DOUBLE</code> DoubleDisplayFactory -
   */
    private static final String DOUBLE = ".Double";

    /**
   * <code>log</code> DoubleDisplayFactory -
   */
    private static final Logger log = Logger.getLogger(DoubleDisplayFactory.class);

    /**
   * <code>internal</code> DoubleDisplayFactory -
   */
    private static DoubleDisplayFactory internal = null;

    /**
   * Constructor
   */
    private DoubleDisplayFactory() {
        super();
    }

    /**
   * This method ...
   * 
   * @return DoubleDisplayFactory
   */
    public static synchronized DoubleDisplayFactory getInstance() {
        if (internal == null) {
            internal = new DoubleDisplayFactory();
        }
        return internal;
    }

    /**
   * This method generates the correct DoubleDisplay for the given type.
   * 
   * @param pPackage
   * @param pArg0
   * @param pArg1
   * @return DoubleDisplay
   */
    @SuppressWarnings({ "unchecked", "boxing" })
    public final DoubleDisplay makeDoubleDisplay(final String pPackage, final Composite pArg0, final int pArg1) {
        String cls = DoubleDisplayFactory.class.getName();
        cls = cls.substring(0, cls.lastIndexOf(Chars.DOT) + 1) + pPackage;
        cls += DOUBLE + pPackage.substring(0, 1).toUpperCase() + pPackage.substring(1) + DISPLAY;
        try {
            final Class<DoubleDisplay> c = (Class<DoubleDisplay>) Class.forName(cls);
            final Constructor<DoubleDisplay> con = c.getConstructor(new Class[] { Composite.class, int.class });
            return con.newInstance(new Object[] { pArg0, pArg1 });
        } catch (final ClassNotFoundException cnf) {
            log.error(Debug.getString(DoubleDisplayFactory.class, 1), cnf);
            return null;
        } catch (final InvocationTargetException ite) {
            log.error(Debug.getString(DoubleDisplayFactory.class, 1), ite);
            return null;
        } catch (final SecurityException e) {
            log.error(Debug.getString(DoubleDisplayFactory.class, 1), e);
            return null;
        } catch (final NoSuchMethodException e) {
            log.error(Debug.getString(DoubleDisplayFactory.class, 1), e);
            return null;
        } catch (final IllegalArgumentException e) {
            log.error(Debug.getString(DoubleDisplayFactory.class, 1), e);
            return null;
        } catch (final InstantiationException e) {
            log.error(Debug.getString(DoubleDisplayFactory.class, 1), e);
            return null;
        } catch (final IllegalAccessException e) {
            log.error(Debug.getString(DoubleDisplayFactory.class, 1), e);
            return null;
        }
    }
}
