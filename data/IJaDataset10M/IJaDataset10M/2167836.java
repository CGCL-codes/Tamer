package com.mainatom.db;

import com.mainatom.af.*;
import com.mainatom.tab.*;
import com.mainatom.utils.*;
import java.util.*;

public class DataBaseService extends AService {

    private static DataBaseService _inst;

    private HashMap<String, ADataBase> _registred;

    public static DataBaseService getInst() {
        if (_inst == null) {
            _inst = new DataBaseService();
        }
        return _inst;
    }

    protected void onInit() throws Exception {
        super.onInit();
        _registred = new HashMap<String, ADataBase>();
    }

    public void registerDb(String name, ADataBase db) {
        _registred.put(name, db);
    }

    public ADataBase getDb(String name) {
        ADataBase db = _registred.get(name);
        if (db == null) {
            throw new MfError("База данных с именем [{0}] не зарегистрирована", name);
        }
        return db;
    }

    /**
     * Создать список таблиц, которые определяют логическую структуру базы.
     *
     * @param tagName имя тега, которым помечены классы - таблицы
     * @return списко созданных таблиц
     */
    public ListAObject<ATable> createDbStruct(String tagName) {
        ListObject<DeclareObject> lst = getDef().getListOwnerTag("table", tagName);
        ListAObject<ATable> res = new ListAObject<ATable>(null);
        for (DeclareObject dc : lst) {
            res.set(dc.getName(), (ATable) getDef().create(dc));
        }
        return res;
    }
}
