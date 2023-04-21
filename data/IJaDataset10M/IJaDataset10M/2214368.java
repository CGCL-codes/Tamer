package client;

public interface SSOSoap_PortType extends java.rmi.Remote {

    /**
     * 进行登录检验
     */
    public client.LoginResult loginCheck(java.lang.String systemid, java.lang.String sysAccount, java.lang.String sysPassword, java.lang.String gmccSystemid, java.lang.String loginid, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * 采用通用的GMCC存取角色号进行登录检验
     */
    public client.LoginResult loginCheck2(java.lang.String systemid, java.lang.String sysAccount, java.lang.String sysPassword, java.lang.String loginid, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * 让不能调用FSLLIAMS.SSO.DLL的第三方应用根据Cookie获取UserID
     */
    public client.ActionResult getUserID(java.lang.String systemid, java.lang.String sysAccount, java.lang.String sysPassword, boolean isgmccCookie, java.lang.String cookieValue) throws java.rmi.RemoteException;

    /**
     * 让不能调用FSLLIAMS.SSO.DLL的第三方应用根据Cookie获取Account
     */
    public client.ActionResult getUserAccount(java.lang.String systemid, java.lang.String sysAccount, java.lang.String sysPassword, boolean isgmccCookie, java.lang.String cookieValue) throws java.rmi.RemoteException;

    /**
     * 加密将要写入cookie的字符串，返回密文。原文格式必须是 userid|loginid
     */
    public client.ActionResult encryptStringForCookie(java.lang.String systemid, java.lang.String sysAccount, java.lang.String sysPassword, java.lang.String originalString) throws java.rmi.RemoteException;

    /**
     * 当用户每次访问进入一个应用系统时，向IAP填写一条用户访问记录
     */
    public client.ActionResult setAppAccessLog(java.lang.String systemID, java.lang.String sysAccount, java.lang.String sysPassword, java.lang.String loginID) throws java.rmi.RemoteException;
}
