package interfaces;

/**
 * O sistema implementa um sistema de pontos na tentativa de manter clientes comprando
 * no site. Em compras efetuadas s�o adicionados pontos na conta do usu�rio que mais
 * tarde podem ser revertidos em descontos ou produtos.
 * 
 * Esta classe implementa uma interface para minipular os pontos.
 * @author Alex Usida, Alvaro Souza, Marcel Omori, Renato Koyama, S�rgio Montazzolli Silva
 *
 */
public interface CartaoFidelidade {

    /**
	 * Adiciona novos pontos ao cliente a cada venda realizada para o mesmo.
	 * @param codigo_cliente C�digo do cliente que esta efetuando a compra.
	 * @param novos_pontos Pontos que serao somados ao cliente em seu cartao de fidelidade.
	 */
    void adicionarPontos(int codigo_cliente, int novos_pontos);

    /**
	 * Decrementa o numero de pontos que o cliente for utilizar na sua compra.
	 * @param codigo_cliente C�digo do cliente que esta efetuando a compra.
	 * @param quantidade_pontos Quantidade de pontos que ele ira utilizar, desde que os tenha.
	 * @return Retorna verdadeiro se pontos utilizados com sucesso, caso contrario retorna falso.
	 */
    boolean utilizarPontos(int codigo_cliente, int quantidade_pontos);

    /**
	 * Remove os pontos que expirarem em suas respectivas datas, visto que o cliente tem prazo para utilizar os pontos ganhos.
	 * @param codigo_cliente C�digo do cliente que tera os pontos expirados.
	 */
    void removerPontosAntigos(int codigo_cliente);

    /**
	 * Obtem a soma total de pontos do cliente.
	 * @param codigo_cliente C�digo do cliente o qual se buscara a soma de pontos.
	 * @return Retorna a soma total de pontos do cliente.
	 */
    int obterSomaDePontos(int codigo_cliente);
}
