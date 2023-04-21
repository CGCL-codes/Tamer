package jfun.yan.monitoring;

import java.lang.reflect.Constructor;

/**
 * <p>
 * This interface represents something that monitors the invocation of a constructor.
 * </p>
 * @author Michelle Lei
 *
 */
public interface CtorMonitor {

    /**
   * This method is called right before the constructor is invoked.
   * @param ctor the constructor to be called.
   * @param args the arguments passed to the constructor.
   */
    void constructing(Constructor ctor, Object[] args);

    /**
   * This method is called right after the constructor is invoked successfully.
   * @param ctor the constructor. 
   * @param args the arguments passed to the constructor.
   * @param instance the object created by the constructor.
   * @param duration excactly how long the constructor execution takes. (in milliseconds)
   */
    void constructed(Constructor ctor, Object[] args, Object instance, long duration);

    /**
   * This method is called after the constructor failed.
   * @param ctor the constructor.
   * @param args the arguments passed to the constructor.
   * @param e the exception thrown out of the constructor.
   * @param duration exactly how long the constructor execution takes. (in milliseconds)
   */
    void constructionFailed(Constructor ctor, Object[] args, Throwable e, long duration);
}
