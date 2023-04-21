package abbot.swt.tester;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.swt.widgets.Widget;
import abbot.swt.Log;
import abbot.swt.utilities.Cache;

public class WidgetTesterFactoryImpl implements WidgetTesterFactory {

    /**
	 * A {@link List} of packages to be searched for {@link WidgetTester}
	 * {@link Class}es.
	 */
    private final Map<String, ClassLoader> packages;

    /**
	 * A cache of {@link WidgetTester}s that can be looked up by {@link Widget}
	 * {@link Class}.
	 */
    private final Cache<Class<? extends Widget>, WidgetTester> testers;

    /**
	 * A cache of {@link WidgetTester} {@link Class}es that can be looked up by
	 * {@link Widget} {@link Class}.
	 */
    private final Cache<Class<? extends Widget>, Class<? extends WidgetTester>> classes;

    public WidgetTesterFactoryImpl() {
        packages = new HashMap<String, ClassLoader>();
        testers = new Cache<Class<? extends Widget>, WidgetTester>() {

            protected WidgetTester newValue(Class<? extends Widget> widgetClass) {
                return createTester(widgetClass);
            }
        };
        classes = new Cache<Class<? extends Widget>, Class<? extends WidgetTester>>() {

            protected Class<? extends WidgetTester> newValue(Class<? extends Widget> widgetClass) {
                return findTesterClass(widgetClass);
            }
        };
    }

    /**
	 * Adds a package name to the list of packages to be searched for
	 * {@link WidgetTester}s
	 * 
	 * @param packageName
	 * @see #packages
	 * @see #findTesterClass(Class)
	 */
    public synchronized void addPackage(String packageName, ClassLoader classLoader) {
        checkPackageName(packageName);
        packages.put(packageName, classLoader);
    }

    /**
	 * Removes a package name from the list of packages to be searched for
	 * {@link WidgetTester}s
	 * 
	 * @param packageName
	 * @see #packages
	 * @see #findTesterClass(Class)
	 */
    public synchronized void removePackage(String packageName) {
        checkPackageName(packageName);
        packages.remove(packageName);
    }

    private void checkPackageName(String packageName) {
        if (packageName == null) throw new IllegalArgumentException("packageName is null");
    }

    /**
	 * Returns a {@link WidgetTester} for the specified {@link Widget}
	 * {@link Class}.
	 * 
	 * @param widgetClass
	 * @return the {@link WidgetTester}
	 */
    public synchronized WidgetTester getTester(Class<? extends Widget> widgetClass) {
        checkWidgetClass(widgetClass);
        return testers.get(widgetClass);
    }

    public synchronized void setTester(Class<? extends Widget> widgetClass, WidgetTester tester) {
        checkWidgetClass(widgetClass);
        if (tester == null) throw new IllegalArgumentException("tester is null");
        testers.put(widgetClass, tester);
    }

    private void checkWidgetClass(Class<? extends Widget> widgetClass) {
        if (widgetClass == null) throw new IllegalArgumentException("widgetClass is null");
        if (!Widget.class.isAssignableFrom(widgetClass)) throw new IllegalArgumentException("widget class is invalid: " + widgetClass);
    }

    /**
	 * @param widgetClass
	 * @return a {@link WidgetTester}
	 * @see #testers
	 */
    private WidgetTester createTester(Class<? extends Widget> widgetClass) {
        Class<? extends WidgetTester> testerClass = getTesterClass(widgetClass);
        if (testerClass != null) {
            Constructor<? extends WidgetTester> constructor = getConstructor(testerClass, null);
            if (constructor != null) return newInstance(constructor, null);
        }
        return null;
    }

    private Constructor<? extends WidgetTester> getConstructor(Class<? extends WidgetTester> testerClass, Class<?>[] parameterTypes) {
        try {
            return testerClass.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            Log.warning(e);
        }
        return null;
    }

    private WidgetTester newInstance(Constructor<? extends WidgetTester> constructor, Object[] arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (IllegalArgumentException e) {
            Log.warning(e);
        } catch (InstantiationException e) {
            Log.warning(e);
        } catch (IllegalAccessException e) {
            Log.warning(e);
        } catch (InvocationTargetException e) {
            Log.warning(e);
        }
        return null;
    }

    private Class<? extends WidgetTester> getTesterClass(Class<? extends Widget> widgetClass) {
        checkWidgetClass(widgetClass);
        return classes.get(widgetClass);
    }

    /**
	 * @param widgetClass
	 * @return the {@link WidgetTester} {@link Class} (or <code>null</code> if
	 *         there isn't one)
	 * @see #classes
	 */
    @SuppressWarnings("unchecked")
    private synchronized Class<? extends WidgetTester> findTesterClass(Class<? extends Widget> widgetClass) {
        String testerClassName = widgetClass.getSimpleName() + "Tester";
        for (Entry<String, ClassLoader> entry : packages.entrySet()) {
            String fullTesterClassName = entry.getKey() + "." + testerClassName;
            Class<? extends WidgetTester> testerClass = resolveClass(fullTesterClassName, entry.getValue());
            if (testerClass != null) return testerClass;
        }
        if (widgetClass != Widget.class) return findTesterClass((Class<? extends Widget>) widgetClass.getSuperclass());
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends WidgetTester> resolveClass(String fullClassName, ClassLoader classLoader) {
        try {
            if (classLoader == null) return (Class<? extends WidgetTester>) Class.forName(fullClassName);
            return (Class<? extends WidgetTester>) Class.forName(fullClassName, true, classLoader);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }
}
