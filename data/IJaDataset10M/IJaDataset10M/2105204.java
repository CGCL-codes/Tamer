package com.intellij.uiDesigner.compiler;

import com.intellij.uiDesigner.lw.LwRootContainer;

/**
 * @author yole
 */
public interface NestedFormLoader {

    LwRootContainer loadForm(String formFileName) throws Exception;

    String getClassToBindName(LwRootContainer container);
}
