package monopoly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Armazena os comandos possíveis do jogo
 * @author Marcus
 */
public class Comandos {

    /**
     * Lista dos comandos
     */
    private List<String> cmds = new ArrayList<String>();

    private List<String> cmdsOrdenados = new ArrayList<String>();

    /**
     * Flag de comando Pay incluido
     */
    private boolean comandoPayIncluido = false;

    /**
     * Flag de comando Build incluido
     */
    private boolean comandoBuildIncluido = false;

    /**
     * Flag de comando Sell incluido
     */
    private boolean comandoSellIncluido = false;

    /**
     * Flag de comando Mortgage incluido
     */
    private boolean comandoMortgageIncluido = false;

    private boolean comandoUnmortgageIncluido = false;

    private boolean comandoGiveUpIncluido = false;

    private boolean comandoRollIncluido = false;

    private boolean comandoAvoidIncluido;

    /**
     * construtor de comandos
     */
    public Comandos() {
        cmds.clear();
        initComandos();
    }

    /**
     * Inicializa a colecao de comandos
     */
    public void initComandos() {
        addComandoRoll();
        cmds.add("status");
        cmds.add("quit");
        cmdsOrdenados.add("roll");
        cmdsOrdenados.add("status");
        cmdsOrdenados.add("quit");
        cmdsOrdenados.add("pay");
        cmdsOrdenados.add("build");
        cmdsOrdenados.add("sell");
        cmdsOrdenados.add("mortgage");
        cmdsOrdenados.add("unmortgage");
        cmdsOrdenados.add("avoid");
        cmdsOrdenados.add("giveup");
    }

    public List<String> retornaComandosNaOrdem() {
        List<String> comandosExistentesOrdenados = new ArrayList<String>();
        for (String co : cmdsOrdenados) for (String c : cmds) if (co.equals(c)) {
            comandosExistentesOrdenados.add(c);
            break;
        }
        return comandosExistentesOrdenados;
    }

    /**
     * Obtem a lista de comandos
     * @return a lista de comandos
     */
    public List getCmds() {
        return cmds;
    }

    /**
     * Adiciona comando Pay
     */
    public void addComandoPay() {
        if (!comandoPayIncluido) {
            cmds.add("pay");
            this.comandoPayIncluido = true;
        }
    }

    /**
     * Remove comando Pay
     */
    public void removerComandoPay() {
        if (comandoPayIncluido) {
            cmds.remove("pay");
            this.comandoPayIncluido = false;
        }
    }

    /**
     * Adiciona comando Build
     */
    public void addComandoBuild() {
        if (!comandoBuildIncluido) {
            cmds.add("build");
            this.comandoBuildIncluido = true;
        }
    }

    /**
     * Adiciona comando Sell
     */
    public void addComandoSell() {
        if (!comandoSellIncluido) {
            cmds.add("sell");
            this.comandoSellIncluido = true;
        }
    }

    /**
     * Remove comando Build
     */
    public void removerComandoBuild() {
        if (comandoBuildIncluido) {
            cmds.remove("build");
            this.comandoBuildIncluido = false;
        }
    }

    /**
     * Adiciona comando Hypothecate
     */
    public void addComandoMortgage() {
        if (!comandoMortgageIncluido) {
            cmds.add("mortgage");
            this.comandoMortgageIncluido = true;
        }
    }

    public void addComandoAvoid() {
        if (!comandoAvoidIncluido) {
            cmds.add("avoid");
            this.comandoAvoidIncluido = true;
        }
    }

    public boolean isComandoAvoidIncluido() {
        return comandoAvoidIncluido;
    }

    public void removerComandoAvoid() {
        if (comandoAvoidIncluido) {
            cmds.remove("avoid");
            this.comandoAvoidIncluido = false;
        }
    }

    /**
     * Adiciona comando Desipotecar
     */
    public void addComandoUnMortgage() {
        if (!comandoUnmortgageIncluido) {
            cmds.add("unmortgage");
            this.comandoUnmortgageIncluido = true;
        }
    }

    public boolean isComandoRollIncluido() {
        return comandoRollIncluido;
    }

    public void addComandoGiveUp() {
        if (!comandoGiveUpIncluido) {
            cmds.add("giveup");
            this.comandoGiveUpIncluido = true;
        }
    }

    public void addComandoRoll() {
        if (!comandoRollIncluido) {
            cmds.add("roll");
            this.comandoRollIncluido = true;
        }
    }

    public void removerComandoGiveUp() {
        if (comandoGiveUpIncluido) {
            cmds.remove("giveup");
            this.comandoGiveUpIncluido = false;
        }
    }

    public void removerComandoRoll() {
        if (comandoRollIncluido) {
            cmds.remove("roll");
            this.comandoRollIncluido = false;
        }
    }

    /**
     * Remove comando Hypothecate
     */
    public void removerComandoUnMortgage() {
        if (comandoUnmortgageIncluido) {
            cmds.remove("unmortgage");
            this.comandoUnmortgageIncluido = false;
        }
    }

    /**
     * Remove comando Hypothecate
     */
    public void removerComandoMortgage() {
        if (comandoMortgageIncluido) {
            cmds.remove("mortgage");
            this.comandoMortgageIncluido = false;
        }
    }

    /**
     * Remover comando Sell
     */
    public void removerComandoSell() {
        if (comandoSellIncluido) {
            cmds.remove("sell");
            this.comandoSellIncluido = false;
        }
    }

    /**
     * Exibe os comandos
     */
    public void showComandos() {
        Iterator<String> a = cmds.iterator();
        while (a.hasNext()) {
            System.out.print("[" + a.next() + "]");
        }
        System.out.println("\n");
    }
}
