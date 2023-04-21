package br.com.newti.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import br.com.newti.dao.CTRCDAO;
import br.com.newti.parser.CTRCParser;
import br.com.newti.util.DateUtils;

@Path("/ctrc")
public class CTRCService {

    @Path("/por-data")
    @GET
    public String getPorDataAlteracao(@QueryParam("dataIni") String dataIni, @QueryParam("dataFim") String dataFim) {
        Date d1 = null;
        Date d2 = null;
        if (dataIni == null || dataFim == null) throw new IllegalArgumentException();
        d1 = DateUtils.parseFromString(dataIni, "yyyy-MM-dd");
        d2 = DateUtils.parseFromString(dataFim, "yyyy-MM-dd");
        Map<Integer, Map<String, Object>> ctrcByData = CTRCDAO.getByDataModificacao(d1, d2);
        return CTRCParser.listMapToXML(ctrcByData);
    }
}
