package com.prefabware.jmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.prefabware.commons.CollectionUtil;
import com.prefabware.commons.JavaUtil;
import com.prefabware.commons.QualifiedName;
import com.prefabware.commons.collection.Filter;
import com.prefabware.commons.reflection.ReflectionUtil;
import com.prefabware.jmodel.beans.Bean;
import com.prefabware.jmodel.beans.BeanTypeFactoryDelegee;
import com.prefabware.jmodel.beans.BeanTypeFactoryDelegee.Candidate;
import com.prefabware.jmodel.beans.BeanUtil;
import com.prefabware.jmodel.beans.Property;
import com.prefabware.jmodel.code.JBlock;
import com.prefabware.jmodel.code.JCodable;
import com.prefabware.jmodel.code.JCodeOptions;
import com.prefabware.jmodel.code.JCodeOptionsBase;
import com.prefabware.jmodel.sample.instancebuilder.Address;
import com.prefabware.jmodel.sample.instancebuilder.Customer;
import com.prefabware.jmodel.typefactory.TypeFactory;

public class BeanTest {

    private JConstructor customerCon;

    private Bean customerBean;

    private TypeFactory typeFactory;

    private BeanTypeFactoryDelegee beanFactory;

    @Before
    public void setUp() throws Exception {
        Filter<Candidate> filter = new Filter<Candidate>() {

            @Override
            public boolean includes(Candidate candidate) {
                return candidate.typeClass.equals(Bean.class) || candidate.qualifiedName.getQualifier().startsWith("com.prefabware.jmodel.sample");
            }
        };
        beanFactory = new BeanTypeFactoryDelegee(filter);
        typeFactory = new TypeFactory(beanFactory);
        QualifiedName qn = new QualifiedName(this.getClass().getPackage().getName(), "Customer");
        customerBean = typeFactory.getJType(Bean.class, qn, null, JModifiers.NONE, JVisibility.PUBLIC);
        assertNotNull(customerBean);
        JField name = JTypeUtil.newJField("name", typeFactory.getJType(String.class), customerBean, JModifiers.NONE, JVisibility.PUBLIC);
        customerBean.add(name);
        JField number = JTypeUtil.newJField("number", typeFactory.getJType(Integer.class), customerBean, JModifiers.NONE, JVisibility.PUBLIC);
        customerBean.add(number);
        BeanUtil.addGetterForAllFields(customerBean, JModifiers.NONE, JVisibility.PUBLIC);
        BeanUtil.addSetterForAllFields(customerBean, JModifiers.NONE, JVisibility.PUBLIC);
        JTypeUtil.addDefaultConstructor(customerBean, JModifiers.NONE, JVisibility.PUBLIC);
        customerCon = customerBean.getDefaultConstructor();
        assertNotNull(customerCon);
    }

    @Test
    public void testReflectBean() throws Exception {
        List<Method> methods = ReflectionUtil.getAllMethods(Address.class);
        Map<String, Method> methodMap = CollectionUtil.asNameMap(methods);
        Method getCountry = methodMap.get("getCountry");
        assertNotNull(getCountry);
        assertTrue(JavaUtil.isGetter(getCountry));
        Bean address = typeFactory.getJType(Bean.class, Address.class);
        assertEquals(2, address.getGetter().size());
        assertEquals(0, address.getSetter().size());
        assertEquals(3, address.getMethods().size());
        Bean person = typeFactory.getJType(Bean.class, Customer.class);
        assertEquals(2, person.getGetter().size());
        assertEquals(0, person.getSetter().size());
        assertEquals(3, person.getMethods().size());
    }

    class WithAllFieldConstructor {

        Integer id;

        WithAllFieldConstructor(Integer id) {
            super();
            this.id = id;
        }
    }

    @Test
    public void testAllFieldConstructor() throws Exception {
        Bean bean = typeFactory.getJType(Bean.class, WithAllFieldConstructor.class);
        JField field = bean.getField("id");
        assertNotNull(field);
        JConstructor con = BeanUtil.createAllFieldConstructor(bean, JVisibility.PUBLIC);
        assertNotNull(con);
        JBlock block = con.getBlocks().get(0);
        assertNotNull(block);
        JCodable line = block.getCodables().get(0);
        assertEquals("this.id = id", line.asCode(JCodeOptions.DEFAULT));
    }

    class WithGenericFields {

        List<String> stringList;
    }

    @Test
    public void testGenericField() throws Exception {
        Bean bean = typeFactory.getJType(Bean.class, WithGenericFields.class);
        JField field = bean.getField("stringList");
        assertNotNull(field);
        Property property = BeanUtil.createProperty(field, JModifiers.NONE, JVisibility.PUBLIC);
        assertNotNull(property);
        assertEquals(typeFactory.getJType(List.class), property.getGetter().getDeclaration().getType());
        assertEquals(1, property.getGetter().getDeclaration().getTypeArguments().size());
        assertEquals(typeFactory.getJType(String.class), property.getGetter().getDeclaration().getTypeArguments().get(0));
        assertEquals("public java.util.List<java.lang.String> getStringList()", property.getGetter().getDeclaration().asCode(JCodeOptionsBase.DEFAULT));
        assertEquals(typeFactory.getJType(void.class), property.getSetter().getDeclaration().getType());
        assertEquals(0, property.getSetter().getDeclaration().getTypeArguments().size());
        assertEquals("public void setStringList(final java.util.List<java.lang.String> stringList)", property.getSetter().getDeclaration().asCode(JCodeOptionsBase.DEFAULT));
    }

    private void log(String message, Object... args) {
        System.out.println(String.format(message, args));
    }
}
