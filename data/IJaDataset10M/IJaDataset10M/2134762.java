package game.packs;

import org.kamimark.enums._EnumInterface;

public interface _Pack<ENUMTYPE extends _EnumInterface> extends Cloneable {

    public Object clone();

    public boolean equals(final Object obj);

    public Object getElements();

    public _EnumInterface[] getNames();

    public String toString();
}
