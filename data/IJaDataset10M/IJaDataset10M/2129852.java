package com.ssg.tools.jsonviewer.components.helpers;

import com.ssg.tools.jsonxml.Parser;
import com.ssg.tools.jsonxml.common.ParserContext;
import com.ssg.tools.jsonxml.common.FormatterContext;
import com.ssg.tools.jsonxml.common.STRUCTURE_FORMAT_ELEMENT;
import com.ssg.tools.jsonxml.common.Utilities.Pair;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;

/**
 *
 * @author ssg
 */
public class StructureContent implements AbstractDocument.Content {

    Object _object;

    Map<Integer, ObjectPositions> _objectPositions = new HashMap<Integer, ObjectPositions>();

    Map<Integer, Object> _objects = new HashMap<Integer, Object>();

    GapContent _content = new GapContent();

    FormatterContext _formatterContext;

    ParserContext _parserContext;

    Parser _parser;

    public StructureContent(Object object, FormatterContext formatterContext, ParserContext parserContext, Parser parser) {
        _object = object;
        _formatterContext = formatterContext;
        _parserContext = parserContext;
        _parser = parser;
    }

    /**
     * Clears underlying content implementation by creating new empty one...
     */
    public void reset() {
        _content = new GapContent();
        _objectPositions.clear();
    }

    /**
     * Accumulates object-related positions
     * 
     * @param object
     * @param type
     * @param pos 
     */
    public void registerObjectPosition(Object object, STRUCTURE_FORMAT_ELEMENT type, Position pos) {
        if (object == null) {
            return;
        }
        int hash = System.identityHashCode(object);
        ObjectPositions loc = null;
        if (!_objects.containsKey(hash)) {
            _objects.put(hash, object);
        }
        if (_objectPositions.containsKey(hash)) {
            loc = _objectPositions.get(hash);
        } else {
            loc = new ObjectPositions();
            _objectPositions.put(hash, loc);
        }
        loc.registerObjectPosition(type, pos);
    }

    public Map<Integer, ObjectPositions> getObjectsPositions() {
        return _objectPositions;
    }

    /**
     * Returns collection of object-related positions
     * @param object
     * @return 
     */
    public ObjectPositions getObjectPositions(Object object) {
        ObjectPositions result = null;
        if (object == null) {
            return result;
        }
        int hash = System.identityHashCode(object);
        return _objectPositions.get(hash);
    }

    /**
     * Returns hierarchical list of objects for position
     * Top-level object is with last (with largest index).
     * Object with index 0 is the actual object for position.
     * 
     * @param position
     * @return 
     */
    public List<Pair<Object, Pair<Position, Position>>> findObjectsForPosition(int position) {
        List<Pair<Object, Pair<Position, Position>>> objs = new ArrayList<Pair<Object, Pair<Position, Position>>>();
        for (Entry<Integer, Object> e : _objects.entrySet()) {
            Pair<Position, Position> pp = _objectPositions.get(e.getKey()).findPositionsRange(position);
            if (pp != null) {
                objs.add(new Pair<Object, Pair<Position, Position>>(e.getValue(), pp));
            }
        }
        Collections.sort(objs, new Comparator<Pair<Object, Pair<Position, Position>>>() {

            public int compare(Pair<Object, Pair<Position, Position>> o1, Pair<Object, Pair<Position, Position>> o2) {
                if (o1.getB().getA().getOffset() < o2.getB().getA().getOffset()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return objs;
    }

    /**
     * Removes registered info on object positions (if any)
     * 
     * @param object 
     */
    public void unregisterObject(Object object) {
        if (object == null) {
            return;
        }
        int hash = System.identityHashCode(object);
        if (_objectPositions.containsKey(hash)) {
            _objectPositions.remove(hash);
        }
        if (_objects.containsKey(hash)) {
            _objects.remove(hash);
        }
    }

    public String dumpObjectPositions(Object object) {
        return dumpObjectPositions(System.identityHashCode(object));
    }

    public String dumpObjectPositions(Integer objectKey) {
        StringBuilder sb = new StringBuilder();
        for (Integer key : _objectPositions.keySet()) {
            ObjectPositions ps = _objectPositions.get(key);
            if (objectKey == null || objectKey.equals(key)) {
                sb.append("Object hash: " + key + ", position groups: \n");
                int epIdx = 0;
                for (Map<STRUCTURE_FORMAT_ELEMENT, Position> ep : ps.getObjectPositions()) {
                    sb.append("  { [" + epIdx + "]\n");
                    for (STRUCTURE_FORMAT_ELEMENT e : STRUCTURE_FORMAT_ELEMENT.values()) {
                        if (ep.containsKey(e)) {
                            Position p = ep.get(e);
                            sb.append("    " + e + " : " + p.getOffset() + "\n");
                        }
                    }
                    sb.append("  }\n");
                    epIdx++;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 
     * @param text
     * @return 
     */
    public Object restoreObject(String text) {
        if (_parser == null) {
            return null;
        }
        _parserContext.getObjectsRegistry().clear();
        try {
            Object obj = _parser.parse(new StringReader(text), _parserContext);
            return obj;
        } catch (IOException ioex) {
            ioex.printStackTrace(System.out);
        }
        return null;
    }

    public void reformatObject(Object object) {
        if (object != null) {
            int a = 0;
        }
    }

    public Position createPosition(int offset) throws BadLocationException {
        return _content.createPosition(offset);
    }

    public int length() {
        return _content.length();
    }

    public UndoableEdit insertString(int where, String str) throws BadLocationException {
        return _content.insertString(where, str);
    }

    public UndoableEdit remove(int where, int nitems) throws BadLocationException {
        return _content.remove(where, nitems);
    }

    public String getString(int where, int len) throws BadLocationException {
        return _content.getString(where, len);
    }

    public void getChars(int where, int len, Segment txt) throws BadLocationException {
        _content.getChars(where, len, txt);
    }

    public class ObjectPositions {

        List<Map<STRUCTURE_FORMAT_ELEMENT, Position>> _positions = new ArrayList<Map<STRUCTURE_FORMAT_ELEMENT, Position>>();

        /**
         * Accumulates object-related positions
         * 
         * @param object
         * @param type
         * @param pos 
         */
        public void registerObjectPosition(STRUCTURE_FORMAT_ELEMENT type, Position pos) {
            Map<STRUCTURE_FORMAT_ELEMENT, Position> loc = (_positions.isEmpty()) ? null : _positions.get(_positions.size() - 1);
            if (loc != null && loc.containsKey(type)) {
                loc = null;
            }
            if (loc == null) {
                loc = new EnumMap<STRUCTURE_FORMAT_ELEMENT, Position>(STRUCTURE_FORMAT_ELEMENT.class);
                _positions.add(loc);
            }
            loc.put(type, pos);
            boolean isMapOrList = loc.containsKey(STRUCTURE_FORMAT_ELEMENT.endMap) || loc.containsKey(STRUCTURE_FORMAT_ELEMENT.endList);
            if ((isMapOrList || type == STRUCTURE_FORMAT_ELEMENT.endValue) && getObjectPositions().size() > 1) {
                Map<STRUCTURE_FORMAT_ELEMENT, Position> eSE = null;
                Map<STRUCTURE_FORMAT_ELEMENT, Position> eSE2 = null;
                Position eS = null;
                Position eE = null;
                for (Map<STRUCTURE_FORMAT_ELEMENT, Position> pp : getObjectPositions()) {
                    eSE2 = pp;
                    Position pS = pp.get(STRUCTURE_FORMAT_ELEMENT.startElement);
                    Position pE = pp.get(STRUCTURE_FORMAT_ELEMENT.endElement);
                    if (eSE == null) {
                        eSE = pp;
                    }
                    if (eS == null) {
                        eS = pS;
                    } else {
                        if (pS.getOffset() < eS.getOffset()) {
                            eS = pS;
                        }
                    }
                    if (eE == null) {
                        eE = pE;
                    } else {
                        if (pE.getOffset() > eE.getOffset()) {
                            eE = pE;
                        }
                    }
                }
                if (eSE != null) {
                    if (eS != null) {
                        eSE.put(STRUCTURE_FORMAT_ELEMENT.startElement, eS);
                    }
                    if (eE != null) {
                        eSE.put(STRUCTURE_FORMAT_ELEMENT.endElement, eE);
                    }
                    if (isMapOrList && eSE2 != null && System.identityHashCode(eSE) != System.identityHashCode(eSE2)) {
                        while (getObjectPositions().size() > 1) {
                            getObjectPositions().remove(getObjectPositions().size() - 1);
                        }
                        for (STRUCTURE_FORMAT_ELEMENT key : eSE2.keySet()) {
                            if (!eSE.containsKey(key)) {
                                eSE.put(key, eSE2.get(key));
                            }
                        }
                    }
                }
            }
        }

        /**
         * Returns collection of object-related positions
         * @param object
         * @return 
         */
        public List<Map<STRUCTURE_FORMAT_ELEMENT, Position>> getObjectPositions() {
            return _positions;
        }

        /**
         * Returns range of matching instance
         * @param object
         * @return 
         */
        public Pair<Position, Position> findPositionsRange(int position) {
            for (Map<STRUCTURE_FORMAT_ELEMENT, Position> p : _positions) {
                Pair<Position, Position> range = getPositionsRange(p);
                if (position >= range.getA().getOffset() && position <= range.getB().getOffset()) {
                    return range;
                }
            }
            return null;
        }

        /**
         * Returns collection of object-related positions
         * @param object
         * @return 
         */
        public Map<STRUCTURE_FORMAT_ELEMENT, Position> findPositions(int position) {
            for (Map<STRUCTURE_FORMAT_ELEMENT, Position> p : _positions) {
                Pair<Position, Position> range = getPositionsRange(p);
                if (position >= range.getA().getOffset() && position <= range.getB().getOffset()) {
                    return p;
                }
            }
            return null;
        }

        /**
         * Evaluate range
         * @param pp
         * @return 
         */
        public Pair<Position, Position> getPositionsRange(Map<STRUCTURE_FORMAT_ELEMENT, Position> pp) {
            Position min = null;
            Position max = null;
            for (Position p : pp.values()) {
                if (min == null) {
                    min = p;
                } else if (min.getOffset() > p.getOffset()) {
                    min = p;
                }
                if (max == null) {
                    max = p;
                } else if (max.getOffset() < p.getOffset()) {
                    max = p;
                }
            }
            return new Pair<Position, Position>(min, max);
        }
    }
}
