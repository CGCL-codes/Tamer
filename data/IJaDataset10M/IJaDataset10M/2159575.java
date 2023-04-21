package com.faithbj.shop.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 实体类 - 日志
 * <p>Copyright: Copyright (c) 2011</p> 
 * 
 * <p>Company: www.faithbj.com</p>
 * 
 * @author 	faithbj
 * @date 	2011-12-16
 * @version 1.0
 */
@Entity
public class Log extends BaseEntity {

    private static final long serialVersionUID = -4494144902110236826L;

    private String operationName;

    private String operator;

    private String actionClassName;

    private String actionMethodName;

    private String ip;

    private String info;

    @Column(nullable = false)
    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Column(nullable = false)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(nullable = false)
    public String getActionClassName() {
        return actionClassName;
    }

    public void setActionClassName(String actionClassName) {
        this.actionClassName = actionClassName;
    }

    @Column(nullable = false)
    public String getActionMethodName() {
        return actionMethodName;
    }

    public void setActionMethodName(String actionMethodName) {
        this.actionMethodName = actionMethodName;
    }

    @Column(nullable = false)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(length = 5000)
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
