package jmemento.api.dao.db;

import jmemento.api.domain.user.IUserMeta;

/**
 * @author rusty
 * 
 */
public interface IUserMetaDao {

    /**
     * @param displayId
     * @return
     */
    IUserMeta getUserMetaByDisplayId(String displayId);

    /**
     * @param name
     * @return
     */
    IUserMeta getUserMetaByName(String name);

    /**
     * @param userName
     * @return
     */
    String getDisplayIdByName(String userName);

    /**
     * @param userMeta
     */
    void addNewUser(IUserMeta userMeta);

    /**
     * @param userId
     * @return
     */
    String getPasswordByDisplayId(String userId);

    /**
     * @param userId
     * @param password
     */
    void setPasswordByDisplayId(String userId, String password);
}
