package org.spbu.pldoctoolkit.cache;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.Validator;
import com.thaiopensource.validate.auto.AutoSchemaReader;
import com.thaiopensource.xml.sax.ErrorHandlerImpl;
import com.thaiopensource.xml.sax.Jaxp11XMLReaderCreator;
import com.thaiopensource.xml.sax.XMLReaderCreator;

public class SchemaCache {

    private static final SchemaReader SCHEMA_READER = new AutoSchemaReader();

    private static final XMLReaderCreator XML_READER_CREATOR = new Jaxp11XMLReaderCreator();

    private static final PropertyMap READER_PROPERTIES;

    static {
        PropertyMapBuilder builder = new PropertyMapBuilder();
        ValidateProperty.XML_READER_CREATOR.put(builder, XML_READER_CREATOR);
        ValidateProperty.ERROR_HANDLER.put(builder, new ErrorHandlerImpl());
        READER_PROPERTIES = builder.toPropertyMap();
    }

    private Map<String, Schema> schemaMap = new HashMap<String, Schema>();

    public Schema getSchema(URL url) throws IOException, SAXException, IncorrectSchemaException {
        String urlString = url.toString();
        Schema schema = schemaMap.get(urlString);
        if (schema == null) {
            schema = SCHEMA_READER.createSchema(new InputSource(urlString), READER_PROPERTIES);
            schemaMap.put(urlString, schema);
        }
        return schema;
    }

    public Validator getValidator(URL schemaURL, ErrorHandler errorHandler) throws IOException, SAXException, IncorrectSchemaException {
        Schema schema = getSchema(schemaURL);
        PropertyMapBuilder builder = new PropertyMapBuilder();
        ValidateProperty.XML_READER_CREATOR.put(builder, XML_READER_CREATOR);
        ValidateProperty.ERROR_HANDLER.put(builder, errorHandler);
        return schema.createValidator(builder.toPropertyMap());
    }
}
