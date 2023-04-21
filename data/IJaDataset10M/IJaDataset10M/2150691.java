package dr.evomodelxml.speciation;

import dr.evolution.util.Units;
import dr.evomodel.speciation.BirthDeathSerialSamplingModel;
import dr.evoxml.util.XMLUnits;
import dr.inference.model.Parameter;
import dr.xml.*;
import java.util.logging.Logger;

/**
 * @author Andrew Rambaut
 * @author Tanja Stadler
 * @author Alexei Drummond
 * @author Joseph Heled
 */
public class BirthDeathEpidemiologyModelParser extends AbstractXMLObjectParser {

    public static final String BIRTH_DEATH_EPIDEMIOLOGY = "birthDeathEpidemiology";

    public static final String R0 = "R0";

    public static final String RECOVERY_RATE = "recoveryRate";

    public static final String SAMPLING_PROBABILITY = "samplingProbability";

    public static final String ORIGIN = BirthDeathSerialSamplingModelParser.ORIGIN;

    public String getParserName() {
        return BIRTH_DEATH_EPIDEMIOLOGY;
    }

    public Object parseXMLObject(XMLObject xo) throws XMLParseException {
        final String modelName = xo.getId();
        final Units.Type units = XMLUnits.Utils.getUnitsAttr(xo);
        final Parameter R0Parameter = (Parameter) xo.getElementFirstChild(R0);
        final Parameter recoveryRateParameter = (Parameter) xo.getElementFirstChild(RECOVERY_RATE);
        final Parameter samplingProbabiltyParameter = (Parameter) xo.getElementFirstChild(SAMPLING_PROBABILITY);
        Parameter origin = null;
        if (xo.hasChildNamed(ORIGIN)) {
            origin = (Parameter) xo.getElementFirstChild(ORIGIN);
        }
        Logger.getLogger("dr.evomodel").info("Using epidemiological parameterization of " + getCitationRT());
        return new BirthDeathSerialSamplingModel(modelName, R0Parameter, recoveryRateParameter, samplingProbabiltyParameter, origin, units);
    }

    public static String getCitationPsiOrg() {
        return "Stadler T (2010) J Theor Biol 267, 396-404 [Birth-Death with Serial Samples].";
    }

    public static String getCitationRT() {
        return "Stadler et al (2011) : Estimating the basic reproductive number from viral sequence data, " + "Mol.Biol.Evol., doi: 10.1093/molbev/msr217, 2011";
    }

    public String getParserDescription() {
        return "Stadler et al (2011) model of epidemiology.";
    }

    public Class getReturnType() {
        return BirthDeathSerialSamplingModel.class;
    }

    public XMLSyntaxRule[] getSyntaxRules() {
        return rules;
    }

    private final XMLSyntaxRule[] rules = { new ElementRule(ORIGIN, Parameter.class, "The origin of the infection, x0 > tree.rootHeight", true), new ElementRule(R0, new XMLSyntaxRule[] { new ElementRule(Parameter.class) }), new ElementRule(RECOVERY_RATE, new XMLSyntaxRule[] { new ElementRule(Parameter.class) }), new ElementRule(SAMPLING_PROBABILITY, new XMLSyntaxRule[] { new ElementRule(Parameter.class) }), XMLUnits.SYNTAX_RULES[0] };
}
