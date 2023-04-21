package jp.eflow.hisano.dalvikvm.jvmtests;

public class Implementation implements Interface {

    public String print(String message) {
        System.out.println(message);
        return "result";
    }
}
