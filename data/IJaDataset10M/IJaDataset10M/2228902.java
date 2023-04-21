package br.com.cinefilmes.ejb;

import javax.ejb.Local;
import br.com.cinefilmes.domain.Filme;
import br.com.cinefilmes.domain.Matrizacao;

@Local
public interface MatrizacaoBO {

    /**
	 * Finaliza uma matrizacao, alterando a dataFim para a data atual.
	 * @param captacao 
	 */
    public void finalizarMatrizacao(Matrizacao matrizacao);

    /**
	 * Salva a matrizacao.
	 * @param captacao
	 */
    public void salvar(Matrizacao matrizacao);

    /**
	 * Busca um matrizacao pelo filme.
	 * @param filme
	 * @return
	 */
    public Matrizacao buscarPorFilme(Filme filme);
}
