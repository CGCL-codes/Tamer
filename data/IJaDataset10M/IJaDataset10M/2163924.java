package com.googlecode.jerato.library.store;

import java.util.List;
import java.util.Map;
import com.googlecode.jerato.core.Service;
import com.googlecode.jerato.core.store.StoreFunction;
import com.googlecode.jerato.core.store.StoreTransaction;
import com.googlecode.jerato.core.store.StoreTransfer;
import com.googlecode.jerato.library.FunctionService;
import com.googlecode.jerato.library.Settings;
import com.googlecode.jerato.library.SystemException;
import com.googlecode.jerato.library.SystemService;

public class StoreService implements Service {

    protected static final String SETTINGS_NAME = "com.googlecode.jerato.library.JeratoDataSettings";

    protected Settings _settings = new Settings(SETTINGS_NAME);

    protected FunctionService _functionServie;

    public static StoreService getInstance() {
        StoreService dataService = (StoreService) SystemService.getInstance().getService("dataService");
        return dataService;
    }

    public void shutdown() {
    }

    public void startup() {
        _settings.load();
        _functionServie = SystemService.getInstance().getFunction();
    }

    public StoreFunction getData(Class dataClass) {
        if (dataClass == null) {
            throw new IllegalArgumentException("dataClass is null");
        }
        if (_functionServie.exists(dataClass.getName())) {
            return (StoreFunction) _functionServie.get(dataClass.getName());
        }
        try {
            return (StoreFunction) dataClass.newInstance();
        } catch (InstantiationException e) {
            throw new SystemException("Data function create failed.", e);
        } catch (IllegalAccessException e) {
            throw new SystemException("Data function create failed.", e);
        }
    }

    public StoreFunction getData(String dataName) {
        if (dataName == null) {
            throw new IllegalArgumentException("dataName is null");
        }
        if (_functionServie.exists(dataName)) {
            return (StoreFunction) _functionServie.get(dataName);
        }
        throw new SystemException("Data function not found.");
    }

    public List select(StoreTransfer trans, Class dataClass, Map map) {
        StoreFunction func = getData(dataClass);
        StoreParametersImpl input = new StoreParametersImpl();
        if (map != null) {
            input.putAll(map);
        }
        StoreParametersImpl output = new StoreParametersImpl();
        func.execute(trans, input, output);
        return output.getResultList();
    }

    public int update(StoreTransfer trans, Class dataClass, Map map) {
        StoreFunction func = getData(dataClass);
        StoreParametersImpl input = new StoreParametersImpl();
        if (map != null) {
            input.putAll(map);
        }
        StoreParametersImpl output = new StoreParametersImpl();
        func.execute(trans, input, output);
        return output.getUpdateCount();
    }

    public static void main(String[] args) {
        StoreTransaction transaction = null;
        try {
            SystemService.staticInitialize();
            transaction = StoreTransactionService.beginTransaction();
        } finally {
            StoreTransactionService.endTransaction(transaction);
            SystemService.staticFinalize();
        }
    }
}
