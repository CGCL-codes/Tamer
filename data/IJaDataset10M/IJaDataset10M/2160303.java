package org.fulworx.core.invoker;

import org.fulworx.core.descriptor.URITemplate;
import org.fulworx.core.descriptor.action.Accessor;
import org.fulworx.core.descriptor.action.ReadAction;
import org.fulworx.core.descriptor.action.UpdateAction;
import org.fulworx.core.rest.SomeAddrVO;
import org.fulworx.core.rest.SomeVO;

/**
 * User: teastlack Date: Oct 27, 2008 Time: 8:05:31 PM
 */
@URITemplate(uri = "/someUpdatingResource")
@Accessor("entity")
public class UpdatingAction implements ReadAction, UpdateAction {

    private String retValue;

    private SomeVO entity;

    public String read() throws Exception {
        entity = new SomeVO();
        entity.setFirstname("testFirst");
        entity.setLastname("testLast");
        SomeAddrVO addrVO = new SomeAddrVO();
        addrVO.setStreet1("testStreet1");
        addrVO.setStreet2("testStreet2");
        entity.setAddress(addrVO);
        entity.setValue(entity.getValue() + 1);
        return SUCCESS;
    }

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Method execute not yet implemented");
    }

    public String update() throws Exception {
        entity.setValue(entity.getValue() + 1);
        return SUCCESS;
    }

    public String getRetValue() {
        return retValue;
    }

    public void setRetValue(String retValue) {
        this.retValue = retValue;
    }

    public SomeVO getEntity() {
        return entity;
    }

    public void setEntity(SomeVO entity) {
        this.entity = entity;
    }
}
