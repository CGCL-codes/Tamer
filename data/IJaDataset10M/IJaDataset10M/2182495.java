package astcentric.structure.basic;

import java.io.InputStream;
import java.io.OutputStream;

interface ASTReaderWriterFactory {

    String getFileType();

    ASTReader createReader(InputStream inputStream);

    ASTWriter createWriter(OutputStream outputStream);
}
