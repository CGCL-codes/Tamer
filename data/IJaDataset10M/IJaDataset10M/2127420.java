package com.iclotho.foundation.pub.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CDStore extends GenericVO {

    private List CDS = new ArrayList();

    public void addCD(CD cd) {
        this.CDS.add(cd);
    }

    public static void main(String[] args) {
        CDStore cds = new CDStore();
        CD cd1 = new CD();
        CD cd2 = new CD();
        CD cd3 = new CD();
        Address addr = new Address();
        addr.setDistrict("YangPu");
        addr.setStreet("SongHu");
        addr.setUnit("178");
        cd1.setName("cd1");
        cd1.setBonusCd(cd2);
        cd1.setAddress(addr);
        cd2.setName("cd2");
        cd2.setId("2");
        cd3.setName("");
        cds.addCD(cd1);
        cds.addCD(cd2);
        cds.addCD(cd3);
        String result = VO2XML.parse(cds);
        System.out.println(result);
    }
}
