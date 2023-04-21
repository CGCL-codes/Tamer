package bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "gen_usuariopapel", sequenceName = "gen_usuariopapel")
public class UsuarioPapel {

    protected Integer id;

    protected Usuario usuario;

    protected Papel papel;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cdusuario")
    public Usuario getUsuario() {
        return usuario;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cdpapel")
    public Papel getPapel() {
        return papel;
    }

    public void setId(Integer cdusuariopapel) {
        this.id = cdusuariopapel;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }
}
