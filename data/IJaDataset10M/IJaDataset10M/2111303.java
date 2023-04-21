package net.sf.vcaperture.util.spring;

import net.sf.vcaperture.model.IApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The static factory which provides the context as necesary within the
 * application.
 * 
 * <p>
 * TODO: This class assumes that only one context will ever be used within the
 * container in which it is running. It could make use of a <tt>ThreadLocal</tt>
 * (or similar) to provide for multiple contexts to be available.
 * </p>
 * 
 * <p>
 * TODO: Not so much a factory as it is a locator. Perhaps should be renamed.
 * </p>
 * 
 * @author Jeremy Thomerson (jthomerson@users.sourceforge.net)
 */
public class ApplicationContextFactory implements ApplicationContextAware {

    private static final ApplicationContextFactory INSTANCE = new ApplicationContextFactory();

    public static ApplicationContextFactory getFactory() {
        return INSTANCE;
    }

    private IApplicationContext mWrapper;

    public final IApplicationContext getContext() {
        return mWrapper;
    }

    public final void setContext(ApplicationContext context) {
        mWrapper = new AppContextWrapper(context);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        getFactory().setContext(applicationContext);
    }

    private static class AppContextWrapper implements IApplicationContext {

        private final ApplicationContext mContext;

        private AppContextWrapper(ApplicationContext context) {
            mContext = context;
        }

        @SuppressWarnings("unchecked")
        public <T> T getBean(Class<T> clazz) {
            return (T) mContext.getBean(clazz.getName());
        }

        public Object getBean(String name) {
            return mContext.getBean(name);
        }

        public String getName() {
            return (String) mContext.getBean("context-name");
        }
    }
}
