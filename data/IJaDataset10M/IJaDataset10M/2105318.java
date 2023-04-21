package snipsnap.versioning.cookbook;

import snipsnap.api.versioning.DifferenceService;
import java.util.List;

/**
 * Returns differences between two text strings
 * and implements the DifferenceService component inferface
 *
 * @author Stephan J. Schmidt
 * @version $Id: CookbookDifferenceService.java 1877 2006-08-18 15:04:46Z leo $
 */
public class CookbookDifferenceService implements DifferenceService {

    public CookbookDifferenceService() {
    }

    public List diff(String text1, String text2) {
        CookbookDiff diff = new CookbookDiff();
        return diff.diff(text1, text2);
    }
}
