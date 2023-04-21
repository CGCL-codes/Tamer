package org.jprovocateur.basis.businesslayer.translation;

import org.jprovocateur.basis.error.BasisError;
import org.jprovocateur.basis.error.BasisErrorDef;
import org.jprovocateur.basis.error.BasisException;
import org.jprovocateur.basis.objectmodel.translation.impl.MessageTranslation;
import org.jprovocateur.basis.serviceslayer.BusinessLogicInt;
import org.jprovocateur.basis.serviceslayer.impl.BusinessLogic;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p/>
 *
 * @author Michael Pitsounis 
 */
@Service("messageTranslationCreateService")
@Scope("prototype")
@Transactional(readOnly = false)
public class MessageTranslationCreateService extends BusinessLogic<MessageTranslation> {

    public String getExecutionType() {
        return BusinessLogicInt.CREATE;
    }

    public void validate(MessageTranslation data) throws BasisException {
        super.validate(data);
        MessageTranslation messageTranslation = data;
        MessageTranslation messageTranslationTest = new MessageTranslation();
        messageTranslationTest.setId(messageTranslation.getKey().toString().concat("." + messageTranslation.getLang().toString()));
        if (this.getDao().list(messageTranslationTest).size() > 0) {
            BasisError error = new BasisError("msg for the developer : translation ID is  already defined", BasisErrorDef.TRANSLATION_IS_ALREADY_REGISTERED, BasisError.BUSINESS, this, "id", null);
            throw new BasisException(error);
        }
    }

    public Object execute(MessageTranslation messageTranslation) throws BasisException {
        messageTranslation.setId(messageTranslation.getKey().concat("." + messageTranslation.getLang().toString()));
        super.execute(messageTranslation);
        return messageTranslation;
    }
}
