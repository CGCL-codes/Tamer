package de.kugihan.dictionaryformids.hmi_j2me.lcdui_extension;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import de.kugihan.dictionaryformids.general.DictionaryException;
import de.kugihan.dictionaryformids.hmi_j2me.uidisplaytext.UIDisplayTextItem;

public class DfMForm extends Form implements LanguageUISensitiveItem {

    public DfMForm(String Title) {
        super(Title);
    }

    public void redisplayLabels() throws DictionaryException {
        for (int currentIndexItem = 0; currentIndexItem <= size() - 1; ++currentIndexItem) {
            Item item = get(currentIndexItem);
            if (item instanceof LanguageUISensitiveItem) {
                LanguageUISensitiveItem languageUISensitiveItem = (LanguageUISensitiveItem) item;
                languageUISensitiveItem.redisplayLabels();
            }
        }
        setupCommands();
    }

    public void setupCommands() throws DictionaryException {
    }

    public DfMCommand updateCommand(DfMCommand oldCommand, UIDisplayTextItem languageUILabel, int commandType, int priority) throws DictionaryException {
        removeCommand(oldCommand);
        DfMCommand newCommand = new DfMCommand(languageUILabel, commandType, priority);
        addCommand(newCommand);
        return newCommand;
    }

    public DfMCommand updateItemCommand(Item item, DfMCommand oldCommand, UIDisplayTextItem languageUILabel, int commandType, int priority) throws DictionaryException {
        if (oldCommand != null) item.removeCommand(oldCommand);
        DfMCommand newCommand = new DfMCommand(languageUILabel, commandType, priority);
        item.setDefaultCommand(newCommand);
        return newCommand;
    }
}
