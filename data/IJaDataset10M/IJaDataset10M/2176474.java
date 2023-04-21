package joelib2.molecule;

import java.util.Properties;

/**
 *
 * @.author       wegner
 * @.wikipedia Atom
 * @.license      GPL
 * @.cvsversion   $Revision: 1.2 $, $Date: 2005/02/17 16:48:36 $
 */
public abstract class AbstractAtom implements Atom {

    Properties CustomTypes = new Properties();

    public void addCustomType(String type, String SMARTSPattern) {
        if (CustomTypes.containsKey(type)) return;
        CustomTypes.put(type, SMARTSPattern);
    }

    public Properties getCustomTypes() {
        return CustomTypes;
    }

    public abstract Object clone();
}
