package org.nlsde.ipv6.sengine.mining;

import java.util.Vector;
import org.nlsde.ipv6.sengine.database.ImageInsert;
import org.nlsde.ipv6.sengine.meta.Meta;
import org.nlsde.ipv6.sengine.parse.ImageDigger;
import org.nlsde.ipv6.sengine.util.FileUtil;

public class MiningImage {

    public static void mining(String filePath) {
        String[] files = FileUtil.getFiles(filePath);
        System.out.print("get " + files.length + " files...");
        int i = 0;
        for (String file : files) {
            try {
                String html = FileUtil.loadFileToString(file);
                ImageDigger id = new ImageDigger();
                Vector<Meta> vector = id.digger(html);
                ImageInsert.insert(vector);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            i++;
            System.out.println("finised the " + i + " th file successfully!!!");
        }
    }

    public static void main(String args[]) {
        String filePath = "D:\\filess\\";
        MiningImage.mining(filePath);
    }
}
