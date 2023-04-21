package springsecurity.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author campello
 */
@Entity
@Table(name = "usuariopapel")
@SequenceGenerator(name = "Sequencia", sequenceName = "seq_UsuarioPapel")
public class UsuarioPapel implements Serializable {

    @Id
    @GeneratedValue(generator = "Sequencia")
    private Long idUsuarioPapel;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idPapel")
    private Papel papel;

    public Long getIdUsuarioPapel() {
        return idUsuarioPapel;
    }

    public void setIdUsuarioPapel(Long idUsuarioPapel) {
        this.idUsuarioPapel = idUsuarioPapel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }
}
