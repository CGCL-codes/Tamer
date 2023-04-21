package com.cb.eclipse.folding.java.calculation;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.preferences.PreferenceKeys;
import com.cb.eclipse.folding.util.JDTUtil;

/**
 * This strategy deals specifically with Methods.
 * 
 * This class is heavily reliant on the GenericBlockStrategy super class.
 * 
 * @author RJ
 */
public class MethodStrategy extends AbstractBlockStrategy {

    private static final int COLLAPSE = 0;

    private static final int FOLD = 99;

    public MethodStrategy() {
        super(true);
    }

    public boolean shouldCollapse(IJavaElement owner, int token) throws JavaModelException {
        return shouldDo(isEmpty(), COLLAPSE, owner, token);
    }

    public boolean shouldFold(IJavaElement owner, int token) throws JavaModelException {
        return shouldDo(isEmpty(), FOLD, owner, token);
    }

    public boolean shouldFilterLastLine(IJavaElement owner, int token) throws JavaModelException {
        if (isEmpty()) {
            return FoldingPlugin.getBoolean(PreferenceKeys.LAST_LINE_METHODS);
        } else {
            return FoldingPlugin.getBoolean(PreferenceKeys.LAST_LINE_CONTROLS);
        }
    }

    private boolean shouldDo(boolean isEmpty, int type, IJavaElement owner, int token) throws JavaModelException {
        boolean shouldDo = false;
        if (owner instanceof IMethod) {
            IMethod method = (IMethod) owner;
            if (isEmpty) {
                if (isConstructor(method)) {
                    if (type == COLLAPSE) {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_CONSTRUCTORS);
                    } else {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_CONSTRUCTORS);
                    }
                } else if (isMainMethod(method)) {
                    if (type == COLLAPSE) {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_MAIN_METHODS);
                    } else {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_MAIN_METHODS);
                    }
                } else if (isGetterOrSetter(method)) {
                    if (type == COLLAPSE) {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_GETTERS_SETTERS);
                    } else {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_GETTERS_SETTERS);
                    }
                } else {
                    if (type == COLLAPSE) {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.COLLAPSE_METHODS);
                    } else {
                        shouldDo = FoldingPlugin.getBoolean(PreferenceKeys.FOLD_METHODS);
                    }
                }
            } else {
                if (type == COLLAPSE) {
                    shouldDo = false;
                } else {
                    shouldDo = shouldDo(true, type, owner, token);
                }
            }
        }
        return shouldDo;
    }

    private boolean isMainMethod(IMethod elem) throws JavaModelException {
        boolean result = elem.isMainMethod();
        return result;
    }

    private boolean isConstructor(IMethod elem) throws JavaModelException {
        boolean result = elem.isConstructor();
        return result;
    }

    private boolean isGetterOrSetter(IMethod elem) throws JavaModelException {
        boolean result = JDTUtil.isGetterOrSetter(elem);
        return result;
    }
}
