package org.dishevelled.matrix.io.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.dishevelled.matrix.Matrix2D;
import org.dishevelled.matrix.io.Matrix2DWriter;

/**
 * Abstract writer for matrices of objects in two dimensions.
 *
 * @param <E> 2D matrix element type
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public abstract class AbstractMatrix2DWriter<E> implements Matrix2DWriter<E> {

    /** {@inheritDoc} */
    public final void write(final Matrix2D<? extends E> matrix, final File file) throws IOException {
        if (matrix == null) {
            throw new IllegalArgumentException("matrix must not be null");
        }
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            append(matrix, writer);
        } finally {
            MatrixIOUtils.closeQuietly(writer);
        }
    }

    /** {@inheritDoc} */
    public final void write(final Matrix2D<? extends E> matrix, final OutputStream outputStream) throws IOException {
        if (matrix == null) {
            throw new IllegalArgumentException("matrix must not be null");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream must not be null");
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        append(matrix, writer);
        writer.flush();
    }
}
