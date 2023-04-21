package com.clican.pluto.orm.dynamic;

import java.util.List;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.orm.BaseTestCase;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.desc.PropertyDescription;
import com.clican.pluto.orm.dynamic.impl.DynamicClassLoader;
import com.clican.pluto.orm.dynamic.inter.DynamicORMManage;

public class DynamicORMManageHibernateImplTestCase extends BaseTestCase {

    private DynamicORMManage dynamicORMManage;

    private DynamicClassLoader dynamicClassLoader;

    public void setDynamicORMManage(DynamicORMManage dynamicORMManage) {
        this.dynamicORMManage = dynamicORMManage;
    }

    public void setDynamicClassLoader(DynamicClassLoader dynamicClassLoader) {
        this.dynamicClassLoader = dynamicClassLoader;
    }

    public void testGenerateORM() throws Exception {
        ModelDescription modelDescription = new ModelDescription();
        modelDescription.setName("Test");
        List<PropertyDescription> list = modelDescription.getPropertyDescriptionList();
        PropertyDescription pd1 = new PropertyDescription();
        pd1.setName("nickName");
        pd1.setType(new StringType());
        PropertyDescription pd2 = new PropertyDescription();
        pd2.setName("password");
        pd2.setType(new StringType());
        list.add(pd1);
        list.add(pd2);
        modelDescription.setPropertyDescriptionList(list);
        dynamicORMManage.generateORM(modelDescription);
        dynamicClassLoader.loadClass("com.clican.pluto.orm.model.dynamic.Test").newInstance();
    }
}
