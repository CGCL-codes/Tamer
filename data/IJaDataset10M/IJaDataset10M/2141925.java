package queens.daos.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import queens.beans.BusquedaClientesBean;
import queens.beans.ClienteBean;
import queens.daos.IClienteDAO;
import queens.util.Constantes;
import queens.util.Utilidades;

public class ClienteDAO extends NamedParameterJdbcDaoSupport implements IClienteDAO {

    private RowMapper rowMapper = new BeanPropertyRowMapper(ClienteBean.class);

    private static final String INSERTARCLIENTE_SQL = "INSERT INTO " + Tablas.CLIENTE + "(DNI, NOMBRE, APELLIDO1, APELLIDO2, SEXO, FECHANACIMIENTO, ACTIVO, FUMO) VALUES (:dni, :nombre, :apellido1, :apellido2, :sexo, :fechanacimiento, :activo, :fumo)";

    private static final String EDITARCLIENTE_SQL = "UPDATE " + Tablas.CLIENTE + " SET NOMBRE = :nombre, APELLIDO1 = :apellido1, APELLIDO2 = :apellido2, SEXO = :sexo, FECHANACIMIENTO = :fechanacimiento, ACTIVO = :activo, FUMO = :fumo WHERE DNI = :dni";

    private static final String BORRARCLIENTE_SQL = "UPDATE " + Tablas.CLIENTE + " SET ACTIVO = :activo, FUMO = :fumo WHERE DNI = :dni";

    private static final String EXISTECLIENTE_SQL = "SELECT COUNT(*) FROM " + Tablas.CLIENTE + " WHERE DNI = :dni AND ACTIVO = :activo";

    private static final String BUSCARCLIENTES_SQL = "SELECT DNI, NOMBRE, APELLIDO1, APELLIDO2, SEXO, FECHANACIMIENTO, ACTIVO, FUMO FROM " + Tablas.CLIENTE + " WHERE PARAM_CAMPOWHERE LIKE PARAM_DATOWHERE AND ACTIVO = 'S' ORDER BY PARAM_CAMPOORDER";

    private static final String OBTENERCLIENTE_SQL = "SELECT DNI, NOMBRE, APELLIDO1, APELLIDO2, SEXO, FECHANACIMIENTO, ACTIVO, FUMO FROM " + Tablas.CLIENTE + " WHERE DNI = :dni";

    private static final String LISTADOCLIENTES_SQL = "SELECT DNI, NOMBRE, APELLIDO1, APELLIDO2, SEXO, FECHANACIMIENTO, ACTIVO, FUMO FROM " + Tablas.CLIENTE + " WHERE ACTIVO = 'S' ORDER BY PARAM_CAMPOORDER";

    public ClienteDAO() {
        super();
    }

    public void insertarCliente(ClienteBean clienteBean) {
        String fechaNacimientoAux = clienteBean.getFechanacimientoAux();
        if ("".equals(fechaNacimientoAux)) fechaNacimientoAux = Constantes.FECHA_NULA;
        clienteBean.setFechanacimiento(Utilidades.getDate(fechaNacimientoAux));
        clienteBean.setActivo(Constantes.SI);
        clienteBean.setFumo(new Date());
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(clienteBean);
        getNamedParameterJdbcTemplate().update(INSERTARCLIENTE_SQL, namedParameters);
    }

    @SuppressWarnings("unchecked")
    public List buscarClientes(BusquedaClientesBean busquedaClientesBean) {
        String sql = "";
        sql = BUSCARCLIENTES_SQL.replaceAll("PARAM_CAMPOWHERE", busquedaClientesBean.getTipoBusqueda());
        sql = sql.replaceAll("PARAM_DATOWHERE", "'%" + busquedaClientesBean.getTermino() + "%'");
        sql = sql.replaceAll("PARAM_CAMPOORDER", busquedaClientesBean.getOrdenacion());
        return (List) getJdbcTemplate().query(sql, rowMapper);
    }

    public void editarCliente(ClienteBean clienteBean) {
        clienteBean.setFechanacimiento(Utilidades.getDate(clienteBean.getFechanacimientoAux()));
        clienteBean.setFumo(new Date());
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(clienteBean);
        getNamedParameterJdbcTemplate().update(EDITARCLIENTE_SQL, namedParameters);
    }

    @SuppressWarnings("unchecked")
    public boolean existeCliente(String dni, String activo) {
        Map parametros = new HashMap();
        parametros.put("dni", dni);
        parametros.put("activo", activo);
        return (getNamedParameterJdbcTemplate().queryForInt(EXISTECLIENTE_SQL, parametros) > 0);
    }

    @SuppressWarnings("unchecked")
    public ClienteBean obtenerCliente(String dni) {
        Map parametros = new HashMap();
        parametros.put("dni", dni);
        List listaCliente = getNamedParameterJdbcTemplate().query(OBTENERCLIENTE_SQL, parametros, rowMapper);
        if (listaCliente.isEmpty()) {
            return null;
        }
        ClienteBean clienteBean = (ClienteBean) listaCliente.get(0);
        clienteBean.setFechanacimientoAux(Utilidades.getDate(clienteBean.getFechanacimiento()));
        return clienteBean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void borrarCliente(String dni) {
        Map parametros = new HashMap();
        parametros.put("activo", Constantes.NO);
        parametros.put("fumo", new Date());
        parametros.put("dni", dni);
        getNamedParameterJdbcTemplate().update(BORRARCLIENTE_SQL, parametros);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List listadoClientes(String ordenacionListado) {
        String sql = "";
        sql = LISTADOCLIENTES_SQL;
        sql = sql.replaceAll("PARAM_CAMPOORDER", ordenacionListado);
        return (List) getJdbcTemplate().query(sql, rowMapper);
    }
}
