package net.sourceforge.sdm.util;

import net.sourceforge.sdm.controller.*;
import net.sourceforge.sdm.model.*;

/**
 * @author cmccann
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface PrintHandler {

    public void execute(LoginEntry printLoginEntry, SDMController sdm);
}
