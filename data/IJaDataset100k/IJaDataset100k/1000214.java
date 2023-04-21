package jacky.lanlan.song.extension.strecks.action;

import org.strecks.action.BasicDispatchAction;

/**
 * @author 宋兰岚
 * 
 *         又有Form又有Mapping导航的Action
 */
public interface FormMappingAction extends BasicDispatchAction {

    public void setInputError(boolean inputError);
}
