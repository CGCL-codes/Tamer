package de.fraunhofer.isst.axbench.operations.metrics;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;

/**
 * @brief Computations of expense metrics.
 * 
 * This class encapsulates the computations according to kiebusch.
 * 
 * It additionally contains the computation tables for experience values.
 * The tables are predefined, later they have to be adaptable.
 * 
 * @todo make the computation tables editable
 * @todo translate documentation
 * 
 * @author ekleinod
 * @version 0.8.0
 * @since 0.8.0
 */
public class ExpenseComputation {

    private static final String ERROR_COMPLEXITY = "Complexity ''{1}'' of subcomponent ''{0}'' not valid.";

    private static final String ERROR_HOOKS = "The number of hooks of subcomponent ''{0}'' is zero.";

    private static final String ERROR_IMPLEMENTATIONS = "The number of implementations of subcomponent ''{0}'' is zero.";

    private static final String ERROR_PORTSUM = "The number of ports of subcomponent ''{0}'' is zero.";

    private static final String ERROR_PRODUCTCOUNT = "The number of products is zero.";

    private static final String ERROR_REALTIMECATEGORY = "Realtime category ''{1}'' of subcomponent ''{0}'' not valid.";

    private static final String ERROR_VARIABILITY = "Variability ''{1}'' of subcomponent ''{0}'' not valid.";

    private static final int REALTIMECATEGORY_SOFT = 0;

    private static final int REALTIMECATEGORY_HARD = 1;

    /** @brief realtime categories */
    private static final ArrayList<String> REALTIMECATEGORY_VALUES = new ArrayList<String>(Arrays.asList(new String[] { "soft", "hard" }));

    private static final int COMPLEXITY_LOW = 0;

    private static final int COMPLEXITY_MIDDLE = 1;

    private static final int COMPLEXITY_HIGH = 2;

    /** 
	 * @brief Komplexitätstabelle
	 * 
	 * Indices:
	 * -# Ausgangssignale
	 * -# Eingangssignale
	 */
    private static final int[][] arrComplexity = { { COMPLEXITY_LOW, COMPLEXITY_LOW, COMPLEXITY_LOW, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_HIGH }, { COMPLEXITY_LOW, COMPLEXITY_LOW, COMPLEXITY_LOW, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_HIGH }, { COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_MIDDLE, COMPLEXITY_HIGH }, { COMPLEXITY_HIGH, COMPLEXITY_HIGH, COMPLEXITY_HIGH, COMPLEXITY_HIGH, COMPLEXITY_HIGH, COMPLEXITY_HIGH, COMPLEXITY_HIGH } };

    /** @brief Komplexitätswerte (korrespondierend zu den Komplexitätsindices) */
    private static final ArrayList<String> COMPLEXITY_VALUES = new ArrayList<String>(Arrays.asList(new String[] { "low", "medium", "high" }));

    /** 
	 * @brief Umrechnungsfaktorentabelle (gemeinsam)
	 * 
	 * Indices:
	 * -# Echtzeitkategorie
	 * -# Komplexität
	 */
    private static final int[][] arrComputationFactorShared = { { 10, 15, 23 }, { 15, 23, 35 } };

    /** 
	 * @brief Umrechnungsfaktorentabelle (variable)
	 * 
	 * Indices:
	 * -# Echtzeitkategorie
	 * -# Komplexität
	 */
    private static final int[][] arrComputationFactorVariable = { { 5, 7, 10 }, { 7, 10, 15 } };

    /** 
	 * @brief komplexitätsabhängige Korrekturfaktoren (gemeinsam)
	 * 
	 * Index:
	 * -# Komplexität
	 */
    private static final double[] arrCorrectionFactorShared = { 1.0, 1.0, 1.0 };

    /** 
	 * @brief komplexitätsabhängige Korrekturfaktoren (variabel)
	 * 
	 * Index:
	 * -# Komplexität
	 */
    private static final double[] arrCorrectionFactorVariable = { 1.0, 1.0, 1.0 };

    protected static final int VARIABILITY_EG = 0;

    protected static final int VARIABILITY_EV = 1;

    /** @brief Variabilitätswerte (korrespondierend zu den Variabilitätsindices) */
    protected static final String[] VARIABILITY_VALUES = { "EG", "EV" };

    /**
	 * @brief Berechnung der Funktionswerte
	 * 
	 * Diese Funktion berechnet die gesamten Werte für eine Funktion.@n
	 * Dabei werden die Werte direkt in die übergebene Funktion eingetragen,
	 * daher hat die Funktion keinen Rückgabewert.
	 * 
	 * Die Funktion dient dazu, nicht die einzelnen Berechnungsschritte
	 * einzeln aufrufen zu müssen.
	 * Sie stellt zudem sicher, dass Funktionen, die berechnete Werte
	 * benötigen, auch erst dann aufgerufen werden, wenn diese Werte
	 * berechnet wurden. 
	 * Daher können in diesen Funktionen die berechneten Werte als vorhanden 
	 * vorausgesetzt werden.
	 * 
	 * @param theSubComponent subcomponent to compute
	 * @param iProductCount Produktanzahl
	 * @throws AXLException if the values of the subcomponent are erroneous
	 */
    public static void computeValues(ExpenseSubComponent fctInput, int iProductCount) throws AXLException {
        if (iProductCount == 0) {
            throw new AXLException(ERROR_PRODUCTCOUNT);
        }
        int iRealtime = REALTIMECATEGORY_VALUES.indexOf(fctInput.sRealTimeCategory);
        if ((iRealtime != REALTIMECATEGORY_HARD) && (iRealtime != REALTIMECATEGORY_SOFT)) {
            throw new AXLException(MessageFormat.format(ERROR_REALTIMECATEGORY, fctInput.sName, fctInput.sRealTimeCategory));
        }
        if ((fctInput.iPortsIn + fctInput.iPortsOut) == 0) {
            throw new AXLException(MessageFormat.format(ERROR_PORTSUM, fctInput.sName));
        }
        if (fctInput.iHooks == 0) {
            throw new AXLException(MessageFormat.format(ERROR_HOOKS, fctInput.sName));
        }
        if ((fctInput.sVariability != VARIABILITY_VALUES[0]) && (fctInput.sVariability != VARIABILITY_VALUES[1])) {
            throw new AXLException(MessageFormat.format(ERROR_VARIABILITY, fctInput.sName, fctInput.sVariability));
        }
        if (fctInput.dImplementations == 0.0) {
            throw new AXLException(MessageFormat.format(ERROR_IMPLEMENTATIONS, fctInput.sName));
        }
        computeComplexity(fctInput);
        int iComplexity = COMPLEXITY_VALUES.indexOf(fctInput.sComplexity);
        if (iComplexity < 0) {
            throw new AXLException(MessageFormat.format(ERROR_COMPLEXITY, fctInput.sName, fctInput.sComplexity));
        }
        computeComputationFactorShared(fctInput);
        computeComputationFactorVariable(fctInput);
        computeCorrectionFactorShared(fctInput);
        computeCorrectionFactorVariable(fctInput);
        computeGFactor(fctInput);
        computeVFactor(fctInput);
        computeUPFPhEG(fctInput, iProductCount);
        computeUPFPhEV(fctInput, iProductCount);
        computeUPFPver(fctInput);
        computeUPFP(fctInput);
        computeUPFPPA(fctInput, iProductCount);
    }

    /**
	 * @brief Berechnung der Komplexität
	 * 
	 * Die Berechnung der Komplexität beruht auf Werten der Komplexitätstabelle.@n
	 * Eingangsparameter:
	 * - Anzahl der Eingangssignale
	 * - Anzahl der Ausgangssignale
	 * 
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeComplexity(ExpenseSubComponent fctInput) {
        int iOut = (fctInput.iPortsOut > (arrComplexity.length - 1)) ? (arrComplexity.length - 1) : fctInput.iPortsOut;
        int iIn = (fctInput.iPortsIn > (arrComplexity[0].length - 1)) ? (arrComplexity[0].length - 1) : fctInput.iPortsIn;
        fctInput.sComplexity = COMPLEXITY_VALUES.get(arrComplexity[iOut][iIn]);
    }

    /**
	 * @brief Berechnung des Umrechnungsfaktors (gemeinsam) @f$UF_{gem}@f$
	 * 
	 * Die Berechnung des Umrechnungsfaktors (gemeinsam) beruht auf Werten der Umrechnungsfaktorentabelle (gemeinsam).@n
	 * Eingangsparameter:
	 * - Komplexität
	 * - Echtzeitkategorie
	 * 
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeComputationFactorShared(ExpenseSubComponent fctInput) {
        int iRealtime = REALTIMECATEGORY_VALUES.indexOf(fctInput.sRealTimeCategory);
        int iComplexity = COMPLEXITY_VALUES.indexOf(fctInput.sComplexity);
        fctInput.iUFgem = arrComputationFactorShared[iRealtime][iComplexity];
    }

    /**
	 * @brief Berechnung des Umrechnungsfaktors (variabel) @f$UF_{var}@f$
	 * 
	 * Die Berechnung des Umrechnungsfaktors (variabel) beruht auf Werten der Umrechnungsfaktorentabelle (variabel).@n
	 * Eingangsparameter:
	 * - Komplexität
	 * - Echtzeitkategorie
	 * 
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeComputationFactorVariable(ExpenseSubComponent fctInput) {
        int iRealtime = REALTIMECATEGORY_VALUES.indexOf(fctInput.sRealTimeCategory);
        int iComplexity = COMPLEXITY_VALUES.indexOf(fctInput.sComplexity);
        fctInput.iUFvar = arrComputationFactorVariable[iRealtime][iComplexity];
    }

    /**
	 * @brief Berechnung des komplexitätsabhängigen Korrekturfaktors (gemeinsam) @f$KG@f$
	 * 
	 * Die Berechnung des komplexitätsabhängigen Korrekturfaktors (gemeinsam) 
	 * beruht auf Werten der komplexitätsabhängigen Korrekturfaktoren (gemeinsam).@n
	 * Eingangsparameter:
	 * - Komplexität
	 * 
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeCorrectionFactorShared(ExpenseSubComponent fctInput) {
        int iComplexity = COMPLEXITY_VALUES.indexOf(fctInput.sComplexity);
        fctInput.dKG = arrCorrectionFactorShared[iComplexity];
    }

    /**
	 * @brief Berechnung des komplexitätsabhängigen Korrekturfaktors (variabel) @f$KV@f$
	 * 
	 * Die Berechnung des komplexitätsabhängigen Korrekturfaktors (variabel) 
	 * beruht auf Werten der komplexitätsabhängigen Korrekturfaktoren (variabel).@n
	 * Eingangsparameter:
	 * - Komplexität
	 * 
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeCorrectionFactorVariable(ExpenseSubComponent fctInput) {
        int iComplexity = COMPLEXITY_VALUES.indexOf(fctInput.sComplexity);
        fctInput.dKV = arrCorrectionFactorVariable[iComplexity];
    }

    /**
	 * @brief Berechnung des Anteils gemeinsamer Signale @f$G@f$
	 * 
	 * Die Berechnung des Anteils gemeinsamer Signale beruht auf: 
	 * - Anzahl der Eingangssignale
	 * - Anzahl der Ausgangssignale
	 * - Anzahl der variablen Signale
	 @f{eqnarray*}
	 	G &=& \frac{B_G}{C} \\
	   	  &=& \frac{\#_{in}+\#_{out}-\#_{var}}{\#_{in}+\#_{out}}
	 @f}
	 *
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeGFactor(ExpenseSubComponent fctInput) {
        double dBG = fctInput.iPortsIn + fctInput.iPortsOut - fctInput.iPortsVar;
        double dC = fctInput.iPortsIn + fctInput.iPortsOut;
        fctInput.dG = dBG / dC;
    }

    /**
	 * @brief Berechnung des Anteils variabler Signale @f$V@f$
	 * 
	 * Die Berechnung des Anteils variabler Signale beruht auf: 
	 * - Anzahl der Eingangssignale
	 * - Anzahl der Ausgangssignale
	 * - Anzahl der variablen Signale
	 * 
	 @f{eqnarray*}
	 	G &=& \frac{B_V}{C} \\
	   	  &=& \frac{\#_{var}}{\#_{in}+\#_{out}}
	 @f}
	 *
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeVFactor(ExpenseSubComponent fctInput) {
        double dBV = fctInput.iPortsVar;
        double dC = fctInput.iPortsIn + fctInput.iPortsOut;
        fctInput.dV = dBV / dC;
    }

    /**
	 * @brief Berechnung der unjustierten PFP für horizontal gemeinsame Funktionen @f$unjustierte~PFP_{EGW/EGH}@f$
	 * 
	 * Die Berechnung der unjustierten PFP für horizontal gemeinsame Funktionen beruht auf: 
	 * - komplexitätsabhängiger Korrekturfaktors (gemeinsam) @f$KG@f$
	 * - Umrechnungsfaktor (gemeinsam) @f$UF_{gem}@f$
	 * - Anzahl der Hooks
	 * - Produktanzahl
	 * 
	 @f{eqnarray*}
	 	unjustierte~PFP_{EGW/EGH} &=& \frac{KG\cdot UF_{gem}}{\#_{Hooks}\cdot\#_{Produkte}}
	 @f}
	 * 
	 * Die Anzahl der Hooks wird im Nenner berücksichtigt, da die Implementierungshäufigkeit
	 * abhängig von der Produktanzahl angegeben wird. 
	 *
	 * @param fctInput Daten der zu berechnenden Funktion
	 * @param iProductCount Produktanzahl
	 */
    private static void computeUPFPhEG(ExpenseSubComponent fctInput, int iProductCount) {
        double dDenominator = fctInput.iHooks * iProductCount;
        fctInput.dUPFPhorEG = (fctInput.dKG * ((double) fctInput.iUFgem)) / dDenominator;
    }

    /**
	 * @brief Berechnung der unjustierten PFP für horizontal variable Funktionen @f$unjustierte~PFP_{EVW/EVH}@f$
	 * 
	 * Die Berechnung der unjustierten PFP für horizontal variable Funktionen beruht auf: 
	 * - komplexitätsabhängiger Korrekturfaktors (variabel) @f$KV@f$
	 * - Umrechnungsfaktor (variabel) @f$UF_{var}@f$
	 * - Anzahl der Hooks
	 * - Produktanzahl
	 * 
	 @f{eqnarray*}
	 	unjustierte~PFP_{EVW/EVH} &=& \frac{KV\cdot UF_{var}}{\#_{Hooks}\cdot\#_{Produkte}}
	 @f}
	 *
	 * Die Anzahl der Hooks wird im Nenner berücksichtigt, da die Implementierungshäufigkeit
	 * abhängig von der Produktanzahl angegeben wird. 
	 *
	 * @param fctInput Daten der zu berechnenden Funktion
	 * @param iProductCount Produktanzahl
	 */
    private static void computeUPFPhEV(ExpenseSubComponent fctInput, int iProductCount) {
        double dDenominator = fctInput.iHooks * fctInput.dImplementations * iProductCount;
        fctInput.dUPFPhorEV = (fctInput.dKV * ((double) fctInput.iUFvar)) / dDenominator;
    }

    /**
	 * @brief Berechnung der unjustierten PFP für vertikale Funktionen @f$unjustierte~PFP_{vert}@f$
	 * 
	 * Die Berechnung der unjustierten PFP für vertikale Funktionen beruht auf: 
	 * - Anteil variabler Signale @f$V@f$
	 * - unjustierte PFP für horizontal gemeinsame Funktionen @f$unjustierte~PFP_{EGW/EGH}@f$
	 * - unjustierte PFP für horizontal variable Funktionen @f$unjustierte~PFP_{EVW/EVH}@f$
	 * 
	 @f{eqnarray*}
	 	unjustierte~PFP_{vert} &=& V\cdot unjustierte~PFP_{EVW/EVH} + (1-V)\cdot unjustierte~PFP_{EGW/EGH}
	 @f}
	 *
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeUPFPver(ExpenseSubComponent fctInput) {
        fctInput.dUPFPver = (fctInput.dV * fctInput.dUPFPhorEV) + ((1 - fctInput.dV) * fctInput.dUPFPhorEG);
    }

    /**
	 * @brief Berechnung der unjustierten PFP für die Funktion @f$unjustierte~PFP@f$
	 * 
	 * Die Berechnung der unjustierten PFP für die Funktion beruht auf: 
	 * - unjustierte PFP für horizontal gemeinsame Funktionen @f$unjustierte~PFP_{EGW/EGH}@f$
	 * - unjustierte PFP für horizontal variable Funktionen @f$unjustierte~PFP_{EVW/EVH}@f$
	 * - unjustierte PFP für vertikale Funktionen @f$unjustierte~PFP_{vert}@f$
	 * 
	 @verbatim
if (not var) {
  if (Variability == "EG") {
    UPFPhEG
  } else {
    UPFPhEV
  }
} else {
  UPFPv
}
	 @endverbatim
	 *
	 * @param fctInput Daten der zu berechnenden Funktion
	 */
    private static void computeUPFP(ExpenseSubComponent fctInput) {
        if (fctInput.iPortsVar == 0) {
            fctInput.dUPFP = (fctInput.sVariability.equalsIgnoreCase(VARIABILITY_VALUES[VARIABILITY_EG])) ? fctInput.dUPFPhorEG : fctInput.dUPFPhorEV;
        } else {
            fctInput.dUPFP = fctInput.dUPFPver;
        }
    }

    /**
	 * @brief Berechnung der unjustierten Gesamt-PFP @f$unjustierte~PFP_{gesamt}@f$
	 * 
	 * Die Berechnung der unjustierten Gesamt-PFP für die Funktion beruht auf: 
	 * - unjustierte PFP für die Funktion @f$unjustierte~PFP@f$
	 * - Produktanzahl
	 * 
	 @f{eqnarray*}
	 	unjustierte~PFP_{gesamt} &=& unjustierte~PFP\cdot \#_{Produkte}
	 @f}
	 *
	 * @param fctInput Daten der zu berechnenden Funktion
	 * @param iProductCount Produktanzahl
	 */
    private static void computeUPFPPA(ExpenseSubComponent fctInput, int iProductCount) {
        fctInput.dUPFPPA = fctInput.dUPFP * iProductCount;
    }
}
