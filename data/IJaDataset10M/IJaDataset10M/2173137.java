package ezinjector.test.model.impl;

import ezinjector.test.model.SingletonService;

public class SingletonServiceImpl implements SingletonService {

    private static SingletonServiceImpl instance;

    private SingletonServiceImpl() {
    }

    public static synchronized SingletonService getInstance() {
        if (instance == null) {
            instance = new SingletonServiceImpl();
        }
        return instance;
    }

    public boolean perform(Object obj) {
        return obj != null;
    }
}
