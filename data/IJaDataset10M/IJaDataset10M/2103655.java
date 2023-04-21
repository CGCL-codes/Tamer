package com.cvicse.ks.chain;

/**
 * <p>
 * Description: The Test class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: CVIC SE
 * </p>
 * 
 * @author geng_lchao
 * @checker geng_lchao
 * @version 1.0
 * @created at 2007-7-31
 * @modified by geng_lchao at at 2007-7-31
 */
public class Test {

    public static void main(String[] args) {
        CustomerRequest customerRequest_1 = new CustomerRequest(1, "��Ʒά��");
        CustomerRequest customerRequest_2 = new CustomerRequest(2, "�����˻�");
        CustomerRequest customerRequest_3 = new CustomerRequest(3, "�⳥��ʧ");
        CustomerServiceCenter customerServiceCenter = new CustomerServiceCenter();
        System.out.println("------------��������1------------");
        customerServiceCenter.handleCustomerRequest(customerRequest_1);
        System.out.println("------------��������2------------");
        customerServiceCenter.handleCustomerRequest(customerRequest_2);
        System.out.println("------------��������3------------");
        customerServiceCenter.handleCustomerRequest(customerRequest_3);
        System.out.println("------------���?��------------");
        customerServiceCenter.handleCustomerOrder();
    }
}
