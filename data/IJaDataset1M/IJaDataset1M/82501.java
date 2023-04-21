package app.paineis.controle;

import app.padroes.concretos.interfaces.PanelColleague;
import app.padroes.concretos.interfaces.PanelDirector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ControleTempoCorrente extends JPanel implements PanelColleague {

    public ControleTempoCorrente() {
        initComponents();
    }

    private void initComponents() {
        jButtonContinuarTempo = new JButton();
        jButtonCadastrarVoltarAoJogo = new JButton();
        jButtonCadastrarVoltarAoJogo.setVisible(false);
        jButtonInstrucoes = new JButton();
        setBackground(new Color(255, 255, 255));
        jButtonContinuarTempo.setIcon(new ImageIcon(getClass().getResource("/app/paineis/continuar.png")));
        jButtonContinuarTempo.setActionCommand("ContinuarT");
        jButtonContinuarTempo.setMaximumSize(new Dimension(93, 24));
        jButtonContinuarTempo.setMinimumSize(new Dimension(93, 24));
        jButtonContinuarTempo.setPreferredSize(new Dimension(93, 24));
        jButtonContinuarTempo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jButtonContinuarTempoActionPerformed(evt);
            }
        });
        add(jButtonContinuarTempo);
        jButtonCadastrarVoltarAoJogo.setIcon(new ImageIcon(getClass().getResource("/app/paineis/retornar.png")));
        jButtonCadastrarVoltarAoJogo.setActionCommand("VoltarJogo");
        jButtonCadastrarVoltarAoJogo.setMaximumSize(new Dimension(93, 24));
        jButtonCadastrarVoltarAoJogo.setMinimumSize(new Dimension(93, 24));
        jButtonCadastrarVoltarAoJogo.setPreferredSize(new Dimension(93, 24));
        jButtonCadastrarVoltarAoJogo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jButtonCadastrarVoltarAoJogoActionPerformed(evt);
            }
        });
        add(jButtonCadastrarVoltarAoJogo);
        jButtonInstrucoes.setIcon(new ImageIcon(getClass().getResource("/app/paineis/ajuda.png")));
        jButtonInstrucoes.setActionCommand("??");
        jButtonInstrucoes.setMaximumSize(new Dimension(93, 24));
        jButtonInstrucoes.setMinimumSize(new Dimension(93, 24));
        jButtonInstrucoes.setPreferredSize(new Dimension(93, 24));
        jButtonInstrucoes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jButtonInstrucoesActionPerformed(evt);
            }
        });
        add(jButtonInstrucoes);
    }

    private void jButtonContinuarTempoActionPerformed(ActionEvent evt) {
        changedPanel(evt);
    }

    private void jButtonInstrucoesActionPerformed(ActionEvent evt) {
        changedPanel(evt);
    }

    private void jButtonCadastrarVoltarAoJogoActionPerformed(ActionEvent evt) {
        changedPanel(evt);
    }

    public void addPanelDirector(PanelDirector p) {
        panelDirector = p;
    }

    public void changedPanel(ActionEvent evt) {
        panelDirector.changedPanel(this, evt);
    }

    public JButton getJButtonCadastrarVoltarAoJogo() {
        return jButtonCadastrarVoltarAoJogo;
    }

    public void setJButtonCadastrarVoltarAoJogo(JButton jButtonCadastrarVoltarAoJogo) {
        this.jButtonCadastrarVoltarAoJogo = jButtonCadastrarVoltarAoJogo;
    }

    public JButton getJButtonContinuarTempo() {
        return jButtonContinuarTempo;
    }

    public void setJButtonContinuarTempo(JButton jButtonContinuarTempo) {
        this.jButtonContinuarTempo = jButtonContinuarTempo;
    }

    public JButton getJButtonInstrucoes() {
        return jButtonInstrucoes;
    }

    public void setJButtonInstrucoes(JButton jButtonInstrucoes) {
        this.jButtonInstrucoes = jButtonInstrucoes;
    }

    private PanelDirector panelDirector;

    private JButton jButtonCadastrarVoltarAoJogo;

    private JButton jButtonContinuarTempo;

    private JButton jButtonInstrucoes;
}
