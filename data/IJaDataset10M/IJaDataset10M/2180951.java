package net.infian.framework.db.dao;

import java.util.HashMap;

/**
 * This class defines a basic Dao.<br />
 * <br />
 * It is easily extended to include setters and getters by making calls to the HashMap methods, put(String) and get(String).<br />
 * <br />
 * Example:<br />
 * <br />
 * {@code public final Integer getValue() &#123;}<br />
 * {@code 	return (Integer) get("value");}<br />
 * {@code &#125;}<br />
 * <br />
 * {@code public final void setValue(Integer value) &#123;}<br />
 * {@code 	put("value", value);}<br />
 * {@code &#125;}
 */
@SuppressWarnings("serial")
public class Dao extends HashMap<String, Object> {

    /**
	 * This is the DaoInfo contained in each Dao.
	 */
    public final DaoInfo info;

    /**
	 * This is a constructor using a DaoInfo object.
	 * @param info DaoInfo for the Dao
	 */
    public Dao(DaoInfo info) {
        super(info.columns.length, 1);
        this.info = info;
    }

    /**
	 * This is a constructor that pulls the DaoInfo from the DaoManager by the name of the Dao.
	 * @param dao name of the Dao
	 */
    public Dao(String dao) {
        this(DaoManager.getDaoInfo(dao));
    }
}
