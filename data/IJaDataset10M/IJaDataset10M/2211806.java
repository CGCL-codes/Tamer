package net.sf.bootstrap.framework.dto;

import java.util.Map;

/**
 *
 * @author Mark Moloney
 */
public interface ObjectFactory {

    public Object newDTO(Object domainObject);

    public Object newDomainObject(Map properties);

    public Object newDomainObject(Object dto);
}
