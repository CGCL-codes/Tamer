package dproxy.application.ui.commands;

import java.util.Iterator;

/**
 * Utility classes for command line validation.
 */
public final class ValidationUtils {

    /**
     * Private constructor to avoid instantiation.
     */
    private ValidationUtils() {
    }

    /**
     * Checks if this is an "> file.txt" pattern output.
     * @param itParameters Parameters iterator
     * @return file name
     * @throws ValidationException validation error
     */
    protected static String getFileOutputParameter(Iterator<String> itParameters) throws ValidationException {
        String fileName;
        if (itParameters.hasNext()) {
            String parameter = itParameters.next().trim();
            if (parameter.indexOf(">") == 0) {
                if (">".equals(parameter)) {
                    if (itParameters.hasNext()) {
                        fileName = itParameters.next().trim();
                    } else {
                        throw new ValidationException();
                    }
                } else {
                    fileName = parameter.substring(1);
                }
            } else {
                throw new ValidationException();
            }
        } else {
            throw new NoMoreParametersException();
        }
        return fileName;
    }

    /**
     * Checks if this parameter is valid and numeric.
     * @param itParameters Parameters iterator
     * @return number parameter
     * @throws ValidationException validation error
     */
    protected static int getNumberParameter(Iterator<String> itParameters) throws ValidationException {
        int numberParameter;
        if (itParameters.hasNext()) {
            try {
                numberParameter = Integer.parseInt(itParameters.next());
            } catch (NumberFormatException e) {
                throw new ValidationException();
            }
        } else {
            throw new NoMoreParametersException();
        }
        return numberParameter;
    }

    /** 
     * Checks if there are no more parameters.
     * @param itParameters Parameters iterator
     * @throws ValidationException validation error
     */
    protected static void validateNoMoreParameters(Iterator<String> itParameters) throws ValidationException {
        if (itParameters.hasNext()) {
            throw new ValidationException();
        }
    }

    /**
     * Get the next parameter
     * @param itParameters Parameters iterator
     * @return next parameter, when available
     * @throws NoMoreParametersException when no more parameters are available
     */
    public static String getStringParameter(Iterator<String> itParameters) throws NoMoreParametersException {
        String parameter;
        if (itParameters.hasNext()) {
            parameter = itParameters.next().trim();
        } else {
            throw new NoMoreParametersException();
        }
        return parameter;
    }
}
