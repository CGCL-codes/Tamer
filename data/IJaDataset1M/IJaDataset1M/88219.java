package org.alfredlibrary.utilitarios.financas;

import org.alfredlibrary.utilitarios.matematica.Logaritmo;

/**
 * Utilitário para cálculos envolvendo juros compostos, usando a fórmula:
 * 
 * FV = PV * (1 + i) ^ n
 * 
 * FV: Valor Futuro (do inglês Future Value)
 * PV: Valor Presente (do inglês Present Value)
 * i: Taxa de juros (do inglês Interest Rate)
 * n: Número de períodos
 *  
 * @author Rodrigo Moreira Fagundes
 * @since 03/10/2010
 */
public final class JurosCompostos {

    private JurosCompostos() {
        throw new AssertionError();
    }

    /**
	 * Obtém o valor futuro, que é o valor presente (inicial) ao final
	 * da operação, após sofrer todos juros por todo período indicado.
	 * 
	 * @param valorPresente Valor sujeito a juros
	 * @param juros Percentual de juros a que se sujeira o valor presente 
	 * @param periodos Quantidade de períodos a que o valor presente
	 * 					se sujeita aos juros
	 * @return Valor Futuro
	 */
    public static double obterValorFuturo(double valorPresente, double juros, double periodos) {
        return valorPresente * Math.pow(1 + (juros / 100), periodos);
    }

    /**
	 * Obtém o valor presente, original da operação, antes de se sujeitar
	 * aos juros.
	 * 
	 * @param valorFuturo Valor ao final da operação, após sofrer todos
	 * 						juros por todo período indicado
	 * @param juros Percentual de juros a que se sujeira o valor presente
	 * @param periodos Quantidade de períodos a que o valor presente
	 * 					se sujeita aos juros
	 * @return Valor Presente
	 */
    public static double obterValorPresente(double valorFuturo, double juros, double periodos) {
        return valorFuturo / Math.pow(1 + (juros / 100), periodos);
    }

    /**
	 * Obtém o juro percentual a que o valor presente se sujeita
	 * 
	 * @param valorPresente Valor sujeito a juros
	 * @param valorFuturo Valor ao final da operação, após sofrer todos
	 * 						juros por todo período indicado
	 * @param periodos Quantidade de períodos a que o valor presente
	 * 					se sujeita aos juros
	 * @return Percentual de juros a que se sujeira o valor presente
	 */
    public static double obterJuros(double valorPresente, double valorFuturo, double periodos) {
        return (Math.pow(valorFuturo / valorPresente, 1 / periodos) - 1) * 100;
    }

    /**
	 * Obtém quantidade de períodos em que o valor presente está sujeito aos juros
	 * 
	 * @param valorPresente Valor sujeito a juros
	 * @param valorFuturo Valor ao final da operação, após sofrer todos
	 * 						juros por todo período indicado
	 * @param juros Percentual de juros a que se sujeira o valor presente
	 * @return Quantidade de períodos a que o valor presente se sujeita aos juros
	 */
    public static double obterPeriodos(double valorPresente, double valorFuturo, double juros) {
        return Logaritmo.obter(valorFuturo / valorPresente, 1 + (juros / 100));
    }
}
