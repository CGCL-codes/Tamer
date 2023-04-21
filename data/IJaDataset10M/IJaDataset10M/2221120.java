package de.erdesignerng.model.utils;

import de.erdesignerng.ERDesignerBundle;

/**
 * @author $Author: mirkosertic $
 * @version $Date: 2009-03-09 19:07:30 $
 */
public class MissingViewInfo extends MissingInfo {

    public MissingViewInfo(String aWhat) {
        super(ERDesignerBundle.MISSINGENTITY, aWhat);
    }
}
