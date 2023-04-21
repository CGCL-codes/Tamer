package eu.pisolutions.ocelot.content.context;

import eu.pisolutions.ocelot.version.PdfVersion;

public abstract class AbstractContentContext<T extends ContentContext<?>> extends Object implements ContentContext<T> {

    private boolean closed;

    private PdfVersion requiredPdfVersion = PdfVersion.VERSION_1_0;

    protected AbstractContentContext() {
        super();
    }

    public final boolean isClosed() {
        return this.closed;
    }

    public PdfVersion getRequiredPdfVersion() {
        return this.requiredPdfVersion;
    }

    public final T close() {
        if (!this.closed) {
            this.doClose();
            this.closed = true;
        }
        return this.getParent();
    }

    protected final void requireNotClosed() {
        if (this.closed) {
            throw new IllegalStateException("Closed content context");
        }
    }

    protected final void requirePdfVersion(PdfVersion pdfVersion) {
        if (pdfVersion.compareTo(this.requiredPdfVersion) > 0) {
            this.requiredPdfVersion = pdfVersion;
        }
    }

    protected void doClose() {
    }
}
