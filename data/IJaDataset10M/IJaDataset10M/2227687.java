package br.usp.ime.origami.foldcut.packing;

import java.util.Map;
import java.util.Set;
import br.usp.ime.origami.foldcut.structures.Pair;
import br.usp.ime.origami.foldcut.structures.TreeFaces.TreeFaces;
import br.usp.ime.origami.foldcut.structures.simplegraph.Edge;
import br.usp.ime.origami.foldcut.structures.winged.WingedFace;
import br.usp.ime.origami.foldcut.util.Molecule;
import br.usp.ime.origami.foldcut.util.MoleculeEdge;
import br.usp.ime.origami.foldcut.util.TangentEdge;

public class Assigner {

    private Map<WingedFace, Molecule> molecules;

    private TreeFaces treeFace;

    private WingedFace externalFace;

    public Assigner(TreeFaces treeFace, Map<WingedFace, Molecule> molecules, WingedFace externalFace) {
        this.treeFace = treeFace;
        this.molecules = molecules;
        this.externalFace = externalFace;
    }

    public void assign() {
        Map<WingedFace, Set<WingedFace>> tree = treeFace.getTree();
        for (WingedFace mother : tree.keySet()) {
            Molecule motherMolecule = molecules.get(mother);
            for (WingedFace son : tree.get(mother)) {
                Molecule sonMolecule = molecules.get(son);
                TangentEdge tangentEdge = motherMolecule.findSonTangent(sonMolecule);
                sonMolecule.changeTangentOrientation(tangentEdge);
            }
        }
        for (TangentEdge edge : molecules.get(this.treeFace.getRoot()).getTangents()) {
            if (edge.getExternalFace().equals(externalFace)) {
                molecules.get(this.treeFace.getRoot()).changeTangentOrientation(edge);
                break;
            }
        }
    }
}
