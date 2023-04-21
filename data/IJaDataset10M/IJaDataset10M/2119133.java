package gui.colecaodiscos;

import exceptions.DataException;
import fachada.Fachada;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cmc.music.common.ID3ReadException;
import org.cmc.music.common.ID3WriteException;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;
import util.Util;
import classesbasicas.Assunto;
import classesbasicas.Constantes;
import classesbasicas.Musica;
import classesbasicas.MusicaNaColecao;

public class GerarColecaoDiscosThread extends Thread {

    private GerarColecaoDiscosPanel painel = null;

    private Map<String, String> map = null;

    private List<Musica> colecao = null;

    public GerarColecaoDiscosThread(GerarColecaoDiscosPanel painel, Map<String, String> map) {
        this.painel = painel;
        this.map = map;
    }

    public GerarColecaoDiscosThread(List<Musica> musicas, GerarColecaoDiscosPanel painel, Map<String, String> map) {
        this.painel = painel;
        this.map = map;
        colecao = musicas;
    }

    public void run() {
        painel.getStatusTextField().setBackground(Color.RED);
        painel.getStatusTextField().setText("Consultando M�sicas no BD");
        painel.getLogTextArea().append("Consultando M�sicas no Banco de Dados\n");
        painel.getProgressBar().setBackground(Color.RED);
        try {
            List<Musica> musicasPorOrdemAlfabetica;
            if (colecao == null) {
                musicasPorOrdemAlfabetica = Fachada.listarTodasAsMusicasEmOrdemAlfabetica();
            } else {
                musicasPorOrdemAlfabetica = colecao;
            }
            List<MusicaNaColecao> colecaoOrdemAlfabetica = new ArrayList<MusicaNaColecao>();
            painel.getProgressBar().setMinimum(0);
            painel.getProgressBar().setMaximum(musicasPorOrdemAlfabetica.size());
            painel.getTotalDeMusicasTextField().setText("" + musicasPorOrdemAlfabetica.size());
            painel.getLogTextArea().append("Total de M�sicas: " + musicasPorOrdemAlfabetica.size() + "\n");
            painel.getStatusTextField().setText("Copiando M�sicas");
            painel.getLogTextArea().append("Iniciando a c�pia das M�sicas...\n\n");
            File diretorio = new File(map.get(Constantes.LOCAL_DE_DESTINO) + File.separator + map.get(Constantes.NOME_COLECAO).replaceAll("[/\\|?<>*]", "_"));
            int i = 1;
            while (diretorio.exists()) {
                diretorio = new File(map.get(Constantes.LOCAL_DE_DESTINO) + File.separator + map.get(Constantes.NOME_COLECAO).replaceAll("[/\\|?<>*]", "_") + i);
                i++;
            }
            diretorio.mkdir();
            String midia = map.get(Constantes.MIDIA);
            int numeroDoDisco = 1;
            int numeroDaMusicaNoDisco = 1;
            long tamanhoTotal = 0;
            long tamanhoMaximo;
            if (midia.equals(Constantes.MIDIA_CD)) {
                tamanhoMaximo = Constantes.TAMANHO_MAXIMO_CD;
            } else if (midia.equals(Constantes.MIDIA_DVD)) {
                tamanhoMaximo = Constantes.TAMANHO_MAXIMO_DVD;
            } else {
                tamanhoMaximo = Constantes.TAMANHO_MAXIMO_CD_AUDIO;
            }
            boolean limitarNumeroDeMusicas = map.get(Constantes.LIMITAR_MUSICAS).equals("true") ? true : false;
            int limiteDeMusicas = 0;
            if (limitarNumeroDeMusicas == true) {
                limiteDeMusicas = Integer.parseInt(map.get(Constantes.LIMITE_DE_MUSICAS));
            }
            DecimalFormat df = new DecimalFormat("000");
            File diretorioDoDisco = new File(diretorio.getPath() + File.separator + midia + df.format(numeroDoDisco));
            diretorioDoDisco.mkdir();
            for (Musica m : musicasPorOrdemAlfabetica) {
                painel.getLogTextArea().append("Copiando a M�sica " + m.getNome() + "\n");
                painel.getLogTextArea().setCaretPosition(painel.getLogTextArea().getText().length());
                painel.getMusicaAtualTextField().setText(m.getNome());
                File origem = new File(Util.getDiretorioBase() + File.separator + m.getDiretorio() + File.separator + m.getNomeArquivo());
                if (limitarNumeroDeMusicas) {
                    if (numeroDaMusicaNoDisco > limiteDeMusicas) {
                        numeroDaMusicaNoDisco = 1;
                        numeroDoDisco++;
                        tamanhoTotal = 0;
                        diretorioDoDisco = new File(diretorio.getPath() + File.separator + midia + df.format(numeroDoDisco));
                        diretorioDoDisco.mkdir();
                    }
                }
                if (midia.equals(Constantes.MIDIA_CD_AUDIO)) {
                    tamanhoTotal += m.getDuracao();
                } else {
                    tamanhoTotal += origem.length();
                }
                if (tamanhoTotal > tamanhoMaximo) {
                    if (midia.equals(Constantes.MIDIA_CD_AUDIO)) {
                        tamanhoTotal = m.getDuracao();
                    } else {
                        tamanhoTotal = origem.length();
                    }
                    numeroDoDisco++;
                    numeroDaMusicaNoDisco = 1;
                    diretorioDoDisco = new File(diretorio.getPath() + File.separator + midia + df.format(numeroDoDisco));
                    diretorioDoDisco.mkdir();
                }
                painel.getLogTextArea().append("Disco " + numeroDoDisco + ", Faixa " + numeroDaMusicaNoDisco + "... ");
                painel.getLogTextArea().setCaretPosition(painel.getLogTextArea().getText().length());
                String caminhoDestino = diretorioDoDisco.getPath() + File.separator;
                if (map.get(Constantes.INSERIR_LETRAS_INICIAIS).equals("true")) {
                    caminhoDestino += Util.intToString(numeroDaMusicaNoDisco) + " - ";
                }
                caminhoDestino += m.getNome();
                if (m.getCantores() != null && m.getCantores().size() > 0) {
                    caminhoDestino += " - " + m.getCantores().get(0).getNomeSemEspacos();
                }
                caminhoDestino += ".mp3";
                File destino = new File(caminhoDestino);
                i = 1;
                while (destino.exists()) {
                    caminhoDestino = diretorioDoDisco.getPath() + File.separator;
                    if (map.get(Constantes.INSERIR_LETRAS_INICIAIS).equals("true")) {
                        caminhoDestino += Util.intToString(numeroDaMusicaNoDisco) + " - ";
                    }
                    caminhoDestino += m.getNome() + " #" + df.format(i);
                    if (m.getCantores() != null || m.getCantores().size() > 0) {
                        caminhoDestino += " - " + m.getCantores().get(0).getNomeSemEspacos();
                    }
                    caminhoDestino += ".mp3";
                    destino = new File(caminhoDestino);
                }
                Util.copyFile(origem.getPath(), destino.getPath());
                File src = destino;
                try {
                    MusicMetadataSet src_set = new MyID3().read(src);
                    if (src_set == null) {
                        System.out.println("Arquivo sem informa��es de Tags");
                    }
                    MusicMetadata metadata = (MusicMetadata) src_set.getSimplified();
                    metadata.setAlbum(map.get(Constantes.NOME_COLECAO) + ", Disco " + df.format(numeroDoDisco));
                    metadata.setArtist(m.getCantores() == null || m.getCantores().size() == 0 ? "N�o Informado" : m.getCantores().get(0).getNome());
                    metadata.setDiscNumber(numeroDoDisco);
                    metadata.setSongTitle(m.getNome());
                    metadata.setTrackNumberNumeric(numeroDaMusicaNoDisco);
                    new MyID3().update(destino, src_set, metadata);
                } catch (ID3ReadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ID3WriteException e) {
                    e.printStackTrace();
                }
                painel.getLogTextArea().append("OK\n\n");
                MusicaNaColecao musicaNaColecao = new MusicaNaColecao();
                musicaNaColecao.setNomeMusica(m.getNome());
                if (m.getCantores() != null && m.getCantores().size() > 0) {
                    musicaNaColecao.setCantor(m.getCantores().get(0).getNome());
                }
                if (m.getTipo() != null) {
                    musicaNaColecao.setTipo(m.getTipo().getTipo());
                }
                musicaNaColecao.setNumeroDVD(numeroDoDisco);
                musicaNaColecao.setNumeroFaixa(numeroDaMusicaNoDisco);
                musicaNaColecao.setDuracaoMusica(m.getDuracao());
                if (m.getAssuntos() != null && m.getAssuntos().size() > 0) {
                    musicaNaColecao.setAssunto("");
                    for (Assunto a : m.getAssuntos()) {
                        musicaNaColecao.setAssunto(musicaNaColecao.getAssunto() + a.getAssunto());
                        if (m.getAssuntos().indexOf(a) < m.getAssuntos().size() - 1) {
                            musicaNaColecao.setAssunto(musicaNaColecao.getAssunto() + ", ");
                        }
                    }
                }
                colecaoOrdemAlfabetica.add(musicaNaColecao);
                numeroDaMusicaNoDisco++;
                painel.getProgressBar().setValue(musicasPorOrdemAlfabetica.indexOf(m) + 1);
            }
            painel.getProgressBar().setForeground(Color.RED);
            painel.getProgressBar().setString("Aguarde...");
            painel.getLogTextArea().append("Preparando para criar as listagens...\n");
            painel.getLogTextArea().setCaretPosition(painel.getLogTextArea().getText().length());
            painel.getStatusTextField().setText("Criando as listagens");
            boolean pdf = map.get(Constantes.RELATORIO_PDF).equals("true") ? true : false;
            boolean html = map.get(Constantes.RELATORIO_HTML).equals("true") ? true : false;
            Map<String, String> reportParameters = new HashMap<String, String>();
            reportParameters.put(Constantes.MIDIA, midia.equals(Constantes.MIDIA_DVD) ? Constantes.MIDIA_DVD : Constantes.MIDIA_CD);
            reportParameters.put(Constantes.NOME_COLECAO, map.get(Constantes.NOME_COLECAO));
            painel.getLogTextArea().append("criando as listagens...\n");
            painel.getLogTextArea().setCaretPosition(painel.getLogTextArea().getText().length());
            if (map.get(Constantes.RELATORIO_ORDEM_ALFABETICA).equals("true")) {
                Fachada.criaRelatorioColecaoPorOrdemAlfabetica(colecaoOrdemAlfabetica, reportParameters, pdf, html, diretorio.getPath());
            }
            if (map.get(Constantes.RELATORIO_POR_CANTOR).equals("true")) {
                List<Musica> musicasPorCantor = Fachada.listarTodasAsMusicasOrdenarPorCantor();
                List<MusicaNaColecao> colecaoPorCantor = new ArrayList<MusicaNaColecao>();
                for (Musica m : musicasPorCantor) {
                    int indice = musicasPorOrdemAlfabetica.indexOf(m);
                    if (indice != -1) {
                        colecaoPorCantor.add(colecaoOrdemAlfabetica.get(indice));
                    }
                }
                Fachada.criaRelatorioColecaoPorCantor(colecaoPorCantor, reportParameters, pdf, html, diretorio.getPath());
            }
            if (map.get(Constantes.RELATORIO_POR_ASSUNTO).equals("true")) {
                List<Musica> musicasPorAssunto = Fachada.listarTodasAsMusicasOrdenarPorAssunto();
                List<MusicaNaColecao> colecaoPorAssunto = new ArrayList<MusicaNaColecao>();
                List<Musica> listaTemp = new ArrayList<Musica>();
                List<Integer> nTemp = new ArrayList<Integer>();
                for (Musica m : musicasPorAssunto) {
                    if (m.getAssuntos() != null && m.getAssuntos().size() > 1) {
                        int n = listaTemp.indexOf(m);
                        if (n == -1) {
                            n = 0;
                            listaTemp.add(m);
                            nTemp.add(n);
                        }
                    }
                    int indice = musicasPorOrdemAlfabetica.indexOf(m);
                    if (indice != -1) {
                        if (m.getAssuntos() != null && m.getAssuntos().size() > 1) {
                            MusicaNaColecao musicaTemp = (MusicaNaColecao) colecaoOrdemAlfabetica.get(indice).clone();
                            int ind = listaTemp.indexOf(m);
                            int t = nTemp.get(ind);
                            musicaTemp.setAssunto(m.getAssuntos().get(t).getAssunto());
                            nTemp.set(ind, t + 1);
                            colecaoPorAssunto.add(musicaTemp);
                        } else {
                            colecaoPorAssunto.add(colecaoOrdemAlfabetica.get(indice));
                        }
                    }
                }
                Fachada.criaRelatorioColecaoPorAssunto(colecaoPorAssunto, reportParameters, pdf, html, diretorio.getPath());
            }
            painel.getLogTextArea().append("Listagens criadas com sucesso.\n\n");
            painel.getLogTextArea().setCaretPosition(painel.getLogTextArea().getText().length());
            painel.getProgressBar().setForeground(Color.GREEN);
            painel.getStatusTextField().setBackground(Color.GREEN);
            painel.getLogTextArea().append("Opera��o conclu�da com sucesso!");
            painel.getLogTextArea().setCaretPosition(painel.getLogTextArea().getText().length());
            painel.getProgressBar().setString("Conclu�do!");
            painel.getStatusTextField().setText("Opera��o Conclu�da com Sucesso!");
        } catch (DataException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Override hashCode.
	 *
	 * @return the Objects hashcode.
	 */
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode = 31 * hashCode + (painel == null ? 0 : painel.hashCode());
        hashCode = 31 * hashCode + (map == null ? 0 : map.hashCode());
        return hashCode;
    }
}
