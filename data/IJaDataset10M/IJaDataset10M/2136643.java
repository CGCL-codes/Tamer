package com.magicpwd.d.dx;

import com.magicpwd.d.db.DBA4000;
import com.magicpwd.__i.IEditItem;
import com.magicpwd._comn.mpwd.MpwdHeader;
import com.magicpwd.__a.AEditItem;
import com.magicpwd._cons.ConsEnv;
import com.magicpwd._util.Bean;
import com.magicpwd.d.db.DBAccess;
import com.magicpwd.m.SafeMdl;
import com.magicpwd.m.UserMdl;

/**
 *
 * @author Amon
 */
public class DXA2000 extends DXA {

    public DXA2000() {
    }

    @Override
    public int importByKind(UserMdl userMdl, SafeMdl safeMdl, java.io.File file, String kindHash) throws Exception {
        DBAccess dba = new DBAccess();
        int cnt = 0;
        java.io.BufferedReader reader = null;
        try {
            dba.init(userMdl);
            reader = new java.io.BufferedReader(new java.io.FileReader(file));
            java.util.ArrayList<IEditItem> itemList = new java.util.ArrayList<IEditItem>();
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\d+");
            MpwdHeader tempKeys = new MpwdHeader();
            String tmp1;
            String tmp2;
            IEditItem item;
            String[] list;
            java.util.regex.Matcher matcher;
            String line = reader.readLine();
            while (line != null) {
                list = line.replace("\\;", "\b").split(";");
                if (list == null || list.length < ConsEnv.PWDS_HEAD_SIZE) {
                    continue;
                }
                for (String tmp : list) {
                    tmp1 = tmp.replace("\b", "\\;").trim();
                    matcher = pattern.matcher(tmp1);
                    if (!matcher.find()) {
                        continue;
                    }
                    tmp2 = matcher.group();
                    item = AEditItem.getInstance(userMdl, Integer.parseInt(tmp2, 10));
                    if (item != null) {
                        item.importByTxt(tmp1.substring(tmp2.length() + 1));
                        itemList.add(item);
                    }
                }
                tempKeys.setP30F0105(userMdl.getCode());
                itemList.get(0).setData(kindHash);
                safeMdl.enCrypt(tempKeys, itemList);
                DBA4000.saveMpwdData(dba, tempKeys);
                cnt += 1;
                tempKeys.setDefault();
                itemList.clear();
                line = reader.readLine();
                Thread.sleep(1);
            }
        } finally {
            Bean.closeReader(reader);
            dba.dispose();
        }
        return cnt;
    }

    @Override
    public int importByKeys(java.util.ArrayList<java.util.ArrayList<String>> data, String kindHash) throws Exception {
        return 0;
    }

    @Override
    public int exportByKind(UserMdl userMdl, SafeMdl safeMdl, java.io.File file, String kindHash) throws Exception {
        int cnt = 0;
        java.util.ArrayList<MpwdHeader> dataList = new java.util.ArrayList<MpwdHeader>();
        DBA4000.listRecHeaderByCat(userMdl, kindHash, dataList);
        if (dataList == null || dataList.size() < 1) {
            return cnt;
        }
        java.io.BufferedWriter writer = null;
        java.util.ArrayList<IEditItem> tempList = new java.util.ArrayList<IEditItem>();
        try {
            writer = new java.io.BufferedWriter(new java.io.FileWriter(file));
            StringBuilder builder = new StringBuilder();
            for (MpwdHeader keys : dataList) {
                keys.setP30F0105(userMdl.getCode());
                keys.getMpwd().setDefault();
                if (!DBA4000.readMpwdData(userMdl, keys)) {
                    continue;
                }
                tempList.clear();
                safeMdl.deCrypt(keys, tempList);
                for (IEditItem item : tempList) {
                    builder.append(item.getType()).append(',');
                    item.exportAsTxt(builder);
                    builder.append(';');
                }
                writer.write(builder.append('\n').toString());
                cnt += 1;
                builder.delete(0, builder.length());
            }
        } finally {
            Bean.closeWriter(writer);
        }
        return cnt;
    }

    @Override
    public int exportByKeys(java.util.ArrayList<java.util.ArrayList<String>> data, String kindHash) throws Exception {
        return 0;
    }
}
