package xbird.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class BasicResourceInjector implements ResourceInjector {

    @Nonnull
    private Object injectResource;

    public BasicResourceInjector(@CheckForNull Object injectResource) {
        if (injectResource == null) {
            throw new IllegalArgumentException();
        }
        this.injectResource = injectResource;
    }

    public void setResource(@CheckForNull Object injectResource) {
        if (injectResource == null) {
            throw new IllegalArgumentException();
        }
        this.injectResource = injectResource;
    }

    public void inject(Field f, Object target, Annotation ann) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.set(target, injectResource);
    }
}
