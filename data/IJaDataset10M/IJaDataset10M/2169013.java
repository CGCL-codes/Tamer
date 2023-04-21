package net.sourceforge.freejava.text.lop;

import net.sourceforge.freejava.sio.position.IXYTellable;

public interface Token extends IXYTellable {

    int getId();

    String getName();

    String getText();

    int length();

    char charAt(int index);

    Object getValue();

    IXYTellable end();
}
