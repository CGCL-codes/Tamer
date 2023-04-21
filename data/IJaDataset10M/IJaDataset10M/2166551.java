package de.kugihan.dictionaryformids.hmi_java_me.lcdui_extension;

import javax.microedition.lcdui.Command;
import de.kugihan.dictionaryformids.hmi_java_me.uidisplaytext.UIDisplayTextItem;
import de.kugihan.dictionaryformids.general.DictionaryException;

public class DfMCommand extends Command {

    UIDisplayTextItem languageUILabel = null;

    public DfMCommand(UIDisplayTextItem languageUILabelParam, int commandType, int priority) throws DictionaryException {
        super(languageUILabelParam.getItemDisplayText(), commandType, priority);
        languageUILabel = languageUILabelParam;
    }
}
