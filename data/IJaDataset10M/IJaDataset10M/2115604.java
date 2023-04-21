package com.psm.core.plugins.model.plugin.model;

import com.psm.core.interfaces.IPluginDb;
import com.psm.core.interfaces.IPluginDbVo;
import com.psm.core.plugin.tag.TagPlugin;

public class PluginDb implements IPluginDb {

    private IPluginDbVo vo;

    public String getId() {
        return vo.getId();
    }

    public void setId(String id) {
        vo.setId(id);
    }

    public IPluginDbVo getVo() {
        return vo;
    }

    public PluginDb(TagPlugin pluginInfo) {
        vo = new PluginDbVo();
        this.setId(pluginInfo.getId());
        aggiorna(pluginInfo);
    }

    public void aggiorna(TagPlugin pluginInfo) {
        this.setDisattivabile(pluginInfo.getDisattivabile());
        this.setRole(pluginInfo.getRole());
        this.setVisibile(pluginInfo.getVisibile());
        this.setPluginType(pluginInfo.getPluginType().name());
    }

    public void setVo(PluginDbVo pluginDbVO) {
        this.vo = pluginDbVO;
    }

    public PluginDb(IPluginDbVo pluginDbVO) {
        this.vo = pluginDbVO;
    }

    public String getPluginType() {
        return vo.getPluginType();
    }

    public void setPluginType(String pluginType) {
        vo.setPluginType(pluginType);
    }

    public TagPlugin.ROLE getRole() {
        return TagPlugin.ROLE.valueOf(vo.getRole());
    }

    public void setRole(TagPlugin.ROLE role) {
        vo.setRole(role.name());
    }

    public Boolean getVisibile() {
        return vo.getVisibile();
    }

    public void setVisibile(Boolean visibile) {
        vo.setVisibile(visibile);
    }

    public Boolean getDisattivabile() {
        return vo.getDisattivabile();
    }

    public void setDisattivabile(Boolean disattivabile) {
        vo.setDisattivabile(disattivabile);
    }
}
