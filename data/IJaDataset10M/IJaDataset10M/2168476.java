package packGestionAgendas.packExcepciones;

public class DiaAgendaNoEncontradoException extends Exception {

    public DiaAgendaNoEncontradoException() {
        super();
    }

    public DiaAgendaNoEncontradoException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DiaAgendaNoEncontradoException(String arg0) {
        super(arg0);
    }

    public DiaAgendaNoEncontradoException(Throwable arg0) {
        super(arg0);
    }
}
