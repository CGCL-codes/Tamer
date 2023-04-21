package core.util;

import gui.MainSystray;
import gui.factory.components.AbstractDialogWithTime;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import resources.Messages;

/**
 * @author Glauber Magalh�es Pires
 *
 */
public final class OSFunctions {

    /**
     * Turns Off the computer
     *
     */
    public static final void turnOff() {
        String ok = Messages.message.getString("geral.ok");
        String cancel = Messages.message.getString("geral.cancel");
        String[] opcoesTexto = new String[2];
        opcoesTexto[0] = ok;
        opcoesTexto[1] = cancel;
        String pergunta = Messages.message.getString("geral.confirmTurnOff");
        AbstractDialogWithTime dialogWithTime = MainSystray.guiFactory.getMessageDisplayer().createDialogWithTime(pergunta, 30, opcoesTexto, ok, cancel, cancel);
        dialogWithTime.start();
        Object opcaoSelecionada = dialogWithTime.getAnswer();
        if (opcaoSelecionada.equals(cancel)) return;
        String SO = System.getProperty("os.name");
        if (SO.equals("Windows XP")) turnOffWinNT(); else if (SO.equals("Windows 2000")) turnOffWinNT(); else if (SO.equals("Windows 2003")) turnOffWinNT(); else if (SO.equals("Linux")) turnOffLinux(); else MainSystray.guiFactory.getMessageDisplayer().displayErrorMessage(Messages.exception.getString("geral.OSFunctionRestriction"));
    }

    public static final void openFile(String arquivo) {
        String SO = System.getProperty("os.name");
        if (SO.equals("Windows XP")) abrirNT(arquivo); else if (SO.equals("Windows 2000")) abrirNT(arquivo); else if (SO.equals("Windows 2003")) abrirNT(arquivo); else MainSystray.guiFactory.getMessageDisplayer().displayErrorMessage(Messages.exception.getString("geral.OSFunctionRestriction"));
    }

    private static final void turnOffWinNT() {
        try {
            Runtime.getRuntime().exec("shutdown -s -f");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final void hibernate() {
        try {
            Runtime.getRuntime().exec("Rundll32.exe Powrprof.dll,SetSuspendState Hibernate");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final void turnOffLinux() {
        try {
            Runtime.getRuntime().exec("shutdown -h 0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final void abrirNT(String arquivo) {
        try {
            Runtime.getRuntime().exec("cmd.exe /c start \"OpenP2M\" \"" + arquivo + "\"");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getClipboard() {
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try {
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) t.getTransferData(DataFlavor.stringFlavor);
                return text;
            }
        } catch (UnsupportedFlavorException e) {
        } catch (IOException e) {
        }
        return null;
    }

    public static void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }
}
