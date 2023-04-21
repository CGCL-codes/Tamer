package com.cqut.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * 会议室
 * @author heyanqiu
 *
 */
@Entity
public class MeetingRoom {

    @Id
    @GeneratedValue
    private int id;

    @Column(length = 30)
    private String name;

    @Column(length = 10)
    private int count;

    @OneToMany(mappedBy = "id")
    @JoinColumn(unique = true, name = "equipments")
    private List<Equipment> equipments;

    @Column(length = 100)
    private String address;

    @Column(length = 2000, nullable = true)
    private String info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
