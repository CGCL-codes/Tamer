package voxToolkit;

import javax.swing.JTextField;

public class VoxTextField extends JTextField {

    String rotulo;

    /**
	 * Constrói um objeto com tamanho definido e um rótulo para ser lido pelo
	 * sintetizador.
	 * 
	 * @param tamanho
	 *            Número de colunas
	 * @param Rotulo
	 *            Título a ser lido pelo sintetizador
	 */
    public VoxTextField(int tamanho, String Rotulo) {
        super(tamanho);
        this.rotulo = Rotulo;
        montaLayout();
    }

    /**
	 * Constrói um objeto com rótulo para ser lido pelo sintetizador.
	 * 
	 * @param rotulo
	 *            Título a ser lido pelo sintetizador
	 */
    public VoxTextField(String rotulo) {
        super();
        this.rotulo = rotulo;
        montaLayout();
    }

    /**
	 * Monta layout do componente de acordo com o padrão MECDaisy.
	 */
    private void montaLayout() {
        this.setBackground(FabricaVoxToolkit.branco);
        this.setFont(FabricaVoxToolkit.fonteMedia);
        this.setForeground(FabricaVoxToolkit.azulMedio);
        this.addKeyListener(VoxTextFieldEvent.instancia());
        this.addFocusListener(VoxTextFieldEvent.instancia());
    }

    /**
	 * Retorna o rótulo associado ao componente.
	 * 
	 * @return
	 */
    public String getRotulo() {
        return rotulo;
    }
}
