package fitlibrary.specify.valueObject;

import fitlibrary.parser.lookup.ParseDelegation;
import fitlibrary.specify.eg.FixedPoint;
import fitlibrary.traverse.FitLibrarySelector;
import fitlibrary.traverse.Traverse;

public class ParseMyFixedPointAsStringWithDelegate {

    public ParseMyFixedPointAsStringWithDelegate() {
        ParseDelegation.registerParseDelegate(FixedPoint.class, FixedPointDelegate.class);
    }

    public FixedPoint aFixedPoint(FixedPoint point) {
        return point;
    }

    public Traverse aFixedPointAsDomainObject(FixedPoint point) {
        return FitLibrarySelector.selectDomainCheck(point);
    }

    public static class FixedPointDelegate {

        public static FixedPoint parse(String s) {
            if (!s.startsWith("(") || !s.endsWith(")")) throw new RuntimeException("Badly formatted point");
            int comma = s.indexOf(",");
            if (comma < 0) throw new RuntimeException("Badly formatted point");
            return new FixedPoint(Integer.parseInt(s.substring(1, comma)), Integer.parseInt(s.substring(comma + 1, s.length() - 1)));
        }
    }
}
