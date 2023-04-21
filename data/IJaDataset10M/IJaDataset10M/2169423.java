package org.deved.antlride.core.model;

public interface IParameters extends IModelElement {

    ITargetAction getAction();

    IParameter findParameter(String name);

    IParameter getParemeter(int index);

    IParameter[] getParemeters();

    int getParametersCount();
}
