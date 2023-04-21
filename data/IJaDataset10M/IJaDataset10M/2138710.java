package gndzh.som.summer.schema;

import java.math.BigDecimal;

/**
 * SmFuncregister entity. @author MyEclipse Persistence Tools
 */
public class SmFuncregister implements java.io.Serializable {

    private String pkFunc;

    private String dispCode;

    private String funcCode;

    private String funcDesc;

    private String forbidFlag;

    private BigDecimal funcLevel;

    private String funcName;

    private String funcJsp;

    private String pkParent;

    private String funcProperty;

    private String groupFlag;

    private String subsystemId;

    private String ts;

    /** default constructor */
    public SmFuncregister() {
    }

    /** minimal constructor */
    public SmFuncregister(String pkFunc) {
        this.pkFunc = pkFunc;
    }

    /** full constructor */
    public SmFuncregister(String pkFunc, String dispCode, String funcCode, String funcDesc, String forbidFlag, BigDecimal funcLevel, String funcName, String funcJsp, String pkParent, String funcProperty, String groupFlag, String subsystemId, String ts) {
        this.pkFunc = pkFunc;
        this.dispCode = dispCode;
        this.funcCode = funcCode;
        this.funcDesc = funcDesc;
        this.forbidFlag = forbidFlag;
        this.funcLevel = funcLevel;
        this.funcName = funcName;
        this.funcJsp = funcJsp;
        this.pkParent = pkParent;
        this.funcProperty = funcProperty;
        this.groupFlag = groupFlag;
        this.subsystemId = subsystemId;
        this.ts = ts;
    }

    public String getPkFunc() {
        return this.pkFunc;
    }

    public void setPkFunc(String pkFunc) {
        this.pkFunc = pkFunc;
    }

    public String getDispCode() {
        return this.dispCode;
    }

    public void setDispCode(String dispCode) {
        this.dispCode = dispCode;
    }

    public String getFuncCode() {
        return this.funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getFuncDesc() {
        return this.funcDesc;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc;
    }

    public String getForbidFlag() {
        return this.forbidFlag;
    }

    public void setForbidFlag(String forbidFlag) {
        this.forbidFlag = forbidFlag;
    }

    public BigDecimal getFuncLevel() {
        return this.funcLevel;
    }

    public void setFuncLevel(BigDecimal funcLevel) {
        this.funcLevel = funcLevel;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncJsp() {
        return this.funcJsp;
    }

    public void setFuncJsp(String funcJsp) {
        this.funcJsp = funcJsp;
    }

    public String getPkParent() {
        return this.pkParent;
    }

    public void setPkParent(String pkParent) {
        this.pkParent = pkParent;
    }

    public String getFuncProperty() {
        return this.funcProperty;
    }

    public void setFuncProperty(String funcProperty) {
        this.funcProperty = funcProperty;
    }

    public String getGroupFlag() {
        return this.groupFlag;
    }

    public void setGroupFlag(String groupFlag) {
        this.groupFlag = groupFlag;
    }

    public String getSubsystemId() {
        return this.subsystemId;
    }

    public void setSubsystemId(String subsystemId) {
        this.subsystemId = subsystemId;
    }

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
