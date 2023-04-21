package br.unb.cic.gerval.server.chart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import br.unb.cic.gerval.client.NegocioException;
import br.unb.cic.gerval.client.rpc.vo.ConsultaTestesVO;
import br.unb.cic.gerval.client.rpc.vo.Teste;
import br.unb.cic.gerval.server.dao.impl.TesteDAO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * A simple demonstration application showing how to create a bar chart.
 */
public class Graficos {

    public static final String[] NOMES_MESES_CURTOS = { "jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez" };

    public static final String SERIES_VALIDACOES = "Validações";

    public static final String SERIES_PROBLEMAS_ENCONTRADOS = "Média de Problemas Encontrados";

    public static final Comparable SERIES_TEMPO_MEDIO = "Tempo Médio";

    public static final Comparable SERIES_APROVADOS = "Aprovados";

    public static final Comparable SERIES_REPROVADOS = "Reprovados";

    public static final String KEY_GRAFICO_SESSAO = "grafico_gerado";

    public static final Logger LOGGER = Logger.getLogger(Graficos.class);

    private TesteDAO testeDAO;

    public CategoryDataset getDatasetGraficoValidacoesProblemasEncontrados(ConsultaTestesVO consulta) throws NegocioException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        consulta.setConsideraRodadasAnteriores(true);
        List testes = testeDAO.consultaTestes(consulta);
        Calendar cal = Calendar.getInstance();
        Iterator it = testes.iterator();
        int mesAtual = -1;
        int qtdeTestesDoMes = 0;
        int somaQtdeProblemas = 0;
        while (it.hasNext()) {
            Teste t = (Teste) it.next();
            cal.setTime(t.getDataSolicitacao());
            int mes = cal.get(Calendar.MONTH);
            if (mes != mesAtual) {
                if (mesAtual > -1) {
                    dataset.addValue(qtdeTestesDoMes, SERIES_VALIDACOES, NOMES_MESES_CURTOS[mesAtual]);
                    dataset.addValue((double) somaQtdeProblemas / (double) qtdeTestesDoMes, SERIES_PROBLEMAS_ENCONTRADOS, NOMES_MESES_CURTOS[mesAtual]);
                }
                qtdeTestesDoMes = 0;
                somaQtdeProblemas = 0;
                mesAtual = mes;
            }
            qtdeTestesDoMes += 1;
            somaQtdeProblemas += t.getProblemas().size();
        }
        if (testes.size() > 0) {
            dataset.addValue(qtdeTestesDoMes, SERIES_VALIDACOES, NOMES_MESES_CURTOS[mesAtual]);
            dataset.addValue((double) somaQtdeProblemas / (double) qtdeTestesDoMes, SERIES_PROBLEMAS_ENCONTRADOS, NOMES_MESES_CURTOS[mesAtual]);
        }
        return dataset;
    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset, String titulo) {
        JFreeChart chart = ChartFactory.createBarChart(titulo, "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, new Color(0, 64, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        return chart;
    }

    /**
     * Converts a JFreeChart to PDF syntax.
     * @param filename	the name of the PDF file
     * @param chart		the JFreeChart
     * @param width		the width of the resulting PDF
     * @param height	the height of the resulting PDF
     */
    public static void convertToPdf(JFreeChart chart, int width, int height, String filename) {
        Document document = new Document(new Rectangle(width, height));
        try {
            PdfWriter writer;
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D r2d = new Rectangle2D.Double(0, 0, width, height);
            chart.draw(g2d, r2d);
            g2d.dispose();
            cb.addTemplate(tp, 0, 0);
        } catch (DocumentException de) {
            de.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.close();
    }

    /**
     * 
     * gera o pdf e retorna o array de bytes
     * 
     * @param filename	the name of the PDF file
     * @param chart		the JFreeChart
     * @param width		the width of the resulting PDF
     * @param height	the height of the resulting PDF
     */
    public static byte[] getPDFBytes(JFreeChart chart, int width, int height) {
        Document document = new Document(new Rectangle(width, height));
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
            PdfWriter writer;
            writer = PdfWriter.getInstance(document, buf);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D r2d = new Rectangle2D.Double(0, 0, width, height);
            chart.draw(g2d, r2d);
            g2d.dispose();
            cb.addTemplate(tp, 0, 0);
        } catch (DocumentException de) {
            de.printStackTrace();
            LOGGER.error("erro na geração dos bytes do gráfico", de);
        }
        document.close();
        return buf.toByteArray();
    }

    public TesteDAO getTesteDAO() {
        return testeDAO;
    }

    public void setTesteDAO(TesteDAO testeDAO) {
        this.testeDAO = testeDAO;
    }

    public CategoryDataset getDatasetGraficoValidacoesTempoMedio(ConsultaTestesVO consulta) throws NegocioException {
        Calendar cal = Calendar.getInstance();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        consulta.setConsideraRodadasAnteriores(true);
        List testes = testeDAO.consultaTestes(consulta);
        Iterator it = testes.iterator();
        int mesAtual = -1;
        int qtdeTestesDoMes = 0;
        double somaDuracoes = 0;
        while (it.hasNext()) {
            Teste t = (Teste) it.next();
            LOGGER.debug("teste " + t.getId() + " data inicio " + t.getDataInicioTesteLong());
            if (t.getDataInicioTeste() == null || t.getDataFimTeste() == null) {
                LOGGER.debug("desconsidera ");
                continue;
            }
            cal.setTime(t.getDataSolicitacao());
            int mes = cal.get(Calendar.MONTH);
            if (mes != mesAtual) {
                if (mesAtual > -1) {
                    dataset.addValue(qtdeTestesDoMes, SERIES_VALIDACOES, NOMES_MESES_CURTOS[mesAtual]);
                    dataset.addValue(somaDuracoes / (double) qtdeTestesDoMes, SERIES_TEMPO_MEDIO, NOMES_MESES_CURTOS[mesAtual]);
                }
                qtdeTestesDoMes = 0;
                somaDuracoes = 0;
                mesAtual = mes;
            }
            double duracao = t.getDataFimTeste().getTime() - t.getDataInicioTeste().getTime();
            duracao = (double) duracao / (double) (1000 * 60 * 60 * 24);
            qtdeTestesDoMes += 1;
            somaDuracoes += duracao;
        }
        dataset.addValue(qtdeTestesDoMes, SERIES_VALIDACOES, NOMES_MESES_CURTOS[mesAtual]);
        dataset.addValue((double) somaDuracoes / (double) qtdeTestesDoMes, SERIES_TEMPO_MEDIO, NOMES_MESES_CURTOS[mesAtual]);
        return dataset;
    }

    public CategoryDataset getDatasetGraficoAprovadsReprovados(ConsultaTestesVO consulta) throws NegocioException {
        Calendar cal = Calendar.getInstance();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        consulta.setConsideraRodadasAnteriores(true);
        List testes = testeDAO.consultaTestes(consulta);
        Iterator it = testes.iterator();
        int mesAtual = -1;
        int qtdeAprovadosMes = 0;
        int qtdeReprovadosMes = 0;
        while (it.hasNext()) {
            Teste t = (Teste) it.next();
            cal.setTime(t.getDataSolicitacao());
            int mes = cal.get(Calendar.MONTH);
            if (mes != mesAtual) {
                if (mesAtual > -1) {
                    dataset.addValue(qtdeAprovadosMes, SERIES_APROVADOS, NOMES_MESES_CURTOS[mesAtual]);
                    dataset.addValue(qtdeReprovadosMes, SERIES_REPROVADOS, NOMES_MESES_CURTOS[mesAtual]);
                }
                qtdeAprovadosMes = 0;
                qtdeReprovadosMes = 0;
                mesAtual = mes;
            }
            if (t.getEstado().equals(Teste.TESTE_REJEITADO)) {
                qtdeReprovadosMes += 1;
            } else if (t.getEstado().equals(Teste.TESTE_APROVADO_TECNICO) || t.getEstado().equals(Teste.TESTE_APROVADO_DIRETOR) || t.getEstado().equals(Teste.TESTE_APROVADO_LIDER)) {
                qtdeAprovadosMes += 1;
            }
        }
        dataset.addValue(qtdeAprovadosMes, SERIES_APROVADOS, NOMES_MESES_CURTOS[mesAtual]);
        dataset.addValue(qtdeReprovadosMes, SERIES_REPROVADOS, NOMES_MESES_CURTOS[mesAtual]);
        return dataset;
    }

    /**
	 * Retorna os bytes do pdf já prontinho pra enviar pro browser
	 * 
	 * @param consulta
	 * @return
	 * @throws NegocioException
	 */
    public byte[] getBytesGraficoValidacoesProblemasEncontrados(ConsultaTestesVO consulta) throws NegocioException {
        CategoryDataset dataset = getDatasetGraficoValidacoesProblemasEncontrados(consulta);
        JFreeChart chart = createChart(dataset, "Validações X Média de Problemas Encontrados");
        return getPDFBytes(chart, 400, 400);
    }

    public byte[] getBytesGraficoValidacoesTempoMedio(ConsultaTestesVO consulta) throws NegocioException {
        CategoryDataset dataset = getDatasetGraficoValidacoesTempoMedio(consulta);
        JFreeChart chart = createChart(dataset, "Validações X Tempo médio");
        return getPDFBytes(chart, 400, 400);
    }

    public byte[] getBytesGraficoAprovadosReprovados(ConsultaTestesVO consulta) throws NegocioException {
        CategoryDataset dataset = getDatasetGraficoAprovadsReprovados(consulta);
        JFreeChart chart = createChart(dataset, "Aprovações X Reprovações");
        return getPDFBytes(chart, 400, 400);
    }
}
