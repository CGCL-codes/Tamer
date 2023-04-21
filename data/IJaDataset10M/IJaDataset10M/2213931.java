package net.narusas.si.auction.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class 지역 {

    public static Collection<지역> 최상위지역;

    public static List<지역> all;

    int id;

    String 지역명;

    String 대체지역명;

    Set<지역> 하위지역;

    Integer 상위지역Code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get지역명() {
        return 지역명;
    }

    public void set지역명(String 지역명) {
        this.지역명 = 지역명;
    }

    public String get대체지역명() {
        return 대체지역명;
    }

    public void set대체지역명(String 대체지역명) {
        this.대체지역명 = 대체지역명;
    }

    public Set<지역> get하위지역() {
        return 하위지역;
    }

    public void set하위지역(Set<지역> 하위지역) {
        this.하위지역 = 하위지역;
    }

    public String toString() {
        return 지역명;
    }

    public Integer get상위지역Code() {
        return 상위지역Code;
    }

    public void set상위지역Code(Integer code) {
        상위지역Code = code;
    }

    public boolean match지역명(String target) {
        if (get대체지역명() != null) {
            return target.contains(get지역명()) || get지역명().contains(target) || get대체지역명().contains(target);
        }
        return target.contains(get지역명()) || get지역명().contains(target);
    }
}
