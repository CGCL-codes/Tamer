package com.csol.chem.forcefield;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import com.csol.chem.core.Atom;

/**
 * @author jiro
 *
 */
public class ForceField {

    public ArrayList<AtomType> types = new ArrayList<AtomType>();

    public HashMap<String, AtomType> typeByName = new HashMap<String, AtomType>();

    public ArrayList<BondLength> bondLengths = new ArrayList<BondLength>();

    public ArrayList<BondAngle> bondAngles = new ArrayList<BondAngle>();

    public ArrayList<TorsionAngle> torsionAngles = new ArrayList<TorsionAngle>();

    public static ForceField instance = null;

    /**
     * 
     */
    public ForceField() {
        ForceField.instance = this;
    }

    /**
     * 
     * @param instance
     */
    public ForceField(ForceField instance) {
        ForceField.instance = instance;
    }

    /**
     * 
     * @param forcefieldClass
     * @throws IllegalArgumentException
     */
    public ForceField(Class forcefieldClass) throws IllegalArgumentException {
        Constructor[] constructors = forcefieldClass.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameters = constructors[0].getParameterTypes();
            if (parameters.length > 0) {
                continue;
            } else {
                try {
                    ForceField.instance = (ForceField) constructor.newInstance(null);
                    break;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Problem invoking an instance " + "of the class " + forcefieldClass.getName() + ".", e);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Problem invoking an instance " + "of the class " + forcefieldClass.getName() + ".", e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Problem invoking an instance " + "of the class " + forcefieldClass.getName() + ".", e);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Problem invoking an instance " + "of the class " + forcefieldClass.getName() + ".", e);
                }
            }
        }
    }

    public static void initialize() {
        ForceField.instance = new ForceField();
    }

    public static void initialize(Class forcefieldClass) {
        ForceField.instance = new ForceField(forcefieldClass);
    }

    public static ForceField i() {
        if (instance == null) throw new IllegalStateException("ForceField singleton " + "has not been initiated yet.");
        return ForceField.instance;
    }

    public static ForceField instance() {
        return i();
    }

    public AtomType getType(String name) {
        return typeByName.get(name);
    }

    /**
     * Types an atom based on the atom and its environment.
     * @param atom the atom to be typed.
     * @return the type of the atom.
     * @throws MissingParameterException 
     */
    public AtomType getType(Atom atom) throws MissingParameterException {
        for (AtomType type : types) {
            if (type.patternAtom.matches(atom)) {
                return type;
            }
        }
        throw new MissingParameterException("The specified atom " + atom + " cannot be found");
    }

    public BondLength getBondLength(AtomType type1, AtomType type2) throws MissingParameterException {
        for (BondLength bl : bondLengths) {
            if (bl.matches(type1, type2)) return bl;
        }
        throw new MissingParameterException("The force field lacks the required " + "bond length parameter: " + type1 + " - " + type2);
    }

    public BondAngle getBondAngle(AtomType type1, AtomType type2, AtomType type3) throws MissingParameterException {
        for (BondAngle ba : bondAngles) {
            if (ba.matches(type1, type2, type3)) return ba;
        }
        throw new MissingParameterException("The force field lacks the required " + "bond angle parameter.");
    }

    public TorsionAngle getTorsionAngle(AtomType type1, AtomType type2, AtomType type3, AtomType type4) throws MissingParameterException {
        for (TorsionAngle ta : torsionAngles) {
            if (ta.matches(type1, type2, type3, type4)) return ta;
        }
        throw new MissingParameterException("The force field lacks the required " + "torsion angle parameter.");
    }

    public void add(AtomType type, String name) {
        if (typeByName.containsKey(name)) throw new IllegalStateException("" + "The type of name " + name + " already exists.");
        types.add(type);
        typeByName.put(name, type);
    }

    public void add(BondLength bondLength) {
        bondLengths.add(bondLength);
    }

    public void add(BondAngle bondAngle) {
        bondAngles.add(bondAngle);
    }

    public void add(TorsionAngle torsionAngle) {
        torsionAngles.add(torsionAngle);
    }

    public static double optimalBondLength(Atom atom1, Atom atom2) throws MissingParameterException {
        ForceField ff = ForceField.instance();
        AtomType t1 = ff.getType(atom1);
        AtomType t2 = ff.getType(atom2);
        BondLength bl = ff.getBondLength(t1, t2);
        return bl.length;
    }

    public static double optimalBondAngle(Atom atom1, Atom atom2, Atom atom3) throws MissingParameterException {
        ForceField ff = ForceField.instance();
        AtomType t1 = ff.getType(atom1);
        AtomType t2 = ff.getType(atom2);
        AtomType t3 = ff.getType(atom3);
        BondAngle ba = ff.getBondAngle(t1, t2, t3);
        return ba.angle;
    }

    public static double optimalTorsionAngle(Atom atom1, Atom atom2, Atom atom3, Atom atom4) throws MissingParameterException {
        ForceField ff = ForceField.instance();
        AtomType t1 = ff.getType(atom1);
        AtomType t2 = ff.getType(atom2);
        AtomType t3 = ff.getType(atom3);
        AtomType t4 = ff.getType(atom4);
        TorsionAngle ta = ff.getTorsionAngle(t1, t2, t3, t4);
        return ta.angle;
    }

    public static boolean hasAtomType(Atom atom) {
        ForceField ff = ForceField.instance();
        for (AtomType type : ff.types) {
            if (type.patternAtom.matches(atom)) return true;
        }
        return false;
    }

    public static boolean hasBondLength(Atom atom1, Atom atom2) {
        ForceField ff = ForceField.instance();
        if (!hasAtomType(atom1) || !hasAtomType(atom2)) return false;
        AtomType type1 = ff.getType(atom1);
        AtomType type2 = ff.getType(atom2);
        for (BondLength bl : ff.bondLengths) {
            if (bl.matches(type1, type2)) return true;
        }
        return false;
    }
}
