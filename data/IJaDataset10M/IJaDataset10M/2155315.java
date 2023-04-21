package org.jowidgets.api.widgets.descriptor;

import java.util.Collection;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.descriptor.setup.ICombinedCollectionInputFieldSetup;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

public interface ICombinedCollectionInputFieldDescriptor<ELEMENT_TYPE> extends ICombinedCollectionInputFieldSetup<ELEMENT_TYPE>, IWidgetDescriptor<IInputControl<Collection<ELEMENT_TYPE>>> {
}
