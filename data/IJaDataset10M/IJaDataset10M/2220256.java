package net.sf.doolin.sqm.gui.form;

import net.sf.doolin.sqm.to.TOSquadronCreate;

public class FormSquadronCreate extends FormTO<TOSquadronCreate> {

    public FormSquadronCreate() {
        setObject(new TOSquadronCreate());
    }

    /**
	 * Returns the manager
	 * 
	 * @return manager
	 */
    public FormAccount getManager() {
        return new FormAccount(getObject().getManager());
    }

    /**
	 * Sets the manager
	 * 
	 * @param manager
	 */
    public void setManager(FormAccount manager) {
        getObject().setManager(manager.getObject());
    }
}
