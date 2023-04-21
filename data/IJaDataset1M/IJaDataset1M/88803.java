package jp.seraph.same.view.cui;

import jp.seraph.cui.AbstractCommand;
import jp.seraph.cui.InternalConsole;
import jp.seraph.cui.StringData;
import jp.seraph.cui.StringDataType;
import jp.seraph.cui.SystemNode;
import jp.seraph.cui.SystemNodeType;

/**
 *
 */
public class SetConditionElementNameCommand extends AbstractCommand {

    /**
     * @param parent
     * @param console
     */
    public SetConditionElementNameCommand(MotionSessionData parent, InternalConsole console) {
        super(parent, console);
    }

    /**
     * @see jp.seraph.cui.AbstractCommand#executeImpl(jp.seraph.cui.SystemNode[], int)
     */
    @Override
    protected void executeImpl(SystemNode[] aArgs, int aArgListIndex) {
        MotionElementData tElement = (MotionElementData) aArgs[0];
        String tConditionName = ((StringData) aArgs[1]).getData();
        tElement.setConditionElementName(tConditionName);
    }

    /**
     * @see jp.seraph.cui.Command#getArgumentTypes()
     */
    public SystemNodeType[][] getArgumentTypes() {
        SystemNodeType[][] tResult = new SystemNodeType[1][];
        tResult[0] = new SystemNodeType[] { MotionElementDataType.getInstance(), StringDataType.getInstance() };
        return tResult;
    }

    /**
     * @see jp.seraph.cui.Command#getHelp()
     */
    public String getHelp() {
        return this.getName() + " ElementData \"ConditionElementName\"";
    }

    /**
     * @see jp.seraph.cui.SystemNode#getName()
     */
    public String getName() {
        return "SetConditionName";
    }

    /**
     * @see jp.seraph.cui.SystemNode#getType()
     */
    public SystemNodeType getType() {
        return SetConditionElementNameCommandType.getInstance();
    }
}
