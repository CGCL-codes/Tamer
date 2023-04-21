package engine;

public class TypeTrajetCourt extends TypeTrajet {

    private static TypeTrajetCourt instance;

    private TypeTrajetCourt() {
    }

    public static TypeTrajetCourt getInstance() {
        if (instance == null) instance = new TypeTrajetCourt();
        return instance;
    }

    public String toString() {
        return "le plus court";
    }

    public int getPoids(int longueur, int vitesse) {
        return longueur;
    }
}
