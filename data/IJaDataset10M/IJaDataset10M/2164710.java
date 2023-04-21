package net.sf.grobid.lib.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.commons.lang.StringUtils;

/**
 * @author Patrice Lopez
 */
public class FeatureTrainerUtil {

    private static int nbBins = 12;

    /**
	 * Add feature for header recognition. The boolean indicates if the label set should be limited or not to only title
	 * and date.
	 */
    public static String[] addFeaturesHeader(List<String> lines, String tag) {
        ArrayList<String> featuresVectors = new ArrayList<String>();
        FeatureFactory featureFactory = FeatureFactory.getInstance();
        boolean newline = true;
        boolean newBlock = true;
        int n = 0;
        while (n < lines.size()) {
            String text = lines.get(n);
            if (StringUtils.isBlank(text.trim())) {
                n++;
                continue;
            }
            FeaturesVectorHeader features = new FeaturesVectorHeader();
            features.string = text;
            if (newline) {
                features.lineStatus = "LINESTART";
            }
            if (newBlock) {
                features.blockStatus = "BLOCKSTART";
            }
            if (StringUtils.containsAny(text, TextUtilities.PUNCTUATIONS)) {
                features.punctType = "PUNCT";
            }
            if ((text.equals("(")) | (text.equals("["))) {
                features.punctType = "OPENBRACKET";
            } else if ((text.equals(")")) | (text.equals("]"))) {
                features.punctType = "ENDBRACKET";
            } else if (text.equals(".")) {
                features.punctType = "DOT";
            } else if (text.equals(",")) {
                features.punctType = "COMMA";
            } else if (text.equals("-")) {
                features.punctType = "HYPHEN";
            } else if (text.equals("\"") | text.equals("\'") | text.equals("`")) {
                features.punctType = "QUOTE";
            }
            if (n == 0) {
                if (features.lineStatus != null) {
                    features.lineStatus = "LINESTART";
                }
                if (features.blockStatus != null) {
                    features.blockStatus = "BLOCKSTART";
                }
            } else if (lines.size() == n + 1) {
                if (features.lineStatus != null) {
                    features.lineStatus = "LINEEND";
                }
                if (features.blockStatus != null) {
                    features.blockStatus = "BLOCKEND";
                }
            } else {
                boolean endline = false;
                int i = 1;
                boolean endloop = false;
                while ((lines.size() > n + i) & (!endloop)) {
                    String newLine = lines.get(n + i);
                    if (StringUtils.isBlank(newLine)) {
                        endline = true;
                        if (features.lineStatus != null) {
                            features.lineStatus = "LINEEND";
                        }
                        if (features.blockStatus != null) {
                            features.blockStatus = "BLOCKEND";
                        }
                    } else {
                        endloop = true;
                    }
                    if ((endline) & (features.lineStatus != null)) {
                        features.lineStatus = "LINEEND";
                    }
                    i++;
                }
            }
            newline = false;
            newBlock = false;
            features.fontStatus = "SAMEFONT";
            features.fontSize = "SAMEFONTSIZE";
            if (features.blockStatus != null) {
                features.blockStatus = "BLOCKIN";
            }
            if (features.lineStatus != null) {
                features.lineStatus = "LINEIN";
            }
            if (text.length() == 1) {
                features.singleChar = true;
                ;
            }
            if (Character.isUpperCase(text.charAt(0))) {
                features.capitalisation = "INITCAP";
            }
            if (featureFactory.test_all_capital(text)) {
                features.capitalisation = "ALLCAP";
            }
            if (features.capitalisation == null) features.capitalisation = "NOCAPS";
            if (featureFactory.test_digit(text)) {
                features.digit = "CONTAINSDIGITS";
            }
            if (featureFactory.test_common(text)) {
                features.commonName = true;
            }
            if (featureFactory.test_names(text)) {
                features.properName = true;
            }
            if (featureFactory.test_month(text)) {
                features.month = true;
            }
            if (text.indexOf("-") != -1) {
                features.containDash = true;
            }
            Matcher m = featureFactory.isDigit.matcher(text);
            if (m.find()) {
                features.digit = "ALLDIGIT";
            }
            if (features.digit == null) features.digit = "NODIGIT";
            Matcher m2 = featureFactory.YEAR.matcher(text);
            if (m2.find()) {
                features.year = true;
            }
            Matcher m3 = featureFactory.EMAIL.matcher(text);
            if (m3.find()) {
                features.email = true;
            }
            Matcher m4 = featureFactory.HTTP.matcher(text);
            if (m4.find()) {
                features.http = true;
            }
            if (features.punctType == null) features.punctType = "NOPUNCT";
            features.label = tag;
            featuresVectors.add(features.printVector(true));
            n++;
        }
        return featuresVectors.toArray(new String[featuresVectors.size()]);
    }

    /**
	 * Add feature for citation parsing.
	 */
    public static String[] addFeaturesCitation(List<String> tokens) {
        ArrayList<String> featuresVectors = new ArrayList<String>();
        FeatureFactory featureFactory = FeatureFactory.getInstance();
        int n = 0;
        while (n < tokens.size()) {
            String text = tokens.get(n);
            if (StringUtils.isBlank(text.trim())) {
                n++;
                continue;
            }
            String tag = "<citation>";
            FeaturesVectorCitation features = new FeaturesVectorCitation();
            features.string = text;
            features.relativePosition = featureFactory.relativeLocation(n, tokens.size(), nbBins);
            if (StringUtils.containsAny(text, TextUtilities.PUNCTUATIONS)) {
                features.punctType = "PUNCT";
            }
            if ((text.equals("(")) | (text.equals("["))) {
                features.punctType = "OPENBRACKET";
            } else if ((text.equals(")")) | (text.equals("]"))) {
                features.punctType = "ENDBRACKET";
            } else if (text.equals(".")) {
                features.punctType = "DOT";
            } else if (text.equals(",")) {
                features.punctType = "COMMA";
            } else if (text.equals("-")) {
                features.punctType = "HYPHEN";
            } else if (text.equals("\"") | text.equals("\'") | text.equals("`")) {
                features.punctType = "QUOTE";
            }
            if (n == 0) {
                if (features.lineStatus == null) {
                    features.lineStatus = "LINESTART";
                }
            } else if (n == tokens.size() - 1) {
                if (features.lineStatus == null) {
                    features.lineStatus = "LINEEND";
                }
            }
            if (features.lineStatus == null) {
                features.lineStatus = "LINEIN";
            }
            if (text.length() == 1) {
                features.singleChar = true;
            }
            if (Character.isUpperCase(text.charAt(0))) {
                features.capitalisation = "INITCAP";
            }
            if (featureFactory.test_all_capital(text)) {
                features.capitalisation = "ALLCAP";
            }
            if (featureFactory.test_digit(text)) {
                features.digit = "CONTAINSDIGITS";
            }
            if (featureFactory.test_common(text)) {
                features.commonName = true;
            }
            if (featureFactory.test_names(text)) {
                features.properName = true;
            }
            if (featureFactory.test_month(text)) {
                features.month = true;
            }
            Matcher m = featureFactory.isDigit.matcher(text);
            if (m.find()) {
                features.digit = "ALLDIGIT";
            }
            Matcher m2 = featureFactory.YEAR.matcher(text);
            if (m2.find()) {
                features.year = true;
            }
            Matcher m3 = featureFactory.EMAIL.matcher(text);
            if (m3.find()) {
                features.email = true;
            }
            Matcher m4 = featureFactory.HTTP.matcher(text);
            if (m4.find()) {
                features.http = true;
            }
            if (features.capitalisation == null) {
                features.capitalisation = "NOCAPS";
            }
            if (features.digit == null) {
                features.digit = "NODIGIT";
            }
            if (features.punctType == null) {
                features.punctType = "NOPUNCT";
            }
            features.label = tag;
            featuresVectors.add(features.printVector());
            n++;
        }
        return featuresVectors.toArray(new String[featuresVectors.size()]);
    }

    /**
	 * Add the features for the patent reference extraction model.
	 */
    public static FeaturesVectorReference addFeaturesPatentReferences(String token, int totalLength, int position, boolean isJournalToken, boolean isAbbrevJournalToken, boolean isConferenceToken, boolean isPublisherToken) {
        FeatureFactory featureFactory = FeatureFactory.getInstance();
        FeaturesVectorReference featuresVector = new FeaturesVectorReference();
        featuresVector.string = token;
        if (token.length() == 1) {
            featuresVector.singleChar = true;
        }
        if (featureFactory.test_all_capital(token)) featuresVector.capitalisation = "ALLCAPS"; else if (featureFactory.test_first_capital(token)) featuresVector.capitalisation = "INITCAP"; else featuresVector.capitalisation = "NOCAPS";
        if (featureFactory.test_number(token)) featuresVector.digit = "ALLDIGIT"; else if (featureFactory.test_digit(token)) featuresVector.digit = "CONTAINDIGIT"; else featuresVector.digit = "NODIGIT";
        if (featureFactory.test_common(token)) featuresVector.commonName = true;
        if (featureFactory.test_names(token)) featuresVector.properName = true;
        if (featureFactory.test_month(token)) featuresVector.month = true;
        if (StringUtils.containsAny(token, TextUtilities.PUNCTUATIONS)) {
            featuresVector.punctType = "PUNCT";
        }
        if ((token.equals("(")) | (token.equals("["))) {
            featuresVector.punctType = "OPENBRACKET";
        } else if ((token.equals(")")) | (token.equals("]"))) {
            featuresVector.punctType = "ENDBRACKET";
        } else if (token.equals(".")) {
            featuresVector.punctType = "DOT";
        } else if (token.equals(",")) {
            featuresVector.punctType = "COMMA";
        } else if (token.equals("-")) {
            featuresVector.punctType = "HYPHEN";
        } else if (token.equals("\"") | token.equals("\'") | token.equals("`")) {
            featuresVector.punctType = "QUOTE";
        }
        Matcher m2 = featureFactory.YEAR.matcher(token);
        if (m2.find()) {
            featuresVector.year = true;
        }
        Matcher m4 = featureFactory.HTTP.matcher(token);
        if (m4.find()) {
            featuresVector.http = true;
        }
        if (featuresVector.capitalisation == null) featuresVector.capitalisation = "NOCAPS";
        if (featuresVector.digit == null) featuresVector.digit = "NODIGIT";
        if (featuresVector.punctType == null) featuresVector.punctType = "NOPUNCT";
        if (featureFactory.test_country_codes(token)) featuresVector.isCountryCode = true;
        if (featureFactory.test_kind_codes(token)) featuresVector.isKindCode = true;
        featuresVector.relativeDocumentPosition = featureFactory.relativeLocation(position, totalLength, nbBins);
        if (isJournalToken) {
            featuresVector.isKnownJournalTitle = true;
        }
        if (isAbbrevJournalToken) {
            featuresVector.isKnownAbbrevJournalTitle = true;
        }
        if (isConferenceToken) {
            featuresVector.isKnownConferenceTitle = true;
        }
        if (isPublisherToken) {
            featuresVector.isKnownPublisher = true;
        }
        return featuresVector;
    }
}
