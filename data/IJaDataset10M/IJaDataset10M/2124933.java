package x36dip.utils;

import x36dip.model.Goods;

/**
 *
 * @author Petr
 */
public class GoodsMarker {

    private boolean check;

    private Integer number;

    private Goods goods;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
