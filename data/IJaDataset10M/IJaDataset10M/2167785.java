package org.exist.xquery.modules.compression;

import org.apache.commons.io.output.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.exist.dom.QName;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * Compresses a sequence of resources and/or collections into a Zip file
 * 
 * @author Adam Retter <adam@exist-db.org>
 * @version 1.0
 */
public class ZipFunction extends AbstractCompressFunction {

    private static final QName ZIP_FUNCTION_NAME = new QName("zip", CompressionModule.NAMESPACE_URI, CompressionModule.PREFIX);

    private static final String ZIP_FUNCTION_DESCRIPTION = "Zips nodes, resources and collections.";

    public static final FunctionSignature signatures[] = { new FunctionSignature(ZIP_FUNCTION_NAME, ZIP_FUNCTION_DESCRIPTION, new SequenceType[] { SOURCES_PARAM, COLLECTION_HIERARCHY_PARAM }, new SequenceType(Type.BASE64_BINARY, Cardinality.ZERO_OR_MORE)), new FunctionSignature(ZIP_FUNCTION_NAME, ZIP_FUNCTION_DESCRIPTION, new SequenceType[] { SOURCES_PARAM, COLLECTION_HIERARCHY_PARAM, STRIP_PREFIX_PARAM }, new SequenceType(Type.BASE64_BINARY, Cardinality.ZERO_OR_MORE)) };

    public ZipFunction(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    @Override
    protected void closeEntry(Object os) throws IOException {
        ((ZipOutputStream) os).closeEntry();
    }

    @Override
    protected Object newEntry(String name) {
        return new ZipEntry(name);
    }

    @Override
    protected void putEntry(Object os, Object entry) throws IOException {
        ((ZipOutputStream) os).putNextEntry((ZipEntry) entry);
    }

    @Override
    protected OutputStream stream(ByteArrayOutputStream baos) {
        return new ZipOutputStream(baos);
    }
}
