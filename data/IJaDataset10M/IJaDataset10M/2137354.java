package at.ac.lbg.media.vis.framework.model;

import javax.xml.bind.annotation.XmlRegistry;
import at.ac.lbg.media.vis.framework.model.ArtworkType.Categories;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.ac.lbg.media.vis.framework.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.ac.lbg.media.vis.framework.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CategoryType }
     * 
     */
    public CategoryType createCategoryType() {
        return new CategoryType();
    }

    /**
     * Create an instance of {@link ArtworkType }
     * 
     */
    public ArtworkType createArtworkType() {
        return new ArtworkType();
    }

    /**
     * Create an instance of {@link ArtworkType.Categories }
     * 
     */
    public ArtworkType.Categories createArtworkTypeCategories() {
        return new ArtworkType.Categories();
    }

    /**
     * Create an instance of {@link ThemeRequest }
     * 
     */
    public ThemeRequest createThemeRequest() {
        return new ThemeRequest();
    }

    /**
	 * @param c Category instance to be translated
	 * @return categoryType initialized with values from c
	 */
    public CategoryType createCategoryType(Category c) {
        CategoryType ct = new CategoryType();
        ct.setId(c.getId());
        ct.setName(c.getName());
        if (c.getParent() != null) {
            ct.setParent(createCategoryType(c.getParent()));
        }
        return ct;
    }

    /**
	 * @param a Artwork instance to be translated
	 * @return artworkType initialized with values from a
	 */
    public ArtworkType createArtworkType(Artwork a) {
        ArtworkType at = new ArtworkType();
        at.setId(a.getId());
        at.setTitle(a.getTitle());
        at.setX(a.getX());
        at.setY(a.getY());
        Categories artworkTypeCategories = createArtworkTypeCategories();
        for (Category c : a.getCategories()) {
            artworkTypeCategories.getCategory().add(createCategoryType(c));
        }
        at.setCategories(artworkTypeCategories);
        return null;
    }
}
