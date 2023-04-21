package jade.domain.FIPAAgentManagement;

import jade.content.Predicate;

/** 
* 
* @see jade.domain.FIPAAgentManagement.FIPAManagementOntology
* @author Fabio Bellifemine - CSELT S.p.A.
* @version $Date: 2003-03-10 16:38:45 +0100 (lun, 10 mar 2003) $ $Revision: 3775 $
*/
public class UnrecognisedParameterValue extends RefuseException implements Predicate {

    public UnrecognisedParameterValue() {
        this("unknown-parameter-name", "unknown-parameter-value");
    }

    public UnrecognisedParameterValue(String parameterName, String parameterValue) {
        super("(unrecognised-parameter-value " + parameterName + " \"" + parameterValue + "\")");
        s1 = parameterName;
        s2 = parameterValue;
    }

    /**
  @serial
  */
    String s1, s2;

    public void setParameterName(String a) {
        s1 = a;
        setMessage("(unrecognised-parameter-value " + s1 + " \"" + s2 + "\")");
    }

    public String getParameterName() {
        return s1;
    }

    public void setParameterValue(String a) {
        s2 = a;
        setMessage("(unrecognised-parameter-value " + s1 + " \"" + s2 + "\")");
    }

    public String getParameterValue() {
        return s2;
    }
}
