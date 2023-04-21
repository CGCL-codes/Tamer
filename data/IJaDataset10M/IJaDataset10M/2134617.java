package net.sf.beezle.mork.classfile.attribute;

import net.sf.beezle.mork.classfile.Input;
import net.sf.beezle.mork.classfile.Output;
import java.io.IOException;

public abstract class Empty extends Attribute {

    public Empty(String name, Input src) throws IOException {
        super(name);
        int len;
        len = src.readU4();
        if (len != 0) {
            throw new RuntimeException("non-emptry attribute " + name);
        }
    }

    public Empty(String name) {
        super(name);
    }

    @Override
    public void write(Output dest) throws IOException {
        dest.writeUtf8(name);
        dest.writeU4(0);
    }

    @Override
    public String toString() {
        return name + " attribute";
    }
}
