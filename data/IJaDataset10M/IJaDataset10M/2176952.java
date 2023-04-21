package emtigi.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author Thomas Kamps
 *
 */
public class ManaCost {

    private Vector<HashSet<ManaColor>> symbols;

    /**
	 * Creates a ManaCost object for Manacosts
	 * @param cost The String that represents tha Manacosts.
	 * The common format is used.
	 */
    public ManaCost(String cost) {
        symbols = new Vector<HashSet<ManaColor>>();
        parseCosts(cost);
    }

    public ManaCost(Vector<HashSet<ManaColor>> symbols) {
        this.symbols = symbols;
    }

    private void parseCosts(String cost) {
        Vector<String> tmpSymbols = new Vector<String>();
        int parsePos = 0;
        while (parsePos < cost.length()) {
            if (cost.charAt(parsePos) == '{') {
                String colors = "";
                parsePos++;
                while (cost.charAt(parsePos) != '}') {
                    colors = colors.concat(String.valueOf(cost.charAt(parsePos)));
                    parsePos++;
                }
                tmpSymbols.add(colors);
            } else if ("0123456789".indexOf(cost.charAt(parsePos)) != -1) {
                String num = String.valueOf(cost.charAt(parsePos));
                parsePos++;
                while ((parsePos < cost.length()) && ("0123456789".indexOf(cost.charAt(parsePos)) != -1)) {
                    num = num.concat(String.valueOf(cost.charAt(parsePos)));
                    parsePos++;
                }
                parsePos--;
                tmpSymbols.add(num);
            } else {
                tmpSymbols.add(String.valueOf(cost.charAt(parsePos)));
            }
            parsePos++;
        }
        Vector<String> sortedSymbols = new Vector<String>();
        for (String s : tmpSymbols) {
            String sortedSymbol = "";
            for (ManaColor c : ManaColor.values()) if (s.indexOf(ManaColor.getChar(c)) != -1) sortedSymbol = sortedSymbol + ManaColor.getChar(c);
            if (sortedSymbol.equals("")) {
                int n = -1;
                try {
                    n = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                }
                if (n != -1) sortedSymbols.add(s);
            } else {
                sortedSymbols.add(sortedSymbol);
            }
        }
        for (String s : sortedSymbols) {
            int n = -1;
            try {
                n = Integer.parseInt(s);
            } catch (NumberFormatException e) {
            }
            if (n != -1) {
                for (int i = 0; i < n; i++) {
                    symbols.add(randomColorSymbol());
                }
            } else {
                HashSet<ManaColor> colors = new HashSet<ManaColor>();
                for (char c : s.toCharArray()) {
                    ManaColor col = ManaColor.getFromChar(c);
                    if (col != null) colors.add(col);
                }
                symbols.add(colors);
            }
        }
    }

    private HashSet<ManaColor> randomColorSymbol() {
        HashSet<ManaColor> ret = new HashSet<ManaColor>();
        for (ManaColor c : ManaColor.values()) ret.add(c);
        return ret;
    }

    public Vector<HashSet<ManaColor>> getSingleCosts() {
        return new Vector<HashSet<ManaColor>>(symbols);
    }

    /**
	 * Checks if the ManaCost can be payed with the given Manapool
	 * @param p The manaPool to check for
	 * @return true, if the ManaCost can be payed, false otherwise
	 */
    public boolean canBePayed(Manapool p) {
        HashMap<ManaColor, Integer> manas = new HashMap<ManaColor, Integer>();
        for (ManaColor c : ManaColor.values()) {
            int m = p.getNumberOfMana(c);
            if (m >= 0) manas.put(c, m);
        }
        Vector<HashSet<ManaColor>> singleSymbols = new Vector<HashSet<ManaColor>>(symbols);
        while (!singleSymbols.isEmpty()) {
            HashSet<ManaColor> h = null;
            ManaColor payColor = null;
            int minColors = 99;
            for (HashSet<ManaColor> s : singleSymbols) {
                int payableColors = 0;
                ManaColor curPayColor = null;
                for (ManaColor c : s) {
                    if (manas.get(c) > 0) {
                        curPayColor = c;
                        payableColors++;
                    }
                }
                minColors = Math.min(minColors, payableColors);
                if (payableColors == 1) {
                    h = s;
                    payColor = curPayColor;
                }
            }
            if (minColors == 0) return false;
            if (minColors > 1) return true;
            if (h != null) {
                manas.put(payColor, manas.get(payColor) - 1);
                singleSymbols.remove(h);
            }
        }
        return true;
    }

    /**
	 * Pays the manacost as far as it is possbile with only one way.
	 * The Rest costs are returned
	 * @param p The Manapool to pay with
	 * @return The restCosts. The User must decide how to pay it.
	 */
    public Vector<HashSet<ManaColor>> payManaCost(Manapool p) {
        Vector<HashSet<ManaColor>> singleSymbols = new Vector<HashSet<ManaColor>>(symbols);
        while (!singleSymbols.isEmpty()) {
            HashSet<ManaColor> h = null;
            ManaColor payColor = null;
            int minColors = 99;
            for (HashSet<ManaColor> s : singleSymbols) {
                int payableColors = 0;
                ManaColor curPayColor = null;
                for (ManaColor c : s) {
                    if (p.getNumberOfMana(c) > 0) {
                        curPayColor = c;
                        payableColors++;
                    }
                }
                minColors = Math.min(minColors, payableColors);
                if (payableColors == 1) {
                    h = s;
                    payColor = curPayColor;
                }
            }
            if (minColors > 1) return singleSymbols;
            if (h != null) {
                p.removeMana(payColor, 1);
                singleSymbols.remove(h);
            }
        }
        return singleSymbols;
    }

    public String toString() {
        String ret = "";
        for (HashSet<ManaColor> s : symbols) {
            ret = ret + "{";
            for (ManaColor c : s) ret = ret + ManaColor.getChar(c);
            ret = ret + "}";
        }
        return ret;
    }
}
