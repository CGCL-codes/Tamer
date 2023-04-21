package it.aco.mandragora.vo;

import java.util.Collection;

public class Node1VO extends ValueObject {

    private String idNode1;

    private String idNode11;

    private String idRoot;

    private String description;

    private String note1;

    private Node11VO node11VO;

    private Collection<Node12VO> node12VOs;

    public String getIdNode1() {
        return idNode1;
    }

    public void setIdNode1(String idNode1) {
        this.idNode1 = idNode1;
    }

    public String getIdNode11() {
        return idNode11;
    }

    public void setIdNode11(String idNode11) {
        this.idNode11 = idNode11;
    }

    public String getIdRoot() {
        return idRoot;
    }

    public void setIdRoot(String idRoot) {
        this.idRoot = idRoot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public Node11VO getNode11VO() {
        return node11VO;
    }

    public void setNode11VO(Node11VO node11VO) {
        this.node11VO = node11VO;
    }

    public Collection<Node12VO> getNode12VOs() {
        return node12VOs;
    }

    public void setNode12VOs(Collection<Node12VO> node12VOs) {
        this.node12VOs = node12VOs;
    }

    public String toString() {
        String result = "Node1VO [";
        result += "idNode1: " + ((idNode1 == null) ? "" : idNode1.toString());
        result += ",\n idNode11: " + ((idNode11 == null) ? "" : idNode11.toString());
        result += ",\n idRoot: " + ((idRoot == null) ? "" : idRoot.toString());
        result += ",\n description: " + ((description == null) ? "" : description.toString());
        result += "]";
        return result;
    }
}
