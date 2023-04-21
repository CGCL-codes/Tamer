package net.sf.mailsomething.mail.parsers;

import net.sf.mailsomething.mail.*;
import net.sf.mailsomething.mail.util.Base64;
import net.sf.mailsomething.mail.util.QuotedPrintable;
import java.io.*;

/**
 * This is not finished. For usage see the Pop3ServerProtocol class.
 * 
 *  This is a simple version. Should include encoding of messagefield 
 * values, eg. when charset is different.
 *
 *@author     stig
 *@created    December 19, 2001
 */
public class MailEncoder {

    static byte[] CRLF = new byte[] { (byte) 13, (byte) 10 };

    static byte LF = (byte) 10;

    static byte CR = (byte) 13;

    static byte lineStartNoBreak = (byte) 32;

    /**
	 * Encodes the MimeTypeObject argument (a mail message) into bytes
	 * in format of rfc822, which is written to the OutputStream argument.
	 * 
	 * This doesnt writes the CRLF + . + CRLF ending to the stream.
	 * 
	 * @param message
	 * @param stream
	 * @return OutputStream
	 * @throws IOException
	 */
    public static OutputStream encodeMail(MimeTypeObject message, OutputStream stream) throws IOException {
        message.setField(RFC822.X.MAILER, "Mailsomething");
        encodeBody(message, stream, true);
        return stream;
    }

    /**
	 * 
	 * This writes a body into an outputstream. Can be used to encode
	 * a message, since it treats a message similar to a body. Ie, it
	 * takes all fields from the Message and writes them as a header. 
	 * It doesnt use any of the Model-methods in the message class,
	 * like - getSubject or similar, so if one wanna set a headerfield
	 * for a message one needs to be sure that it is set through setField()
	 * 
	 * 
	 * @param message the message to encode
	 * @param out The outputstream to write the message too
	 * @param top if the MimeTypeObject is a Message or a Bodypart
	 * @return OutputStream
	 * @throws IOException
	 */
    public static OutputStream encodeBody(MimeTypeObject message, OutputStream out, boolean top) throws IOException {
        byte lf = (byte) 10;
        byte cr = (byte) 13;
        byte[] bytes;
        String boundary = "";
        Object[] fieldNames = message.getFieldNames();
        String headerField;
        for (int i = 0; i < fieldNames.length; i++) {
            if (!top && fieldNames[i].equals(RFC822.MESSAGE_ID) || fieldNames[i].equals(RFC822.CONTENT_TYPE)) continue;
            try {
                headerField = (String) fieldNames[i] + ": " + message.getField((String) fieldNames[i]);
            } catch (ClassCastException f) {
                continue;
            }
            try {
                bytes = headerField.getBytes("ASCII");
                out.write(bytes, 0, bytes.length);
            } catch (UnsupportedEncodingException f) {
            }
            out.write(CRLF, 0, CRLF.length);
        }
        if (message.getMimeType() != null) {
            try {
                String ctype = getContentTypeBytes(message.getMimeType());
                if (message.getMimeType().getPrimaryType().equalsIgnoreCase("multipart")) {
                    if (message.getMimeType().getParameter("boundary") == null) {
                        boundary = "----=_NextPart_" + MailUtils.getUniqeID();
                        boundary += "";
                        ctype += ";";
                        ctype += new String(CRLF);
                        ctype += " ";
                        ctype += "boundary=" + '"';
                        ctype += boundary;
                        ctype += '"';
                        boundary = "--" + boundary;
                    } else {
                        boundary = "--" + message.getMimeType().getParameter("boundary");
                    }
                }
                bytes = ctype.getBytes("ASCII");
                out.write(bytes, 0, bytes.length);
                out.write(CRLF, 0, CRLF.length);
            } catch (UnsupportedEncodingException f) {
            }
        } else {
            if (message.getField(RFC822.CONTENT_TYPE) != null) {
                headerField = null;
                try {
                    headerField = RFC822.CONTENT_TYPE + ": " + message.getField(RFC822.CONTENT_TYPE);
                } catch (ClassCastException f) {
                }
                if (headerField != null) {
                    try {
                        bytes = headerField.getBytes("ASCII");
                        out.write(bytes, 0, bytes.length);
                    } catch (UnsupportedEncodingException f) {
                    }
                    out.write(CRLF, 0, CRLF.length);
                }
            }
        }
        out.write(CRLF, 0, CRLF.length);
        String contentType = message.getField(RFC822.CONTENT_TYPE);
        if (contentType == null) {
            encodeUnknownPart(message, out);
        } else if (contentType.startsWith("text")) {
            encodeTextPart(message, out, -1);
        } else if (contentType.startsWith("multipart")) {
            for (int i = 0; i < message.getBodyCount(); i++) {
                out.write(boundary.getBytes(), 0, boundary.getBytes().length);
                out.write(CRLF, 0, CRLF.length);
                encodeBody(message.getBody(i), out, false);
                out.write(CRLF, 0, CRLF.length);
            }
            out.write(boundary.getBytes(), 0, boundary.getBytes().length);
            out.write(CRLF, 0, CRLF.length);
        } else {
            encodeUnknownPart(message, out);
        }
        out.write(CRLF, 0, CRLF.length);
        return out;
    }

    /**
	 * @param mimeType
	 * @return
	 */
    public static String getContentTypeBytes(MimeType mimeType) {
        if (mimeType == null) return "Content-Type: text/plain";
        String contentType = "Content-Type: ";
        contentType += mimeType.toString();
        return contentType;
    }

    /**
	 * Im not sure of the idea of this. 
	 * 
	 * @param message
	 * @param out
	 * @return OutputStream
	 * @throws IOException
	 */
    public static OutputStream encodeMultiPart(MimeTypeObject message, OutputStream out) throws IOException {
        return out;
    }

    public static OutputStream encodeTextPart(MimeTypeObject message, OutputStream out) throws IOException {
        String contentType = message.getField(RFC822.CONTENT_TYPE);
        boolean isHtml = false;
        if (contentType != null) {
            if (contentType.indexOf("html") != -1) isHtml = true;
        }
        InputStream stream = message.getInputStream();
        int streamLength = stream.available();
        if (streamLength == 0) return out;
        byte[] body = new byte[streamLength];
        stream.read(body, 0, streamLength);
        if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING) != null) {
            if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.QP)) {
                QuotedPrintable.encode(body, out);
                return out;
            } else if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.BASE64)) {
                body = com.permabit.util.mime.Base64.encode(body);
                int j = 0;
                for (int i = 0; i < (body.length / 69); i++) {
                    out.write(body, i * 69, 69);
                    out.write(CRLF);
                    j = i;
                }
                j++;
                if (body.length - j * 69 > 0) out.write(body, j * 69, body.length - j * 69);
                return out;
            }
        }
        int k = 0;
        int notAddedBytes = 0;
        for (int i = 0; i < (body.length / 69) + 1; i++) {
            if (isHtml) {
                boolean lineBreak = false;
                if (body[0] == LF) body[0] = (byte) 32;
                for (int j = i * 69; j < (i + 1) * 69; j++) {
                    if (j >= body.length) continue;
                    if (body[j] == LF && body[j - 1] != CR) {
                        out.write((byte) 32);
                    } else if (body[j] == LF && body[j - 1] == CR) {
                        lineBreak = true;
                        out.write(body[j]);
                    } else {
                        out.write(body[j]);
                    }
                }
                if (lineBreak = false) {
                    out.write(CRLF, 0, CRLF.length);
                } else {
                    notAddedBytes = +2;
                }
            } else {
                boolean lineBreak = false;
                if (body[0] == LF) body[0] = (byte) 32;
                for (int j = i * 69; j < (i + 1) * 69; j++) {
                    if (j >= body.length) continue;
                    if (body[j] == LF && body[j - 1] != CR) {
                        out.write(CR);
                        out.write(LF);
                        lineBreak = true;
                    } else {
                        out.write(body[j]);
                    }
                }
                if (lineBreak = false) {
                    out.write(CRLF, 0, CRLF.length);
                } else {
                    notAddedBytes += 2;
                }
            }
            k = i + 1;
        }
        return out;
    }

    public static OutputStream encodeUnknownPart(MimeTypeObject message, OutputStream out) throws IOException {
        InputStream stream = message.getInputStream();
        if (stream == null) {
            String describ = "This message doesnt have a body. Possible reason " + "is that the message have been filtered because of size is to big, or " + "other filters.";
            out.write(describ.getBytes());
            return out;
        }
        int streamLength = stream.available();
        if (streamLength == 0) return out;
        byte[] body = new byte[streamLength];
        stream.read(body, 0, streamLength);
        if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING) != null) {
            if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.QP)) {
                QuotedPrintable.encode(body, out);
                return out;
            } else if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.BASE64)) {
                body = com.permabit.util.mime.Base64.encode(body);
                int j = 0;
                for (int i = 0; i < (body.length / 69); i++) {
                    out.write(body, i * 69, 69);
                    out.write(CRLF);
                    j = i;
                }
                j++;
                out.write(body, j * 69, body.length - j * 69);
                return out;
            }
        }
        int k = 0;
        int notAddedBytes = 0;
        for (int i = 0; i < (body.length / 69) + 1; i++) {
            boolean lineBreak = false;
            if (body[0] == LF) body[0] = (byte) 32;
            for (int j = i * 69; j < (i + 1) * 69; j++) {
                if (j >= body.length) continue;
                if (body[j] == LF && body[j - 1] != CR) {
                    out.write(CR);
                    out.write(LF);
                    lineBreak = true;
                } else {
                    out.write(body[j]);
                }
            }
            if (lineBreak = false) {
                out.write(CRLF, 0, CRLF.length);
            } else {
                notAddedBytes += 2;
            }
            k = i + 1;
        }
        return out;
    }

    /**
	 * 
	 * This writes the header of the message to the outputstream.
	 * NOTE: this method is primarely included to support the optional
	 * TOP command when acting as a MailServer. In general dont use
	 * this when sending mails.
	 * 
	 * @param message
	 * @param out
	 * @return OutputStream
	 * @throws IOException
	 */
    public static OutputStream encodeHeader(Message message, OutputStream out) throws IOException {
        byte lf = (byte) 10;
        byte cr = (byte) 13;
        byte[] bytes;
        String boundary = "";
        Object[] fieldNames = message.getFieldNames();
        String headerField;
        for (int i = 0; i < fieldNames.length; i++) {
            try {
                headerField = (String) fieldNames[i] + ": " + message.getField((String) fieldNames[i]);
            } catch (ClassCastException f) {
                continue;
            }
            try {
                bytes = headerField.getBytes("ASCII");
                out.write(bytes, 0, bytes.length);
            } catch (UnsupportedEncodingException f) {
            }
            out.write(CRLF, 0, CRLF.length);
        }
        try {
            if (message.getMimeType() != null) {
                String ctype = getContentTypeBytes(message.getMimeType());
                if (message.getMimeType().getPrimaryType().equalsIgnoreCase("multipart")) {
                    if (message.getMimeType().getParameter("boundary") == null) {
                        boundary = "----=_NextPart_" + MailUtils.getUniqeID();
                        ctype += '\n';
                        ctype += "boundary=" + '"';
                        ctype += boundary;
                        ctype += '"';
                    } else {
                        boundary = "--" + message.getMimeType().getParameter("boundary");
                    }
                }
                bytes = ctype.getBytes("ASCII");
                out.write(bytes, 0, bytes.length);
                out.write(CRLF, 0, CRLF.length);
            }
        } catch (UnsupportedEncodingException f) {
        }
        out.write(CRLF, 0, CRLF.length);
        return out;
    }

    /**
	 * 
	 * Method for writing a specific number of lines of the encoded message
	 * into an output stream. This is server specific, to support the top
	 * command. This should probably be done differently, like doing a
	 * custom outputstream which counts the lines written and then breaks
	 * when the desired number is hit. Only problem with that is, that
	 * we also want the encoding to stop when thats encountered... perhabs
	 * by adding a method for stopping it. 
	 * 
	 * @param message
	 * @param out
	 * @return OutputStream
	 * @throws IOException
	 */
    public static int encodeBody(MimeTypeObject message, OutputStream out, int lines) throws IOException {
        byte lf = (byte) 10;
        byte cr = (byte) 13;
        byte[] bytes;
        String boundary = "";
        Object[] fieldNames = message.getFieldNames();
        String headerField;
        if (message.getMimeType() != null) {
            String ctype = getContentTypeBytes(message.getMimeType());
            if (message.getMimeType().getPrimaryType().equalsIgnoreCase("multipart")) {
                if (message.getMimeType().getParameter("boundary") == null) {
                    boundary = "----=_NextPart_" + MailUtils.getUniqeID();
                    ctype += '\n';
                    ctype += "boundary=" + '"';
                    ctype += boundary;
                    ctype += '"';
                } else {
                    boundary = "--" + message.getMimeType().getParameter("boundary");
                }
            }
        }
        int lineswritten = 0;
        String contentType = message.getField(RFC822.CONTENT_TYPE);
        if (contentType == null) {
            return encodeUnknownPart(message, out, lines);
        } else if (contentType.startsWith("text")) {
            return encodeTextPart(message, out, lines);
        } else if (contentType.startsWith("multipart")) {
            for (int i = 0; i < message.getBodyCount(); i++) {
                out.write(boundary.getBytes(), 0, boundary.getBytes().length);
                out.write(CRLF, 0, CRLF.length);
                lineswritten++;
                if (lineswritten >= lines) return lineswritten;
                lineswritten += encodeBody(message.getBody(i), out, lines - lineswritten);
                if (lineswritten >= lines) return lineswritten;
                out.write(CRLF, 0, CRLF.length);
                lineswritten++;
                if (lineswritten >= lines) return lineswritten;
            }
        } else {
            encodeUnknownPart(message, out);
        }
        out.write(CRLF, 0, CRLF.length);
        return lineswritten;
    }

    /**
	 * Similar to encodeTextPart excepts this stops if argument lines number
	 * is written to the outputstream, ie, this method is for supporting
	 * the optional pop3 TOP command IN THE SERVER.
	 * @param message
	 * @param out
	 * @param lines
	 * @return int
	 * @throws IOException
	 */
    public static int encodeTextPart(MimeTypeObject message, OutputStream out, int lines) throws IOException {
        String contentType = message.getField(RFC822.CONTENT_TYPE);
        boolean isHtml = false;
        if (contentType != null) {
            if (contentType.indexOf("html") != -1) isHtml = true;
        }
        InputStream stream = message.getInputStream();
        int streamLength = stream.available();
        if (streamLength == 0) return 0;
        byte[] body = new byte[streamLength];
        stream.read(body, 0, streamLength);
        int lineswritten = 0;
        if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING) != null) {
            if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.QP)) {
                QuotedPrintable.encode(body, out);
                return body.length / 69;
            } else if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.BASE64)) {
                body = com.permabit.util.mime.Base64.encode(body);
                int j = 0;
                for (int i = 0; i < (body.length / 69); i++) {
                    out.write(body, i * 69, 69);
                    out.write(CRLF);
                    j = i;
                    if (lines != -1) {
                        lineswritten++;
                        if (lineswritten == lines) {
                            return lineswritten;
                        }
                    }
                }
                j++;
                if (body.length - j * 69 > 0) out.write(body, j * 69, body.length - j * 69);
                lineswritten++;
                return lineswritten;
            }
        }
        int k = 0;
        int notAddedBytes = 0;
        for (int i = 0; i < (body.length / 69) + 1; i++) {
            if (isHtml) {
                boolean lineBreak = false;
                if (body[0] == LF) body[0] = (byte) 32;
                for (int j = i * 69; j < (i + 1) * 69; j++) {
                    if (j >= body.length) continue;
                    if (body[j] == LF && body[j - 1] != CR) {
                        out.write((byte) 32);
                    } else if (body[j] == LF && body[j - 1] == CR) {
                        lineBreak = true;
                        out.write(body[j]);
                    } else {
                        out.write(body[j]);
                    }
                }
                if (lineBreak = false) {
                    out.write(CRLF, 0, CRLF.length);
                    if (lines != -1) {
                        lineswritten++;
                        if (lineswritten == lines) {
                            return lineswritten;
                        }
                    }
                } else {
                    notAddedBytes = +2;
                }
            } else {
                boolean lineBreak = false;
                if (body[0] == LF) body[0] = (byte) 32;
                for (int j = i * 69; j < (i + 1) * 69; j++) {
                    if (j >= body.length) continue;
                    if (body[j] == LF && body[j - 1] != CR) {
                        out.write(CR);
                        out.write(LF);
                        lineBreak = true;
                    } else {
                        out.write(body[j]);
                    }
                }
                if (lineBreak = false) {
                    out.write(CRLF, 0, CRLF.length);
                    if (lines != -1) {
                        lineswritten++;
                        if (lineswritten == lines) {
                            return lineswritten;
                        }
                    }
                } else {
                    notAddedBytes += 2;
                }
            }
            k = i + 1;
        }
        return lineswritten;
    }

    /**
	 * Similar to encodeUnknown excepts this stops if argument lines number
	 * is written to the outputstream, ie, this method is for supporting
	 * the optional pop3 TOP command IN THE SERVER.
	 * 
	 * @param message
	 * @param out
	 * @param lines
	 * @return int
	 * @throws IOException
	 */
    public static int encodeUnknownPart(MimeTypeObject message, OutputStream out, int lines) throws IOException {
        InputStream stream = message.getInputStream();
        if (stream == null) {
            String describ = "This message doesnt have a body. Possible reason " + "is that the message have been filtered because of size is to big, or " + "other filters.";
            out.write(describ.getBytes());
            return 3;
        }
        int streamLength = stream.available();
        if (streamLength == 0) return 0;
        byte[] body = new byte[streamLength];
        stream.read(body, 0, streamLength);
        int lineswritten = 0;
        if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING) != null) {
            if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.QP)) {
                QuotedPrintable.encode(body, out);
                return body.length / 69;
            } else if (message.getField(RFC822.CONTENT_TRANSFER_ENCODING).equalsIgnoreCase(MailDecoder.BASE64)) {
                body = com.permabit.util.mime.Base64.encode(body);
                int j = 0;
                for (int i = 0; i < (body.length / 69); i++) {
                    out.write(body, i * 69, 69);
                    out.write(CRLF);
                    j = i;
                    if (lines != -1) {
                        lineswritten++;
                        if (lineswritten == lines) {
                            return lineswritten;
                        }
                    }
                }
                j++;
                out.write(body, j * 69, body.length - j * 69);
                lineswritten++;
                return lineswritten;
            }
        }
        int k = 0;
        int notAddedBytes = 0;
        for (int i = 0; i < (body.length / 69) + 1; i++) {
            boolean lineBreak = false;
            if (body[0] == LF) body[0] = (byte) 32;
            for (int j = i * 69; j < (i + 1) * 69; j++) {
                if (j >= body.length) continue;
                if (body[j] == LF && body[j - 1] != CR) {
                    out.write(CR);
                    out.write(LF);
                    lineBreak = true;
                    if (lines != -1) {
                        lineswritten++;
                        if (lineswritten == lines) {
                            return lineswritten;
                        }
                    }
                } else {
                    out.write(body[j]);
                }
            }
            if (lineBreak = false) {
                out.write(CRLF, 0, CRLF.length);
                if (lines != -1) {
                    lineswritten++;
                    if (lineswritten == lines) {
                        return lineswritten;
                    }
                }
            } else {
                notAddedBytes += 2;
            }
            k = i + 1;
        }
        return lineswritten;
    }

    /**
	 * Method to search for a specific contenttype within a message. 
	 * I placed this in this class, not sure if theres a better place.
	 * I dont wanna have it in the message class (though the message)
	 * class uses it, for example in hasAttachments method.
	 * 
	 * @param contentType
	 * @param o
	 * @return boolean
	 */
    public static boolean findContentType(String contentType, MimeTypeObject o) {
        if (o == null) return false;
        if (o.getBodyCount() > 1) {
            MimeType mime = o.getMimeType();
            String cctype = mime.getPrimaryType() + "/" + mime.getSubType();
            if (cctype.indexOf(contentType) != -1) return true;
            MimeTypeObject object;
            int index = 0;
            while ((object = o.getBody(index++)) != null) {
                MimeType mimetype = object.getMimeType();
                if (mimetype != null) {
                    String ctype = mimetype.getPrimaryType() + "/" + mimetype.getSubType();
                    if (ctype.indexOf(contentType) != -1) return true; else if (ctype.startsWith("multipart")) if (findContentType(contentType, object)) return true;
                }
            }
        } else {
            MimeType mimetype = o.getMimeType();
            if (mimetype != null) {
                String ctype = mimetype.getPrimaryType() + "/" + mimetype.getSubType();
                if (ctype.indexOf(contentType) != -1) return true; else if (ctype.startsWith("multipart")) if (findContentType(contentType, o.getBody(0))) return true;
            }
        }
        return false;
    }

    /**
	 * 
	 * Method to write a headerline to a stream. Im not to crazy about
	 * this, instead it should be a class which buffers the string and
	 * bytes and stream. 
	 * 
	 * @param key
	 * @param value
	 * @param out
	 * @throws IOException
	 */
    public static void writeHeaderLine(String key, String value, OutputStream out) throws IOException {
        String headerField = key + ": " + value;
        byte[] bytes;
        try {
            bytes = headerField.getBytes("ASCII");
            out.write(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException f) {
        }
        out.write(CRLF, 0, CRLF.length);
    }

    /**
	 * Encode field as described in rfc 2047
	 *
	 * @param field
	 * @return something like =?iso-8859-1?b?YWxsIHRoYXQ=?=
	 * @throws UnsupportedEncodingException
	 * @author bogdan
	 */
    public static String encodeField(String field) throws UnsupportedEncodingException {
        String encField = null;
        String javaCharset = System.getProperty("file.encoding", "ISO-8859-1");
        String charset = System.getProperty("mail.mime.charset", javaCharset);
        byte byteField[] = null;
        int i = 0, j = 0;
        byteField = field.getBytes(charset);
        for (int k = 0; k < byteField.length; k++) {
            int symbol = byteField[k] & 0xff;
            if (symbol >= 127 || symbol < 32 && symbol != 13 && symbol != 10 && symbol != 9) {
                j++;
                byteField[k] = (byte) symbol;
                int y = 0;
            } else i++;
        }
        if (j == 0) return field; else if (i > j) {
            StringBuffer stringbuffer = new StringBuffer();
            encode(field, charset, "Q", false, stringbuffer);
            return stringbuffer.toString();
        } else if (i <= j) {
            StringBuffer stringbuffer = new StringBuffer();
            encode(field, charset, "B", false, stringbuffer);
            return stringbuffer.toString();
        }
        return encField;
    }

    /**
	 *
	 * @param field
	 * @param charset
	 * @param encoding
	 * @param last
	 * @param stringbuffer
	 * @throws UnsupportedEncodingException
	 * @author bogdan
	 */
    private static void encode(String field, String charset, String encoding, boolean last, StringBuffer stringbuffer) throws UnsupportedEncodingException {
        byte fieldInByte[] = field.getBytes(charset);
        int encodedLength;
        if (encoding.equalsIgnoreCase("B")) encodedLength = encodedLengthBase64(fieldInByte); else encodedLength = encodedLengthQuoted(fieldInByte);
        if ((encodedLength > 68 - charset.length()) && (field.length() > 1)) {
            encode(field.substring(0, field.length() / 2), charset, encoding, last, stringbuffer);
            encode(field.substring(field.length() / 2), charset, encoding, true, stringbuffer);
            return;
        }
        if (last) stringbuffer.append("\r\n ");
        stringbuffer.append("=?".concat(charset).concat("?").concat(encoding).concat("?"));
        if (encoding.equalsIgnoreCase("B")) {
            byte[] byteField = Base64.encode(fieldInByte);
            for (int l = 0; l < byteField.length; l++) stringbuffer.append((char) byteField[l]);
        } else if (encoding.equalsIgnoreCase("Q")) {
            byte[] byteField = QuotedPrintable.encode(fieldInByte, true);
            for (int l = 0; l < byteField.length; l++) stringbuffer.append((char) byteField[l]);
        }
        stringbuffer.append("?=");
    }

    /**
	  * CALCULATES LENGTH OF THE CODED byte[]
	  * @param abyte0
	  * @return
	  * @author bogdan
	  */
    private static int encodedLengthBase64(byte abyte0[]) {
        return ((abyte0.length + 2) / 3) * 4;
    }

    /**
	  * CALCULATES LENGTH OF THE CODED byte[]
	  * @param in
	  * @return
	  * @author bogdan
	  */
    private static int encodedLengthQuoted(byte in[]) {
        int length = 0;
        for (int i = 0; i < in.length; i++) {
            if (((in[i] >= 65) && (in[i] <= 90)) || ((in[i] >= 97) && (in[i] <= 122)) || ((in[i] >= 48) && (in[i] <= 57)) || (in[i] == '!') || (in[i] == '*') || (in[i] == '+') || (in[i] == '-') || (in[i] == '/') || (in[i] == ' ') || (in[i] == 0x20)) {
                length++;
            } else length += 3;
        }
        return length;
    }
}
