package org.eweb4j.config.bean;

import java.util.ArrayList;
import java.util.List;

public class Properties {

    private List<Prop> file = new ArrayList<Prop>();

    public List<Prop> getFile() {
        return file;
    }

    public void setFile(List<Prop> file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Properties [file=" + file + "]";
    }
}
