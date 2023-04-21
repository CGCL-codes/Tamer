package de.jcommandlineparser.options;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * 
 * @author Alexander Kerner
 *
 */
public class FileMultiOption extends AbstractMultiOption<File> {

    protected FileMultiOption(Class<File> returnType, char identifierShort, String identifierLong, String description, boolean required) {
        super(returnType, identifierShort, identifierLong, description, required);
    }

    protected FileMultiOption(Class<File> returnType, char identifierShort, String identifierLong, String description, boolean required, Set<AbstractOption<?>> clashOptions) {
        super(returnType, identifierShort, identifierLong, description, required, clashOptions);
    }

    @Override
    public void parse(String string) throws NoValueGivenException {
        if (string == null || string.length() == 0) throw new NoValueGivenException("No value given for option " + this);
        final String[] s = string.split(DELIM_PATTERN);
        ArrayList<File> l = new ArrayList<File>();
        for (String ss : s) {
            l.add(new File(ss));
        }
        setValues(l);
    }
}
