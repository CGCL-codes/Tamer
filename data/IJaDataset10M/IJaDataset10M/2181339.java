package com.jdevflow.sunny.components.form;

import java.util.Collection;

public interface IFormComponent {

    public void setValid(boolean propValid, Collection<String> propErrors);
}
