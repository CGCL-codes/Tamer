package farmacia.apresentacao;

import javax.swing.JDesktopPane;

public class GestorJanela {

    final int ALTURA = 35;

    final int DISTANCIA = 25;

    private static GestorJanela gestor;

    private JDesktopPane areaTrabalho = new JDesktopPane();

    private GestorJanela() {
    }

    public JDesktopPane getAreaTrabalho() {
        return areaTrabalho;
    }

    public static GestorJanela getInstance() {
        if (gestor == null) {
            gestor = new GestorJanela();
        }
        return gestor;
    }

    public void abrir(JanelaInternaBase janela) {
        new Posicionador().posicionar(janela, areaTrabalho);
        janela.setVisible(true);
        areaTrabalho.add(janela);
        areaTrabalho.getDesktopManager().activateFrame(janela);
    }
}
