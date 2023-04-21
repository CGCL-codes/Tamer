package fr.macymed.commons.clp;

import fr.macymed.commons.lang.StringUtilities;

/**
 * <p>
 * Parses a command-line and creates associated option.
 * </p>
 * <p>
 * The complex parser simply wait for a '-' or '--' char before each option, even for the short form. The option's arguments are separed by equal '=', space ' ', colon ',' or semi-colon ';'.
 * </p>
 * <pp>
 * Moreover, if an argument like "-Xrun" (second character is capitalized), and if an Option -"X" but not "-Xrun" exist, then an "-X" option is added to the token list, with a "run" parameter.
 * </p>
 * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
 * @version 1.0.0
 * @version Commons - CommandLineParser API 1.0
 */
public class ComplexParser extends CommandLineParser {

    /**
     * <p>
	 * Creates a new ComplexParser.
     * </p>
	 */
    public ComplexParser() {
        super();
    }

    /**
     * <p>
	 * Tokenizes the arguments. The implementation quite simple, see description for more info.
     * </p>
	 * @param _args The command-line arguments to tokenize.
	 * @param _options The option group.
	 * @throws ParserException If an exception occurs while parsing.
	 */
    @Override
    public void tokenize(String[] _args, OptionGroup _options) throws ParserException {
        for (int i = 0, n = _args.length; i < n; i++) {
            String[] arr = StringUtilities.split(_args[i], " ,;=", -1);
            for (int j = 0, m = arr.length; j < m; j++) {
                if (arr[j].startsWith("--")) {
                    arr[j] = arr[j].substring(1, _args[j].length());
                }
                if (arr[j].length() > 2 && arr[j].charAt(0) == '-' && Character.isUpperCase(arr[j].charAt(1)) && !_options.hasOption(arr[j].substring(1, arr[j].length())) && _options.hasOption(arr[j].substring(1, 2))) {
                    this.addToken(arr[j].substring(0, 2));
                    arr[j] = arr[j].substring(2, arr[j].length());
                    j--;
                } else {
                    this.addToken(arr[j]);
                }
            }
        }
    }

    /**
     * <p>
	 * If an error occurs, then we print the help for the option group.
     * </p>
	 * @param _options The option group we will print the help.
	 */
    @Override
    public void printHelp(OptionGroup _options) {
        System.out.println("Usage:");
        System.out.println("\t prog_name [-options [arguments]] file_name");
        System.out.println("where options are:");
        System.out.print(_options.formatHelp());
    }
}
