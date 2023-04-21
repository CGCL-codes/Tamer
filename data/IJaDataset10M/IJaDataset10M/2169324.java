package dk.i2m.converge.ejb.facades;

import dk.i2m.converge.ejb.services.DataNotFoundException;
import dk.i2m.converge.core.metadata.Subject;
import dk.i2m.converge.core.metadata.Concept;
import dk.i2m.converge.nar.newsml.g2.power.KnowledgeItem;
import java.util.List;
import javax.ejb.Local;

/**
 * Local interface for the meta data facade enterprise bean.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface MetaDataFacadeLocal {

    /**
     * Stores a {@link Concept} in the database.
     *
     * @param concept
     *          {@link Concept} to store in the database
     * @return Created {@link Concept}
     */
    Concept create(Concept concept);

    /**
     * Updates a {@link Concept} in the database.
     *
     * @param concept
     *          {@link Concept} to update in the database
     * @return Updated {@link Concept}
     */
    Concept update(Concept concept);

    /**
     * Deletes a {@link Concept} from the database.
     *
     * @param id
     *          Unique identifier of the {@link Concept}
     */
    void deleteConcept(Long id);

    void delete(Class clazz, Long id);

    /**
     * Gets all {@link Concept}s.
     *
     * @return {@link List} of all {@link Concept}s.
     */
    List<Concept> getConcepts();

    /**
     * Finds a {@link Concept} by its unique identifier.
     *
     * @param id
     *          Unique identifier of the {@link Concept}
     * @return {@link Concept} matching the given <code>id</code>
     * @throws DataNotFoundException
     *          If no {@link Concept} matched the <code>id</code>
     */
    Concept findConceptById(Long id) throws DataNotFoundException;

    /**
     * Finds a {@link Concept} by its name;
     *
     * @param name
     *          Name of the {@link Concept}
     * @return {@link Concept} matching the <code>name</code>
     * @throws DataNotFoundException
     *          If no {@link Concept} matches the <code>name</code>
     *
     */
    Concept findConceptByName(String name) throws DataNotFoundException;

    /**
     * Gets a {@link List} of {@link Subject}s with a given <code>parent</code>.
     *
     * @param parent
     *          Parent of the {@link Subject}s to find
     * @return {@link List} of {@link Subject}s with the given <code>parent</code>.
     */
    List<Subject> findSubjectsByParent(Subject parent);

    List<Subject> findTopLevelSubjects();

    /**
     * Imports a NAR {@link KnowledgeItem} into the subject database.
     *
     * @param xml
     *          XML string
     * @param language
     *          Language which to import
     * @return Number of items imported
     */
    int importKnowledgeItem(String xml, String language);

    /**
     * Gets the available languages from a NewsML {@link KnowledgeItem}.
     * @param xml
     *          Raw NewsML
     * @return Number of items imported
     */
    String[] getLanguagesAvailableForImport(String xml);

    /**
     * Finds a {@link dk.i2m.converge.domain.meta.Concept} by its unique code.
     *
     * @param code
     *          Code of the {@link dk.i2m.converge.domain.meta.Concept}
     * @return {@link dk.i2m.converge.domain.meta.Concept} with the given code
     * @throws DataNotFoundException
     *          If a {@link dk.i2m.converge.domain.meta.Concept} could not be found with the given code
     */
    dk.i2m.converge.core.metadata.Concept findConceptByCode(java.lang.String code) throws DataNotFoundException;

    /**
     * Finds recently added {@link dk.i2m.converge.domain.meta.Concept}s.
     *
     * @param count
     *          Number of recent {@link dk.i2m.converge.domain.meta.Concept}s
     *          to return.
     * @return Recently added {@link dk.i2m.converge.domain.meta.Concept}s
     */
    java.util.List<dk.i2m.converge.core.metadata.Concept> findRecentConcepts(int count);

    /**
     * Searches the meta data database for concepts.
     *
     * @param search
     *          Search phrase
     * @return {@link Concept}s matching the search phrase
     */
    List<Concept> search(String search);

    /**
     * Find all {@link Concept}s of a given type.
     *
     * @param type
     *          Class of the concept
     * @return {@link List} of {@link Concept}s of the given {@code type}
     */
    List<Concept> findConceptByType(Class type);

    /**
     * Finds {@link Concept}s with a given {@link String} in their name.
     *
     * @param conceptName
     *          Part of the {@link Concept} name
     * @param types
     *          Type of {@link Concept}s to retrieve
     * @return {@link List} of {link Concept}s where {@code conceptName} is part
     *         of their name
     */
    List<Concept> findConceptsByName(String conceptName, Class... types);

    dk.i2m.converge.core.content.ContentTag findOrCreateContentTag(java.lang.String name);

    java.util.List<dk.i2m.converge.core.content.ContentTag> findContentTagLikeName(java.lang.String name);

    java.util.List<dk.i2m.converge.core.metadata.OpenCalaisMapping> getOpenCalaisMappings();

    dk.i2m.converge.core.metadata.OpenCalaisMapping create(dk.i2m.converge.core.metadata.OpenCalaisMapping mapping);

    dk.i2m.converge.core.metadata.OpenCalaisMapping update(dk.i2m.converge.core.metadata.OpenCalaisMapping mapping);

    void deleteOpenCalaisMapping(java.lang.Long id);

    dk.i2m.converge.core.metadata.Concept findOpenCalaisMapping(java.lang.String typeGroup, java.lang.String field, java.lang.String value) throws dk.i2m.converge.ejb.services.DataNotFoundException;
}
