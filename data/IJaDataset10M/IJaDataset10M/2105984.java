package com.m4f.business.service.ifc;

import java.util.Collection;
import java.util.List;
import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;

public interface UserService {

    InternalUser createUser();

    List<InternalUser> getAllUser() throws Exception;

    InternalUser getUser(Long id) throws Exception;

    InternalUser getUser(String email) throws Exception;

    void save(InternalUser user) throws Exception;

    void delete(InternalUser user) throws Exception;

    Collection<InternalUser> findUsersByMediationService(MediationService mediationService) throws Exception;

    long countUsers() throws Exception;

    Collection<InternalUser> getUsersByRange(String ordering, int init, int end) throws Exception;
}
