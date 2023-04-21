package org.trinkets.util.jni;

import org.trinkets.util.jni.annotations.JNIBundle;
import org.trinkets.util.jni.annotations.JNILibrary;
import java.text.MessageFormat;

@JNILibrary("JNIHelloWorld")
@JNIBundle("JNIHelloWorldBundle.jar")
public final class JNIHelloWorldImpl implements JNIHelloWorld {

    private final String user;

    public JNIHelloWorldImpl(String me) {
        this.user = me;
    }

    /**
     * Here is public wrapper to native method.
     *
     * @param hello Hello string
     */
    public void sayHello(String hello) {
        sayHello0(MessageFormat.format("{0}>{1}", user, hello));
    }

    /**
     * Here is native method
     *
     * @param hello Hello string
     */
    private native void sayHello0(String hello);
}
