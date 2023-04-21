package com.mycompany.mypackage.business.implementation;

import com.mycompany.mypackage.bean.MyBean;
import com.mycompany.mypackage.business.IMyBeanBC;
import br.gov.component.demoiselle.crud.supercrud.SuperCrudBC;

@SuppressWarnings("serial")
public class MyBeanBC extends SuperCrudBC<MyBean> implements IMyBeanBC {
}
