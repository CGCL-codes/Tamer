package net.sourceforge.docfetcher.model.index.outlook;

import net.sourceforge.docfetcher.model.Document;
import net.sourceforge.docfetcher.model.DocumentType;
import net.sourceforge.docfetcher.util.annotations.NotNull;

/**
 * @author Tran Nam Quang
 */
@SuppressWarnings("serial")
final class MailDocument extends Document<MailDocument, MailFolder> {

    public MailDocument(@NotNull MailFolder parent, @NotNull String name, @NotNull String displayName, long lastModified) {
        super(parent, name, displayName, lastModified);
    }

    protected DocumentType getType() {
        return DocumentType.OUTLOOK;
    }

    public boolean isModified(long newLastModified) {
        return getLastModified() != newLastModified;
    }
}
