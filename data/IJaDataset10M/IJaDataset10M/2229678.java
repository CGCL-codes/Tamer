package org.dtools.ini;

/**
 * <p>This class provides some useful methods for testing if two
 * <code>IniItem</code>s or two <code>IniSection</code>s are clones of one
 * another.</p>
 * 
 * <p>The definition of a clone is that it has equal values as the original
 * object, but does not use any of the same object as the original object (i.e.
 * a deep clone).</p>
 * 
 * @author David Lewis
 * @version 1.0.00
 * @since 1.0.00
 */
public class CloneTests {

    /**
     * <p>This method tests if an <code>IniItem</code> is a clone of another
     * <code>IniItem</code>.</p>
     * 
     * @param item The original IniItem to test.
     * @param cloneItem The supposed cloned IniItem of item.
     * @return Returns true if the cloneItem object is a clone of item, false if
     * it's not
     * @since 1.0.00
     */
    static boolean isClone(IniItem item, IniItem cloneItem) {
        boolean result = true;
        result &= item != cloneItem;
        result &= item.getName().equals(cloneItem.getName());
        result &= item.getValue().equals(cloneItem.getValue());
        result &= item.getValidator().equals(cloneItem.getValidator());
        result &= item.getPreComment().equals(cloneItem.getPreComment());
        result &= item.getPostComment().equals(cloneItem.getPostComment());
        result &= item.getEndLineComment().equals(cloneItem.getEndLineComment());
        result &= item.isCaseSensitive() == cloneItem.isCaseSensitive();
        result &= item.equals(cloneItem);
        result &= item.getName() != cloneItem.getName();
        result &= item.getValue() != cloneItem.getValue();
        result &= item.getValidator() != cloneItem.getValidator();
        result &= item.getPreComment() != cloneItem.getPreComment();
        result &= item.getPostComment() != cloneItem.getPostComment();
        result &= item.getEndLineComment() != cloneItem.getEndLineComment();
        return result;
    }

    /**
     * <p>This method tests if an <code>IniSection</code> is a clone of another
     * <code>IniSection</code>.</p>
     * 
     * @param section The original IniItem to test.
     * @param clonedSection The supposed cloned IniItem of item.
     * @return Returns true if the cloneItem object is a clone of item, false if
     * it's not
     * @since 1.0.00
     */
    static boolean isClone(IniSection section, IniSection clonedSection) {
        boolean result = true;
        result &= section != clonedSection;
        result &= section.getName().equals(clonedSection.getName());
        result &= section.getValidator().equals(clonedSection.getValidator());
        result &= section.getPreComment().equals(clonedSection.getPreComment());
        result &= section.getPostComment().equals(clonedSection.getPostComment());
        result &= section.getEndLineComment().equals(clonedSection.getEndLineComment());
        result &= section.isCaseSensitive() == clonedSection.isCaseSensitive();
        result &= section.getNumberOfItems() == clonedSection.getNumberOfItems();
        if (!result) return false;
        for (int i = 0; i < section.getNumberOfItems(); i++) {
            result &= isClone(section.getItem(i), clonedSection.getItem(i));
        }
        if (!result) return false;
        result &= section.equals(clonedSection);
        result &= section.getName() != clonedSection.getName();
        result &= section.getValidator() != clonedSection.getValidator();
        result &= section.getPreComment() != clonedSection.getPreComment();
        result &= section.getPostComment() != clonedSection.getPostComment();
        result &= section.getEndLineComment() != clonedSection.getEndLineComment();
        return result;
    }

    /**
     * <p>This method tests if an <code>IniFile</code> is a clone of another
     * <code>IniFile</code>.</p>
     * 
     * @param ini The original IniItem to test.
     * @param clonedIni The supposed cloned IniItem of item.
     * @return Returns true if the cloneItem object is a clone of item, false if
     * it's not
     * @since 1.0.00
     */
    static boolean isClone(IniFile ini, IniFile clonedIni) {
        boolean result = true;
        result &= ini != clonedIni;
        result &= ini.getValidator().equals(clonedIni.getValidator());
        result &= ini.getValidator() != clonedIni.getValidator();
        result &= ini.isCaseSensitive() == clonedIni.isCaseSensitive();
        result &= ini.getNumberOfSections() == clonedIni.getNumberOfSections();
        if (!result) return false;
        for (int i = 0; i < ini.getNumberOfSections(); i++) {
            result &= isClone(ini.getSection(i), clonedIni.getSection(i));
        }
        if (!result) return false;
        result &= ini.equals(clonedIni);
        result &= ini.getValidator() != clonedIni.getValidator();
        return result;
    }
}
