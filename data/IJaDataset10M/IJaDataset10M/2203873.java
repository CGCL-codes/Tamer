package org.elite.jdcbot.examples;

import java.io.*;
import org.elite.jdcbot.framework.*;
import org.elite.jdcbot.shareframework.FileListManager;
import org.elite.jdcbot.shareframework.HashException;
import org.elite.jdcbot.shareframework.ShareManager;
import org.elite.jdcbot.shareframework.ShareManagerListener;

/**
 * Created on 3-Aug-10<br>
 * A simple POJO to capture bot
 * settings. Passing settings as arguments
 * to constructor requires that we look at the javadoc
 * of the class to determine the position of an
 * argument. That is very cumbersome and error
 * prone.
 * 
 * @since 1.1.3
 * @version 1.0
 */
public class QuickAndDirtyBot {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            BotConfig config = new BotConfig();
            config.setBotname("nam");
            config.setBotIP("127.0.0.1");
            final MultiHubsAdapter mha = new MultiHubsAdapter(config);
            mha.setDirs("C:\\tmp", "C:\\tmp");
            final ShareManager shareManager = new ShareManager(mha);
            DownloadCentral dc = new DownloadCentral(mha);
            shareManager.addListener(new ShareManagerListener() {

                @Override
                public void onMiscMsg(String msg) {
                }

                @Override
                public void onFilelistDownloadFinished(User u, boolean success, Exception e) {
                    FileListManager flm = shareManager.getOthersFileListManager(u);
                    System.out.println(flm.getFilelist().printTree());
                    mha.terminate();
                }

                @Override
                public void hashingOfFileStarting(String file) {
                }

                @Override
                public void hashingOfFileSkipped(String f, String reason) {
                }

                @Override
                public void hashingOfFileComplete(String f, boolean success, HashException e) {
                }

                @Override
                public void hashingJobFinished() {
                }
            });
            mha.setShareManager(shareManager);
            mha.setDownloadCentral(dc);
            jDCBot bot = new jDCBot(mha) {
            };
            mha.connect("localhost", 411, bot);
            Thread.sleep(3000);
            User user = bot.getUser("Bangla");
            System.out.println("Downloading File list...");
            mha.getShareManager().downloadOthersFileList(user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BotException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
