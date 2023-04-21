package com.aurorasoftworks.signal.tools.core.context.loader;

public class PrimitiveArgs extends PropertiesBase {

    public static PrimitiveArgs createPrimitiveArgs(int i, long l, short s, byte b, char c, float f, double d, String str) {
        return new PrimitiveArgs(i, l, s, b, c, f, d, str);
    }

    public PrimitiveArgs(int i, long l, short s, byte b, char c, float f, double d, String str) {
        this.i = i;
        this.l = l;
        this.s = s;
        this.b = b;
        this.c = c;
        this.f = f;
        this.d = d;
        this.str = str;
    }
}
