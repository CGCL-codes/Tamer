package Interface;

import java.awt.BorderLayout;
import javax.swing.*;

/**
 *
 * @author Leonardo
 */
public class Configuracoes extends JPanel {

    protected Principal Principal;

    private Configuracoes_Alvo Configuracoes_Alvo;

    private Configuracoes_Base Configuracoes_Base;

    /** Creates a new instance of Configuracoes */
    public Configuracoes(Principal Principal) {
        super();
        this.Principal = Principal;
        Configuracoes_Alvo = new Configuracoes_Alvo(Principal);
        Configuracoes_Base = new Configuracoes_Base(Principal);
        add(Configuracoes_Base, BorderLayout.LINE_START);
        add(Configuracoes_Alvo, BorderLayout.LINE_END);
    }

    public String getToolTip() {
        return "<html>Blue Wavy Line border art crew:<br>&nbsp;&nbsp;&nbsp;Bill Pauley<br>" + "&nbsp;&nbsp;&nbsp;Cris St. Aubyn<br>" + "&nbsp;&nbsp;&nbsp;Ben Wronsky<br>" + "&nbsp;&nbsp;&nbsp;Nathan Walrath<br>" + "&nbsp;&nbsp;&nbsp;Tommy Adams, special consultant</html>";
    }

    public void restaurar() {
        Configuracoes_Alvo.restaurar();
        Configuracoes_Base.restaurar();
    }
}
