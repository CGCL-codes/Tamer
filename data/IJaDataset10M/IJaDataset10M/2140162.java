package funiture.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import funiture.domains.model.bom.MaterialAttribute;
import funiture.domains.model.bom.MaterialAttributeDefine;
import funiture.domains.model.bom.MaterialObject;
import funiture.domains.model.bom.MaterialObjectModel;
import funiture.domains.model.bom.MaterialObjectModelName;
import funiture.domains.model.bom.MaterialObjectRespository;
import funiture.domains.model.bom.MaterialType;
import furniture.core.dao.RepositoryFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-core-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class MaterialObjectRespositoryImplTest {

    @Autowired
    private MaterialObjectRespository materialObjectRespository;

    @Autowired
    private RepositoryFacade repositoryFacade;

    @Test
    @Transactional
    @Rollback(false)
    public void storeMaterialAttributeDefine() {
        MaterialAttributeDefine attrDefine1 = new MaterialAttributeDefine("长");
        repositoryFacade.create(attrDefine1);
        MaterialAttributeDefine attrDefine2 = new MaterialAttributeDefine("宽");
        repositoryFacade.create(attrDefine2);
    }

    public MaterialAttributeDefine getAttributeDefine(String name) {
        return repositoryFacade.findBy(MaterialAttributeDefine.class, "name", name);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void storeMaterialAttribute() {
        List<MaterialAttribute> attributes = new ArrayList<MaterialAttribute>();
        MaterialAttribute attr1 = new MaterialAttribute(getAttributeDefine("长"), "10");
        MaterialAttribute attr2 = new MaterialAttribute(getAttributeDefine("宽"), "5");
        attributes.add(attr1);
        attributes.add(attr2);
        repositoryFacade.create(attr1);
        repositoryFacade.create(attr2);
    }

    public MaterialAttribute getMaterialAttribute(String attrName) {
        return repositoryFacade.findBy(MaterialAttribute.class, "attributeDefine.name", attrName);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void storeMaterialObjectModel() {
        List<MaterialAttribute> attributes = new ArrayList<MaterialAttribute>();
        attributes.add(getMaterialAttribute("长"));
        attributes.add(getMaterialAttribute("宽"));
        MaterialObjectModel define = new MaterialObjectModel(new MaterialObjectModelName("木板10M"), attributes, MaterialType.Material, 10, 1, null);
        MaterialObjectModel define1 = new MaterialObjectModel(new MaterialObjectModelName("木板20M"), attributes, MaterialType.Part, 10, 1, null);
        define1.addChild(define);
        materialObjectRespository.store(define1);
    }

    public MaterialObjectModel getMaterialObjectModel(String name) {
        return repositoryFacade.findBy(MaterialObjectModel.class, "modelName.name", name);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void storeMaterialObject() {
        MaterialObject object = new MaterialObject(getMaterialObjectModel("木板20M"));
        materialObjectRespository.store(object);
        MaterialObject findBy = materialObjectRespository.findBy(object.barCode());
        Assert.assertNotNull(findBy);
    }
}
