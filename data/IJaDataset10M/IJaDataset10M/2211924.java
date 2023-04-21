package cat.jm.languages.adapter.impl;

import cat.jm.languages.model.Vocabulary;
import cat.jm.languages.adapter.VocabularyAdapter;
import cat.jm.languages.adapter.generic.GenericAdapter;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * An class that provides a data management to the Vocabulary table.
 */
@Component(value = "vocabularyAdapter")
public class VocabularyAdapterImpl extends GenericAdapter implements VocabularyAdapter {

    /**
	 * Get a Vocabulary Object resource by id.
	 *
	 * @param id The data object to create.
     * @return Vocabulary Object instance
	 */
    public Vocabulary find(Integer id) {
        if (id == null) throw new IllegalArgumentException("Object Id can not be null");
        return getVocabularyFacade().find(id);
    }

    /**
	 * Create a Vocabulary resource.
	 *
	 * @param vocabulary The data object.
	 */
    public void create(Vocabulary vocabulary) {
        if (vocabulary == null) throw new IllegalArgumentException("vocabulary can not be null");
        getVocabularyFacade().create(vocabulary);
    }

    /**
	 * Delete a Vocabulary resource.
	 *
	 * @param vocabulary The data object.
	 */
    public void remove(Vocabulary vocabulary) {
        if (vocabulary == null) throw new IllegalArgumentException("vocabulary can not be null");
        getVocabularyFacade().remove(vocabulary);
    }

    /**
	 * Delete a Vocabulary resource by Id.
	 *
	 * @param id The id data object.
	 */
    public void remove(Integer id) {
        if (id == null) throw new IllegalArgumentException("Vocabulary Id can not be null");
        getVocabularyFacade().remove(id);
    }

    /**
	 * Update a Vocabulary resource.
	 *
	 * @param vocabulary The data object.
	 */
    public void update(Vocabulary vocabulary) {
        if (vocabulary == null) throw new IllegalArgumentException("vocabulary can not be null");
        getVocabularyFacade().update(vocabulary);
    }

    /**
	 * Search a Vocabulary list resource.
     * @return A Vocabulary list resource
	 */
    public List<Vocabulary> get() {
        return getVocabularyFacade().get();
    }

    /**
	 * Search a Vocabulary list resource.
	 *
	 * @param index The page number.
     * @param maxResults The max results for page.
     * @return A Vocabulary list resource
	 */
    public List<Vocabulary> get(Integer index, Integer maxResults) {
        return getVocabularyFacade().get(index, maxResults);
    }
}
