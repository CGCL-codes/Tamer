package com.compendium.io.questmap;

import java.util.*;

/**
 * class TokenTable
 *
 * @author  Ron van Hoof
 */
public class TokenTable extends CodeTable {

    public TokenTable() {
        this(10);
    }

    public TokenTable(int size) {
        super(size);
    }

    public int isToken(String key) {
        return getCode(key);
    }
}
