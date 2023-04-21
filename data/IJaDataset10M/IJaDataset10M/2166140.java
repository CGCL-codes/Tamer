package com.android.internal.policy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManagerPolicy;
import com.android.internal.policy.IPolicy;

/**
 * {@hide}
 */
public final class PolicyManager {

    private static final String POLICY_IMPL_CLASS_NAME = "com.android.internal.policy.impl.Policy";

    private static final IPolicy sPolicy;

    static {
        try {
            Class policyClass = Class.forName(POLICY_IMPL_CLASS_NAME);
            sPolicy = (IPolicy) policyClass.newInstance();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(POLICY_IMPL_CLASS_NAME + " could not be loaded", ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(POLICY_IMPL_CLASS_NAME + " could not be instantiated", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(POLICY_IMPL_CLASS_NAME + " could not be instantiated", ex);
        }
    }

    private PolicyManager() {
    }

    public static Window makeNewWindow(Context context) {
        return sPolicy.makeNewWindow(context);
    }

    public static LayoutInflater makeNewLayoutInflater(Context context) {
        return sPolicy.makeNewLayoutInflater(context);
    }

    public static WindowManagerPolicy makeNewWindowManager() {
        return sPolicy.makeNewWindowManager();
    }
}
