package com.schinzer.fin.basic;

public class Category {

    private long id;

    private String level1Txt;

    private String level1Order;

    private String level2Txt;

    private String level2Order;

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (int) (id ^ (id >>> 32));
        result = PRIME * result + ((level1Order == null) ? 0 : level1Order.hashCode());
        result = PRIME * result + ((level1Txt == null) ? 0 : level1Txt.hashCode());
        result = PRIME * result + ((level2Order == null) ? 0 : level2Order.hashCode());
        result = PRIME * result + ((level2Txt == null) ? 0 : level2Txt.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Category other = (Category) obj;
        if (id != other.id) return false;
        if (level1Order == null) {
            if (other.level1Order != null) return false;
        } else if (!level1Order.equals(other.level1Order)) return false;
        if (level1Txt == null) {
            if (other.level1Txt != null) return false;
        } else if (!level1Txt.equals(other.level1Txt)) return false;
        if (level2Order == null) {
            if (other.level2Order != null) return false;
        } else if (!level2Order.equals(other.level2Order)) return false;
        if (level2Txt == null) {
            if (other.level2Txt != null) return false;
        } else if (!level2Txt.equals(other.level2Txt)) return false;
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLevel1Order() {
        return level1Order;
    }

    public void setLevel1Order(String level1Order) {
        this.level1Order = level1Order;
    }

    public String getLevel1Txt() {
        return level1Txt;
    }

    public void setLevel1Txt(String level1Txt) {
        this.level1Txt = level1Txt;
    }

    public String getLevel2Order() {
        return level2Order;
    }

    public void setLevel2Order(String level2Order) {
        this.level2Order = level2Order;
    }

    public String getLevel2Txt() {
        return level2Txt;
    }

    public void setLevel2Txt(String level2Txt) {
        this.level2Txt = level2Txt;
    }
}
