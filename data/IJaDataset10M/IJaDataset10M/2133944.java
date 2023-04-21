package org.webdocwf.util.smime.smime;

import org.webdocwf.util.smime.exception.SMIMEException;
import org.webdocwf.util.smime.exception.ErrorStorage;
import org.webdocwf.util.smime.activation.CMSSignedDataSource;
import org.webdocwf.util.smime.mail.MultipartGenerator;
import org.webdocwf.util.smime.util.ConvertAssist;
import org.webdocwf.util.smime.activation.StreamDataSource;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.HeadersUtil;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetAddress;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import java.util.Vector;
import java.util.Properties;
import java.util.SimpleTimeZone;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.security.Security;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;
import sun.security.provider.Sun;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * SignedSMIME class is used for creating and sending signed S/MIME message.<BR>
 * <BR>
 * Email message is in general composed of the content of the message and of one or
 * more attachments. The content is visible part of the message, and attacments are
 * mostly files or other binary data, which are not visible parts of message and
 * which are used by email as a transport medium. In this implementation content
 * can be represented in two different forms: <BR>
 * <BR>
 * <UL><LI>
 * text/plain (only text withouth any formating) or
 * </LI> <LI>
 * text/html (html coded view of message)
 * </LI></UL>
 * Also, content can be absent, but than at least one attachment must be added.
 * Content can be set on few manners. For text/plain type it can be done in time
 * of construction with constructor designed special for creation of text/plain
 * messages. Also, text content can be created by any of setContent() methods,
 * if construction of object was done by other constructor which create object
 * with empty content. Construction with other constructor offers a few different
 * posibilities for importing content data (File, InputStream, String) by using
 * appropriate setContent() method. If method with four parameters is used, 3rd
 * ant 4th parameters are not in use for text/plain message and could be set
 * null. For setting text/html content, construction of object should be done
 * only by second mentioned constructor, which creates object with empty content.
 * Content should be populated by html code with setContent() method. 3rd
 * parameter is used for resolving relative addresses of resources in html
 * code (images, movies...) and 4th parameter serves as data source for resources
 * that are on special way addressed in html code. Also, there is a setContent()
 * method which doesn't care about resources and which creates message content
 * withouth them. For more information refer to setContent() methods.<BR>
 * <BR>
 * Message can contain any number of attachments. Also, message can
 * be wihouth any attachment, but then content must be present. Every attachment
 * should be added by performing single addAttachment() method. Attachments
 * can be added from file or from InputStream. Mime-type which corresponds to
 * particular attachment is obtained according to extension of file name
 * (virtual or real file name) passed to addAttachment() method. File mime.types
 * in META_INF directory contains list of mime-types and corresponding extensions
 * which are used in determination of mime-type. File can be changed to satisfy
 * secific requrements. For more information refer to addAttachmenttent()
 * method.<BR>
 * <BR>
 * Message can be external (explicit) or internal (implicit) signed. External
 * signing allows email receiving clients wihouth implemented SMIME
 * capabilities to preview the signed SMIME email messages.<BR>
 * <BR>
 * Message can be signed with or without Signed Attributes. Signed Attributes
 * are one optional part of CMS (Cryptographic Message Syntax) signed objects,
 * and consist of some atributes used in the process of signing (date and time
 * of signing, capabilities of sending email client, message digest value...).
 * If those attributes are ommited, only pure message is taken in the process
 * of signing.<BR>
 * <BR>
 * Digest algorithm can be SHA1, MD2 or MD5 which depends on selected signing algorithm.<BR>
 * <BR>
 * Capabilities Attributes are one of Signed Attributes, and in the process of
 * signing (if Signed Attributes are involved) can be set. This attributes
 * indicate to recipient email client which encipher, symmetric and/or signature
 * algorithms signer's email client preferes, and they can be used in the next
 * communication between each others. Setting this posibilities is optional, but
 * if it is set, order of adding gives the information about most preferes algorithms
 * within paricular group of algorithms. Defined Capabilities Attributes in this version
 * of Signed SMIME can be from group: RC2 40, RC2 64, RC2 128, DES and DES_EDE3 for
 * symmetric encryption algorihms, from group: MD2 with RSA, MD5 with RSA, SHA1 with RSA
 * and SHA1 with DSA for signing algorithms, and RSA for encipher algorithm. For more
 * information see setCapabilities method in this class.<BR>
 * <BR>
 * Certificates of signers and their root authorities can be included in the
 * signed message. This posibilities allow the recipient of signed SMIME
 * message to automatically include signer's certificates as trusted, and verify
 * signed message. This posibilities are optional.<BR>
 * <BR>
 * More than one signer can perform signing of message and they can use
 * different signing algorithms. Digital signing can be performed by SHA1_WITH_RSA,
 * MD2_WITH_RSA, MD5_WITH_RSA or SHA1_WITH_DSA.<BR>
 * <BR>
 */
public class SignedSMIME {

    /**
     * Container for MIME message
     */
    private MimeMessage message;

    /**
     * Storage for .pfx files corresponding to appropriate signing session (used
     * for first type of addSigner function).
     */
    private Vector ksArray = new Vector(0, 1);

    /**
     * Storage for digest algorithm corresponding to appropriate signing session
     * (used for first type of addSigner function).
     */
    private Vector digestArray = new Vector(0, 1);

    /**
     * Storage for byte[2] grouped indicators (used for first type of addSigner
     * function).
     */
    private Vector including = new Vector(0, 1);

    /**
     * Storage for certificate chain corresponding to appropriate signing session
     * (used for second type of addSigner function)
     */
    private Vector certChainArray = new Vector(0, 1);

    /**
     * Storage for private key corresponding to appropriate signing session (used
     * for second type of addSigner function)
     */
    private Vector privKeyArray = new Vector(0, 1);

    /**
     * Storage for digest algorithm corresponding to appropriate signing session
     * (used for second type of addSigner function)
     */
    private Vector digestArray2 = new Vector(0, 1);

    /**
     * Storage for byte[2] grouped indicators (used for second type of addSigner
     * function)
     */
    private Vector including2 = new Vector(0, 1);

    /**
     * Storage for MIME bodyparts
     */
    private Vector bodyPartArray = new Vector(0, 1);

    /**
     * Storage for additional certificates
     */
    private Vector aditionalCerts = new Vector(0, 1);

    /**
     * Temporary storage for capabilities (after method addSigner, this object is
     * copied to capabilities or capabilities2)
     */
    private Vector capabilitiesTemp = new Vector(0, 1);

    /**
     * Storage for capabilities (used for first type of addSigner function)
     */
    private Vector capabilities = new Vector(0, 1);

    /**
     * Storage for capabilities (used for second type of addSigner function)
     */
    private Vector capabilities2 = new Vector(0, 1);

    /**
     * Indication that at least one recipient must be TO (others may be CC or BCC)
     */
    private boolean indicatorTo = false;

    /**
     * Indicator of the presence of plain text content
     */
    private boolean textContentPresence = false;

    /**
     * Initializes the JavaMail session for SMTP and MimeMessage for signing.
     * Dynamically loads the BC and SUN provider necessary for encryption. This
     * constructor is used for creating message with text/plain content. For creating
     * html formated content (text/html) other constructor should be used in
     * combination with one of the setContent methods. Note that after using this
     * constructor setContent method can be used only if "content" argument of
     * constructor was given as null, otherwise setContent method can't be used
     * because content is already set as text/plain.
     * @param smtpHost name of SMTP host used for sending email
     * @param fromAddress email address of sender (FROM field in email header)
     * @param subject subject of email (SUBJECT field in email header)
     * @param content text/plain content of email message
     * @exception SMIMEException if smtpHost or fromAddress parameters are null.
     * Also, it can be caused by non SMIMEException which is MessagingException.
     */
    public SignedSMIME(String smtpHost, String fromAddress, String subject, String content) throws SMIMEException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Security.addProvider(new Sun());
            if (smtpHost == null | fromAddress == null) throw new SMIMEException(this, 1041);
            Properties sesProp = new Properties();
            sesProp.setProperty("mail.smtp.host", smtpHost);
            Session ses = Session.getInstance(sesProp);
            message = new MimeMessage(ses);
            InternetAddress from = new InternetAddress(fromAddress);
            message.setFrom(from);
            if (subject != null) message.setSubject(subject);
            if (content != null) {
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.setText(content);
                bodyPartArray.addElement(mbp);
                textContentPresence = true;
            }
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "constructor");
        }
    }

    /**
     * Initializes the JavaMail session for SMTP and MimeMessage for signing.
     * Dynamically loads the BC and SUN provider necessary for encryption. This
     * constructor does not create content of message and it can be set later with
     * one of setContent methods. Also, message can be left withouth content, but
     * then at least one attachement must be added.
     * @param smtpHost name of SMTP host used for sending email
     * @param fromAddress email address of sender (FROM field in email header)
     * @param subject subject of email (SUBJECT field in email header)
     * @exception SMIMEException if smtpHost or fromAddress parameters are null.
     * Also, it can be caused by non SMIMEException which is MessagingException.
     */
    public SignedSMIME(String smtpHost, String fromAddress, String subject) throws SMIMEException {
        this(smtpHost, fromAddress, subject, null);
    }

    /**
     * Sets message content. Message content can be given in two differrent forms:
     * text and html code. If content is type of text, parameter "type" should be
     * "text/plain" and other two parameters are not in use (should be set null).
     * If content is type of html code, parameter "type" should be set as "text/html",
     * otherwise (if it is set as "text/plain") html code is processed as a plain
     * text. This method can be performed only once.<BR>
     * <BR>
     * In case of html content, it is essential to (on appropriate way) associate some
     * attributes of particular elements in html code ("background" or "src" atributes)
     * with corresponding ressources (URL-s, relative file addresses or byte array
     * streams). This resources should all be sent with message to enable recipient
     * to see complete html message. Location of resources can be given in few
     * different forms and depending on that, allocation resolving can be successful or
     * not. Following text represents different possibilities for defining locations
     * of resources (pictures, animations, sound...) inside of html code passed to
     * this method, and necessery passed additional parameters used for resolving
     * this resource locations.<BR>
     * <BR>
     * <UL>
     * <LI>URL defined as: http://... is left unchanged. This resource is not sent
     * with the message and it couldn't be seen by recipient if it is not online on
     * the internet.</LI>
     * <LI>URL defined as: file://... is transformed to corresponding Content ID if
     * the resource can be found on specified location and it is sent with message.</LI>
     * <LI>Absolute path, for example defined as: "c:\tmp\test\picture.bmp", is
     * transformed to corresponding Content ID if the resource can be found on
     * specified location, and is sent with message. If all resources in html
     * code are specified with its absolute path, the 3rd parameter in this method
     * can be null.</LI>
     * <LI>Relative path of all resources specified in html code, for example
     * defined as: ".\test\picture.bmp" and ".\example\flush.swf", must be defined
     * to be relative to same directory path (in this case it is c:\tmp). This parameter
     * (common directory path) is given as 3rd parameter in this method, and is named
     * "path". If html code is obtained from .html file, necessery common directory
     * path is usually path to this .html file. Location of resource is transformed
     * to corresponding Content ID if the resource can be found on specified location.
     * This location is sent with the message.</LI>
     * <LI>Byte array stream as resource for html attribute must be referenced from
     * html code as: <BR>
     * <BR><PRE>
     * *****nnn&lt;virtual_file_name&gt;<BR>
     * <BR></PRE>
     * Five '*' characters (must be five) define that it is resource expected from
     * the stream. Other three characters must be digits (000-999) and represent
     * index of corresponding stream in stream array. virtual_file_name is name and
     * extension assigned to data passed from stream. Name is used in construction of
     * "name" parameter in Content-Type, while extension of file name is used in
     * detection of appropriate mime-type. Lenght of virtual_file_name is not
     * important. If there is no data referenced from byte array stream ,4th
     * parameter of this method named "resources" can be null. Also, if all resources
     * are passed through the array of streams, 3th parameter ("path") can be null.
     * Location of resource is transformed to corresponding Content ID if no error
     * has occured during the process of allocation.</LI>
     * </UL>
     * <BR>
     * All mentioned resource allocation types can be combined together in the same
     * html code, and all will be processed with appropriate use of this method.<BR>
     * <BR>
     * Note that number of resource references that are defined in html code by
     * using virtual_file_names must be greater than or equal to number of elements
     * in array of InputStream (4th parameter). If one resource (one element in array
     * of IputStream) is used in html code more than once, it is advisable to use
     * same virtual_file_name in html code because message is then sent with only
     * one attached resource (image, movie...). It is essetntial that desired resource
     * in input stream array corresponds to specified "nnn" part of virtual_file_name.<BR>
     * <BR>
     * If resources specified on any described name can not be found or resolved,
     * or if any exception has occured during its processing, they won't be added and
     * html message will be sent withouth them.
     * @param content String representation of message content (text or html code).
     * @param type type of given content. It can take values: "text/plain" or
     * "text/html".
     * @param path common directory path for relative file locations in html code.
     * It can be null if all resources set absolute path or are defined by
     * byte array streams, or if sending resources with relative address it is not desired.
     * Also, it is set to null in case of text/plain message.
     * @param resources way for representing resources used in the given html code
     * which will be added to message as array of InputStream. Detail use
     * of this argument is described above. It can be null if no resources as byte
     * array stream are used, or if sending resources given in that way is not desired.
     * Also, it is set to null in case of text/plain message.
     * @exception SMIMEException if content is tried to be added twice, or in case of
     * wrong "type" parameter. Also, it can be caused by non SMIMEException which can
     * be one of the following: MessagingException UnsoportedEncodingException.
     */
    public void setContent(String content, String type, String path, InputStream[] resources) throws SMIMEException {
        if (content != null) {
            ByteArrayInputStream bais = null;
            try {
                bais = new ByteArrayInputStream(content.getBytes("ISO-8859-1"));
            } catch (Exception e) {
                throw SMIMEException.getInstance(this, e, "setContent");
            }
            this.setContent(bais, type, path, resources);
        } else throw new SMIMEException(this, 1035);
    }

    /**
     * Sets message content from InputStream. This method can be performed only once.
     * Message content can be given in two differrent forms: text and html code. If
     * content is type of text, parameter "type" should be "text/plain", while if
     * content is type of html code, parameter "type" should be set as "html/code".
     * For further information refer to setContent method with four arguments
     * (String, String, String, InputStream[] ) which is called by this method.
     * @param content message content data given from any InputStream.
     * Data can be text or html code and will be interpreted according to second
     * parameter: "type".
     * @param type type of given content. It can take values: "text/plain" or
     * "text/html".
     * @param path common directory path for relative file locations in html code.
     * It can be null if all resources in html code have set absolute path or are
     * defined by byte array streams, or if sending resources with relative address
     * is not desired. Also, it is set to null in case of text/plain message.
     * @param resources way for representing resources used in the given html code
     * which will be added to message as array of InputStreams. Detail use
     * of this argument is described in other setContent methods mentioned before.
     * It can be null if no resources as byte array stream are used, or if sending
     * resources given in that way is not desired. Also, it is set to null in case
     * of text/plain message.
     * @exception SMIMEException if content is tried to be added twice , in case of
     * wrong "type" parameter or in case when parameter content is null. Also, it can
     * be caused by non SMIMEException which is MessagingException.
     */
    public void setContent(InputStream content, String type, String path, InputStream[] resources) throws SMIMEException {
        if (textContentPresence) throw new SMIMEException(this, 1049);
        if (content != null) {
            try {
                if (type.equalsIgnoreCase("text/plain")) {
                    MimeBodyPart mbp = new MimeBodyPart();
                    String temp = new String(ConvertAssist.inStreamToByteArray(content), "ISO-8859-1");
                    mbp.setText(temp, "ISO-8859-1");
                    bodyPartArray.add(0, mbp);
                    textContentPresence = true;
                } else if (type.equalsIgnoreCase("text/html")) {
                    MimeMultipart htmlMultipart = MultipartGenerator.getHtmlMultipart(content, path, resources);
                    bodyPartArray.add(0, htmlMultipart);
                    textContentPresence = true;
                } else throw new SMIMEException(this, 1048);
            } catch (Exception e) {
                throw SMIMEException.getInstance(this, e, "setContent");
            }
        } else throw new SMIMEException(this, 1035);
    }

    /**
     * Sets message content from InputStream. This method can be performed only once.
     * Message content can be given in two differrent forms: text and html code. If
     * content is type of text, parameter "type" should be "text/plain", while if
     * content is type of html code, parameter "type" should be set as "html/code".
     * If html code content is set by this method, message will be generated withouth
     * inclusion of the resources defined in paricular html element's attribute ("src" and
     * "background"). Only plain html code will be sent and any reference to local
     * file system resources will be useless for recipient of the message. HTTP referenced
     * resources can still be available if recipient is online on Internet. Message
     * generated on this way is smaller so encrypting process should be faster.
     * @param content message content data given from any InputStream.
     * Data could be text or html code and will be interpreted according to second
     * parameter: "type".
     * @param type type of given content. It can take values: "text/plain" or
     * "text/html".
     * @exception SMIMEException if content is tried to be added twice , in case of
     * wrong "type" parameter or in case when parameter content is null. Also, it can
     * be caused by non SMIMEException which is MessagingException.
     */
    public void setContent(InputStream content, String type) throws SMIMEException {
        if (textContentPresence) throw new SMIMEException(this, 1049);
        if (content != null) {
            try {
                if (type.equalsIgnoreCase("text/plain")) {
                    MimeBodyPart mbp = new MimeBodyPart();
                    String temp = new String(ConvertAssist.inStreamToByteArray(content), "ISO-8859-1");
                    mbp.setText(temp, "ISO-8859-1");
                    bodyPartArray.add(0, mbp);
                    textContentPresence = true;
                } else if (type.equalsIgnoreCase("text/html")) {
                    MimeMultipart htmlMultipart = MultipartGenerator.getHtmlMultipart(content);
                    bodyPartArray.add(0, htmlMultipart);
                    textContentPresence = true;
                } else throw new SMIMEException(this, 1048);
            } catch (Exception e) {
                throw SMIMEException.getInstance(this, e, "setContent");
            }
        } else throw new SMIMEException(this, 1035);
    }

    /**
     * Sets message content from String. This method can be performed only once.
     * Message content can be given in two differrent forms: text and html code. If
     * content is type of text, parameter "type" should be "text/plain", while if
     * content is type of html code, parameter "type" should be set as "html/code".
     * If html code content is set by this method, message will be generated withouth
     * inclusion of the resources defined in paricular html element's attribute ("src" or
     * "background"). Only plain html code will be sent and any reference to local
     * file system resources will be useless for recipient of the message. HTTP referenced
     * resources can still be available if recipient is online on Internet. Message
     * generated on this way is smaller, so encrypting process should be faster.
     * @param content message content data given as String which can
     * be text or html code and will be interpreted according to second parameter:
     * "type".
     * @param type type of given content. It can take values: "text/plain" or
     * "text/html".
     * @exception SMIMEException if content is tried to be added twice, or in case of
     * wrong "type" parameter. Also, it can be caused by non SMIMEException which can
     * be one of the following: MessagingException UnsoportedEncodingException.
     */
    public void setContent(String content, String type) throws SMIMEException {
        if (content != null) {
            ByteArrayInputStream bais = null;
            try {
                bais = new ByteArrayInputStream(content.getBytes("ISO-8859-1"));
            } catch (Exception e) {
                throw SMIMEException.getInstance(this, e, "setContent");
            }
            this.setContent(bais, type);
        } else throw new SMIMEException(this, 1035);
    }

    /**
     * Sets message content from file represented by File object. This method can be
     * performed only once. Message content can be given in two differrent forms:
     * text and html code. If content is type of text, parameter "type" should be
     * "text/plain", while if content is type of html code, parameter "type" should
     * be set as "html/code". For further information refer to setContent method
     * with four arguments (String, String, String, InputStream[] ) which is called
     * by this method.
     * @param inFile location of file which is used for content of the message
     * @param type type of given content. It can take values: "text/plain" or
     * "text/html".
     * @exception SMIMEException if content is tried to be added twice, in case of
     * wrong "type" parameter, or if passed file (as File object) does not exist in
     * file sistem. Also, it can be caused by non SMIMEException which can be one of
     * the following: MessagingException or IOException.
     */
    public void setContent(File inFile, String type) throws SMIMEException {
        if (textContentPresence) throw new SMIMEException(this, 1049);
        if (inFile != null && inFile.exists()) {
            try {
                File inFileAbs = inFile.getAbsoluteFile().getCanonicalFile();
                String content = ConvertAssist.readFileToString(inFileAbs);
                this.setContent(content, type, inFile.getParent(), null);
            } catch (Exception e) {
                throw SMIMEException.getInstance(this, e, "setContent");
            }
        } else throw new SMIMEException(this, 1034);
    }

    /**
     * Sets REPLY TO field in message header
     * @param replyAddress email address used for reply
     * @exception SMIMEException caused by non SMIMEException which is
     * MessagingException. Also javax.mail.internet.AddressException is thrown
     * from instances of InternetAddress class (but AddressException extends
     * MessagingException).
     */
    public void setReply(String replyAddress) throws SMIMEException {
        try {
            InternetAddress reply[] = new InternetAddress[1];
            reply[0] = new InternetAddress(replyAddress);
            message.setReplyTo(reply);
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "addRecipient");
        }
    }

    /**
     * Adds recipient address, type and .cer file of email recipient.
     * @param recipientAddress email address of recipent (fields TO or CC or BCC
     * in email message header)
     * @param type should be TO, CC or BCC.
     * @exception SMIMEException if type of addressing of the messages is not TO, CC,
     * or BCC.
     * @exception SMIMEException caused by non SMIMEException which is
     * MessagingException.
     */
    public void addRecipient(String recipientAddress, String type) throws SMIMEException {
        try {
            if (!type.equalsIgnoreCase("TO") & !type.equalsIgnoreCase("BCC") & !type.equalsIgnoreCase("CC")) throw new SMIMEException(this, 1042);
            if (type.equalsIgnoreCase("TO")) {
                message.addRecipients(Message.RecipientType.TO, recipientAddress);
                indicatorTo = true;
            } else if (type.equalsIgnoreCase("CC")) message.addRecipients(Message.RecipientType.CC, recipientAddress); else if (type.equalsIgnoreCase("BCC")) message.addRecipients(Message.RecipientType.BCC, recipientAddress);
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "addRecipient");
        }
    }

    /**
     * Adds file as attachment to email message
     * @param fileName path and file name used for attachment
     * @exception SMIMEException if passed file (as File object) does not exist in
     * file sistem. Also, it can be caused by non SMIMEException which is
     * MessagingException
     */
    public void addAttachment(String fileName) throws SMIMEException {
        File fn = new File(fileName);
        this.addAttachment(fn);
    }

    /**
     * Adds file as attachment to email message
     * @param file used for attachment represented as File object
     * @exception SMIMEException if passed file (as File object) does not exist in
     * file sistem. Also, it can be caused by non SMIMEException which is
     * MessagingException
     */
    public void addAttachment(File file) throws SMIMEException {
        if (!file.exists()) throw new SMIMEException(this, 1034);
        MimeBodyPart attachment = new MimeBodyPart();
        FileDataSource fd = new FileDataSource(file);
        try {
            attachment.setDataHandler(new DataHandler(fd));
            attachment.setDisposition(attachment.ATTACHMENT);
            attachment.setFileName(file.getName());
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "addAttachment");
        }
        bodyPartArray.addElement(attachment);
    }

    /**
     * Adds data from InputStream as attachment to email message
     * @param data byte array from InputStream
     * @param fileName virtual or real file name (wihouth path). Correct information
     * about name; extension of file name is especially important. Name
     * is used in construction of "name" parameter in Content-Type header line of
     * body parts of mime message. Extension of file is used in detection of
     * appropriate mime-type.
     * @exception SMIMEException caused by non SMIMEException which is
     * MessagingException
     */
    public void addAttachment(InputStream data, String fileName) throws SMIMEException {
        MimeBodyPart attachment = new MimeBodyPart();
        try {
            attachment.setDataHandler(new DataHandler(new StreamDataSource(data, fileName)));
            attachment.setDisposition(attachment.ATTACHMENT);
            attachment.setFileName(fileName);
            bodyPartArray.addElement(attachment);
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "addAttachment");
        }
    }

    /**
     * Sets Capabilities Attributes (method is optional, but if exists, must be
     * performed before addSigner method). Depending on parameter type0, other five
     * parameters make order in specific group of algorithms. Groups of algorithms
     * with positions of specific algorithms are:<BR>
     * (SIGNATURE, MD2 with RSA, MD5 with RSA, SHA1 with RSA, SHA1 with DSA, Unused field)<BR>
     * (SYMMETRIC, RC2 40 bits, RC2 64 bits, RC2 128 bits, DES, DES_EDE3)<BR>
     * (ENCIPHER, RSA, Unused field, Unused field, Unused field, Unused field)<BR>
     * <BR>
     * For example, if we wish to set Capabilities Attributes for symmetric algorithms
     * in order: RC2 64 bits, RC2 40 bits and DES, encipher algorithm RSA (only possible
     * in this version), and signature algorithms in order: SHA1 with RSA, MD5 with RSA
     * and MD2 with RSA, we should make following lines of code<BR>
     * <BR>
     * setCapabilities ("SYMMETRIC", 2, 1, 0, 3, 0)<BR>
     * setCapabilities ("ENCIPHER", 1, 0, 0, 0, 0)<BR>
     * setCapabilities ("SIGNATURE", 3, 2, 1, 0, 0)<BR>
     * <BR>
     * 0 means exclusion of algorithm from the specified position in the method. It is
     * free to decide which algorithm will be included, or which group of algorithm
     * will be included in Capabilities Attributes. If no groups are added, capabilities
     * attributes won't be added to Signed Attributes. If two or more signers will
     * sign the message, and their capabilities are different, this method should
     * be performed before every signing if we wish to specify Capabilities
     * Attributes for all particular signers. If type0 parameter is set as:<BR>
     * setCapabilities ("DEFAULT", 0, 0, 0, 0, 0)<BR>
     * it is equivalent to:<BR>
     * setCapabilities ("SYMMETRIC", 1, 0, 0, 0, 0)<BR>
     * setCapabilities ("ENCIPHER", 0, 0, 1, 0, 0)<BR>
     * setCapabilities ("SIGNATURE", 1, 0, 0, 0, 0)<BR>
     * @param type0 sets group of algorithms for capabilities attributes. It can be set
     * with values: SIGNATURE, SYMMETRIC, ENCIPHER or DEFAULT.
     * @param par10 sets order in group of parameters, or exclude some algorithms
     * from capabilities atributes. Can take values 1, 2, 3, 4 or 5 and 0 for
     * exclusion of the particular algorithm.
     * @param par20 same as for par10
     * @param par30 same as for par10
     * @param par40 same as for par10
     * @param par50 same as for par10
     * @exception SMIMEException if method is performed more than three times for one signer,
     * or in case of wrong values of parameters.
     */
    public void setCapabilities(String type0, int par10, int par20, int par30, int par40, int par50) throws SMIMEException {
        int[] tempType = { par10, par20, par30, par40, par50 };
        capabilitiesTemp.addElement(type0);
        capabilitiesTemp.addElement(tempType);
        if (capabilitiesTemp.size() > 6) throw new SMIMEException(this, 1045);
    }

    /**
     * Adds signer to signed S/MIME message
     * @param pfxfileName path and file name with certificate and private key
     * corresponding to the sender of the message (file with .p12 or .pfx extension)
     * @param password used to access to .pfx or .p12 file
     * @param signingAlg algorithm used for signing (can be SHA1_WITH_RSA,
     * MD2_WITH_RSA, MD5_WITH_RSA or SHA1_WITH_DSA).
     * @param includingCert including/not including certificates to signed
     * message
     * @param includingSignAttrib including/not including signed attributes
     * to signed message. Must be set to true in case of implicit signing
     * @exception SMIMEException caused by non SMIMEException which can be one of the
     * following: FileNotFoundException, NoSuchProviderException, KeyStoreException
     * CertificateException, NoSuchAlgorithmException or IOException.
     */
    public void addSigner(String pfxfileName, String password, String signingAlg, boolean includingCert, boolean includingSignAttrib) throws SMIMEException {
        try {
            char[] paswCh = password.toCharArray();
            FileInputStream inPFX = new FileInputStream(pfxfileName);
            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            ks.load(inPFX, paswCh);
            inPFX.close();
            boolean[] incl = { includingCert, includingSignAttrib };
            ksArray.addElement(ks);
            digestArray.addElement(signingAlg);
            including.addElement(incl);
            if (capabilitiesTemp.size() != 0) {
                for (int i = 0; i != capabilitiesTemp.size(); i++) capabilities.addElement(capabilitiesTemp.elementAt(i));
            }
            for (int i = 0; i != (6 - capabilitiesTemp.size()); i++) capabilities.addElement(null);
            capabilitiesTemp = new Vector(0, 1);
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "addSigner");
        }
    }

    /**
     * Adds signer to signed S/MIME message
     * @param pfxfileName path and file name with certificate and private key
     * corresponding to the sender of the message (file with .p12 or .pfx extension)
     * @param password used to access to .pfx or .p12 file
     * @param signingAlg algorithm used for signing (can be SHA1_WITH_RSA,
     * MD2_WITH_RSA, MD5_WITH_RSA or SHA1_WITH_DSA).
     * @param includingCert including/not including certificates to signed
     * message
     * @param includingSignAttrib including/not including signed attributes
     * to signed message. Must be set to true in case of implicit signing
     * @exception SMIMEException caused by non SMIMEException which can be one of the
     * following: FileNotFoundException, NoSuchProviderException, KeyStoreException
     * CertificateException, NoSuchAlgorithmException or IOException.
     */
    public void addSigner(KeyStore ks, String signingAlg, boolean includingCert, boolean includingSignAttrib) throws SMIMEException {
        try {
            boolean[] incl = { includingCert, includingSignAttrib };
            ksArray.addElement(ks);
            digestArray.addElement(signingAlg);
            including.addElement(incl);
            if (capabilitiesTemp.size() != 0) {
                for (int i = 0; i != capabilitiesTemp.size(); i++) capabilities.addElement(capabilitiesTemp.elementAt(i));
            }
            for (int i = 0; i != (6 - capabilitiesTemp.size()); i++) capabilities.addElement(null);
            capabilitiesTemp = new Vector(0, 1);
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "addSigner");
        }
    }

    /**
     * Adds signer to signed S/MIME message
     * @param chain certificate chain. First certificate in array must be
     * owner's certificate, and last certificate has to be root certificate
     * @param privKey private key corresponding to owner's certificate (DSA
     * or RSA depend on type of signing)
     * @param signingAlg algorithm used for signing (can be SHA1_WITH_RSA,
     * MD2_WITH_RSA, MD5_WITH_RSA or SHA1_WITH_DSA).
     * @param includingCert including/not including certificates to signed
     * message
     * @param includingSignAttrib including/not including signed attributes
     * to signed message. Must be set to true in case implicit signing
     */
    public void addSigner(X509Certificate[] chain, PrivateKey privKey, String signingAlg, boolean includingCert, boolean includingSignAttrib) {
        boolean[] incl = { includingCert, includingSignAttrib };
        certChainArray.addElement(chain);
        privKeyArray.addElement(privKey);
        digestArray2.addElement(signingAlg);
        including2.addElement(incl);
        if (capabilitiesTemp.size() != 0) {
            for (int i = 0; i != capabilitiesTemp.size(); i++) capabilities2.addElement(capabilitiesTemp.elementAt(i));
        }
        for (int i = 0; i != (6 - capabilitiesTemp.size()); i++) capabilities2.addElement(null);
        capabilitiesTemp = new Vector(0, 1);
    }

    /**
     * Adds additional certificate to signed message.
     * @param cert X509 certificate.
     */
    public void addCertificate(X509Certificate cert) {
        aditionalCerts.addElement(cert);
    }

    /**
     * Creates and signes the message with default implicit signing.
     * @exception SMIMEException if one of recipients is not declared as TO
     * recipient, or if there is no message for signing. Also, it can be caused
     * by non SMIMEException which can be one of the following: MessagingException
     * or IOException.
     */
    public void signing() throws SMIMEException {
        this.signing(false);
    }

    /**
     * Creates and signes the message
     * @param externalSignature choice between implicit and explicit signing
     * (true = explicit or external signing, false = implicit or internal signing).
     * @exception SMIMEException if one of recipients is not declared as TO
     * recipient, or if there is no message for signing. Also, it can be caused
     * by non SMIMEException which can be one of the following: MessagingException,
     * or IOException.
     */
    public void signing(boolean externalSignature) throws SMIMEException {
        try {
            if (indicatorTo != true) throw new SMIMEException(this, 1043);
            if (textContentPresence & bodyPartArray.size() == 1) {
                if (bodyPartArray.elementAt(0) instanceof MimeBodyPart) {
                    MimeBodyPart contentBody = (MimeBodyPart) bodyPartArray.elementAt(0);
                    message.setContent((String) contentBody.getContent(), contentBody.getContentType());
                    message.setDisposition(message.INLINE);
                } else message.setContent((MimeMultipart) bodyPartArray.elementAt(0));
            } else if (bodyPartArray.size() != 0) {
                Multipart mp = new MimeMultipart();
                for (int i = 0; i != bodyPartArray.size(); i++) {
                    if (bodyPartArray.elementAt(i) instanceof MimeMultipart) {
                        MimeBodyPart forMulti = new MimeBodyPart();
                        forMulti.setContent((MimeMultipart) bodyPartArray.elementAt(i));
                        mp.addBodyPart(forMulti);
                    } else mp.addBodyPart((MimeBodyPart) bodyPartArray.elementAt(i));
                }
                message.setContent(mp);
            } else throw new SMIMEException(this, 1044);
            CMSSignedDataSource ss = new CMSSignedDataSource(message, externalSignature);
            for (int i = 0; i < ksArray.size(); i++) {
                boolean[] incl = (boolean[]) including.elementAt(i);
                for (int j = 6 * i; j != (6 * (i + 1)) && capabilities.elementAt(j) != null; j = j + 2) {
                    int[] capabil = (int[]) capabilities.elementAt(j + 1);
                    ss.setCapabilities((String) capabilities.elementAt(j), capabil[0], capabil[1], capabil[2], capabil[3], capabil[4]);
                }
                ss.addSigner((KeyStore) ksArray.elementAt(i), incl[0], incl[1], (String) digestArray.elementAt(i));
            }
            for (int i = 0; i < certChainArray.size(); i++) {
                boolean[] incl2 = (boolean[]) including2.elementAt(i);
                for (int j = 6 * i; j != (6 * (i + 1)) && capabilities2.elementAt(j) != null; j = j + 2) {
                    int[] capabil = (int[]) capabilities2.elementAt(j + 1);
                    ss.setCapabilities((String) capabilities2.elementAt(j), capabil[0], capabil[1], capabil[2], capabil[3], capabil[4]);
                }
                ss.addSigner((X509Certificate[]) certChainArray.elementAt(i), (PrivateKey) privKeyArray.elementAt(i), incl2[0], incl2[1], (String) digestArray2.elementAt(i));
            }
            for (int i = 0; i < aditionalCerts.size(); i++) {
                ss.addCertificate((X509Certificate) aditionalCerts.elementAt(i));
            }
            message.setDataHandler(new DataHandler(ss));
            HeadersUtil.updateHeaders(message);
            message.setDescription("Signed SMIME message.");
            message.setDisposition(message.ATTACHMENT);
            SimpleTimeZone tz = (SimpleTimeZone) SimpleTimeZone.getDefault();
            GregorianCalendar cal = new GregorianCalendar(tz);
            message.setSentDate(cal.getTime());
            clean();
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "signing");
        }
    }

    /**
     * Returns SMIME Message
     * @return Signed S/MIME message
     */
    public MimeMessage getSignedMessage() {
        return message;
    }

    /**
     * Sends S/MIME message to SMTP host
     * @exception MessagingException caused by use of methods from objects of class
     * Transport.
     */
    public void send() throws MessagingException {
        Transport.send(message);
    }

    /**
     * Releases unnecessary memory
     */
    private void clean() {
        ksArray = null;
        digestArray = null;
        including = null;
        certChainArray = null;
        privKeyArray = null;
        digestArray2 = null;
        including2 = null;
        bodyPartArray = null;
        aditionalCerts = null;
        capabilitiesTemp = null;
        capabilities = null;
        capabilities2 = null;
    }
}
