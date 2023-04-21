package com.gwtent.client.reflection;

import com.gwtent.client.uibinder.Parameter;

public interface AbstractMethod extends HasAnnotations {

    public Parameter findParameter(String name);

    public ClassType getEnclosingType();

    public String getName();

    public Parameter[] getParameters();

    public String getReadableDeclaration();

    public Type[] getThrows();

    public Constructor isConstructor();

    public boolean isVarArgs();

    public void setVarArgs();
}
