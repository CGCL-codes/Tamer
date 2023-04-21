package com.jflickrorganizr.flickr.service.connector.parser.xml;

import java.util.EnumMap;
import org.w3c.dom.Element;
import com.jflickrorganizr.flickr.model.APIMethodReturnable;
import com.jflickrorganizr.flickr.model.FlickrCollectionList;
import com.jflickrorganizr.flickr.model.UserCredentials;
import com.jflickrorganizr.flickr.model.UserFrob;
import com.jflickrorganizr.flickr.service.connector.parser.ResponseParser;
import com.jflickrorganizr.flickr.service.connector.parser.ResponseParserException;
import com.jflickrorganizr.flickr.service.connector.parser.ResponseParserFactory;

public class XMLResponseParserFactory implements ResponseParserFactory<Element> {

    private enum ValidReturnable {

        USER_CREDENTIALS(UserCredentials.class, UserCredentialsParser.class), USER_FROB(UserFrob.class, UserFrobParser.class), FLICKR_COLLECTIONS(FlickrCollectionList.class, FlickrCollectionListParser.class);

        private Class<? extends APIMethodReturnable> returnableClass;

        private Class<? extends ResponseParser<Element, ? extends APIMethodReturnable>> translatorClass;

        ValidReturnable(final Class<? extends APIMethodReturnable> returnableClass, final Class<? extends ResponseParser<Element, ? extends APIMethodReturnable>> translatorClass) {
            this.returnableClass = returnableClass;
            this.translatorClass = translatorClass;
        }

        Class<? extends APIMethodReturnable> getReturnableClass() {
            return returnableClass;
        }

        Class<? extends ResponseParser<Element, ? extends APIMethodReturnable>> getTranslatorClass() {
            return translatorClass;
        }

        static ValidReturnable get(final Class<? extends APIMethodReturnable> returnableClass) {
            for (ValidReturnable returnable : ValidReturnable.values()) {
                if (returnable.getReturnableClass() == returnableClass) {
                    return returnable;
                }
            }
            return null;
        }
    }

    private EnumMap<ValidReturnable, ResponseParser<Element, ? extends APIMethodReturnable>> translators = new EnumMap<ValidReturnable, ResponseParser<Element, ? extends APIMethodReturnable>>(ValidReturnable.class);

    public XMLResponseParserFactory() {
    }

    public ResponseParser<Element, ? extends APIMethodReturnable> getTranslator(final Class<? extends APIMethodReturnable> returnableClass) throws ResponseParserException {
        ValidReturnable returnable = ValidReturnable.get(returnableClass);
        if (returnable == null) {
            throw new ResponseParserException("Unrecognised response type");
        }
        ResponseParser<Element, ? extends APIMethodReturnable> translator = translators.get(returnable);
        if (translator == null) {
            try {
                translator = returnable.getTranslatorClass().newInstance();
            } catch (InstantiationException ex) {
                throw new ResponseParserException(ex);
            } catch (IllegalAccessException ex) {
                throw new ResponseParserException(ex);
            }
            translators.put(returnable, translator);
        }
        return translator;
    }
}
