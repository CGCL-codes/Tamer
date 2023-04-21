package opencafe;

import opencafe.banco.Banco;
import opencafe.banco.listener.BancoOpenAdapter;
import opencafe.banco.listener.BancoOpenFailedAdapter;
import opencafe.beans.Config;
import opencafe.tela.TelaHorarios;
import opencafe.tela.TelaPrimeiraConfig;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Esta � a classe que representar� a aplica��o como um todo. ela � um singleton
 * que ir� fazer as vezes de contexto geral para facilitar o acesso a certos
 * componentes, como o banco e objetos de configura��o.
 * 
 * @author leonardo
 * 
 */
public class Main {

    private static Main main;

    public static Main getInstancia() {
        return main;
    }

    private Banco banco;

    private Display display = new Display();

    private Image icone;

    /**
	 * Forma r�pida de encontrarmos o banco. � melhor do que ifcar distribuindo
	 * refer�ncias a ele como quem d� santinho de candidato na rua.
	 * 
	 * @return
	 */
    public Banco getBanco() {
        return banco;
    }

    /**
	 * getter das configura��es de pre�o, etc...
	 * @return
	 */
    public Config getConfig() {
        return banco.getConfig();
    }

    /**
	 * getter para nosso �cone.
	 * @return
	 */
    public Image getIcone() {
        return icone;
    }

    /**
	 * No construtor iremos definir alguns dos eventos mais importantes do
	 * banco, sua abertura, fechamento e configura��o inicial.
	 * 
	 * @throws Exception
	 */
    public Main() throws Exception {
        try {
            banco = new Banco();
        } catch (Exception e) {
            Shell s = new Shell(display);
            MessageBox mb = new MessageBox(s, SWT.ICON_ERROR);
            mb.setText("Erro");
            mb.setMessage("J� existe uma inst�ncia do Open Caf� aberta. " + "Se quiser uma nova, deve fechar a outra. Verifique " + "as janelas abertas e o �cone da bandeja.");
            mb.open();
            s.close();
            this.close();
            throw e;
        }
    }

    /**
	 * iniciamos colocando dois eventos que determinam o que ir� ocorrer em cada
	 * tentativa de abrir o banco.
	 */
    private void prepararBanco() {
        banco.addBancoOpenListener(new BancoOpenAdapter() {

            public void opened(Banco self) {
                Shell shellPrincipal = new Shell(display);
                shellPrincipal.setImage(icone);
                new TelaHorarios(shellPrincipal);
                shellPrincipal.pack();
                shellPrincipal.setSize(800, 600);
                shellPrincipal.setVisible(true);
            }
        });
        banco.addBancoOpenFailedListener(new BancoOpenFailedAdapter() {

            public void openFailed(Banco self) {
                Shell shellConfiguracao = new Shell(display, SWT.CLOSE);
                shellConfiguracao.setImage(icone);
                new TelaPrimeiraConfig(shellConfiguracao);
                shellConfiguracao.pack();
                Point p = shellConfiguracao.getSize();
                p.x = 640;
                shellConfiguracao.setSize(p);
                shellConfiguracao.setVisible(true);
            }
        });
    }

    /**
	 * MainLoop da aplica��o
	 */
    private void iniciar() {
        icone = new Image(display, getClass().getResourceAsStream("opencafe.png"));
        banco.open();
        Display.setAppName("OpenCafe");
        while (!display.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    /**
	 * Helper para terminarmos a aplica��o a qualquer instante.
	 */
    public void close() {
        if (banco != null) banco.close();
        display.dispose();
        icone.dispose();
    }

    /**
	 * Entry point da aplica��o. N�o recebemos argumento ainda, mas podemos
	 * mudar isso no futuro.
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        main = new Main();
        main.prepararBanco();
        main.iniciar();
    }
}
