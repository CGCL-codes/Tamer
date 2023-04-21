package sirf.util;

/**
 * Operaciones a realizar a partir de BusquedaAvanzadaJson crear un String 
 * con el SQL formado para lanzarlo a la base de datos.
 * 
 * @author Alejandro P. Arcos
 *
 */
public class BusquedaAvanzada {

    private BusquedaAvanzadaJson json;

    private String seccionSelect;

    private String seccionWhere;

    private String seccionFrom;

    private String seccionLimit;

    private String seccionOrder;

    private String seccionInnerJoin;

    public BusquedaAvanzada(BusquedaAvanzadaJson json) {
        this.json = json;
        this.seccionSelect = "SELECT DISTINCT y.id";
        this.seccionFrom = "FROM yacimiento y";
        setWithSomething();
        this.seccionWhere = this.getWhere();
        this.seccionLimit = "";
        this.seccionOrder = "";
    }

    /**
	 * 
	 * @param byOrder
	 * @param order tipo de orden ASC, DESC
	 * @return
	 */
    public void setOrder(String byOrder, String order) {
        this.seccionOrder = "ORDER BY " + byOrder + " " + order;
    }

    public void setLimits(Integer numPagina, Integer numFilaPorPagina) {
        this.seccionLimit = "LIMIT " + numFilaPorPagina + " OFFSET " + ((numPagina - 1) * numFilaPorPagina);
    }

    public String getQuery() {
        String query = String.format("%s %s %s %s %s %s", this.seccionSelect, this.seccionFrom, this.seccionInnerJoin, this.seccionWhere, this.seccionOrder, this.seccionLimit);
        return query;
    }

    public void setSeccionInnerJoin(String seccionInnerJoin) {
        this.seccionInnerJoin = seccionInnerJoin;
    }

    public String getCountQuery() {
        return String.format("SELECT count(*) %s %s", this.seccionFrom, this.seccionWhere);
    }

    private String getWhere() {
        String where = String.format("WHERE (%s) OR (%s)", this.busquedaPorCamposCheckBox(), this.busquedaPorCamposObjeto());
        return where;
    }

    private String operador(boolean op) {
        if (!op) return " OR "; else return " AND ";
    }

    /**
	 * Si un id es 0, nos referimos a todos.
	 * @param field
	 * @param valor
	 * @return
	 */
    private String convertZeroId(String field, Integer valor, boolean operador) {
        if (valor == null || valor == 0) {
            return "";
        } else {
            return field + " = " + valor + this.operador(operador) + ' ';
        }
    }

    @SuppressWarnings("unused")
    private String convertZeroId(boolean operador, String field, Integer valor) {
        if (valor == null || valor == 0) {
            return "";
        } else {
            return this.operador(operador) + ' ' + field + " = " + valor + ' ';
        }
    }

    @SuppressWarnings("unused")
    private String convertZeroId(String field, Integer valor) {
        if (valor == null || valor == 0) {
            return "";
        } else {
            return field + " = " + valor;
        }
    }

    /**
	 * Tablas auxiliares. Crea una sentencia SQL con la consulta de las tablas
	 * intermedias.
	 * 
	 * @param table
	 * @param field
	 * @param valor
	 * @return
	 */
    private String manyToMany(String table, String field, Integer valor) {
        boolean sinWhere = (valor == null || valor == 0);
        String q = "SELECT id_yacimiento FROM " + table;
        String qInFormat = "y.id IN ( %s %s )";
        String where = "";
        if (!sinWhere) {
            where = "WHERE " + field + " = " + valor;
        }
        return String.format(qInFormat, q, where);
    }

    /**
	 * Este m�todo genera a partir de los datos introducido en los comboboxes y los operadores OR/AND
	 * parte del WHERE de la sentencia SQL.
	 * @return Cadena con el formato SQL para el WHERE de la sentencia SQL.
	 */
    private String busquedaPorCamposObjeto() {
        String q = this.convertZeroId("id_tipo", this.json.getId_tipo(), this.json.isOp1()) + this.convertZeroId("id_pais", this.json.getId_pais(), this.json.isOp5()) + this.convertZeroId("id_region", this.json.getId_region(), this.json.isOp2()) + this.convertZeroId("id_tipo_visibilidad", this.json.getId_visibilidad(), this.json.isOp6()) + this.manyToMany("tipo_entorno_yacimiento", "id_tipo_entorno", this.json.getId_entorno()) + ' ' + this.operador(this.json.isOp4()) + ' ' + this.manyToMany("tipo_topografia_yacimiento", "id_tipo_topografia", this.json.getId_topografia());
        return q;
    }

    private void setWithSomething() {
        this.seccionInnerJoin = "";
        if (this.json.isWithFoto()) {
            this.seccionInnerJoin += " INNER JOIN fotografia f ON (y.id = f.id_yacimiento) ";
        }
        if (this.json.isWithBibliografia()) {
            this.seccionInnerJoin += " INNER JOIN bibliografia b ON (y.id = b.id_yacimiento) ";
        }
        if (this.json.isWithEstructura()) {
            this.seccionInnerJoin += " INNER JOIN estructura e ON (y.id = e.id_yacimiento) ";
        }
    }

    /**
	 * Crea la parte de la sentencia SQL en donde buscar.
	 * Se utilizan OR's.
	 * @return Cadena Where con los campos nombre, localidad, descripcion, web, cronologia y geometria.
	 */
    private String busquedaPorCamposCheckBox() {
        String nombre = "1 = 0", localidad = "1 = 0", descripcion = "1 = 0", web = "1 = 0", cronologia = "1 = 0", geometria = "1 = 0";
        if (json.isNombre() || json.buscarEnTodosCampos()) {
            nombre = "y.nombre LIKE '%" + json.getTexto() + "%'";
        }
        if (json.isLocalidad() || json.buscarEnTodosCampos()) {
            localidad = "y.localidad LIKE '%" + json.getTexto() + "%'";
        }
        if (json.isDescripcion() || json.buscarEnTodosCampos()) {
            descripcion = "y.descripcion LIKE '%" + json.getTexto() + "%'";
        }
        if (json.isWeb() || json.buscarEnTodosCampos()) {
            web = "y.web LIKE '%" + json.getTexto() + "%'";
        }
        if (json.isCronologia() || json.buscarEnTodosCampos()) {
            cronologia = "y.cronologia LIKE '%" + json.getTexto() + "%'";
        }
        if (json.isGeometria() || json.buscarEnTodosCampos()) {
            geometria = "y.geometria_predominante LIKE '%" + json.getTexto() + "%'";
        }
        return String.format("%s OR %s OR %s OR %s OR %s OR %s", nombre, localidad, descripcion, web, cronologia, geometria);
    }
}
