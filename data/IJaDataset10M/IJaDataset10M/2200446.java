package br.ufrj.dcc.comp2.projeto.vistas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import br.ufrj.dcc.comp2.projeto.controles.MarteAtaca;
import br.ufrj.dcc.comp2.projeto.internacionalizacao.*;

/**
 * Menu do jogo.
 * @author Bruno(Magoo), Eluan(Acre), Diego(Silvio), Davi.
 *
 */
public class MenuJogo extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private JMenu jogo;

    private JMenuItem iniciar;

    public JMenuItem encerrar;

    private JMenuItem recordes;

    private JMenuItem sair;

    private JMenu opcoes;

    private JMenuItem idioma;

    private JMenuItem idioma_en;

    private JMenuItem idioma_ptbr;

    private JMenu sobre;

    private JMenuItem creditos;

    private JMenuItem ajuda;

    private MarteAtaca ma;

    public MenuJogo(MarteAtaca ma) {
        this.ma = ma;
        ma.setMenu(this);
        jogo = new JMenu();
        iniciar = new JMenuItem();
        encerrar = new JMenuItem();
        recordes = new JMenuItem();
        sair = new JMenuItem();
        iniciar.addActionListener(new OuvinteMenu());
        encerrar.addActionListener(new OuvinteMenu());
        recordes.addActionListener(new OuvinteMenu());
        sair.addActionListener(new OuvinteMenu());
        jogo.add(iniciar);
        jogo.add(encerrar);
        jogo.add(recordes);
        jogo.add(sair);
        opcoes = new JMenu();
        idioma = new JMenu();
        idioma_en = new JMenuItem();
        idioma_ptbr = new JMenuItem();
        idioma_en.addActionListener(new OuvinteMenu());
        idioma_ptbr.addActionListener(new OuvinteMenu());
        opcoes.add(idioma);
        idioma.add(idioma_en);
        idioma.add(idioma_ptbr);
        sobre = new JMenu();
        creditos = new JMenuItem();
        ajuda = new JMenuItem();
        creditos.addActionListener(new OuvinteMenu());
        ajuda.addActionListener(new OuvinteMenu());
        sobre.add(creditos);
        sobre.add(ajuda);
        this.add(jogo);
        this.add(opcoes);
        this.add(sobre);
    }

    /**
	 * Altera idioma do menu.
	 * @param idioma ID do idioma
	 */
    public void mudaIdiomaMenu(int idioma) {
        this.jogo.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_JOGO));
        this.opcoes.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_OPCOES));
        this.sobre.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_SOBRE));
        this.iniciar.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_INICIAR));
        this.encerrar.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_ENCERRAR));
        this.recordes.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_RECORDES));
        this.sair.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_SAIR));
        this.idioma.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_IDIOMA));
        this.idioma_en.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_IDIOMA_EN));
        this.idioma_ptbr.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_IDIOMA_PTBR));
        this.creditos.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_CREDITOS));
        this.ajuda.setText(InterGeral.retornaStringLocalizada(idioma, InterGeral.MENU_AJUDA));
    }

    /**
	 * Lister para capturar o que foi clicado no menu.
	 * @author Bruno(Magoo), Eluan(Acre), Diego(Silvio), Davi.
	 *
	 */
    class OuvinteMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == iniciar) ma.novoJogo();
            if (e.getSource() == encerrar) ma.gameOver(true);
            if (e.getSource() == recordes) ma.mostraRecordes();
            if (e.getSource() == sair) System.exit(0);
            if (e.getSource() == idioma_en) InterGeral.AlteraIdioma(ma, Ingles.ID);
            if (e.getSource() == idioma_ptbr) InterGeral.AlteraIdioma(ma, Portugues.ID);
            if (e.getSource() == creditos) ma.mostraCreditos();
            if (e.getSource() == ajuda) ma.mostraAjuda();
        }
    }
}
