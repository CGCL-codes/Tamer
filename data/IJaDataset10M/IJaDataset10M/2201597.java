package net.sourceforge.dashbov.negocio;

import java.io.File;
import net.sourceforge.dashbov.persistencia.sistema_de_arquivos.ManipuladorDeArquivos;

/**
 *
 * @author everton
 */
public class SistemaDeArquivos {

    public static File obterPastaPessoal() {
        File pastaPessoal = ManipuladorDeArquivos.obterPastaPessoal();
        return pastaPessoal;
    }
}
