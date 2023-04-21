package org.isistan.flabot.edit.componenteditor.dialogs.responsibility;

import org.isistan.flabot.ExtensionPointConstants;
import org.isistan.flabot.coremodel.Responsibility;
import org.isistan.flabot.util.edition.tab.EditionTabItem;
import org.isistan.flabot.util.edition.tab.EditionTabItemLoader;
import org.isistan.flabot.util.extension.PropertiesReader;

/**
 * Abstract responsibility edition tab
 * @author $Author: dacostae $
 *
 */
public interface ResponsibilityEditionItem extends EditionTabItem<Responsibility> {

    public static final EditionTabItemLoader<ResponsibilityEditionItem> LOADER = new EditionTabItemLoader<ResponsibilityEditionItem>(ExtensionPointConstants.RESPONSIBILITY_EDITION_TAB_ITEM, ExtensionPointConstants.RESPONSIBILITY_EDITION_TAB_ITEM__TAB_TAG, ExtensionPointConstants.RESPONSIBILITY_EDITION_TAB_ITEM__CLASS_ATTRIBUTE, ExtensionPointConstants.RESPONSIBILITY_EDITION_TAB_ITEM__ORDER_ATTRIBUTE, new PropertiesReader());
}
