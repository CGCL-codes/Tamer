package com.leclercb.taskunifier.api.models;

import com.leclercb.taskunifier.api.models.beans.FolderBean;

public class FolderFactory<F extends Folder, FB extends FolderBean> extends AbstractModelFactory<Folder, FolderBean, F, FB> {

    private static FolderFactory<Folder, FolderBean> FACTORY;

    @SuppressWarnings("unchecked")
    public static <F extends Folder, FB extends FolderBean> void initializeWithClass(Class<F> modelClass, Class<FB> modelBeanClass) {
        if (FACTORY == null) {
            FACTORY = (FolderFactory<Folder, FolderBean>) new FolderFactory<F, FB>(modelClass, modelBeanClass);
        }
    }

    public static FolderFactory<Folder, FolderBean> getInstance() {
        if (FACTORY == null) FACTORY = new FolderFactory<Folder, FolderBean>(Folder.class, FolderBean.class);
        return FACTORY;
    }

    private FolderFactory(Class<F> modelClass, Class<FB> modelBeanClass) {
        super(Folder.class, FolderBean.class, modelClass, modelBeanClass);
    }

    @Override
    protected String getModelNodeName() {
        return "folder";
    }

    @Override
    protected String getModelListNodeName() {
        return "folders";
    }
}
