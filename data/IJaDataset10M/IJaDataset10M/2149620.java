package update5.static_import;

import static java.lang.System.out;
import static java.util.Calendar.*;

/**
 * Podemos importar vari�veis st�ticas, ou seja globais (n�o necess�riamente constantes).
 * 
 *  * Use com cuidado! N�o abuse! � Importa��o de constantes est�ticas (static
 * import) misturam o namespace da sua classe com outros namespaces: pode causar
 * conflitos e obter o resultado oposto (que � menor legibilidade)
 * 
 * @author Fabr�cio Silva Epaminondas
 * 
 */
public class TestStatic {

    public static void main(String[] args) {
        out.println("The static way of print");
    }
}
