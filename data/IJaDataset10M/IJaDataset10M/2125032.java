package org.jcompany.control.jsf.action.entity;

import org.jcompany.commons.PlcBaseEntity;

/**
 * Value Object espec�fico para a gera��o de scheme do banco. 
 */
public class PlcScheme extends PlcBaseEntity {

    private static final long serialVersionUID = 1L;

    private String type = "U";

    private String generateFile = "N";

    private String fileGenerate;

    private String delimiter = ";";

    private boolean objTable = true;

    private boolean objConstraint = true;

    private boolean objSequence = true;

    private boolean objIndex = true;

    private String scheme;

    private String owner;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String esquema) {
        this.scheme = esquema;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getGenerateFile() {
        return generateFile;
    }

    public void setGenerateFile(String generateFile) {
        this.generateFile = generateFile;
    }

    public boolean getObjConstraint() {
        return objConstraint;
    }

    public void setObjConstraint(boolean objConstraint) {
        this.objConstraint = objConstraint;
    }

    public boolean getObjIndex() {
        return objIndex;
    }

    public void setObjIndex(boolean objIndice) {
        this.objIndex = objIndice;
    }

    public boolean getObjSequence() {
        return objSequence;
    }

    public void setObjSequence(boolean objSequence) {
        this.objSequence = objSequence;
    }

    public boolean getObjTable() {
        return objTable;
    }

    public void setObjTable(boolean objTabela) {
        this.objTable = objTabela;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileGenerated() {
        return fileGenerate;
    }

    public void setFileGenerated(String fileGenerated) {
        this.fileGenerate = fileGenerated;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
