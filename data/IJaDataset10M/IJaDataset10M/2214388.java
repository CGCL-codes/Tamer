package org.alfredlibrary.utilitarios.io;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.alfredlibrary.AlfredException;

/**
 * Utilitário para imagens.
 * 
 * @author Mario Jorge Pereira
 * @author Marlon Silva Carvalho
 * @since 09/06/2009
 */
public final class Imagem {

    private Imagem() {
        throw new AssertionError();
    }

    /**
	 * Carregar a imagem em um BufferedImage.
	 * 
	 * @param ref Nome do arquivo da imagem.
	 * @return BufferedImage.
	 */
    public static BufferedImage carregarImagem(String ref) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(ref));
        } catch (IOException e) {
            throw new AlfredException("Erro ao tentar ler a imagem!", e);
        }
        return bimg;
    }

    /**
	 * Redimensionar uma imagem.
	 * 
	 * @param imagem Imagem a ser redimensionada.
	 * @param novaLargura Nova Largura.
	 * @param novaAltura Nova Altura.
	 * @param manterProporcao Se deve ser mantida a proporção.
	 * @return Nova imagem redimensionada.
	 */
    public static BufferedImage redimensionar(byte[] imagem, int novaLargura, int novaAltura, boolean manterProporcao) {
        return redimensionar(obterBufferedImage(imagem), novaLargura, novaAltura, manterProporcao);
    }

    /**
	 *  Redimensionar uma imagem.
	 *  
	 * @param bi Imagem a ser redimensionada.
	 * @param novaLargura Nova Largura.
	 * @param novaAltura Nova Altura.
	 * @param manterProporcao Se deve ser mantida a proporção.
	 * @return Nova imagem redimensionada.
	 */
    public static BufferedImage redimensionar(BufferedImage bi, int novaLargura, int novaAltura, boolean manterProporcao) {
        double thumbRatio = (double) novaLargura / (double) novaAltura;
        int altura = bi.getHeight(null);
        int largura = bi.getWidth(null);
        double imageRatio = (double) largura / (double) altura;
        if (manterProporcao) {
            if (thumbRatio < imageRatio) {
                novaAltura = (int) (novaLargura / imageRatio);
            } else {
                novaLargura = (int) (novaAltura * imageRatio);
            }
        }
        Image i = bi.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
        BufferedImage bie = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bie.getGraphics();
        g.drawImage(i, 0, 0, null);
        g.dispose();
        return bie;
    }

    /**
	 *  Redimensionar uma imagem.
	 *  
	 * @param img Imagem a ser redimensionada.
	 * @param novaLargura Nova Largura.
	 * @param novaAltura Nova Altura.
	 * @param manterProporcao Se deve ser mantida a proporção.
	 * @return Nova imagem redimensionada.
	 */
    public static BufferedImage redimensionar(Image img, int novaLargura, int novaAltura, boolean manterProporcao) {
        double thumbRatio = (double) novaLargura / (double) novaAltura;
        int largura = img.getWidth(null);
        int altura = img.getHeight(null);
        double imageRatio = (double) largura / (double) altura;
        if (manterProporcao) {
            if (thumbRatio < imageRatio) {
                novaAltura = (int) (novaLargura / imageRatio);
            } else {
                novaLargura = (int) (novaAltura * imageRatio);
            }
        }
        BufferedImage dimg = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, novaLargura, novaAltura, 0, 0, largura, altura, null);
        g.dispose();
        return dimg;
    }

    /**
	 * Salvar uma imagem.
	 * 
	 * @param img Imagem a ser salva.
	 * @param arquivo Nome do arquivo.
	 */
    public static void salvarImagem(BufferedImage img, String arquivo) {
        BufferedOutputStream out;
        String tipo = Arquivo.obterExtensao(arquivo).toUpperCase();
        try {
            out = new BufferedOutputStream(new FileOutputStream(arquivo));
            ImageIO.write(img, tipo, out);
        } catch (FileNotFoundException ex) {
            throw new AlfredException("Arquivo não encontrado! ", ex);
        } catch (IOException ex) {
            throw new AlfredException("Erro ao salvar o arquivo! ", ex);
        }
    }

    /**
	 * Obter uma imagem a partir de um array de bytes.
	 * 
	 * @param imagem Imagem em bytes.
	 * @return Imagem.
	 */
    public static BufferedImage obterBufferedImage(byte[] imagem) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new ByteArrayInputStream(imagem));
        } catch (IOException ex) {
            throw new AlfredException("Erro ao obter ler a imagem! ", ex);
        }
        return bi;
    }

    /**
	 * Obter uma imagem.
	 * 
	 * @param imagem Dados da imagem em bytes.
	 * @param descricao Nome da imagem.
	 * @return Imagem.
	 */
    public static Image obterImagem(byte[] imagem, String descricao) {
        return new ImageIcon(imagem, descricao).getImage();
    }
}
