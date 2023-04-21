package sql.dml.selecao.filtro;

public class FiltroSQLSimpleBasico extends FiltroSQl {

    String sql = null;

    String preFiltro = AND;

    public FiltroSQLSimpleBasico(String sql) {
        this.sql = sql;
    }

    public String codigoSQL() {
        return sql;
    }

    public String operadorCondicionalComOutrosFiltros() {
        return preFiltro;
    }
}
