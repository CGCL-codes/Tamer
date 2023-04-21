package org.fulworx.core.invoker;

import org.apache.log4j.Logger;
import org.fulworx.core.descriptor.URITemplate;
import org.fulworx.core.descriptor.action.Accessor;
import org.fulworx.core.descriptor.action.CreateAction;
import org.fulworx.core.descriptor.action.ReadAction;
import org.fulworx.core.descriptor.action.UpdateAction;
import org.fulworx.core.invoker.errors.BundledErrorDetail;
import org.fulworx.core.rest.SomeAddrVO;
import org.fulworx.core.rest.SomeVO;
import java.util.ResourceBundle;

/**
 * @version $Id: $
 */
@URITemplate(uri = "/someResource")
@Accessor("entity")
public class SomeAction implements CreateAction, ReadAction, UpdateAction {

    private static final Logger log = Logger.getLogger(SomeAction.class.getName());

    private String retValue;

    private SomeVO entity;

    public String getRetValue() {
        return retValue;
    }

    public void setRetValue(String retValue) {
        this.retValue = retValue;
    }

    public String create() {
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
        return retValue;
    }

    public String read() {
        log.info("READING from SomeAction");
        entity = new SomeVO();
        entity.setFirstname("testFirst");
        entity.setLastname("testLast");
        return SUCCESS;
    }

    public String update() {
        throw new ApplicationException(new BundledErrorDetail("111", ResourceBundle.getBundle("ErrorMessages"), new Object[] { "someAction error" }), 404);
    }

    public SomeVO getEntity() {
        return entity;
    }

    public void setEntity(SomeVO entity) {
        this.entity = entity;
    }
}
