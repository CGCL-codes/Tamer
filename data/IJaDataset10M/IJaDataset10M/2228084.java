package ch.tarnet.things.bonzai;

public class BonzaiFactory {

    public static Bonzai createBabyBonzai() {
        TreePart trunk = BonzaiFactory.createBabyBranch();
        trunk.setLength(5);
        trunk.setSize(2);
        Burgeon burgeon = createBabyBurgeon();
        burgeon.setParent(trunk);
        trunk.addChildren(burgeon);
        Bonzai bonzai = new Bonzai(trunk);
        return bonzai;
    }

    public static Branch createBabyBranch() {
        Branch newBranch = new Branch();
        newBranch.setLength(1d);
        newBranch.setSize(1);
        return newBranch;
    }

    public static Burgeon createBabyBurgeon() {
        Burgeon newBurgeon = new Burgeon();
        newBurgeon.setSize(1);
        return newBurgeon;
    }
}
