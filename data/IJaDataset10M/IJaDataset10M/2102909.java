package org.epoline.phoenix.manager.shared;

import java.io.Serializable;
import org.epoline.phoenix.common.shared.Item;
import org.epoline.phoenix.common.shared.PhoenixUserException;

public class ItemMVSConfig extends Item implements Serializable, Cloneable {

    private String transactionName = "";

    private String parameterName = "";

    private String parameterValue = "";

    private static final int MAXLENGTH_PARAMETER_VALUE = 80;

    private static final int MAXLENGTH_TRANSACTION_NAME = 4;

    private static final int MAXLENGTH_PARAMETER_NAME = 8;

    public ItemMVSConfig() {
        super();
    }

    public ItemMVSConfig(String newTransactionName, String newParameterName, String newParameterValue) {
        super();
        if (!isValidTransactionName(newTransactionName) || !isValidParameterName(newParameterName) || !isValidParameterValue(newParameterValue)) {
            throw new IllegalArgumentException("Illegal argument :" + newTransactionName + "," + newParameterName + "," + newParameterValue);
        }
        transactionName = newTransactionName;
        parameterName = newParameterName;
        parameterValue = newParameterValue;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ItemMVSConfig item = (ItemMVSConfig) obj;
        if (item == null || item.getClass() != this.getClass()) {
            return false;
        }
        ItemMVSConfig MVSC = (ItemMVSConfig) item;
        return MVSC.getTransactionName().equals(getTransactionName()) && MVSC.getParameterName().equals(getParameterName()) && MVSC.getParameterValue().equals(getParameterValue());
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public int hashCode() {
        return getTransactionName().hashCode() + getParameterName().hashCode();
    }

    public boolean isValid() {
        return isValidTransactionName(getTransactionName()) && isValidParameterName(getParameterName()) && isValidParameterValue(getParameterValue());
    }

    public boolean isValidParameterName(String checkedParam) {
        if (checkedParam == null) {
            throw new NullPointerException("parameterName");
        }
        String tempPar = checkedParam.trim();
        return (tempPar.length() > 0) && (tempPar.length() <= MAXLENGTH_PARAMETER_NAME);
    }

    public boolean isValidParameterValue(String checkedParam) throws NullPointerException {
        if (checkedParam == null) {
            throw new NullPointerException("parameterValue");
        }
        return checkedParam.trim().length() <= MAXLENGTH_PARAMETER_VALUE;
    }

    public boolean isValidTransactionName(String checkedParam) {
        if (checkedParam == null) {
            throw new NullPointerException("transactionName");
        }
        String tempPar = checkedParam.trim();
        return (tempPar.length() > 0) && (tempPar.length() <= MAXLENGTH_TRANSACTION_NAME);
    }

    public void setParameterName(String newParameterName) throws PhoenixUserException {
        if (!isValidParameterName(newParameterName)) {
            throw new PhoenixUserException(newParameterName);
        }
        String oldParameterName = parameterName;
        parameterName = newParameterName;
        firePropertyChange("parameterName", oldParameterName, newParameterName);
    }

    public void setParameterValue(String newParameterValue) throws PhoenixUserException {
        if (!isValidParameterValue(newParameterValue)) {
            throw new PhoenixUserException(newParameterValue);
        }
        String oldParameterValue = parameterValue;
        parameterValue = newParameterValue;
        firePropertyChange("parameterValue", oldParameterValue, newParameterValue);
    }

    public void setTransactionName(String newTransactionName) throws PhoenixUserException {
        if (!isValidTransactionName(newTransactionName)) {
            throw new PhoenixUserException(newTransactionName);
        }
        String oldTransactionName = transactionName;
        transactionName = newTransactionName;
        firePropertyChange("transactionName", oldTransactionName, newTransactionName);
    }

    public String toString() {
        return "Transaction:" + getTransactionName() + "; Parameter:" + getParameterName();
    }
}
