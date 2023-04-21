package com.nexirius.framework.dataviewer;

import com.nexirius.framework.datamodel.DataModel;

public interface DataModelTreeComponentCreator {

    DataModelTreeComponent createDataModelTreeComponent(DataModelTreeViewer treeViewer, DataModel model);
}
