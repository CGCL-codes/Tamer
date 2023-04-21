package l1j.server.server.model.classes;

class L1ElfClassFeature extends L1ClassFeature {

    @Override
    public int getAcDefenseMax(int ac) {
        return ac / 3;
    }

    @Override
    public int getMagicLevel(int playerLevel) {
        return Math.min(6, playerLevel / 8);
    }
}
