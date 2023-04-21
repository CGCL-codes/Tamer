package tutorial.BuilderSiDAO.DAOPattern;

import tutorial.BuilderSiDAO.commons.ptDAOPersoana;
import tutorial.BuilderSiDAO.customGUI.PersoanaGUIInput;
import tutorial.BuilderSiDAO.customGUI.PersoanaGUIOutput;

/**
 *Aceasta implementare a interfetei PersoanaDAO stie sa preia date de la un form
 * swing (PersoanaGUIInput) si le exporta la alt form swing (PersoanaGUIOutput)
 * 
 * din nou aceste doua form-uri sunt injectate in constructoru
 * @author Boogie
 */
public class SwingPersoanaDAO implements ptDAOPersoana.PersoanaDAO {

    private final PersoanaGUIInput input;

    private final PersoanaGUIOutput output;

    public SwingPersoanaDAO(PersoanaGUIInput input, PersoanaGUIOutput output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public ptDAOPersoana importPersoasa() {
        ptDAOPersoana p = new ptDAOPersoana();
        p.setNume(input.getNumeField().getText());
        p.setPrenume(input.getPrenumeField().getText());
        p.setAdresa(input.getAdresaField().getText());
        return p;
    }

    @Override
    public void exportPersoana(ptDAOPersoana p) {
        output.getNumeLabel().setText(p.getNume());
        output.getPrenumeLabel().setText(p.getPrenume());
        output.getAdresaLabel().setText(p.getAdresa());
    }
}
