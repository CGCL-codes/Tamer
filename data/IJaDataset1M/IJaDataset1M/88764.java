package jemfis.theme.def;

import jemfis.core.Colors;
import jemfis.kern.theme.Color;

/**
 *
 * @author Max Vyaznikov
 */
public class DefaultColors implements Colors {

    Color menuColor = GRAY;

    Color selectedMenuColor = YELLOW;

    Color fileColor = DARK_BROWN;

    Color selectedFileColor = SWEET_PINK;

    Color dirColor = ORANGE;

    Color selectedDirColor = YELLOW;

    Color ctrlColor = GRAY;

    Color selectedCtrlColor = WHITE;

    Color linkLineColor = WHITE;

    Color textColor = WHITE;

    /**
	 * Color of objects uses for build menus
	 * @param isSelected Color for selected object or not
	 * @return color of objects uses for build menus
	 */
    public Color getMenuColor(boolean isSelected) {
        return isSelected ? selectedMenuColor : menuColor;
    }

    public void setMenuColor(Color c, boolean isSelected) {
        if (isSelected) {
            selectedMenuColor = c;
        } else {
            menuColor = c;
        }
    }

    /**
	 * Color of objects symbolize files (not directories)
	 * @param isSelected Color for selected object or not
	 * @return color of objects symbolize files (not directories)
	 */
    public Color getFileColor(boolean isSelected) {
        return isSelected ? selectedFileColor : fileColor;
    }

    public void setFileColor(Color c, boolean isSelected) {
        if (isSelected) {
            selectedFileColor = c;
        } else {
            fileColor = c;
        }
    }

    /**
	 * Color of objects symbolize directories
	 * @param isSelected Color for selected object or not
	 * @return color of objects symbolize directories
	 */
    public Color getDirColor(boolean isSelected) {
        return isSelected ? selectedDirColor : dirColor;
    }

    public void setDirColor(Color c, boolean isSelected) {
        if (isSelected) {
            selectedDirColor = c;
        } else {
            dirColor = c;
        }
    }

    /**
	 * Color of control objects
	 * @param isSelected Color for selected object or not
	 * @return color of control objects
	 */
    public Color getControlColor(boolean isSelected) {
        return isSelected ? selectedCtrlColor : ctrlColor;
    }

    public void setControlColor(Color c, boolean isSelected) {
        if (isSelected) {
            selectedCtrlColor = c;
        } else {
            ctrlColor = c;
        }
    }

    /**
	 * Objects color that link another objects
	 * @return color for objects that link another objects
	 */
    public Color getLinkLineColor() {
        return linkLineColor;
    }

    public void setLinkLineColor(Color c) {
        linkLineColor = c;
    }

    /**
	 * Color fo text
	 * @return color of text
	 */
    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color c) {
        textColor = c;
    }

    static final Color WHITE = new Color(10.0f, 10.0f, 10.0f);

    static final Color GRAY = new Color(0.3f, 0.3f, 0.3f);

    static final Color DARK_GRAY = new Color(0.1f, 0.1f, 0.1f);

    static final Color DARK_BROWN = new Color(0.321f, 0.1255f, 0.1255f);

    static final Color ORANGE = new Color(0.55f, 0.35f, 0.0f);

    static final Color SWEET_PINK = new Color(0.7529f, 0.3686f, 0.3255f);

    static final Color YELLOW = new Color(0.8f, 0.7f, 0.0f);
}
