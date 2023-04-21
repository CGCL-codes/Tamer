package jk.spider.util.cheat;

import jk.spider.core.SpiderController;

public abstract class BaseSpiderCheatImpl implements SpiderCheat {

    protected SpiderController controller;

    public BaseSpiderCheatImpl(SpiderController controller) {
        this.controller = controller;
    }
}
