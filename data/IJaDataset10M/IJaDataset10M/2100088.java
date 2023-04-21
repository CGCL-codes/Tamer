package omschaub.stuffer.main;

import java.io.File;

public class PluginGet extends DownloadImp {

    public void percentageCommands(final int percentage) {
    }

    public void postCommands() {
        if (totalk < (1024 * 100)) {
            System.out.println("Stuffer Auto Upgrade: Server reporting file not found on server.. trying different server");
            File file_to_die = new File(dir + fileName);
            file_to_die.delete();
            if (SFMirrorParse.MIRROR_NUM == 11) {
                System.out.println("Stuffer Auto Upgrade: All download attempts failed.  Please wait and try again or email me at omschaub@users.sourceforge.net");
                return;
            }
            AutoUpdate.downloadPlugin();
            return;
        }
        SFMirrorParse.MIRROR_NUM = 0;
        System.out.println("Stuffer Auto Upgrade: Download of new plugin successful");
        AutoUpdate.insertLatest();
    }

    public void failedCommands() {
        System.out.println("Stuffer Auto Upgrade:  Failed to download " + fileName);
    }
}
