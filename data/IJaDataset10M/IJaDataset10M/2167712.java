package com.sitechasia.webx.core.utils.dozer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.dozer.util.mapping.DozerBeanMapper;
import net.sf.dozer.util.mapping.util.CollectionUtils;
import com.sitechasia.webx.core.model.IDomainObject;
import com.sitechasia.webx.core.model.IViewObject;
import com.sitechasia.webx.core.utils.base.ResourceBinder;

public class DozerConvertUtilImle implements DozerConvertUtil {

    private DozerBeanMapper dozerBeanMapper;

    public void setDozerBeanMapper(DozerBeanMapper dozerBeanMapper) {
        this.dozerBeanMapper = dozerBeanMapper;
    }

    public void domainObjectToViewObject(IDomainObject domainObject, IViewObject viewObject) {
        dozerBeanMapper.map(domainObject, viewObject);
    }

    public List domainObjectsToViewObjects(List domainObjects, Class viewObject) {
        List viewObjects = new ArrayList(0);
        try {
            if (CollectionUtils.isCollection(domainObjects.getClass())) {
                int domainObjects_length = CollectionUtils.getLengthOfCollection(domainObjects);
                viewObjects = new ArrayList(domainObjects_length);
                for (int i = 0; i < domainObjects_length; i++) {
                    IViewObject vo = (IViewObject) viewObject.newInstance();
                    dozerBeanMapper.map(CollectionUtils.getValueFromCollection(domainObjects, i), vo);
                    viewObjects.add(vo);
                }
            }
        } catch (Exception e) {
            viewObjects = new ArrayList(0);
        }
        return viewObjects;
    }

    public void viewObjectToDomainObject(IViewObject viewObject, IDomainObject domainObject) {
        dozerBeanMapper.map(viewObject, domainObject);
    }

    public List viewObjectsToDomainObjects(List viewObjects, Class domainObject) {
        List domainObjects = new ArrayList(0);
        try {
            if (CollectionUtils.isCollection(viewObjects.getClass())) {
                int viewObjects_length = CollectionUtils.getLengthOfCollection(viewObjects);
                domainObjects = new ArrayList(viewObjects_length);
                for (int i = 0; i < viewObjects_length; i++) {
                    IDomainObject DO = (IDomainObject) domainObject.newInstance();
                    dozerBeanMapper.map(CollectionUtils.getValueFromCollection(viewObjects, i), DO);
                    domainObjects.add(DO);
                }
            }
        } catch (Exception e) {
            domainObjects = new ArrayList(0);
        }
        return domainObjects;
    }

    /**
	 * 
	 * 构造方法，功能是可按照通配符描述加载dozer mapping配置文件
	 * 
	 * @param dozerResourceBinder
	 * @see com.sitechasia.webx.core.utils.base.ResourceBinder
	 */
    public DozerConvertUtilImle(ResourceBinder dozerResourceBinder) {
        String[] files = dozerResourceBinder.getResourceFile();
        List mappingFiles = Arrays.asList(files);
        this.dozerBeanMapper = new DozerBeanMapper();
        this.dozerBeanMapper.setMappingFiles(mappingFiles);
    }

    /**
	 * 构造方法
	 */
    public DozerConvertUtilImle() {
    }
}
