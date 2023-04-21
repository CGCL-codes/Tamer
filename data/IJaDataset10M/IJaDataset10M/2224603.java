package net.sourceforge.squirrel_sql.plugins.derby.prefs;

import java.io.Serializable;
import net.sourceforge.squirrel_sql.fw.preferences.BaseQueryTokenizerPreferenceBean;

/**
 * A preference bean for the Derby plugin.
 * 
 * @author manningr
 */
public class DerbyPreferenceBean extends BaseQueryTokenizerPreferenceBean implements Cloneable, Serializable {

    static final long serialVersionUID = 5818886723165356478L;

    private boolean readClobsFully = true;

    public DerbyPreferenceBean() {
        super();
        statementSeparator = ";";
        procedureSeparator = "/";
        lineComment = "--";
        removeMultiLineComments = false;
        readClobsFully = true;
        installCustomQueryTokenizer = true;
    }

    /**
    * @return the readClobsFully
    */
    public boolean isReadClobsFully() {
        return readClobsFully;
    }

    /**
    * @param readClobsFully the readClobsFully to set
    */
    public void setReadClobsFully(boolean readClobsFully) {
        this.readClobsFully = readClobsFully;
    }

    /**
	 * @see net.sourceforge.squirrel_sql.fw.preferences.BaseQueryTokenizerPreferenceBean#clone()
	 */
    @Override
    protected DerbyPreferenceBean clone() {
        return (DerbyPreferenceBean) super.clone();
    }
}
