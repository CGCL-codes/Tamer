package com.nhncorp.cubridqa.replication.compare;

import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @ClassName: CubridTableDataRow
 * @Description: used for save all data of table
 * 
 * 
 * @date 2009-9-1
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class CubridTableDataRow {

    private int rownum;

    private Map<String, Object> datas = new HashMap<String, Object>();

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }

    /**
	 * 
	 */
    public boolean equals(Object obj) {
        if (obj instanceof CubridTableDataRow) {
            CubridTableDataRow row = (CubridTableDataRow) obj;
            if (this.getRownum() != row.getRownum()) return false;
            Map<String, Object> map = this.getDatas();
            Map<String, Object> map2 = row.getDatas();
            if (map.size() != map2.size()) return false;
            Iterator it = map.entrySet().iterator();
            boolean flag = true;
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                Object obj2 = map2.get(key);
                if (obj2 == null) {
                    if (entry.getValue() == null) continue; else {
                        System.out.println("******1***********" + key + " " + obj2 + " " + entry.getValue());
                        flag = false;
                        break;
                    }
                }
                if (obj2 instanceof Object[]) {
                    flag = Arrays.equals((Object[]) obj2, (Object[]) entry.getValue());
                    if (!flag) {
                        System.out.println("*******2**********" + key + " " + obj2 + " " + entry.getValue());
                        Object[] objarr1 = (Object[]) entry.getValue();
                        Object[] objarr2 = (Object[]) obj2;
                        System.out.println(objarr1.length + " ------ " + objarr2.length);
                        for (int i = 0; i < objarr1.length; i++) {
                            System.out.print(objarr1[i]);
                        }
                        System.out.println("");
                        for (int i = 0; i < objarr2.length; i++) {
                            System.out.print(objarr2[i]);
                        }
                        break;
                    }
                } else if (obj2 instanceof byte[]) {
                    flag = Arrays.equals((byte[]) obj2, (byte[]) entry.getValue());
                    if (!flag) {
                        System.out.println("*******4**********" + key + " " + obj2 + " " + entry.getValue());
                        byte[] objarr1 = (byte[]) entry.getValue();
                        byte[] objarr2 = (byte[]) obj2;
                        System.out.println(objarr1.length + "  " + objarr2.length);
                        for (int i = 0; i < objarr1.length; i++) {
                            System.out.print(objarr1[i]);
                        }
                        System.out.println("");
                        for (int i = 0; i < objarr2.length; i++) {
                            System.out.print(objarr2[i]);
                        }
                        break;
                    }
                } else if (obj2 instanceof Time) {
                    Time time1 = (Time) obj2;
                    flag = time1.toString().equals(entry.getValue().toString());
                    if (!flag) {
                        System.out.println("********5*********" + key + " " + obj2 + " " + entry.getValue());
                        break;
                    }
                } else {
                    flag = obj2.equals(entry.getValue());
                    if (!flag) {
                        System.out.println("********3*********" + key + " " + obj2 + " " + entry.getValue());
                        break;
                    }
                }
            }
            return flag;
        } else return false;
    }
}
