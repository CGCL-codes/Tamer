package com.cell.rpg.template;

import com.cell.rpg.NamedObject;
import com.cell.rpg.RPGObject;

public abstract class TemplateNode extends RPGObject implements NamedObject {

    private transient int int_id;

    public String name = "no name";

    public TemplateNode(int id, String name) {
        super(id + "");
        this.int_id = id;
        this.name = name;
    }

    @Override
    protected void init_transient() {
        super.init_transient();
        if (id != null) {
            this.int_id = Integer.parseInt(id);
        }
    }

    public final int getIntID() {
        return int_id;
    }

    @Override
    public String getName() {
        return name;
    }
}
