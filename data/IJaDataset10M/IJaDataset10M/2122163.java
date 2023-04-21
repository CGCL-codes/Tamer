package Predicat;

public class Objet {

    String type;

    String ident;

    public Objet(String _ident) {
        ident = _ident;
    }

    public String getIdent() {
        return ident;
    }

    public String getType() {
        return type;
    }

    public void setType(String _type) {
        type = _type;
    }

    public void setIdent(String _ident) {
        ident = _ident;
    }

    public void afficher() {
        System.out.println("ident: " + ident);
        System.out.println("type: " + type);
    }
}
