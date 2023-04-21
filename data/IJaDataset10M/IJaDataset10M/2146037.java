package com.simconomy.entity;

import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @Column(name = "USER_ID")
    private String id;

    @ManyToMany
    @JoinTable(name = "USER_SHOP", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "SHOP_ID"))
    @org.hibernate.annotations.IndexColumn(name = "DISPLAY_POSITION")
    @org.hibernate.annotations.ForeignKey(name = "FK_USER_SHOP_USER_ID", inverseName = "FK_USER_SHOP_SHOP_ID")
    private Set<Shop> shops;

    @OneToMany(mappedBy = "createBy")
    private Set<Shop> createdShops;

    @OneToMany(mappedBy = "createBy")
    private Set<Shop> createdImages;

    @OneToMany(mappedBy = "user")
    private Set<Address> deliverAddresses;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address homeAddress;

    public User(String userName) {
        this.id = userName;
    }

    public Set<Shop> getCreatedImages() {
        return createdImages;
    }

    public void setCreatedImages(Set<Shop> createdImages) {
        this.createdImages = createdImages;
    }

    public Set<Shop> getCreatedShops() {
        return createdShops;
    }

    public void setCreatedShops(Set<Shop> createdShops) {
        this.createdShops = createdShops;
    }

    public Set<Address> getDeliverAddresses() {
        return deliverAddresses;
    }

    public void setDeliverAddresses(Set<Address> deliverAddresses) {
        this.deliverAddresses = deliverAddresses;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Shop> getShops() {
        return shops;
    }

    public void setShops(Set<Shop> shops) {
        this.shops = shops;
    }
}
