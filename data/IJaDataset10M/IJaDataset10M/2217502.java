package com.memetix.mst.examples;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;

/**
 * DetectLanguageExample
 * 
 * Calls Microsoft to determine origin language of provided text.
 * 
 * Shows how to turn the two character response code into a language and how to localize the Language name
 *
 * @author griggs.jonathan
 * @date Jun 1, 2011
 * @since 0.3 June 1, 2011
 */
public class DetectLanguageExample {

    public static void main(String[] args) throws Exception {
        Detect.setClientId("YOUR_CLIENT_ID_HERE");
        Detect.setClientSecret("YOUR_CLIENT_SECRET_HERE");
        Language detectedLanguage = Detect.execute("Bonjour le monde");
        System.out.println(detectedLanguage);
        System.out.println(detectedLanguage.getName(Language.ENGLISH));
        System.out.println(detectedLanguage.getName(Language.FRENCH));
        System.out.println(detectedLanguage.getName(Language.GERMAN));
    }
}
