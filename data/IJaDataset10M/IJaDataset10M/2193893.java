package AnaliseLexicaFinal.EstruturaDeDados;

public class Identificador extends Token {

    protected int id;

    protected String tipo;

    protected Object valor;

    private static int proxid = 0;

    public Identificador(String token, String lexema) {
        super(token, lexema);
        id = proxId();
    }

    private static int proxId() {
        return proxid++;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }
}
