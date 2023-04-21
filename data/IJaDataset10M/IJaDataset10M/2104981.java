package com.alipay.poc.remoting.hessian.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import junit.framework.TestCase;
import com.alipay.poc.remoting.data.basic.CompoundBean;
import com.alipay.poc.remoting.data.basic.SimpleBean;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * hession���л�����
 * 
 * @author qihao
 * 
 * HessianSerializeTest.java 2007-10-10
 */
public class HessianSerializeTest extends TestCase {

    public void testSimpleBean() throws Exception {
        SimpleBean bean = new SimpleBean(1, 2, "3", new Date(4));
        byte[] ser = serialize(bean);
        SimpleBean bean2 = (SimpleBean) deSerialize(ser);
        assertEquals(bean.toString(), bean2.toString());
    }

    public void testCompoundBean() throws Exception {
        SimpleBean innerBean1 = new SimpleBean(1, 2, "3", new Date(4));
        SimpleBean innerBean2 = new SimpleBean(5, 6, "7", new Date(8));
        CompoundBean bean = new CompoundBean(innerBean1, innerBean2, new BigDecimal("1234.5678"));
        byte[] ser = serialize(bean);
        CompoundBean bean2 = (CompoundBean) deSerialize(ser);
        assertEquals(bean.toString(), bean2.toString());
    }

    /**
	 * ���л�
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
    protected byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput out = new HessianOutput(os);
        out.writeObject(object);
        os.close();
        return os.toByteArray();
    }

    /**
	 * �����л�
	 * 
	 * @param ser
	 * @return
	 * @throws IOException
	 */
    protected Object deSerialize(byte[] ser) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(ser);
        HessianInput in = new HessianInput(is);
        return in.readObject();
    }
}
