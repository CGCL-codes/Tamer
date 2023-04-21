package com.netx.cubigraf.servlets;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.netx.data.Connection;
import com.netx.data.DatabaseException;
import com.netx.ebs.EbsRequest;
import com.netx.ebs.EbsResponse;
import com.netx.ebs.TemplateValues;
import com.netx.generics.sql.Row;
import com.netx.generics.sql.Table;
import com.netx.generics.util.Strings;
import com.netx.cubigraf.entities.Entities;
import com.netx.cubigraf.entities.Referencia;

public class SrvFichasEntregaFicha extends CubigrafServlet {

    public SrvFichasEntregaFicha() {
        super("fichas-ver");
    }

    public void doGet(EbsRequest request, EbsResponse response) throws IOException, DatabaseException {
        response.sendDisableCache();
        Connection c = request.getDatabaseConnection();
        long cdgFicha = request.getLongParameter("codigo", true).longValue();
        Table[] result = Entities.getFichasEntrega(c).verFichaEntrega(cdgFicha);
        Row ficha = result[0].getRow(0);
        Table servicos = result[1];
        TemplateValues tv = new TemplateValues();
        tv.put("numero", ficha.getString(1));
        tv.put("cliente", ficha.getString(2));
        tv.put("data_criacao", ficha.getDate(3).format());
        tv.put("facturacao", ficha.getBoolean(4) ? "A" : "B");
        double total = 0;
        List<TemplateValues> svc = new ArrayList<TemplateValues>();
        for (int i = 0; i < servicos.getRowCount(); i++) {
            TemplateValues tv2 = new TemplateValues();
            tv2.put("codigo_servico", servicos.getString(1, i));
            tv2.put("referencia", new Referencia(servicos.getDate(4, i), servicos.getInt(3, i)).toString());
            tv2.put("designacao", servicos.getString(2, i));
            tv2.put("num_cores", servicos.getString(5, i));
            tv2.put("medidas", servicos.getString(6, i) + "x" + servicos.getString(7, i));
            double preco = Entities.getServicos(c).verPrecoServico(servicos.getInt(1, i));
            tv2.put("preco", Strings.valueOf(preco, 0, 2));
            total += preco;
            svc.add(tv2);
        }
        tv.put("total", Strings.valueOf(total, 0, 2));
        tv.putList("servicos", svc);
        c.close();
        response.setContentType("text/html");
        response.sendDisableCache();
        response.sendTemplate("fichas-entrega.ficha.tplt", tv);
    }
}
