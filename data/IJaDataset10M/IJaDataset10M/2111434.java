package spell.english;

import spell.services.DictionaryService;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * An implementation of the Dictionary service containing English words
 * see DictionaryService for details of the service.
 **/
public class EnglishDictionary implements DictionaryService {

    String[] m_dictionary = { "welcome", "to", "the", "Persist", "ipojo", "tutorial" };

    Integer myranking = 33;

    /**
     * Implements DictionaryService.checkWord(). Determines
     * if the passed in word is contained in the dictionary.
     * @param word the word to be checked.
     * @return true if the word is in the dictionary,
     *         false otherwise.
     **/
    public boolean checkWord(String word) {
        word = word.toLowerCase();
        for (int i = 0; i < m_dictionary.length; i++) {
            if (m_dictionary[i].equals(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implements DictionaryService.describes(). D
     * @return desrcibes the dictionary
     **/
    public String[] describe() {
        String description[] = m_dictionary;
        return description;
    }

    public void startUserSession(IDigitalPersonalIdentifier dpi) {
        System.out.println("Start English session for \"" + dpi + "\"");
    }

    public void stopUserSession(IDigitalPersonalIdentifier dpi) {
        System.out.println("End English  session for \"" + dpi + "\"");
    }
}
