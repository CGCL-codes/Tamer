package telas.componentes;

import java.lang.reflect.Method;
import business.sistema.IObjetoSistema;

public class CampoNumeroInteiro extends CampoEditavel {

    private static final long serialVersionUID = 1L;

    public CampoNumeroInteiro() {
        super();
    }

    @Override
    public void verificaTecla(java.awt.event.KeyEvent tecla) {
        char c = tecla.getKeyChar();
        if (!Character.isDigit(c)) {
            super.verificaTecla(tecla);
            tecla.consume();
        }
    }

    @Override
    public void update(IObjetoSistema objeto, String mensagem) throws Exception {
        if (objeto != null) {
            Class<?> objClasse = objeto.getClass();
            Method objMetodo = objClasse.getMethod("get" + getAtributoObservado());
            setText(String.valueOf(objMetodo.invoke(objeto)));
        }
    }
}
