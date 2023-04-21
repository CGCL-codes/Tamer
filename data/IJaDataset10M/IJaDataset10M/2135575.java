package com.das.user.logic;

import com.das.core.logic.BizModel;

public interface User extends BizModel {

    public static final String FLD_ID = "id";

    public static final String FLD_USERID = "userid";

    public static final String FLD_PASSWD = "passwd";

    public static final String FLD_NAME = "name";

    public static final String FLD_EMAIL = "email";

    public static final String FLD_ALWAYSLOGIN = "alwaysLogin";

    public Long getId();

    public void setId(Object id);

    public String getUserid();

    public void setUserid(Object userid);

    public String getPasswd();

    public void setPasswd(Object passwd);

    public String getName();

    public void setName(Object name);

    public String getEmail();

    public void setEmail(Object email);

    public String getAlwaysLogin();

    public void setAlwaysLogin(Object alwaysLogin);

    public int doCountByUseridPasswd(User user) throws Exception;

    public User doRetrieveByUserid(String userid);
}
