package ru.newton.pokertrainer.businesslogic.databaseinterface.dictionary;

/**
 * @author newton
 *         Date: Dec 22, 2010
 *         Time: 12:39:24 PM
 */
public class DictionaryLimitType_OLD {

    private final String listNamesDictionary[] = { "No Limit", "Fixed Limit", "Pot Limit" };

    private final int listIdDictionary[] = { 1, 2, 3 };

    DictionaryTools dictionaryTools = new DictionaryTools();

    public DictionaryLimitType_OLD() {
        dictionaryTools.setListNamesDictionary(listNamesDictionary);
        dictionaryTools.setListIdDictionary(listIdDictionary);
    }

    public String getName(final int requiredId) {
        String returnedName = "";
        returnedName = dictionaryTools.getName(requiredId);
        return returnedName;
    }

    public int getId(final String requiredName) {
        int returnedId = -1;
        returnedId = dictionaryTools.getId(requiredName);
        return returnedId;
    }

    public int findNameInString(String testedTextLine) {
        int returnedId = -1;
        returnedId = dictionaryTools.findNameInString(testedTextLine);
        return returnedId;
    }
}
