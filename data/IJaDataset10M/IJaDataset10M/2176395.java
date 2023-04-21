package org.jmol.viewer;

class MeshRibbon extends Mps {

    Mps.Mpspolymer allocateMpspolymer(Polymer polymer) {
        return new Schain(polymer);
    }

    class Schain extends Mps.Mpspolymer {

        Schain(Polymer polymer) {
            super(polymer, -2, 3000, 800, 5000);
        }
    }
}
