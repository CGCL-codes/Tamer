package org.javalid.test.spring.model;

import org.javalid.annotations.core.JvMethod;
import org.javalid.annotations.core.JvParam;
import org.javalid.annotations.core.ValidateDefinition;
import org.javalid.annotations.helper.BeanLookup;
import org.javalid.annotations.helper.Lookup;
import org.javalid.test.spring.bean.Employee_LookupBean;

@ValidateDefinition(primaryGroup = "1", validationType = ValidateDefinition.TYPE_LOOKUP, lookup = @Lookup(type = Lookup.LOOKUP_NORMAL_BEAN, beanLookup = @BeanLookup(beanClass = Employee_LookupBean.class, method = @JvMethod(name = "validate", params = { @JvParam(valueRetrievalMode = JvParam.MODE_CURRENT_OBJECT), @JvParam(valueRetrievalMode = JvParam.MODE_CURRENT_PATH), @JvParam(valueRetrievalMode = JvParam.MODE_LOOKUP_SPRING, type = "java.lang.Long", lookupName = "SimpleServiceBean", requiresMethodCall = true, methodName = "getLongValue") }))), forceDoubleValidation = false)
public class Employee_Bean {

    private String firstName, lastName;

    public Employee_Bean() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}
