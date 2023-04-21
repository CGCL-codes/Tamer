package com.hk.web.bomb.action;

import com.hk.bean.BombLaba;
import com.hk.web.pub.action.LabaVo;

public class BombLabaVo {

    private BombLaba bombLaba;

    private LabaVo labaVo;

    public BombLaba getBombLaba() {
        return bombLaba;
    }

    public void setBombLaba(BombLaba bombLaba) {
        this.bombLaba = bombLaba;
    }

    public LabaVo getLabaVo() {
        return labaVo;
    }

    public void setLabaVo(LabaVo labaVo) {
        this.labaVo = labaVo;
    }
}
