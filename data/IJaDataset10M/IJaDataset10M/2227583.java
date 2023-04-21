package omschaub.azcvsupdater.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import omschaub.azcvsupdater.main.StatusBoxUtils;
import omschaub.azcvsupdater.main.View;

public class SFMirrorParse {

    public static int MIRROR_NUM;

    public static String getMirror() {
        String[] mirrorURL = new String[12];
        mirrorURL[0] = "unc";
        int counter = 1;
        String userPath = View.getPluginInterface().getPluginDirectoryName();
        File configFile = new File(userPath, "plugin.cache");
        if (!configFile.isFile()) {
            StatusBoxUtils.mainStatusAdd(" Error retrieving SourceForge mirror list.. reverting to default -- use manual update to solve this problem", 2);
            return mirrorURL[0];
        }
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(configFile));
            String line = null;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("<TR BGCOLOR=#DDDDDD><TD align=center><A HREF=")) {
                    boolean status = true;
                    String old_line = line;
                    while (status) {
                        try {
                            old_line = old_line.substring(old_line.indexOf("use_mirror=") + 11, old_line.length());
                            mirrorURL[counter] = old_line.substring(0, old_line.indexOf(">"));
                            counter++;
                        } catch (Exception e) {
                            status = false;
                        }
                    }
                }
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int server_num = MIRROR_NUM;
        MIRROR_NUM++;
        if (MIRROR_NUM > counter) MIRROR_NUM = 0;
        return mirrorURL[server_num];
    }
}
