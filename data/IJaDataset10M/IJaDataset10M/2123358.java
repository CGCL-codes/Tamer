package net.sourceforge.sctmf.view.modelos_formais.ling_regul.afnd;

import net.sourceforge.sctmf.model.pojo.AFD;
import net.sourceforge.sctmf.model.pojo.AFND;
import net.sourceforge.sctmf.model.pojo.Estado;
import net.sourceforge.sctmf.model.pojo.ModeloFormal;
import net.sourceforge.sctmf.model.pojo.Simbolo;
import net.sourceforge.sctmf.view.modelos_formais.ling_regul.afd.AfdGUI;
import java.util.Collection;

/**
 *
 * @author  Cassolato
 */
public class AfndGUI extends AfdGUI {

    private CadEstAFND cea = null;

    private CadFunTransAFND cfta = null;

    private ValSeqAFND vsa = null;

    /** Creates new form BeanForm */
    public AfndGUI() {
        cea = new CadEstAFND();
        cfta = new CadFunTransAFND();
        vsa = new ValSeqAFND(this);
        this.posInit();
    }

    private void posInit() {
        getPCard().remove(1);
        getPCard().add(cea, cardNames.get(1), 1);
        getPCard().remove(2);
        getPCard().add(cfta, cardNames.get(2), 2);
        getPCard().remove(3);
        getPCard().add(vsa, cardNames.get(3), 3);
    }

    protected void changeCards(final int direction) {
        switch(direction) {
            case NEXT:
                activeCard++;
                if (activeCard == 2) cfta.observer(cea.getEstados(), getPCadAlf().getSimbolos()); else if (activeCard == 3) vsa.observerValSeq((AFND) getModeloFormal());
                break;
            case PREVIOUS:
                activeCard--;
                break;
            default:
                {
                    activeCard = 0;
                    getBNext().setEnabled(true);
                    getBBack().setEnabled(false);
                }
        }
        ((java.awt.CardLayout) getPCard().getLayout()).show(getPCard(), cardNames.get(activeCard));
    }

    private void initComponents() {
    }

    public void setModeloFormal(ModeloFormal fm) {
        AFND afnd = (AFND) fm;
        this.getPCadAlf().addSimbolos(afnd.getSimbolos());
        cea.setEstados(afnd.getEstados());
        cea.setEstadosIniciais(afnd.getEstadosIniciais());
        cea.setEstadosFinais(afnd.getEstadosFinais());
        cfta.setFuncTrans(afnd.getTransicoes());
        this.changeCards(-1);
    }

    public ModeloFormal getModeloFormal() {
        AFND afnd = new AFND();
        Collection<Simbolo> simbolos = getPCadAlf().getSimbolos();
        afnd.addAllSimbolos(simbolos);
        Collection<Estado> estados = cea.getEstados();
        afnd.addAllEstados(estados);
        afnd.addAllEstadosIniciais(cea.getEstadosIniciais());
        afnd.addAllEstFinais(cea.getEstadosFinais());
        cfta.observer(estados, simbolos);
        afnd.clearAllTransicoes();
        afnd.addAllTransicoes(cfta.getFuncTrans());
        return afnd;
    }
}
