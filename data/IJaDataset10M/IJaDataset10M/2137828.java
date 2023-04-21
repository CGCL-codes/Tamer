package jpfm.operations;

import java.util.concurrent.atomic.AtomicBoolean;
import jpfm.FileId;
import jpfm.JPfmError;

/**
 * Called to modify size of a file.
 * <br/><br/>
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
 * @see jpfm.JPfmFileSystem#setSize(jpfm.operations.SetSizeImpl)
 * @author Shashank Tulsyan
 */
public final class SetSizeImpl extends FileSystemOperationImpl implements SetSize {

    private final long handle;

    private final long formatterDispatch;

    private final long fileId;

    private final long fileSize;

    private final AtomicBoolean completed = new AtomicBoolean(false);

    SetSizeImpl(final long handle, final long formatterDispatch, final long fileidr, final long fileSize) {
        this.handle = handle;
        this.formatterDispatch = formatterDispatch;
        this.fileId = fileidr;
        this.fileSize = fileSize;
    }

    public final FileId getFileId() {
        return FILEID_BUILDER.constructFileId(fileId);
    }

    public final boolean isCompleted() {
        return completed.get();
    }

    public final long getFileSize() {
        return fileSize;
    }

    public final void complete(final JPfmError pfmError) throws IllegalStateException {
        if (!completed.compareAndSet(false, true)) {
            throw AlreadyCompleteException.createNew();
        }
        NativeInterfaceMethods.completeSetSize(handle, formatterDispatch, pfmError == null ? JPfmError.FAILED.getErrorCode() : pfmError.getErrorCode());
    }

    @Override
    protected final long getFormatterDispatchHandle() {
        return formatterDispatch;
    }

    public final void handleUnexpectedCompletion(final Exception exception) {
        if (!this.completed.get()) {
            this.complete(JPfmError.FAILED);
        }
    }

    @Override
    public String toString() {
        return "SetSize{" + fileId + "}";
    }
}
