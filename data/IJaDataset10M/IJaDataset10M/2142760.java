package com.belt.design.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.annotation.ejb.Local;
import java.util.Collection;

/**
 * @author      Tiziano
 */
public class Document implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @uml.property  name="id"
	 */
    private Long id;

    /**
	 * @uml.property  name="name"
	 */
    private String name;

    /**
	 * @uml.property  name="description"
	 */
    private String description;

    /**
	 * @uml.property  name="format"
	 */
    private String format;

    /**
	 * @uml.property  name="flag"
	 */
    private Boolean flag;

    /**
	 * @uml.property  name="classe"
	 */
    private String classe;

    /**
	 * @uml.property  name="category"
	 */
    private String category;

    /**
	 * @uml.property  name="docName"
	 */
    private String DocName;

    /**
	 * @uml.property  name="docFolder"
	 */
    private String DocFolder;

    /**
	 * @uml.property  name="docExt"
	 */
    private String DocExt;

    /**
	 * @uml.property  name="docType"
	 */
    private String DocType;

    /**
	 * @uml.property  name="docVersion"
	 */
    private String DocVersion;

    /**
	 * @uml.property  name="url"
	 */
    private String Url;

    /**
	 * @uml.property  name="output"
	 */
    private String output;

    /**
	 * @uml.property  name="model_id"
	 */
    private Long model_id;

    /**
	 * @uml.property  name="models"
	 */
    private List<Model> models = new ArrayList<Model>(0);

    /**
	 * @uml.property  name="model"
	 */
    private Collection<Model> model;

    /**
	 * @uml.property  name="contentType"
	 */
    private String contentType;

    /**
	 * @uml.property  name="data"
	 */
    private byte[] data;

    public Document() {
    }

    /**
	 * @return    the id
	 * @uml.property  name="id"
	 */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    /**
	 * @param id    the id to set
	 * @uml.property  name="id"
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return    the name
	 * @uml.property  name="name"
	 */
    @NotNull
    @Length(max = 45)
    public String getName() {
        return name;
    }

    /**
	 * @param name    the name to set
	 * @uml.property  name="name"
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return    the description
	 * @uml.property  name="description"
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description    the description to set
	 * @uml.property  name="description"
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return    the format
	 * @uml.property  name="format"
	 */
    public String getFormat() {
        return format;
    }

    /**
	 * @param format    the format to set
	 * @uml.property  name="format"
	 */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
	 * @return    the flag
	 * @uml.property  name="flag"
	 */
    public Boolean getFlag() {
        return flag;
    }

    /**
	 * @param flag    the flag to set
	 * @uml.property  name="flag"
	 */
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    /**
	 * @return    the docExt
	 * @uml.property  name="docExt"
	 */
    public String getDocExt() {
        return DocExt;
    }

    /**
	 * @return    the docFolder
	 * @uml.property  name="docFolder"
	 */
    public String getDocFolder() {
        return DocFolder;
    }

    /**
	 * @return    the docName
	 * @uml.property  name="docName"
	 */
    public String getDocName() {
        return DocName;
    }

    /**
	 * @return    the docType
	 * @uml.property  name="docType"
	 */
    public String getDocType() {
        return DocType;
    }

    /**
	 * @param docExt    the docExt to set
	 * @uml.property  name="docExt"
	 */
    public void setDocExt(String docExt) {
        DocExt = docExt;
    }

    /**
	 * @param docFolder    the docFolder to set
	 * @uml.property  name="docFolder"
	 */
    public void setDocFolder(String docFolder) {
        DocFolder = docFolder;
    }

    /**
	 * @param docName    the docName to set
	 * @uml.property  name="docName"
	 */
    public void setDocName(String docName) {
        DocName = docName;
    }

    /**
	 * @param docType    the docType to set
	 * @uml.property  name="docType"
	 */
    public void setDocType(String docType) {
        DocType = docType;
    }

    /**
	 * @return    the docVersion
	 * @uml.property  name="docVersion"
	 */
    public String getDocVersion() {
        return DocVersion;
    }

    /**
	 * @param docVersion    the docVersion to set
	 * @uml.property  name="docVersion"
	 */
    public void setDocVersion(String docVersion) {
        DocVersion = docVersion;
    }

    /**
	 * @return
	 * @uml.property  name="output"
	 */
    public String getOutput() {
        return output;
    }

    /**
	 * @param  output
	 * @uml.property  name="output"
	 */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
	 * @return    the url
	 * @uml.property  name="url"
	 */
    public String getUrl() {
        return Url;
    }

    /**
	 * @param url    the url to set
	 * @uml.property  name="url"
	 */
    public void setUrl(String url) {
        Url = url;
    }

    /**
	 * @return    the classe
	 * @uml.property  name="classe"
	 */
    public String getClasse() {
        return classe;
    }

    /**
	 * @param classe    the classe to set
	 * @uml.property  name="classe"
	 */
    public void setClasse(String classe) {
        this.classe = classe;
    }

    /**
	 * @return
	 * @uml.property  name="category"
	 */
    public String getCategory() {
        return category;
    }

    /**
	 * @param  category
	 * @uml.property  name="category"
	 */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
	 * @return    the models
	 * @uml.property  name="models"
	 */
    public List<Model> getModels() {
        return models;
    }

    /**
	 * @param models    the models to set
	 * @uml.property  name="models"
	 */
    public void setModels(List<Model> models) {
        this.models = models;
    }

    /**
	 * @return    the model_id
	 * @uml.property  name="model_id"
	 */
    public Long getModel_id() {
        return model_id;
    }

    /**
	 * @param model_id    the model_id to set
	 * @uml.property  name="model_id"
	 */
    public void setModel_id(Long model_id) {
        this.model_id = model_id;
    }

    /**
	 * @return
	 * @uml.property  name="contentType"
	 */
    public String getContentType() {
        return contentType;
    }

    /**
	 * @param contentType
	 * @uml.property  name="contentType"
	 */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
	 * @return
	 * @uml.property  name="data"
	 */
    @Local
    public byte[] getData() {
        return data;
    }

    /**
	 * @param data
	 * @uml.property  name="data"
	 */
    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString() {
        return "Document(" + id + "," + name + "," + format + ")";
    }

    /**
	 * Getter of the property <tt>model</tt>
	 * @return    Returns the model.
	 * @uml.property  name="model"
	 */
    public Collection<Model> getModel() {
        return model;
    }

    /**
	 * Setter of the property <tt>model</tt>
	 * @param model    The model to set.
	 * @uml.property  name="model"
	 */
    public void setModel(Collection<Model> model) {
        this.model = model;
    }
}
