package net.sourceforge.eclipsex.parser.fast;

import java.util.Collections;
import java.util.List;
import net.sourceforge.eclipsex.parser.ASArgument;
import net.sourceforge.eclipsex.parser.ASDocComment;
import net.sourceforge.eclipsex.parser.ASModifiers;
import net.sourceforge.eclipsex.parser.ASModifiersSimple;
import net.sourceforge.eclipsex.parser.AbstractSelection;
import net.sourceforge.eclipsex.parser.EXTokenizer;
import net.sourceforge.eclipsex.parser.EXTokenizingException;
import net.sourceforge.eclipsex.parser.ISelection;

public class ASArgumentFast extends AbstractSelection implements ASArgument {

    private String _name = null;

    private ISelection _outline = null;

    private String _type = null;

    private int _beginIndex = 0;

    private int _endIndex = 0;

    private final ASModifiersSimple _modifiers = new ASModifiersSimple();

    private final CharSequence _source;

    public ASArgumentFast(final EXTokenizer t, final int offset) throws EXTokenizingException {
        _source = t.getSource();
        _outline = t.getToken();
        boolean type = false;
        loop: while (t.hasNext()) {
            switch(t.next()) {
                case EXTokenizer.PUNCTUATOR:
                    char c = t.getToken().toString().charAt(0);
                    switch(c) {
                        case ',':
                            break loop;
                        case ')':
                            t.resume();
                            break loop;
                        case ':':
                            type = true;
                            break;
                        default:
                            break;
                    }
                    break;
                case EXTokenizer.RESERVED_WORD:
                    String word = t.getToken().toString();
                    if (word.equals("final")) {
                        _modifiers.add(word);
                    }
                    break;
                case EXTokenizer.SELECTION:
                    if (type) {
                        _type = t.getToken().toString();
                    } else {
                        _outline = t.getToken();
                        _beginIndex = t.getBeginIndex();
                    }
                    _endIndex = t.getEndIndex();
                    break;
                default:
                    break;
            }
        }
        if (_type == null) {
            _type = "Object";
        }
        _name = _outline.toString();
    }

    public List<String> getAnnotations() {
        return Collections.emptyList();
    }

    public int getBeginIndex() {
        return _beginIndex;
    }

    public ASDocComment getDoc() {
        return null;
    }

    public int getEndIndex() {
        return _endIndex;
    }

    public ASModifiers getModifiers() {
        return _modifiers;
    }

    public String getName() {
        return _name;
    }

    public ISelection getOutline() {
        return _outline;
    }

    public CharSequence getSource() {
        return _source;
    }

    public String getType() {
        return _type;
    }

    public String toString() {
        return getType() + " " + getName();
    }
}
