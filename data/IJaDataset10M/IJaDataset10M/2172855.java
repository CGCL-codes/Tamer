package pxAnalyzer;

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.lang.Comparable;
import pxAnalyzer.Analyzer.Hooks;

/**
 * This class provide means to analyzes a data file and regroup all fields in order
 * to highlight the structure and possibly the data values.
 * 
 * @author Copyright 2007 Yves Pr�lot ; distributed under the terms of the GNU General Public License
 */
public class FileCollapser {

    /**
	 * Class describing the structure of a data file as seen during the analysis process.
	 * The only possible operation at this time is to print it.
	 */
    protected static class ExploredField implements Comparable<Object> {

        enum FieldType {

            BASE('B'), ANON('A'), STRUCT('S'), LIST('L'), EMPTY('E');

            char _c;

            FieldType(char c) {
                _c = c;
            }

            ;
        }

        ;

        String _name;

        LinkedList<ExploredField> _subFields;

        SortedSet<String> _values;

        Set<FieldType> _types = new TreeSet<FieldType>();

        short _pos[] = new short[16];

        int _matches;

        int _total;

        int _runningPos;

        int _occMax;

        int _occCur;

        short _posCur;

        String _lastField;

        int _thread;

        int _totalThread;

        ExploredField _owner;

        static int __limit = 2048;

        ExploredField(String name) {
            _name = name;
        }

        public int compareTo(Object f) {
            if (f instanceof ExploredField) return _name.compareTo(((ExploredField) f)._name);
            if (f instanceof String) return _name.compareTo((String) f);
            throw new ClassCastException();
        }

        /**
		 * Finds the field with that name in the sub-list of fields, or null if it is
		 * not present.
		 * @param name name of the field to identify
		 * @return found existing field, or null
		 */
        ExploredField findField(String name) {
            if (_subFields == null) {
                _subFields = new LinkedList<ExploredField>();
                return null;
            }
            for (ExploredField f : _subFields) if (f.compareTo(name) == 0) return f;
            return null;
        }

        void checkPosSize(int thread) {
            if (thread > _pos.length - 1) {
                try {
                    int l = _pos.length;
                    while (thread > l - 1) l = l + l;
                    short[] p = new short[l];
                    System.arraycopy(_pos, 0, p, 0, _pos.length);
                    _pos = p;
                } catch (OutOfMemoryError e) {
                    System.out.println("thread = " + thread);
                    throw e;
                }
            }
        }

        void setNewPos(int thread, short position) {
            checkPosSize(thread);
            _pos[thread] = position;
            _runningPos += position;
        }

        void closeOccurences(boolean currentToo) {
            if (_subFields != null) for (ExploredField f : _subFields) f.closeOccurence(true);
            closeOccurence(currentToo);
        }

        void closeOccurence(boolean reset) {
            if (_occCur > _occMax) _occMax = _occCur;
            _posCur = 0;
            if (reset) _occCur = 0;
        }

        void openOccurence() {
            if (_occCur < 0) _occCur = 0;
            _occCur++;
            _totalThread++;
            if (_thread >= __limit) return;
            _thread++;
        }

        void addValue(String v) {
            if (_values == null) _values = new TreeSet<String>();
            _values.add(v);
        }

        void checkPos(ExploredField f) {
            if (f._name.equals(_lastField)) return;
            _lastField = f._name;
            f._total++;
            if (_thread >= __limit) return;
            f.setNewPos(_thread, ++_posCur);
            f._matches++;
        }

        ExploredField addField(String name) {
            ExploredField f = new ExploredField(name);
            _subFields.addLast(f);
            f._owner = this;
            return f;
        }

        boolean substractPos(ExploredField orig, ExploredField f) {
            int n = 0, m = 0;
            for (int i = 0; i < orig._pos.length && i < f._pos.length && m < orig._matches; i++) {
                if (orig._pos[i] > 0) {
                    m++;
                    if (f._pos[i] > 0) {
                        f._pos[i]--;
                        f._runningPos--;
                        if (++n >= f._matches) break;
                    }
                }
            }
            return f._matches == f._runningPos;
        }

        ExploredField updateAllPos(ExploredField orig) {
            ExploredField first = null;
            for (ExploredField f : _subFields) if (substractPos(orig, f) && first == null) first = f;
            return first;
        }

        ExploredField getFirst() {
            for (ExploredField f : _subFields) if (f._matches == f._runningPos) return f;
            return null;
        }

        boolean orderFields() {
            if (_subFields == null || _subFields.size() == 1 || _thread == 1) return true;
            LinkedList<ExploredField> s = new LinkedList<ExploredField>();
            ExploredField f = getFirst();
            while (f != null) {
                _subFields.remove(f);
                s.add(f);
                f = updateAllPos(f);
            }
            boolean ok = _subFields.isEmpty();
            if (!ok) {
                s.addAll(_subFields);
                System.out.format("ordering failed for %s ; %d fields remained", _name, _subFields.size());
            }
            _subFields = s;
            return ok;
        }

        boolean orderAll() {
            boolean b = true;
            if (_name != null) b = orderFields();
            if (_subFields != null) for (ExploredField f : _subFields) b &= f.orderAll();
            return b;
        }

        /**
		 * Prints the structure of the data.
		 * @param out  where to write
		 * @param full complete description is output if true, including found values
		 *             and number of occurences. Note that if this value is set to false,
		 *             the result is less complete, but is then a usable template (see
		 *             {@link Patterns}.
		 * @throws IOException
		 */
        public void print(Writer out, boolean full) throws IOException {
            print(out, full, 0);
        }

        private void print(Writer out, boolean full, int depth) throws IOException {
            char[] tab = new char[4 * depth + 1];
            for (int i = 0; i < tab.length; i++) tab[i] = ' ';
            tab[0] = '\n';
            if (_name != null) {
                out.write(tab);
                if (_name.length() > 0) out.write(_name); else out.write("*");
                if (!(_types.contains(FieldType.BASE) && _types.size() == 1)) {
                    char x[] = new char[_types.size()];
                    int i = 0;
                    for (FieldType c : _types) x[i++] = c._c;
                    out.write(" (");
                    out.write(x);
                    out.write(") ");
                }
                if (full) {
                    boolean optional = _total != _owner._totalThread;
                    if (_occMax != 1 || optional) {
                        if (_occMax > 1 && optional) out.write("[optional, up to " + _occMax + "]"); else if (optional) out.write("[optional]"); else if (_occMax > 1) out.write("[up to " + _occMax + "]");
                    }
                    if (_values != null) {
                        out.write(" ==> ");
                        for (String s : _values) {
                            out.write(s + ", ");
                        }
                    }
                }
            }
            if (_subFields != null) {
                out.write(" {");
                Iterator<ExploredField> i = _subFields.iterator();
                boolean b;
                do {
                    i.next().print(out, full, depth + 1);
                    b = i.hasNext();
                    if (b) out.write(",");
                } while (b);
                out.write(tab);
                out.write("}");
            }
        }
    }

    static class ExplorerHooks implements Hooks {

        ExploredField _root = new ExploredField(null);

        ExploredField _cur = _root;

        String _lastName;

        public boolean beforeStruct(Token t) {
            _cur._types.add(ExploredField.FieldType.STRUCT);
            return false;
        }

        ;

        public boolean afterStruct(Token t) {
            _cur.closeOccurences(false);
            _cur = _cur._owner;
            return false;
        }

        ;

        public boolean beforeAnon(Token t) {
            ExploredField f = _cur.findField("");
            if (f == null) f = _cur.addField("");
            f.openOccurence();
            _cur.checkPos(f);
            f._types.add(ExploredField.FieldType.ANON);
            _cur = f;
            return false;
        }

        ;

        public boolean afterAnon(Token t) {
            _cur.closeOccurences(false);
            _cur = _cur._owner;
            return false;
        }

        ;

        public boolean beforeList(Token t) {
            _cur._types.add(ExploredField.FieldType.LIST);
            return false;
        }

        ;

        public boolean afterList(Token t) {
            _cur.closeOccurences(false);
            _cur = _cur._owner;
            return false;
        }

        ;

        public void beforeEmpty(Token t) {
        }

        ;

        public boolean afterEmpty(Token t) {
            _cur._types.add(ExploredField.FieldType.EMPTY);
            _cur.closeOccurences(false);
            _cur = _cur._owner;
            return false;
        }

        ;

        public void begin() {
            _root.openOccurence();
        }

        ;

        public void end(Token t) {
            _root.closeOccurences(true);
            _root.orderAll();
        }

        ;

        public void getName(Token t) {
            _lastName = t._value;
            ExploredField f = _cur.findField(_lastName);
            if (f == null) f = _cur.addField(_lastName);
            f.openOccurence();
            _cur.checkPos(f);
            _cur = f;
        }

        ;

        public void beforeBase(Token t) {
            _cur._types.add(ExploredField.FieldType.BASE);
        }

        ;

        public boolean afterBase(Token t) {
            _cur.addValue(t._value);
            _cur.closeOccurences(false);
            _cur = _cur._owner;
            return false;
        }

        ;

        public boolean afterListData(Token t) {
            _cur.addValue(t._value);
            return false;
        }

        ;
    }

    /**
	 * This function analyzes a data file and collapses all fields in order
	 * to highlight the structure and possibly the data values.
	 * @param r     source of the data.
	 * @return      the analyzed result
	 */
    public static ExploredField analyze(Reader r) throws IOException {
        ExplorerHooks h = new ExplorerHooks();
        Analyzer.analyze(r, h);
        return h._root;
    }

    /**
	 * This function analyzes a data file and collapses all fields in order
	 * to highlight the structure and possibly the data values.
	 * @param filename     source of the data.
	 * @return      the analyzed result
	 */
    public static ExploredField analyze(String filename) throws IOException {
        return analyze(FileLoader.load(filename, FileLoader.Mode.READ));
    }
}
