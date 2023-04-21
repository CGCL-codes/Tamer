package com.schwidder.fun.spring.runtime;

import java.util.HashMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.schwidder.fun.spring.parser.IFSpringParser;
import com.schwidder.nucleus.helper.interfaces.ITemplateStore;
import com.schwidder.nucleus.objects.interfaces.INetGroup;

/**
 * @author kai@schwidder.com
 * @version 1.0
 */
public class DemoParser {

    public static void main(String args[]) throws Exception {
        INetGroup pApp;
        ITemplateStore aStore;
        ApplicationContext ctx;
        HashMap aMap;
        if (args.length == 0) {
            System.out.println("Please provide a spring-bean filename");
            return;
        }
        ctx = new FileSystemXmlApplicationContext(args[0]);
        IFSpringParser g = (IFSpringParser) ctx.getBean("parserDemo");
        g.setCtx(ctx);
        pApp = g.start();
        if (pApp.isEmpty()) pApp.registerNet(g.startNet(pApp));
        aMap = (HashMap) pApp.getAttributes();
        aMap.put("spring.ctx", ctx);
        aStore = g.getTemplateStore();
        aStore = (ITemplateStore) aStore.get(pApp);
        if (pApp.getLazyLoad()) pApp.setTemplateStore(aStore);
        Thread aThread = new Thread(new DemoParserNetGroup(pApp));
        aThread.start();
    }
}
