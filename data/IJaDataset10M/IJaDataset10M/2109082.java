package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import jpa.Assistido;
import jpa.Familia;
import jpa.SituacaoFamilia;

@Named
public class FamiliaUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private AssistidoUtil assistidoUtil;

    /**
	 * Retorna a quantidade de dependentes
	 * 
	 * @return Integer
	 */
    public Integer getTotalDependentes(Familia familia) {
        if (SituacaoFamilia.Espera.equals(familia.getSituacao())) {
            return familia.getInscricao().getDependentes();
        } else {
            if (familia.getAssistidos() == null) {
                return null;
            }
            int total = 0;
            for (Assistido assistido : familia.getAssistidos()) {
                if (assistidoUtil.isDependente(assistido)) {
                    total++;
                }
            }
            return total;
        }
    }

    /**
	 * Retorna a lista de dependentes
	 * 
	 * @return List<Assistido>
	 */
    public List<Assistido> getDependentes(Familia familia) {
        if (SituacaoFamilia.Espera.equals(familia.getSituacao()) || familia.getAssistidos() == null) {
            return null;
        }
        List<Assistido> la = new ArrayList<Assistido>(0);
        for (Assistido assistido : familia.getAssistidos()) {
            Boolean isDep = assistidoUtil.isDependente(assistido);
            if (isDep != null && isDep) {
                la.add(assistido);
            }
        }
        return la;
    }
}
