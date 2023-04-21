package org.hmaciel.descop.otros.Lucene;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.opensih.servicioJMX.invocador.InvocadorService2;
import org.opensih.servicioJMX.invocador.InvocadorServiceBean2;

@Stateless
public class BuscarDoc implements IBuscarDoc {

    public List<String> busqueda(String filtro) {
        List<String> ids = new LinkedList<String>();
        String aux = "";
        try {
            InvocadorService2 inv = InvocadorServiceBean2.getInstance();
            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
            Directory IndexStoreDir = new SimpleFSDirectory(new File(inv.getDirectorioLucene()));
            Query q = new QueryParser(Version.LUCENE_30, null, analyzer).parse(filtro);
            int hitspp = 10000;
            IndexSearcher searcher = new IndexSearcher(IndexStoreDir);
            TopDocsCollector<ScoreDoc> collector = TopScoreDocCollector.create(hitspp, false);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                aux += d.get("idDocument") + ",";
                if (i != 0 && i % 1000 == 0) {
                    if (aux.length() > 0) aux = aux.substring(0, aux.length() - 1);
                    ids.add(aux);
                    aux = "";
                }
            }
            if (aux.length() > 0) {
                aux = aux.substring(0, aux.length() - 1);
                ids.add(aux);
            }
            searcher.close();
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        return ids;
    }

    public String crearFiltro(String exten, String ext_role_pac, String ci_autor, String ci_resp, Date f_cir, Date f_cir2, int idserv, String cirugia, String proc_realizado, String categoria, String oportunidad) {
        String filtro = "";
        if (exten.compareTo("") != 0) {
            filtro += "idDocument:" + exten + " AND ";
        }
        if (ext_role_pac.compareTo("") != 0) {
            filtro += "ciPat:" + ext_role_pac + " AND ";
        }
        if (ci_autor.compareTo("") != 0) {
            filtro += "ciAut:" + ci_autor + " AND ";
        }
        if (ci_resp.compareTo("") != 0) {
            filtro += "ciResp:" + ci_resp + " AND ";
        }
        if (f_cir != null && f_cir2 != null) {
            filtro += "f_cirugia:[" + DateTools.dateToString(f_cir, DateTools.Resolution.DAY) + " TO " + DateTools.dateToString(f_cir2, DateTools.Resolution.DAY) + "] AND ";
        } else if (f_cir != null) {
            filtro += "f_cirugia:[" + DateTools.dateToString(f_cir, DateTools.Resolution.DAY) + " TO " + DateTools.dateToString(new Date(), DateTools.Resolution.DAY) + "] AND ";
        } else if (f_cir2 != null) {
            filtro += "f_cirugia:[" + DateTools.dateToString(new Date(20090101), DateTools.Resolution.DAY) + " TO " + DateTools.dateToString(f_cir2, DateTools.Resolution.DAY) + "] AND ";
        }
        if (idserv > 0) {
            filtro += "servicio:" + idserv + " AND ";
        }
        if (cirugia.compareTo("") != 0) {
            filtro += "cirugia:" + cirugia + " AND ";
        }
        if (proc_realizado.compareTo("") != 0) {
            filtro += "procedimiento:(" + Utils.parsear(Utils.encode(proc_realizado)) + ") AND ";
        }
        if (categoria.compareTo("No Especificada") != 0) {
            filtro += "categoria:" + categoria + " AND ";
        }
        if (oportunidad.compareTo("No Especificada") != 0) {
            filtro += "oportunidad:" + oportunidad + " AND ";
        }
        if (filtro.length() > 5) filtro = filtro.substring(0, filtro.length() - 5);
        return filtro;
    }
}
