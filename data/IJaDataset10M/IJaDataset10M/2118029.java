package jpfm.operations;

import java.util.concurrent.atomic.AtomicBoolean;
import jpfm.AccessLevel;
import jpfm.FileAttributesProvider;
import jpfm.FileControlFlag;
import jpfm.FileId;
import jpfm.JPfmError;

/**
 * <u>Note </u> : ANY PFM OpenID (or FileId) field or method is equivalently represented
 * in JPfm by a field or method with return type FileId<br/>
 * A example can be found in javadoc of {@link Replace }
 * <br/>
 * <br/><br/><br/><br/>
 * <b><u>Additional reference from PFM docs</u></b><br/><br/>
 * <i>Please note : Native implementation is different from java implementation. The description
 * below must be used only in absence of javadoc, or for reference sake only.</i><br/><br/>
 * You can also read the latest version of this at {@link http://www.pismotechnic.com/pfm/doc/ }
 * <br/><br/>
 * This function is called by the marshaller to process move requests 
 * from the driver. This request is made when a file is being renamed.
 * Proper handling of the newExistingOpenId parameter, exists result,
 * openAttribs->openId result, and openAttribs->openSequence result, are
 * critical to proper functioning of the atomicity guarantees provided by
 * the driver.<br/><br/>
 * The sourceOpenId parameter specifies the previously opened file that
 * is being renamed. The sourceParentFileId and sourceEndName parameters
 * can be used by formatters that support hard links to determine which
 * name for the file is being renamed.<br/><br/>
 * The targetNameParts and targetNamePartCount parameters specified the
 * new file name (target) for the file.<br/><br/>
 * If the parent folder of the target file name does not exist then the
 * formatter should return pfmErrorParentNotFound.<br/><br/>
 * If a file already exists with the target file name, then the existing
 * target file should be opened. In this case the source file is left
 * unmodified and no rename or move operation is performed. The variable
 * pointed to by the existed parameter must be set to true. The
 * newExistingOpenId, openAttribs, parentFileId, and endName parameters
 * should be used in the same manner as is described for the
 * PfmFormatterOps::Open function when an existing file is opened.<br/><br/>
 * If the source file name has been deleted then the move request is
 * the equivalent of an undelete. Formatters must support this for files,
 * but can return pfmErrorInvalid for folders.<br/><br/>
 * If the deleteSource parameter is false, and the formatter does not
 * determine that the source file name is deleted, then the move request
 * is creating an additional name for the file (hard link). Formatters
 * that do not support hard links should fail the request with
 * pfmErrorInvalid.<br/><br/>
 * If the formatter is able to create the new name for the file then 
 * updated information about the source file is returned through the
 * openAttribs and parentFileId parameters, openAttribs->openId must
 * contain sourceOpenId, openAttribs->openSequence must contain a value
 * equal to or higher than the largest openSequence value returned
 * in any previous open for the file. The variable pointed to by the
 * existed parameter must be set to false.
 * @see jpfm.JPfmFileSystem#move(jpfm.operations.MoveImpl)
 * @author Shashank Tulsyan
 */
public final class MoveImpl extends FileSystemOperationImpl implements Move {

    private final long handle;

    private final long formatterDispatch;

    private final long sourceFileId;

    private final long sourceParentFileId;

    private final String sourceEndName;

    private final String[] targetName;

    private final boolean deleteSource;

    private final long writeTime;

    private final long newExistingFileId;

    private final AtomicBoolean completed = new AtomicBoolean(false);

    MoveImpl(final long handle, final long formatterDispatch, final long sourceFileId, final long sourceParentFileId, final String sourceEndName, final String[] targetName, final boolean deleteSource, final long writeTime, final long newExistingOpenId) {
        this.handle = handle;
        this.formatterDispatch = formatterDispatch;
        this.sourceFileId = sourceFileId;
        this.sourceParentFileId = sourceParentFileId;
        this.sourceEndName = sourceEndName;
        this.targetName = targetName;
        this.deleteSource = deleteSource;
        this.writeTime = writeTime;
        this.newExistingFileId = newExistingOpenId;
    }

    public final boolean isDeleteSource() {
        return deleteSource;
    }

    public final FileId getNewExistingFileId() {
        return FILEID_BUILDER.constructFileId(newExistingFileId);
    }

    public final String getSourceEndName() {
        return sourceEndName;
    }

    public final boolean isCompleted() {
        return completed.get();
    }

    public final FileId getSourceFileId() {
        return FILEID_BUILDER.constructFileId(sourceFileId);
    }

    public final FileId getSourceParentFileId() {
        return FILEID_BUILDER.constructFileId(sourceParentFileId);
    }

    public final String[] getTargetName() {
        return targetName;
    }

    public final long getWriteTime() {
        return writeTime;
    }

    public final void complete(final JPfmError pfmError, final boolean existed, final FileAttributesProvider fileAttributesProvider, final String endName, final AccessLevel accessLevel, final FileControlFlag controlFlag) throws IllegalStateException {
        if (!completed.compareAndSet(false, true)) {
            throw AlreadyCompleteException.createNew();
        }
        if (fileAttributesProvider != null) {
            if (fileAttributesProvider.getFileDescriptor() != null) {
                if (pfmError != null) {
                    if (pfmError == JPfmError.SUCCESS) {
                        FileSystemOperationImpl.FD_MODIFIER.getAndIncrementOpenSequence(fileAttributesProvider.getFileDescriptor());
                    }
                }
            }
        }
        NativeInterfaceMethods.completeMove(handle, formatterDispatch, pfmError == null ? JPfmError.FAILED.getErrorCode() : pfmError.getErrorCode(), existed, fileAttributesProvider, fileAttributesProvider == null ? null : fileAttributesProvider.getParentFileDescriptor(), endName == null ? "" : endName, fileAttributesProvider == null ? null : fileAttributesProvider.getFileDescriptor(), accessLevel == null ? 0 : accessLevel.getAccessLevel(), controlFlag == null ? 0 : controlFlag.getControlFlag());
    }

    @Override
    protected final long getFormatterDispatchHandle() {
        return formatterDispatch;
    }

    public final void handleUnexpectedCompletion(final Exception exception) {
        if (!isCompleted()) {
            this.complete(JPfmError.FAILED, false, null, null, null, null);
        }
    }

    @Override
    public String toString() {
        return "Move{\n\t\tsourceFileId=" + sourceFileId + "\n\t\tsourceParentFileId=" + sourceParentFileId + "\n\t\tsourceEndName=" + sourceEndName + "\n\t\ttargetName=" + getAsString(targetName) + "\n\t\tdeleteSource=" + deleteSource + "\n\t\twriteTime=" + writeTime + "\n\t\tnewExistingFileId=" + newExistingFileId + "\n}";
    }

    private static final String getAsString(String[] a) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("$Root\\");
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
        }
        return sb.toString();
    }
}
