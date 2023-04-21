package negocios;

import negocios.excecoes.LocalizacaoException;
import negocios.excecoes.OcultoException;

/**
 * Classe que representa o relacionamento entre dois usuarios. No
 * relacionamento, o usuario pode ocultar sua localizacao do amigo, ou
 * compartilha-la.
 * 
 * @author Jonathan Brilhante
 * @author Jose Rafael
 * @author Nata Venancio
 * @author Renato Almeida
 * 
 * @version 2.0
 */
public class Relacionamento implements Comparable<Relacionamento> {

    private boolean ocultar;

    private String login;

    private Usuario amigo;

    /**
	 * Constroi um relacionamento entre dois usuarios
	 * 
	 * @param amigo
	 *            o amigo no relacioamento
	 * @param login
	 *            o login do detentor desse Amigo
	 */
    public Relacionamento(Usuario amigo, String login) {
        this(amigo, login, false);
    }

    /**
	 * Constroi um relacionamento entre dois usuarios
	 * 
	 * @param amigo
	 *            o amigo no relacioamento
	 * @param login
	 *            o login do detentor desse Amigo
	 * @param oculto
	 *            o modo de compartilhamento inicial (compartilhando por padrao)
	 */
    public Relacionamento(Usuario amigo, String login, boolean oculto) {
        this.amigo = amigo;
        this.login = login;
        this.ocultar = oculto;
    }

    /**
	 * Seta o status para oculto
	 */
    public void ocultar() {
        this.ocultar = true;
    }

    /**
	 * Seta o status para compartilhar
	 */
    public void compartilhar() {
        this.ocultar = false;
    }

    /**
	 * Retorna o status de compartilhamento
	 * 
	 * @return true se o usuario oculta sua localizacao do amigo, false caso
	 *         contrario
	 */
    public boolean isOculto() {
        return ocultar;
    }

    /**
	 * Retorna o amigo do usuario que possui o relacionamento
	 * 
	 * @return o amigo
	 */
    public Usuario getAmigo() {
        return amigo;
    }

    /**
	 * Retorna a localizacao do usuario, se o mesmo nao estiver ocultado ela
	 * 
	 * @return a localizacao do usuario, ou uma mensagem de localizacao
	 *         indisponivel
	 * @throws OcultoException
	 *             caso nao seja possivel obter a localizacao do amigo
	 * @throws LocalizacaoException
	 *             caso a localizacao nao possa ser obtida
	 */
    public Localizacao getLocalizacao() throws OcultoException, LocalizacaoException {
        for (Relacionamento a : getAmigo().amigos()) {
            if (a.getAmigo().getLogin().equals(getLogin()) && !a.isOculto()) {
                return getAmigo().localizacao();
            }
        }
        throw new OcultoException();
    }

    /**
	 * Retorna o login do detentor desse amigo
	 * 
	 * @return o login
	 */
    public String getLogin() {
        return login;
    }

    /**
	 * Retorna uma representacao String do objeto amigo
	 * 
	 * @return o nome do amigo
	 */
    public String toString() {
        return getAmigo().getNome();
    }

    /**
	 * Retorna uma comparacao entre dois amigos, como inteiro
	 * 
	 * @return 1, se esse amigo for maior na ordem lexicografia do nome, 0, se
	 *         forem iguais, -1 se for maior
	 */
    public int compareTo(Relacionamento a) {
        return getAmigo().getNome().compareTo(a.getAmigo().getNome());
    }
}
