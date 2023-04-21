package sk.sigp.tetras.dao.websearch;

import java.util.List;
import sk.sigp.tetras.dao.general.GenericDao;
import sk.sigp.tetras.entity.VyhladavaciAlgoritmus;
import sk.sigp.tetras.entity.websearch.SearchCategory;

public interface SearchCategoryDao extends GenericDao<SearchCategory, Long> {

    /**
	 * will delete all search categories for selected algorithm
	 * cascading will affect
	 * searched
	 * searchprefs
	 * searchplan
	 */
    void deleteAllCascade(VyhladavaciAlgoritmus algorithm);

    /**
	 * will delete search category
	 * cascading will affect
	 * searched
	 * searchprefs
	 * searchplan
	 */
    void deleteCascade(SearchCategory category);

    /**
	 * will return all search categories for seelected algorithm
	 * if algorithm passed as parameter is null 
	 * method will return empty list
	 * @return
	 */
    List<SearchCategory> findAll(VyhladavaciAlgoritmus algorithm);

    /**
	 * will return all search categories with selected code and algotirthm
	 * if algorithm passed as parameter is null 
	 * method will return empty list
	 * @return
	 */
    SearchCategory findByCode(String code, VyhladavaciAlgoritmus algorithm);

    /**
	 * will return all search categories with selected name and algotirthm
	 * if algorithm passed as parameter is null 
	 * method will return empty list
	 * @return
	 */
    SearchCategory findByName(String name, VyhladavaciAlgoritmus algorithm);

    /**
	 * returns all search categories registered in system regardless of algorithm
	 * @return
	 */
    List<SearchCategory> findAll();
}
