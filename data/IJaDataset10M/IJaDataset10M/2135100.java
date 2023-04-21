package br.com.lawoffice.dados;

import java.util.List;
import br.com.lawoffice.dominio.Pessoa;

/**
 * 
 * Interface de servico de dados cadastrais para {@link Pessoa} do escritorio.
 * 
 * @author robson
 *
 */
public interface PessoaService {

    /** 
	 * Lista a(s) {@link Pessoa}(s) pelo seu nome.
	 * 
	 * @param c - tipo de pessoa.
	 * @param nome - a ser pesquisado.
	 * @return {@link List} de {@link Pessoa}(s) com o nome pesquisado.
	 */
    public <T extends Pessoa> List<T> listarPorNome(Class<T> c, String nome);

    /**
	 * Atualiza os dados de uma {@link Pessoa}
	 * 
	 * @param t - tipo de pessoa.
	 * @return {@link Pessoa} com o nome atualizado.
	 */
    public <T extends Pessoa> T atualizar(T t);

    /**
	 * Remove uma {@link Pessoa}.
	 * 
	 * @param c - tipo pessoa.
	 * @param t - {@link Pessoa} a ser removida.
	 */
    public <T extends Pessoa> void remover(Class<T> c, T t);

    /**
	 * Localiza uma {@link Pessoa} através do seu id.
	 * 
	 * @param c - tipo de pessoa.
	 * @param t - pessoa a ser localizada.
	 * @return {@link Pessoa} localizada ou null quando nao encontrada.
	 */
    public <T extends Pessoa> T localizar(Class<T> c, T t);

    /**
	 * Salva um {@link Pessoa}
	 * 
	 * @param t - tipo de pessoa.
	 * @return {@link Pessoa} salva.
	 */
    public <T extends Pessoa> T salvar(T t);

    /**
	 * 
	 * Lista todas as {@link Pessoa}.
	 * 
	 * @param c - tipo de pessoa.
	 * @return {@link List} de {@link Pessoa}
	 */
    public <T extends Pessoa> List<T> listar(Class<T> c);
}
