package StudyTest;

public class Lady {

    private String name;

    private Creature pet;

    public Lady(String name, Creature pet) {
        this.name = name;
        this.pet = pet;
    }

    public void MyPetEnjoy() {
        Object Brid;
        if (pet instanceof Brid) {
            Brid g = new Brid("", "");
            g.enjoy();
        }
    }
}
