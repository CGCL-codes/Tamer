package com.clican.pluto.cms.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import com.clican.pluto.cms.core.service.DataModelService;
import com.clican.pluto.cms.dao.DataModelDao;
import com.clican.pluto.cms.dao.DirectoryDao;
import com.clican.pluto.orm.annotation.DynamicModel;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ITemplate;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;

public class DataModelServiceImpl extends BaseService implements DataModelService {

    private ModelContainer modelContainer;

    private ClassLoaderUtil classLoaderUtil;

    private DataModelDao dataModelDao;

    private DirectoryDao directoryDao;

    public void setModelContainer(ModelContainer modelContainer) {
        this.modelContainer = modelContainer;
    }

    public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
        this.classLoaderUtil = classLoaderUtil;
    }

    public void setDataModelDao(DataModelDao dataModelDao) {
        this.dataModelDao = dataModelDao;
    }

    public void setDirectoryDao(DirectoryDao directoryDao) {
        this.directoryDao = directoryDao;
    }

    public List<ModelDescription> findAllDataModelDesc() {
        return new ArrayList<ModelDescription>(modelContainer.getModelDescs());
    }

    public IDataModel newDataModel(IDirectory parent, ModelDescription modelDescription) {
        return classLoaderUtil.newDataModel(parent, modelDescription);
    }

    @Transactional
    public void save(IDataModel dataModel) {
        dataModelDao.save(dataModel);
    }

    public void delete(List<IDataModel> dataModels, ModelDescription modelDescription) {
        if (modelDescription == null) {
            Map<ModelDescription, List<IDataModel>> dataModelMap = new HashMap<ModelDescription, List<IDataModel>>();
            for (IDataModel dataModel : dataModels) {
                ModelDescription md = modelContainer.getModelDesc(classLoaderUtil.getClass(dataModel).getAnnotation(DynamicModel.class).name());
                if (!dataModelMap.containsKey(md)) {
                    dataModelMap.put(md, new ArrayList<IDataModel>());
                }
                dataModelMap.get(md).add(dataModel);
            }
            for (ModelDescription md : dataModelMap.keySet()) {
                dataModelDao.delete(dataModelMap.get(md), md);
            }
        } else {
            dataModelDao.delete(dataModels, modelDescription);
        }
    }

    @Transactional
    public void configureTemplates(IDataModel dataModel, List<ITemplate> selectedTemplates) {
        classLoaderUtil.configureTemplates(dataModel, selectedTemplates);
        dataModelDao.update(dataModel);
    }

    public List<IDataModel> getDataModels(IDirectory parent, ModelDescription modelDescription, List<String> orderBy) {
        if (modelDescription == null) {
            List<IDataModel> dataModels = new ArrayList<IDataModel>();
            for (ModelDescription md : modelContainer.getModelDescs()) {
                dataModels.addAll(dataModelDao.getDataModels(parent, md, orderBy));
            }
            return dataModels;
        } else {
            return dataModelDao.getDataModels(parent, modelDescription, orderBy);
        }
    }

    @Transactional(readOnly = true)
    public List<ModelDescription> getModelDescriptions(IDirectory directory) {
        Object[] modelCount = directoryDao.getDirectoryModelCount(directory.getId());
        List<ModelDescription> result = new ArrayList<ModelDescription>();
        for (int i = 0; i < modelCount.length; i = i + 2) {
            String modelName = (String) modelCount[i];
            Integer count = (Integer) modelCount[i + 1];
            ModelDescription modelDescription = modelContainer.getModelDesc(modelName);
            if (count > 0 && modelDescription != null) {
                result.add(modelDescription);
            }
        }
        return result;
    }

    public List<IDataModel> getDataModels(String modelName, String name) {
        return this.dataModelDao.getDataModels(modelContainer.getModelDesc(modelName), name);
    }

    public IDataModel loadDataModel(String modelClass, Long id) {
        return this.dataModelDao.loadDataModels(classLoaderUtil.getClass(modelClass), id);
    }
}
