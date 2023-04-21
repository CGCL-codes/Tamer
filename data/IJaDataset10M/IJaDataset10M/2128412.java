package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart;

/**
 * represents free text in a rabbit document. It consists of an opening quote
 * sign, some string and an ending quote.
 * 
 * @author rdenaux
 * 
 */
public interface IParsedFreeText extends IParsedPart {

    IParsedKeyphrase getOpening();

    String getText();

    IParsedKeyphrase getClosing();
}
