package com.taobao.api.model;

/**
 * @author sulinchong.pt
 * 
 */
public class UserGetResponse extends TaobaoResponse {

    /**
	 * 
	 */
    private static final long serialVersionUID = 233273952439627981L;

    private User user;

    public UserGetResponse() {
        super();
    }

    public UserGetResponse(TaobaoResponse rsp) {
        super(rsp);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
