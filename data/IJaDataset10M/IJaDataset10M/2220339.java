package es.cea;

import org.testng.annotations.Test;

public class ServicioGoogleTest {

    @Test
    public void conecta() throws ParametroNuloException {
        ServicioGoogle servicio = new ServicioGoogle();
        boolean estado = servicio.conecta("admin", "admin");
        assert (estado) : "Se esperaba conectar con admin";
        boolean estado2 = servicio.conecta("cea", "cea");
        assert (estado2);
        try {
            servicio.conecta(null, "");
            assert (false);
        } catch (ParametroNuloException e) {
            assert (true);
        }
    }
}
