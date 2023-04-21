package com.kescom.matrix.core.directory;

import java.util.List;

public interface ICategory extends IDirectoryItem {

    List<ICategory> getSubCategories();

    List<ISeriesTemplate> getTemplates();
}
