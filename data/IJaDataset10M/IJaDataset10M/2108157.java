package org.polepos.teams.db4o;

import org.polepos.framework.*;
import com.db4o.ext.*;

public abstract class Db4oCar extends Car {

    private String name;

    protected Db4oCar(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    public void initialize() {
    }

    public abstract ExtObjectContainer createObjectContainer();
}
