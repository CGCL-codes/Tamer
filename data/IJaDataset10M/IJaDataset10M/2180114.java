package web.examples;

import org.jreform.InputDataType;

/**
 * This class is an example of a custom InputDataType.
 * 
 * It converts a string value into an instance of EmploymentStatus enum.
 */
public class EmploymentStatusDataType implements InputDataType<EmploymentStatus> {

    public Class<EmploymentStatus> getInputDataClass() {
        return EmploymentStatus.class;
    }

    public EmploymentStatus parseValue(String value) {
        try {
            return EmploymentStatus.getByID(Integer.parseInt(value));
        } catch (Exception ex) {
            return null;
        }
    }
}
