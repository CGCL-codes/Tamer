package cn.poco.bean;

import java.io.Serializable;

public class RegisterData implements Serializable {

    private String result;

    private String message;

    private String pocoId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPocoId() {
        return pocoId;
    }

    public void setPocoId(String pocoId) {
        this.pocoId = pocoId;
    }

    @Override
    public String toString() {
        return "RegisterData [result=" + result + ", message=" + message + ", pocoId=" + pocoId + "]";
    }
}
