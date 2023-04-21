package org.openscience.cdkweb.om;

import org.apache.torque.om.Persistent;
import org.openscience.cdkweb.util.Soundex;
import org.openscience.cdkweb.util.StringAndInt;

/** 
  * You should add additional methods to this class to meet the
  * application requirements.  This class will only be generated as
  * long as it does not already exist in the output directory.
  */
public class DBCanonicalName extends org.openscience.cdkweb.om.BaseDBCanonicalName implements Persistent {

    public DBCanonicalName() {
        super();
    }

    public DBCanonicalName(String autonom, DBMolecule mol) throws Exception {
        super();
        setDBMolecule(mol);
        setCanonicalNameTypeId(1);
        setName(autonom);
        setNameSoundex(Soundex.soundex(autonom, "9", new StringAndInt()));
    }

    public DBCanonicalName(String name, DBMolecule mol, int typeindex) throws Exception {
        super();
        setDBMolecule(mol);
        setCanonicalNameTypeId(typeindex);
        setName(name);
        setNameSoundex(Soundex.soundex(name, "9", new StringAndInt()));
    }
}
