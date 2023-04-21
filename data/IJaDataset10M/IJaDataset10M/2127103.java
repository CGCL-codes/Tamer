package com.gusto.engine.colfil;

/**
 * <p>Represents the distance between 2 entities by:
 * <ul>
 *   <li>The number of common ratings
 *   <li>The distance between the 2 rating vectors
 * </ul> 
 * </p>
 * 
 * @author amokrane.belloui@gmail.com
 *
 */
public class Distance {

    private Long id1;

    private Long id2;

    private Integer count;

    private Double distance;

    public Distance() {
        super();
    }

    public Distance(Long id1, Long id2, Integer count, Double distance) {
        this.id1 = id1;
        this.id2 = id2;
        this.count = count;
        this.distance = distance;
    }

    public Distance(Integer count, Double distance) {
        this.count = count;
        this.distance = distance;
    }

    public String toString() {
        return "Distance " + id1 + "-" + id2 + " : " + distance + " (" + count + ")";
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
