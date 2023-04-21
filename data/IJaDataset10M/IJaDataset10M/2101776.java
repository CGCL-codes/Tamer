package gov.nist.scap.cpe.language;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import org.mitre.cpe.naming.util.CPEName;
import scap.check.Check;

public interface LogicalTest<CPE extends CPEFactReference> {

    public enum Operator {

        AND, OR
    }

    boolean isNegate();

    void setNegate(boolean b);

    Operator getOperator();

    void setOperator(Operator op);

    List<? extends LogicalTest<CPE>> getLogicalTests();

    LogicalTest<CPE> addLogicalTest();

    CPEFactReference addCPEFactReference(CPEName name) throws ParseException;

    CheckFactReference addCheckFactReference(Check check);

    Platform<CPE> getPlatform();

    List<CPE> getCPEFactReferences();

    List<? extends CheckFactReference> getCheckFactReferences();

    Set<Check> getChecks();
}
