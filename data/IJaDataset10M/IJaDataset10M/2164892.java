package tp1POO.Modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "cpf")
public class Motorista extends Pessoa implements Serializable {

    @Column(nullable = false, unique = true, length = 13)
    private String cnh;

    @ManyToOne
    private Locacao locacao;

    public Motorista() {
    }

    /** Construtor com as especifições exigidas, ou seja, só será permitido instaciar um motorista
	 * caso haja os dados exigidos como parametro.
	 * @param nome
	 * @param cpf
	 * @param cnh
	 */
    public Motorista(String nome, String cpf, String cnh) {
        this(nome);
        this.setCpf(cpf);
        this.cnh = cnh;
    }

    /** Construtor com apenas duas especificações exigidas.
	 *
	 * @param ref
	 * @param cnh
	 */
    public Motorista(Pessoa ref, String cnh) {
        this.copy(ref);
        setCnh(cnh);
    }

    /** Construtor com apenas uma especificação exigida, possibilitando instanciar um
	 *  motorista apenas com o seu nome.
	 * @param nome
	 */
    public Motorista(String nome) {
        this.setNome(nome);
    }

    /** Método para setar os dados da CNH do motorista
	 *
	 * @param parString
	 */
    public void setCnh(String parString) {
        this.cnh = parString;
    }

    /** Método para identificar o número da CNH do motorista
	 *
	 * @return String CNH
	 */
    public String getCNH() {
        return cnh;
    }

    /** Método para identificar a locação
	 *
	 * @return Locacao - locacao
	 */
    public Locacao getLocacao() {
        return locacao;
    }

    /** Método para setar o valor da locação
	 *
	 * @param parLocacao
	 */
    public void setLocacao(Locacao parLocacao) {
        if (parLocacao != null) parLocacao.addMotorista(this); else if (locacao != null) this.locacao.removerMotorista(this);
        this.locacao = parLocacao;
    }
}
