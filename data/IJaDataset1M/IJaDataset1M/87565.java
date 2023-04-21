package com.ctp.arquilliandemo.ex1.service;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import com.ctp.arquilliandemo.ex1.domain.Share;
import com.ctp.arquilliandemo.ex1.domain.User;
import com.ctp.arquilliandemo.ex1.event.Buy;
import com.ctp.arquilliandemo.ex1.event.Sell;
import com.ctp.arquilliandemo.ex1.event.ShareEvent;

@Stateless
public class TradeService {

    @Inject
    @Buy
    private Event<ShareEvent> buyEvent;

    @Inject
    @Sell
    private Event<ShareEvent> sellEvent;

    public void buy(User user, Share share, Integer amount) {
        user.addShares(share, amount);
        ShareEvent shareEvent = new ShareEvent(share, user, amount);
        buyEvent.fire(shareEvent);
    }

    public void sell(User user, Share share, Integer amount) {
        user.removeShares(share, amount);
        ShareEvent shareEvent = new ShareEvent(share, user, amount);
        sellEvent.fire(shareEvent);
    }
}
