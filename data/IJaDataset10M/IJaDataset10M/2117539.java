package org.pqt.mr2rib;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import org.pqt.mr2rib.mrutil.NamedElement;
import org.pqt.mr2rib.ribtranslator.ObjectTranslator;
import org.pqt.mr2rib.script.NameList;
import org.pqt.mr2rib.script.Script;

/**An instance of GlobalRegistry is created for each mental ray file we process
 * it contains all the things that change on a file by file basis.
 *
 * @author  Peter Quint 
 */
public class GlobalRegistry {

    /**This is a registry of all named items*/
    public Hashtable registry = new Hashtable();

    /** This stores translated object definitions, indexed by name*/
    public Hashtable ribObjectRegistry = new Hashtable();

    /** This stores textures*/
    public Hashtable textureRegistry = new Hashtable();

    /** This stores ShaderDefinitions indexed by name*/
    public Hashtable shaderDefinitionRegistry = new Hashtable();

    /** This stores ShaderDeclarations indexed by name*/
    public Hashtable shaderDeclarationRegistry = new Hashtable();

    /** This stores render statements from the parsed file*/
    public Vector renderStatements = new Vector(2);

    /** This stores data definitions (i.e. a data definition filled out
        with actual values), indexed by name*/
    public Hashtable dataDefinitionRegistry = new Hashtable();

    /** This stores data declarations, indexed by name*/
    public Hashtable dataDeclarationRegistry = new Hashtable();

    /** This stores the materials that have been translated, indexed by name*/
    public Hashtable materialTranslatorRegistry = new Hashtable();

    /** This stores material definitions, indexed by name*/
    public Hashtable materialRegistry = new Hashtable();

    /** This stores instances, indexed by name */
    public Hashtable instanceRegistry = new Hashtable();

    /** This stores instance groups, indexed by name*/
    public Hashtable instGroupRegistry = new Hashtable();

    /** This stores light name strings and their corresponding handles 
     *  this is only used when GlobalOptions.useIntegerHandles is true, otherwise
     *  the names strings are used directly. The names are the names of the 
     *  instance containing the light, not the names of the lights themselves*/
    public Hashtable lightHandleRegistry = new Hashtable();

    /** A hashtable of all the options blocks, indexed by name */
    public Hashtable optionsRegistry = new Hashtable();

    /** A hashtable of object definitions, indexed by name*/
    public Hashtable objectDefinitionRegistry = new Hashtable();

    /** This store lights (as in the class Light, not LightTranslator) indexed
     * by name*/
    public Hashtable lightRegistry = new Hashtable();

    /** A hashtable of camera definitions, indexed by name*/
    public Hashtable cameraRegistry = new Hashtable();

    /**True if at least one light in the scene has shadowmaps switched on*/
    public Boolean needsShadowMaps = null;

    /**This hashtable has object name strings as the keys (these are the names that
     *appear at the instance level) and a HashSet as the value, containing
     *strings representing all the lights that are switched on for this 
     *object. Again lights are identified by the names of their instances.
     *If currentLinkList is null then no light linking has been set up. If an object
     *does not appear in the hashtable then it is assumed to be illuminated by all
     *lights.*/
    public Hashtable currentLinkList = null;

    /**This is a vector of Files, representing the shader source code files that
     * need to be compiled*/
    public HashSet shadersToCompile = new HashSet();

    /**This is is the fstop value to be used for the renderman camera, -1 means
     depth of field is not active*/
    public float fstop = -1;

    /**This is is the focallength value to be used for the renderman camera, -1 means
     depth of field is not active*/
    public float focallength = -1;

    /**This is is the focaldistance value to be used for the renderman camera, -1 means
     depth of field is not active*/
    public float focaldistance = -1;

    /**The circle of confusion at the camera's focal distance - used to calculate
     * the values for depth of field in 3dsmax*/
    public float circleOfConfusion = -1;

    /**The plane specified in the 3dsmax dof statement*/
    public float plane = Float.NaN;

    /**The maximum width or height of the image in pixels*/
    public float maxDimension = -1;

    public int frameNo = -1;

    /**True if there are environment or reflection maps to be generated for this frame*/
    public boolean hasEnvReflMaps = false;

    /**This hash table stores the names of object instances (the keys) and the
     * surfact shaders assigned to them (the values)*/
    public Hashtable surfaceShaderAssignments = new Hashtable();

    /**This hash table stores the names of object instances (the keys) and the
     * surfact shaders assigned to them (the values)*/
    public Hashtable displacementShaderAssignments = new Hashtable();

    /**This hash table stores the names of light source instances (the keys) and the
     * light shaders assigned to them (the values)*/
    public Hashtable lightShaderAssignments = new Hashtable();

    public NamedElement[] getAllWithID(int id) {
        NamedElement[] result = new NamedElement[registry.size()];
        Enumeration e = registry.elements();
        int count = 0;
        while (e.hasMoreElements()) {
            result[count] = (NamedElement) e.nextElement();
            count++;
        }
        return result;
    }

    public NamedElement get(String name) {
        return (NamedElement) registry.get(name);
    }

    public ObjectTranslator getObject(String name) {
        return (ObjectTranslator) ribObjectRegistry.get(name);
    }

    /**This creates a namelist of all objects that are illuminated
     * by a specific light. 
     * @param lightName the instance name of the light
     * @return a namelist, or null if this light illuminates all objects
     * in the scene
     */
    public NameList extractObjectsForLight(String lightName) {
        NameList list = new NameList();
        if (currentLinkList != null) {
            Enumeration e = currentLinkList.keys();
            HashSet hs;
            String objName;
            while (e.hasMoreElements()) {
                objName = (String) e.nextElement();
                hs = (HashSet) currentLinkList.get(objName);
                if (hs.contains(lightName)) list.addName(objName);
            }
        }
        if (list.size() == 0) return null; else return list;
    }
}
