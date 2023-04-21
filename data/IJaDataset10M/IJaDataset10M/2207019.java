package pt.igeo.snig.mig.editor.list;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.list.exception.InitializingException;
import pt.igeo.snig.mig.editor.list.exception.MalformedListValuesFileException;
import pt.igeo.snig.mig.editor.list.exception.ParsingListValuesFileException;
import pt.igeo.snig.mig.editor.record.filter.SimpleFileFilter;

/**
 * Singleton class for handling all the FixedList data read from the
 * XML files present at the data.lists package.
 * @see #getFixedLists(String)
 * @see #getFixedListItem(String, String)
 * @see #getFixedListItemById(String, String)
 * 
 * @author Jos� Pedro Dias
 * @version $Revision: 11271 $
 * @since 1.0
 */
public class ListValueManager {

    /** Logger for this class */
    private static Logger logger = Logger.getLogger(ListValueManager.class);

    /** the map for storing option lists */
    private HashMap<String, HashMap<String, FixedList>> typeMap = new HashMap<String, HashMap<String, FixedList>>();

    /** the map for storing prefixes */
    private HashMap<String, String> prefixMap = new HashMap<String, String>();

    /** the map for storing namespaces */
    private HashMap<String, String> namespaceMap = new HashMap<String, String>();

    /** the map for storing translation keys - lang - */
    private HashMap<String, HashMap<String, HashMap<String, String>>> langKeyMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();

    /** current list language */
    private String currentLanguage = Constants.defaultListLanguage;

    /** the only instance */
    private static ListValueManager instance = null;

    /** private constructor so that only getInstance can create an instance */
    private ListValueManager() {
    }

    /**
	 * public interface for getting the instance
	 * 
	 * @return the only ListValueManager instance
	 */
    public static ListValueManager getInstance() {
        if (instance == null) {
            instance = new ListValueManager();
        }
        return instance;
    }

    /**
	 * Scans a directory for xml files of the supported type and recognizes lists of listvalues.
	 * 
	 * @param directory where to scan for xml files
	 * @throws InitializingException NOTE: use the message field to get a more detailed reason for failure.
	 * @throws MalformedListValuesFileException
	 * @throws ParsingListValuesFileException NOTE: use the message field to get a more detailed reason for failure.
	 */
    public void initialize(String directory) throws InitializingException, MalformedListValuesFileException, ParsingListValuesFileException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        File listsDir = new File(directory);
        if (!listsDir.isDirectory()) {
            throw new InitializingException("Can't find directory " + directory + "!");
        }
        Boolean valuesInitialized = false;
        File[] dirs = listsDir.listFiles();
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }
            File[] files = dir.listFiles(new SimpleFileFilter("xml"));
            HashMap<String, HashMap<String, String>> tempFileTransMap = new HashMap<String, HashMap<String, String>>();
            for (File f : files) {
                String fName = f.getName();
                int li = fName.lastIndexOf('_');
                fName = fName.substring(0, li);
                Document doc;
                try {
                    DocumentBuilder builder = docFactory.newDocumentBuilder();
                    doc = builder.parse(new File(f.toString()));
                } catch (Exception ex) {
                    throw new ParsingListValuesFileException(fName);
                }
                HashMap<String, FixedList> tempMap = null;
                HashMap<String, String> tempTransMap = new HashMap<String, String>();
                if (valuesInitialized == false) {
                    tempMap = new HashMap<String, FixedList>();
                }
                NodeList nl = doc.getElementsByTagName("fixed-list");
                int count = nl.getLength();
                if (count != 1) {
                    throw new MalformedListValuesFileException();
                }
                Element fixedListElement = (Element) nl.item(0);
                if (valuesInitialized == false) {
                    String prefix = fixedListElement.getAttribute("prefix");
                    if ((prefix != null) && (prefix.length() > 0)) {
                        prefixMap.put(fName, prefix);
                    }
                }
                if (valuesInitialized == false) {
                    String namespace = fixedListElement.getAttribute("namespace");
                    if ((namespace != null) && (namespace.length() > 0)) {
                        namespaceMap.put(fName, namespace);
                    } else {
                        namespaceMap.put(fName, "gmd");
                    }
                }
                nl = fixedListElement.getElementsByTagName("option");
                for (int i = 0; i < nl.getLength(); ++i) {
                    Element optionElement = (Element) nl.item(i);
                    String name = optionElement.getAttribute("name");
                    if (valuesInitialized == false) {
                        String id = optionElement.getAttribute("id");
                        FixedList lv = new FixedList(fName, id, name);
                        tempMap.put(name, lv);
                    }
                    String content = optionElement.getTextContent();
                    tempTransMap.put(name, fromCamelCase(content));
                }
                if (valuesInitialized == false) {
                    typeMap.put(fName, tempMap);
                }
                tempFileTransMap.put(fName, tempTransMap);
            }
            langKeyMap.put(dir.getName(), tempFileTransMap);
            valuesInitialized = true;
        }
    }

    /**
	 * 
	 * @param key
	 * @return a collection containing possible values for this fixed list item
	 */
    public Collection<FixedList> getFixedLists(String key) {
        HashMap<String, FixedList> tempMap = typeMap.get(key);
        if (tempMap == null) {
            int li = key.indexOf('_');
            String noPref = key.substring(li + 1);
            tempMap = typeMap.get(noPref);
            logger.debug("Searching " + key + " has " + noPref);
            if (tempMap == null) {
                return getFixedLists(Constants.emptyList);
            }
        }
        LinkedList<FixedList> ll = new LinkedList<FixedList>();
        ll.addAll(tempMap.values());
        Collections.sort(ll);
        return ll;
    }

    /**
	 * 
	 * @param codeList
	 * @param codeValue
	 * @return the fixed list item from list value manager with matching code value
	 */
    public FixedList getFixedListItem(String codeList, String codeValue) {
        FixedList result = null;
        HashMap<String, FixedList> tempMap = typeMap.get(codeList);
        if (tempMap != null) {
            result = tempMap.get(codeValue);
        } else {
            int li = codeList.indexOf('_');
            String noPref = codeList.substring(li + 1);
            tempMap = typeMap.get(noPref);
            if (tempMap != null) {
                result = tempMap.get(codeValue);
            } else {
                tempMap = typeMap.get("Empty");
            }
        }
        if (result == null) {
            result = tempMap.values().iterator().next();
        }
        return result;
    }

    /**
	 * 
	 * @param codeList
	 * @param id
	 * @return the fixed list item from list value manager with matching code id
	 */
    public FixedList getFixedListItemById(String codeList, String id) {
        Collection<FixedList> fixedLists = this.getFixedLists(codeList);
        for (FixedList fl : fixedLists) {
            if (fl.getId().equals(id)) {
                return fl;
            }
        }
        return null;
    }

    /**
	 * Returns the prefixed name of given code list
	 * 
	 * @param list
	 * @return prefixed name of given codelist
	 */
    public String getPrefixedName(String list) {
        String prefix = prefixMap.get(list);
        if (prefix == null) {
            return list;
        } else {
            return prefix + "_" + list;
        }
    }

    /**
	 * @return the currentLanguage
	 */
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
	 * @param currentLanguage the currentLanguage to set
	 */
    public void setCurrentLanguage(String currentLanguage) {
        if ((currentLanguage != null) && (langKeyMap.containsKey(currentLanguage))) {
            this.currentLanguage = currentLanguage;
        } else {
            this.currentLanguage = Constants.defaultListLanguage;
        }
    }

    /**
	 * Get the namespace entry from map
	 * 
	 * @param codeList
	 * @return the namespace string
	 */
    public String getNameSpace(String codeList) {
        String namespace = namespaceMap.get(codeList);
        if (namespace == null) {
            int li = codeList.indexOf('_');
            String noPref = codeList.substring(li + 1);
            namespace = namespaceMap.get(noPref);
            if (namespace == null) {
                return "gmd";
            }
        }
        return namespace;
    }

    /**
	 * Checks for existence of given item in given list
	 * 
	 * @param codeList
	 * @param codeValue
	 * @return true if item exists in given list
	 */
    public boolean exists(String codeList, String codeValue) {
        HashMap<String, FixedList> tempMap = typeMap.get(codeList);
        if (tempMap != null) {
            return tempMap.containsKey(codeValue);
        } else {
            return false;
        }
    }

    /**
	 * Converts thisIsGood to this is good
	 * 
	 * @param s a camelCase-like string
	 * @return the normalized string
	 */
    private String fromCamelCase(String s) {
        String s2 = new String();
        boolean isBeginWord = true;
        boolean camelCase = true;
        if (s == null) {
            return null;
        }
        for (int i = 0; i < s.length(); ++i) {
            Character c = s.charAt(i);
            if (isBeginWord) {
                if (Character.isUpperCase(c)) {
                    camelCase = false;
                    s2 += c;
                    isBeginWord = false;
                } else {
                    if (!Character.isSpaceChar(c)) {
                        s2 += " " + Character.toLowerCase(c);
                        isBeginWord = false;
                    } else {
                        s2 += c;
                    }
                }
            } else {
                if (camelCase && (Character.isUpperCase(c))) {
                    s2 += " " + Character.toLowerCase(c);
                    continue;
                }
                if (Character.isSpaceChar(c)) {
                    isBeginWord = true;
                    camelCase = true;
                }
                s2 += c;
            }
        }
        return new String(s2.trim());
    }

    /**
	 * This is very ugly code. TODO: All fixed list items MUST NOT HAVE the associated prefix name
	 * 
	 * @param codeList
	 * @param codeValue
	 * @return the translated string
	 */
    public String translateItem(String codeList, String codeValue) {
        HashMap<String, HashMap<String, String>> keys = langKeyMap.get(currentLanguage);
        String res = null;
        if (keys == null) {
            return Constants.defaultEmptyValue;
        } else {
            HashMap<String, String> keys2 = keys.get(codeList);
            if (keys2 == null) {
                int li = codeList.indexOf('_');
                String noPref = codeList.substring(li + 1);
                keys2 = keys.get(noPref);
                if (keys2 == null) {
                    return Constants.defaultEmptyValue;
                }
            }
            res = keys2.get(codeValue);
            if (res == null) {
                return Constants.defaultEmptyValue;
            }
        }
        return res;
    }

    /**
	 * Translates por -> pt, eng -> en
	 * 
	 * @param longLanguage
	 * @return the shortened language abreviation
	 */
    public String getShortLanguage(String longLanguage) {
        return langKeyMap.get(Constants.defaultListLanguage).get(Constants.languageCodeList).get(longLanguage);
    }

    /**
	 * Translates por -> locale-pt, eng -> locale-en
	 * 
	 * @param longLanguage
	 * @return the shortened language abreviation
	 */
    public String getLocaleLanguage(String longLanguage) {
        return typeMap.get(Constants.languageCodeList).get(longLanguage).getId();
    }

    /**
	 * Finds the foreign language
	 * 
	 * @param languageCodeValue
	 * @return the foreign language
	 */
    public String getForeignLanguage(String languageCodeValue) {
        if (languageCodeValue.equals(Constants.defaultListLanguage)) {
            return "eng";
        } else {
            return Constants.defaultListLanguage;
        }
    }

    /**
	 * Searches for an item with given text (uses current language)
	 * 
	 * @param measureNameList
	 * @param text
	 * @return the item found or null otherwise
	 */
    public FixedList getMatch(String measureNameList, String text) {
        HashMap<String, String> tempLang = langKeyMap.get(currentLanguage).get(measureNameList);
        if (tempLang.containsValue(text)) {
            for (String test : tempLang.keySet()) {
                String test2 = tempLang.get(test);
                if (test2.equals(text)) {
                    return typeMap.get(measureNameList).get(test);
                }
            }
        }
        return null;
    }
}
