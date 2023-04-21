package com.kn.controller.test;

import com.kn.controller.model.builder.Struts1Builder;
import com.kn.controller.model.builder.StrutsBuilder;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String packageName = "base";
        String className = "MyExampleXXX";
        String templateDefinition = "templateDefinition";
        StrutsBuilder sb = new Struts1Builder(packageName, className, templateDefinition);
        sb.buildParts();
    }
}
