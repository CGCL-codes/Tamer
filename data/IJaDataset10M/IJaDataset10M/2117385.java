package org.pqt.mr2rib.script;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.pqt.mr2rib.matsub.RSLParam;
import org.pqt.mr2rib.matsub.ShaderConstructor;
import org.pqt.mr2rib.matsub.ShaderRec;
import org.pqt.mr2rib.mrutil.ParamValueHolder;
import org.pqt.mr2rib.ribtranslator.Util;

/**
 *
 * @author Peter Quint  */
public class Replace extends ScriptItem {

    public static final int DISPLACEMENT = 1;

    public static final int SURFACE = 2;

    public static final int LIGHT = 3;

    /**The type of the shader to be replaces*/
    public int type;

    /**The name of the shader we are going to replace the current one with*/
    public String shaderName = null;

    /**A hashtable matching parameter names e.g. 'uniform color lightColor' with
     *values (one of the standard renderman value types - float, float[], string[]
     *and string*/
    public Hashtable standardArguments = new Hashtable();

    /**A hashtable matching parameter names against arrays of two strings - the
     *first string is the name of the shaderNode whose value we are interested in
     *the second the parameter name from this shader*/
    public Hashtable assignedArguments = new Hashtable();

    /** If non null this contains the values that must appear in the RIB
     *  Color statement for objects to which the shader applies*/
    public float[] color = null;

    /** If non null this contains the values that must appear in the RIB
 *  Opacity statement before objects to which the shader applies*/
    public float[] opacity = null;

    public void doProcess(org.pqt.mr2rib.ribtranslator.LightTranslator light) {
    }

    public void doProcess(org.pqt.mr2rib.ribtranslator.MaterialTranslator material) {
        if (type == SURFACE) {
            String name = material.theMaterial.getName();
            boolean ok = nameList.match(name);
            if ((nameList == null) || (nameList.match(name))) material.surfReplace = this;
        } else if (type == DISPLACEMENT) {
            if ((material.theMaterial.displace != null) && (material.theMaterial.displace.length > 0)) {
                String name = material.theMaterial.getName();
                if ((nameList == null) || (nameList.match(name))) material.dispReplace = this;
            }
        }
    }

    /**Create an array of RIB shader arguments, using the information in
     * the standardArguments table
     * @return an array of individual arguments
     */
    public String[] getStandardArguments() {
        Vector v = new Vector();
        Enumeration e = standardArguments.keys();
        String key, arg;
        Object value;
        ShaderRec.TopLevelArgRec tla;
        while (e.hasMoreElements()) {
            key = (String) e.nextElement();
            value = standardArguments.get(key);
            arg = '"' + key + "\" " + Util.toString(value) + ' ';
            v.add(arg);
        }
        String result[] = new String[v.size()];
        v.toArray(result);
        return result;
    }
}
