package org.susan.java.design.prototype;

public class N1Client {

    public static void main(String args[]) {
        N1PersonalOrder oal = new N1PersonalOrder();
        oal.setOrderProductNum(100);
        System.out.println("这是第一次获取的对象实例==" + oal.getOrderProductNum());
        N1PersonalOrder oa2 = (N1PersonalOrder) oal.clone();
        oa2.setOrderProductNum(80);
        System.out.println("输出克隆对象的实例==" + oa2.getOrderProductNum());
        System.out.println("再次输入原型实例==" + oal.getOrderProductNum());
    }
}
