package org.dishevelled.matrix.io.impl;

import java.io.IOException;
import org.dishevelled.matrix.Matrix2D;
import org.dishevelled.matrix.impl.SparseMatrix2D;
import org.dishevelled.matrix.io.AbstractMatrix2DWriterTest;
import org.dishevelled.matrix.io.Matrix2DWriter;

/**
 * Unit test for ValuesMatrix2DWriter.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class ValuesMatrix2DWriterTest extends AbstractMatrix2DWriterTest {

    /** {@inheritDoc} */
    protected <E> Matrix2DWriter<E> createMatrix2DWriter() {
        return new ValuesMatrix2DWriter<E>();
    }

    public void testEmptyMatrix() throws IOException {
        Matrix2D<String> matrix = new SparseMatrix2D<String>(0L, 0L);
        Matrix2DWriter<String> writer = new ValuesMatrix2DWriter<String>();
        StringBuffer appendable = new StringBuffer();
        appendable = writer.append(matrix, appendable);
        assertEquals("[]", appendable.toString());
    }

    public void testOneElementMatrix() throws IOException {
        Matrix2D<String> matrix = new SparseMatrix2D<String>(1L, 1L);
        matrix.setQuick(0L, 0L, "foo");
        Matrix2DWriter<String> writer = new ValuesMatrix2DWriter<String>();
        StringBuffer appendable = new StringBuffer();
        appendable = writer.append(matrix, appendable);
        assertEquals("[foo]", appendable.toString());
    }
}
