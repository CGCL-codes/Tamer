package de.ddb.conversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.ddb.charset.CharsetUtil;
import de.ddb.conversion.util.ErrorTransportingPipedReader;
import de.ddb.conversion.util.RecordsSeparator;

/**
 * <p>
 * Provides an implementation of the method
 * {@link de.ddb.conversion.GenericConverter#convert(InputStream, OutputStream, String, String)}:
 * <p>
 * Before conversion the in-bytestream is converted into a character-stream
 * using the source charset. The resulting output-character-stream is converted
 * into a byte stream using the target charset.
 * </p>
 * <p>
 * This implementation is reasonable for converters that are independent of
 * input- and output-charsets.
 * </p>
 */
public abstract class CharstreamConverter extends GenericConverter {

    private static final Log LOGGER = LogFactory.getLog(CharstreamConverter.class);

    /**
	 * <p>
	 * Default implementation of this method. Before conversion the
	 * in-bytestream is converted into a character-stream using the source
	 * charset. The resulting output-character-stream is converted into a byte
	 * stream using the target charset.
	 * </p>
	 * 
	 * @see de.ddb.conversion.GenericConverter#convert(InputStream,
	 *      OutputStream, ConversionParameters)
	 * @since 29.04.2005
	 */
    public void convert(InputStream in, OutputStream out, ConversionParameters params) throws ConverterException, IOException {
        if (params.getSourceCharset() == null || params.getTargetCharset() == null) {
            throw new ConverterException("Missing charset {source=" + params.getSourceCharset() + ", target=" + params.getTargetCharset() + "}.");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating reader(" + params.getSourceCharset() + ") and writer(" + params.getTargetCharset() + ").");
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        reader = new BufferedReader(new InputStreamReader(in, CharsetUtil.forName(params.getSourceCharset())));
        writer = new BufferedWriter(new OutputStreamWriter(out, CharsetUtil.forName(params.getTargetCharset())));
        convert(reader, writer, params);
        try {
            writer.flush();
        } catch (Exception e) {
        }
    }

    /**
	 * @param in
	 * @param out
	 * @throws ConverterException
	 * @throws IOException
	 */
    public void convert(Reader in, Writer out) throws ConverterException, IOException {
        convert(in, out, new ConversionParameters());
    }

    /**
	 * @param reader
	 * @param writer
	 * @param params
	 * @throws ConverterException
	 * @throws IOException
	 */
    public abstract void convert(Reader reader, Writer writer, ConversionParameters params) throws ConverterException, IOException;

    /**
     * <p>
     * Converts a string and returns the result. Best choice for small chunks of
     * input-data.
     * </p>
     * 
     * @param input
     *            the characters that are to be converted
     * @return converted string
     * @throws ConverterException
     * @throws IOException
     * @see de.ddb.conversion.Converter#convert(java.lang.String)
     * @since 16.06.2005
     */
    public String convert(String input) throws ConverterException, IOException {
        StringWriter out = new StringWriter();
        convert(new StringReader(input), out);
        return out.getBuffer().toString();
    }

    /**
     * <p>
     * Converts a string and returns the result. Best choice for small chunks of
     * input-data.
     * </p>
     * 
     * @param input
     *            the characters that are to be converted
     * @param params
     *            the ConversionParameters for this request
     * @return converted string
     * @throws ConverterException
     * @throws IOException
     * 
     * @see de.ddb.conversion.Converter#convert(java.lang.String, ConversionParameters)
     * @since 16.06.2005
     */
    public String convert(String input, ConversionParameters params) throws ConverterException, IOException {
        StringWriter out = new StringWriter();
        convert(new StringReader(input), out, params);
        return out.getBuffer().toString();
    }

    /**
     * @param input
     * @return List of records
     * @throws ConverterException
     * @throws IOException
     */
    public List<byte[]> convertToList(String input) throws ConverterException, IOException {
        List<byte[]> ret = new ArrayList<byte[]>();
        String output = this.convert(input);
        RecordsSeparator.splitRecords(output, ret, this.getConverterContext().getTargetFormat().getPattern(), this.getConverterContext().getTargetFormat().getPatternType());
        return ret;
    }

    /**
     * @param source 
     * @return 
     * @throws ConverterException 
     * @throws IOException 
     * @see de.ddb.conversion.Converter#convert(java.io.Reader)
     * @since 16.01.2006
     */
    public final Reader convert(Reader source) throws ConverterException, IOException {
        return convert(source, new ConversionParameters());
    }

    /**
     * @param source 
     * @param params 
     * @return 
     * @throws ConverterException 
     * @throws IOException 
     * @see de.ddb.conversion.Converter#convert(java.io.Reader,
     *      ConversionParameters)
     * @since 16.01.2006
     */
    public final Reader convert(Reader source, ConversionParameters params) throws ConverterException, IOException {
        PipedWriter pipeIn = new PipedWriter();
        ErrorTransportingPipedReader pipeOut = new ErrorTransportingPipedReader();
        try {
            pipeOut.connect(pipeIn);
        } catch (IOException e) {
        }
        CharStreamConversionThread thread = new CharStreamConversionThread();
        thread.in = new BufferedReader(source);
        thread.out = pipeIn;
        thread.pipeOut = pipeOut;
        thread.params = params;
        thread.start();
        return pipeOut;
    }

    class CharStreamConversionThread extends Thread {

        /**
         * Logger for this class
         */
        private final Log logger = LogFactory.getLog(CharStreamConversionThread.class);

        PipedWriter out;

        Reader in;

        ErrorTransportingPipedReader pipeOut;

        ConversionParameters params;

        /**
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            if (logger.isDebugEnabled()) {
                logger.debug("Converting char stream...");
            }
            try {
                convert(this.in, this.out, this.params);
            } catch (Exception e) {
                logger.error(e);
                IOException ioe = new IOException(CharstreamConverter.class + " failed. Reason: " + e.getMessage());
                ioe.initCause(e);
                pipeOut.setException(ioe);
            } finally {
                try {
                    this.in.close();
                    this.out.close();
                } catch (IOException e) {
                    this.logger.error("run()", e);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Done converting char srtream.");
            }
        }
    }
}
