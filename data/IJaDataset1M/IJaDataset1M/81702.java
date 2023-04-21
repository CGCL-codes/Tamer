package Imagem;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelImagem extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int COMPRIMENTO_IMAGEM = 300;

    private static final int LARGURA_IMAGEM = 300;

    JLabel labelImg;

    Imagem img;

    protected PanelImagem(Imagem _img) {
        img = _img;
        criarPanelDeImagem(_img);
        validate();
    }

    private JLabel criarLabelDeImagem(Imagem _img) {
        ImageIcon icone = _img.criarImagem(0, 0, false);
        labelImg = new JLabel(icone);
        labelImg.setIcon(icone);
        return labelImg;
    }

    private void criarPanelDeImagem(Imagem _img) {
        img = _img;
        add(criarLabelDeImagem(_img));
        setBounds(0, 0, 500, 500);
        revalidate();
        repaint();
    }
}
