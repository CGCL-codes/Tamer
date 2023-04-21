package org.javanuke.core.model.dto.dbobjects;

import org.javanuke.core.model.dto.DataTransferObjectSupport;

public class Module extends DataTransferObjectSupport implements SimpleDBObjectInterface {

    private Integer active;

    private String customTitle;

    private Integer id;

    private Integer inMenu;

    private String title;

    private Integer view;

    public Integer getActive() {
        return this.active;
    }

    public void setActive(Integer param) {
        this.active = param;
    }

    public String getCustomTitle() {
        return this.customTitle;
    }

    public void setCustomTitle(String param) {
        this.customTitle = param;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer param) {
        this.id = param;
    }

    public Integer getInMenu() {
        return this.inMenu;
    }

    public void setInMenu(Integer param) {
        this.inMenu = param;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String param) {
        this.title = param;
    }

    public Integer getView() {
        return this.view;
    }

    public void setView(Integer param) {
        this.view = param;
    }
}
