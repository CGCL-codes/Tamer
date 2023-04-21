package org.akrogen.tkui.core.dom.attributes;

import org.akrogen.tkui.core.converters.ITkuiConverter;

public interface ITkuiSimpleAttrDescriptor extends ITkuiAttrDescriptor {

    public ITkuiConverter getConverter();
}
