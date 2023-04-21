package ru.scriptum.model.service;

import java.util.List;
import ru.scriptum.model.data.IElement;
import ru.scriptum.model.data.IPersistable;
import ru.scriptum.model.data.ITemplateElement;
import ru.scriptum.model.domaindata.User;
import ru.scriptum.model.exception.BeanException;

public interface IBeanService {

    public boolean elementExists(Object value, String id);

    public boolean elementExists(String elementName);

    public void delete(ITemplateElement hetero) throws BeanException;

    public List<IElement> findByParameter(String paramName, String paramValue);

    public List<ITemplateElement> findByTemplateType(String templateType);

    public List<IElement> getAll() throws BeanException;

    public List<IElement> getAllRoots(String relationType);

    public List<IElement> getAllRoots(String relationType, User user);

    public IPersistable getPersistable(long l);

    public boolean otherElementExists(Long id, String name);

    public IElement save(IElement element) throws BeanException;

    public IElement update(IElement element) throws BeanException;

    public IElement findByName(String name);
}
