package com.inet.qlcbcc.facade.result;

import com.inet.qlcbcc.domain.User;

/**
 * UserFacadeResultFactory.
 *
 * @author Dung Nguyen
 * @version $Id: UserFacadeResultFactory.java 2011-05-16 19:34:41z nguyen_dv $
 *
 * @since 1.0
 */
public interface UserFacadeResultFactory {

    /**
   * Makes the {@link UserResult} object from the given fetch mode and the {@link User} information.
   *
   * @param fetchMode the given fetch mode.
   * @param user the given {@link User user} information.
   * @return the {@link UserResult} object.
   */
    UserResult make(int fetchMode, User user);
}
