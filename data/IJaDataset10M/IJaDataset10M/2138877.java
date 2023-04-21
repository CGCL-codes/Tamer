package net.sourceforge.eclipsex.parser.fast;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.eclipsex.parser.ASClassDef;
import net.sourceforge.eclipsex.parser.ASClassDefUtils;
import net.sourceforge.eclipsex.parser.ASElement;
import net.sourceforge.eclipsex.parser.ASFunction;
import net.sourceforge.eclipsex.parser.ASIdentifier;
import net.sourceforge.eclipsex.parser.ASVar;
import net.sourceforge.eclipsex.parser.EXParsingException;
import net.sourceforge.eclipsex.parser.EXToken;
import net.sourceforge.eclipsex.parser.EXTokenizer;
import net.sourceforge.eclipsex.parser.EXTokenizingException;

class ASClassDefFast extends ASIdentifierFast implements ASClassDef, ASBlockFast {

    private String _package;

    private final List<String> _anchestors = new ArrayList<String>(0);

    private final List<String> _interfaces = new ArrayList<String>(0);

    private final List<ASVar> _fields = new ArrayList<ASVar>(0);

    private final boolean _isInterface;

    private final List<ASFunction> _functions = new ArrayList<ASFunction>(0);

    /**
     *
     * @param t
     * @param prefix
     * @param isInterface
     * @param offset
     * @throws EXParsingException
     * @throws EXTokenizingException
     */
    public ASClassDefFast(final EXTokenizer t, final List<EXToken> prefix, final boolean isInterface, final int offset) throws EXParsingException, EXTokenizingException {
        super(t, prefix, offset);
        _isInterface = isInterface;
        boolean anchestors = false;
        boolean interfaces = false;
        loop: while (t.hasNext()) {
            switch(t.next()) {
                case EXTokenizer.PUNCTUATOR:
                    char c = t.getToken().toString().charAt(0);
                    if (c == '{') {
                        break loop;
                    }
                    break;
                case EXTokenizer.RESERVED_WORD:
                    {
                        String word = t.getToken().toString();
                        if (word.equals("extends")) {
                            anchestors = true;
                            interfaces = false;
                        } else if (word.equals("implements")) {
                            interfaces = true;
                            anchestors = false;
                        } else {
                            throw new EXParsingException("Unexpected reserved word: " + word);
                        }
                        break;
                    }
                case EXTokenizer.SELECTION:
                    {
                        String word = t.getToken().toString();
                        if (anchestors) {
                            _anchestors.add(word);
                        } else if (interfaces) {
                            _interfaces.add(word);
                        }
                        break;
                    }
                default:
            }
        }
    }

    public void add(final ASElement element) {
        if (element instanceof ASVar) {
            _fields.add((ASVar) element);
        } else if (element instanceof ASFunction) {
            _functions.add((ASFunction) element);
            ASFunction function = (ASFunction) element;
            if (function.getName().equals(getName())) {
                function.setReturnType(getName());
            }
        }
    }

    public List<String> getAnchestors() {
        return _anchestors;
    }

    public List<ASVar> getFields() {
        return _fields;
    }

    public String getFullName() {
        return ASClassDefUtils.getFullName(this);
    }

    public List<ASFunction> getFunctions() {
        return _functions;
    }

    public List<ASIdentifier> getIdentifiers() {
        return ASClassDefUtils.getIdentifiers(this);
    }

    public List<String> getInterfaces() {
        return _interfaces;
    }

    public String getPackage() {
        return _package;
    }

    public boolean isInterface() {
        return _isInterface;
    }

    void setPackage(final String pkg) {
        _package = pkg;
    }

    public String getClassName() {
        return getName();
    }
}
