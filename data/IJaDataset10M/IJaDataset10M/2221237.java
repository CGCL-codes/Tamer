package cat.jm.languages.facade.impl;

import cat.jm.languages.model.PastExamples;
import cat.jm.languages.facade.PastExamplesFacade;
import cat.jm.languages.facade.generic.GenericFacade;
import org.springframework.stereotype.Component;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An class that provides a data management to the PastExamples table.
 */
@Component(value = "pastExamplesFacade")
public class PastExamplesFacadeImpl extends GenericFacade implements PastExamplesFacade {

    private static final Logger logger = LoggerFactory.getLogger(PastExamplesFacadeImpl.class);

    /**
	 * Get a PastExamples Object resource by id.
	 *
	 * @param id The data i object to find.
     * @return PastExamples Object instance
	 */
    public PastExamples find(Integer id) {
        if (id == null) throw new IllegalArgumentException("Object Id can not be null");
        return getPastExamplesService().find(id);
    }

    /**
	 * Create a PastExamples resource.
	 *
	 * @param pastExamples The data object.
	 */
    public void create(@Valid PastExamples pastExamples) {
        if (pastExamples == null) throw new IllegalArgumentException("pastExamples can not be null");
        getPastExamplesService().create(pastExamples);
    }

    /**
	 * Delete a PastExamples resource.
	 *
	 * @param pastExamples The data object.
	 */
    public void remove(PastExamples pastExamples) {
        if (pastExamples == null) throw new IllegalArgumentException("pastExamples can not be null");
        getPastExamplesService().remove(pastExamples);
    }

    /**
	 * Delete a PastExamples resource by Id.
	 *
	 * @param id The id data object.
	 */
    public void remove(Integer id) {
        if (id == null) throw new IllegalArgumentException("PastExamples Id can not be null");
        getPastExamplesService().remove(id);
    }

    /**
	 * Update a PastExamples resource.
	 *
	 * @param pastExamples The data object.
	 */
    public void update(@Valid PastExamples pastExamples) {
        if (pastExamples == null) throw new IllegalArgumentException("pastExamples can not be null");
        getPastExamplesService().update(pastExamples);
    }

    /**
	 * Search a PastExamples list resource.
     * @return A PastExamples list resource
	 */
    public List<PastExamples> get() {
        return getPastExamplesService().get();
    }

    /**
	 * Search a PastExamples list resource.
	 *
	 * @param index The page number.
     * @param maxResults The max results for page.
     * @return A PastExamples list resource
	 */
    public List<PastExamples> get(Integer index, Integer maxResults) {
        return getPastExamplesService().get(index, maxResults);
    }
}
