package ao.com.bna.util;

import java.util.Calendar;

public class Calendario {

    public static void main(String[] args) {
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.set(2011, Calendar.MAY, 10);
        Calendar dataFinal = Calendar.getInstance();
        long diferenca = dataFinal.getTimeInMillis() - dataInicio.getTimeInMillis();
        int tempoDia = 1000 * 60 * 60 * 24;
        long diasDiferenca = diferenca / tempoDia;
        System.out.println("Entre a data inicial e final são " + diasDiferenca + " dias de diferença.");
    }
}
