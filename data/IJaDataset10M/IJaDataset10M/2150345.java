package org.middleheaven.ui.components;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.models.UIOutputModel;

public interface UIOutput extends UIComponent {

    public UIReadState getReadState();

    public UIOutputModel getUIModel();
}
