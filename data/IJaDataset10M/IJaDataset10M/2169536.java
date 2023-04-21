package com.developerlife.support;

/**
 * ConstantsIF contains various constants used in the clienthelper and EmailServlet impl of the mail
 * service. <br> Here are some links:<br> <ul style=square> <li><a href=http://www.utoronto.ca/webdocs/HTMLdocs/Book/Book-3ed/appb/mimetype.html>Listing
 * of MIME types</a></li> <li><a href=http://mgrand.home.mindspring.com/mime.html>MIME
 * Overview</a></li> <li><a href=http://en.wikipedia.org/wiki/MIME>MIME on Wikipedia</a></li> <li><a
 * href=http://www.w3schools.com/media/media_mimeref.asp>MIME Reference</a></li> </ul>
 *
 * @author Nazmul Idris
 * @since Dec 26, 2006, 4:07:30 PM
 */
public interface MIMETypeConstantsIF {

    public static String SEND_EMAIL = "send_email";

    public static String SEND_EMAIL_WITH_ATTACHMENT = "send_email_with_attachment";

    public static String MESSAGE_SENT_OK = "message_sent_ok";

    /** this is the key of the SMTP host stored in web.xml */
    public static final String MAIL_HOST = "mail.host";

    public static final String MAIL_PORT = "mail.port";

    public static final String MAIL_USER = "mail.user";

    public static final String MAIL_PSWD = "mail.pswd";

    public static String PLAIN_TEXT_TYPE = "text/plain";

    public static String HTML_TEXT_TYPE = "text/html";

    public static String CSS_TYPE = "text/css";

    public static String XML_TYPE = "text/xml";

    public static String ICS_TYPE = "text/calendar";

    public static String GIF_TYPE = "image/gif";

    public static String PNG_TYPE = "image/x-png";

    public static String JPEG_TYPE = "image/jpeg";

    public static String TIFF_TYPE = "image/tiff";

    public static String WINDOWS_BMP_TYPE = "image/x-ms-bmp";

    public static String MP3_AUDIO_TYPE = "audio/mpeg";

    public static String MPEG_VIDEO_TYPE = "video/mpeg";

    public static String PDF_TYPE = "application/pdf";

    public static String RTF_TYPE = "application/rtf";

    public static String MSWORD_TYPE = "application/msword";

    public static String ZIP_TYPE = "application/zip";

    public static String BINARY_TYPE = "application/octet-stream";
}
