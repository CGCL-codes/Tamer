package cn.webwheel.builder;

import java.util.ArrayList;
import java.util.List;

public class ActUpdateModel {

    public String pkgName;

    public Model model;

    private static List<Col> allcols(Model model) {
        List<Col> list = new ArrayList<Col>();
        for (Col col : model.cols) {
            if (col.pk) {
                if (col.fk == null) {
                    list.add(col);
                } else {
                    for (Col c : allcols(col.fk)) {
                        Col c2 = new Col();
                        c2.name = col.name + "_" + c.name;
                        c2.type = c.type;
                        list.add(c2);
                    }
                }
            }
        }
        return list;
    }

    public List<Col> allcols() {
        List<Col> list = new ArrayList<Col>();
        for (Col col : model.cols) {
            if (col.fk == null) {
                list.add(col);
            } else {
                for (Col c : allcols(col.fk)) {
                    Col c2 = new Col();
                    c2.name = col.name + "_" + c.name;
                    c2.type = c.type;
                    c2.pk = col.pk;
                    list.add(c2);
                }
            }
        }
        return list;
    }

    public boolean hasBlob() {
        for (Col col : model.cols) {
            if (col.blob()) return true;
        }
        return false;
    }
}
