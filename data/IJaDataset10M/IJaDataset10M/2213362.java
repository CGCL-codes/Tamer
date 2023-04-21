package nuts.ext.xwork2.validator.validators;

import java.awt.image.RenderedImage;
import com.opensymphony.xwork2.validator.ValidationException;

/**
 * image size field validator.
 */
public class ImageSizeFieldValidator extends ImageFieldValidator {

    private Integer width;

    private Integer height;

    private Integer minWidth;

    private Integer minHeight;

    private Integer maxWidth;

    private Integer maxHeight;

    /**
	 * @return the width
	 */
    public Integer getWidth() {
        return width;
    }

    /**
	 * @return the height
	 */
    public Integer getHeight() {
        return height;
    }

    /**
	 * @return the minWidth
	 */
    public Integer getMinWidth() {
        return minWidth;
    }

    /**
	 * @param minWidth the minWidth to set
	 */
    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    /**
	 * @return the minHeight
	 */
    public Integer getMinHeight() {
        return minHeight;
    }

    /**
	 * @param minHeight the minHeight to set
	 */
    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    /**
	 * @return the maxWidth
	 */
    public Integer getMaxWidth() {
        return maxWidth;
    }

    /**
	 * @param maxWidth the maxWidth to set
	 */
    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
	 * @return the maxHeight
	 */
    public Integer getMaxHeight() {
        return maxHeight;
    }

    /**
	 * @param maxHeight the maxHeight to set
	 */
    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
	 * @see com.opensymphony.xwork2.validator.Validator#validate(java.lang.Object)
	 */
    public void validate(Object object) throws ValidationException {
        Object value = getFieldValue(getFieldName(), object);
        if (value == null) {
            return;
        }
        RenderedImage ri = getImage(value);
        width = ri.getWidth();
        height = ri.getHeight();
        if (minWidth != null && width < minWidth) {
            addFieldError(getFieldName(), object);
        } else if (minHeight != null && height < minHeight) {
            addFieldError(getFieldName(), object);
        } else if (maxWidth != null && width > maxWidth) {
            addFieldError(getFieldName(), object);
        } else if (maxHeight != null && height > maxHeight) {
            addFieldError(getFieldName(), object);
        }
    }
}
