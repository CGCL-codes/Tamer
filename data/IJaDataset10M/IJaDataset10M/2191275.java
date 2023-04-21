package com.shine.framework.NodeJs.example;

import com.shine.framework.NodeJs.NodeJsManager;

public class Example {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        NodeJsManager.getManager().startNodeJsServer("D:\\workspace\\JavaFramework2.5\\src\\com\\shine\\framework\\NodeJs\\config\\test.js");
    }
}
