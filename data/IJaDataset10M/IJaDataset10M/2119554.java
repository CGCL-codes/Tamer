package cat.jm.languages.service.impl;

import cat.jm.languages.model.Vocabulary;
import cat.jm.languages.service.VocabularyService;
import cat.jm.languages.service.generic.GenericService;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An class that provides a data management to the Vocabulary table.
 */
@Service(value = "vocabularyService")
public class VocabularyServiceImpl extends GenericService implements VocabularyService {

    private static final Logger logger = LoggerFactory.getLogger(VocabularyServiceImpl.class);

    /**
	 * Get a Vocabulary Object resource by id.
	 *
	 * @param id The data object to create.
     * @return Vocabulary Object instance
	 */
    public Vocabulary find(Integer id) {
        if (id == null) throw new IllegalArgumentException("Object Id can not be null");
        return getVocabularyDao().find(id);
    }

    /**
	 * Create a Vocabulary resource.
	 *
	 * @param vocabulary The data object.
	 */
    public void create(Vocabulary vocabulary) {
        if (vocabulary == null) throw new IllegalArgumentException("vocabulary can not be null");
        getVocabularyDao().create(vocabulary);
    }

    /**
	 * Delete a Vocabulary resource.
	 *
	 * @param vocabulary The data object.
	 */
    public void remove(Vocabulary vocabulary) {
        if (vocabulary == null) throw new IllegalArgumentException("vocabulary can not be null");
        getVocabularyDao().remove(vocabulary);
    }

    /**
	 * Delete a Vocabulary resource by Id.
	 *
	 * @param id The id data object.
	 */
    public void remove(Integer id) {
        if (id == null) throw new IllegalArgumentException("Vocabulary Id can not be null");
        getVocabularyDao().remove(id);
    }

    /**
	 * Update a Vocabulary resource.
	 *
	 * @param vocabulary The data object.
	 */
    public void update(Vocabulary vocabulary) {
        if (vocabulary == null) throw new IllegalArgumentException("vocabulary can not be null");
        getVocabularyDao().update(vocabulary);
    }

    /**
	 * Search a Vocabulary list resource.
     * @return A Vocabulary list resource
	 */
    public List<Vocabulary> get() {
        return getVocabularyDao().get();
    }

    /**
	 * Search a Vocabulary list resource.
	 *
	 * @param index The page number.
     * @param maxResults The max results for page.
     * @return A Vocabulary list resource
	 */
    public List<Vocabulary> get(Integer index, Integer maxResults) {
        return getVocabularyDao().get(index, maxResults);
    }
}
