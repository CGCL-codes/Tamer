package com.k_int.gen.AsnUseful;

import com.k_int.codec.runtime.*;
import java.io.Serializable;

public class encoding_inline0_type extends ChoiceType implements Serializable {

    public static final transient int single_asn1_type_CID = 0;

    public static final transient int octet_aligned_CID = 1;

    public static final transient int arbitrary_CID = 2;

    public encoding_inline0_type(int which, Object o) {
        this.which = which;
        this.o = o;
    }

    public encoding_inline0_type() {
    }
}
