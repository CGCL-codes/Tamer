package glaureano.uteis.leitorArquivos.ouvidor;

import glaureano.uteis.persistencia.ControladorPersistencia;

/**
 *
 * @author gilberto
 */
public class InsersorBancoDados<T> implements OuvidorLeitorArquivos<T> {

    ControladorPersistencia controladorPersistencia;

    public InsersorBancoDados(ControladorPersistencia controladorPersistencia) {
        this.controladorPersistencia = controladorPersistencia;
    }

    public void receba(T objeto) {
        controladorPersistencia.salvar(objeto);
    }

    public void close() {
        controladorPersistencia.close();
    }
}
