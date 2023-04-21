package com.cubusmail.server.mail.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.AndTerm;
import javax.mail.search.BodyTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cubusmail.common.model.MessageListFields;
import com.cubusmail.common.util.CubusConstants;
import com.sun.mail.imap.IMAPFolder;

/**
 * Util class for general message functions.
 * 
 * @author Juergen Schlierf
 */
public class MessageUtils {

    private static final Log log = LogFactory.getLog(MessageUtils.class.getName());

    /**
	 * Type of address string
	 * 
	 * @author Juergen Schlierf
	 */
    public enum AddressStringType {

        COMPLETE, PERSONAL, PERSONAL_ONLY, EMAIL
    }

    ;

    public static final int BUFSIZE = 8192;

    /**
	 * @param part
	 * @return
	 * @throws MessagingException
	 */
    public static boolean isImagepart(Part part) throws MessagingException {
        return part.isMimeType("image/png") || part.isMimeType("image/gif") || part.isMimeType("image/jpg") || part.isMimeType("image/jpeg");
    }

    /**
	 * @param emailAddress
	 * @param displayName
	 * @return
	 */
    public static String toInternetAddress(String emailAddress, String displayName) {
        InternetAddress address;
        try {
            if (!StringUtils.isEmpty(displayName)) {
                address = new InternetAddress(emailAddress, displayName);
                return address.toUnicodeString();
            } else {
                return emailAddress;
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
	 * Parses the adresslist.
	 * 
	 * @param addresslist
	 * @param charset
	 * @return
	 */
    public static InternetAddress[] parseInternetAddress(String addresslist, String charset) throws MessagingException {
        if (addresslist != null) {
            addresslist = addresslist.replace(';', ',');
            InternetAddress[] addressArray = InternetAddress.parse(addresslist);
            if (addressArray != null) {
                for (InternetAddress address : addressArray) {
                    String personal = address.getPersonal();
                    if (personal != null) {
                        try {
                            address.setPersonal(personal, charset);
                        } catch (UnsupportedEncodingException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
            return addressArray;
        }
        return null;
    }

    /**
	 * Format the date.
	 * 
	 * @param date
	 * @return
	 */
    public static String formatMailDate(Date date) {
        if (date != null) {
            DateFormat format = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT, Locale.GERMAN);
            return format.format(date);
        } else {
            return "";
        }
    }

    /**
	 * Format the size of a part like message or attachment.
	 * 
	 * @param size
	 * @return
	 */
    public static String formatPartSize(int size, NumberFormat format) {
        String value = null;
        if (size >= 1024) {
            value = format.format(size / 1024) + " KB";
        } else {
            if (size > 0) {
                value = Integer.toString(size) + " B";
            } else {
                value = "n/a";
            }
        }
        return value;
    }

    /**
	 * @param locale
	 * @return
	 */
    public static NumberFormat createSizeFormat(Locale locale) {
        NumberFormat sizeFormat = DecimalFormat.getNumberInstance(locale);
        sizeFormat.setGroupingUsed(true);
        sizeFormat.setMaximumFractionDigits(0);
        sizeFormat.setMinimumFractionDigits(0);
        return sizeFormat;
    }

    /**
	 * Calculate the real size bytes.
	 * 
	 * @param orgSize
	 * @return
	 */
    public static int calculateAttachmentSize(int orgSize) {
        if (orgSize > 0) {
            double size = (double) orgSize;
            size = size * CubusConstants.MESSAGE_SIZE_FACTOR;
            return (int) Math.round(size);
        }
        return orgSize;
    }

    /**
	 * @param addressArray
	 * @param emailAddress
	 * @return
	 */
    public static boolean findEmailAddress(Address[] addressArray, Address addressToFind) {
        if (addressArray != null) {
            for (Address address : addressArray) {
                if (address.equals(addressToFind)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * @param msg
	 * @return
	 * @throws MessagingException
	 */
    public static boolean isHtmlMessage(Message msg) throws MessagingException {
        return msg.isMimeType("text/html");
    }

    /**
	 * Method for checking if the message has attachments.
	 */
    public static List<MimePart> attachmentsFromPart(Part part) throws MessagingException, IOException {
        List<MimePart> attachmentParts = new ArrayList<MimePart>();
        if (part instanceof MimePart) {
            MimePart mimePart = (MimePart) part;
            if (part.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) mimePart.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) mp.getBodyPart(i);
                    if (Part.ATTACHMENT.equals(bodyPart.getDisposition()) || Part.INLINE.equals(bodyPart.getDisposition())) {
                        attachmentParts.add(bodyPart);
                    }
                }
            } else if (part.isMimeType("application/*")) {
                attachmentParts.add(mimePart);
            }
        }
        return attachmentParts;
    }

    /**
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
    public static boolean hasAttachments(Part part) throws MessagingException, IOException {
        try {
            if (part.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) part.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) mp.getBodyPart(i);
                    if (Part.ATTACHMENT.equals(bodyPart.getDisposition()) || Part.INLINE.equals(bodyPart.getDisposition())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
	 * @param addressArray
	 * @param personalOnly
	 * @return
	 * @throws MessagingException
	 */
    public static String getMailAdressString(Address[] addressArray, AddressStringType type) throws MessagingException {
        String addressString = "";
        if (addressArray != null) {
            for (int i = 0; i < addressArray.length; i++) {
                String address = getMailAdressString(addressArray[i], type);
                if (i < (addressArray.length - 1)) {
                    addressString += address + ", ";
                } else {
                    addressString += address;
                }
            }
        } else {
            addressString = "";
        }
        return addressString;
    }

    /**
	 * @param address
	 * @param personalOnly
	 * @return
	 * @throws MessagingException
	 */
    public static String getMailAdressString(Address address, AddressStringType type) throws MessagingException {
        String addressString = null;
        if (type == AddressStringType.PERSONAL || type == AddressStringType.PERSONAL_ONLY) {
            addressString = ((InternetAddress) address).getPersonal();
            if (StringUtils.isEmpty(addressString) && type == AddressStringType.PERSONAL) {
                addressString = ((InternetAddress) address).getAddress();
            }
        } else if (type == AddressStringType.COMPLETE) {
            addressString = ((InternetAddress) address).toUnicodeString();
        } else if (type == AddressStringType.EMAIL) {
            addressString = ((InternetAddress) address).getAddress();
        }
        return addressString;
    }

    /**
	 * @param data
	 * @return
	 */
    public static String decodeText(String data) {
        int start = 0, i;
        StringBuffer sb = new StringBuffer();
        while ((i = data.indexOf("=?", start)) >= 0) {
            sb.append(data.substring(start, i));
            int end = data.indexOf("?=", i);
            if (end < 0) {
                break;
            }
            String s = data.substring(i, end + 2);
            try {
                sb.append(MimeUtility.decodeWord(s));
            } catch (Exception e) {
                sb.append(s);
            }
            start = end + 2;
        }
        if (start == 0) return data;
        if (start < data.length()) sb.append(data.substring(start));
        return sb.toString();
    }

    /**
	 * @param msg
	 * @param flag
	 * @param readFlag
	 * @throws MessagingException
	 */
    public static void setMessageFlag(Message msg, Flags.Flag flag, boolean readFlag) throws MessagingException {
        boolean currentFlag = msg.isSet(flag);
        if (currentFlag != readFlag) {
            msg.setFlag(flag, readFlag);
        }
    }

    /**
	 * @param msg
	 * @return
	 * @throws MessagingException
	 */
    public static boolean isMessageReadFlag(Message msg) throws MessagingException {
        return msg.isSet(Flags.Flag.SEEN);
    }

    /**
	 * 
	 * 
	 * @param msg
	 * @return
	 */
    public static int getMessagePriority(Message msg) {
        int prio = CubusConstants.PRIORITY_NONE;
        try {
            String header[] = msg.getHeader(CubusConstants.FETCH_ITEM_PRIORITY);
            if (header != null && header.length > 0 && header[0].length() > 0) {
                String first = header[0].substring(0, 1);
                if (StringUtils.isNumeric(first)) {
                    return Integer.parseInt(first);
                }
            }
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
        return prio;
    }

    /**
	 * Sort the messages.
	 * 
	 * @param fieldName
	 * @param ascending
	 */
    public static void sortMessages(Message[] msgs, MessageListFields sortField, boolean ascending) {
        if (msgs != null && msgs.length > 0) {
            Arrays.sort(msgs, new MessageComparator(sortField, ascending));
        }
    }

    /**
	 * Create a search term for filtering messages.
	 * 
	 * @param searchFields
	 * @param searchValues
	 * @return
	 */
    public static SearchTerm createSearchTerm(MessageListFields[] searchFields, String[] searchValues) {
        SearchTerm[] terms = new SearchTerm[searchFields.length];
        for (int i = 0; i < searchFields.length; i++) {
            terms[i] = createSearchTerm(searchFields[i], searchValues);
        }
        if (searchFields.length > 1) {
            return new OrTerm(terms);
        } else {
            return terms[0];
        }
    }

    private static SearchTerm createSearchTerm(MessageListFields searchField, String[] searchValues) {
        SearchTerm[] terms = new SearchTerm[searchValues.length];
        for (int i = 0; i < searchValues.length; i++) {
            if (searchField == MessageListFields.SUBJECT) {
                terms[i] = new SubjectTerm(searchValues[i]);
            } else if (searchField == MessageListFields.FROM) {
                terms[i] = new FromStringTerm(searchValues[i]);
            } else if (searchField == MessageListFields.TO) {
                terms[i] = new RecipientStringTerm(RecipientType.TO, searchValues[i]);
            } else if (searchField == MessageListFields.CC) {
                terms[i] = new RecipientStringTerm(RecipientType.CC, searchValues[i]);
            } else if (searchField == MessageListFields.CONTENT) {
                terms[i] = new BodyTerm(searchValues[i]);
            } else {
                throw new IllegalArgumentException("Search field now allowed: " + searchField.name());
            }
        }
        if (searchValues.length > 1) {
            return new AndTerm(terms);
        } else {
            return terms[0];
        }
    }

    /**
	 * @param complete
	 * @param sortfield
	 * @return
	 */
    public static FetchProfile createFetchProfile(boolean complete, MessageListFields sortfield) {
        FetchProfile fp = new FetchProfile();
        if (complete) {
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.FLAGS);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            fp.add(IMAPFolder.FetchProfileItem.SIZE);
            fp.add(CubusConstants.FETCH_ITEM_PRIORITY);
            fp.add(UIDFolder.FetchProfileItem.UID);
        } else {
            if (sortfield != null) {
                if (MessageListFields.SUBJECT == sortfield || MessageListFields.FROM == sortfield || MessageListFields.TO == sortfield || MessageListFields.SEND_DATE == sortfield) {
                    fp.add(FetchProfile.Item.ENVELOPE);
                } else if (MessageListFields.SIZE == sortfield) {
                    fp.add(IMAPFolder.FetchProfileItem.SIZE);
                }
            }
        }
        return fp;
    }
}
