package org.epoline.phoenix.manager.shared;

import java.io.ObjectStreamException;
import java.io.Serializable;
import org.epoline.phoenix.common.shared.Item;

/**
 * Insert the type's description here. Creation date: (30-11-00 15:49:53)
 *
 */
public class ItemOverlay extends Item implements Cloneable, Serializable {

    private int pageNumber;

    private int xValue;

    private int yValue;

    private ItemPageType pageType;

    private ItemOverlayLayout layout;

    public static class ItemPageType implements Serializable {

        private String name;

        private int type;

        private static int nextOrdinal = 0;

        private final int ordinal = nextOrdinal++;

        private ItemPageType(String name, int type) {
            this.type = type;
            this.name = name;
        }

        public int getTypeValue() {
            return type;
        }

        public String toString() {
            return name;
        }

        public static final ItemPageType PAGE = new ItemPageType("Page", 0);

        public static final ItemPageType FIRST = new ItemPageType("First", 1);

        public static final ItemPageType MIDDLE = new ItemPageType("Middle", 2);

        public static final ItemPageType LAST = new ItemPageType("Last", 3);

        public static final ItemPageType ALL = new ItemPageType("All", 4);

        private static final ItemPageType[] TYPES = { PAGE, FIRST, MIDDLE, LAST, ALL };

        public static ItemPageType getType(int dmsType) {
            if (dmsType < 0 || dmsType > TYPES.length) {
                throw new IllegalArgumentException();
            }
            return TYPES[dmsType];
        }

        private Object readResolve() throws ObjectStreamException {
            return TYPES[ordinal];
        }
    }

    private String formName = "";

    private String docCode = "";

    private ItemForm.ItemLanguageType formLanguage;

    public String key = "";

    /**
	 * ItemOverlay constructor comment.
	 */
    public ItemOverlay() {
        super();
    }

    public ItemOverlay(int pageNumber, ItemPageType pageType, int xValue, int yValue, ItemOverlayLayout layout, String docCode, String formName, ItemForm.ItemLanguageType formLanguage) {
        if (formName == null || formLanguage == null || docCode == null) {
            throw new NullPointerException();
        }
        if (formName.equals("")) {
            throw new IllegalArgumentException("form");
        }
        if (!isValidLayout(layout)) {
            throw new IllegalArgumentException("layout");
        }
        setPageNumber(pageNumber);
        setPageType(pageType);
        setXValue(xValue);
        setYValue(yValue);
        setLayout(layout);
        this.formName = formName;
        this.formLanguage = formLanguage;
        this.docCode = docCode;
        if (!isValid()) {
            throw new IllegalArgumentException();
        }
    }

    public ItemOverlay(int pageNumber, ItemPageType pageType, int xValue, int yValue, ItemOverlayLayout layout, String docCode, String formName, ItemForm.ItemLanguageType formLanguage, String key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (formName == null || formLanguage == null || docCode == null) {
            throw new NullPointerException();
        }
        if (formName.equals("")) {
            throw new IllegalArgumentException();
        }
        if (!isValidLayout(layout)) {
            throw new IllegalArgumentException("layout");
        }
        this.key = key;
        setPageNumber(pageNumber);
        setPageType(pageType);
        setXValue(xValue);
        setYValue(yValue);
        setLayout(layout);
        this.formName = formName;
        this.formLanguage = formLanguage;
        this.docCode = docCode;
        if (!isValid()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ItemOverlay inOverlay = (ItemOverlay) obj;
        if (!isValid() || !inOverlay.isValid()) {
            return false;
        }
        return (inOverlay.getLayout().equals(getLayout()) && inOverlay.getPageNumber() == getPageNumber() && inOverlay.getPageType().equals(getPageType()) && inOverlay.getXValue() == getXValue() && inOverlay.getYValue() == getYValue());
    }

    public String getDocCode() {
        return docCode;
    }

    public ItemForm.ItemLanguageType getFormLanguage() {
        return formLanguage;
    }

    public String getFormName() {
        return formName;
    }

    public String getKey() {
        return key;
    }

    public ItemOverlayLayout getLayout() {
        return layout;
    }

    /**
	 * Insert the method's description here. Creation date: (30-11-00 15:50:24)
	 *
	 * @return int
	 */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
	 * Insert the method's description here. Creation date: (30-11-00 15:50:58)
	 *
	 * @return int
	 */
    public ItemPageType getPageType() {
        return pageType;
    }

    /**
	 * Insert the method's description here. Creation date: (30-11-00 15:50:35)
	 *
	 * @return int
	 */
    public int getXValue() {
        return xValue;
    }

    /**
	 * Insert the method's description here. Creation date: (30-11-00 15:50:49)
	 *
	 * @return int
	 */
    public int getYValue() {
        return yValue;
    }

    public int hashCode() {
        return getLayout().hashCode() + getPageNumber() + getPageType().getTypeValue() + getXValue() + getYValue();
    }

    public boolean isValid() {
        return isValidLayout(getLayout()) && isValidPageNumber(getPageNumber()) && isValidPageType(getPageType());
    }

    private static boolean isValidLayout(ItemOverlayLayout newOverlayLayout) {
        if (newOverlayLayout == null) {
            throw new NullPointerException();
        }
        return newOverlayLayout.isValid();
    }

    public boolean isValidPageNumber(int pageNumber) {
        return (pageNumber >= 0 && pageNumber <= 99);
    }

    public boolean isValidPageType(ItemOverlay.ItemPageType pageType) {
        if (pageType == null) {
            throw new NullPointerException();
        }
        return true;
    }

    public boolean isValidXValue(int xValue) {
        return (xValue >= 0 && xValue <= 99999);
    }

    public boolean isValidYValue(int yValue) {
        return (xValue >= 0 && xValue <= 99999);
    }

    public void setDocCode(String newDocCode) {
        if (newDocCode != null && !newDocCode.trim().equals("")) {
            docCode = newDocCode;
        }
    }

    /**
	 * Insert the method's description here. Creation date: (30-11-00 15:54:55)
	 *
	 * @param newLayout org.epoline.phoenix.manager.shared.ItemOverlayLayout
	 */
    public void setLayout(ItemOverlayLayout newLayout) {
        if (newLayout == null) {
            throw new NullPointerException("layout");
        }
        ItemOverlayLayout oldLayout = layout;
        if (!isValidLayout(newLayout)) {
            throw new IllegalArgumentException("layout");
        }
        layout = newLayout;
        firePropertyChange("layout", oldLayout, layout);
    }

    public void setPageNumber(int newPageNumber) {
        if (!isValidPageNumber(newPageNumber)) {
            throw new IllegalArgumentException("pageNumber");
        }
        int oldPageNumber = pageNumber;
        pageNumber = newPageNumber;
        firePropertyChange("pageNumber", new Integer(oldPageNumber), new Integer(pageNumber));
    }

    /**
	 * Insert the method's description here. Creation date: (30-11-00 15:50:58)
	 *
	 * @param newPageType int
	 */
    public void setPageType(ItemPageType newPageType) {
        if (newPageType == null) {
            throw new NullPointerException("pageType");
        }
        ItemPageType oldPageType = pageType;
        pageType = newPageType;
        firePropertyChange("pageType", oldPageType, pageType);
    }

    public void setXValue(int newXValue) {
        if (!isValidXValue(newXValue)) {
            throw new IllegalArgumentException("xValue");
        }
        int oldXValue = xValue;
        xValue = newXValue;
        firePropertyChange("XValue", new Integer(oldXValue), new Integer(xValue));
    }

    public void setYValue(int newYValue) {
        if (!isValidYValue(newYValue)) {
            throw new IllegalArgumentException("yValue");
        }
        int oldYValue = yValue;
        yValue = newYValue;
        firePropertyChange("YValue", new Integer(oldYValue), new Integer(yValue));
    }

    public String toString() {
        return getPageNumber() + " : " + getPageType();
    }
}
