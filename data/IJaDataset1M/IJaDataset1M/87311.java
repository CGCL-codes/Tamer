package org.jowidgets.common.widgets.builder;

public interface IFrameSetupBuilderCommon<INSTANCE_TYPE extends IFrameSetupBuilderCommon<?>> extends ITitledWindowSetupBuilderCommon<INSTANCE_TYPE>, IContainerSetupBuilderCommon<INSTANCE_TYPE> {

    INSTANCE_TYPE setCloseable(boolean closeable);
}
