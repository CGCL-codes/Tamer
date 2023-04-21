package cat.jm.languages.facade;

import cat.jm.languages.model.PastExamples;
import java.util.List;
import javax.validation.Valid;

/**
 * An interface that provides a data management interface to the PastExamples table.
 */
public interface PastExamplesFacade {

    /**
	 * Get a PastExamples Object resource by id.
	 *
	 * @param id The data object to create.
     * @return PastExamples Object instance
	 */
    public PastExamples find(Integer id);

    /**
	 * Create a PastExamples resource.
	 *
	 * @param pastExamples The data object.
	 */
    public void create(@Valid PastExamples pastExamples);

    /**
	 * Delete a PastExamples resource.
	 *
	 * @param pastExamples The data object.
	 */
    public void remove(PastExamples pastExamples);

    /**
	 * Delete a PastExamples resource by Id.
	 *
	 * @param id The id data object.
	 */
    public void remove(Integer id);

    /**
	 * Update a PastExamples resource.
	 *
	 * @param pastExamples The data object.
	 */
    public void update(@Valid PastExamples pastExamples);

    /**
	 * Search a PastExamples list resource.
     * @return A PastExamples list resource
	 */
    public List<PastExamples> get();

    /**
	 * Search a PastExamples list resource.
	 *
	 * @param index The page number.
     * @param maxResults The max results for page.
     * @return A PastExamples list resource
	 */
    public List<PastExamples> get(Integer index, Integer maxResults);
}
