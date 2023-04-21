package trabalho.biblioteca;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import trabalho.comum.*;

public class Emprestimo extends UnicastRemoteObject implements IEmprestimo {

    private static final long serialVersionUID = 1L;

    private Date data_inicio;

    private Date data_fim;

    private Date devolvido;

    private IMembro membro;

    /**
	 * Constructor
	 * @param membro
	 * @param duracao_emprestimo
	 */
    public Emprestimo(IMembro membro, int dias_emprestimo) throws RemoteException {
        this.membro = membro;
        this.data_inicio = new Date();
        DefinirDuracao(dias_emprestimo);
    }

    public Date getData_fim() throws RemoteException {
        return data_fim;
    }

    public Date getData_inicio() throws RemoteException {
        return data_inicio;
    }

    public Date getDevolvido() throws RemoteException {
        return devolvido;
    }

    public IMembro getMembro() throws RemoteException {
        return membro;
    }

    /**
	 * Termina um empr�stimo
	 */
    public void Terminar() throws RemoteException {
        devolvido = new Date();
    }

    /**
	 * Indica se o objecto se encontra ou n�o emprestado
	 * @return
	 */
    public boolean EstaEmprestado() throws RemoteException {
        if (devolvido == null) return true;
        return false;
    }

    /**
	 * Define a data_fim atrav�s da dura��o do empr�stimo
	 * @param duracao_emprestimo - Ter� de ter uma data com tamanho int e n�o long
	 */
    public void DefinirDuracao(int duracao) throws RemoteException {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(data_inicio);
        cal1.add(Calendar.SECOND, duracao);
        data_fim = cal1.getTime();
    }

    public String toStr() throws RemoteException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String str = "Emprestado em " + df.format(data_inicio);
        str += ", ao " + membro.toStr();
        if (devolvido != null) str += ", devolvido em " + df.format(devolvido);
        return str;
    }
}
