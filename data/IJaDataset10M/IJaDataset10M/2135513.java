package jvc.web.module;

import java.util.*;
import org.jdom.Element;

/**
 * <p>Title :Command��װ</p>
 * <p>Description:</p>
 * <p>Created on 2005-5-20</p>
 * <p>Company :JVC</p>
 *  @author : Ru_fj
 *  @version : 1.0
 */
public class Command {

    private String Name;

    private boolean TransParam;

    private boolean Dbtrans;

    private boolean Allow;

    private String Caption = "";

    private String DBName;

    private Vector Actions;

    public Command(Element e, Element root) {
        Actions = new Vector();
        Name = e.getAttributeValue("name");
        TransParam = Boolean.valueOf(e.getAttributeValue("transparam")).booleanValue();
        Dbtrans = Boolean.valueOf(e.getAttributeValue("dbtrans")).booleanValue();
        Allow = Boolean.valueOf(e.getAttributeValue("allow")).booleanValue();
        DBName = e.getAttributeValue("dbname");
        Caption = e.getAttributeValue("caption");
        List listAction = e.getChildren();
        for (int ilistAction = 0; ilistAction < listAction.size(); ilistAction++) {
            Element el = (Element) listAction.get(ilistAction);
            if (el.getName().equals("action")) Actions.add(new Action(el));
            if (el.getName().equals("command")) {
                List listCommand = root.getChildren("command");
                for (Iterator it = listCommand.iterator(); it.hasNext(); ) {
                    Element eCommand = (Element) it.next();
                    if (eCommand.getAttributeValue("name").equals(el.getAttributeValue("name"))) {
                        List vCommandActions = new Command(eCommand, root).getActions();
                        for (int ivCommandActions = 0; ivCommandActions < vCommandActions.size(); ivCommandActions++) {
                            Actions.add((Action) vCommandActions.get(ivCommandActions));
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
    }

    /**
	 * @return Returns the actions.
	 */
    public List getActions() {
        return Actions;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return Name;
    }

    /**
	 * @return Returns the transParam.
	 */
    public boolean isTransParam() {
        return TransParam;
    }

    public boolean isDbtrans() {
        return Dbtrans;
    }

    public boolean isAllow() {
        return Allow;
    }

    /**
	 * @return Returns the dbname.
	 */
    public String getDBName() {
        return DBName;
    }

    public String getCaption() {
        return Caption;
    }
}
