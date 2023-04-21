package org.expasy.jpl.core.mol.chem.impl;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.expasy.jpl.commons.base.record.RecordsCount;
import org.expasy.jpl.commons.collection.symbol.Symbol.SymbolType;
import org.expasy.jpl.core.mol.AbstractMolecularSymbolManager;
import org.expasy.jpl.core.mol.MolecularFormatRegister;
import org.expasy.jpl.core.mol.chem.ChemicalFacade.MonomerType;
import org.expasy.jpl.core.mol.chem.api.Atom;
import org.expasy.jpl.core.mol.chem.api.MolecularExpressionBuilder;
import org.expasy.jpl.core.mol.chem.api.Molecule;

/**
 * Manage a pool of unique molecules.
 * 
 * @author nikitin
 * 
 * @version 1.0
 */
public final class MoleculeManager {

    private static final MoleculeManager INSTANCE = new MoleculeManager();

    public static final String AMINO_ACID_TOKEN = MolecularFormatRegister.lookupToken("AA_TOKEN");

    public static final String RIBONUC_TOKEN = MolecularFormatRegister.lookupToken("NUC_TOKEN");

    public static final String DEOXYRIBONUC_TOKEN = MolecularFormatRegister.lookupToken("DNUC_TOKEN");

    private static final MoleculeParser MOLECULE_PARSER = MoleculeParser.getInstance();

    private static final MoleculeBuilder MOLECULE_BUILDER = MoleculeBuilder.getInstance();

    private TreeMap<String, Molecule> pool;

    private MoleculeManager() {
        pool = new TreeMap<String, Molecule>();
    }

    /**
	 * @return the singleton.
	 */
    public static MoleculeManager getInstance() {
        return INSTANCE;
    }

    /**
	 * @return the unmodifiable pool of molecules
	 */
    public Map<String, Molecule> getPool() {
        return Collections.unmodifiableMap(pool);
    }

    /**
	 * Add a new molecule to the pool.
	 * 
	 * @param formula the molecular formula (by default an atom is
	 *        monoisotopic).
	 * @throws ParseException if formula is badly formatted.
	 */
    private String addNewMolecule(String formula) throws ParseException {
        if (!hasMolecule(formula)) {
            pool.put(formula, MOLECULE_PARSER.getBuilder().build());
        }
        return formula;
    }

    private void addNewMolecule(RecordsCount<Atom> atoms, int charge) {
        MOLECULE_BUILDER.reset();
        MOLECULE_BUILDER.addAtoms(atoms);
        MOLECULE_BUILDER.charge(charge);
        Molecule molecule = MOLECULE_BUILDER.build();
        pool.put(molecule.getFormula(), molecule);
    }

    public final boolean hasMolecule(String formula) {
        return pool.containsKey(formula);
    }

    public Molecule getMolecule(String formula) throws ParseException {
        return getMoleculeFromFormula(formula);
    }

    public Molecule getMolecule(MonomerType type, char symbol) {
        try {
            Class.forName("org.expasy.jpl.core.mol.MolecularSymbolFacade");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("problem while loading class", e);
        }
        SymbolType<Molecule> symbolType = AbstractMolecularSymbolManager.getSymbolType(type.getName());
        return symbolType.getDataSymbolManager().lookupData(symbol);
    }

    private Molecule getMoleculeFromFormula(String formula) throws ParseException {
        if (!hasMolecule(formula)) {
            MOLECULE_PARSER.parse(formula);
            formula = MOLECULE_PARSER.getBuilder().buildFormula();
            addNewMolecule(formula);
        }
        if (!pool.containsKey(formula)) {
            throw new IllegalArgumentException("cannot find molecule with formula " + formula + " in pool " + pool);
        }
        return pool.get(formula);
    }

    public Molecule getMoleculeFromAtoms(RecordsCount<Atom> atoms, int charge) {
        String formula = getFormulaFromAtoms(atoms, charge);
        if (!hasMolecule(formula)) {
            addNewMolecule(atoms, charge);
        }
        if (!pool.containsKey(formula)) {
            throw new IllegalArgumentException("cannot find molecule with formula " + formula + " in pool " + pool);
        }
        return pool.get(formula);
    }

    /**
	 * Generate the molecule formula from the atom/isotope's content.
	 * 
	 * @param atoms the atoms composing the molecule.
	 * @param charge the molecular charge.
	 * 
	 * @return the formula of the molecule with lexico sorted atom/isotopes.
	 */
    String getFormulaFromAtoms(RecordsCount<Atom> atoms, int charge) {
        Set<Atom> sortedIsotopes = new TreeSet<Atom>(ElementManager.SORTED_BY_SYMBOL_AND_A);
        sortedIsotopes.addAll(atoms.getRecords());
        StringBuffer sbFormula = new StringBuffer();
        for (Atom element : sortedIsotopes) {
            int count = atoms.getCount(element);
            if (count != 0) {
                sbFormula.append(element.toString());
                if (count != 1) {
                    sbFormula.append(count);
                }
            }
        }
        if (charge != 0) {
            sbFormula.append(ElementManager.AtomEnclosingToken.BEGIN_CHARGE);
            if (charge != 1 && charge != -1) {
                sbFormula.append((charge > 0) ? charge + "+" : -charge + "-");
            } else {
                sbFormula.append((charge > 0) ? "+" : "-");
            }
            sbFormula.append(ElementManager.AtomEnclosingToken.END_CHARGE);
        }
        return sbFormula.toString();
    }

    public int size() {
        return pool.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String formula : pool.keySet()) {
            sb.append(formula);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    public static MolecularExpressionBuilder newMolecularExpressionBuilder() {
        return new MolecularExpression.Builder();
    }
}
