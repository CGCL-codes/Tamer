package br.com.dip.gerentes;

import br.com.dip.entidade.File;
import br.com.dip.excecoes.ApplicationException;

public interface GerenteUpload {

    public abstract String upload(File file) throws ApplicationException;
}
