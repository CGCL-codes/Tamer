package testes;

import controller.GerenciadorDeEventos;
import controller.GerenciadorDeSalas;
import controller.GerenciadorDeSalasAlocadas;
import exception.ExceptionAtributoInexistenteEvento;
import exception.ExceptionAtributoInvalido;
import exception.ExceptionDataInvalida;
import exception.ExceptionDataInvalidaFinalDeSemana;
import exception.ExceptionEntradaInvalida;
import exception.ExceptionEventoIdJaExiste;
import exception.ExceptionEventoJaAlocado;
import exception.ExceptionIdentificacaoInvalida;
import exception.ExceptionNaoEscalonaveis;
import exception.ExceptionSalaEventoInexistente;
import exception.ExceptionSalaIndisponivelHorario;
import exception.ExceptionSalaNaoExiste;

public class UserFacede7 {

    private GerenciadorDeSalas gerenciadorSala = GerenciadorDeSalas.getInstance();

    private GerenciadorDeEventos gerenciadorEvento = GerenciadorDeEventos.getInstance();

    private GerenciadorDeSalasAlocadas gerenciadorSalasAlocadas = GerenciadorDeSalasAlocadas.getInstance();

    /**
	 * Metodo adiciona uma sala ao sistema, a patir de dados fornecidos.
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
	 * Metodo adiciona uma sala ao sistema, a patir de dados fornecidos.
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
	 * Metodo adiciona uma sala ao sistema, a patir de dados fornecidos.
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
	 * @throws ExceptionDataInvalida
	 * @throws ExceptionDataInvalidaFinalDeSemana
	 * @throws ExceptionIdentificacaoInvalida
	 * @throws ExceptionEventoIdJaExiste
	 * @throws ExceptionAtributoInvalido
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
	 * @throws ExceptionDataInvalida
	 * @throws ExceptionDataInvalidaFinalDeSemana
	 * @throws ExceptionIdentificacaoInvalida
	 * @throws ExceptionEventoIdJaExiste
	 * @throws ExceptionAtributoInvalido
	 */
    public void adicionarEvento(String id, String nome, String inicio, String fim, String area, String contato, int repeticoes) throws Exception {
        gerenciadorEvento.adicionarEvento(id, nome, inicio, fim, area, contato, repeticoes);
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
	 * Metodo usado para localizar eventos e as salas onde vao ocorrer, usando
	 * um atributo em comum dos eventos e seu valor como parametros.
	 * 
	 * @param atributo
	 * @param valor
	 * @return String
	 * @throws ExceptionAtributoInexistenteEvento
	 * @throws ExceptionIdentificacaoInvalida
	 * @throws ExceptionDataInvalidaFinalDeSemana
	 * @throws ExceptionDataInvalida
	 * @throws ExceptionEntradaInvalida
	 */
    public String localizarEvento(String atributo, String valor) throws ExceptionAtributoInexistenteEvento, ExceptionEntradaInvalida, ExceptionDataInvalida, ExceptionDataInvalidaFinalDeSemana, ExceptionIdentificacaoInvalida {
        return gerenciadorSalasAlocadas.localizarEvento(atributo, valor);
    }

    /**
	 * Metodo que retorna o valor do atributo de uma sala apartir do seu id.
	 * 
	 * @param idSala
	 * @param atributo
	 * @return String - valor do atributo
	 * @throws Exception
	 *             Caso algum parametro esteja incorreto ou nao existe sala
	 *             cadastrada com o id fornecido
	 */
    public String getAtributoSala(String idSala, String atributo) throws Exception {
        return gerenciadorSala.getAtributoSala(idSala, atributo);
    }

    /**
	 * Metodo que retorna o valor do campo do atributo passado.
	 * 
	 * @param id
	 * @param atributo
	 * @return Sring - valor do campo;
	 * @throws ExceptionSalaEventoInexistente
	 * @throws ExceptionAtributoInexistenteEvento
	 */
    public String getAtributoEvento(String id, String atributo) throws ExceptionSalaEventoInexistente, ExceptionAtributoInexistenteEvento {
        return gerenciadorEvento.getAtributoEvento(id, atributo);
    }

    /**
	 * metodo que remove uma sala do sistema
	 * 
	 * @param idSala
	 * @throws ExceptionSalaNaoExiste
	 */
    public void removerSala(String idSala) throws ExceptionSalaNaoExiste {
        gerenciadorSalasAlocadas.removerSalaDoEvento(idSala);
        gerenciadorSala.removerSala(idSala);
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

    /**
	 * Metodo que encerra o sistema.
	 * 
	 */
    public void encerrarSistema() {
        gerenciadorSala.encerrarSistema();
        gerenciadorEvento.encerrarSistema();
        gerenciadorSalasAlocadas.encerrarSistema();
    }
}
