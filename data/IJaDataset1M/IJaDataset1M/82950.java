package org.openscience.cdk.silent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openscience.cdk.interfaces.IMonomer;
import org.openscience.cdk.interfaces.IPDBAtom;
import org.openscience.cdk.interfaces.IPDBPolymer;
import org.openscience.cdk.interfaces.IPDBStructure;
import org.openscience.cdk.interfaces.IStrand;

/**
 * An entry in the PDB database. It is not just a regular protein, but the
 * regular PDB mix of protein or protein complexes, ligands, water molecules
 * and other species.
 *
 * @cdk.module  silent
 * @cdk.githash
 *
 * @author      Egon Willighagen
 * @cdk.created 2006-04-19
 * @cdk.keyword polymer
 */
public class PDBPolymer extends BioPolymer implements Cloneable, IPDBPolymer {

    private static final long serialVersionUID = 4173552834313952358L;

    List<String> sequentialListOfMonomers;

    List<IPDBStructure> secondaryStructures;

    /**
	 * Constructs a new Polymer to store the {@link IMonomer}s.
	 */
    public PDBPolymer() {
        super();
        sequentialListOfMonomers = new ArrayList<String>();
        secondaryStructures = new ArrayList<IPDBStructure>();
    }

    public void addStructure(IPDBStructure structure) {
        secondaryStructures.add(structure);
    }

    public Collection<IPDBStructure> getStructures() {
        return new ArrayList<IPDBStructure>(secondaryStructures);
    }

    /**
	 * Adds the atom oAtom without specifying a {@link IMonomer} or a Strand. Therefore the
	 * atom to this AtomContainer, but not to a certain Strand or {@link IMonomer} (intended
	 * e.g. for HETATMs).
	 *
	 * @param oAtom  The {@link IPDBAtom} to add
	 */
    public void addAtom(IPDBAtom oAtom) {
        super.addAtom(oAtom);
    }

    /**
	 * Adds the atom oAtom to a specified Monomer. Additionally, it keeps
	 * record of the iCode.
	 *
	 * @param oAtom  The IPDBAtom to add
	 * @param oMonomer  The monomer the atom belongs to
	 */
    public void addAtom(IPDBAtom oAtom, IMonomer oMonomer) {
        super.addAtom(oAtom, oMonomer);
        if (!sequentialListOfMonomers.contains(oMonomer.getMonomerName())) sequentialListOfMonomers.add(oMonomer.getMonomerName());
    }

    /**
	 * Adds the IPDBAtom oAtom to a specified Monomer of a specified Strand.
	 * Additionally, it keeps record of the iCode.
	 *
	 * @param oAtom  The IPDBAtom to add
	 * @param oMonomer  The monomer the atom belongs to
	 */
    public void addAtom(IPDBAtom oAtom, IMonomer oMonomer, IStrand oStrand) {
        super.addAtom(oAtom, oMonomer, oStrand);
        if (!sequentialListOfMonomers.contains(oMonomer.getMonomerName())) sequentialListOfMonomers.add(oMonomer.getMonomerName());
    }

    /**
	 * Returns the monomer names in the order in which they were added.
	 * 
	 * @see org.openscience.cdk.interfaces.IPolymer#getMonomerNames()
	 */
    public Collection<String> getMonomerNamesInSequentialOrder() {
        return new ArrayList<String>(sequentialListOfMonomers);
    }

    public String toString() {
        StringBuffer stringContent = new StringBuffer();
        stringContent.append("PDBPolymer(");
        stringContent.append(this.hashCode()).append(", ");
        stringContent.append(super.toString());
        stringContent.append(")");
        return stringContent.toString();
    }
}
