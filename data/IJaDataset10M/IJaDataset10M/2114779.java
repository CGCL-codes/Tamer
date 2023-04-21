package org.jcompany.config.domain;

import org.jcompany.config.domain.PlcConfigPattern.Complexity;
import org.jcompany.config.domain.PlcConfigPattern.ExclusionMode;
import org.jcompany.config.domain.PlcConfigPattern.Logic;
import org.jcompany.config.domain.PlcConfigPattern.ModalityLogic;

/**
 * Conven��es globais de defini��o de l�gicas MVC-P padroes para A�oes
 *
 */
public class PlcConfigPatternConvention {

    /**
	 * Define a l�gica da colabora��o
	 */
    private Logic logic;

    /**
	 * Define complexidade da colabora��o
	 */
    private Complexity complexity;

    /**
	 * Define como o jCompany vai processar um exclus�o
	 * @see ExclusionMode 
	 */
    private ExclusionMode exclusionMode = ExclusionMode.PHYSICAL;

    /**
	 * Define o type de varia��o da colabora��o 
	 */
    private ModalityLogic modality = ModalityLogic.A;

    public Logic logic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public Complexity complexity() {
        return complexity;
    }

    public void setComplexity(Complexity complexity) {
        this.complexity = complexity;
    }

    public ExclusionMode exclusionMode() {
        return exclusionMode;
    }

    public void setExclusionMode(ExclusionMode exclusionMode) {
        this.exclusionMode = exclusionMode;
    }

    public ModalityLogic modality() {
        return modality;
    }

    public void setModality(ModalityLogic modality) {
        this.modality = modality;
    }
}
