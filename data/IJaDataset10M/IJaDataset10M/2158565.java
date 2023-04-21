package com.ericdaugherty.mail.server.services.smtp;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Each line of an outgoing message is parsed using rfc MIME rules.
 *
 * @author Andreas Kyrmegalos
 */
public class MIMESender extends MIMEConstants {

    /** Logger */
    private static final Log log = LogFactory.getLog(MIMESender.class);

    protected Locale locale = Locale.ENGLISH;

    protected boolean initialHeaders, readingHeaders, convertNextPart;

    protected List<String> boundaries;

    protected int mime;

    protected Base64OutputStream b64os;

    protected byte[] bufferForb64os;

    protected int emptyStringCount, currentRead, previousRead, currentTotal;

    private PrintWriter printOut;

    protected MIMESender() {
    }

    protected void setPrintOut(PrintWriter printOut) {
        this.printOut = printOut;
    }

    protected final void processDATA(byte[] output, boolean prependDot, OutputStream out) throws IOException {
        if (!(initialHeaders || readingHeaders || (output.length != 0 && (output[0] == 0x02D && output[1] == 0x02D)))) {
            if (!convertNextPart) {
                if (prependDot) {
                    out.write(new byte[] { 0x02e });
                }
                out.write(output);
                out.write(CRLF_BYTES);
                out.flush();
            } else {
                if (output.length == 0) {
                    emptyStringCount++;
                } else {
                    if (emptyStringCount > 0) {
                        b64os.flush();
                        for (int i = 0; i < emptyStringCount; i++) {
                            b64os.write(CRLF_BYTES);
                        }
                        emptyStringCount = 0;
                    }
                    if (prependDot) {
                        b64os.write(new byte[] { 0x02e });
                    }
                    if (previousRead > 0) {
                        b64os.write(bufferForb64os);
                        bufferForb64os = null;
                        b64os.write(CRLF_BYTES);
                        currentTotal = Math.min(output.length, 57 - previousRead);
                        b64os.write(output, 0, currentTotal);
                        if (output.length <= currentTotal) {
                            b64os.write(CRLF_BYTES);
                            currentTotal = previousRead = 0;
                            return;
                        }
                        previousRead = 0;
                    }
                    do {
                        currentRead = Math.min(57, output.length - currentTotal);
                        if (currentRead == 57) {
                            b64os.write(output, currentTotal, currentRead);
                            currentTotal += currentRead;
                        }
                    } while (currentRead == 57);
                    if (currentRead < 57) {
                        bufferForb64os = new byte[currentRead];
                        System.arraycopy(output, currentTotal, bufferForb64os, 0, currentRead);
                        previousRead = currentRead;
                    }
                    currentTotal = 0;
                }
            }
        } else {
            if (previousRead > 0) {
                b64os.write(bufferForb64os);
                for (int i = bufferForb64os.length - 1; i >= 0; i--) {
                    bufferForb64os[i] = 0;
                }
                bufferForb64os = null;
                b64os.write(CRLF_BYTES);
                previousRead = 0;
            }
            if (emptyStringCount > 0) {
                for (int i = 0; i < emptyStringCount - 1; i++) {
                    b64os.write(CRLF_BYTES);
                }
                emptyStringCount = 0;
                b64os.flush();
                b64os.close();
                out.write(CRLF_BYTES);
                out.flush();
            } else if (convertNextPart && !readingHeaders) {
                b64os.flush();
                b64os.close();
            }
            convertNextPart = false;
            String input = process(output);
            if (initialHeaders) {
                String capitalized = input.toUpperCase(locale);
                if (capitalized.startsWith(MIMECONTENT_TYPE) && mime == MIME_UNDEFINED) {
                    if (capitalized.indexOf(MIMEMULTIPART) != -1) {
                        mime = MIME_MULTIPART;
                        if (capitalized.indexOf(MIMEMULTIPART) != -1) {
                            if (input.lastIndexOf("\"") != -1) {
                                boundaries.add(input.substring(input.indexOf("\"") + 1, input.lastIndexOf("\"")));
                            } else {
                                boundaries.add(input.substring(input.lastIndexOf("=") + 1));
                            }
                        }
                    } else {
                        readingHeaders = true;
                        initialHeaders = false;
                    }
                } else if (mime == MIME_MULTIPART) {
                    String possibleBoundary = "";
                    try {
                        if (input.lastIndexOf("\"") != -1) {
                            possibleBoundary = input.substring(input.indexOf("\"") + 1, input.lastIndexOf("\""));
                        } else {
                            possibleBoundary = input.substring(input.lastIndexOf("=") + 1);
                        }
                    } catch (IndexOutOfBoundsException iobe) {
                    }
                    if (capitalized.indexOf(MIMEMULTIPART) != -1) {
                        boundaries.add(possibleBoundary);
                    }
                    if (possibleBoundary.length() > 0 && boundaries.contains(possibleBoundary)) {
                        readingHeaders = true;
                        initialHeaders = false;
                        mime = MIME_UNDEFINED;
                    }
                }
                write(input);
            } else if (readingHeaders) {
                String capitalized = input.toUpperCase(locale);
                if (capitalized.startsWith(MIMECONTENT_TYPE)) {
                    if (capitalized.indexOf(MIMEMULTIPART) != -1) {
                        mime = MIME_MULTIPART;
                    } else {
                        mime = MIME_UNDEFINED;
                    }
                    write(input);
                } else if (mime != MIME_MULTIPART) {
                    if (capitalized.startsWith(MIMECONTENT_TRANSFER_ENCODING) && capitalized.indexOf(MIME8BIT) != -1) {
                        convertNextPart = true;
                        b64os = new Base64OutputStream(out, true);
                    } else if (input.equals("")) {
                        readingHeaders = false;
                    }
                    write(input);
                } else if (input.equals("")) {
                    readingHeaders = false;
                    write(input);
                }
            } else {
                Iterator<String> iter = boundaries.iterator();
                while (iter.hasNext()) {
                    if (input.indexOf(iter.next()) != -1) {
                        if (convertNextPart) {
                            convertNextPart = false;
                        }
                        readingHeaders = true;
                        write(input);
                        return;
                    }
                }
            }
        }
    }

    /**
     * This method converts a sequence of bytes to a String
     *
     * @param input a sequence of bytes from the incoming stream
     * @return a string corresponding to a line of input from the stream encoded
     * in US-ASCII
     */
    private String process(byte[] input) {
        int length = input.length;
        if (length == 0) return "";
        try {
            return new String(input, 0, length, US_ASCII);
        } catch (UnsupportedEncodingException ex) {
            log.error(ex.getMessage());
            return new String(input, 0, length);
        }
    }

    /**
     * Writes the specified output message to the client.
     */
    private void write(String message) {
        if (message != null) {
            if (log.isDebugEnabled()) {
                log.debug("Writing: " + message);
            }
            printOut.print(message + CRLF_STRING);
            printOut.flush();
        }
    }

    protected static final String CRLF_STRING = "\r\n";

    protected static final byte[] CRLF_BYTES = new byte[] { 0x0d, 0x0a };

    protected static final String US_ASCII = "US-ASCII";
}
