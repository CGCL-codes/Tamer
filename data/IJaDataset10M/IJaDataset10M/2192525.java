package gov.nasa.gsfc.visbard.repository.resource;

import gov.nasa.gsfc.visbard.util.Range;
import gov.nasa.gsfc.visbard.util.VisbardException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import org.gjt.fredgc.unit.Unit;
import org.gjt.fredgc.unit.ParseException;

public class ColumnFactory {

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(ColumnFactory.class.getName());

    private static ColumnFactory sInstance = new ColumnFactory();

    public static ColumnFactory getInstance() {
        return sInstance;
    }

    public Column createCombinedColumnTime(double delta, Range timespan) throws VisbardException {
        return new ColumnCombinedTime(delta, timespan);
    }

    public Column createCombinedColumn(Resource rsrc, String colname) throws VisbardException {
        ResourceReader rdr = rsrc.getReader();
        Column[] cols = rdr.getAllAvaliableColumns();
        for (int i = 0; i < cols.length; i++) {
            if (cols[i].getName().equals(colname)) {
                return new ColumnCombined(cols[i], rdr);
            }
        }
        throw new VisbardException("Requested column not found in resource");
    }

    private Column createCDFColumn(String name, HashMap parameters, String filename, boolean isVirtual) throws VisbardException {
        String size = (String) parameters.get("size");
        String type = (String) parameters.get("type");
        String source = (String) parameters.get("source");
        String unit = (String) parameters.get("unit");
        Unit u = null;
        try {
            if (unit == null) throw new VisbardException("Unable to find " + "units in RDF.\nQuantity: " + name);
            u = Unit.newUnit(unit);
        } catch (ParseException e) {
            sLogger.error("Unable to recognize unit \"" + unit + "\" of \"" + name + "\" in RDF describing \"" + filename + "\".\n  See the following " + "for valid units:\n    http://www.gjt.org/pkgdoc/org/gjt/fredgc/unit/" + "\nOr ask the author to add nonstandard ones to the UnitManager.\n" + e.getMessage() + "\n");
            u = Unit.pure;
        }
        boolean isScalar = type.equals("scalar");
        boolean isDouble = size.equals("double");
        return new ColumnCdf(name, isScalar, isDouble, u, source, isVirtual);
    }

    private Column createASCIIColumn(String name, HashMap parameters, boolean isVirtual) throws VisbardException {
        String type = (String) parameters.get("type");
        if (type.equals("TimeYDH")) {
            String colYear = (String) parameters.get("colYear");
            String colDay = (String) parameters.get("colDay");
            String colHour = (String) parameters.get("colHour");
            return new ColumnASCIITime(Integer.parseInt(colYear) - 1, Integer.parseInt(colDay) - 1, Integer.parseInt(colHour) - 1);
        }
        String size = (String) parameters.get("size");
        String unit = (String) parameters.get("unit");
        Unit u;
        try {
            if (unit == null) throw new VisbardException("Unable to find " + "units in RDF.\nQuantity: " + name);
            u = Unit.newUnit(unit);
        } catch (ParseException e) {
            sLogger.error("Unable to recognize units of \"" + name + "\".  Defaulting to \"" + Unit.pure + "\"\n" + "  See the following for valid units:  \n" + "     http://www.gjt.org/pkgdoc/org/gjt/fredgc/unit/" + "\nOr ask the author to add nonstandard ones to the UnitManager.\n" + e.getMessage() + "\n");
            u = Unit.pure;
        }
        ;
        ArrayList missingDataStrings = new ArrayList();
        for (Iterator i = parameters.keySet().iterator(); i.hasNext(); ) {
            String str = (String) i.next();
            if (str.startsWith("missingDataString")) missingDataStrings.add(parameters.get(str));
        }
        String[] missingData = new String[missingDataStrings.size()];
        missingDataStrings.toArray(missingData);
        boolean isScalar = type.equals("scalar");
        boolean isDouble = size.equals("double");
        if (isScalar) {
            String col = (String) parameters.get("col");
            if (col != null) {
                return new ColumnASCII(name, Integer.parseInt(col) - 1, isDouble, u, missingData, isVirtual);
            } else {
                throw new VisbardException("Missing column index");
            }
        } else {
            String colX = (String) parameters.get("colX");
            String colY = (String) parameters.get("colY");
            String colZ = (String) parameters.get("colZ");
            if ((colX != null) && (colY != null) && (colZ != null)) {
                return new ColumnASCII(name, new int[] { Integer.parseInt(colX) - 1, Integer.parseInt(colY) - 1, Integer.parseInt(colZ) - 1 }, isDouble, u, missingData, isVirtual);
            } else {
                throw new VisbardException("Missing column index");
            }
        }
    }

    public Column createColumnCdf(String name, HashMap parameters, String filename, boolean isVirtual) throws VisbardException {
        return createCDFColumn(name, parameters, filename, isVirtual);
    }

    public Column createColumnASCII(String name, HashMap parameters, boolean isVirtual) throws VisbardException {
        return createASCIIColumn(name, parameters, isVirtual);
    }
}
