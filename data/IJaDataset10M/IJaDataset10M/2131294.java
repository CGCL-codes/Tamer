package pInterficie;

import java.io.*;
import pConfig.CConfig;
import pVaqueria.CVaqueria;

public abstract class CInterficieCLI extends CInterficie {

    private boolean val_inicialitzats, fi_menu_inici;

    private int sistemes, munyibots, vaques;

    private int i_opcio;

    private String s_opcio;

    private BufferedReader buffer;

    private static BufferedWriter buf_fitx;

    public CInterficieCLI(CVaqueria vac) {
        super(vac);
        val_inicialitzats = false;
        fi_menu_inici = false;
        sistemes = 0;
        munyibots = 0;
        vaques = 0;
        buffer = new BufferedReader(new InputStreamReader(System.in));
        try {
            buf_fitx = new BufferedWriter(new FileWriter("prueba_tres_sistemas.txt"));
        } catch (Exception e) {
        }
    }

    @Override
    public void Obrir() {
        System.out.println("JMunyibot v. 0.1 en " + CConfig.SISNOM + " " + CConfig.SISVER + " (" + CConfig.SISARQ + ")");
        System.out.println("============================" + "============================\n\n");
        MostrarMissatgeInicial();
        while (!fi_menu_inici) {
            MostrarMenuInicial();
            if (!fi_menu_inici) {
                Esborrar();
                System.out.println("JMunyibot v. 0.1 en " + CConfig.SISNOM + " " + CConfig.SISVER + " (" + CConfig.SISARQ + ")");
                System.out.println("============================" + "============================\n\n");
            }
        }
        Executar();
    }

    public void MostrarMissatgeInicial() {
        System.out.println("\tPer comen" + CTRENCMIN + "ar, ha " + "d'introduir el n" + UTILDAGMIN + "mero\n" + "de sistemes de control, munyibots i " + "vaques dels\n" + "que constar" + ATILDGRMIN + " la simulaci" + OTILDAGMIN + ".\n");
    }

    public void MostrarMenuInicial() {
        System.out.println("\tMen" + UTILDAGMIN + " Inicial");
        System.out.println("----------------------------");
        if (sistemes == 0) System.out.println("1 " + PTLGEMIN + " Introduir el n" + UTILDAGMIN + "mero de sistemes"); else System.out.println("N" + UTILDAGMIN + "mero de sistemes = " + sistemes);
        if (munyibots == 0) System.out.println("2 " + PTLGEMIN + " Introduir el n" + UTILDAGMIN + "mero de munyibots"); else System.out.println("N" + UTILDAGMIN + "mero de munyibots = " + munyibots);
        if (vaques == 0) System.out.println("3 " + PTLGEMIN + " Introduir el n" + UTILDAGMIN + "mero de vaques"); else System.out.println("N" + UTILDAGMIN + "mero de vaques = " + vaques);
        if (sistemes > 0 && munyibots > 0 && vaques > 0) {
            System.out.println("\nEls par" + ATILDGRMIN + "metres s" + OTILDAGMIN + "n correctes? (S/N)");
            System.out.println("(Si s" + OTILDAGMIN + "n correctes, la" + "simulaci" + OTILDAGMIN + " començar" + ATILDGRMIN + ")");
        }
        try {
            s_opcio = buffer.readLine();
        } catch (IOException e) {
        }
        while (!OpcioIniciValida()) {
            System.out.println("\"" + s_opcio + "\"" + " no " + ETILDAGMIN + "s una opci" + OTILDAGMIN + " v" + ATILDGRMIN + "lida.");
            System.out.println("Per favor, introdueix una opci" + OTILDAGMIN + " v" + ATILDGRMIN + "lida.");
            try {
                s_opcio = buffer.readLine();
            } catch (IOException e) {
            }
        }
        switch(i_opcio) {
            case 1:
                sistemes = ObtenirValor("sistemes");
                if (munyibots > 0 && vaques > 0) val_inicialitzats = true;
                break;
            case 2:
                munyibots = ObtenirValor("munyibots");
                if (sistemes > 0 && vaques > 0) val_inicialitzats = true;
                break;
            case 3:
                vaques = ObtenirValor("vaques");
                if (sistemes > 0 && munyibots > 0) val_inicialitzats = true;
                break;
            case 4:
                fi_menu_inici = true;
                break;
            case 5:
                sistemes = vaques = munyibots = 0;
                val_inicialitzats = false;
                break;
        }
    }

    private boolean OpcioIniciValida() {
        try {
            i_opcio = Integer.parseInt(s_opcio);
            if (!val_inicialitzats && (i_opcio <= 0 || i_opcio > 3)) {
                return false;
            } else if (!val_inicialitzats) {
                if ((i_opcio == 1 && sistemes > 0) || (i_opcio == 2 && munyibots > 0) || (i_opcio == 3 && vaques > 0)) {
                    return false;
                }
            } else {
                if (s_opcio != "1" || s_opcio != "2") {
                    return false;
                } else {
                    if (s_opcio == "1") i_opcio = 4; else i_opcio = 5;
                }
            }
            return true;
        } catch (Exception e) {
            if (val_inicialitzats) {
                if (s_opcio.equals("s") || s_opcio.equals("S")) {
                    i_opcio = 4;
                    return true;
                }
                if (s_opcio.equals("n") || s_opcio.equals("N")) {
                    i_opcio = 5;
                    return true;
                }
            }
            return false;
        }
    }

    private int ObtenirValor(String par) {
        int n = 0;
        Esborrar();
        System.out.println("Per favor, introdueix el n" + UTILDAGMIN + "mero de " + par + " que simular" + ATILDGRMIN + " el programa.");
        try {
            n = Integer.parseInt(buffer.readLine());
        } catch (Exception e) {
        }
        while (!(n > 0)) {
            System.out.println(n + " no " + ETILDAGMIN + "s un n" + UTILDAGMIN + "mero v" + ATILDGRMIN + "lid.");
            System.out.println("Per favor, introdueix un n" + UTILDAGMIN + "mero major que zero.");
            try {
                n = Integer.parseInt(buffer.readLine());
            } catch (Exception e) {
            }
        }
        return n;
    }

    private void Executar() {
        char espera;
        vaqueria = new CVaqueria(sistemes, munyibots, vaques);
        try {
            espera = (char) System.in.read();
            Imprimir("---CAPTURADA SENYAL D'ACABAMENT---");
            System.in.skip(System.in.available());
        } catch (IOException e) {
        }
        Tancar(vaqueria, buf_fitx);
    }

    public static synchronized void Imprimir(String text) {
        System.out.println(text);
        try {
            buf_fitx.write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void Esborrar();
}
