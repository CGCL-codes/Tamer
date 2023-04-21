package com.rapidminer.operator.text.io.filereader;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.UserError;
import com.rapidminer.parameter.ParameterHandler;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.io.Encoding;

/**
 * @author Sebastian Land, Dominik Halfkann
 *
 */
public class FileReaders {

    public static final String PARAMETER_CONTENT_TYPE = "content_type";

    public static final String PARAMETER_USE_FILE_EXTENSION = "use_file_extension_as_type";

    public static final String PARAMETER_USE_DEFAULT_ENCODING = "use_default_encoding";

    public static final String PARAMETER_ENCODING = "encoding";

    public static final Class<?> DEFAULT_FILE_READER_CLASS = TextFileReader.class;

    public static final Map<String, Class<?>> FILE_TYPE_READER_MAPPING = new HashMap<String, Class<?>>();

    static {
        FILE_TYPE_READER_MAPPING.put("txt", TextFileReader.class);
        FILE_TYPE_READER_MAPPING.put("pdf", PDFFileReader.class);
        FILE_TYPE_READER_MAPPING.put("xml", XMLFileReader.class);
        FILE_TYPE_READER_MAPPING.put("htm", HTMLFileReader.class);
        FILE_TYPE_READER_MAPPING.put("html", HTMLFileReader.class);
    }

    public static final String[] FILE_TYPES = new String[] { "txt", "pdf", "xml", "html" };

    public static final String[] ENCODINGS = new String[] { "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16" };

    public static List<ParameterType> getParameterTypes(Operator operator) {
        List<ParameterType> types = new LinkedList<ParameterType>();
        types.add(new ParameterTypeBoolean(PARAMETER_USE_FILE_EXTENSION, "If checked, the type of the files will be determined by their extensions. Unkown extensions will be treated as text files.", true, false));
        ParameterType type = new ParameterTypeCategory(PARAMETER_CONTENT_TYPE, "The content type of the input texts", FILE_TYPES, 0);
        type.setExpert(false);
        type.registerDependencyCondition(new BooleanParameterCondition(operator, PARAMETER_USE_FILE_EXTENSION, true, false));
        types.add(type);
        types.addAll(Encoding.getParameterTypes(operator));
        return types;
    }

    /**
	 * Determines the correct FileReader for the given file based upon the file extension. If not present or unknown,
	 * the default FileReader is returned.
	 * @throws UserError 
	 */
    public static FileReader createFileReader(File file, ParameterHandler handler) throws UserError {
        try {
            String typeString = "";
            if (handler.getParameterAsBoolean(PARAMETER_USE_FILE_EXTENSION)) {
                String sourceName = file.getName();
                int index = sourceName.lastIndexOf('.');
                if (index >= 0) {
                    typeString = sourceName.substring(index + 1);
                }
            } else {
                typeString = FILE_TYPES[handler.getParameterAsInt(PARAMETER_CONTENT_TYPE)];
            }
            Class<?> readerClass;
            if (FILE_TYPE_READER_MAPPING.containsKey(typeString)) readerClass = FILE_TYPE_READER_MAPPING.get(typeString); else readerClass = DEFAULT_FILE_READER_CLASS;
            return (FileReader) readerClass.newInstance();
        } catch (InstantiationException e) {
            throw new UserError(null, 904, "FileReader", e);
        } catch (IllegalAccessException e) {
            throw new UserError(null, 904, "FileReader", e);
        }
    }

    /**
	 * This method returns the chosen charset or the default charset for any operator, containing
	 * the parameters provided by this class.
	 * @throws UserError 
	 */
    public static Charset getEncoding(Operator operator) throws UserError {
        return Encoding.getEncoding(operator);
    }
}
