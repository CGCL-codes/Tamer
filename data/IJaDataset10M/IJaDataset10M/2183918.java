package de.kugihan.dictionaryformids.dictgen.dictionaryupdate;

import java.util.Vector;
import de.kugihan.dictionaryformids.dataaccess.content.ContentLib;
import de.kugihan.dictionaryformids.dictgen.IndexKeyWordEntry;
import de.kugihan.dictionaryformids.general.DictionaryException;

public class DictionaryUpdateFreedictDeuEngGer extends DictionaryUpdate {

    public String updateDictionaryExpression(String dictionaryExpression) throws DictionaryException {
        return DictionaryUpdateLib.setContentPronounciation(super.updateDictionaryExpression(dictionaryExpression), 1);
    }

    public String removeNonSearchParts(String expression) {
        return DictionaryUpdateLib.removeBrackets(expression);
    }

    public void updateKeyWordVector(Vector keyWordVector) throws DictionaryException {
        super.updateKeyWordVector(keyWordVector);
        int elementCount = 0;
        if (keyWordVector.size() > 1) {
            do {
                String keyWord = ((IndexKeyWordEntry) keyWordVector.elementAt(elementCount)).keyWord;
                if (keyWord.equalsIgnoreCase("npl") || keyWord.equalsIgnoreCase("nsf") || keyWord.equalsIgnoreCase("nsm") || keyWord.equalsIgnoreCase("nsn") || keyWord.equalsIgnoreCase("nss")) {
                    keyWordVector.removeElementAt(elementCount);
                } else {
                    ++elementCount;
                }
            } while (elementCount < keyWordVector.size());
        }
    }
}
