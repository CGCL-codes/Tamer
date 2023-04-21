package BaseDeDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ConsultarAsesinato {

    private int IdArma;

    private int IdEscenario;

    private int IdPersonaje;

    private String escenario;

    private String personaje;

    private String arma;

    public ConsultarAsesinato(Connection a, int IdAsesinato) {
        try {
            PreparedStatement selectuser;
            selectuser = a.prepareStatement(" select IdPersonaje, IdEscenario, IdArma from Asesinato where idAsesinato = ? ");
            selectuser.setInt(1, IdAsesinato);
            ResultSet result = selectuser.executeQuery();
            int cont = 0;
            while (result.next()) {
                this.IdPersonaje = result.getInt(1);
                this.IdEscenario = result.getInt(2);
                this.IdArma = result.getInt(3);
            }
            ConsultarEscenario cE = new ConsultarEscenario(a);
            ConsultarPersonajes cP = new ConsultarPersonajes(a);
            ConsultarArmas cA = new ConsultarArmas(a);
            escenario = cE.buscarEscenario(this.IdEscenario);
            personaje = cP.buscarPersonaje(this.IdPersonaje);
            arma = cA.buscarArma(this.IdArma);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEscenario() {
        return escenario;
    }

    public String getPersonaje() {
        return personaje;
    }

    public String getArma() {
        return arma;
    }
}
