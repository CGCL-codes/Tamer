package com.scissor.xmlconfig;

import java.util.Collection;
import java.util.Collections;

public abstract class ConfigItem {

    private final ConfigItem parent;

    private boolean used = false;

    protected static final ConfigItem ROOT = new RootConfigItem();

    private static final Collection<ConfigItem> EMPTY_CONFIG_ITEM_LIST = Collections.emptyList();

    public ConfigItem(ConfigItem parent) {
        this.parent = parent;
    }

    public String path() {
        return parent.path() + "/" + getName();
    }

    public abstract String getName();

    public boolean isUsed() {
        return used;
    }

    public void markUsed() {
        used = true;
    }

    public Collection<ConfigItem> getUnusedItems() {
        return used ? EMPTY_CONFIG_ITEM_LIST : Collections.singletonList(this);
    }

    public String toString() {
        return path();
    }

    public String text() {
        markUsed();
        return "";
    }

    public Format format() {
        return parent.format();
    }

    private static class RootConfigItem extends ConfigItem {

        public RootConfigItem() {
            super(null);
        }

        public String getName() {
            return "";
        }

        public String path() {
            return "";
        }

        public Format format() {
            return Format.NULL;
        }
    }
}
