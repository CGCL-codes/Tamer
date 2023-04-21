package project.cn.dataType;

import java.util.ArrayList;

public class DContactList extends Data {

    private static final long serialVersionUID = -4341469181719551092L;

    private ArrayList<DContactPerson> list;

    public DContactList() {
        list = new ArrayList<DContactPerson>();
    }

    public void setList(ArrayList<DContactPerson> list) {
        this.list = list;
    }

    public ArrayList<DContactPerson> getList() {
        return list;
    }
}
