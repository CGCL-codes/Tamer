package net.sourceforge.docfetcher.model.index.outlook;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import net.sourceforge.docfetcher.model.Cancelable;
import net.sourceforge.docfetcher.model.DocumentType;
import net.sourceforge.docfetcher.model.Path;
import net.sourceforge.docfetcher.model.TreeIndex;
import net.sourceforge.docfetcher.model.UtilModel;
import net.sourceforge.docfetcher.model.index.IndexWriterAdapter;
import net.sourceforge.docfetcher.model.index.IndexingError;
import net.sourceforge.docfetcher.model.index.IndexingError.ErrorType;
import net.sourceforge.docfetcher.model.index.IndexingException;
import net.sourceforge.docfetcher.model.index.IndexingReporter;
import net.sourceforge.docfetcher.util.Util;
import net.sourceforge.docfetcher.util.annotations.NotNull;
import net.sourceforge.docfetcher.util.annotations.Nullable;
import net.sourceforge.docfetcher.util.annotations.RecursiveMethod;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

/**
 * @author Tran Nam Quang
 */
@SuppressWarnings("serial")
public final class OutlookIndex extends TreeIndex<MailDocument, MailFolder> {

    private MailFolder simplifiedRootFolder;

    public OutlookIndex(@Nullable File indexParentDir, @NotNull File pstFile) {
        super(indexParentDir, pstFile);
    }

    @NotNull
    protected String getIndexDirName(@NotNull File pstFile) {
        return Util.splitFilename(pstFile)[0];
    }

    @NotNull
    protected MailFolder createRootFolder(@NotNull Path path) {
        return new MailFolder(path);
    }

    public boolean isEmailIndex() {
        return true;
    }

    public DocumentType getDocumentType() {
        return DocumentType.OUTLOOK;
    }

    public IndexingResult doUpdate(@NotNull IndexingReporter reporter, @NotNull Cancelable cancelable) {
        reporter.setStartTime(System.currentTimeMillis());
        MailFolder rootFolder = getRootFolder();
        rootFolder.setError(null);
        IndexWriterAdapter writer = null;
        try {
            File rootFile = getCanonicalRootFile();
            long newLastModified = rootFile.lastModified();
            if (UtilModel.isUnmodifiedArchive(rootFolder, newLastModified)) return IndexingResult.SUCCESS_UNCHANGED;
            rootFolder.setLastModified(newLastModified);
            writer = new IndexWriterAdapter(getLuceneDir());
            OutlookContext context = new OutlookContext(getConfig(), writer, reporter, cancelable);
            PSTFile pstFile = new PSTFile(rootFile.getPath());
            visitFolder(context, rootFolder, pstFile.getRootFolder());
            simplifiedRootFolder = new TreeRootSimplifier<MailFolder>() {

                protected boolean hasContent(MailFolder node) {
                    return node.getDocumentCount() > 0;
                }

                protected boolean hasDeepContent(MailFolder node) {
                    return node.hasDeepContent();
                }

                protected Collection<MailFolder> getChildren(MailFolder node) {
                    return node.getSubFolders();
                }
            }.getSimplifiedRoot(getRootFolder());
            writer.optimize();
            return IndexingResult.SUCCESS_CHANGED;
        } catch (PSTException e) {
            report(reporter, e);
        } catch (IOException e) {
            report(reporter, e);
        } catch (IndexingException e) {
            report(reporter, e.getIOException());
        } finally {
            Closeables.closeQuietly(writer);
            reporter.setEndTime(System.currentTimeMillis());
        }
        return IndexingResult.FAILURE;
    }

    private void report(@NotNull IndexingReporter reporter, @NotNull Exception e) {
        MailFolder rootFolder = getRootFolder();
        IndexingError error = new IndexingError(ErrorType.IO_EXCEPTION, rootFolder, e);
        rootFolder.setError(error);
        reporter.fail(error);
    }

    @NotNull
    public MailFolder getSimplifiedRootFolder() {
        if (simplifiedRootFolder == null) return getRootFolder();
        return simplifiedRootFolder;
    }

    @RecursiveMethod
    private static void visitFolder(@NotNull OutlookContext context, @NotNull MailFolder folder, @NotNull PSTFolder pstFolder) throws IndexingException, PSTException {
        folder.setHasDeepContent(false);
        final Map<String, MailDocument> unseenMails = Maps.newHashMap(folder.getDocumentMap());
        final Map<String, MailFolder> unseenSubFolders = Maps.newHashMap(folder.getSubFolderMap());
        if (pstFolder.getContentCount() > 0) {
            try {
                PSTMessage pstMail = (PSTMessage) pstFolder.getNextChild();
                while (pstMail != null) {
                    if (context.isStopped()) break;
                    String id = String.valueOf(pstMail.getDescriptorNodeId());
                    long newLastModified = pstMail.getLastModificationTime().getTime();
                    MailDocument mail = unseenMails.remove(id);
                    if (mail == null) {
                        String subject = pstMail.getSubject();
                        mail = new MailDocument(folder, id, subject, newLastModified);
                        context.index(mail, pstMail, true);
                    } else if (mail.isModified(newLastModified)) {
                        mail.setLastModified(newLastModified);
                        context.index(mail, pstMail, false);
                    }
                    try {
                        pstMail = (PSTMessage) pstFolder.getNextChild();
                    } catch (IndexOutOfBoundsException e) {
                        Util.printErr(e.getMessage());
                        pstMail = null;
                    }
                }
            } catch (IOException e) {
                throw new IndexingException(e);
            }
            folder.setHasDeepContent(true);
        }
        if (pstFolder.hasSubfolders()) {
            try {
                for (PSTFolder pstSubFolder : pstFolder.getSubFolders()) {
                    if (context.isStopped()) break;
                    String foldername = pstSubFolder.getDisplayName();
                    MailFolder subFolder = unseenSubFolders.remove(foldername);
                    if (subFolder == null) subFolder = new MailFolder(folder, foldername);
                    visitFolder(context, subFolder, pstSubFolder);
                    if (subFolder.hasDeepContent()) folder.setHasDeepContent(true);
                }
            } catch (IOException e) {
                throw new IndexingException(e);
            }
        }
        if (context.isStopped()) return;
        for (MailDocument mail : unseenMails.values()) {
            context.deleteFromIndex(mail.getUniqueId());
            folder.removeDocument(mail);
        }
        for (MailFolder subFolder : unseenSubFolders.values()) folder.removeSubFolder(subFolder);
    }
}
