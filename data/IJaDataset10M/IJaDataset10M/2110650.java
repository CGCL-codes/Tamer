package cn.edu.zucc.leyi.obj;

import java.util.ArrayList;
import java.util.List;

public class NetWord extends Word {

    private List<String> sent = new ArrayList<String>();

    public NetWord() {
        super();
    }

    public List<String> getSent() {
        return sent;
    }

    public void setSent(List<String> sent) {
        this.sent = sent;
    }

    public void addOneSent(String msent) {
        sent.add(msent);
    }
}
