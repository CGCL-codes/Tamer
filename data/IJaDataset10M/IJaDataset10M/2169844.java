package com.foshanshop.ejb3.bean;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem implements Serializable {

    private static final long serialVersionUID = -1166337687856636179L;

    private Integer id;

    private String productname;

    private Float price;

    private Order order;

    public OrderItem() {
    }

    public OrderItem(String productname, Float price) {
        this.productname = productname;
        this.price = price;
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(length = 100, nullable = false)
    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * ���ض����ɢ�д���ֵ����ʵ�ָ�ݴ˶���
     * �� id �ֶμ���ɢ�д���ֵ��
     * @return �˶����ɢ�д���ֵ��
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : super.hashCode());
        return hash;
    }

    /**
     * ȷ����������Ƿ���ڴ� OrderItem�����ҽ���
     * ����Ϊ null �Ҹò����Ǿ�����˶�����ͬ id �ֶ�ֵ�� OrderItem ����ʱ��
     * ����Ϊ <code>true</code>��
     * @param ����Ҫ�Ƚϵ����ö���
     * ���˶����������ͬ���� @return <code>true</code>��
     * ����Ϊ <code>false</code>��
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrderItem)) {
            return false;
        }
        OrderItem other = (OrderItem) object;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) return false;
        return true;
    }

    /**
     * ���ض�����ַ��ʾ������ʵ�ָ�� id �ֶ�
     * ����˱�ʾ����
     * @return ������ַ��ʾ����
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "[id=" + id + "]";
    }
}
