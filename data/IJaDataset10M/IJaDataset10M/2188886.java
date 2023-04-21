package testes;

import controller.GerenciadorDeEventos;
import controller.GerenciadorDeSalas;
import controller.GerenciadorDeSalasAlocadas;
import exception.ExceptionAtributoInexistenteEvento;
import exception.ExceptionDataInvalida;
import exception.ExceptionDataInvalidaFinalDeSemana;
import exception.ExceptionEntradaInvalida;
import exception.ExceptionEventoJaAlocado;
import exception.ExceptionIdentificacaoInvalida;
import exception.ExceptionNaoEscalonaveis;
import exception.ExceptionSalaEventoInexistente;
import exception.ExceptionSalaIndisponivelHorario;

public class UserFacede4 {

    private GerenciadorDeSalas gerenciadorSala = GerenciadorDeSalas.getInstance();

    private GerenciadorDeEventos gerenciadorEvento = GerenciadorDeEventos.getInstance();

    private GerenciadorDeSalasAlocadas gerenciadorSalasAlocadas = GerenciadorDeSalasAlocadas.getInstance();

    /**
	 * Metodo adiciona a patir de dados fornecidos uma sala ao sistema.
	 * 
	 * @param id
	 * @param capacidade
	 * @param finalidade
	 * @param tipo
	 * @throws Exception
	 *             Caso algum parametro esteja incorreto
	 */
    public void adicionarSala(String id, int capacidade, String finalidade, String tipo) throws Exception {
        gerenciadorSala.adicionarSala(id, capacidade, finalidade, tipo);
    }

    /**
	 * Metodo adiciona a partir de dados fornecidos uma sala ao sistema.
	 * 
	 * @param id
	 * @param capacidade
	 * @param finalidade
	 * @param tipo
	 * @param apelido
	 * @throws Exception
	 *             Caso algum parametro esteja incorreto
	 */
    public void adicionarSala(String id, int capacidade, String finalidade, String tipo, String apelido) throws Exception {
        gerenciadorSala.adicionarSala(id, capacidade, finalidade, tipo, apelido);
    }

    /**
	 * Metodo adiciona a partir de dados fornecidos uma sala ao sistema.
	 * 
	 * @param id
	 * @param capacidade
	 * @param finalidade
	 * @param tipo
	 * @param apelido
	 * @param aberto
	 * @throws Exception
	 *             Caso algum parametro esteja incorreto
	 */
    public void adicionarSala(String id, int capacidade, String finalidade, String tipo, String apelido, boolean aberto) throws Exception {
        gerenciadorSala.adicionarSala(id, capacidade, finalidade, tipo, apelido, aberto);
    }

    /**
	 * Metodo que adiciona um evento sem repeticoes.
	 * 
	 * @param id
	 * @param nome
	 * @param inicio
	 * @param fim
	 * @param area
	 * @param contato
	 * @throws Exception
	 *             - Caso algum paramentro esteja incorreto.
	 */
    public void adicionarEvento(String id, String nome, String inicio, String fim, String area, String contato) throws Exception {
        gerenciadorEvento.adicionarEvento(id, nome, inicio, fim, area, contato);
    }

    /**
	 * Metodo que adiciona um evento com repeticoes.
	 * 
	 * @param id
	 * @param nome
	 * @param inicio
	 * @param fim
	 * @param area
	 * @param contato
	 * @param repeticoes
	 * @throws Exception
	 *             - Caso algum parametro esteja incorreto.
	 */
    public void adicionarEvento(String id, String nome, String inicio, String fim, String area, String contato, int repeticoes) throws Exception {
        gerenciadorEvento.adicionarEvento(id, nome, inicio, fim, area, contato, repeticoes);
    }

    /**
	 * Metodo usado para localizar eventos e as salas onde vao ocorrer, usando
	 * um atributo em comum dos eventos e seu valor como parametros.
	 * 
	 * @param atributo
	 * @param valor
	 * @return String
	 * @throws ExceptionAtributoInexistenteEvento
	 * @throws ExceptionSalaEventoInexistente
	 * @throws ExceptionEntradaInvalida
	 * @throws ExceptionIdentificacaoInvalida
	 * @throws ExceptionDataInvalidaFinalDeSemana
	 * @throws ExceptionDataInvalida
	 */
    public String localizarEvento(String atributo, String valor) throws ExceptionAtributoInexistenteEvento, ExceptionSalaEventoInexistente, ExceptionEntradaInvalida, ExceptionDataInvalida, ExceptionDataInvalidaFinalDeSemana, ExceptionIdentificacaoInvalida {
        return gerenciadorSalasAlocadas.localizarEvento(atributo, valor);
    }

    /**
	 * Metodo que aloca um determinado evento a uma sala, respeitando algumas
	 * restricoes.
	 * 
	 * @param idEvento
	 * @param idSala
	 * @throws ExceptionSalaEventoInexistente
	 * @throws ExceptionNaoEscalonaveis
	 * @throws ExceptionDataInvalidaFinalDeSemana
	 * @throws ExceptionEventoJaAlocado
	 * @throws ExceptionSalaIndisponivelHorario
	 */
    public void alocarEvento(String idEvento, String idSala) throws ExceptionSalaEventoInexistente, ExceptionNaoEscalonaveis, ExceptionDataInvalidaFinalDeSemana, ExceptionEventoJaAlocado, ExceptionSalaIndisponivelHorario {
        gerenciadorSalasAlocadas.alocarEvento(idEvento, idSala);
    }

    /**
	 * Metodo que encerra o sistema.
	 * 
	 */
    public void encerrarSistema() {
        gerenciadorSala.encerrarSistema();
        gerenciadorEvento.encerrarSistema();
        gerenciadorSalasAlocadas.encerrarSistema();
    }

    /**
	 * Metodo que zera o sistema.
	 * 
	 */
    public void zerarSistema() {
        gerenciadorSala.zerarSistema();
        gerenciadorEvento.zerarSistema();
        gerenciadorSalasAlocadas.zeraSistema();
    }
}
