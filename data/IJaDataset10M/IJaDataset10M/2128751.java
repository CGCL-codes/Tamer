package org.drftpd.commands.zipscript.mp3.list;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.drftpd.GlobalContext;
import org.drftpd.commands.list.ListElementsContainer;
import org.drftpd.commands.zipscript.list.NoEntryAvailableException;
import org.drftpd.commands.zipscript.list.ZipscriptListStatusBarInterface;
import org.drftpd.commands.zipscript.mp3.vfs.ZipscriptVFSDataMP3;
import org.drftpd.exceptions.NoAvailableSlaveException;
import org.drftpd.protocol.zipscript.mp3.common.ID3Tag;
import org.drftpd.protocol.zipscript.mp3.common.MP3Info;
import org.drftpd.vfs.DirectoryHandle;
import org.tanesha.replacer.ReplacerEnvironment;

/**
 * @author djb61
 * @version $Id: ZipscriptMP3StatusBar.java 2172 2010-10-13 20:34:10Z djb61 $
 */
public class ZipscriptMP3StatusBar implements ZipscriptListStatusBarInterface {

    public ArrayList<String> getStatusBarEntry(DirectoryHandle dir, ListElementsContainer container) throws NoEntryAvailableException {
        ResourceBundle bundle = container.getCommandManager().getResourceBundle();
        String keyPrefix = this.getClass().getName() + ".";
        boolean statusBarEnabled = GlobalContext.getGlobalContext().getPluginsConfig().getPropertiesForPlugin("zipscript.conf").getProperty("statusbar.enabled", "false").equalsIgnoreCase("true");
        if (statusBarEnabled) {
            try {
                ArrayList<String> statusBarEntries = new ArrayList<String>();
                ZipscriptVFSDataMP3 mp3Data = new ZipscriptVFSDataMP3(dir);
                MP3Info mp3Info = mp3Data.getMP3Info();
                ReplacerEnvironment env = new ReplacerEnvironment();
                ID3Tag id3 = mp3Info.getID3Tag();
                if (id3 != null) {
                    env.add("artist", id3.getArtist());
                    env.add("genre", id3.getGenre());
                    env.add("album", id3.getAlbum());
                    env.add("year", id3.getYear());
                } else {
                    throw new NoEntryAvailableException();
                }
                statusBarEntries.add(container.getSession().jprintf(bundle, keyPrefix + "statusbar.id3tag", env, container.getUser()));
                return statusBarEntries;
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } catch (NoAvailableSlaveException e) {
            }
        }
        throw new NoEntryAvailableException();
    }
}
