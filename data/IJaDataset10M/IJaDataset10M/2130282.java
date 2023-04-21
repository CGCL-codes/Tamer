package AGO.Modelo.Estructuras;

public class Instrumento {

    String name = null;

    Moneda mon = null;

    public Instrumento() {
        this("instrumento-default");
    }

    public Instrumento(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
