package jp.sourceforge.medaka.actions;

import jp.sourceforge.medaka.MessageException;
import jp.sourceforge.medaka.preferences.PreferenceConstants;

/**
 * 
 * @author TIS301016
 *
 */
public class Cmd08Action extends AbstractMedakaAction {

    @Override
    protected String doAction() throws MessageException {
        return editArgments(getProperty(PreferenceConstants.CMD_8));
    }
}
