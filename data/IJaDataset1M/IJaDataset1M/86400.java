package org.vramework.mvc.web.sample;

import org.vramework.commons.resources.DefaultMessage;
import org.vramework.commons.resources.MessageHelper;

/**
 * @author thomas.mahringer
 */
public class SampleUserMessages extends DefaultMessage {

    public static SampleUserMessages StoreSucessful = new SampleUserMessages();

    public static SampleUserMessages ValidationRangeError = new SampleUserMessages();

    public static SampleUserMessages ValidationFormatError = new SampleUserMessages();

    public static SampleUserMessages ValidationDateAllFieldsOrNone = new SampleUserMessages();

    public static SampleUserMessages UploadSuccessful = new SampleUserMessages();

    public static SampleUserMessages UploadEmptyDocument = new SampleUserMessages();

    public static SampleUserMessages ToManyResultsForQuery = new SampleUserMessages();

    public static SampleUserMessages CannotSortByThisColumn_NotParOfQuery = new SampleUserMessages();

    public static SampleUserMessages SampleMessage = new SampleUserMessages();

    static {
        MessageHelper.initClassFields(SampleUserMessages.class);
    }
}
