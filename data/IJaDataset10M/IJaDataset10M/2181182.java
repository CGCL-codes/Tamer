package es.randres.aemet.model;

import static org.junit.Assert.*;
import java.sql.Date;
import org.junit.Test;

public class ModelTest {

    private static final String EXPECTED_ID = "0000";

    private static final String EXPECTED_NOMBRE = "EST0";

    private static final Double EXPECTED_LAT = 1.0;

    private static final Double EXPECTED_LONG = 2.0;

    private static final Long EXPECTED_DATE = 0L;

    @Test
    public void EstacionTest() {
        Estacion est = new Estacion(EXPECTED_ID, EXPECTED_NOMBRE, EXPECTED_LAT, EXPECTED_LONG);
        assertTrue(EXPECTED_NOMBRE.equals(est.getNombre()));
        assertTrue(EXPECTED_ID.equals(est.getId()));
        assertTrue(EXPECTED_LAT.equals(est.getLatitud()));
        assertTrue(EXPECTED_LONG.equals(est.getLongitud()));
        assertTrue(est.getDatos() == null);
    }

    @Test
    public void ObservacionTest() {
        Observacion obs = new Observacion(new Date(EXPECTED_DATE));
        assertTrue(EXPECTED_DATE.equals(obs.getFecha().getTime()));
        assertTrue(obs.getEstaciones().isEmpty());
    }
}
