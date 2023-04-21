package gerenciadorescola.controller.pattern.command;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro Freitas
 */
public class ComandoLogin implements Command {

    public Map execute(Map<String, String> entrada) {
        String usuario = entrada.get("usuario");
        String senha = entrada.get("senha");
        Map saida = new HashMap();
        if ((usuario.equalsIgnoreCase("usuario")) && (senha.equalsIgnoreCase("senha"))) saida.put("status", true); else saida.put("status", false);
        return saida;
    }
}
