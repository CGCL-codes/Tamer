package javaframework.capadeaccesoadatos.bd;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.Date;
import base.ClaseAbstractaBaseRastreada;
import javaframework.capadeaplicación.mensajes.rastreo.rastreador.Rastreador;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Representa un gestor abstrato de acceso a un SGBD. Por medio de esta clase es posible establecer una
 * conexión con una base de datos, ejecutar consultas SQL y procedimientos almacenados así como
 * gestionar el control a nivel transaccional.
 * 
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b> 01/01/2007<br/>
 * <b>· Revisiones:</b> 02/05/2010<br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version InterfazNavegadorBD.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
public abstract class ClaseAbstractaNavegadorBD extends ClaseAbstractaBaseRastreada implements InterfazNavegadorBD {

    private java.sql.Connection conexión;

    protected void setConexión(final Connection conexión) {
        this.conexión = conexión;
    }

    private Connection getConexión() {
        return this.conexión;
    }

    /**
	 * Instancia un objeto <code>ClaseAbstractaNavegadorBD</code>
	 *
	 * @param rastreador	Objeto <code>Rastreador</code> que efectuará el seguimiento del objeto. Si
	 *						se especifica un valor <code>null</code>, no se efectúa el seguimiento.
	 *
	 */
    public ClaseAbstractaNavegadorBD(final Rastreador rastreador) {
        super(rastreador);
    }

    @Override
    public void conectar(final DataSource poolDeConexiones) {
        try {
            final Connection CONEXIÓN = poolDeConexiones.getConnection();
            this.setConexión(CONEXIÓN);
        } catch (Exception e) {
        }
    }

    @Override
    public void conectar(final NombresDeDriver nombreDeDriver, final String cadenaDeConexión) {
        try {
            boolean conectar = false;
            if (this.getConexión() == null) {
                conectar = true;
            } else {
                try {
                    if (this.getConexión().isClosed()) conectar = true;
                } catch (Exception e2) {
                    conectar = true;
                }
            }
            if (conectar) {
                Class.forName(nombreDeDriver.getValor());
                final Connection CONEXIÓN = DriverManager.getConnection(cadenaDeConexión);
                this.setConexión(CONEXIÓN);
            }
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "conectar", null, null);
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "conectar", e, null);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Statement crearSentenciaDeSelección(final TiposDeDesplazamientoEnResultset tipoDeDesplazamientoEnResultset, final TiposDeAccesoEnResultset tipoDeAccesoEnResultset) {
        try {
            java.sql.Statement sentencia = this.getConexión().createStatement(tipoDeDesplazamientoEnResultset.getValor(), tipoDeAccesoEnResultset.getValor());
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaDeSelección", null, null, tipoDeDesplazamientoEnResultset, tipoDeAccesoEnResultset);
            return sentencia;
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaDeSelección", e, null, tipoDeDesplazamientoEnResultset, tipoDeAccesoEnResultset);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ResultSet ejecutarSentenciaDeSelección(final String consultaSQL, final Statement sentenciaSQL) {
        try {
            sentenciaSQL.executeQuery(consultaSQL);
            final ResultSet RS = sentenciaSQL.getResultSet();
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "ejecutarSentenciaDeSelección", null, null, consultaSQL, sentenciaSQL);
            return RS;
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "ejecutarSentenciaDeSelección", e, null, consultaSQL, sentenciaSQL);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Statement crearSentenciaDeModificación() {
        try {
            java.sql.Statement sentencia = this.getConexión().createStatement();
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaDeModificación", null, null);
            return sentencia;
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaDeModificación", e, null);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int ejecutarSentenciaDeModificación(final String consultaSQL, final Statement sentenciaSQL) {
        try {
            final int númeroDeFilasAfectadas = sentenciaSQL.executeUpdate(consultaSQL);
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "ejecutarSentenciaDeModificación", null, null, consultaSQL, sentenciaSQL);
            return númeroDeFilasAfectadas;
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "ejecutarSentenciaDeModificación", e, null, consultaSQL, sentenciaSQL);
            return -1;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public PreparedStatement crearSentenciaPrecompiladaDeSelección(final String consultaSQL, final TiposDeDesplazamientoEnResultset tipoDeDesplazamientoEnResultset, final TiposDeAccesoEnResultset tipoDeAccesoEnResultset) {
        try {
            java.sql.PreparedStatement sentenciaPrecompilada = this.getConexión().prepareStatement(consultaSQL, tipoDeDesplazamientoEnResultset.getValor(), tipoDeAccesoEnResultset.getValor());
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaPrecompiladaDeSelección", null, null, consultaSQL, tipoDeDesplazamientoEnResultset, tipoDeAccesoEnResultset);
            return sentenciaPrecompilada;
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaPrecompiladaDeSelección", e, null, consultaSQL, tipoDeDesplazamientoEnResultset, tipoDeAccesoEnResultset);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ResultSet ejecutarSentenciaPrecompiladaDeSelección(final PreparedStatement sentenciaPrecompilada) {
        try {
            sentenciaPrecompilada.executeQuery();
            final ResultSet RS = sentenciaPrecompilada.getResultSet();
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "ejecutarSentenciaPrecompiladaDeSelección", null, null, sentenciaPrecompilada);
            return RS;
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "ejecutarSentenciaPrecompiladaDeSelección", e, null, sentenciaPrecompilada);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public PreparedStatement crearSentenciaPrecompiladaDeModificación(final String consultaSQL) {
        try {
            java.sql.PreparedStatement sentenciaPrecompilada = getConexión().prepareStatement(consultaSQL);
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaPrecompiladaDeModificación", null, null, consultaSQL);
            return sentenciaPrecompilada;
        } catch (Exception e) {
            super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "crearSentenciaPrecompiladaDeModificación", null, null, consultaSQL);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int ejecutarSentenciaPrecompiladaDeModificación(final PreparedStatement sentenciaPrecompilada) {
        try {
            final int númeroDeFilasAfectadas = sentenciaPrecompilada.executeUpdate();
            return númeroDeFilasAfectadas;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public CallableStatement crearLlamadaAProcedimientoAlmacenado(final String consultaSQL, final TiposDeDesplazamientoEnResultset tipoDeDesplazamientoEnResultset, final TiposDeAccesoEnResultset tipoDeAccesoEnResultset) {
        try {
            java.sql.CallableStatement llamadaAProcedimientoRemoto = this.getConexión().prepareCall("{ call " + consultaSQL + " }", tipoDeDesplazamientoEnResultset.getValor(), tipoDeAccesoEnResultset.getValor());
            return llamadaAProcedimientoRemoto;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void ejecutarProcedimientoAlmacenado(final CallableStatement llamadaAProcedimientoAlmacenado) {
        try {
            llamadaAProcedimientoAlmacenado.execute();
        } catch (Exception e) {
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void cambiarModoTransaccional(final boolean activado) {
        try {
            this.getConexión().setAutoCommit(!activado);
        } catch (Exception e) {
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Savepoint crearPuntoDeControl(final String nombrePuntoDeControl) {
        try {
            java.sql.Savepoint puntoDeControl = this.getConexión().setSavepoint(nombrePuntoDeControl);
            return puntoDeControl;
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void consolidarTransacción() {
        try {
            this.getConexión().commit();
        } catch (Exception e) {
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void revertirTransacción(final Savepoint puntoDeControl) {
        try {
            if (puntoDeControl == null) this.getConexión().rollback(); else this.getConexión().rollback(puntoDeControl);
        } catch (Exception e) {
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int obtenerNúmeroDeFilasEnResultset(ResultSet rs) {
        try {
            int númeroDeFilas;
            if (rs != null) {
                if (rs.getType() != java.sql.ResultSet.TYPE_FORWARD_ONLY) {
                    int filaActual;
                    if (rs.isAfterLast()) filaActual = -2; else {
                        if (rs.isBeforeFirst()) filaActual = -1; else filaActual = rs.getRow();
                    }
                    rs.last();
                    númeroDeFilas = rs.getRow();
                    if (filaActual == -2) rs.afterLast(); else {
                        if (filaActual == -1) rs.beforeFirst(); else rs.absolute(filaActual);
                    }
                } else númeroDeFilas = -1;
            } else númeroDeFilas = -1;
            return númeroDeFilas;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void desconectar() {
        try {
            if (this.getConexión() != null) this.getConexión().close();
        } catch (Exception e) {
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void liberarRecursos() {
        try {
            this.desconectar();
            this.setConexión(null);
            super.liberarRecursos();
        } catch (Exception e) {
        }
    }
}
