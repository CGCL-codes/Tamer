package test.de.fhg.igd.semoa.webservice.hello;

public class HelloInfo {

    private int cnt;

    private String message;

    public HelloInfo() {
        message = "";
        cnt = 0;
    }

    public HelloInfo(String msg, int c) {
        message = msg;
        cnt = c;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public void setCnt(int c) {
        cnt = c;
    }

    public String getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public String toString() {
        return ("[HelloInfo] Message='" + message + "' | Cnt='" + cnt + "'");
    }
}
