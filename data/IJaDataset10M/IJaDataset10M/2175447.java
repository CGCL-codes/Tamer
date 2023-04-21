package com.zhiyun.admin.vo;

import java.io.Serializable;

public class EbActivityItem implements Serializable {

    private static final long serialVersionUID = 2461363926948736447L;

    private Long id;

    private EbActivity activity;

    private EbItem item;

    private Float cheapen;

    private Integer points;

    private Long salesCount;

    private Long maxPer;

    public EbActivityItem() {
    }

    public EbActivityItem(EbActivity activity, EbItem item, Float cheapen, Float disc, Integer points, Long salesCount, Long maxPer) {
        super();
        this.activity = activity;
        this.item = item;
        this.cheapen = cheapen;
        this.points = points;
        this.salesCount = salesCount;
        this.maxPer = maxPer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EbActivity getActivity() {
        return activity;
    }

    public void setActivity(EbActivity activity) {
        this.activity = activity;
    }

    public EbItem getItem() {
        return item;
    }

    public void setItem(EbItem item) {
        this.item = item;
    }

    public Float getCheapen() {
        return cheapen;
    }

    public void setCheapen(Float cheapen) {
        this.cheapen = cheapen;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Long getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Long salesCount) {
        this.salesCount = salesCount;
    }

    public Long getMaxPer() {
        return maxPer;
    }

    public void setMaxPer(Long maxPer) {
        this.maxPer = maxPer;
    }
}
