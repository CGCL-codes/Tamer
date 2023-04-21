package org.auroraide.client.ui.codePressEditor.impl;

import org.auroraide.client.ui.codePressEditor.CodePressEditor1;

public class CodePressEditorImpl {

    public native CodePressEditor1 create(String elementID);

    public native CodePressEditor1 create1(String elementID);

    public native String getCode(CodePressEditor1 editor);

    public native void setCode(CodePressEditor1 editor, String code);

    public native void toggleLineNumbers(CodePressEditor1 editor);

    public native void edit(CodePressEditor1 editor, String language);

    public native void toggleEditor(CodePressEditor1 editor);

    public native void toggleReadOnly(CodePressEditor1 editor);

    public native void toggleAutoComplete(CodePressEditor1 editor);
}
