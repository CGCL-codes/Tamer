package org.jdiameter.client.api.annotation;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;

/**
 * This interface provide methods for create diameter messages from your annotated domain object and
 * create domain object from diameter message.
 * 
 * @author erick.svenson@yahoo.com
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface IRecoder {

    /**
   * Create Request message from specified annotated domain object
   * 
   * @param yourDomainMessageObject annotated domain object
   * @param additionalAvp additional avp
   * @return message instance
   * @throws RecoderException throw if object can not be encoded to diameter message
   */
    public Message encodeToRequest(Object yourDomainMessageObject, Avp... additionalAvp) throws RecoderException;

    /**
   * Create Answer message from specified annotated domain object
   * 
   * @param yourDomainMessageObject annotated domain object
   * @param request request message
   * @param resultCode result code of answer
   * @return message answer instance 
   * @throws RecoderException throw if object can not be encoded to diameter message
   */
    public Message encodeToAnswer(Object yourDomainMessageObject, Request request, long resultCode) throws RecoderException;

    /**
   * Create specified domain object by message and class of object
   * 
   * @param message diameter message
   * @param yourDomainMessageObject class of domain object
   * @return instance of domain object
   * @throws RecoderException throw if message can not be decoded to domain object
   */
    public <T> T decode(Message message, java.lang.Class<T> yourDomainMessageObject) throws RecoderException;
}
