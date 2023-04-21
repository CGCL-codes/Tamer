package jaga.external.bucanon;

import java.util.LinkedList;
import java.util.ListIterator;

/** Modified Miguel Garvie 22/05/2004 for use with JaGa */
public class QuineMcCluskey {

    public static final boolean DNF = false;

    public static final boolean CNF = true;

    public static final boolean HORIZONTAL = false;

    public static final boolean VERTICAL = true;

    TheoForm formula;

    boolean formType;

    boolean displayType;

    AtomForm[] atoms;

    public TheoForm pnf;

    String picture;

    private LinkedList columns = new LinkedList();

    private FormulaDisplay fd = new FormulaDisplay();

    private static AtomFormComparator comp = new AtomFormComparator();

    public QuineMcCluskey(TheoForm formula, boolean formType, boolean displayType) {
        this.formula = formula;
        this.formType = formType;
        this.displayType = displayType;
        AtomFormSet s = Eval.atoms(formula);
        atoms = s.atomFormArray();
        if (formType) {
            generateColumns(cnfColumn(formula));
        } else {
            generateColumns(dnfColumn(formula));
        }
        if (formType) {
            pnf = pcnf(primeValuations());
        } else {
            pnf = pdnf(primeValuations());
        }
        if (displayType) {
            picture = verticalDisplay();
        } else {
            picture = horizontalDisplay();
        }
    }

    String horizontalDisplay() {
        StringBuffer sb = new StringBuffer();
        sb.append("<table> \n <tr> \n");
        ListIterator i = columns.listIterator();
        while (i.hasNext()) {
            sb.append("<td> \n");
            sb.append(((Column) (i.next())).asHTMLTable());
            sb.append("</td> \n");
        }
        sb.append("</tr> \n </table> \n");
        return sb.toString();
    }

    String verticalDisplay() {
        StringBuffer sb = new StringBuffer();
        ListIterator i = columns.listIterator();
        while (i.hasNext()) {
            sb.append("<p> \n");
            sb.append(((Column) (i.next())).asHTMLTable());
            sb.append("</p> \n");
        }
        return sb.toString();
    }

    class Cell {

        int index;

        Valuation valuation;

        LinkedList pred;

        LinkedList succ;

        Cell(Valuation valuation, int index) {
            this.index = index;
            this.valuation = valuation;
            pred = new LinkedList();
            succ = new LinkedList();
        }

        Cell(Valuation valuation) {
            this.valuation = valuation;
            pred = new LinkedList();
            succ = new LinkedList();
        }
    }

    class Column extends LinkedList {

        Column() {
            super();
        }

        String asHTMLTable() {
            StringBuffer sb = new StringBuffer();
            sb.append("<table border=1> \n");
            sb.append(" <tr> <th> pred </th> <th> index </th> " + "<th> valuation </th> <th> succ </th> </tr> \n");
            ListIterator i = listIterator();
            while (i.hasNext()) {
                Cell cell = (Cell) (i.next());
                sb.append(" <tr> <td> ");
                ListIterator j = cell.pred.listIterator();
                while (j.hasNext()) {
                    sb.append(((Integer) (j.next())).intValue());
                    if (j.hasNext()) {
                        sb.append(" ");
                    }
                }
                sb.append(" </td> <td> " + cell.index + " </td> <td> ");
                if (formType) {
                    sb.append(fd.line(cell.valuation.nld()));
                } else {
                    sb.append(fd.line(cell.valuation.nlc()));
                }
                sb.append(" </td> <td> ");
                ListIterator k = cell.succ.listIterator();
                while (k.hasNext()) {
                    sb.append(((Integer) (k.next())).intValue());
                    if (k.hasNext()) {
                        sb.append(" ");
                    }
                }
                sb.append(" </td> </tr>\n");
            }
            sb.append("</table> \n");
            return sb.toString();
        }

        void addSucc(int cellIndex, int succ) {
            ListIterator i = listIterator();
            Cell cell;
            while (i.hasNext()) {
                cell = (Cell) (i.next());
                if (cell.index == cellIndex) {
                    cell.succ.addLast(new Integer(succ));
                    break;
                }
            }
        }
    }

    Valuation nextVal(Valuation v) {
        boolean[] values = new boolean[v.atoms.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = v.values[i];
        }
        Valuation v1 = new Valuation(v.atoms, values);
        v1.increase();
        return v1;
    }

    Column cnfColumn(TheoForm f) {
        int cellIndex = 1;
        int exp = 1;
        for (int i = 0; i < atoms.length; i++) {
            exp = exp * 2;
        }
        Valuation v = new Valuation(new AtomFormSet(atoms));
        Column col = new Column();
        for (int i = 0; i < exp; i++) {
            if ((Eval.eval(Eval.val(f, invert(v)))).isZeroBit()) {
                col.addLast(new Cell(v, cellIndex));
                cellIndex++;
            }
            v = nextVal(v);
        }
        return col;
    }

    Column dnfColumn(TheoForm f) {
        int cellIndex = 1;
        int exp = 1;
        for (int i = 0; i < atoms.length; i++) {
            exp = exp * 2;
        }
        Valuation v = new Valuation(new AtomFormSet(atoms));
        Column col = new Column();
        for (int i = 0; i < exp; i++) {
            if ((Eval.eval(Eval.val(f, v))).isUnitBit()) {
                col.addLast(new Cell(v, cellIndex));
                cellIndex++;
            }
            v = nextVal(v);
        }
        return col;
    }

    Column nextColumn(Column col) {
        Column nextCol = new Column();
        int n = col.size();
        Valuation v;
        Valuation v1;
        Valuation v2;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                v1 = ((Cell) (col.get(i))).valuation;
                v2 = ((Cell) (col.get(j))).valuation;
                v = resolution(v1, v2);
                if (v != null) {
                    Cell cell = new Cell(v);
                    cell.pred.addLast(new Integer(((Cell) (col.get(i))).index));
                    cell.pred.addLast(new Integer(((Cell) (col.get(j))).index));
                    nextCol.addLast(cell);
                }
            }
        }
        return nextCol;
    }

    Column insert(Column col, Cell cell) {
        Column col0 = new Column();
        while (!col.isEmpty()) {
            Cell cell1 = (Cell) (col.removeFirst());
            int n = cell.valuation.compareTo(cell1.valuation);
            if (n < 0) {
                col0.addLast(cell);
                col0.addLast(cell1);
                col0.addAll(col);
                return col0;
            } else if (n > 0) {
                col0.addLast(cell1);
            } else {
                cell.pred.addAll(cell1.pred);
                cell.succ.addAll(cell1.succ);
                col0.add(cell);
                col0.addAll(col);
                return col0;
            }
        }
        col0.addLast(cell);
        return col0;
    }

    Column sort(Column col) {
        Column sortCol = new Column();
        while (!col.isEmpty()) {
            sortCol = insert(sortCol, (Cell) (col.removeFirst()));
        }
        return sortCol;
    }

    void addIndices(Column col) {
        ListIterator i = col.listIterator();
        int j = 1;
        while (i.hasNext()) {
            ((Cell) (i.next())).index = j;
            j++;
        }
    }

    void addSucc(Column preCol, Column postCol) {
        ListIterator i = postCol.listIterator();
        while (i.hasNext()) {
            Cell cell = (Cell) (i.next());
            ListIterator j = cell.pred.listIterator();
            while (j.hasNext()) {
                preCol.addSucc(((Integer) (j.next())).intValue(), cell.index);
            }
        }
    }

    void generateColumns(Column initColumn) {
        Column preCol = initColumn;
        Column postCol = nextColumn(preCol);
        postCol = sort(postCol);
        addIndices(postCol);
        addSucc(preCol, postCol);
        columns.addLast(preCol);
        while (!postCol.isEmpty()) {
            columns.addLast(postCol);
            preCol = postCol;
            postCol = nextColumn(preCol);
            postCol = sort(postCol);
            addIndices(postCol);
            addSucc(preCol, postCol);
        }
    }

    LinkedList primeValuations() {
        Column col = new Column();
        ListIterator i = columns.listIterator();
        Cell cell;
        while (i.hasNext()) {
            ListIterator j = ((Column) (i.next())).listIterator();
            while (j.hasNext()) {
                cell = (Cell) (j.next());
                if (cell.succ.isEmpty()) {
                    col.addLast(cell);
                }
            }
        }
        col = sort(col);
        LinkedList valList = new LinkedList();
        ListIterator k = col.listIterator();
        while (k.hasNext()) {
            valList.addLast(((Cell) (k.next())).valuation);
        }
        return valList;
    }

    Conjunction nlc(Valuation v) {
        TheoFormList l = new TheoFormList();
        for (int i = 0; i < v.atoms.length; i++) {
            if (v.values[i]) {
                l.addLast(v.atoms[i]);
            } else {
                l.addLast(new Negation(v.atoms[i]));
            }
        }
        return new Conjunction(l);
    }

    Disjunction nld(Valuation v) {
        TheoFormList l = new TheoFormList();
        for (int i = 0; i < v.atoms.length; i++) {
            if (v.values[i]) {
                l.addLast(v.atoms[i]);
            } else {
                l.addLast(new Negation(v.atoms[i]));
            }
        }
        return new Disjunction(l);
    }

    Disjunction pdnf(LinkedList primeValuations) {
        TheoFormList l = new TheoFormList();
        ListIterator i = primeValuations.listIterator();
        while (i.hasNext()) {
            l.addLast(nlc((Valuation) (i.next())));
        }
        return new Disjunction(l);
    }

    Conjunction pcnf(LinkedList primeValuations) {
        TheoFormList l = new TheoFormList();
        ListIterator i = primeValuations.listIterator();
        while (i.hasNext()) {
            l.addLast(nld((Valuation) (i.next())));
        }
        return new Conjunction(l);
    }

    static Valuation resolution(Valuation v1, Valuation v2) {
        if (v1.size() == v2.size() && v1.size() > 0) {
            int newSize = v1.size() - 1;
            AtomForm[] atoms = new AtomForm[newSize];
            boolean[] bitValues = new boolean[newSize];
            int complementary = 0;
            for (int i = 0; i <= newSize; i++) {
                if (comp.compare(v1.atoms[i], v2.atoms[i]) == 0) {
                    if (v1.values[i] == v2.values[i]) {
                        if (i - complementary < newSize) {
                            atoms[i - complementary] = v1.atoms[i];
                            bitValues[i - complementary] = v1.values[i];
                        }
                    } else {
                        complementary++;
                    }
                } else {
                    return null;
                }
            }
            if (complementary == 1) {
                return new Valuation(atoms, bitValues);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    static Valuation invert(Valuation v) {
        Valuation w = new Valuation(new AtomFormSet(v.atoms));
        for (int i = 0; i < w.atoms.length; i++) {
            if (v.values[i]) {
                w.values[i] = false;
            } else {
                w.values[i] = true;
            }
        }
        return w;
    }
}
