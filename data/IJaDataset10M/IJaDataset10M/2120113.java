package com.netx.cubigraf.servlets;

import java.io.IOException;
import com.netx.cubigraf.entities.Precos;
import com.netx.cubigraf.entities.Referencia;
import com.netx.cubigraf.entities.Servicos;
import com.netx.data.Connection;
import com.netx.data.DatabaseException;
import com.netx.data.RestrictionException;
import com.netx.data.ValueList;
import com.netx.generics.sql.Row;
import com.netx.generics.sql.Table;
import com.netx.generics.util.Strings;
import com.netx.ebs.EbsRequest;
import com.netx.ebs.EbsResponse;
import com.netx.ebs.TemplateValues;
import java.util.ArrayList;
import java.util.List;
import com.netx.cubigraf.entities.Entities;

public class SrvServicoAprecarFicha extends CubigrafServlet {

    public SrvServicoAprecarFicha() {
        super("servico-aprecar");
    }

    protected void doGet(EbsRequest request, EbsResponse response) throws IOException, DatabaseException {
        long cdgServico = request.getLongParameter("codigo", true).longValue();
        Connection c = request.getDatabaseConnection();
        Servicos servicos = Entities.getServicos(c);
        Row servico = servicos.verServicoParaEditar(cdgServico);
        TemplateValues tv = new TemplateValues();
        tv.put("codigo", cdgServico);
        tv.put("referencia", new Referencia(servico.getDate(4), servico.getInt(2)).toString());
        tv.put("nome", servico.getString(3), true);
        tv.put("cliente_val", servico.getString(17));
        tv.put("cliente", servico.getString(18), true);
        tv.put("desenhador", CubigrafUtils.getFirstName(servico.getString(20)), true);
        char tamanho = servico.getChar("tamanho");
        tv.putRadioButton("tamanho_", "oar", tamanho);
        tv.put("tamanho_a_valor", "");
        tv.put("tamanho_r_valor", "");
        if (tamanho == 'a' || tamanho == 'r') {
            tv.put("tamanho_" + tamanho + "_valor", servico.getString("alteracao_tamanho"));
        }
        tv.putOptionValues("tipo_", "oeptrd", servico.getChar("tipo"));
        tv.put("numero_cores", servico.getString("numero_cores"));
        tv.putCheckBox("branco_highlight", servico.getBoolean("branco_highlight"));
        tv.putCheckBox("base", servico.getBoolean("base"));
        tv.putCheckBox("floco", servico.getBoolean("floco"));
        tv.put("medidas", servico.getInt("largura") + "x" + servico.getInt("altura"));
        tv.putOptionValues("camada_", "tf", servico.getChar("camada"));
        tv.put("numero_linhas", servico.getString("numero_linhas"));
        tv.put("num_impressoes", servico.getString("num_impressoes"));
        int usSeleccionada = servico.getInt("cdg_unidade_saida");
        Table unidadeSaida = Entities.getUnidadesSaida(c).procurarUnidadesSaida();
        List<TemplateValues> uList = new ArrayList<TemplateValues>();
        for (int i = 0; i < unidadeSaida.getRowCount(); i++) {
            TemplateValues tvUs = new TemplateValues();
            Row uSaida = unidadeSaida.getRow(i);
            int codigoUS = uSaida.getInt("cdg_unidade_saida");
            tvUs.put("codigo", codigoUS);
            tvUs.put("nome", uSaida.getString("nome"), true);
            tvUs.putOptionValue("seleccionada", usSeleccionada == codigoUS);
            uList.add(tvUs);
        }
        tv.putList("unidades_saida", uList);
        Precos p = (Precos) c.getEntity("Precos");
        Table precos = p.procurarPrecos();
        for (int i = 0; i < 15; i++) {
            if (precos.getDouble("preco", i) == 0) {
                tv.put("radio_" + (i + 1), "&nbsp;");
                tv.put("preco_" + (i + 1), "&nbsp;");
            } else {
                tv.put("radio_" + (i + 1), "<input name=\"preco\" type=\"radio\" value=\"" + precos.getDouble("preco", i) + "\">");
                tv.put("preco_" + (i + 1), precos.getDouble("preco", i) + "");
            }
        }
        double preco = servicos.verPrecoServico(cdgServico);
        tv.put("preco", preco == 0.0 ? "" : Strings.valueOf(preco, 0, 2));
        c.close();
        response.setContentType("text/html");
        response.sendDisableCache();
        response.sendTemplate("servico-aprecar.ficha.tplt", tv);
    }

    public void doPost(EbsRequest request, EbsResponse response) throws IOException, DatabaseException {
        final String operacao = request.getParameter("operacao", true);
        final long cdgServico = request.getLongParameter("codigo", true).longValue();
        ValueList values = new ValueList();
        String tamanho = request.getParameter("tamanho", true);
        values.addValue("tamanho", tamanho);
        values.addValue("alteracao_tamanho", request.getParameter("tamanho_" + tamanho));
        String medidas = request.getParameter("medidas", true);
        int index = medidas.indexOf("x");
        if (index == -1) {
            index = medidas.indexOf("X");
        }
        values.addValue("largura", new Integer((medidas.substring(0, index)).trim()));
        values.addValue("altura", new Integer((medidas.substring(index + 1)).trim()));
        values.addValue("cdg_cliente", request.getParameter("cliente_val", true));
        values.addValue("tipo", request.getParameter("tipo", true));
        values.addValue("numero_cores", request.getParameter("numero_cores", true));
        values.addValue("branco_highlight", request.getBooleanParameter("branco_highlight"));
        values.addValue("base", request.getBooleanParameter("base"));
        values.addValue("floco", request.getBooleanParameter("floco"));
        values.addValue("numero_linhas", request.getParameter("numero_linhas", true));
        values.addValue("num_impressoes", request.getParameter("num_impressoes"));
        values.addValue("camada", request.getParameter("camada", true));
        values.addValue("cdg_unidade_saida", request.getParameter("unidade_saida", true));
        response.setContentType("text/html");
        response.sendDisableCache();
        Connection c = request.getDatabaseConnection();
        Servicos servicos = Entities.getServicos(c);
        try {
            servicos.corrigirServico(cdgServico, values);
            if (operacao.equals("aprecar")) {
                try {
                    boolean facturar = request.getParameter("enviar_facturacao") == null ? false : true;
                    servicos.aprecarServico(cdgServico, request.getDoubleParameter("preco_servico", true).doubleValue(), facturar);
                    int proximo = servicos.proximoParaAprecar(cdgServico);
                    if (proximo == 0) {
                        response.sendRedirect("servico-aprecar.correntes");
                    } else {
                        response.sendRedirect("servico-aprecar.ficha?codigo=" + proximo);
                    }
                } catch (RestrictionException re) {
                    CubigrafUtils.showError(request, response, "ERRO!", "Este servi�o j� est� apre�ado!", true);
                }
            } else {
                response.sendRedirect("servico-aprecar.ficha?codigo=" + cdgServico);
            }
        } catch (RestrictionException re) {
            CubigrafUtils.showError(request, response, "ERRO!", "Este servi�o j� est� apre�ado!", true);
        }
        c.close();
    }
}
