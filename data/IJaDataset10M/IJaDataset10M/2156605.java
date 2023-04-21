package de.mogwai.common.web.utils;

import java.util.List;
import java.util.Vector;
import de.mogwai.common.business.entity.EnumItem;
import de.mogwai.common.business.enums.BaseEnumItemEnum;
import de.mogwai.common.web.model.EnumItemMultiLanguageSelectItem;

/**
 * Spezialisierte Liste aus EnumItemMultiLanguageSelectItem.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:15 $
 */
public class EnumItemMultiLanguageSelectItemList extends Vector<EnumItemMultiLanguageSelectItem> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1859187515135497658L;

    /**
     * Default - Konstruktor.
     */
    public EnumItemMultiLanguageSelectItemList() {
    }

    /**
     * Copy - Konstruktor.
     * 
     * Es werden alle Eintr�ge aus einer bestehenden Liste in die neue
     * �bernommen
     * 
     * @param aData
     *                die Liste der Daten
     */
    public EnumItemMultiLanguageSelectItemList(List<EnumItemMultiLanguageSelectItem> aData) {
        super(aData);
    }

    /**
     * Entfernt alle Enums aus der Liste.
     * 
     * @param aEnum
     *                die Liste der Enums, die entfernt werden sollen
     */
    public void removeByEnum(BaseEnumItemEnum... aEnum) {
        Vector<EnumItemMultiLanguageSelectItem> itemsToRemove = new Vector<EnumItemMultiLanguageSelectItem>();
        for (EnumItemMultiLanguageSelectItem theItem : this) {
            for (BaseEnumItemEnum theEnum : aEnum) {
                if (((EnumItem) theItem.getValue()).getId().equals(theEnum.getId())) {
                    itemsToRemove.add(theItem);
                }
            }
        }
        removeAll(itemsToRemove);
    }

    /**
     * Entfernen aller Enums aus der Liste, die nicht definiert sind.
     * 
     * @param aEnum
     *                die Liste der Enums, die behalten werden soll
     */
    public void leaveByEnum(BaseEnumItemEnum... aEnum) {
        Vector<EnumItemMultiLanguageSelectItem> itemsToRemove = new Vector<EnumItemMultiLanguageSelectItem>();
        for (EnumItemMultiLanguageSelectItem theItem : this) {
            boolean theLeave = true;
            for (BaseEnumItemEnum theEnum : aEnum) {
                if (!((EnumItem) theItem.getValue()).getId().equals(theEnum.getId())) {
                    theLeave = false;
                }
            }
            if (!theLeave) {
                itemsToRemove.add(theItem);
            }
        }
        removeAll(itemsToRemove);
    }
}
