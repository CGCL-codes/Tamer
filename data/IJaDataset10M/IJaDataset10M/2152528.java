package org.easyrec.plugin.arm.model;

import org.easyrec.model.core.ItemVO;

/**
 * <DESCRIPTION>
 * <p/>
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2007</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: pmarschik $<br/>
 * $Date: 2011-02-11 11:04:49 +0100 (Fr, 11 Feb 2011) $<br/>
 * $Revision: 17656 $</p>
 *
 * @author Stephan Zavrel
 */
public class TupleVO {

    private ItemVO<Integer, Integer> item1;

    private ItemVO<Integer, Integer> item2;

    private Integer support;

    public TupleVO(ItemVO<Integer, Integer> item1, ItemVO<Integer, Integer> item2, Integer support) {
        super();
        this.item1 = item1;
        this.item2 = item2;
        this.support = support;
    }

    public ItemVO<Integer, Integer> getItem1() {
        return item1;
    }

    public void setItem1(ItemVO<Integer, Integer> item1) {
        this.item1 = item1;
    }

    public ItemVO<Integer, Integer> getItem2() {
        return item2;
    }

    public void setItem2(ItemVO<Integer, Integer> item2) {
        this.item2 = item2;
    }

    public Integer getSupport() {
        return support;
    }

    public void setSupport(Integer support) {
        this.support = support;
    }
}
