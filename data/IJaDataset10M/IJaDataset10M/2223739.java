package net.sourceforge.dashbov.persistencia.sistema_de_arquivos;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.sourceforge.dashbov.entidades.Tipos.Andamento;
import net.sourceforge.dashbov.persistencia.comum.Parametrizacao;

/**
 *
 * @author everton
 */
public class ManipuladorDeArquivos {

    private static final int BYTES_POR_LEITURA = 1024;

    private static final Preferences PREFERENCIAS_DO_USUARIO = Preferences.userNodeForPackage(ManipuladorDeArquivos.class);

    public static File obterPastaPessoal() {
        File pastaPessoal = new File(System.getProperty("user.home"), ".dashbov");
        if (!pastaPessoal.exists()) {
            pastaPessoal.mkdir();
        }
        return pastaPessoal;
    }

    public static File baixarPlanilhaDoHistoricoDosProventosEmDinheiro(Andamento pObservador) throws MalformedURLException, IOException {
        File diretorioParaGravacaoDoArquivo = obterPastaPessoal();
        String enderecoDoArquivoASerBaixado = Parametrizacao.obterConfiguracaoDoSistema("url_arquivo_proventos_em_dinheiro"), itemDesejado = null;
        int tamanhoPadraoDoArquivoASerBaixado = -1;
        return baixarArquivoDaInternet(enderecoDoArquivoASerBaixado, -1, itemDesejado, diretorioParaGravacaoDoArquivo, pObservador);
    }

    public static File baixarPlanilhaDeSetores(Andamento pObservador) throws MalformedURLException, IOException {
        File diretorioParaGravacaoDoArquivo = obterPastaPessoal();
        Locale localidade = Locale.getDefault();
        String itemDesejado = null;
        int tamanhoPadraoDoArquivoASerBaixado = -1;
        String enderecoDoArquivoASerBaixado;
        if (localidade.equals(new Locale("pt", "BR"))) {
            enderecoDoArquivoASerBaixado = Parametrizacao.obterConfiguracaoDoSistema("url_arquivo_classificacao_setorial_pt-br");
        } else {
            enderecoDoArquivoASerBaixado = Parametrizacao.obterConfiguracaoDoSistema("url_arquivo_classificacao_setorial_en-us");
        }
        return baixarArquivoDaInternet(enderecoDoArquivoASerBaixado, tamanhoPadraoDoArquivoASerBaixado, itemDesejado, diretorioParaGravacaoDoArquivo, pObservador);
    }

    public static File baixarArquivoDasEmpresasAbertas(Andamento pObservador) throws MalformedURLException, IOException {
        File diretorioParaGravacaoDoArquivo = obterPastaPessoal();
        String enderecoDoArquivoASerBaixado = Parametrizacao.obterConfiguracaoDoSistema("url_arquivo_empresas_abertas"), itemDesejado = null;
        int tamanhoPadraoDoArquivoASerBaixado = -1;
        return baixarArquivoDaInternet(enderecoDoArquivoASerBaixado, tamanhoPadraoDoArquivoASerBaixado, itemDesejado, diretorioParaGravacaoDoArquivo, pObservador);
    }

    public static File baixarArquivoDosEmissoresDeTitulosFinanceiros(Andamento pObservador) throws MalformedURLException, IOException {
        File diretorioParaGravacaoDoArquivo = obterPastaPessoal();
        String enderecoDoArquivoASerBaixado = Parametrizacao.obterConfiguracaoDoSistema("url_arquivo_emissores_titulos"), itemDesejado = "EMISSOR.TXT";
        long TAMANHO_PADRAO_DO_ARQUIVO = 2932841;
        return baixarArquivoDaInternet(enderecoDoArquivoASerBaixado, TAMANHO_PADRAO_DO_ARQUIVO, itemDesejado, diretorioParaGravacaoDoArquivo, pObservador);
    }

    private static String obterEnderecoDaPlanilhaDoBancoDeDados() {
        final String MODELO_DO_ENDERECO_DO_ARQUIVO_A_SER_BAIXADO = Parametrizacao.obterConfiguracaoDoSistema("url_arquivo_banco_dados_dari");
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatador = new SimpleDateFormat("MMMM_yyyy", new Locale("pt", "BR"));
        calendario.add(Calendar.MONTH, -1);
        String sufixoPenultimoMes = formatador.format(calendario.getTime());
        calendario.add(Calendar.MONTH, -1);
        String sufixoAntepenultimoMes = formatador.format(calendario.getTime());
        String enderecoDoArquivoASerBaixado = null;
        String[] enderecosPossiveis = { MODELO_DO_ENDERECO_DO_ARQUIVO_A_SER_BAIXADO.replace("{MES_ANO}", sufixoPenultimoMes).replace("{EXTENSAO}", "xlsx"), MODELO_DO_ENDERECO_DO_ARQUIVO_A_SER_BAIXADO.replace("{MES_ANO}", sufixoPenultimoMes).replace("{EXTENSAO}", "xls"), MODELO_DO_ENDERECO_DO_ARQUIVO_A_SER_BAIXADO.replace("{MES_ANO}", sufixoAntepenultimoMes).replace("{EXTENSAO}", "xlsx"), MODELO_DO_ENDERECO_DO_ARQUIVO_A_SER_BAIXADO.replace("{MES_ANO}", sufixoAntepenultimoMes).replace("{EXTENSAO}", "xls") };
        for (String enderecoPossivel : enderecosPossiveis) {
            try {
                URLConnection conexao = new URL(enderecoPossivel).openConnection();
                String tipoDoConteudo = conexao.getContentType();
                if (!tipoDoConteudo.equalsIgnoreCase("text/html")) {
                    enderecoDoArquivoASerBaixado = enderecoPossivel;
                    break;
                }
            } catch (IOException ex) {
                System.out.println("Ok - vai para a próxima tentativa...");
            }
        }
        return enderecoDoArquivoASerBaixado;
    }

    public static File baixarPlanilhaDoBancoDeDadosDARI(Andamento pObservador) throws MalformedURLException, IOException {
        File diretorioParaGravacaoDoArquivo = obterPastaPessoal();
        String enderecoDoArquivoASerBaixado = obterEnderecoDaPlanilhaDoBancoDeDados(), itemDesejado = null;
        int tamanhoPadraoDoArquivoASerBaixado = -1;
        return baixarArquivoDaInternet(enderecoDoArquivoASerBaixado, tamanhoPadraoDoArquivoASerBaixado, itemDesejado, diretorioParaGravacaoDoArquivo, pObservador);
    }

    private static File baixarArquivoDaInternet(String pEnderecoDoArquivoASerBaixado, final long TAMANHO_PADRAO_DO_ARQUIVO_A_SER_BAIXADO, String pItemDesejadoDoArquivo, File pDiretorioParaGravacaoDoArquivo, Andamento pObservador) throws MalformedURLException, IOException {
        InputStream origem = null;
        OutputStream destino = null;
        File arquivoBaixado = null;
        boolean downloadNecessario = false;
        try {
            URL enderecoDaOrigemDosDados;
            byte[] reservatorio;
            int bytesLidos;
            enderecoDaOrigemDosDados = new URL(pEnderecoDoArquivoASerBaixado);
            URLConnection conexao = enderecoDaOrigemDosDados.openConnection();
            long totalDeBytesASeremBaixados = conexao.getContentLength();
            Map<String, List<String>> propriedadesDoArquivoASerBaixado = conexao.getHeaderFields();
            String nomeDoArquivo;
            if (propriedadesDoArquivoASerBaixado.containsKey("Content-Disposition") || propriedadesDoArquivoASerBaixado.containsKey("content-disposition")) {
                List<String> disposicaoDoConteudo;
                if (propriedadesDoArquivoASerBaixado.containsKey("Content-Disposition")) {
                    disposicaoDoConteudo = propriedadesDoArquivoASerBaixado.get("Content-Disposition");
                } else {
                    disposicaoDoConteudo = propriedadesDoArquivoASerBaixado.get("content-disposition");
                }
                String anexo = disposicaoDoConteudo.get(0);
                nomeDoArquivo = anexo.substring(anexo.lastIndexOf("filename=") + "filename=".length());
            } else {
                String[] localFileNameTmp = enderecoDaOrigemDosDados.getPath().split("/");
                nomeDoArquivo = localFileNameTmp[localFileNameTmp.length - 1];
            }
            nomeDoArquivo = URLDecoder.decode(nomeDoArquivo, "UTF-8");
            long tmpDataDaUltimaAlteracaoNaOrigem = conexao.getLastModified();
            boolean faltandoDataDaUltimaAlteracaoDoArquivo = (tmpDataDaUltimaAlteracaoNaOrigem <= 0), faltandoTamanhoDoArquivo = (totalDeBytesASeremBaixados <= 0), arquivoCompactado = nomeDoArquivo.toLowerCase().endsWith(".zip");
            if ((faltandoDataDaUltimaAlteracaoDoArquivo) && arquivoCompactado) {
                ZipInputStream origemCompactada = null;
                try {
                    origemCompactada = new ZipInputStream(new BufferedInputStream(enderecoDaOrigemDosDados.openStream()));
                    ZipEntry itemCompactado;
                    boolean continuar = true;
                    while ((itemCompactado = origemCompactada.getNextEntry()) != null && continuar) {
                        if (itemCompactado.getName().equalsIgnoreCase(pItemDesejadoDoArquivo)) {
                            tmpDataDaUltimaAlteracaoNaOrigem = itemCompactado.getTime();
                            continuar = false;
                        }
                        origemCompactada.closeEntry();
                    }
                } finally {
                    if (origemCompactada != null) {
                        origemCompactada.close();
                    }
                }
            }
            File arquivoASerCriadoLocalmente = new File(pDiretorioParaGravacaoDoArquivo, nomeDoArquivo);
            boolean baixadoAnteriormente = arquivoASerCriadoLocalmente.exists(), atualizacaoNecessaria = false;
            String chaveDoMetadadoDeExpiracaoDaCopiaLocal = "dataDoCache" + nomeDoArquivo;
            if (baixadoAnteriormente) {
                Date dataDaUltimaAlteracaoNaOrigem = new Date(tmpDataDaUltimaAlteracaoNaOrigem);
                Date dataDaUltimaAlteracaoDaCopiaLocal = new Date(PREFERENCIAS_DO_USUARIO.getLong(chaveDoMetadadoDeExpiracaoDaCopiaLocal, 0));
                atualizacaoNecessaria = (dataDaUltimaAlteracaoNaOrigem.after(dataDaUltimaAlteracaoDaCopiaLocal));
            }
            if (faltandoTamanhoDoArquivo) {
                if (baixadoAnteriormente) {
                    long tamanhoDaCopiaAnterior = arquivoASerCriadoLocalmente.length();
                    totalDeBytesASeremBaixados = tamanhoDaCopiaAnterior;
                } else {
                    totalDeBytesASeremBaixados = TAMANHO_PADRAO_DO_ARQUIVO_A_SER_BAIXADO;
                }
            }
            downloadNecessario = ((!baixadoAnteriormente) || atualizacaoNecessaria);
            if (downloadNecessario) {
                if (baixadoAnteriormente) {
                    arquivoASerCriadoLocalmente.delete();
                }
                destino = new BufferedOutputStream(new FileOutputStream(arquivoASerCriadoLocalmente));
                origem = conexao.getInputStream();
                reservatorio = new byte[BYTES_POR_LEITURA];
                double totalDeBytesLidos = 0;
                while ((bytesLidos = origem.read(reservatorio)) != -1) {
                    destino.write(reservatorio, 0, bytesLidos);
                    totalDeBytesLidos += bytesLidos;
                    if (pObservador != null) {
                        pObservador.setPercentualCompleto((int) (totalDeBytesLidos / totalDeBytesASeremBaixados * 100));
                    }
                }
                destino.flush();
                PREFERENCIAS_DO_USUARIO.putLong(chaveDoMetadadoDeExpiracaoDaCopiaLocal, tmpDataDaUltimaAlteracaoNaOrigem);
            }
            arquivoBaixado = arquivoASerCriadoLocalmente;
        } finally {
            if (pObservador != null) {
                pObservador.setPercentualCompleto(100);
            }
            try {
                if (downloadNecessario) {
                    origem.close();
                    destino.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ManipuladorDeArquivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return arquivoBaixado;
    }

    public static Map<String, File> descompactarArquivo(File pArquivoCompactado, Andamento pObservador) {
        ZipInputStream origemCompactada = null;
        OutputStream destino = null;
        Map<String, File> arquivosDescompactados = new HashMap<String, File>();
        try {
            File diretorioParaGravacaoDoArquivoDescompactado = pArquivoCompactado.getParentFile();
            origemCompactada = new ZipInputStream(new BufferedInputStream(new FileInputStream(pArquivoCompactado)));
            long totalDeBytesCompactados = pArquivoCompactado.length();
            double totalDeBytesDescompactados = 0;
            String nomeDoItem;
            ZipEntry itemCompactado;
            while ((itemCompactado = origemCompactada.getNextEntry()) != null) {
                nomeDoItem = itemCompactado.getName();
                File arquivoASerCriadoLocalmente = new File(diretorioParaGravacaoDoArquivoDescompactado, nomeDoItem);
                if (arquivoASerCriadoLocalmente.exists()) {
                    arquivoASerCriadoLocalmente.delete();
                }
                int bytesLidos;
                byte[] reservatorio = new byte[BYTES_POR_LEITURA];
                double tamanhoCompactado = itemCompactado.getCompressedSize();
                double tamanhoDescompactado = itemCompactado.getSize();
                double fatorDeCompactacaoDoItemAtual = tamanhoCompactado / tamanhoDescompactado;
                try {
                    destino = new BufferedOutputStream(new FileOutputStream(arquivoASerCriadoLocalmente));
                    while ((bytesLidos = origemCompactada.read(reservatorio)) != -1) {
                        destino.write(reservatorio, 0, bytesLidos);
                    }
                    destino.flush();
                    totalDeBytesDescompactados += tamanhoCompactado;
                    pObservador.setPercentualCompleto((int) (totalDeBytesDescompactados / totalDeBytesCompactados * 100));
                } finally {
                    origemCompactada.closeEntry();
                    if (destino != null) {
                        destino.close();
                    }
                }
                arquivosDescompactados.put(nomeDoItem, arquivoASerCriadoLocalmente);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManipuladorDeArquivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pObservador.setPercentualCompleto(100);
            try {
                origemCompactada.close();
            } catch (IOException ex) {
                Logger.getLogger(ManipuladorDeArquivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return arquivosDescompactados;
    }

    public static File obterCertificadoParaUsoDoWebserviceDoBacen() throws IOException {
        String enderecoDoRepositorioDeChaves = Parametrizacao.obterConfiguracaoDoSistema("url_repositorio_chaves");
        File certificado = baixarArquivoDaInternet(enderecoDoRepositorioDeChaves, -1, null, obterPastaPessoal(), null);
        return certificado;
    }
}
