package pedrociarlini.net;

import pedrociarlini.net.impl.ConexaoImpl;

public class ConexaoFactory {

    /**
     * Usado para criar uma inst�ncia de <code>Conexao</code>.
     * @return Uma inst�ncia de um tipo de conex�o que ser� usada em toda 
     * a aplca��o.
     */
    public static IConexao createConexaoInstance() {
        return new ConexaoImpl();
    }
}
