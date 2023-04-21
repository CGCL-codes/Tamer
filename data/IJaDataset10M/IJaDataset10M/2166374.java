package net.sourceforge.ondex.workflow2.gui.arg;

import net.sourceforge.ondex.workflow2.definition.ArgumentBean;

/**
 * 
 * @author lysenkoa
 *
 */
public interface ArgumnetHolder {

    public ArgumentBean getArgumentTemplate();

    public void setValue(String value);
}
