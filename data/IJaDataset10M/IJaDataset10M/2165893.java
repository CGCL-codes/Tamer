package fr.inria.zvtm.clustering;

class RemoveAllGlyphsDelta implements Delta {

    RemoveAllGlyphsDelta() {
    }

    public void apply(SlaveUpdater slaveUpdater) {
        slaveUpdater.removeAllGlyphs();
    }

    @Override
    public String toString() {
        return "RemoveAllGlyphsDelta";
    }
}
