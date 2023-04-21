package net.sf.webphotos.gui;

import net.sf.webphotos.util.Util;

/**
 * Painel de apresenta��o da Foto selecionada.
 * Ao clicar nele, uma janela de popup � aberta.
 * Para saber mais sobre essa janela de visualiza��o procure sobre a classe {@link net.sf.webphotos.gui.Visualizador Visualizador}.
 * @author guilherme
 */
public class PainelFoto extends javax.swing.JPanel {

    /**
     * Distancia em Pixels entre a figura e a Borda do componente
     */
    public static final int FOLGA = 1;

    private java.awt.Image foto;

    private int tamMax = 200;

    private float novaLargura;

    private float novaAltura;

    private float x, y;

    private String nomeArquivo;

    private boolean mostrandoMsgErro;

    private javax.swing.ImageIcon ico = new javax.swing.ImageIcon();

    /**
     * Construtor da classe.
     * Chama o m�todo initComponents() que tem a fun��o de gerar uma janela popup para apresentar a foto.
     */
    public PainelFoto() {
        initComponents();
    }

    private void initComponents() {
        lblPainelFoto = new javax.swing.JLabel();
        lblFotoInfo = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        setMaximumSize(new java.awt.Dimension(210, 2147483647));
        setMinimumSize(new java.awt.Dimension(210, 10));
        setPreferredSize(new java.awt.Dimension(210, 210));
        lblPainelFoto.setToolTipText("Clique para visualizar a foto");
        lblPainelFoto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        lblPainelFoto.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPainelFotoMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblPainelFotoMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblPainelFotoMouseExited(evt);
            }
        });
        add(lblPainelFoto, java.awt.BorderLayout.CENTER);
        lblFotoInfo.setFont(new java.awt.Font("SansSerif", 0, 10));
        lblFotoInfo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFotoInfo.setText("??x??");
        add(lblFotoInfo, java.awt.BorderLayout.SOUTH);
    }

    private void lblPainelFotoMouseClicked(java.awt.event.MouseEvent evt) {
        if (getFoto() != null) {
            String foto = getNomeArquivo().replaceFirst("_b", "_d");
            Visualizador fotoMaior = new Visualizador(foto, null, "zOOm");
        }
    }

    private void lblPainelFotoMouseExited(java.awt.event.MouseEvent evt) {
        setCursor(null);
    }

    private void lblPainelFotoMouseEntered(java.awt.event.MouseEvent evt) {
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    /**
     * Limpa a caixa onde � apresentada a foto.
     * Checa se n�o existe mensagem de erro, caso exista, usa o m�todo {@link java.awt.Component#repaint() repaint()} e preenche com a cor cinza.
     * Caso a foto n�o exista, apenas encerra.
     * E por �ltimo, caso a foto exista, seta ela como nula e usa o repaint() para preencher o espa�o da foto.
     */
    public void clear() {
        if (mostrandoMsgErro) {
            mostrandoMsgErro = false;
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) this.getGraphics();
            g2.setPaint(java.awt.Color.GRAY);
            g2.fillRect(0, 0, 200, 200);
            repaint();
        }
        if (getFoto() == null) return;
        setFoto(null);
        repaint();
    }

    /**
     * Carrega uma imagem.
     * Seta a vari�vel nomeArquivo e faz o load da foto.
     * @param caminhoImagem Caminho da imagem.
     */
    public void loadImage(String caminhoImagem) {
        setNomeArquivo(caminhoImagem);
        System.out.println("Lendo:" + caminhoImagem);
        this.loadImage();
    }

    /**
     * Carrega uma imagem.
     * Como n�o recebe o caminho do arquivo, busca a foto pelo valor armazenado no vari�vel nomeArquivo.
     * Checa se a foto foi <I>lida</I> corretamente, e redimensiona a altura e largura.
     */
    public void loadImage() {
        try {
            ico.setImage(java.awt.Toolkit.getDefaultToolkit().getImage(getNomeArquivo()));
        } catch (Exception e) {
            System.out.println("Erro:");
            e.printStackTrace();
        }
        System.out.println("Fim toolkit");
        int status = ico.getImageLoadStatus();
        if (status != java.awt.MediaTracker.COMPLETE) {
            lblPainelFoto.setIcon(null);
            repaint();
            mostrandoMsgErro = true;
            return;
        }
        System.out.println("obtendo informacoes");
        setFoto(ico.getImage());
        float largura = getFoto().getWidth(this);
        float altura = getFoto().getHeight(this);
        tamMax = lblPainelFoto.getWidth();
        System.out.println("Tamanho: " + tamMax);
        if (largura > altura) {
            novaLargura = tamMax;
            novaAltura = -1;
        } else if (largura < altura) {
            novaLargura = -1;
            novaAltura = tamMax;
        } else {
            novaLargura = tamMax;
            novaAltura = tamMax;
        }
        System.out.println("Fim do redimensionamento");
        lblPainelFoto.setIcon(new javax.swing.ImageIcon(getFoto().getScaledInstance((int) novaLargura - (FOLGA * 2), (int) novaAltura - (FOLGA * 2), java.awt.Image.SCALE_SMOOTH)));
        System.out.println("Fim repintagem");
    }

    private javax.swing.JLabel lblFotoInfo;

    private javax.swing.JLabel lblPainelFoto;

    /**
     * Retorna uma foto atrav�s do objeto {@link java.awt.Image Image} chamado foto.
     * @return Retorna uma foto.
     */
    public java.awt.Image getFoto() {
        return foto;
    }

    /**
     * Seta o objeto {@link java.awt.Image Image} foto atrav�s de outro objeto recebido como par�metro.
     * @param foto Foto.
     */
    public void setFoto(java.awt.Image foto) {
        this.foto = foto;
    }

    /**
     * Retorna o nome ou caminho do arquivo. No caso, de uma foto.
     * @return Retorna um nome de arquivo.
     */
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    /**
     * Seta o nome ou caminho do arquivo. No caso, de uma foto.
     * @param nomeArquivo Nome do arquivo.
     */
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    /**
     * Retorna o <I>label</I> da foto.
     * @return Retorna um label.
     */
    public javax.swing.JLabel getLblFotoInfo() {
        return lblFotoInfo;
    }

    /**
     * Seta o label da foto.
     * @param lblFotoInfo Label correspondente a foto.
     */
    public void setLblFotoInfo(javax.swing.JLabel lblFotoInfo) {
        this.lblFotoInfo = lblFotoInfo;
    }

    /**
     * Retorna a dimens�o <I>default</I> atrav�s da classe {@link java.awt.Dimension Dimension}.
     * Passa thumbs como par�metros para setar os valores de dimens�o.
     * @return Retorna o default de dimens�o.
     */
    public static java.awt.Dimension getDefaultSize() {
        return new java.awt.Dimension(Util.getConfig().getInt("thumbnail2"), Util.getConfig().getInt("thumbnail2"));
    }
}
