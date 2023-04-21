package br.ufrj.cad.view.relatorio;

import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.lowagie.text.pdf.PdfPCell;
import br.ufrj.cad.fwk.util.ResourceUtil;
import br.ufrj.cad.model.bo.AnoBase;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.disciplina.DisciplinaEscolhidaService;
import br.ufrj.cad.view.util.DocumentoPDF;
import br.ufrj.cad.view.util.DocumentoPDFUtil;
import br.ufrj.cad.view.util.TabelaDadosPDF;

public class RelatorioHelper {

    public static ByteArrayOutputStream gerarPdfDisciplinasEscolhidas(AnoBase anobase, Usuario usuario, HttpServletRequest request) {
        ByteArrayOutputStream resultado = new ByteArrayOutputStream();
        List<Usuario> usuarios = DisciplinaEscolhidaService.getInstance().retornarUsuariosComDisciplinasEscolhidas(anobase, usuario);
        TabelaDadosDisciplinaEscolhida tabelaDadosDisciplinaEscolhida = new TabelaDadosDisciplinaEscolhida(new String[] { "" });
        tabelaDadosDisciplinaEscolhida.setAnobase(anobase);
        PdfPCell celulaCabecalho = DocumentoPDFUtil.criaCelulaTitulo(ResourceUtil.getResourceMessage("_nls.mensagem.relatorio.disciplinas.escolhidas.titulo"));
        celulaCabecalho.setHorizontalAlignment(TabelaDadosPDF.ALIGN_LEFT);
        DocumentoPDF pdf = new DocumentoPDF(celulaCabecalho, DocumentoPDFUtil.criaGap(), tabelaDadosDisciplinaEscolhida, request);
        pdf.adicionaLinhas(usuarios);
        try {
            pdf.writePDFBytes(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return resultado;
    }
}
