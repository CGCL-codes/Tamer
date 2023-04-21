package biologicalClassification;

public class Eukaryote extends Life {

    private String nucleus;

    public Eukaryote() {
        nucleus = "Has a Nucleus with DNA";
    }

    @Override
    public String toString() {
        return "Eukaryote [getNucleus()=" + getNucleus() + ", " + super.toString() + "]";
    }

    public String getNucleus() {
        return nucleus;
    }

    public void setNucleus(String nucleus) {
        this.nucleus = nucleus;
    }
}
