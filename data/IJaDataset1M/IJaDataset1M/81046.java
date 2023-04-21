package joj.web.controller;

import java.util.regex.Pattern;
import joj.base.InternalMessages;

/**
 * <p>
 * Utility methods for working with controllers, providing services
 * such as name translation and generation, view resolution, who
 * knows what else, whatever I decide belongs here.
 * </p>
 * @author Jason Miller (heinousjay@gmail.com)
 *
 */
public class ControllerUtils {

    private static final InternalMessages messages = InternalMessages.getInstance(ControllerUtils.class);

    private static final String CONTROLLER = messages.getString("ControllerUtils.Controller");

    private static final String DASH = "-";

    private static final String CONTROLLER_ENDING = DASH + CONTROLLER.toLowerCase();

    private static final String SLASH = "/";

    private static final String DOT = ".";

    /**
	 * Matches on the boundary between letters immediately before any uppercase letters, as
	 * defined by {@link Character#isUpperCase(char)}
	 */
    private static final Pattern PRIOR_TO_UPPERCASE_LETTERS = Pattern.compile("(?=\\p{javaUpperCase})");

    private static final Pattern DASHES = Pattern.compile(DASH);

    private static final Pattern SLASHES = Pattern.compile(SLASH);

    private static final Pattern DOTS = Pattern.compile("\\" + DOT);

    /**
	 * <p>
	 * Takes a {@link String} in the form 'PartAnotherLast' or the form
	 * 'partAnotherLast' and returns a {@link String} in the form
	 * 'part-another-last'.
	 * </p>
	 * @param input
	 * @return
	 */
    public static String deCamelCaseAndDash(final String input) {
        final String[] parts = PRIOR_TO_UPPERCASE_LETTERS.split(input);
        final StringBuilder output = new StringBuilder();
        for (final String part : parts) {
            if (part.length() > 0) {
                output.append(part.substring(0, 1).toLowerCase()).append(part.substring(1)).append('-');
            }
        }
        output.deleteCharAt(output.length() - 1);
        return output.toString();
    }

    /**
	 * <p>
	 * Takes a {@link String} in the form 'part-another-last' and returns a
	 * {@link String} in the form 'PartAnotherLast'
	 * </p>
	 *
	 * @param input
	 * @return
	 */
    public static String deDashAndCamelCaseClassStyle(final String input) {
        final String[] parts = DASHES.split(input);
        final StringBuilder output = new StringBuilder();
        for (final String part : parts) {
            output.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
        }
        return output.toString();
    }

    /**
	 * <p>
	 * Takes a {@link String} in the form 'part-another-last' and returns a
	 * {@link String} in the form 'partAnotherLast'
	 * </p>
	 *
	 * @param input
	 * @return
	 */
    public static String deDashAndCamelCaseMethodStyle(final String input) {
        final StringBuilder output = new StringBuilder(deDashAndCamelCaseClassStyle(input));
        output.replace(0, 1, output.substring(0, 1).toLowerCase());
        return output.toString();
    }

    /**
	 * <p>
	 * Takes a controller name, such as one parsed from a URL, and generates candidate class
	 * names based on the following rules:
	 * <ul>
	 *   <li>all names separated with the '/' marker (path separation) are treated as
	 *       package names, aside from the last one (or only one if no path separation is present)</li>
	 *   <li>the last path separated fragment is treated as the class name</li>
	 *   <li>dashes are treated as markers for a split to convert to a camel case name</li>
	 *   <li>package segments are camel cased with a lower case first initial</li>
	 *   <li>the controller class segment is camel cased with an upper case first initial</li>
	 *   <li>the word "Controller" (or the localized equivalent) is appended to one candidate class name</li>
	 *   <li>another candidate is generated without "Controller" (or the localized equivalent) appended.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Examples:<br>
	 * <table>
	 *   <tr>
	 *     <th>Input</th>
	 *     <th>Output</th>
	 *   </tr>
	 *   <tr>
	 *     <td>some/path-to/some-class</td>
	 *     <td>
	 *       <ol>
	 *         <li>some.pathTo.SomeClassController</li>
	 *         <li>some.pathTo.SomeClass</li>
	 *       <ol>
	 *     </td>
	 *   </tr>
	 *   <tr>
	 *     <td>some-class</td>
	 *     <td>
	 *       <ol>
	 *         <li>SomeClassController</li>
	 *         <li>SomeClass</li>
	 *       <ol>
	 *     </td>
	 *   </tr>
	 * </table>
	 * @param controllerName A controller name in the form of the input from the examples above
	 * @return A String array containing candidate names in form of the output from the examples above
	 */
    public static String[] generateControllerClassCandidateNames(final String controllerName) {
        final String[] pathFragments = SLASHES.split(controllerName);
        final StringBuilder classNameBuffer = new StringBuilder();
        for (int i = 0; i < pathFragments.length - 1; ++i) {
            classNameBuffer.append(deDashAndCamelCaseMethodStyle(pathFragments[i])).append(DOT);
        }
        final String classNameCandidateOne = classNameBuffer.append(deDashAndCamelCaseClassStyle(pathFragments[pathFragments.length - 1])).toString();
        final String classNameCandidateTwo = classNameBuffer.append(CONTROLLER).toString();
        return new String[] { classNameCandidateTwo, classNameCandidateOne };
    }

    /**
	 * <p>
	 * Generates a controller name for a given class name, based on the following rules:
	 * <ul>
	 *   <li>all dot separated path fragments are passed through {@link #deCamelCaseAndDash(String)}</li>
	 *   <li>all dots are turned into slashes</li>
	 *   <li>if present, "-controller" (or the localized equivalent) is removed</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Examples:<br>
	 * <table>
	 *   <tr>
	 *     <th>Input</th>
	 *     <th>Output</th>
	 *   </tr>
	 *   <tr>
	 *     <td>some.pathTo.SomeClassController</td>
	 *     <td>some/path-to/some-class</td>
	 *   </tr>
	 *   <tr>
	 *     <td>SomeClass</td>
	 *     <td>some-class</td>
	 *   </tr>
	 * </table>
	 * @param controllerClassName A controller class name in the form of the input from the examples above
	 * @return A controller name in the form of the output from the examples above
	 */
    public static String generateControllerName(final String controllerClassName) {
        final String[] pathFragments = DOTS.split(controllerClassName);
        final StringBuilder outputBuffer = new StringBuilder();
        for (final String pathFragment : pathFragments) {
            outputBuffer.append(deCamelCaseAndDash(pathFragment)).append(SLASH);
        }
        outputBuffer.deleteCharAt(outputBuffer.length() - 1);
        String output = outputBuffer.toString();
        if (output.endsWith(CONTROLLER_ENDING)) {
            output = output.substring(0, output.length() - CONTROLLER_ENDING.length());
        }
        return output;
    }
}
