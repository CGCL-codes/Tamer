package naru.aweb.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import naru.async.pool.PoolBase;
import naru.async.pool.PoolManager;
import naru.async.store.Page;
import naru.aweb.config.Config;
import naru.aweb.util.ByteBufferInputStream;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.Logger;

public class ParameterParser extends PoolBase {

    private static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";

    private static final String CONTENT_TYPE_UPLOAD = "multipart/form-data";

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String CONTENT_TYPE_TEXT_PREFIX = "text/";

    private static Logger logger = Logger.getLogger(ParameterParser.class);

    private static Config config = Config.getConfig();

    public enum ParseType {

        NONE, URL_ENCODED, TEXT, JSON, UPLOAD, UNSUPPORTED
    }

    private ParseType parseType;

    private static String defaultEncoding = "ISO8859_1";

    private Map parameter = new HashMap();

    private String text;

    private JSON jsonObject;

    private CharsetDecoder decoder;

    private CharBuffer textBuffer;

    private File uploadRawFile = null;

    private OutputStream uploadRawFileOutputStream = null;

    private Page uploadPageBuffer = new Page();

    private FileuploadRequestContext fileuploadRequestContext = new FileuploadRequestContext();

    private String encoding = "utf-8";

    private String contentType;

    private long contentLength;

    private long readPointer;

    public static int indexOfByte(byte bytes[], int off, int end, char qq) {
        while (off < end) {
            byte b = bytes[off];
            if (b == qq) return off;
            off++;
        }
        return -1;
    }

    public static int lastIndexOfByte(byte bytes[], int off, int end, char qq) {
        end--;
        while (off < end) {
            byte b = bytes[end];
            if (b == qq) return end;
            end--;
        }
        return -1;
    }

    private void processParameters(byte[] body, String enc) {
        processParameters(body, 0, body.length, enc);
    }

    private void processParameters(byte[] body, int pos, int end, String enc) {
        do {
            boolean noEq = false;
            int valStart = -1;
            int valEnd = -1;
            int nameStart = pos;
            int nameEnd = indexOfByte(body, nameStart, end, '=');
            int nameEnd2 = indexOfByte(body, nameStart, end, '&');
            if ((nameEnd2 != -1) && (nameEnd == -1 || nameEnd > nameEnd2)) {
                nameEnd = nameEnd2;
                noEq = true;
                valStart = nameEnd;
                valEnd = nameEnd;
            }
            if (nameEnd == -1) nameEnd = end;
            if (!noEq) {
                valStart = (nameEnd < end) ? nameEnd + 1 : end;
                valEnd = indexOfByte(body, valStart, end, '&');
                if (valEnd == -1) valEnd = (valStart < end) ? end : valStart;
            }
            pos = valEnd + 1;
            if (nameEnd <= nameStart) {
                continue;
            }
            try {
                String encName = new String(body, nameStart, nameEnd - nameStart, defaultEncoding);
                String encValue = new String(body, valStart, valEnd - valStart, defaultEncoding);
                String name = null;
                try {
                    name = URLDecoder.decode(encName, enc);
                } catch (IllegalArgumentException e) {
                    logger.warn("name decode error.name:" + name);
                    continue;
                }
                String value = null;
                try {
                    value = URLDecoder.decode(encValue, enc);
                } catch (IllegalArgumentException e) {
                    logger.warn("value decode error.value:" + value + " name:" + name);
                }
                addParameter(name, value);
            } catch (IOException e) {
                logger.warn("Parameters: Character decoding failed. " + "Parameter skipped.", e);
            }
        } while (pos < end);
    }

    public void recycle() {
        Iterator itr = parameter.values().iterator();
        while (itr.hasNext()) {
            Object o = itr.next();
            if (o instanceof DiskFileItem) {
                DiskFileItem item = (DiskFileItem) o;
                item.delete();
            }
        }
        parameter.clear();
        readPointer = 0;
        encoding = "utf-8";
        parseType = ParseType.NONE;
        decoder = null;
        textBuffer = null;
        text = null;
        jsonObject = null;
        if (uploadRawFileOutputStream != null) {
            try {
                uploadRawFileOutputStream.close();
            } catch (IOException ignore) {
            }
            uploadRawFileOutputStream = null;
        }
        if (uploadRawFile != null) {
            if (uploadRawFile.exists()) {
                uploadRawFile.delete();
            }
            uploadRawFile = null;
        }
        uploadPageBuffer.recycle();
    }

    public void init(String method, String contentType, long contentLength) {
        init(method, contentType, contentLength, "utf-8");
    }

    public void init(String method, String contentType, long contentLength, String encoding) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.encoding = encoding;
        this.readPointer = 0;
        if (contentType == null) {
            parseType = ParseType.NONE;
            return;
        }
        int semicolon = contentType.indexOf(';');
        if (semicolon >= 0) {
            contentType = contentType.substring(0, semicolon).trim();
        } else {
            contentType = contentType.trim();
        }
        if ((CONTENT_TYPE_URLENCODED.equals(contentType))) {
            parseType = ParseType.URL_ENCODED;
        } else if (CONTENT_TYPE_UPLOAD.equals(contentType)) {
            parseType = ParseType.UPLOAD;
        } else if (CONTENT_TYPE_JSON.equals(contentType)) {
            parseType = ParseType.JSON;
            Charset charset = Charset.forName(encoding);
            decoder = charset.newDecoder();
            textBuffer = CharBuffer.allocate((int) contentLength);
        } else if (CONTENT_TYPE_TEXT_PREFIX.equals(contentType)) {
            parseType = ParseType.TEXT;
            Charset charset = Charset.forName(encoding);
            decoder = charset.newDecoder();
            textBuffer = CharBuffer.allocate((int) contentLength);
        } else {
            parseType = ParseType.UNSUPPORTED;
        }
    }

    public void parseQuery(String query) {
        try {
            processParameters(query.getBytes("ISO8859_1"), encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("fail to getBytes.");
            throw new RuntimeException("fail to getBytes.", e);
        }
    }

    private byte[] lestOfBody = null;

    private void concatLestOfBody(byte[] buf, int pos, int end) {
        int length = 0;
        if (lestOfBody == null) {
            length = end - pos;
        } else {
            length = lestOfBody.length + (end - pos);
        }
        byte[] lest = new byte[length];
        if (lestOfBody != null) {
            System.arraycopy(lestOfBody, 0, lest, 0, lestOfBody.length);
            System.arraycopy(buf, pos, lest, lestOfBody.length, (end - pos));
        } else {
            System.arraycopy(buf, pos, lest, 0, (end - pos));
        }
        lestOfBody = lest;
    }

    private void parseLestOfBody() {
        if (lestOfBody != null) {
            processParameters(lestOfBody, encoding);
            lestOfBody = null;
        }
    }

    private void parseUrlEncode(ByteBuffer buffer) {
        int bufferLength = buffer.remaining();
        int position = buffer.position();
        int end = position + bufferLength;
        buffer.position(end);
        byte[] body = buffer.array();
        if (lestOfBody != null) {
            int firstAnp = indexOfByte(body, position, end, '&');
            if (firstAnp >= 0) {
                concatLestOfBody(body, position, firstAnp);
                parseLestOfBody();
                position = firstAnp;
            } else {
                concatLestOfBody(body, position, end);
                position = position + bufferLength;
            }
        }
        if (readPointer == contentLength) {
            parseLestOfBody();
            processParameters(body, position, end, encoding);
        } else {
            int lastAnp = lastIndexOfByte(body, position, end, '&');
            if (lastAnp >= 0) {
                parseLestOfBody();
                processParameters(body, position, lastAnp, encoding);
                concatLestOfBody(body, lastAnp + 1, end);
            } else {
                concatLestOfBody(body, position, end);
            }
        }
        PoolManager.poolBufferInstance(buffer);
    }

    private void parseText(ByteBuffer buffer) throws CharacterCodingException {
        boolean endOfInput = false;
        if (readPointer >= contentLength) {
            endOfInput = true;
        }
        CoderResult result = decoder.decode(buffer, textBuffer, endOfInput);
        PoolManager.poolBufferInstance(buffer);
        if (result.isError()) {
            result.throwException();
        }
        if (!endOfInput) {
            return;
        }
        result = decoder.flush(textBuffer);
        if (result.isError()) {
            result.throwException();
        }
        textBuffer.flip();
        text = textBuffer.toString();
        if (parseType == ParseType.JSON) {
            jsonObject = JSONSerializer.toJSON(text);
        }
        textBuffer = null;
    }

    private void processUpload(InputStream uploadInputStream) throws IOException {
        DiskFileItemFactory itemFactory = config.getUploadItemFactory();
        FileUpload fileUpload = new FileUpload(itemFactory);
        fileuploadRequestContext.setup(contentType, (int) contentLength, uploadInputStream, encoding);
        try {
            List itemList = fileUpload.parseRequest(fileuploadRequestContext);
            Iterator itr = itemList.iterator();
            while (itr.hasNext()) {
                DiskFileItem item = (DiskFileItem) itr.next();
                if (item.isFormField()) {
                    addParameter(item.getFieldName(), item.getString(encoding));
                    item.delete();
                } else {
                    parameter.put(item.getFieldName(), item);
                }
            }
        } catch (FileUploadException e) {
            logger.warn("fileupload error.", e);
            throw new IOException("fileupload error");
        }
    }

    private void writeUploadRaw(ByteBuffer[] buffers) throws IOException {
        for (int i = 0; i < buffers.length; i++) {
            writeUploadRaw(buffers[i]);
        }
        PoolManager.poolArrayInstance(buffers);
    }

    private void writeUploadRaw(ByteBuffer buffer) throws IOException {
        uploadRawFileOutputStream.write(buffer.array(), buffer.position(), buffer.remaining());
        PoolManager.poolBufferInstance(buffer);
    }

    private void parseUpload(ByteBuffer buffer) throws IOException {
        boolean endOfInput = false;
        if (readPointer >= contentLength) {
            endOfInput = true;
        }
        if (uploadRawFileOutputStream == null) {
            if (!uploadPageBuffer.putBuffer(buffer, false)) {
                uploadRawFile = File.createTempFile("upraw", ".tmp", config.getTmpDir());
                uploadRawFileOutputStream = new FileOutputStream(uploadRawFile);
                ByteBuffer[] buffers = uploadPageBuffer.getBuffer();
                writeUploadRaw(buffers);
                uploadPageBuffer.recycle();
                writeUploadRaw(buffer);
            }
        } else {
            writeUploadRaw(buffer);
        }
        if (!endOfInput) {
            return;
        }
        InputStream uploadInputStream = null;
        if (uploadRawFileOutputStream != null) {
            try {
                uploadRawFileOutputStream.close();
            } catch (IOException ignore) {
            }
            uploadRawFileOutputStream = null;
            uploadInputStream = new FileInputStream(uploadRawFile);
        } else {
            uploadInputStream = new ByteBufferInputStream(uploadPageBuffer.getBuffer());
        }
        processUpload(uploadInputStream);
        try {
            uploadInputStream.close();
        } catch (IOException ignore) {
        }
        if (uploadRawFile != null) {
            if (uploadRawFile.exists()) {
                uploadRawFile.delete();
            }
            uploadRawFile = null;
        }
    }

    /**
	 * �P�o�b�t�@�ɓ���Ă��邱�Ƃ������A���̎��̃p�t�H�[�}���X���l��
	 * �p�P�b�g�������ꂽ�ꍇ�̃p�t�H�[�}���X�����͖̂ڂ��Ԃ�
	 * @param buffer
	 * @return
	 * @throws IOException 
	 */
    public void parse(ByteBuffer buffer) throws IOException {
        if (readPointer >= contentLength) {
            PoolManager.poolBufferInstance(buffer);
            return;
        }
        int bufferLength = buffer.remaining();
        if ((readPointer + bufferLength) > contentLength) {
            bufferLength = (int) (contentLength - readPointer);
        }
        readPointer += (long) bufferLength;
        switch(parseType) {
            case TEXT:
            case JSON:
                parseText(buffer);
                break;
            case URL_ENCODED:
                parseUrlEncode(buffer);
                break;
            case UPLOAD:
                parseUpload(buffer);
                break;
            default:
                PoolManager.poolBufferInstance(buffer);
                break;
        }
    }

    public Map getParameterMap() {
        return parameter;
    }

    public List getParameters(String name) {
        Object param = parameter.get(name);
        if (param instanceof List) {
            return (List) param;
        }
        return null;
    }

    private void addParameter(String name, String value) {
        List values = getParameters(name);
        if (values == null) {
            values = new ArrayList();
            parameter.put(name, values);
        }
        values.add(value);
    }

    public String getParameter(String name) {
        List parameters = getParameters(name);
        if (parameters == null) {
            return null;
        }
        return (String) parameters.get(0);
    }

    public DiskFileItem getItem(String name) {
        Object param = parameter.get(name);
        if (param instanceof DiskFileItem) {
            return (DiskFileItem) param;
        }
        return null;
    }

    public Iterator getParameterNames() {
        return parameter.keySet().iterator();
    }

    public String getText() {
        if (parseType == ParseType.TEXT || parseType == ParseType.JSON) {
            return text;
        }
        return null;
    }

    public JSON getJsonObject() {
        if (parseType != ParseType.JSON) {
            return null;
        }
        return jsonObject;
    }

    public boolean isToBytes() {
        switch(parseType) {
            case UNSUPPORTED:
            case UPLOAD:
                return false;
        }
        return true;
    }

    private byte[] paramToBytes() throws UnsupportedEncodingException {
        Iterator itr = getParameterNames();
        StringBuilder sb = new StringBuilder();
        while (itr.hasNext()) {
            String name = (String) itr.next();
            List values = getParameters(name);
            String encName = URLEncoder.encode(name, encoding);
            for (int i = 0; i < values.size(); i++) {
                String value = (String) values.get(i);
                String encValue = URLEncoder.encode(value, encoding);
                sb.append(encName);
                sb.append("=");
                sb.append(encValue);
            }
        }
        return sb.toString().getBytes();
    }

    public byte[] toBytes() {
        switch(parseType) {
            case TEXT:
            case JSON:
                if (text != null) {
                    try {
                        return text.getBytes(encoding);
                    } catch (UnsupportedEncodingException e) {
                        logger.error("toBytes error.encoding:" + encoding, e);
                    }
                }
                break;
            case URL_ENCODED:
                try {
                    return paramToBytes();
                } catch (UnsupportedEncodingException e) {
                    logger.error("toBytes error.encoding:" + encoding, e);
                }
                break;
            case NONE:
            case UNSUPPORTED:
            case UPLOAD:
        }
        return null;
    }
}
