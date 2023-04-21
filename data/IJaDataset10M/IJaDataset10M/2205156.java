package cunei.corpus;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LanguagePairs {

    private static class LanguagePairIterator implements Iterator<LanguagePair> {

        private Iterator<Language> sourceLanguageIterator;

        private Iterator<Language> targetLanguageIterator;

        private Language sourceLanguage;

        private boolean hasNext;

        public LanguagePairIterator() {
            sourceLanguageIterator = Languages.values().iterator();
            hasNext = Languages.size() > 1;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public LanguagePair next() {
            if (!hasNext) throw new NoSuchElementException();
            if (sourceLanguage == null || !targetLanguageIterator.hasNext()) {
                sourceLanguage = sourceLanguageIterator.next();
                targetLanguageIterator = Languages.values().iterator();
            }
            Language targetLanguage = targetLanguageIterator.next();
            if (targetLanguage == sourceLanguage) targetLanguage = targetLanguageIterator.next();
            hasNext = sourceLanguage.getId() != Languages.size() - 1 || targetLanguage.getId() != Languages.size() - 2;
            return new LanguagePair(sourceLanguage, targetLanguage);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static synchronized LanguagePair valueOf(String name) {
        if (name != null) {
            String[] languages = name.split("-");
            if (languages.length == 2) return new LanguagePair(Languages.valueOf(languages[0]), Languages.valueOf(languages[1]));
        }
        throw new RuntimeException("Unknown language pair: " + name + " (valid options are " + values() + ")");
    }

    public static synchronized Iterable<LanguagePair> values() {
        return new Iterable<LanguagePair>() {

            public Iterator<LanguagePair> iterator() {
                return new LanguagePairIterator();
            }
        };
    }

    protected static synchronized int getId(Language sourceLanguage, Language targetLanguage) {
        final int sourceLanguageId = sourceLanguage.getId();
        int targetLanguageId = targetLanguage.getId();
        if (targetLanguageId > sourceLanguageId) targetLanguageId--;
        return sourceLanguageId * (Languages.size() - 1) + targetLanguageId;
    }

    public static synchronized int size() {
        final int size = Languages.size();
        return (size - 1) * size;
    }
}
