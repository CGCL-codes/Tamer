package es.aeat.eett.workbench.core;

import java.util.EventObject;

/**
 * Informs a listener that  new dispatch.
 * 
 * @author f00992x1
 */
public class AppenderEvent extends EventObject {

    private static final long serialVersionUID = 833224526174749892L;

    private int type;

    private String sortMensage;

    private String longMensage;

    /**
	 * @param source
	 * @param type
	 * @param sortMensage
	 * @param longMensage
	 */
    AppenderEvent(Object source, int type, String sortMensage, String longMensage) {
        super(source);
        this.type = type;
        this.sortMensage = sortMensage;
        this.longMensage = longMensage;
    }

    /**
	 * @return Returns the longMensage.
	 */
    public String getLongMensage() {
        return longMensage;
    }

    /**
	 * @return Returns the sortMensage.
	 */
    public String getSortMensage() {
        return sortMensage;
    }

    /**
	 * @return Returns the type.
	 */
    public int getType() {
        return type;
    }
}
