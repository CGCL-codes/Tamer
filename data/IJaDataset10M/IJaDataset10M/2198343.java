package org.jowidgets.api.model.item;

public interface IContainerItemModelBuilder extends IItemModelBuilder<IContainerItemModelBuilder, IContainerItemModel> {

    IContainerItemModelBuilder setContentCreator(IContainerContentCreator contentCreator);
}
