package org.asam.ods;

/**
 *	Generated from IDL definition of struct "TS_ValueSeq"
 *	@author JacORB IDL compiler 
 */
public final class TS_ValueSeq implements org.omg.CORBA.portable.IDLEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public TS_ValueSeq() {
    }

    public org.asam.ods.TS_UnionSeq u;

    public short[] flag;

    public TS_ValueSeq(org.asam.ods.TS_UnionSeq u, short[] flag) {
        this.u = u;
        this.flag = flag;
    }
}
