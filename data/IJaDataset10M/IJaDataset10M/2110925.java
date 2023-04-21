package be_interpoint;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import org.daisy.braille.table.AbstractConfigurableTableProvider;
import org.daisy.braille.table.BrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.table.Table;

/**
 *
 * @author Bert Frees
 * @author Joel Håkansson
 */
public class InterpointTableProvider extends AbstractConfigurableTableProvider<InterpointTableProvider.TableType> {

    enum TableType {

        USA1_8
    }

    ;

    private final ArrayList<Table> tables;

    public InterpointTableProvider() {
        super(EightDotFallbackMethod.values()[0], '⠀');
        tables = new ArrayList<Table>();
    }

    /**
     * Get a new table instance based on the factory's current settings.
     *
     * @param t
     *            the type of table to return, this will override the factory's
     *            default table type.
     * @return returns a new table instance.
     */
    public BrailleConverter newTable(TableType t) {
        switch(t) {
            case USA1_8:
                {
                    String table = " a,b.k;l\"cif|msp!e:h*o+r>djg`ntq'1?2-u(v$3960x~&<5/8)z={4w7#y}%";
                    StringBuffer sb = new StringBuffer();
                    sb.append(table);
                    return new EmbosserBrailleConverter(sb.toString(), Charset.forName("ISO-8859-1"), fallback, replacement, true);
                }
            default:
                throw new IllegalArgumentException("Cannot find table type " + t);
        }
    }

    public Collection<Table> list() {
        return tables;
    }
}
