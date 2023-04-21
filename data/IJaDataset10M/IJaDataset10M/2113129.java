package ikube.index.content;

import ikube.model.IndexableColumn;
import ikube.toolkit.FileUtilities;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.sql.Types;
import org.apache.log4j.Logger;

/**
 * This class allows writing an object returned from a column in a database to the output stream specified in the
 * parameter list.
 * 
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
public class ColumnContentProvider implements IContentProvider<IndexableColumn> {

    private static final Logger LOGGER = Logger.getLogger(ColumnContentProvider.class);

    @Override
    public void getContent(final IndexableColumn indexable, final OutputStream outputStream) {
        Object object = indexable.getContent();
        int columnType = indexable.getColumnType();
        if (object == null) {
            return;
        }
        if (Blob.class.isAssignableFrom(object.getClass()) && columnType != Types.BLOB) {
            columnType = Types.BLOB;
        } else if (Clob.class.isAssignableFrom(object.getClass()) && columnType != Types.CLOB) {
            columnType = Types.CLOB;
        }
        InputStream inputStream = null;
        try {
            switch(columnType) {
                case Types.BOOLEAN:
                    outputStream.write(object.toString().getBytes());
                    break;
                case Types.BIT:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.BIGINT:
                case Types.FLOAT:
                case Types.REAL:
                case Types.DOUBLE:
                case Types.NUMERIC:
                case Types.DECIMAL:
                    outputStream.write(object.toString().getBytes());
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    outputStream.write(object.toString().getBytes());
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    Timestamp timestamp = null;
                    if (Timestamp.class.isAssignableFrom(object.getClass())) {
                        timestamp = (Timestamp) object;
                    } else {
                        Method method = findMethod(object, Timestamp.class);
                        if (method != null) {
                            method.setAccessible(true);
                            timestamp = (Timestamp) method.invoke(object, (Object[]) null);
                        }
                    }
                    if (timestamp != null) {
                        String string = Long.toString(timestamp.getTime());
                        outputStream.write(string.getBytes());
                    } else {
                        if (java.util.Date.class.isAssignableFrom(object.getClass())) {
                            long time = ((java.util.Date) object).getTime();
                            outputStream.write(Long.toString(time).getBytes());
                        }
                    }
                    break;
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                    outputStream.write((byte[]) object);
                    break;
                case Types.OTHER:
                    break;
                case Types.JAVA_OBJECT:
                case Types.DISTINCT:
                case Types.STRUCT:
                case Types.ARRAY:
                    break;
                case Types.NULL:
                    outputStream.write("null".getBytes());
                    break;
                case Types.BLOB:
                    if (Blob.class.isAssignableFrom(object.getClass())) {
                        inputStream = ((Blob) object).getBinaryStream();
                    } else {
                        Method method = findMethod(object, InputStream.class);
                        if (method != null) {
                            method.setAccessible(true);
                            inputStream = (InputStream) method.invoke(object, (Object[]) null);
                        }
                    }
                    FileUtilities.getContents(inputStream, outputStream, Integer.MAX_VALUE);
                    break;
                case Types.CLOB:
                    Reader reader = null;
                    if (Clob.class.isAssignableFrom(object.getClass())) {
                        reader = ((Clob) object).getCharacterStream();
                    } else {
                        Method method = findMethod(object, Reader.class);
                        if (method != null) {
                            method.setAccessible(true);
                            reader = (Reader) method.invoke(object, (Object[]) null);
                        }
                    }
                    FileUtilities.getContents(reader, outputStream, Integer.MAX_VALUE);
                    break;
                default:
                    throw new Exception("Type of column not known : " + columnType + ", " + indexable);
            }
        } catch (Exception e) {
            LOGGER.error("Exception accessing data from column.", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                LOGGER.error("Exception closing the input stream to the database : ", e);
            }
        }
    }

    /**
	 * Finds a method in an object that has as return type the class of the second parameter. For example in the Blob
	 * object for Oracle there is a method getBinaryStream that returns an input stream to the blob.
	 * 
	 * In DB2 it's getInputStream. We, due to the inability of these database vendors to implement the java.sq.Blob
	 * interface, have to guess what the method is. We don't care if it is a string stream or an ASCII stream the final
	 * data will be read in bytes and converted to string anyway.
	 * 
	 * @param object
	 *            the object that we need to find a method on
	 * @param klass
	 *            the return type of the method that we are looking for
	 * @return a method on the object that has as return type the class parameter
	 */
    protected Method findMethod(final Object object, final Class<?> klass) {
        if (object == null) {
            return null;
        }
        Method[] methods = object.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getParameterTypes().length == 0 && klass.isAssignableFrom(methods[i].getReturnType())) {
                return methods[i];
            }
        }
        return null;
    }
}
