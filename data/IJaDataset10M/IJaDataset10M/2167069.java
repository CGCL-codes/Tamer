package com.intellij.uiDesigner.lw;

public interface IRootContainer extends IContainer {

    String getClassToBind();

    String getButtonGroupName(IComponent component);

    String[] getButtonGroupComponentIds(String groupName);

    boolean isInspectionSuppressed(final String inspectionId, final String componentId);

    IButtonGroup[] getButtonGroups();
}
