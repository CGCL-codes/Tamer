package com.inet.qlcbcc.facade.support;

import static com.inet.qlcbcc.util.Utils.split;
import static org.webos.core.util.StringUtils.hasText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.webos.core.convert.TypeDescriptor;
import org.webos.core.message.ResponseMessage;
import org.webos.core.option.Function;
import org.webos.core.option.Option;
import org.webos.core.service.BusinessServiceException;
import com.inet.qlcbcc.dto.ErrorMessage;
import com.inet.qlcbcc.dto.UserCriteria;
import com.inet.qlcbcc.dto.UserPassword;
import com.inet.qlcbcc.facade.AbstractFacade;
import com.inet.qlcbcc.facade.UserFacade;
import com.inet.qlcbcc.facade.UserModificationException;
import com.inet.qlcbcc.facade.result.UserFacadeResultFactory;
import com.inet.qlcbcc.facade.result.UserResult;
import com.inet.qlcbcc.service.AuthenticationModeService;
import com.inet.qlcbcc.service.DictionaryService;
import com.inet.qlcbcc.service.RoleService;
import com.inet.qlcbcc.service.UserService;
import com.inet.qlcbcc.domain.AdministrativeUnit;
import com.inet.qlcbcc.domain.AuthenticationMode;
import com.inet.qlcbcc.domain.Dictionary;
import com.inet.qlcbcc.domain.Role;
import com.inet.qlcbcc.domain.User;

/**
 * UserFacadeSupport.
 *
 * @author Dung Nguyen
 * @version $Id: UserFacadeSupport.java 2011-05-17 01:11:11z nguyen_dv $
 *
 * @since 1.0
 */
@Component("userFacade")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UserFacadeSupport extends AbstractFacade implements UserFacade {

    private static final Logger LOG = LoggerFactory.getLogger(UserFacadeSupport.class);

    @Autowired(required = false)
    private UserFacadeResultFactory userResultFactory;

    @Autowired(required = false)
    @Qualifier("userService")
    private UserService userService;

    @Autowired(required = false)
    @Qualifier("roleService")
    private RoleService roleService;

    @Autowired(required = false)
    @Qualifier("authenticationModeService")
    private AuthenticationModeService authModeService;

    @Autowired(required = false)
    @Qualifier("dictionaryService")
    private DictionaryService dictionaryService;

    public ResponseMessage findById(final String id, final int fetchMode) {
        try {
            Option<User> user = userService.findById(id);
            if (!user.isDefined()) {
                return createFailureResponse("dkkd.user.not_exists");
            }
            return user.map(new Function<User, ResponseMessage>() {

                public ResponseMessage apply(User user) {
                    return createSuccessResponse(userResultFactory.make(fetchMode, user));
                }
            }).get();
        } catch (final BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An exception occurs during finding the user from the given id [{}], overcome this.", new Object[] { id, ex });
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An exception occurs during finding the user from the given id [{}] with message [{}], overcome this.", new Object[] { id, ex.getMessage() });
            }
            return createFailureResponse(ex.getMessage());
        }
    }

    public ResponseMessage findByCode(final String usercode, final int fetchMode) {
        try {
            Option<User> user = userService.findByCode(usercode);
            if (!user.isDefined()) {
                return createFailureResponse("dkkd.user.not_exists");
            }
            return user.map(new Function<User, ResponseMessage>() {

                public ResponseMessage apply(User user) {
                    return createSuccessResponse(userResultFactory.make(fetchMode, user));
                }
            }).get();
        } catch (final BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An exception occurs during finding the user from the given usercode [{}], overcome this.", new Object[] { usercode, ex });
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An exception occurs during finding the user from the given usercode [{}] with message [{}], overcome this.", new Object[] { usercode, ex.getMessage() });
            }
            return createFailureResponse(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = UserModificationException.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage save(User user) {
        try {
            userService.save(user);
            return createSuccessResponse();
        } catch (BusinessServiceException ex) {
            throw new UserModificationException(ex.getMessage(), ex);
        }
    }

    @Transactional(rollbackFor = UserModificationException.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage update(User user) {
        try {
            userService.update(user);
            return createSuccessResponse();
        } catch (BusinessServiceException ex) {
            throw new UserModificationException(ex.getMessage(), ex);
        }
    }

    @Transactional(rollbackFor = UserModificationException.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage updateProfile(User user) {
        try {
            userService.updateProfile(user);
            return createSuccessResponse();
        } catch (BusinessServiceException ex) {
            throw new UserModificationException(ex.getMessage(), ex);
        }
    }

    @Transactional(rollbackFor = UserModificationException.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage delete(String ids) {
        try {
            if (!validateId(ids)) {
                return createInvalidIdResponse();
            }
            ErrorMessage errorMessage = userService.delete(split(ids, idGenerator.size()));
            if (ErrorMessage.NULL.equals(errorMessage)) {
                return createSuccessResponse();
            } else {
                return createFailureResponse("dkkd.user.delete_failure", errorMessage);
            }
        } catch (BusinessServiceException ex) {
            throw new UserModificationException(ex.getMessage(), ex);
        }
    }

    @Transactional(rollbackFor = UserModificationException.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage lock(String... ids) {
        try {
            userService.lock(ids);
            return createSuccessResponse();
        } catch (final BusinessServiceException ex) {
            throw new UserModificationException(ex.getMessage(), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage unlock(String... ids) {
        try {
            userService.unlock(ids);
            return createSuccessResponse();
        } catch (final BusinessServiceException ex) {
            return createFailureResponse(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = UserModificationException.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage resetPassword(UserPassword userPassword) {
        try {
            if (!hasText(userPassword.getPassword()) && userPassword.getAuthMode() == null) {
                return createFailureResponse("dkkd.user.reset_password_invalid");
            }
            userService.resetPassword(userPassword.getId(), userPassword.getPassword(), userPassword.getAuthMode());
            return createSuccessResponse();
        } catch (final BusinessServiceException ex) {
            throw new UserModificationException(ex.getMessage(), ex);
        }
    }

    @Transactional(rollbackFor = UserModificationException.class, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ResponseMessage changePassword(UserPassword userPassword) {
        try {
            if (!hasText(userPassword.getPassword()) && userPassword.getAuthMode() == null) {
                return createFailureResponse("dkkd.user.change_password_invalid");
            }
            userService.changePassword(userPassword.getId(), userPassword.getCurrentPassword(), userPassword.getPassword(), userPassword.getAuthMode());
            return createSuccessResponse();
        } catch (BusinessServiceException ex) {
            throw new UserModificationException(ex.getMessage(), ex);
        }
    }

    public ResponseMessage findBy(UserCriteria criteria) {
        try {
            return createSuccessResponse(userService.findBy(criteria));
        } catch (BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An error occurs during finding the user from the given criteria, overcome this.", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An error occurs during finding the user from the given criteria with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse("dkkd.user.find_by");
        }
    }

    /**
   * @see com.inet.qlcbcc.facade.UserFacade#findByUsername(java.lang.String)
   */
    public ResponseMessage findPermissionByUsername(String username) {
        try {
            Option<User> user = userService.findByUsername(username);
            if (!user.isDefined()) {
                return createFailureResponse("dkkd.user.not_exist");
            }
            Set<String> permissions = new HashSet<String>();
            for (Role role : user.get().getRoles()) {
                for (com.inet.qlcbcc.domain.Function function : role.getFunctions()) {
                    permissions.add(function.getKey());
                }
            }
            return createSuccessResponse(permissions, TypeDescriptor.valueOfList(String.class));
        } catch (BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An error occurs during finding the user from the given username, overcome this.", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An error occurs during finding the user from the given username with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse("dkkd.user.find_permission_username");
        }
    }

    public ResponseMessage findPermissionByType(String username, String permissionType) {
        try {
            Option<User> user = userService.findByUnameAndFunctionType(username, permissionType);
            Set<String> permissions = new HashSet<String>();
            if (user.isDefined()) {
                for (Role role : user.get().getRoles()) {
                    for (com.inet.qlcbcc.domain.Function function : role.getFunctions()) {
                        permissions.add(function.getKey());
                    }
                }
            }
            return createSuccessResponse(permissions, TypeDescriptor.valueOfList(String.class));
        } catch (BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An error occurs during finding the user from the given username, overcome this.", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An error occurs during finding the user from the given username with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse("dkkd.user.find_permission_username");
        }
    }

    public List<AuthenticationMode> findAllAuthModes() {
        try {
            return authModeService.findAll();
        } catch (BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An error occurs during listing all authentication modes, overcome this.", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An error occurs during listing all authentication modes with message [{}], overcome this.", ex.getMessage());
            }
            return Collections.<AuthenticationMode>emptyList();
        }
    }

    public List<Role> findAllRoles() {
        try {
            return roleService.findAll();
        } catch (BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An error occurs during listing all roles, overcome this.", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An error occurs during listing all roles with message [{}], overcome this.", ex.getMessage());
            }
            return Collections.<Role>emptyList();
        }
    }

    public List<Dictionary> findDepartments() {
        try {
            return dictionaryService.findByKey("USER_DEPARTMENT", 1);
        } catch (BusinessServiceException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An error occurs during listing all deparments, overcome this.", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An error occurs during listing all deparments with message [{}], overcome this.", ex.getMessage());
            }
        }
        return Collections.<Dictionary>emptyList();
    }

    @Override
    public List<AdministrativeUnit> findAllAdministrative(String id) throws BusinessServiceException {
        User user = userResultFactory.make(UserResult.UNIT, userService.findById(getLoginUser().getId()).get()).getUser();
        List<AdministrativeUnit> units = new ArrayList<AdministrativeUnit>();
        for (AdministrativeUnit unit : user.getUnits()) {
            units.add(unit);
        }
        return units;
    }
}
