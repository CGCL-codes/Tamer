package com.bft.commons.enterprise.passport.dal;

import com.bft.commons.enterprise.passport.businessObjects.SignupCriteria;
import com.bft.commons.enterprise.passport.businessObjects.UserInfo;
import com.bft.commons.standard.db.IBATBaseDAO;
import com.bft.commons.standard.exception.MrdnSystemException;

public class SQLMapSignupDAO extends IBATBaseDAO implements SignupDAO {

    public UserInfo validate(SignupCriteria signupCriteria) throws MrdnSystemException {
        return null;
    }

    public void changePassword(SignupCriteria signupCriteria) throws MrdnSystemException {
    }
}
