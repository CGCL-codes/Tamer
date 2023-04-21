package clavicom.gui.edition.key;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import clavicom.core.keygroup.keyboard.command.commandSet.CCommandSet;
import clavicom.core.keygroup.keyboard.key.CKeyCharacter;
import clavicom.gui.language.UIString;
import clavicom.tools.TLevelEnum;

public class UIPanelOptionKeyCharacter extends UIPanelOptionThreeLevelKey {

    CKeyCharacter keyCharacter;

    UIPanelSelectCharacter selectCharacterNormal;

    UIPanelSelectCharacter selectCharacterShift;

    UIPanelSelectCharacter selectCharacterAltGr;

    public UIPanelOptionKeyCharacter() {
        super();
        CCommandSet commandSet = CCommandSet.GetInstance();
        JPanel characters = new JPanel();
        characters.setLayout(new BoxLayout(characters, BoxLayout.X_AXIS));
        JPanel panellevelNormal = new JPanel(new BorderLayout());
        selectCharacterNormal = new UIPanelSelectCharacter(commandSet, UIString.getUIString("LB_KEYSTHREELEVEL_BORDER_NORMAL"), TLevelEnum.NORMAL);
        panellevelNormal.add(selectCharacterNormal, BorderLayout.CENTER);
        characters.add(panellevelNormal);
        JPanel panellevelShift = new JPanel(new BorderLayout());
        selectCharacterShift = new UIPanelSelectCharacter(commandSet, UIString.getUIString("LB_KEYSTHREELEVEL_BORDER_SHIFT"), TLevelEnum.SHIFT);
        panellevelShift.add(selectCharacterShift, BorderLayout.CENTER);
        characters.add(panellevelShift);
        JPanel panellevelAltGr = new JPanel(new BorderLayout());
        selectCharacterAltGr = new UIPanelSelectCharacter(commandSet, UIString.getUIString("LB_KEYSTHREELEVEL_BORDER_ALTGR"), TLevelEnum.ALT_GR);
        panellevelAltGr.add(selectCharacterAltGr, BorderLayout.CENTER);
        characters.add(panellevelAltGr);
        characters.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), UIString.getUIString("LB_KEYCHARACTER_BORDER")));
        add(characters);
    }

    public void setValuesKeyCharacter(CKeyCharacter myKeyCharacter) {
        setValuesKeyThreeLevel(myKeyCharacter);
        keyCharacter = myKeyCharacter;
        selectCharacterNormal.setValues(myKeyCharacter);
        selectCharacterShift.setValues(myKeyCharacter);
        selectCharacterAltGr.setValues(myKeyCharacter);
    }
}
