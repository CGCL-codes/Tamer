package com.dcivision.webservice.samples.example4;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import com.dcivision.user.bean.UserRecord;

public class ListUserClient {

    public static void main(String[] args) throws Exception {
        Options options = new Options(args);
        Service service = new Service();
        Call call = (Call) service.createCall();
        QName qn = new QName("urn:ListUserService", "Order");
        call.registerTypeMapping(UserRecord.class, qn, new org.apache.axis.encoding.ser.BeanSerializerFactory(UserRecord.class, qn), new org.apache.axis.encoding.ser.BeanDeserializerFactory(UserRecord.class, qn));
        UserRecord[] result = new UserRecord[0];
        String error;
        try {
            call.setTargetEndpointAddress(new java.net.URL(options.getURL()));
            call.setOperationName(new QName("ListUserService", "getUserList"));
            call.setReturnClass(Object[].class);
            result = (UserRecord[]) call.invoke(new Object[] {});
        } catch (AxisFault fault) {
            error = "Error : " + fault.toString();
        }
        if (null != result && result.length > 0) {
            for (int i = 0; i < result.length; i++) {
                UserRecord user = (UserRecord) result[i];
                System.out.println(i + ":" + "FullName -- " + user.getFullName() + ", loginName -- " + user.getLoginName());
            }
        } else {
            System.out.println("result is empty!");
        }
    }
}
