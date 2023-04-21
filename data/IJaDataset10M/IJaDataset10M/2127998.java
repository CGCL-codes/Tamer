package org.baatar.opt.genetic.set.meta;

import java.io.Serializable;

/**
 * 
 * 
 * @author B_ZEREN
 */
public class Row implements Serializable {

    public int ndx = 0;

    public int cost = 0;

    public String info = null;

    public Column columns[] = null;
}
