package com.wangyu001.entity;

import java.util.Date;
import java.util.Map;
import org.sysolar.sun.mvc.support.BaseBean;
import org.sysolar.sun.mvc.support.RequestWrapper;

/**
 * 域名表，用户收藏的网址所属的域名。
 */
public final class Domain extends BaseBean {

    private Long domainId;

    private String domainName;

    private String domainHref;

    private Integer domainIcoCreated;

    private Integer domainRank;

    private Integer domainType;

    private Integer domainStatus;

    private Long topDomainId;

    private Long coreUrlId;

    private Date domainCdate;

    private Date domainUdate;

    public static long NATIVE_DOMAIN_ID = 1L;

    public static long UNHTTP_DOMAIN_ID = 2L;

    public static int DOMAIN_STATUS_BLACK = -1;

    public static int DOMAIN_STATUS_UNCHECK = 0;

    public static int DOMAIN_STATUS_WHITE = 1;

    public static int DOMAIN_TYPE_HTTP_WWW = 1;

    public static int DOMAIN_TYPE_NATIVE = 2;

    public static int DOMAIN_TYPE_UNHTTP_WWW = 3;

    private Domain topDomain;

    public Domain() {
    }

    public Domain setDomainId(Long domainId) {
        this.domainId = domainId;
        return this;
    }

    public Long getDomainId() {
        return this.domainId;
    }

    public Domain setDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public Domain setDomainHref(String domainHref) {
        this.domainHref = domainHref;
        return this;
    }

    public String getDomainHref() {
        return this.domainHref;
    }

    public Domain setDomainIcoCreated(Integer domainIcoCreated) {
        this.domainIcoCreated = domainIcoCreated;
        return this;
    }

    public Integer getDomainIcoCreated() {
        return this.domainIcoCreated;
    }

    public Domain setDomainRank(Integer domainRank) {
        this.domainRank = domainRank;
        return this;
    }

    public Integer getDomainRank() {
        return this.domainRank;
    }

    public Domain setDomainType(Integer domainType) {
        this.domainType = domainType;
        return this;
    }

    public Integer getDomainType() {
        return this.domainType;
    }

    public Domain setTopDomainId(Long topDomainId) {
        this.topDomainId = topDomainId;
        return this;
    }

    public Long getTopDomainId() {
        return this.topDomainId;
    }

    public Domain setCoreUrlId(Long coreUrlId) {
        this.coreUrlId = coreUrlId;
        return this;
    }

    public Long getCoreUrlId() {
        return this.coreUrlId;
    }

    public Domain setDomainCdate(Date domainCdate) {
        this.domainCdate = domainCdate;
        return this;
    }

    public Date getDomainCdate() {
        return this.domainCdate;
    }

    public Domain setDomainUdate(Date domainUdate) {
        this.domainUdate = domainUdate;
        return this;
    }

    public Date getDomainUdate() {
        return this.domainUdate;
    }

    public Domain fill(Map<String, Object> row) {
        Object obj = null;
        if (null != (obj = row.get("DOMAIN_ID"))) {
            domainId = ((Number) obj).longValue();
        }
        if (null != (obj = row.get("DOMAIN_NAME"))) {
            domainName = (String) obj;
        }
        if (null != (obj = row.get("DOMAIN_HREF"))) {
            domainHref = (String) obj;
        }
        if (null != (obj = row.get("DOMAIN_ICO_CREATED"))) {
            domainIcoCreated = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("DOMAIN_RANK"))) {
            domainRank = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("DOMAIN_TYPE"))) {
            domainType = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("DOMAIN_STATUS"))) {
            domainStatus = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("TOP_DOMAIN_ID"))) {
            topDomainId = ((Number) obj).longValue();
        }
        if (null != (obj = row.get("CORE_URL_ID"))) {
            coreUrlId = ((Number) obj).longValue();
        }
        if (null != (obj = row.get("DOMAIN_CDATE"))) {
            domainCdate = (Date) obj;
        }
        if (null != (obj = row.get("DOMAIN_UDATE"))) {
            domainUdate = (Date) obj;
        }
        return this;
    }

    public Domain fillJson(Map<String, String> json) {
        String value = null;
        if (null != (value = json.get("domainId"))) {
            domainId = Long.valueOf(value);
        }
        if (null != (value = json.get("domainName"))) {
            domainName = value;
        }
        if (null != (value = json.get("domainHref"))) {
            domainHref = value;
        }
        if (null != (value = json.get("domainIcoCreated"))) {
            domainIcoCreated = Integer.valueOf(value);
        }
        if (null != (value = json.get("domainRank"))) {
            domainRank = Integer.valueOf(value);
        }
        if (null != (value = json.get("domainType"))) {
            domainType = Integer.valueOf(value);
        }
        if (null != (value = json.get("domainStatus"))) {
            domainStatus = Integer.valueOf(value);
        }
        if (null != (value = json.get("topDomainId"))) {
            topDomainId = Long.valueOf(value);
        }
        if (null != (value = json.get("coreUrlId"))) {
            coreUrlId = Long.valueOf(value);
        }
        if (null != (value = json.get("domainCdate"))) {
            domainCdate = new Date(Long.valueOf(value));
        }
        if (null != (value = json.get("domainUdate"))) {
            domainUdate = new Date(Long.valueOf(value));
        }
        return this;
    }

    public Domain fill(RequestWrapper wrapper) throws Exception {
        domainId = wrapper.getLong("domainId");
        domainName = wrapper.getString("domainName");
        domainHref = wrapper.getString("domainHref");
        domainIcoCreated = wrapper.getInt("domainIcoCreated");
        domainRank = wrapper.getInt("domainRank");
        domainType = wrapper.getInt("domainType");
        domainStatus = wrapper.getInt("domainStatus");
        topDomainId = wrapper.getLong("topDomainId");
        coreUrlId = wrapper.getLong("coreUrlId");
        domainCdate = wrapper.getDate("domainCdate");
        domainUdate = wrapper.getDate("domainUdate");
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(512).append("{");
        if (null != domainId) {
            sb.append("domainId:").append(domainId).append(",");
        }
        if (null != domainName) {
            sb.append("domainName:'").append(domainName).append("',");
        }
        if (null != domainHref) {
            sb.append("domainHref:'").append(domainHref).append("',");
        }
        if (null != domainIcoCreated) {
            sb.append("domainIcoCreated:").append(domainIcoCreated).append(",");
        }
        if (null != domainRank) {
            sb.append("domainRank:").append(domainRank).append(",");
        }
        if (null != domainType) {
            sb.append("domainType:").append(domainType).append(",");
        }
        if (null != domainStatus) {
            sb.append("domainStatus:").append(domainStatus).append(",");
        }
        if (null != topDomainId) {
            sb.append("topDomainId:").append(topDomainId).append(",");
        }
        if (null != coreUrlId) {
            sb.append("coreUrlId:").append(coreUrlId).append(",");
        }
        if (null != domainCdate) {
            sb.append("domainCdate:").append(domainCdate.getTime()).append(",");
        }
        if (null != domainUdate) {
            sb.append("domainUdate:").append(domainUdate.getTime()).append(",");
        }
        if (null != topDomain) {
            sb.append("topDomain:").append(topDomain).append(",");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}").trimToSize();
        return sb.toString();
    }

    public void setDomainStatus(Integer domainStatus) {
        this.domainStatus = domainStatus;
    }

    public Integer getDomainStatus() {
        return domainStatus;
    }

    public Domain getTopDomain() {
        return topDomain;
    }

    public void setTopDomain(Domain topDomain) {
        this.topDomain = topDomain;
    }
}
