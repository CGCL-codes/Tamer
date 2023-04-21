package com.openthinks.woms.account.rest;

import java.util.Collection;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import com.openthinks.woms.account.AccountGroup;
import com.openthinks.woms.account.service.AccountGroupService;
import com.openthinks.woms.rest.GenericRestfulController;

@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "accountgroup" }) })
public class AccountgroupController extends GenericRestfulController {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private AccountGroup model = new AccountGroup();

    private Collection<AccountGroup> list;

    private AccountGroupService accountGroupService;

    @Override
    public Object getModel() {
        if (message != null) return message; else return (list != null ? list : model);
    }

    public void setAccountGroupService(AccountGroupService accountGroupService) {
        this.accountGroupService = accountGroupService;
    }

    @Override
    public void validate() {
    }

    @Override
    public HttpHeaders show() {
        try {
            model = accountGroupService.find(model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("index").disableCaching();
    }

    @Override
    public HttpHeaders index() {
        try {
            list = accountGroupService.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("index").disableCaching();
    }

    @Override
    public HttpHeaders create() {
        try {
            accountGroupService.create(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success").setLocationId(model.getId());
    }

    @Override
    public String update() {
        try {
            accountGroupService.update(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Override
    public String destroy() {
        try {
            accountGroupService.delete(model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
