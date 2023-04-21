package javax.wireless.messaging;

public interface MultipartMessage extends Message {

    boolean addAddress(java.lang.String type, java.lang.String address);

    void addMessagePart(MessagePart part) throws SizeExceededException;

    java.lang.String getAddress();

    java.lang.String[] getAddresses(java.lang.String type);

    java.lang.String getHeader(java.lang.String headerField);

    javax.wireless.messaging.MessagePart getMessagePart(java.lang.String contentID);

    javax.wireless.messaging.MessagePart[] getMessageParts();

    java.lang.String getStartContentId();

    java.lang.String getSubject();

    boolean removeAddress(java.lang.String type, java.lang.String address);

    void removeAddresses();

    void removeAddresses(java.lang.String type);

    boolean removeMessagePart(MessagePart part);

    boolean removeMessagePartId(java.lang.String contentID);

    boolean removeMessagePartLocation(java.lang.String contentLocation);

    void setAddress(java.lang.String addr);

    void setHeader(java.lang.String headerField, java.lang.String headerValue);

    void setStartContentId(java.lang.String contentId);

    void setSubject(java.lang.String subject);
}
