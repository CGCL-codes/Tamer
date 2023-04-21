package uk.ekiwi.messaging;

public interface Message {

    public String getMessageID() throws MessagingException;

    public long getTimestamp() throws MessagingException;

    public String getFormat();

    public byte[] getCorrelationIDAsBytes() throws MessagingException;

    public void setCorrelationIDAsBytes(byte[] arg0) throws MessagingException;

    public void setCorrelationID(String arg0) throws MessagingException;

    public String getCorrelationID() throws MessagingException;

    public long getExpiration() throws MessagingException;

    public void setExpiration(long arg0) throws MessagingException;

    public int getPriority() throws MessagingException;

    public void setPriority(int arg0) throws MessagingException;

    public void clearBody() throws MessagingException;
}
