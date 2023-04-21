package org.exist.xquery.modules.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;

/**
 * @author Pierrick Brihaye
 * @author Dizzzz
 * @author Andrzej Taramina
 *
 */
public class FileReadUnicode extends BasicFunction {

    private static final Logger logger = Logger.getLogger(FileReadUnicode.class);

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("read-unicode", FileModule.NAMESPACE_URI, FileModule.PREFIX), "Reads the contents of a file.  Unicode BOM (Byte Order Marker) will be stripped " + "off if found.  This method is only available to the DBA role.", new SequenceType[] { new FunctionParameterSequenceType("path", Type.ITEM, Cardinality.EXACTLY_ONE, "The directory path or URI in the file system.") }, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_ONE, "the contents of the file")), new FunctionSignature(new QName("read-unicode", FileModule.NAMESPACE_URI, FileModule.PREFIX), "Reads the contents of a file.  Unicode BOM (Byte Order Marker) will be stripped " + "off if found.  This method is only available to the DBA role.", new SequenceType[] { new FunctionParameterSequenceType("path", Type.ITEM, Cardinality.EXACTLY_ONE, "The directory path or URI in the file system."), new FunctionParameterSequenceType("encoding", Type.STRING, Cardinality.EXACTLY_ONE, "The file is read with the encoding specified.") }, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_ONE, "the contents of the file")) };

    /**
	 * @param context
	 * @param signature
	 */
    public FileReadUnicode(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (!context.getSubject().hasDbaRole()) {
            XPathException xPathException = new XPathException(this, "Permission denied, calling user '" + context.getSubject().getName() + "' must be a DBA to call this function.");
            logger.error("Invalid user", xPathException);
            throw xPathException;
        }
        String inputPath = args[0].getStringValue();
        File file = FileModuleHelper.getFile(inputPath);
        StringWriter sw;
        try {
            FileInputStream fis = new FileInputStream(file);
            UnicodeReader reader;
            if (args.length > 1) {
                reader = new UnicodeReader(fis, args[1].getStringValue());
            } else {
                reader = new UnicodeReader(fis);
            }
            sw = new StringWriter();
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) > 0) {
                sw.write(buf, 0, len);
            }
            reader.close();
            sw.close();
        } catch (MalformedURLException e) {
            throw new XPathException(this, e);
        } catch (IOException e) {
            throw new XPathException(this, e);
        }
        return (new StringValue(sw.toString()));
    }
}
