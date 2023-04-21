package com.bwepow.wwj;

import com.bwepow.wgapp.wwj.gui.PositionBean;
import com.bwepow.wgapp.wwj.gui.StaticData;
import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.util.List;

/**
 *
 * @author Giampaolo Melis
 */
public class SaveBookmarks extends Thread {

    public SaveBookmarks() {
        super("Save-Bookmarks");
    }

    @Override
    public void run() {
        try {
            List<PositionBean> bookms = StaticData.getBOOKMARKS();
            FileOutputStream out = new FileOutputStream("./bookmarks.xml");
            XMLEncoder enc = new XMLEncoder(out);
            enc.writeObject(bookms);
            enc.flush();
            enc.close();
        } catch (Exception ex) {
        }
    }
}
