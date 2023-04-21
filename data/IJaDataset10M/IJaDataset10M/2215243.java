package org.susan.java.design.adapter;

import java.util.List;

public class N0Adapter implements OLogDbOperateApi {

    private OLogFileOperateApi adaptee;

    public N0Adapter(OLogFileOperateApi adaptee) {
        this.adaptee = adaptee;
    }

    public void createLog(OLogModel m) {
        List<OLogModel> list = adaptee.readLogFile();
        list.add(m);
        adaptee.writeLogFile(list);
    }

    public List<OLogModel> getAllLog() {
        return adaptee.readLogFile();
    }

    public void removeLog(OLogModel m) {
        List<OLogModel> list = adaptee.readLogFile();
        list.remove(m);
        adaptee.writeLogFile(list);
    }

    public void updateLog(OLogModel m) {
        List<OLogModel> list = adaptee.readLogFile();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLogId().equals(m.getLogId())) {
                list.set(i, m);
                break;
            }
        }
        adaptee.writeLogFile(list);
    }
}
