package com.ivis.xprocess.framework.vcs.impl.svn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.internal.wc.admin.SVNAdminArea;
import org.tmatesoft.svn.core.internal.wc.admin.SVNLog;
import org.tmatesoft.svn.core.wc.ISVNMerger;
import org.tmatesoft.svn.core.wc.SVNDiffOptions;
import org.tmatesoft.svn.core.wc.SVNMergeFileSet;
import org.tmatesoft.svn.core.wc.SVNMergeResult;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import com.ivis.xprocess.framework.DataSource;
import com.ivis.xprocess.framework.vcs.IXprocessMerger;
import com.ivis.xprocess.framework.vcs.merge.Merger;
import com.ivis.xprocess.framework.xml.XMLifier;
import com.ivis.xprocess.util.DebugUtil;

/**
 * Define how to handle merging when SVN requires it.
 *
 * NB: mergeBinary is not implemented and returns null.
 *
 */
@SuppressWarnings("unused")
public class XprocessMerger implements ISVNMerger {

    private static final Logger logger = Logger.getLogger(XprocessMerger.class.getName());

    private static final String SVN_BASE_SUFFIX = ".svn-base";

    private byte[] myStart;

    private byte[] mySeparator;

    private byte[] myEnd;

    private Document baseDoc;

    private Document localDoc;

    private Document latestDoc;

    private DataSource myDataSource;

    public XprocessMerger(byte[] start, byte[] sep, byte[] end, DataSource dataSource) {
        myStart = start;
        mySeparator = sep;
        myEnd = end;
        myDataSource = dataSource;
    }

    public SVNStatusType mergeBinary(File arg0, File arg1, File arg2, boolean arg3, OutputStream arg4) throws SVNException {
        return null;
    }

    public SVNStatusType mergeText(File baseFile, File localFile, File latestFile, boolean dryRun, SVNDiffOptions svnDiffOptions) throws SVNException {
        DebugUtil.internalDebugSystemOut("mergeText called");
        DebugUtil.internalDebugSystemOut("DUMP FILE INDEX");
        DebugUtil.internalDebugSystemOut(myDataSource.getPersistenceHelper().getFileIndex().dumpContents());
        DebugUtil.internalDebugSystemOut("\t BASE: " + baseFile.getAbsolutePath());
        DebugUtil.internalDebugSystemOut("\t LOCAL: " + localFile.getAbsolutePath());
        DebugUtil.internalDebugSystemOut("\t LATEST: " + latestFile.getAbsolutePath());
        int status = 0;
        try {
            String path = baseFile.getCanonicalPath();
            if (path.toLowerCase().endsWith(XMLifier.XML_EXTENSION + SVN_BASE_SUFFIX)) {
                DebugUtil.internalDebugSystemOut("Perform XPX Merge");
                SAXBuilder parser = new SAXBuilder();
                baseDoc = parser.build(baseFile);
                localDoc = parser.build(localFile);
                latestDoc = parser.build(latestFile);
                IXprocessMerger merger = new Merger(myDataSource);
                FileOutputStream out = new FileOutputStream(localFile);
                status = merger.merge(baseDoc, localDoc, latestDoc, out);
                out.close();
            } else {
                DebugUtil.internalDebugSystemOut("=======================> OOPS");
                FileInputStream fis = new FileInputStream(localFile);
                byte[] buf = new byte[1024];
                int i = 0;
                while ((i = fis.read(buf)) != -1) {
                }
                fis.close();
            }
        } catch (Exception e) {
            SVNException svne = new SVNException(SVNErrorMessage.create(SVNErrorCode.UNKNOWN, e.getMessage()));
            logger.throwing(this.getClass().getName(), "mergeText", svne);
            throw svne;
        }
        return SVNStatusType.MERGED;
    }

    public SVNMergeResult mergeProperties(String arg0, SVNProperties arg1, SVNProperties arg2, SVNProperties arg3, SVNProperties arg4, SVNAdminArea arg5, SVNLog arg6, boolean arg7, boolean arg8) throws SVNException {
        return SVNMergeResult.createMergeResult(SVNStatusType.MERGED, null);
    }

    public SVNMergeResult mergeText(SVNMergeFileSet files, boolean arg1, SVNDiffOptions options) throws SVNException {
        mergeText(files.getBaseFile(), files.getLocalFile(), files.getRepositoryFile(), false, options);
        return SVNMergeResult.createMergeResult(SVNStatusType.MERGED, null);
    }
}
