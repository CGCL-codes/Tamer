package com.leemba.monitor.server.servlet.api.admin.backup;

import com.leemba.monitor.server.dao.admin.Backup;
import com.leemba.monitor.server.objects.FileInfo;
import com.leemba.leemlet.representation.ObjectRepresentation;
import com.leemba.leemlet.LeemletException;
import com.leemba.leemlet.Representation;
import com.leemba.monitor.server.servlet.MonitorLeemlet;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mrjohnson
 */
public class BackupsResource extends MonitorLeemlet {

    @Override
    public Representation onGet(HttpServletRequest request, HttpServletResponse response) throws LeemletException {
        try {
            File[] ar = Backup.listBackups();
            FileInfo[] info = new FileInfo[ar.length];
            for (int pos = 0; pos < ar.length; pos++) info[pos] = new FileInfo(ar[pos]);
            Arrays.sort(info, new Comparator<FileInfo>() {

                public int compare(FileInfo o1, FileInfo o2) {
                    long r = o1.getModified().getTime() - o2.getModified().getTime();
                    if (r < 0) return 1;
                    if (r > 0) return -1;
                    return 0;
                }
            });
            return new ObjectRepresentation(info);
        } catch (Throwable t) {
            throw new LeemletException(t);
        }
    }
}
