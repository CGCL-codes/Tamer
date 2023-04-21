package edu.columbia.hypercontent.contentmanager.commands;

import edu.columbia.hypercontent.contentmanager.ICommand;
import edu.columbia.hypercontent.contentmanager.CMSessionData;
import edu.columbia.hypercontent.CMSException;
import edu.columbia.hypercontent.PublicationService;
import edu.columbia.hypercontent.L10n;
import edu.columbia.hypercontent.Project;
import edu.columbia.hypercontent.engine.CopyingRequestImpl;
import edu.columbia.filesystem.IFileSystemManager;
import edu.columbia.filesystem.FileSystemFactory;
import edu.columbia.filesystem.io.Util;
import edu.columbia.filesystem.impl.ZipFileSystem;
import org.jasig.portal.MultipartDataSource;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Oct 25, 2003
 * Time: 11:40:46 AM
 * To change this template use Options | File Templates.
 */
public class CopyZip implements ICommand {

    public void execute(CMSessionData session) throws Exception {
        String path = session.runtimeData.getParameter("path");
        if (path == null || path.trim().equals("")) {
            path = SEPARATOR;
        }
        if (session.global.hasPermission(READ_PERMISSION, path) && session.global.hasPermission(CREATE_PERMISSION, path) && session.global.hasPermission(WRITE_PERMISSION, path)) {
            session.runtimeData.setParameter(PublicationService.REQUEST, "true");
            MultipartDataSource part = (MultipartDataSource) session.runtimeData.getObjectParameter("fUploadZip");
            if (part != null) {
                java.io.File zipfile = java.io.File.createTempFile("HyC", ".zip");
                zipfile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(zipfile);
                InputStream is = part.getInputStream();
                try {
                    Util.copyStream(is, out);
                } finally {
                    try {
                        out.close();
                        is.close();
                    } catch (IOException e) {
                    }
                }
                IFileSystemManager source = new ZipFileSystem(zipfile);
                source.mount(new String[0]);
                Project project = session.global.getProject();
                CopyingRequestImpl request = new CopyingRequestImpl(project, source, session.repository, path, session.userLockKey, session);
                request.setIgnoreHidden(false);
                request.setRequiredPermissions(new String[] { READ_PERMISSION, WRITE_PERMISSION, CREATE_PERMISSION });
                String force = session.runtimeData.getParameter("force");
                if (force != null && force.trim().equals("true")) {
                    request.setForced(true);
                }
                String delete = session.runtimeData.getParameter("deleteUnknown");
                if (delete == null || !delete.trim().equals("true")) {
                    request.setDeleteUnknown(false);
                }
                session.publicationReport = PublicationService.processRequest(request);
                session.publicationReport.setTitle(L10n.getLocalizedString(session, "UPLOADING", new String[] { project.getTitle(), path }));
            } else {
                throw new CMSException(CMSException.PROCESSING_ERROR, new Exception(session.localization.getString("NO_DATA")));
            }
        } else {
            throw new CMSException(CMSException.NOT_PERMITTED);
        }
    }
}
