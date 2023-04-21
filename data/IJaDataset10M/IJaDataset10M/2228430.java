package com.spoledge.audao.parser.gql.impl.soft.func;

/**
 * NVL( expr, value )
 * 
 * If expr is not null, then returns expr, otherwise value.
 * Same as Oracle's NVL function.
 */
public class FuncNVL extends Func2 {

    protected Object getFunctionValue(Object o1, Object o2) {
        return o1 != null ? o1 : o2;
    }
}
