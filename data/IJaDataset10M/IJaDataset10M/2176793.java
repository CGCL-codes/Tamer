package test.jmoreno.PruebaConsulta;

import java.awt.GridLayout;

public class Menu4ExtMenues extends MenuesExtPanel {

    private static final long serialVersionUID = 1L;

    public Menu4ExtMenues(final VentanaExtFrame f) {
        super(f);
        label.setText("Presione Explorador Para Elegir El Archivo A Comprimir");
        this.setLayout(new GridLayout(3, 1, 20, 20));
        this.add(label);
        this.add(bBrowser);
        this.add(bvolver);
        f.getContentPane().add(this);
    }

    public void setArch1oArch2oArch4() {
        f.setPresionaBoton(VentanaExtFrame.ARCHIVO_EN_FORM4);
        f.mostrarElegirComoComprimir();
    }
}
