package org.xmlvm.iphone;

import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public abstract class CAPropertyAnimation extends CAAnimation {

    /**
	 * + (id)animationWithKeyPath:(NSString *)path;
	 */
    public static CAPropertyAnimation animationWithKeyPath(String path) {
        throw new RuntimeException("Stub");
    }

    /** Default constructor */
    CAPropertyAnimation() {
    }

    /**
	 * @property(copy) NSString *keyPath;
	 */
    public String getKeyPath() {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(copy) NSString *keyPath;
	 */
    public void setKeyPath(String keyPath) {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(getter=isAdditive) BOOL additive;
	 */
    public boolean isAdditive() {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(getter=isAdditive) BOOL additive;
	 */
    public void setAdditive(boolean additive) {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(getter=isCumulative) BOOL cumulative;
	 */
    public boolean isCumulative() {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(getter=isCumulative) BOOL cumulative;
	 */
    public void setCumulative(boolean cumulative) {
        throw new RuntimeException("Stub");
    }
}
