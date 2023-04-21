package org.brainypdm.modules.commons.classdef;

import java.io.Serializable;

public class PoolConfiguration implements Serializable {

    private static final long serialVersionUID = 1288964247852349382L;

    private String className;

    private Integer maxActive;

    private Integer maxIdle;

    private Integer maxWait;

    private Integer minEvictableIdleTimeMillis;

    private Integer minIdle;

    private Integer numTestsPerEvictionRun;

    private String[] parametersTypes;

    private String[] parametersValues;

    private Long softMinEvictableIdleTimeMillis;

    private Boolean testOnBorrow;

    private Boolean testOnReturn;

    private Boolean testWhileIdle;

    private Long timeBetweenEvictionRunsMillis;

    private String whenExhaustedAction;

    public String getWhenExhaustedAction() {
        return whenExhaustedAction;
    }

    public void setWhenExhaustedAction(String whenExhaustedAction) {
        this.whenExhaustedAction = whenExhaustedAction;
    }

    public String parametersTypesAsString() {
        String out = "";
        if (parametersTypes != null) {
            for (Integer i = 0; i < parametersTypes.length; i++) {
                out = out.concat(parametersTypes[i]).concat(";");
            }
        }
        return out;
    }

    public String getClassName() {
        return className;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public Integer getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public Integer getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public String[] getParametersTypes() {
        return parametersTypes;
    }

    public String[] getParametersValues() {
        return parametersValues;
    }

    public Long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public Boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public Boolean isTestOnReturn() {
        return testOnReturn;
    }

    public Boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public void setParametersTypes(String[] parametersTypes) {
        this.parametersTypes = parametersTypes;
    }

    public void setParametersValues(String[] parametersValues) {
        this.parametersValues = parametersValues;
    }

    public void setSoftMinEvictableIdleTimeMillis(Long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public void setTestWhileIdle(Boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
}
