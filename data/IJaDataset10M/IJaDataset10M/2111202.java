package openschool.domain.dao;

import java.util.List;
import openschool.domain.model.Cours;
import openschool.domain.model.Theme;

/**
 * DAO for the Theme table
 * 
 * @author remi
 *
 */
public interface ThemeDAO {

    /**
	 * Get a specific Theme
	 * @param idtheme
	 * @return a Theme
	 */
    Theme getTheme(Long idTheme);

    /**
	 * 
	 * @return the list of cours
	 */
    List<Theme> getAllTheme();

    /**
	 * Update a Theme
	 * @param Theme
	 */
    void updateTheme(Theme theme);

    /**
	 * Save a theme
	 * @param theme
	 */
    void saveTheme(Theme theme);

    /**
	 * 
	 * @param name
	 * @return the list of theme with the name name
	 */
    List<Theme> searchThemeByName(String name);

    /**
	 * 
	 * @param idTheme
	 * @return list of cours in relation with idTheme
	 */
    List<Cours> searchCoursByIdTheme(Long idTheme);
}
