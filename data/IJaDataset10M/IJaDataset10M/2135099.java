package org.jowidgets.common.widgets.builder;

import org.jowidgets.common.types.SelectionPolicy;

public interface ITreeSetupBuilderCommon<INSTANCE_TYPE extends ITreeSetupBuilderCommon<?>> extends IComponentSetupBuilderCommon<INSTANCE_TYPE> {

    INSTANCE_TYPE setSelectionPolicy(SelectionPolicy selectionPolicy);

    INSTANCE_TYPE setContentScrolled(boolean contentScrolled);
}
