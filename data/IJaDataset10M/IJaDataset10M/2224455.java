package net.pleso.demo.client.dal.operation;

import net.pleso.demo.client.dal.BaseException;
import net.pleso.framework.client.dal.SelectParams;
import com.google.gwt.user.client.rpc.RemoteService;

public interface OperationService extends RemoteService {

    Operation[] select(SelectParams params) throws BaseException;

    int selectCount() throws BaseException;

    Operation[] selectByClient(SelectParams params) throws BaseException;

    int selectCountByClient(Operation searchRow) throws BaseException;

    Operation selectSingle(Integer operation_id) throws BaseException;

    void insert(Operation operation) throws BaseException;

    void update(Operation operation) throws BaseException;

    void delete(Integer operation_id) throws BaseException;
}
