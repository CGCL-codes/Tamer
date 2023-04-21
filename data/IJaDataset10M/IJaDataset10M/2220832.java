package org.jowidgets.api.widgets.blueprint.builder.convenience;

import org.jowidgets.common.color.IColorConstant;

public interface IComponentSetupConvenience<INSTANCE_TYPE> {

    INSTANCE_TYPE setVisible(boolean visible);

    INSTANCE_TYPE setColor(IColorConstant colorConstant);
}
