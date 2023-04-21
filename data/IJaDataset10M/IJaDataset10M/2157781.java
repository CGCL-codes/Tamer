package com.wendal.java.dex.decomplier.javafile.model.statement;

import com.wendal.java.dex.decomplier.javafile.model.PrototypeStatement;
import com.wendal.java.dex.decomplier.javafile.model.Vxxx;

public class PrototypeStatement_iget extends PrototypeStatement {

    @Vxxx(type = Vxxx.Type.PUT)
    public String vx_name;

    @Vxxx
    public String vy_name;

    public String field_name;

    public String class_name;

    public String vx_type;

    @Override
    public void parse() {
        super.parse();
        vx_name = info.substring(info.indexOf(" ") + 1, info.indexOf(",")).trim();
        vy_name = info.substring(info.indexOf(", ") + 2, info.indexOf(", L"));
        field_name = info.substring(info.indexOf(".") + 1, info.indexOf(":"));
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + vx_name + " = " + vy_name + "." + field_name;
    }
}
