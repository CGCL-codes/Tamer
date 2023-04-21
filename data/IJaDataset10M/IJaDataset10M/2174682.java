package course.lecture.slide;

import java.util.Comparator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.Entities;
import org.modelibra.IDomainModel;
import org.modelibra.ISelector;
import org.modelibra.Oid;
import org.modelibra.PropertySelector;
import course.lecture.presentation.Presentation;

/**
 * Slide generated entities. This class should not be changed manually. Use a
 * subclass to add specific code.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-03
 */
public abstract class GenSlides extends Entities<Slide> {

    private static final long serialVersionUID = 1176413490735L;

    private static Log log = LogFactory.getLog(GenSlides.class);

    private Presentation presentation;

    /**
	 * Constructs slides within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public GenSlides(IDomainModel model) {
        super(model);
    }

    /**
	 * Constructs slides for the presentation parent.
	 * 
	 * @param presentation
	 *            presentation
	 */
    public GenSlides(Presentation presentation) {
        this(presentation.getModel());
        setPresentation(presentation);
    }

    /**
	 * Retrieves the slide with a given oid. Null if not found.
	 * 
	 * @param oid
	 *            oid
	 * @return slide
	 */
    public Slide getSlide(Oid oid) {
        return retrieveByOid(oid);
    }

    /**
	 * Retrieves the slide with a given oid unique number. Null if not found.
	 * 
	 * @param oidUniqueNumber
	 *            oid unique number
	 * @return slide
	 */
    public Slide getSlide(Long oidUniqueNumber) {
        return getSlide(new Oid(oidUniqueNumber));
    }

    /**
	 * Retrieves the first slide whose property with a property code is equal to
	 * a property object. Null if not found.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return slide
	 */
    public Slide getSlide(String propertyCode, Object property) {
        return retrieveByProperty(propertyCode, property);
    }

    /**
	 * Selects slides whose property with a property code is equal to a property
	 * object. Returns empty entities if no selection.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return slides
	 */
    public Slides getSlides(String propertyCode, Object property) {
        return (Slides) selectByProperty(propertyCode, property);
    }

    /**
	 * Gets slides ordered by a property.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered slides
	 */
    public Slides getSlides(String propertyCode, boolean ascending) {
        return (Slides) orderByProperty(propertyCode, ascending);
    }

    /**
	 * Gets slides selected by a selector. Returns empty slides if there are no
	 * slides that satisfy the selector.
	 * 
	 * @param selector
	 *            selector
	 * @return selected slides
	 */
    public Slides getSlides(ISelector selector) {
        return (Slides) selectBySelector(selector);
    }

    /**
	 * Gets slides ordered by a composite comparator.
	 * 
	 * @param comparator
	 *            comparator
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered slides
	 */
    public Slides getSlides(Comparator comparator, boolean ascending) {
        return (Slides) orderByComparator(comparator, ascending);
    }

    /**
	 * Gets title slides.
	 * 
	 * @param title
	 *            title
	 * @return title slides
	 */
    public Slides getTitleSlides(String title) {
        PropertySelector propertySelector = new PropertySelector("title");
        propertySelector.defineEqual(title);
        return getSlides(propertySelector);
    }

    /**
	 * Gets number slide.
	 * 
	 * @param number
	 *            number
	 * @return number slide
	 */
    public Slide getNumberSlide(Integer number) {
        PropertySelector propertySelector = new PropertySelector("number");
        propertySelector.defineEqual(number);
        List<Slide> list = getSlides(propertySelector).getList();
        if (list.size() > 0) return list.iterator().next(); else return null;
    }

    /**
	 * Gets slides ordered by number.
	 * 
	 * @param ascending
	 *            <code>true</code> if ascending
	 * @return ordered slides
	 */
    public Slides getSlidesOrderedByNumber(boolean ascending) {
        return getSlides("number", ascending);
    }

    /**
	 * Gets slides ordered by title.
	 * 
	 * @param ascending
	 *            <code>true</code> if ascending
	 * @return ordered slides
	 */
    public Slides getSlidesOrderedByTitle(boolean ascending) {
        return getSlides("title", ascending);
    }

    /**
	 * Sets presentation.
	 * 
	 * @param presentation
	 *            presentation
	 */
    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    /**
	 * Gets presentation.
	 * 
	 * @return presentation
	 */
    public Presentation getPresentation() {
        return presentation;
    }

    /**
	 * Creates slide.
	 * 
	 * @param presentationParent
	 *            presentation parent
	 * @param title
	 *            title
	 * @return slide
	 */
    public Slide createSlide(Presentation presentationParent, String title) {
        Slide slide = new Slide(getModel());
        slide.setPresentation(presentationParent);
        slide.setTitle(title);
        if (!add(slide)) {
            slide = null;
        }
        return slide;
    }
}
