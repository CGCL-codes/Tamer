package com.organizadordeeventos.core.utils;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

public class UtilitarioDB {

    private static DataSource dataSource;

    public void setDataSource(final DataSource dataSource) {
        UtilitarioDB.dataSource = dataSource;
    }

    private static void inicializar(final String nombreProcedimientoInicializacion) {
        final SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource);
        simpleJdbcCall.withProcedureName(nombreProcedimientoInicializacion);
        simpleJdbcCall.execute();
    }

    private static int obtener(final String nombreFuncionObtencion, final String nombreProcedimientoInicializacion) {
        final SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource);
        simpleJdbcCall.withFunctionName(nombreFuncionObtencion);
        Integer nroObtenido;
        do {
            nroObtenido = simpleJdbcCall.executeFunction(Integer.class, new HashMap<String, Object>());
            if (nroObtenido == null) {
                inicializar(nombreProcedimientoInicializacion);
            }
        } while (nroObtenido == null);
        return nroObtenido.intValue();
    }

    public static int obtenerNroPresupuestoActual() {
        return obtener("nroPresupuestoActual", "inicializarSecuenciaNroPresupuesto");
    }

    public static int obtenerNroPresupuestoSiguiente() {
        return obtener("nroPresupuestoSiguiente", "inicializarSecuenciaNroPresupuesto");
    }

    public static int obtenerCodigoUsuarioActual() {
        return obtener("codigoUsuarioActual", "inicializarSecuenciaCodigoUsuario");
    }

    public static int obtenerCodigoUsuarioSiguiente() {
        return obtener("codigoUsuarioSiguiente", "inicializarSecuenciaCodigoUsuario");
    }

    public static int obtenerCodigoServicioActual() {
        return obtener("codigoServicioActual", "inicializarSecuenciaCodigoServicio");
    }

    public static int obtenerCodigoServicioSiguiente() {
        return obtener("codigoServicioSiguiente", "inicializarSecuenciaCodigoServicio");
    }
}
