package voxToolkit;

import java.awt.AWTEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import grafico.Fabrica;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import audio.AudioSintetizado;
import audio.SoundEvent;
import audio.SoundListener;

public class VoxSlider extends JSlider implements FocusListener, ChangeListener, SoundListener {

    private String rotulo;

    private AudioSintetizado sintetizador;

    DecimalFormat valor = new DecimalFormat("0");

    /**
     * Cria um objeto VoxSlider adicionando um tooltip a partir do rótulo
     * @param orientation Orientação - vertical ou horizontal
     * @param min Valor mínimo
     * @param max Valor máximo
     * @param value Valor que estará marcado
     * @param rotulo Utilizado para definir o tooltip e a forma como o componente será lido
     * pelo sintetizador
     */
    public VoxSlider(int orientation, int min, int max, int value, String rotulo) {
        super(orientation, min, max, value);
        this.rotulo = rotulo;
        setBackground(Fabrica.background);
        setToolTipText("<html><body bgcolor=327eae text=ffffff><h2>" + "&nbsp;" + this.rotulo + "&nbsp;" + "</h2></body></html>");
        setMinorTickSpacing(1);
        ativaSintetizador();
        addChangeListener(this);
        addFocusListener(this);
    }

    private void ativaSintetizador() {
        sintetizador = AudioSintetizado.instancia();
        sintetizador.addSoundListener(this);
    }

    public String getRotulo() {
        return rotulo + ". Controle deslizante. ";
    }

    public void setRotulo(String campo) {
        this.rotulo = campo;
    }

    public String getValor() {
        return (String) (valor.format(this.getValue()));
    }

    @Override
    public void focusGained(FocusEvent e) {
        VoxSlider voxSlider = (VoxSlider) e.getSource();
        sintetizador.escreve((String) voxSlider.getRotulo() + (String) voxSlider.getValor());
    }

    @Override
    public void focusLost(FocusEvent e) {
        sintetizador.abortaFala();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        sintetizador.abortaFala();
        VoxSlider voxSlider = (VoxSlider) e.getSource();
        sintetizador.escreve(voxSlider.getValor());
    }

    @Override
    public void FimDeFala(SoundEvent e) {
    }

    @Override
    public void eventDispatched(AWTEvent e) {
        if (e instanceof SoundEvent) FimDeFala((SoundEvent) e);
    }
}
