package alertaboletin.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import alertaboletin.indexer.IndexSearcherCache;

public class Alerta {

    private int id;

    private int idUsuario;

    private String nombre;

    private String criterio;

    private int diasAntes;

    private int periodicidad;

    private int numeroResultados;

    private boolean activado;

    private Date ultimoEnvio;

    private static Logger logger = Logger.getLogger(Alerta.class.getName());

    public Alerta(int id, int idUsuario, String nombre, String criterio, int diasAntes, int periodicidad, int numeroResultados, boolean activado, Date ultimoEnvio) {
        super();
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.criterio = criterio;
        this.diasAntes = diasAntes;
        this.periodicidad = periodicidad;
        this.numeroResultados = numeroResultados;
        this.activado = activado;
        this.ultimoEnvio = ultimoEnvio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreCorto() {
        if (nombre.length() > 30) {
            return nombre.substring(0, 30);
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCriterio() {
        return criterio;
    }

    public String getCriterioCorto() {
        if (criterio.length() > 30) {
            return criterio.substring(0, 30);
        }
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public int getDiasAntes() {
        return diasAntes;
    }

    public void setDiasAntes(int diasAntes) {
        this.diasAntes = diasAntes;
    }

    public int getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(int periodicidad) {
        this.periodicidad = periodicidad;
    }

    public int getNumeroResultados() {
        return numeroResultados;
    }

    public void setNumeroResultados(int numeroResultados) {
        this.numeroResultados = numeroResultados;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    public Date getUltimoEnvio() {
        return ultimoEnvio;
    }

    public void setUltimoEnvio(Date ultimoEnvio) {
        this.ultimoEnvio = ultimoEnvio;
    }

    @SuppressWarnings("deprecation")
    public ResultadoEjecucion ejecutar() throws Exception {
        ArrayList<Document> tmp = new ArrayList<Document>();
        logger.info("Ejecutando alerta: " + this.toString());
        IndexSearcher searcher = null;
        Query queryTexto = null;
        searcher = IndexSearcherCache.getIndexSearcher();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        QueryParser qp = new QueryParser(Version.LUCENE_CURRENT, "contents", analyzer);
        qp.setDefaultOperator(QueryParser.Operator.AND);
        queryTexto = qp.parse(this.criterio);
        TopDocs hits = null;
        if (diasAntes > 0) {
            Calendar cal = Calendar.getInstance();
            final java.util.Date to = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, -(diasAntes + 1));
            final java.util.Date from = cal.getTime();
            final String sFrom = DateTools.dateToString(from, DateTools.Resolution.SECOND);
            final String sTo = DateTools.dateToString(to, DateTools.Resolution.SECOND);
            TermRangeQuery queryFecha = new TermRangeQuery("modified", sFrom, sTo, true, true);
            BooleanQuery booleanQuery = new BooleanQuery();
            booleanQuery.add(queryTexto, BooleanClause.Occur.MUST);
            booleanQuery.add(queryFecha, BooleanClause.Occur.MUST);
            Sort sort = new Sort(new SortField("modified", SortField.STRING, true));
            hits = searcher.search(booleanQuery, null, this.numeroResultados, sort);
        } else {
            Sort sort = new Sort(new SortField("modified", SortField.STRING, true));
            hits = searcher.search(queryTexto, null, this.numeroResultados, sort);
        }
        int num = numeroResultados;
        if (hits.totalHits < numeroResultados) {
            num = hits.totalHits;
        }
        for (int i = 0; i < num; i++) {
            Document doc = searcher.doc(hits.scoreDocs[i].doc);
            tmp.add(doc);
        }
        Document[] ret = new Document[tmp.size()];
        tmp.toArray(ret);
        return new ResultadoEjecucion(hits.totalHits, ret);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (activado ? 1231 : 1237);
        result = prime * result + ((criterio == null) ? 0 : criterio.hashCode());
        result = prime * result + diasAntes;
        result = prime * result + id;
        result = prime * result + idUsuario;
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + numeroResultados;
        result = prime * result + periodicidad;
        result = prime * result + ((ultimoEnvio == null) ? 0 : ultimoEnvio.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Alerta other = (Alerta) obj;
        if (activado != other.activado) return false;
        if (criterio == null) {
            if (other.criterio != null) return false;
        } else if (!criterio.equals(other.criterio)) return false;
        if (diasAntes != other.diasAntes) return false;
        if (id != other.id) return false;
        if (idUsuario != other.idUsuario) return false;
        if (nombre == null) {
            if (other.nombre != null) return false;
        } else if (!nombre.equals(other.nombre)) return false;
        if (numeroResultados != other.numeroResultados) return false;
        if (periodicidad != other.periodicidad) return false;
        if (ultimoEnvio == null) {
            if (other.ultimoEnvio != null) return false;
        } else if (!ultimoEnvio.equals(other.ultimoEnvio)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Alerta [activado=" + activado + ", criterio=" + criterio + ", diasAntes=" + diasAntes + ", id=" + id + ", idUsuario=" + idUsuario + ", nombre=" + nombre + ", numeroResultados=" + numeroResultados + ", periodicidad=" + periodicidad + ", ultimoEnvio=" + ultimoEnvio + "]";
    }
}
