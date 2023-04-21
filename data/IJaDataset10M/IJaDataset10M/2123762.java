package br.ufrj.dcc.sistemasoperacionais.passagensaereas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author  Administrator
 * @version
 */
public class PassagensAereas extends MIDlet implements CommandListener {

    DataInputStream inputStream = null;

    DataOutputStream outputStream = null;

    Command comprar;

    TextField quantidadeVagas;

    ChoiceGroup choices;

    Conexao conexao;

    TextField host;

    Command sair;

    Command conectar;

    String[] elements;

    int choicesId;

    Form principal;

    StringItem sucessoCompra = new StringItem("Compra efetuada com sucesso!", "Compra efetuada com sucesso!");

    StringItem fracassoCompra = new StringItem("Compra n�o efetuada!", "Compra n�o efetuada!");

    int compra;

    public void startApp() {
        Form servidor = new Form("Dados do servidor");
        servidor.append(new StringItem("Host e porta", "\nInsira o host ou ip do servidor no formato host:porta ou ip:porta."));
        host = new TextField("", "127.0.0.1:5000", 120, TextField.ANY);
        servidor.append(host);
        conectar = new Command("Conectar", Command.OK, 1);
        servidor.addCommand(conectar);
        sair = new Command("Sair", Command.EXIT, 1);
        servidor.addCommand(sair);
        servidor.setCommandListener(this);
        getDisplay().setCurrent(servidor);
    }

    public void getPrincipal(Vector trechos, boolean sucesso, boolean comprou) {
        principal = new Form("Passagens A�reas");
        elements = new String[trechos.size()];
        trechos.copyInto(elements);
        Image[] images = new Image[trechos.size()];
        choices = new ChoiceGroup("Escolha a rota:", Choice.EXCLUSIVE, elements, images);
        principal.append(choices);
        quantidadeVagas = new TextField("Quantidade de Vagas", null, 3, TextField.NUMERIC);
        choicesId = principal.append(quantidadeVagas);
        if (!comprou) {
            StringItem labelCompra = new StringItem("", "");
            compra = principal.append(labelCompra);
        } else {
            if (sucesso) {
                compra = principal.append(sucessoCompra);
            } else {
                compra = principal.append(fracassoCompra);
            }
        }
        comprar = new Command("Comprar", Command.OK, 1);
        principal.addCommand(comprar);
        sair = new Command("Sair", Command.EXIT, 1);
        principal.addCommand(sair);
        principal.setCommandListener(this);
        getDisplay().setCurrent(principal);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == comprar) {
            conexao = new Conexao(host.getString(), this, true);
            conexao.start();
            return;
        }
        if (command == sair) {
            exitMIDlet();
        }
        if (command == conectar) {
            conexao = new Conexao(host.getString(), this, false);
            conexao.start();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    public void exitMIDlet() {
        getDisplay().setCurrent(null);
        destroyApp(true);
        notifyDestroyed();
    }
}

class Conexao extends Thread {

    String host;

    PassagensAereas midlet;

    boolean comprar = false;

    public Conexao(String host, PassagensAereas midlet, boolean comprar) {
        this.host = "socket://" + host;
        this.midlet = midlet;
        this.comprar = comprar;
    }

    public void run() {
        try {
            SocketConnection socket = (SocketConnection) Connector.open(host, Connector.READ_WRITE);
            midlet.inputStream = socket.openDataInputStream();
            midlet.outputStream = socket.openDataOutputStream();
            if (comprar) {
                String trecho = midlet.choices.getString(midlet.choices.getSelectedIndex());
                int quantidadeVagas = Integer.parseInt(midlet.quantidadeVagas.getString());
                try {
                    int trechoId = Integer.parseInt(trecho.substring(0, trecho.indexOf(" ")));
                    midlet.outputStream.writeInt(3);
                    midlet.outputStream.writeInt(quantidadeVagas);
                    midlet.outputStream.writeInt(trechoId);
                    midlet.outputStream.writeInt(4);
                    midlet.outputStream.writeInt(quantidadeVagas);
                    midlet.outputStream.writeInt(trechoId);
                    midlet.getPrincipal(getTrechos(), midlet.inputStream.readBoolean(), true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                midlet.getPrincipal(getTrechos(), false, false);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Vector getTrechos() {
        Vector vector = new Vector();
        try {
            midlet.outputStream.writeInt(1);
            String trechos = midlet.inputStream.readUTF();
            String token = ";";
            int tokenPositionBegin = 0;
            int tokenPositionEnd = trechos.indexOf(token);
            while (tokenPositionEnd != -1) {
                String trecho = trechos.substring(tokenPositionBegin, tokenPositionEnd);
                String token2 = "@";
                int primeiroToken = trecho.indexOf(token2);
                int segundoToken = trecho.indexOf(token2, primeiroToken + 1);
                StringBuffer sb = new StringBuffer(trecho);
                sb.deleteCharAt(segundoToken);
                sb.insert(segundoToken, " Vagas:");
                sb.deleteCharAt(primeiroToken);
                sb.insert(primeiroToken, " Trecho:");
                vector.addElement(sb.toString());
                tokenPositionBegin = tokenPositionEnd + 1;
                tokenPositionEnd = trechos.indexOf(token, tokenPositionBegin + 1);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return vector;
    }
}
