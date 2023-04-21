package BaseDeDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConsultarEscenario {

    private List<String> escenarios = new ArrayList();

    private List<Integer> IdEscenarios = new ArrayList();

    public ConsultarEscenario(Connection a) {
        try {
            PreparedStatement selectuser;
            selectuser = a.prepareStatement(" select nombre, idEscenario from Escenario ");
            ResultSet result = selectuser.executeQuery();
            while (result.next()) {
                escenarios.add(result.getString(1));
                IdEscenarios.add(result.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getEscenarios() {
        return escenarios;
    }

    public String buscarEscenario(int idEscenario) {
        int valor = IdEscenarios.indexOf(idEscenario);
        if (valor == 0) return null;
        return escenarios.get(valor);
    }
}
