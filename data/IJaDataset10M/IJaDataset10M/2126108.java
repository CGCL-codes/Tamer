package com.trendsoft.eye.patcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import com.trendsoft.eye.EyeClinitCallback;
import com.trendsoft.eye.EyeConstructorCallback;
import com.trendsoft.eye.EyeModule;
import com.trendsoft.eye.EyeFactory;
import com.trendsoft.eye.EyeFilter;
import com.trendsoft.eye.EyeListener;
import com.trendsoft.eye.EyeMethodCallback;
import com.trendsoft.eye.EyeMethodInfo;
import com.trendsoft.eye.util.EyeMethodListHelper;

/**
 * Main controller for patching byte code. Desides if code should be patched, handles creation of
 * all contexts. Also notifies a listener when class is patched.
 *
 * @author vasiliy
 */
public class EyeClassFilePatchController {

    private static final String PACKAGE_PREFIX = EyeFactory.class.getPackage().getName().replace('.', '/').concat("/");

    private final int count;

    private final EyeModule[] configurations;

    private final EyeFactory[] factorys;

    private final EyeFilter[] filters;

    private final EyeListener[] listeners;

    private final EyeClassPatcher patcher = new EyeClassPatcher();

    /**
     * EyeByteCodePatcher
     *
     * @param configurations
     */
    public EyeClassFilePatchController(EyeModule[] configurations) {
        this.configurations = configurations;
        this.factorys = new EyeFactory[configurations.length];
        this.filters = new EyeFilter[configurations.length];
        this.listeners = new EyeListener[configurations.length];
        count = configurations.length;
        for (int i = 0; i < count; ++i) {
            EyeModule configuration = configurations[i];
            factorys[i] = configuration.getFactory();
            filters[i] = configuration.getFilter();
            listeners[i] = configuration.getListener();
        }
        EyeContextFactory.init(this);
    }

    /**
     * @param newByteImage
     * @param classLoader TODO
     * @param className
     * @param byteImage
     * @return
     */
    public byte[] patchIfNeeded(ClassLoader classLoader, String className, byte[] byteImage) {
        if (className.startsWith(PACKAGE_PREFIX) || className.startsWith("java")) {
            return null;
        }
        byte[] newByteImage = null;
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        EyeFilterSet filterSet = new EyeFilterSet(filters);
        int mask = filterSet.getClassAcceptMask(className);
        if (mask != 0) {
            try {
                newByteImage = patcher.patchClassImage(byteImage, classLoader, mask, filterSet);
                for (int i = 0; i < count; ++i) {
                    int bit = 1 << i;
                    if ((mask & bit) != 0) {
                        if (listeners[i] != null) {
                            listeners[i].classPatched(className, newByteImage);
                        }
                    }
                }
            } catch (RuntimeException exc) {
                exc.printStackTrace();
            }
        }
        return newByteImage;
    }

    /**
     * Creates implementation of {@link EyeClass} by calling {@link EyeFactory} to create class
     * context, method contexts and method callbacks
     *
     * @param cls
     * @param classMask
     * @param methodMasks
     * @return
     */
    public EyeClass createClassContext(Class<?> cls, int classMask, int[] methodMasks) {
        Object[] classContexts = new Object[count];
        for (int i = 0; i < count; ++i) {
            int bit = 1 << i;
            if ((classMask & bit) != 0 && factorys[i] != null) {
                classContexts[i] = factorys[i].createClassContext(cls);
            }
        }
        EyeCallbackHandler[] methods = listMethods(cls, classMask, classContexts, methodMasks);
        return new EyeClassImpl(classContexts, methods);
    }

    /**
     * Creates instance context via {@link EyeFactory#createInstanceContext(Object)}.
     *
     * @param instance
     * @param mask
     * @return
     */
    public Object[] createInstanceContext(Object instance, int mask) {
        Object[] instanceContexts = new Object[count];
        for (int i = 0; i < count; ++i) {
            int bit = 1 << i;
            if ((mask & bit) != 0 && factorys[i] != null) {
                instanceContexts[i] = factorys[i].createInstanceContext(instance);
            }
        }
        return instanceContexts;
    }

    private EyeCallbackHandler[] listMethods(Class<?> cls, int mask, Object[] classContexts, int[] methodMasks) {
        List<EyeMethodInfo> methodsInfos = EyeMethodListHelper.listMethodsInfos(cls);
        int methodsCount = methodsInfos.size();
        EyeCallbackHandler[] callbackHandlers = new EyeCallbackHandler[methodsCount];
        if (methodsCount != methodMasks.length) {
            throw new RuntimeException("Internal error");
        }
        for (int i = 0; i < methodsCount; ++i) {
            int methodMask = methodMasks[i];
            if (methodMask != 0) {
                EyeMethodInfo methodInfo = methodsInfos.get(i);
                Object method = methodInfo.method;
                if (method == null) {
                    EyeClinitCallback[] callbacks = new EyeClinitCallback[count];
                    for (int j = 0; j < count; ++j) {
                        int bit = 1 << j;
                        if ((methodMask & bit) != 0 && factorys[j] != null) {
                            callbacks[j] = factorys[j].createClinitCallback(cls, classContexts[j]);
                        }
                    }
                    callbackHandlers[i] = new EyeClinitCallbackHandler(callbacks);
                } else if (method instanceof Constructor<?>) {
                    EyeConstructorCallback[] callbacks = new EyeConstructorCallback[count];
                    for (int j = 0; j < count; ++j) {
                        int bit = 1 << j;
                        if ((methodMask & bit) != 0 && factorys[j] != null) {
                            callbacks[j] = factorys[j].createConstructorCallback(cls, (Constructor<?>) method, classContexts[j], methodInfo.descriptor);
                        }
                    }
                    callbackHandlers[i] = new EyeConstructorCallbackHandler((Constructor<?>) method, callbacks);
                } else {
                    EyeMethodCallback[] callbacks = new EyeMethodCallback[count];
                    for (int j = 0; j < count; ++j) {
                        int bit = 1 << j;
                        if ((methodMask & bit) != 0 && factorys[j] != null) {
                            callbacks[j] = factorys[j].createMethodCallback(cls, (Method) method, classContexts[j], methodInfo.descriptor);
                        }
                    }
                    callbackHandlers[i] = new EyeMethodCallbackHandler((Method) method, callbacks);
                }
            }
        }
        return callbackHandlers;
    }

    public int indexOf(EyeModule configuration) {
        for (int i = 0; i < configurations.length; ++i) {
            if (configuration == configurations[i]) {
                return i;
            }
        }
        return -1;
    }
}
