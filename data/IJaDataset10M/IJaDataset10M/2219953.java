package donnees;

public class TypeVilleGrande extends TypeVille {

    private static TypeVilleGrande instance;

    private TypeVilleGrande() {
        nom = "grande";
        addNewTypeVille(this);
    }

    public static TypeVilleGrande getInstance() {
        if (instance == null) {
            instance = new TypeVilleGrande();
        }
        return instance;
    }

    public static void initTypeVille() {
        getInstance();
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public int getNbPixelsLarge() {
        return 50;
    }
}
