package wolf.adminpanel.accesspoint.ws.impl;

public class IAdminPanelAccessPoint_isAuthorized_ResponseStruct {

    protected wolf.adminpanel.accesspoint.ws.impl.UserVO result;

    public IAdminPanelAccessPoint_isAuthorized_ResponseStruct() {
    }

    public IAdminPanelAccessPoint_isAuthorized_ResponseStruct(wolf.adminpanel.accesspoint.ws.impl.UserVO result) {
        this.result = result;
    }

    public wolf.adminpanel.accesspoint.ws.impl.UserVO getResult() {
        return result;
    }

    public void setResult(wolf.adminpanel.accesspoint.ws.impl.UserVO result) {
        this.result = result;
    }
}
