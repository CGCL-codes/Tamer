package com.hk.web.util;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.ServletUtil;

public class IncorporatedOrder {

    private static final String HK_ORDERID_KEY = "com_hk_orderid_key";

    private Set<Long> orderIdSet;

    private static final int MAX_AGE = 1 * 7 * 24 * 60 * 60;

    private String cookieDomain;

    private long companyId;

    public IncorporatedOrder(HttpServletRequest request, long companyId) {
        this.companyId = companyId;
        this.cookieDomain = request.getServerName();
        Cookie cookie = ServletUtil.getCookie(request, HK_ORDERID_KEY);
        this.orderIdSet = new HashSet<Long>();
        if (cookie != null) {
            String ids = DataUtil.urlDecoder(cookie.getValue());
            String[] t = ids.split(";");
            if (t[0].equals(companyId + "") && t.length == 2) {
                String[] id = t[1].split(",");
                for (String i : id) {
                    this.orderIdSet.add(Long.parseLong(i));
                }
            }
        }
    }

    public void addOrderId(long orderId) {
        this.orderIdSet.add(orderId);
    }

    public void removeOrderId(long orderId) {
        this.orderIdSet.remove(Long.valueOf(orderId));
    }

    public void removeAllOrderId() {
        this.orderIdSet.clear();
    }

    public Set<Long> getOrderIdSet() {
        return orderIdSet;
    }

    public String toValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.companyId).append(";");
        for (Long l : this.orderIdSet) {
            sb.append(l).append(",");
        }
        if (this.orderIdSet.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String v = DataUtil.urlEncoder(sb.toString());
        return v;
    }

    public boolean containOrderId(long orderId) {
        return this.orderIdSet.contains(orderId);
    }

    public void save(HttpServletResponse response) {
        Cookie cookie = new Cookie(HK_ORDERID_KEY, this.toValue());
        cookie.setMaxAge(MAX_AGE);
        cookie.setDomain(cookieDomain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
