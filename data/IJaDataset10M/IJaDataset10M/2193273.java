package org.pqt.mr2rib.script;

import java.io.File;
import org.pqt.mr2rib.GlobalOptions;

/**This holds a reflmap instruction, but also is the superclass of EnvMap
 * ReflMap [for frames framelist ] namelist [omit namelist ][file filename] [ parameter ] [ reversenormal ] [ blur blur ] [ size size ] [ filter name x y ]
 *
 * @author Peter Quint  */
public class ReflMap extends ScriptItem {

    private static int masterCount = 0;

    public int uniqueNo;

    public ReflMap() {
        uniqueNo = masterCount;
        masterCount++;
    }

    /**A list of names to omit in generating the map, or null*/
    public NameList omit = null;

    /**The filename(s) to write the maps to*/
    public String fileName = null;

    /**The name of the parameter to put the map name into*/
    public String mapParamName = GlobalOptions.envMapName;

    /**The size of the map to create*/
    public int mapSize = GlobalOptions.defaultMapSize;

    /**The width of any filtering*/
    public float filtWidth = 1;

    /**True if we need to reverse the normal used to generate the map*/
    public boolean reverseNormal = false;

    /**Create a file name suitable for holding the environment map generated
     * by this envmap instruction*/
    public String makeFileName() {
        File f = new File(GlobalOptions.textureDirectory, nameList.createName() + '_' + uniqueNo + ".rfl");
        return f.getPath();
    }
}
