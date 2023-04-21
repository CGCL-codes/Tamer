package org.openremote.web.console.panel.entity;

public interface FormButton {

    String getType();

    String getName();

    Navigate getNavigate();

    void setType(String type);

    void setName(String name);

    void setNavigate(Navigate navigate);
}
