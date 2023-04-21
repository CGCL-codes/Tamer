package hojadetrabajo3;

/**
 * Class event for exit or entrance of clients with its details.
 * @author juan
 */
public class Evento {

    private final boolean salida;

    public int t;

    private final int numeroDecliente;

    public Evento(int tiempo, boolean esSalida, int numCliente) {
        t = tiempo;
        salida = esSalida;
        numeroDecliente = numCliente;
    }

    public int getNumeroDecliente() {
        return numeroDecliente;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String convert() {
        String time;
        int hours;
        int minutes;
        hours = t * (1 / 60);
        minutes = t - (hours * (60));
        time = "" + hours + ":" + minutes;
        return time;
    }

    @Override
    public String toString() {
        String clientInfo;
        clientInfo = "El cliente " + this.numeroDecliente + ", se encuentra ";
        if (salida) {
            clientInfo = clientInfo + " de salida, ";
        } else {
            clientInfo = clientInfo + " entrando, ";
        }
        clientInfo = clientInfo + " a las " + convert();
        return clientInfo;
    }
}
