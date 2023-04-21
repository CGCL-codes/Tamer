package net.sf.myway.calibrator.bl.impl;

import java.util.HashSet;
import net.sf.myway.calibrator.CalibratorPlugin;
import net.sf.myway.calibrator.bl.CalibratorBL;
import net.sf.myway.calibrator.da.CalibratorDA;
import net.sf.myway.calibrator.da.entities.Folder;
import net.sf.myway.calibrator.da.entities.ScannedMap;

/**
 * @author Andreas Beckers
 * @version $Revision: 1.2 $
 */
public class CalibratorBLImpl implements CalibratorBL {

    /**
	 * @return
	 */
    protected CalibratorDA getDA() {
        return CalibratorPlugin.getDA();
    }

    @Override
    public Folder getTreeRoot() {
        final Folder f = new Folder() {

            @Override
            protected void initChildren() {
                setChildren(new HashSet<Folder>(getDA().getRootFolders()));
                setMaps(new HashSet<ScannedMap>(getDA().getRootMaps()));
                super.initChildren();
            }
        };
        f.setName("Root");
        return f;
    }

    @Override
    public void rename(final Folder f, final String newName) {
        f.setName(newName);
        getDA().save(f);
    }

    /**
	 * @see net.sf.myway.calibrator.bl.CalibratorBL#rename(net.sf.myway.calibrator.da.entities.ScannedMap,
	 *      java.lang.String)
	 */
    @Override
    public void rename(final ScannedMap map, final String newName) {
        map.setName(newName);
        getDA().save(map);
    }
}
