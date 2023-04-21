package com.openbravo.data.loader.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class QBFParameters implements Serializable {

    public static int LINKTYPE_And = 0;

    public static int LINKTYPE_Or = 1;

    private List<QBFParameter> andList = new ArrayList<QBFParameter>();

    private List<QBFParameter> orList = new ArrayList<QBFParameter>();

    private List<QBFParameters> andSubList = new ArrayList<QBFParameters>();

    private List<QBFParameters> orSubList = new ArrayList<QBFParameters>();

    public QBFParameters and(QBFParameter para) {
        andList.add(para);
        return this;
    }

    public QBFParameters or(QBFParameter para) {
        orList.add(para);
        return this;
    }

    public QBFParameters and(QBFParameters para) {
        andSubList.add(para);
        return this;
    }

    public QBFParameters or(QBFParameters para) {
        orSubList.add(para);
        return this;
    }

    public List<QBFParameter> getAndList() {
        return andList;
    }

    public List<QBFParameter> getOrList() {
        return orList;
    }

    public List<QBFParameters> getAndSubList() {
        return andSubList;
    }

    public List<QBFParameters> getOrSubList() {
        return orSubList;
    }
}
