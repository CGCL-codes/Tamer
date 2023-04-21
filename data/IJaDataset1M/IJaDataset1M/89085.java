package util;

/**
 * 
 * <b>HelpDeskTRE</b><br><br>
 *  
 *  
 * Interface que representa uma Conexao afim de obter
 * a persistencia de um Object 
 * 
 * 
 * @author Arthur Lucio Meneses Farias <br>
 * @author Andre Luiz Alves <br>
 * @author Danilo Coura Moreira <br>
 */
public interface Conexao {

    /**
	 * Realiza a grava��o dem um Object afim de se obter a persistencia dele
	 * 
	 * @param obj
	 *            o Object que sera gravado
	 */
    public void write(Object obj);

    /**
	 * Realiza a leitura de um Object
	 * 
	 * @return O Object lido, ou null se nao for encontrado
	 */
    public Object read();
}
