package org.posper.dngtester;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.gogo.commands.Argument;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.posper.datautils.Formats;

@Command(scope = "dngtester:format", name = "int", description = "handles formating for integer")
public class FormatINTCommand extends OsgiCommandSupport {

    @Argument(name = "operation", description = "type of operation to perform, format or parse", required = true)
    String operationtype;

    @Argument(name = "value", description = "value to operate on.  If you're doing a format, please specify the number with no separators or other non-numeric characters (eg, use 48332 not 48,332)", index = 1)
    String value;

    @Override
    protected Object doExecute() throws Exception {
        if (operationtype.equals("format")) {
            Integer intToFormat = new Integer(value);
            return Formats.INT.formatValue(intToFormat);
        } else if (operationtype.equals("parse")) {
            try {
                return Formats.INT.parseValue(value);
            } catch (Exception e) {
                System.out.println("Exception while trying to parse '" + value + "' : " + e);
                return null;
            }
        } else {
            System.out.println("Unknown operation type.");
            return null;
        }
    }
}
