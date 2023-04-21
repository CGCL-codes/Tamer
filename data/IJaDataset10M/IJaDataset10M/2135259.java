package com.coyou.ad.data;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.coyou.ad.config.Config;
import com.coyousoft.adsys.entity.Email;
import com.coyousoft.adsys.entity.Advert;
import com.coyousoft.adsys.entity.Proxy;

public abstract class AbsMailFetcher {

    protected static final Logger l = Logger.getLogger(AbsMailFetcher.class);

    protected List<Email> emailList = null;

    protected List<Proxy> proxyList = null;

    protected Advert advert = null;

    /**
     * 从服务器获取邮件列表
     * @param size 获取的数目, 0代表全部
     */
    public abstract void fetchMailList(int size) throws Exception;

    /**
     * 从服务器获取代理列表
     */
    public abstract void fetchProxyList(int size);

    /**
     * 获取待发送邮件
     * @return
     */
    public Advert getAdvert() {
        return this.advert;
    }

    /**
     * 设置待发邮件
     */
    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    public List<Proxy> getProxyList(int size) {
        if (this.proxyList == null || this.proxyList.size() == 0) {
            this.fetchProxyList(Config.FETCH_PROXY_SIZE);
        }
        return this.proxyList;
    }

    public List<Email> getEmailList(int size) throws Exception {
        if (this.emailList == null || this.emailList.size() == 0) {
            this.fetchMailList(Config.FETCH_MAIL_SIZE);
        }
        List<Email> tmpList = new ArrayList<Email>(size);
        for (int i = 0; i < size && this.emailList.size() != 0; i++) {
            tmpList.add(this.emailList.remove(0));
        }
        return tmpList;
    }
}
