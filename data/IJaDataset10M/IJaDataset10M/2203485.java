package com.narirelays.ems.persistence.orm;

import java.util.HashSet;
import java.util.Set;

/**
 * Process entity. @author MyEclipse Persistence Tools
 */
public class Process implements java.io.Serializable {

    private String id;

    private ProcessTemplate processTemplate;

    private EntHierarchy entHierarchy;

    private ForwardModel forwardModel;

    private BackwardModel backwardModel;

    private String name;

    private String description;

    private Set productMeasures = new HashSet(0);

    private Set productModels = new HashSet(0);

    private Set PInMeasures = new HashSet(0);

    private Set POutMeasures = new HashSet(0);

    /** default constructor */
    public Process() {
    }

    /** minimal constructor */
    public Process(String id) {
        this.id = id;
    }

    /** full constructor */
    public Process(String id, ProcessTemplate processTemplate, EntHierarchy entHierarchy, ForwardModel forwardModel, BackwardModel backwardModel, String name, String description, Set productMeasures, Set productModels, Set PInMeasures, Set POutMeasures) {
        this.id = id;
        this.processTemplate = processTemplate;
        this.entHierarchy = entHierarchy;
        this.forwardModel = forwardModel;
        this.backwardModel = backwardModel;
        this.name = name;
        this.description = description;
        this.productMeasures = productMeasures;
        this.productModels = productModels;
        this.PInMeasures = PInMeasures;
        this.POutMeasures = POutMeasures;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProcessTemplate getProcessTemplate() {
        return this.processTemplate;
    }

    public void setProcessTemplate(ProcessTemplate processTemplate) {
        this.processTemplate = processTemplate;
    }

    public EntHierarchy getEntHierarchy() {
        return this.entHierarchy;
    }

    public void setEntHierarchy(EntHierarchy entHierarchy) {
        this.entHierarchy = entHierarchy;
    }

    public ForwardModel getForwardModel() {
        return this.forwardModel;
    }

    public void setForwardModel(ForwardModel forwardModel) {
        this.forwardModel = forwardModel;
    }

    public BackwardModel getBackwardModel() {
        return this.backwardModel;
    }

    public void setBackwardModel(BackwardModel backwardModel) {
        this.backwardModel = backwardModel;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set getProductMeasures() {
        return this.productMeasures;
    }

    public void setProductMeasures(Set productMeasures) {
        this.productMeasures = productMeasures;
    }

    public Set getProductModels() {
        return this.productModels;
    }

    public void setProductModels(Set productModels) {
        this.productModels = productModels;
    }

    public Set getPInMeasures() {
        return this.PInMeasures;
    }

    public void setPInMeasures(Set PInMeasures) {
        this.PInMeasures = PInMeasures;
    }

    public Set getPOutMeasures() {
        return this.POutMeasures;
    }

    public void setPOutMeasures(Set POutMeasures) {
        this.POutMeasures = POutMeasures;
    }
}
