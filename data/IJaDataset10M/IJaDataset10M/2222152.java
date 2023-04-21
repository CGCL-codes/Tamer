package de.kugihan.dictionaryformids.dataaccess;

import de.kugihan.dictionaryformids.dataaccess.content.ContentDefinition;
import de.kugihan.dictionaryformids.dataaccess.content.FontStyle;
import de.kugihan.dictionaryformids.dataaccess.content.PredefinedContent;
import de.kugihan.dictionaryformids.dataaccess.content.RGBColour;
import de.kugihan.dictionaryformids.dataaccess.content.SelectionMode;
import de.kugihan.dictionaryformids.general.DictionaryClassNotLoadedException;
import de.kugihan.dictionaryformids.general.DictionaryException;
import de.kugihan.dictionaryformids.general.Util;
import de.kugihan.dictionaryformids.translation.normation.Normation;

public class DictionaryDataFile {

    public static int numberOfAvailableLanguages;

    public static int numberOfInputLanguages;

    public static LanguageDefinition[] supportedLanguages;

    public static String infoText;

    public static String dictionaryAbbreviation;

    public static String pathNameFonts = "fonts";

    public static String pathNameDataFiles = "dictionary";

    public static String prefixSearchListFile = "searchlist";

    public static String suffixSearchListFile = ".csv";

    public static String searchListCharEncoding;

    public static char searchListFileSeparationCharacter;

    public static int searchListFileMaxSize;

    public static String prefixIndexFile = "index";

    public static String suffixIndexFile = ".csv";

    public static String indexCharEncoding;

    public static char indexFileSeparationCharacter;

    public static char indexFileSeparatorFileNumberToPosition = '-';

    public static char indexFileSeparatorFilePositionToSearchIndicator = '-';

    public static char indexFileSeparatorIndexEntries = ',';

    public static int indexFileMaxSize;

    public static String prefixDictionaryFile = "directory";

    public static String suffixDictionaryFile = ".csv";

    public static String dictionaryCharEncoding;

    public static char dictionaryFileSeparationCharacter;

    public static int dictionaryFileMaxSize;

    public static String applicationFileNamePrefix = "DictionaryForMIDs";

    public static String propertyFileName = applicationFileNamePrefix + ".properties";

    public static char contentFontColourSeparationCharacter = ',';

    private static RGBColour backgroundColour;

    private static boolean useBackgroundColour;

    public static String dictionaryGenerationInputCharEncoding;

    public static char dictionaryGenerationSeparatorCharacter;

    public static long dictionaryGenerationMinNumberOfEntriesPerDictionaryFile;

    public static long dictionaryGenerationMinNumberOfEntriesPerIndexFile;

    public static boolean dictionaryGenerationOmitParFromIndex;

    public static String fileEncodingFormat;

    public static boolean useStandardPath = true;

    public static ObjectForClass objectForClassObj = new ObjectForClass();

    public static void initValues(boolean initDictionaryGenerationValues) throws DictionaryException {
        Util utilObj = Util.getUtil();
        utilObj.openProperties(getPathDataFiles());
        infoText = utilObj.getDictionaryPropertyString("infoText");
        dictionaryAbbreviation = utilObj.getDictionaryPropertyString("dictionaryAbbreviation", true);
        numberOfAvailableLanguages = utilObj.getDictionaryPropertyInt("numberOfAvailableLanguages");
        numberOfInputLanguages = 0;
        supportedLanguages = new LanguageDefinition[numberOfAvailableLanguages];
        for (int indexLanguage = 0; indexLanguage < numberOfAvailableLanguages; ++indexLanguage) {
            String indexLanguageString = String.valueOf(indexLanguage + 1);
            String languagePropertyPrefix = "language" + indexLanguageString;
            String languageDisplayText = utilObj.getDictionaryPropertyString(languagePropertyPrefix + "DisplayText");
            String languageFilePostfix = utilObj.getDictionaryPropertyString(languagePropertyPrefix + "FilePostfix");
            boolean isSearchable = utilObj.getDictionaryPropertyBooleanDefault(languagePropertyPrefix + "IsSearchable", true);
            if (isSearchable) ++numberOfInputLanguages;
            boolean hasSeparateDictionaryFile = utilObj.getDictionaryPropertyBooleanDefault(languagePropertyPrefix + "HasSeparateDictionaryFile", false);
            boolean generateIndex = utilObj.getDictionaryPropertyBooleanDefault(languagePropertyPrefix + "GenerateIndex", true);
            String normationClassName = utilObj.getDictionaryPropertyString(languagePropertyPrefix + "NormationClassName", true);
            String dictionaryUpdateClassName = utilObj.getDictionaryPropertyString(languagePropertyPrefix + "DictionaryUpdateClassName", true);
            int indexNumberOfSourceEntries = utilObj.getDictionaryPropertyIntDefault(languagePropertyPrefix + "IndexNumberOfSourceEntries", -1);
            String expressionSplitString = null;
            if (initDictionaryGenerationValues) {
                expressionSplitString = utilObj.getDictionaryPropertyString("dictionaryGenerationLanguage" + indexLanguageString + "ExpressionSplitString", true);
            }
            String languageIcon = null;
            boolean contentDefinitionAvailable = false;
            ContentDefinition content[] = null;
            int numberOfContentDeclarations = 1;
            String numberOfContentDeclarationsString = utilObj.getDictionaryPropertyString(languagePropertyPrefix + "NumberOfContentDeclarations", true);
            if (numberOfContentDeclarationsString != null) {
                numberOfContentDeclarations = Integer.valueOf(numberOfContentDeclarationsString).intValue() + 1;
                contentDefinitionAvailable = true;
            }
            content = new ContentDefinition[numberOfContentDeclarations];
            content[0] = PredefinedContent.getContentNoDefinitionProvided();
            for (int indexContent = 0; indexContent < numberOfContentDeclarations; ++indexContent) {
                String indexContentString = String.valueOf(indexContent);
                if (indexContentString.length() == 1) indexContentString = '0' + indexContentString; else if (indexContentString.length() > 2) throw new DictionaryException("Number of contents too big");
                String contentPropertyPrefix = languagePropertyPrefix + "Content" + indexContentString;
                ContentDefinition contentFromPropertyFile;
                String contentDisplayTextProperty = contentPropertyPrefix + "DisplayText";
                String contentDisplayText = utilObj.getDictionaryPropertyString(contentDisplayTextProperty, true);
                if (contentDisplayText == null) {
                    if (indexContent == 0) {
                        continue;
                    } else {
                        utilObj.propertyNotFound(contentDisplayTextProperty);
                    }
                }
                if (contentDisplayText.startsWith(PredefinedContent.predefinedContentNamePrefix)) {
                    contentFromPropertyFile = PredefinedContent.getPredefinedContent(contentDisplayText);
                } else {
                    contentFromPropertyFile = new ContentDefinition(contentDisplayText);
                }
                String fontColourString = utilObj.getDictionaryPropertyString(contentPropertyPrefix + "FontColour", true);
                RGBColour fontColour;
                if (fontColourString != null) {
                    fontColour = determineRGBColourFromProperty(fontColourString, contentPropertyPrefix);
                    contentFromPropertyFile.setFontColour(fontColour);
                }
                String fontStyleString = utilObj.getDictionaryPropertyString(contentPropertyPrefix + "FontStyle", true);
                if (fontStyleString != null) {
                    FontStyle fontStyle = null;
                    if (Util.stringEqualIgnoreCase(fontStyleString, FontStyle.plainString)) fontStyle = PredefinedContent.fontStylePlain; else if (Util.stringEqualIgnoreCase(fontStyleString, FontStyle.underlinedString)) fontStyle = PredefinedContent.fontStyleUnderlined; else if (Util.stringEqualIgnoreCase(fontStyleString, FontStyle.boldString)) fontStyle = PredefinedContent.fontStyleBold; else if (Util.stringEqualIgnoreCase(fontStyleString, FontStyle.italicString)) fontStyle = PredefinedContent.fontStyleItalic; else throwContentException("Incorrect font style", contentPropertyPrefix);
                    contentFromPropertyFile.setFontStyle(fontStyle);
                }
                String displaySelectablePropertyName = new String(contentPropertyPrefix + "DisplaySelectable");
                String displaySelectableString = utilObj.getDictionaryPropertyString(displaySelectablePropertyName, true);
                if (displaySelectableString != null) {
                    boolean displaySelectable = utilObj.getBooleanFromProperty(displaySelectablePropertyName, displaySelectableString);
                    contentFromPropertyFile.setDisplaySelectable(displaySelectable);
                }
                String selectionModeString = utilObj.getDictionaryPropertyString(contentPropertyPrefix + "SelectionMode", true);
                if (selectionModeString != null) {
                    SelectionMode selectionMode = null;
                    if (Util.stringEqualIgnoreCase(selectionModeString, SelectionMode.noneString)) selectionMode = PredefinedContent.selectionModeNone; else if (Util.stringEqualIgnoreCase(selectionModeString, SelectionMode.singleString)) selectionMode = PredefinedContent.selectionModeSingle; else if (Util.stringEqualIgnoreCase(selectionModeString, SelectionMode.allString)) selectionMode = PredefinedContent.selectionModeAll; else throwContentException("Incorrect selection mode", contentPropertyPrefix);
                    contentFromPropertyFile.setSelectionMode(selectionMode);
                }
                content[indexContent] = contentFromPropertyFile;
            }
            supportedLanguages[indexLanguage] = new LanguageDefinition(languageDisplayText, languageFilePostfix, isSearchable, hasSeparateDictionaryFile, normationClassName, indexNumberOfSourceEntries, contentDefinitionAvailable, content, dictionaryUpdateClassName, generateIndex, expressionSplitString, languageIcon);
        }
        searchListCharEncoding = utilObj.getDictionaryPropertyString("searchListCharEncoding");
        searchListCharEncoding = utilObj.getDeviceCharEncoding(searchListCharEncoding);
        searchListFileSeparationCharacter = utilObj.getDictionaryPropertyChar("searchListFileSeparationCharacter");
        searchListFileMaxSize = utilObj.getDictionaryPropertyIntDefault("searchListFileMaxSize", 10000);
        indexCharEncoding = utilObj.getDictionaryPropertyString("indexCharEncoding");
        indexCharEncoding = utilObj.getDeviceCharEncoding(indexCharEncoding);
        indexFileSeparationCharacter = utilObj.getDictionaryPropertyChar("indexFileSeparationCharacter");
        indexFileMaxSize = utilObj.getDictionaryPropertyIntDefault("indexFileMaxSize", 10000);
        dictionaryCharEncoding = utilObj.getDictionaryPropertyString("dictionaryCharEncoding");
        dictionaryCharEncoding = utilObj.getDeviceCharEncoding(dictionaryCharEncoding);
        dictionaryFileSeparationCharacter = utilObj.getDictionaryPropertyChar("dictionaryFileSeparationCharacter");
        dictionaryFileMaxSize = utilObj.getDictionaryPropertyIntDefault("dictionaryFileMaxSize", 10000);
        String backgroundColourProperty = "backgroundColour";
        String backgroundColourString = utilObj.getDictionaryPropertyString(backgroundColourProperty, true);
        if (backgroundColourString == null) {
            setUseBackgroundColour(false);
            setBackgroundColour(null);
        } else {
            setUseBackgroundColour(true);
            if (Util.stringEqualIgnoreCase(backgroundColourString, PredefinedContent.backgroundColourDefaultName)) {
                setBackgroundColour(PredefinedContent.backgroundColourDefault);
            } else {
                setBackgroundColour(determineRGBColourFromProperty(backgroundColourString, backgroundColourProperty));
            }
        }
        if (initDictionaryGenerationValues) {
            dictionaryGenerationInputCharEncoding = utilObj.getDictionaryPropertyStringDefault("dictionaryGenerationInputCharEncoding", "ISO-8859-1");
            dictionaryGenerationSeparatorCharacter = utilObj.getDictionaryPropertyCharDefault("dictionaryGenerationSeparatorCharacter", '\t');
            dictionaryGenerationMinNumberOfEntriesPerDictionaryFile = utilObj.getDictionaryPropertyIntDefault("dictionaryGenerationMinNumberOfEntriesPerDictionaryFile", 200);
            dictionaryGenerationMinNumberOfEntriesPerIndexFile = utilObj.getDictionaryPropertyIntDefault("dictionaryGenerationMinNumberOfEntriesPerIndexFile", 500);
            dictionaryGenerationOmitParFromIndex = utilObj.getDictionaryPropertyBooleanDefault("dictionaryGenerationOmitParFromIndex", true);
        }
        fileEncodingFormat = utilObj.getDictionaryPropertyString("fileEncodingFormat", true);
        if (fileEncodingFormat == null) {
            fileEncodingFormat = new String("plain_format1");
        }
        for (int indexLanguage = 0; indexLanguage < numberOfAvailableLanguages; ++indexLanguage) {
            String normationClassName = supportedLanguages[indexLanguage].normationClassName;
            Normation normationObj = (Normation) getObjectForClass(normationClassName, Normation.class.getName(), "de.kugihan.dictionaryformids.translation.normation", "de.kugihan.dictionaryformids.translation");
            supportedLanguages[indexLanguage].normationObj = normationObj;
            if (initDictionaryGenerationValues) {
                String dictionaryUpdateClassName = supportedLanguages[indexLanguage].dictionaryUpdateClassName;
                DictionaryUpdateIF dictionaryUpdateObj = (DictionaryUpdateIF) getObjectForClassDynamic(dictionaryUpdateClassName, "de.kugihan.dictionaryformids.dictgen.dictionaryupdate.DictionaryUpdate", "de.kugihan.dictionaryformids.dictgen.dictionaryupdate", "de.kugihan.dictionaryformids.dictgen");
                supportedLanguages[indexLanguage].dictionaryUpdateObj = dictionaryUpdateObj;
            }
        }
    }

    protected static Object getObjectForClass(String className, String fallbackClassName, String newPackageName, String oldPackageName) throws DictionaryException {
        if (className == null) {
            className = fallbackClassName;
        }
        Object classObj;
        classObj = objectForClassObj.createObjectForClass(className);
        if (classObj == null) {
            StringBuffer classNameNewPackage = new StringBuffer(className);
            classNameNewPackage.delete(0, oldPackageName.length());
            String classNameNewPackageStr = newPackageName + classNameNewPackage.toString();
            classObj = objectForClassObj.createObjectForClass(classNameNewPackageStr);
            if (classObj == null) {
                throw new DictionaryClassNotLoadedException("Class could not be loaded: " + className);
            }
        }
        return classObj;
    }

    protected static Object getObjectForClassDynamic(String className, String fallbackClassName, String newPackageName, String oldPackageName) throws DictionaryException {
        if (className == null) {
            className = fallbackClassName;
        }
        Class classToLoad;
        Object classObj;
        try {
            classToLoad = Class.forName(className);
            classObj = classToLoad.newInstance();
        } catch (Exception e) {
            try {
                StringBuffer classNameNewPackage = new StringBuffer(className);
                classNameNewPackage.delete(0, oldPackageName.length());
                String classNameNewPackageStr = newPackageName + classNameNewPackage.toString();
                classToLoad = Class.forName(classNameNewPackageStr);
                classObj = classToLoad.newInstance();
            } catch (Exception e2) {
                throw new DictionaryClassNotLoadedException("Class could not be loaded: " + className);
            }
        }
        return classObj;
    }

    public static int determineColourComponent(String rbgString, String propertyName) throws DictionaryException {
        String rbgStringTrimmed = rbgString.trim();
        int rgbValue = 0;
        try {
            rgbValue = Integer.parseInt(rbgStringTrimmed);
        } catch (NumberFormatException e) {
            throwContentException("RGB value is incorrect: " + e.getMessage(), propertyName);
        }
        if (rgbValue > RGBColour.maxRBGColourValue) throwContentException("RGB value is bigger than " + RGBColour.maxRBGColourValue, propertyName);
        return rgbValue;
    }

    protected static RGBColour determineRGBColourFromProperty(String fontColourString, String propertyName) throws DictionaryException {
        String[] fontColourStringElements = Util.stringSplit(fontColourString, contentFontColourSeparationCharacter);
        if (fontColourStringElements.length != 3) {
            throwContentException("3 components reqired for font colour (red, green, blue)", propertyName);
        }
        int red = determineColourComponent(fontColourStringElements[0], propertyName);
        int green = determineColourComponent(fontColourStringElements[1], propertyName);
        int blue = determineColourComponent(fontColourStringElements[2], propertyName);
        return new RGBColour(red, green, blue);
    }

    public static String getDisplayText(String propertyName) throws DictionaryException {
        Util utilObj = Util.getUtil();
        String displayText;
        String displayTextString = utilObj.getDictionaryPropertyString(propertyName, true);
        if (displayTextString != null) {
            if ((displayTextString.charAt(0) != '\"') || (displayTextString.charAt(displayTextString.length() - 1)) != '\"') {
                throwContentException("String must start with \" and end with \"", propertyName);
            }
            displayText = displayTextString.substring(1, displayTextString.length() - 1);
            if (displayText.indexOf('\"') != -1) {
                throwContentException("String must not contain a \"-character", propertyName);
            }
        } else {
            displayText = null;
        }
        return displayText;
    }

    protected static void throwContentException(String message, String propertyName) throws DictionaryException {
        throw new DictionaryException(propertyName + ": " + message);
    }

    public static void setDictionaryNotAvailable() {
        numberOfAvailableLanguages = 0;
    }

    public static String getPathDataFiles() {
        if (useStandardPath) return "/" + pathNameDataFiles + "/"; else return "";
    }

    public static String getDfMPropertyFileLocation(String dfmBaseDirectory) {
        return dfmBaseDirectory.toString() + getPathDataFiles() + propertyFileName;
    }

    public static RGBColour getBackgroundColour() {
        return backgroundColour;
    }

    public static void setBackgroundColour(RGBColour backgroundColour) {
        DictionaryDataFile.backgroundColour = backgroundColour;
    }

    public static boolean isUseBackgroundColour() {
        return useBackgroundColour;
    }

    public static void setUseBackgroundColour(boolean useBackgroundColour) {
        DictionaryDataFile.useBackgroundColour = useBackgroundColour;
    }
}
