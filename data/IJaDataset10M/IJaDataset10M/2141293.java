package org.torbs.engine.race;

import java.io.Serializable;
import org.torbs.util.TorbsExceptionNotExists;

/**
 */
public class Player implements Serializable {

    private String mName;

    protected Player(String name) {
        mName = name;
    }

    /**
   * @return Returns the name.
   */
    public String getName() {
        return mName;
    }

    public void remove() throws TorbsExceptionNotExists {
        PlayerFactory.getInstance().remove(mName);
    }

    public void rename(String name) throws TorbsExceptionNotExists {
        PlayerFactory.getInstance().rename(mName, name);
    }

    protected void setName(String name) {
        mName = name;
    }
}
