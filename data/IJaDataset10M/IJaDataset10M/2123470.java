package net.sf.gwoc.runner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.sf.gwoc.data.Cache;
import net.sf.gwoc.data.LuceneHelper;
import net.sf.gwoc.data.Singleton;
import net.sf.gwoc.gwapi.GWSoap;
import net.sf.gwoc.gwapi.GWSoapException;
import net.sf.gwoc.gwapi.GWSoapHelper;
import org.apache.lucene.index.IndexWriter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import com.novell.groupwise.ws.Folder;

public class CacheFolders implements IRunnableWithProgress {

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.setTaskName("Caching folders");
        int i = 0;
        try {
            GWSoap soap = GWSoapHelper.getConnection();
            if (soap.isOffline()) return;
            List<Folder> folderList = soap.getFolderList();
            monitor.beginTask("Caching folders", folderList.size());
            monitor.subTask("working on step " + 1);
            monitor.worked(i++);
            IndexWriter iwriter = Singleton.getIWriter();
            for (Folder f : folderList) {
                monitor.worked(i++);
                if (monitor.isCanceled()) return;
                if (!Cache.getConnection().folderExists(f.getId())) try {
                    iwriter.addDocument(LuceneHelper.buildFolderDoc(f));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GWSoapException e) {
            e.printStackTrace();
        } finally {
            Singleton.closeIWriter();
            Singleton.closeISearcherIfOld();
            monitor.done();
        }
    }
}
