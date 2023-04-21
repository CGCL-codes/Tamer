package br.unisinos.cs.gp.image;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import br.unisinos.cs.gp.image.handler.ImageHandlerException;

/**
 * Exibição de Imagens
 * 
 * Auxílio para visualização de imagens carregadas através dos manipuladores de
 * arquivos do projeto.
 * 
 * @author Wanderson Henrique Camargo Rosa
 */
public class ImageViewer extends JFrame {

    /**
     * Número de Serialização
     */
    private static final long serialVersionUID = -6645058729052122411L;

    /**
     * Construtor da Classe
     */
    public ImageViewer() {
        super("Image Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
    }

    /**
     * Método de Execução Principal
     * @param args Argumentos de Entrada
     * @throws ImageHandlerException Exceção Interna ao Pacote
     */
    public static void main(String args[]) throws ImageHandlerException {
        String filename = "data/amuro.pcx";
        String handler = "br.unisinos.cs.gp.image.handler.PcxHandler";
        BufferedImage image = ImageLoader.factory(filename, handler);
        ImageIcon icon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(icon);
        ImageViewer viewer = new ImageViewer();
        Container content = viewer.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        viewer.setVisible(true);
    }
}
