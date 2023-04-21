package ru.newton.pokertrainer.businesslogic.databaseinterface.dictionary;

/**
 * @author newton
 *         Date: Dec 20, 2010
 *         Time: 5:55:22 PM
 */
public class DictionaryTools {

    private String listNamesDictionary[] = null;

    private int listIdDictionary[] = null;

    public DictionaryTools() {
    }

    public DictionaryTools(final String[] newListNamesDictionary, final int[] newListIdDictionary) {
        this.listNamesDictionary = newListNamesDictionary;
        this.listIdDictionary = newListIdDictionary;
    }

    public String[] getListNamesDictionary() {
        return listNamesDictionary;
    }

    public void setListNamesDictionary(String[] newListNamesDictionary) {
        this.listNamesDictionary = newListNamesDictionary;
    }

    public int[] getListIdDictionary() {
        return listIdDictionary;
    }

    public void setListIdDictionary(int[] newListIdDictionary) {
        this.listIdDictionary = newListIdDictionary;
    }

    public int getId(final String requiredName) {
        int returnedId = -1;
        for (int i = 0; i < listNamesDictionary.length; i++) {
            if (requiredName.equals(listNamesDictionary[i])) {
                returnedId = listIdDictionary[i];
            }
        }
        return returnedId;
    }

    public String getName(final int requiredId) {
        String returnedName = "";
        for (int i = 0; i < listNamesDictionary.length; i++) {
            if (requiredId == listIdDictionary[i]) {
                returnedName = listNamesDictionary[i];
            }
        }
        return returnedName;
    }

    public int findNameInString(final String testedTextLine) {
        int occurrenceIndex = -1;
        int returnedId = -1;
        for (int i = 0; i < listNamesDictionary.length; i++) {
            occurrenceIndex = testedTextLine.indexOf(listNamesDictionary[i]);
            if (occurrenceIndex >= 0) {
                returnedId = listIdDictionary[i];
            }
        }
        return returnedId;
    }

    public boolean findId(final int testId) {
        int i = 0;
        while ((i < listNamesDictionary.length) && (testId != listIdDictionary[i])) {
            i++;
        }
        boolean ifFind = (i < listNamesDictionary.length);
        return ifFind;
    }
}
