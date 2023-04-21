package org.albianj.config.entity;

public class ConfigAttribute implements IConfigAttribute {

    private IConfigStorageAttribute configStorage;

    private IConfigNotifyAttribute configNotify;

    private Role role = Role.Client;

    private LoadMode loadMode = LoadMode.Lazyload;

    public void setLoadMode(LoadMode loadMode) {
        this.loadMode = loadMode;
    }

    public LoadMode getLoadMode() {
        return this.loadMode;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return this.role;
    }

    public IConfigStorageAttribute getConfigStorage() {
        return this.configStorage;
    }

    public void setConfigStorage(IConfigStorageAttribute configStorage) {
        this.configStorage = configStorage;
    }

    public IConfigNotifyAttribute getConfigNotify() {
        return this.configNotify;
    }

    public void setConfigNotify(IConfigNotifyAttribute configNotify) {
        this.configNotify = configNotify;
    }
}
