package com.solido.objectkitchen.data;

import com.solido.objectkitchen.config.*;

public class SimpleTextColumn {

    private int id;

    private int type;

    private String name;

    public SimpleTextColumn(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public static SimpleTextColumn createFromConfig(ConfigurationSection config) {
        if (!config.containsKey("ID")) return null;
        if (!config.containsKey("NAME")) return null;
        if (!config.containsKey("TYPE")) return null;
        try {
            String name = config.getValue("NAME").trim();
            int type = Integer.parseInt(config.getValue("TYPE"));
            int id = Integer.parseInt(config.getValue("ID"));
            return new SimpleTextColumn(id, name, type);
        } catch (Exception e) {
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public ConfigurationSection getConfiguration() {
        ConfigurationSection config = new ConfigurationSection("COLUMN");
        config.setValue("ID", "" + getId());
        config.setValue("TYPE", "" + getType());
        config.setValue("NAME", "" + getName());
        return config;
    }
}
