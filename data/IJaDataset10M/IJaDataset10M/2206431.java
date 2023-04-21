package net.paoding.rose.mock.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.rest.Get;

public class OncePerRequest2Controller {

    @Get
    public Object a(final Invocation inv) throws Exception {
        if (inv.getAttribute("OncePerRequest2Interceptor") == null) {
            throw new IllegalArgumentException("OncePerRequest2Interceptor.before should be invoked");
        }
        Object value = "ok";
        inv.getHeadInvocation().setAttribute("once", value);
        inv.getRequest().setAttribute("preInv", inv);
        final Exception[] exs = new Exception[1];
        new Thread() {

            public void run() {
                try {
                    inv.getRequest().getRequestDispatcher("/oncePerRequest2/b").forward(inv.getRequest(), inv.getResponse());
                } catch (ServletException e) {
                    e.printStackTrace();
                    exs[0] = e;
                } catch (IOException e) {
                    e.printStackTrace();
                    exs[0] = e;
                }
            }

            ;
        }.start();
        Thread.sleep(100);
        if (exs[0] != null) {
            throw exs[0];
        }
        return inv.getRequest().getAttribute("msg");
    }

    public String b(Invocation inv) {
        System.out.println("===success to forward  [OncePerRequest2Controller]");
        Invocation preByActionA = (Invocation) inv.getRequest().getAttribute("preInv");
        if (preByActionA == null) {
            return "preInvocation.error.null";
        }
        if (preByActionA != inv.getPreInvocation()) {
            return "preInvocation.error";
        }
        inv.getPreInvocation().getRequest().setAttribute("msg", "ok");
        String ok = (String) inv.getHeadInvocation().getAttribute("once");
        if (!"ok".equals(ok)) {
            throw new IllegalArgumentException("setOncePerRequestAttribute");
        }
        return ok;
    }
}
