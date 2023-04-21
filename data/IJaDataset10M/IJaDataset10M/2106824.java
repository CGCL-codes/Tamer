package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.ITreeComboboxModel;

public interface IDimensionModel {

    public ITreeModel getTreeModel();

    public ITreeComboboxModel getComboboxModel();

    public ISubsetListModel getSubsetListModel();

    public XDimension getDimension();

    public XElement getSelectedElement();

    public void setSelectedElement(XElement element);

    public void dispose();
}
