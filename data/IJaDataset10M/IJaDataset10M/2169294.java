package com.tiger.aowim.folder.model;

import java.util.Date;
import com.framework.base.BaseEntity;
import com.tiger.aowim.info.generalinfo.model.GInfoEntity;

public class FolderEntity extends BaseEntity {

    private Long c_id;

    private String c_name;

    private String c_old_path;

    private String c_p_name;

    private String c_path = "";

    private String c_id_path = "";

    private Long c_parent_id;

    private Date c_create_time;

    private String c_creator = "admin";

    private String c_version;

    private Long c_folder_content;

    private int c_children;

    private String hascontent;

    private GInfoEntity ge = new GInfoEntity();

    public Long getC_id() {
        return c_id;
    }

    public void setC_id(Long cId) {
        c_id = cId;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String cName) {
        c_name = cName;
    }

    public String getC_old_path() {
        return c_old_path;
    }

    public void setC_old_path(String cOldPath) {
        c_old_path = cOldPath;
    }

    public String getC_path() {
        return c_path;
    }

    public void setC_path(String cPath) {
        c_path = cPath;
    }

    public String getC_id_path() {
        return c_id_path;
    }

    public void setC_id_path(String cIdPath) {
        c_id_path = cIdPath;
    }

    public Long getC_parent_id() {
        return c_parent_id;
    }

    public void setC_parent_id(Long cParentId) {
        c_parent_id = cParentId;
    }

    public Date getC_create_time() {
        return c_create_time;
    }

    public void setC_create_time(Date cCreateTime) {
        c_create_time = cCreateTime;
    }

    public String getC_creator() {
        return c_creator;
    }

    public void setC_creator(String cCreator) {
        c_creator = cCreator;
    }

    public String getC_version() {
        return c_version;
    }

    public void setC_version(String cVersion) {
        c_version = cVersion;
    }

    public Long getC_folder_content() {
        return c_folder_content;
    }

    public void setC_folder_content(Long cFolderContent) {
        c_folder_content = cFolderContent;
    }

    public int getC_children() {
        return c_children;
    }

    public void setC_children(int cChildren) {
        c_children = cChildren;
    }

    public String getHascontent() {
        return hascontent;
    }

    public void setHascontent(String hascontent) {
        this.hascontent = hascontent;
    }

    public GInfoEntity getGe() {
        return ge;
    }

    public void setGe(GInfoEntity ge) {
        this.ge = ge;
    }

    public String getC_p_name() {
        return c_p_name;
    }

    public void setC_p_name(String cPName) {
        c_p_name = cPName;
    }
}
