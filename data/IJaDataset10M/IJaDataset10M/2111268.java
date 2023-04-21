package com.jiexplorer.model;

import java.util.List;

public interface IFolderNode extends IFileNode {

    public List<IFileNode> getChildrenFolders();

    public List<IImageNode> getImageList(final Object[] source);

    public List<IImageNode> getImageList();
}
