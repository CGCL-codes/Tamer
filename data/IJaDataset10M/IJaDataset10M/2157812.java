package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;
import controllers.Formulario;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Voto extends Model {

    @Required
    public boolean isAceito;

    @Required
    @ManyToOne
    public Requerimento requerimento;

    @ManyToOne
    public Usuario usuario;

    @Required
    public boolean minerva;

    /**
	 * Construtor
	 * @param isAceito voto do membro do colegiado
	 * @param formulario formulario para o qual o membro esta votando
	 * @param usuario quem esta votando
	 */
    public Voto(boolean isAceito, Requerimento requerimento, Usuario usuario) {
        this.requerimento = requerimento;
        this.isAceito = isAceito;
        this.usuario = usuario;
        this.minerva = false;
    }

    /**
	 * Construtor que possibilita usar o minerva
	 * @param isAceito reposta para a requesição
	 * @param formulario formulario para o qual o membro esta votando
	 * @param usuario quem esta votando
	 * @param minerva se eh um voto de minerva da coordenadora
	 */
    public Voto(boolean isAceito, Requerimento requerimento, Usuario usuario, boolean minerva) {
        this.requerimento = requerimento;
        this.isAceito = isAceito;
        this.usuario = usuario;
        this.minerva = minerva;
    }

    /**
	 * informa os votos dados ao determinado requerimento
	 * @param requerimento requerimento do qual se quer ainformacao 
	 * @return lista de votos do requerimento
	 */
    public static List<Voto> getVotosPorRequerimento(Requerimento requerimento) {
        return Voto.find("byRequerimento", requerimento).fetch();
    }

    /**
	 * Procura um voto do usuario para o referido requerimento
	 * @param requerimento referido requerimento
	 * @param usuario referido usuario
	 * @return voto ou null se o usurio nao votou para o requerimento
	 */
    public static Voto getVotoPorRequerimentoUsuario(Requerimento requerimento, Usuario usuario) {
        return Voto.find("byRequerimentoAndUsuario", requerimento, usuario).first();
    }

    /**
	 * Procura um voto de minerva relacionado ao referido requerimento
	 * @param requerimento referido requerimento
	 * @return voto caso exista, caso nao exitas retorna null
	 */
    public static Voto getVotoMinerva(Requerimento requerimento) {
        return Voto.find("byRequerimentoAndMinerva", requerimento, true).first();
    }

    /**
	 * Informa os votos a favor do referido requerimento
	 * @param requerimento referido requerimento
	 * @return lista de votos ou null se possuir votos com essa caracteristicas
	 */
    public static List<Voto> getAceitos(Requerimento requerimento) {
        return Voto.find("byRequerimentoAndIsAceito", requerimento, true).fetch();
    }

    /**
	 * Informa os votos contra ao referido requerimento
	 * @param requerimento referido requerimento
	 * @return lista de votos ou null se possuir votos com essa caracteristicas
	 */
    public static List<Voto> getRejeitados(Requerimento requerimento) {
        return Voto.find("byRequerimentoAndIsAceito", requerimento, false).fetch();
    }

    /**
	 * deleta todos os votos do bd realizados pelo referido usuario
	 * @param id id do usuario
	 */
    public static void deleteVotosDoUsuario(long id) {
        Usuario usuario = Usuario.findById(id);
        List<Voto> votos = Voto.find("byUsuario", usuario).fetch();
        for (Voto voto : votos) {
            voto.delete();
        }
    }

    /**
	 * deleta todos os votos do bd referentes as requerimento
	 * @param id id do requerimento
	 */
    public static void deleteVotosRequeriento(long id) {
        Requerimento requerimento = Requerimento.findById(id);
        List<Voto> votos = Voto.find("byRequerimento", requerimento).fetch();
        for (Voto voto : votos) {
            voto.delete();
        }
    }
}
