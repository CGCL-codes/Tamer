package org.epoline.phoenix.usersettings.shared;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.epoline.phoenix.common.shared.EnumDossierNotepadTopLevelPage;
import org.epoline.phoenix.common.shared.Item;

public class ItemUserSettingsDNPPage extends Item {

    public static final String PROPERTY_NAME_CURRENT_DNPPAGE = "org.epoline.phoenix.usersettings.ItemUserSettingsDNPPage.CURRENT";

    public static final int DNPPAGE_INDEX_USERSELECTED = 0;

    private static final int DNPPAGE_INDEX_COVER = 1;

    private static final int DNPPAGE_INDEX_CHECKLIST = 2;

    private static final int DNPPAGE_INDEX_TOC = 3;

    private static final int DNPPAGE_INDEX_HISTORY = 4;

    private final String firstPage;

    private final String secondPage;

    private final String thirdPage;

    private final int index;

    private final EnumDossierNotepadTopLevelPage predefinedPage;

    private static final String USER_SETTINGS_DNP_PAGE_PREFIX = "UserSettingsDNPPage_";

    private static final String PAGE_LEVEL_SEPERATOR = ">";

    public static final ItemUserSettingsDNPPage COVER = new ItemUserSettingsDNPPage(EnumDossierNotepadTopLevelPage.COVER, DNPPAGE_INDEX_COVER);

    public static final ItemUserSettingsDNPPage CHECKLIST = new ItemUserSettingsDNPPage(EnumDossierNotepadTopLevelPage.CHECKLIST, DNPPAGE_INDEX_CHECKLIST);

    public static final ItemUserSettingsDNPPage TOC = new ItemUserSettingsDNPPage(EnumDossierNotepadTopLevelPage.TOC, DNPPAGE_INDEX_TOC);

    public static final ItemUserSettingsDNPPage HISTORY = new ItemUserSettingsDNPPage(EnumDossierNotepadTopLevelPage.HISTORY, DNPPAGE_INDEX_HISTORY);

    private static final ItemUserSettingsDNPPage[] VALS = { COVER, CHECKLIST, TOC, HISTORY };

    private static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALS));

    /**
	 * Create new object with supplied name and fixed index 0 (User selected)
	 * 
	 * @param firstPage the first level of the Dossier Notepad page
	 * @param secondPage the second level of the Dossier Notepad page
	 * @param thirdPage the third level of the Dossier Notepad page
	 */
    public ItemUserSettingsDNPPage(String firstPage, String secondPage, String thirdPage) {
        super();
        if (firstPage == null || secondPage == null || thirdPage == null) {
            throw new NullPointerException();
        }
        this.firstPage = firstPage;
        this.secondPage = secondPage;
        this.thirdPage = thirdPage;
        this.index = DNPPAGE_INDEX_USERSELECTED;
        this.predefinedPage = null;
    }

    /**
	 * Create new object with supplied name and index
	 * 
	 * @param page the enumeration instance of the Dossier Notepad page
	 * @param index the type index of the Dossier Notepad page
	 */
    private ItemUserSettingsDNPPage(EnumDossierNotepadTopLevelPage page, int index) {
        this.firstPage = "";
        this.secondPage = "";
        this.thirdPage = "";
        this.index = index;
        this.predefinedPage = page;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        ItemUserSettingsDNPPage other = (ItemUserSettingsDNPPage) obj;
        return other.getIndex() == this.getIndex() && other.getFirstPage().equals(this.getFirstPage()) && other.getSecondPage().equals(this.getSecondPage()) && other.getThirdPage().equals(this.getThirdPage());
    }

    public final java.lang.String getFirstPage() {
        if (getIndex() != DNPPAGE_INDEX_USERSELECTED) {
            return predefinedPage.toString();
        } else {
            return firstPage;
        }
    }

    public int getIndex() {
        return index;
    }

    /**
	 * Returns static instance matching index. Input value must be one of static
	 * non user defined instances.
	 * 
	 * @param index index of non user defined DNP Page instances
	 */
    public static ItemUserSettingsDNPPage getInstance(int index) {
        Iterator iter = VALUES.iterator();
        while (iter.hasNext()) {
            ItemUserSettingsDNPPage dnpPage = (ItemUserSettingsDNPPage) iter.next();
            if (dnpPage.getIndex() == index) {
                return dnpPage;
            }
        }
        throw new IllegalArgumentException("" + index);
    }

    public static final List getItems() {
        return VALUES;
    }

    public final java.lang.String getSecondPage() {
        return secondPage;
    }

    public final java.lang.String getThirdPage() {
        return thirdPage;
    }

    /**
	 * Returns localised string for non user defined pages. Requires that the
	 * key UserSettingsDNPPage_XXX be specified in
	 * org.epoline.phoenix.usersettings.LocaleBundle (class or file) where XXX
	 * is object.getName(). If the key is not specified in the locale bundle, or
	 * is a user defined page, just returns getName().
	 * 
	 * @return localised code if available, code otherwise
	 * @see org.epoline.phoenix.usersettings.LocaleBundle
	 */
    public String toString() {
        String result = getFirstPage();
        if (getSecondPage().length() > 0) {
            result += PAGE_LEVEL_SEPERATOR + getSecondPage();
            if (getThirdPage().length() > 0) {
                result += PAGE_LEVEL_SEPERATOR + getThirdPage();
            }
        }
        return result;
    }
}
