package pl.xperios.rdk.server.rpcservices;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.ArrayList;
import java.util.HashMap;
import org.mybatis.guice.transactional.Transactional;
import pl.xperios.rdk.client.commons.GenericRpcService;
import pl.xperios.rdk.client.commons.Result;
import pl.xperios.rdk.client.commons.XParameters;
import pl.xperios.rdk.server.mapper.GenericFullMapper;
import pl.xperios.rdk.server.services.UserManager;
import pl.xperios.rdk.shared.Bean;
import pl.xperios.rdk.shared.XLog;
import pl.xperios.rdk.shared.beans.RoleAction;
import pl.xperios.rdk.shared.exceptions.ApplicationException;

/**
 *
 * @param <T>
 * @author Praca
 */
public abstract class GenericFullDao<T extends Bean<Long>> implements GenericRpcService<T> {

    @Inject
    Injector injector;

    GenericFullMapper<T> mapper;

    @Inject
    UserManager userManager;

    public Result<T> get(XParameters parameters) throws ApplicationException {
        XLog.debug("DAO: GET beans for: " + parameters);
        checkPermissions(getRoleName(), RoleAction.GET, isCheckingPermissionsRequiredForSet());
        try {
            if (parameters == null) {
                parameters = new XParameters();
            }
            HashMap<String, Object> param = getHashMapFromParameters(parameters);
            ArrayList<T> list = getMapper().get(param);
            XLog.debug("DAO: GET Count: " + list.size());
            Result<T> result = new Result<T>();
            if (parameters.getCounting()) {
                int count = getMapper().count(getHashMapFromParameters(parameters));
                XLog.debug("DAO: GET Count Total: " + count);
                result.setTotalSize(count);
            }
            result.setOffset(parameters.getOffset());
            result.setLimit(list.size());
            result.setList(list);
            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ApplicationException(exception.getMessage());
        }
    }

    /**
     *
     * @param bean
     * @throws ApplicationException
     */
    @Transactional
    public Long save(T bean) throws ApplicationException {
        XLog.debug("DAO: SAVE bean: " + bean);
        try {
            if (null == bean.getId()) {
                checkPermissions(getRoleName(), RoleAction.INSERT, isCheckingPermissionsRequiredForSet());
                mapper.inserting(bean);
                return bean.getId();
            }
            checkPermissions(getRoleName(), RoleAction.UPDATE, isCheckingPermissionsRequiredForSet());
            mapper.update(bean);
            return bean.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Baza danych zwróciła błąd: " + e.getMessage());
        }
    }

    /**
     *
     * @param bean
     * @throws ApplicationException
     */
    public void del(T bean) throws ApplicationException {
        XLog.debug("DAO: DEL bean: " + bean);
        checkPermissions(getRoleName(), RoleAction.DEL, isCheckingPermissionsRequiredForDel());
        try {
            mapper.delete(bean.getId());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Baza danych zwróciła błąd: " + e.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public abstract Class<? extends GenericFullMapper<T>> getMapperClass();

    private HashMap<String, Object> getHashMapFromParameters(XParameters parameters) {
        if (parameters == null) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            return map;
        }
        HashMap<String, Object> out = parameters.getParameters();
        validateParameters(out);
        return out;
    }

    public GenericFullMapper<T> getMapper() throws ApplicationException {
        if (mapper == null) {
            Class<? extends GenericFullMapper<T>> mapperClass = getMapperClass();
            mapper = injector.getInstance(mapperClass);
            if (mapper == null) {
                throw new ApplicationException("Nie można zainicjować poprawnie mappera: " + mapperClass.getName());
            }
        }
        return mapper;
    }

    private void validateParameters(HashMap<String, Object> out) {
        if (!out.containsKey("_columns_visible")) {
            ArrayList<String> columnsVisible = new ArrayList<String>();
            columnsVisible.add("*");
            out.put("_columns_visible", columnsVisible);
        }
    }

    protected String getRoleName() {
        if (initRoleName() == null) {
            return null;
        }
        return initRoleName();
    }

    private void checkPermissions(String roleName, int roleType, boolean isCheckingPermissionsRequired) throws ApplicationException {
        if (!isCheckingPermissionsRequired) {
            return;
        }
        if (!userManager.isUserLogged()) {
            throw new ApplicationException("User is not logged in.");
        }
        if (!userManager.hasRole(roleName, roleType)) {
            throw new ApplicationException("User doesn't have role \"" + roleName + "\"");
        }
    }

    protected String initRoleName() {
        return null;
    }

    protected boolean isCheckingPermissionsRequiredForGet() {
        return true;
    }

    protected boolean isCheckingPermissionsRequiredForSet() {
        return true;
    }

    protected boolean isCheckingPermissionsRequiredForDel() {
        return true;
    }
}
