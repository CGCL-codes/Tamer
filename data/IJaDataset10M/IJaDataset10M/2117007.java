package ceuclar.j2me.conexoes;

import calculadoraws.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class CalculadoraWebService extends MIDlet implements CommandListener, Runnable {

    private Form frm;

    private TextField num1;

    private TextField num2;

    private TextField res;

    private Command cmdSair;

    private Command cmdCalcular;

    private String url = "http://localhost:8084/J2ME_WebService/CalculadoraWS";

    private CalculadoraWSService_Stub servico;

    public CalculadoraWebService() {
        cmdSair = new Command("Sair", Command.EXIT, 0);
        cmdCalcular = new Command("Calcular", Command.ITEM, 0);
        num1 = new TextField("Num 1", "", 5, TextField.NUMERIC);
        num2 = new TextField("Num 2", "", 5, TextField.NUMERIC);
        res = new TextField("Resultado", "", 8, TextField.UNEDITABLE);
        frm = new Form("Calculadora WebService");
        frm.addCommand(cmdSair);
        frm.addCommand(cmdCalcular);
        frm.append(num1);
        frm.append(num2);
        frm.append(res);
        frm.setCommandListener(this);
    }

    public void startApp() {
        Display.getDisplay(this).setCurrent(frm);
        servico = new CalculadoraWSService_Stub();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdSair) {
            destroyApp(false);
            notifyDestroyed();
        } else if (c == cmdCalcular) {
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        try {
            String resultado = servico.Soma(num1.getString(), num2.getString());
            res.setString(resultado);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }
}
