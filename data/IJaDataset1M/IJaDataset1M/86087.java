package br.com.smartinteractive.ii.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class TelaPrincipal extends MIDlet implements CommandListener {

    private StringItem textoApresentacao;

    private Form f;

    private static Display disp;

    private StringItem result;

    /**
	 * Op��es do menu
	 */
    private Command menuFilme = new Command("Filmes", Command.OK, 1);

    private Command menuRequisitarFila = new Command("Requisitar Fila", Command.OK, 1);

    private Command menuAJuda = new Command("Ajuda", Command.OK, 1);

    private Command menuSobre = new Command("Sobre", Command.OK, 1);

    private Command cSair;

    private String TITULO_FORM = "INTERACTIVE INFORMATION";

    public TelaPrincipal() {
        disp = Display.getDisplay(this);
        f = new Form(TITULO_FORM);
        f.setCommandListener(this);
        textoApresentacao = new StringItem("Bem vindo ao Interactive Information. Para informa��es dos cinemas e requisitar filas, clique no menu.", "");
        result = new StringItem("Resultado: ", "");
        cSair = new Command("Sair", Command.EXIT, 1);
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    public static void setarFormAtual(Form form) {
        TelaPrincipal.disp.setCurrent(form);
    }

    protected void startApp() throws MIDletStateChangeException {
        f.append(textoApresentacao);
        f.addCommand(cSair);
        f.addCommand(menuFilme);
        f.addCommand(menuRequisitarFila);
        f.addCommand(menuAJuda);
        f.addCommand(menuSobre);
        disp.setCurrent(f);
    }

    public void commandAction(Command comando, Displayable display) {
        if (comando.equals(menuAJuda)) {
            TelaPrincipal.setarFormAtual(TelaAjuda.getInstancia());
        } else if (comando.equals(menuSobre)) {
        }
    }
}
