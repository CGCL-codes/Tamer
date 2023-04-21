package com.mostka.phprpclibtest.client;

import com.mostka.phprpc.client.PhpRpcCallback;
import com.mostka.phprpc.client.PhpRpcRelocatePath;
import com.mostka.phprpc.client.PhpRpcService;

@PhpRpcRelocatePath("services")
public class TestService implements PhpRpcService {

    public void getTestObject(String arg1, int arg2, TestObject objTest, PhpRpcCallback<TestObject> p_callBack) {
    }

    ;
}
