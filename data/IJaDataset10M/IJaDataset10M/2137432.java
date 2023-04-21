package org.orbeon.oxf.processor.serializer.store;

import org.orbeon.oxf.common.OXFException;
import org.orbeon.oxf.pipeline.api.ExternalContext;
import org.orbeon.oxf.pipeline.api.PipelineContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Result store for binay data (e.g. PDF document)
 */
public class ResultStoreOutputStream extends ByteArrayOutputStream implements ResultStore {

    OutputStream out;

    public ResultStoreOutputStream(OutputStream out) {
        this.out = out;
    }

    public synchronized void write(int b) {
        try {
            out.write(b);
            super.write(b);
        } catch (IOException e) {
            throw new OXFException(e);
        }
    }

    public synchronized void write(byte b[], int off, int len) {
        try {
            out.write(b, off, len);
            super.write(b, off, len);
        } catch (IOException e) {
            throw new OXFException(e);
        }
    }

    public void close() throws IOException {
        if (out != null) out.close();
        out = null;
    }

    public void replay(PipelineContext context) {
        try {
            ExternalContext externalContext = (ExternalContext) context.getAttribute(PipelineContext.EXTERNAL_CONTEXT);
            replay(externalContext.getResponse().getOutputStream());
        } catch (IOException e) {
            throw new OXFException(e);
        }
    }

    public void replay(Writer writer) {
        throw new UnsupportedOperationException();
    }

    public void replay(OutputStream os) {
        try {
            writeTo(os);
        } catch (IOException e) {
            throw new OXFException(e);
        }
    }

    public int length(PipelineContext context) {
        return size();
    }
}
