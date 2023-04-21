package com.endigi.ceedws.page.domain;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class FreemarkerTemplate {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private FreemarkerTemplateType type;

    private String path;

    @Column(length = 65535)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FreemarkerTemplateType getType() {
        return type;
    }

    public void setType(FreemarkerTemplateType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void writeContentToFile(HttpServletRequest request) throws IOException {
        String path = request.getSession().getServletContext().getRealPath(File.separator) + "WEB-INF" + File.separator + "freemarker" + File.separator + "templates" + File.separator + type.getFlag();
        File directory = new File(path);
        FileUtils.forceMkdir(directory);
        FileUtils.writeStringToFile(new File(path + File.separator + id + ".ftl"), content);
    }

    public void delelteTempalteFile(HttpServletRequest request) throws IOException {
        String path = request.getSession().getServletContext().getRealPath(File.separator) + "WEB-INF" + File.separator + "freemarker" + File.separator + "templates" + File.separator + this.path;
        FileUtils.forceDelete(new File(path));
    }

    public void processTemplatePath() {
        System.out.println(type.getFlag() + File.separator + id + ".ftl");
        this.setPath(type.getFlag() + File.separator + id + ".ftl");
    }
}
