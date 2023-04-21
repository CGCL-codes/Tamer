package org.std;

import org.xbig.base.*;

public interface Iofstream extends INativeObject {

    /** **/
    public void clear();

    /** **/
    public boolean eof();

    /** **/
    public boolean fail();

    /** **/
    public short fill();

    /** **/
    public short fill(short ch);

    /** **/
    public org.std.Iostream flush();

    /** **/
    public boolean good();

    /** **/
    public org.std.Iostream put(short ch);

    /** **/
    public int width();

    /** **/
    public int width(int w);

    /** **/
    public boolean is_open();

    /** **/
    public void close();

    /** **/
    public void open(String filename);
}
