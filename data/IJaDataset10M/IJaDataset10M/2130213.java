package com.hironico.jsftp.ssh.remote;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import java.util.Comparator;

/**
 * Comparateur d'entrée de listing de répertoire SFTP.
 * @author niramousse
 * @since 2.2.0
 */
public class LsEntryComparator implements Comparator<LsEntry> {

    /**
     * Compare les noms des entrées de listing de répertoire SFTP.
     * Les répertoires sont prioritaires sur le reste des fichiers.
     * @param o1 entrée1
     * @param o2 entrée2
     * @return -1, 0, 1 si o1 est plus petit, égal ou plus grand que o2
     * @since 2.2.0
     */
    @Override
    public int compare(LsEntry o1, LsEntry o2) {
        if (o1.getAttrs().isDir() && !o2.getAttrs().isDir()) return -1; else if (!o1.getAttrs().isDir() && o2.getAttrs().isDir()) return 1; else return o1.getLongname().compareTo(o2.getLongname());
    }
}
