package net.sourceforge.eclipsex.parser;

public class EXToken extends Selection {

    private final int _type;

    public EXToken(final int type, final CharSequence source, final int beginIndex, final int endIndex) {
        super(source, beginIndex, endIndex);
        _type = type;
    }

    public boolean equals(final Object obj) {
        return super.equals(obj) && ((EXToken) obj)._type == _type;
    }

    public int getType() {
        return _type;
    }
}
