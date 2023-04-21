package websiteschema.vips.extraction.rule;

import java.lang.reflect.Method;
import java.util.Set;
import websiteschema.analyzer.context.BrowserContext;

/**
 *
 * @author ray
 */
public class DivideRuleFactory {

    double pageSize = 0.0;

    double threshold = 0.0;

    String referrer = "";

    Set<String> allDividableNode = null;

    BrowserContext context = null;

    public DivideRule create(String className) {
        DivideRule rule = null;
        try {
            Class clazz = Class.forName(className);
            Object obj = clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName().toLowerCase();
                Class[] args = method.getParameterTypes();
                if (1 == args.length) {
                    if (methodName.contains("set")) {
                        Class arg = args[0];
                        if (methodName.contains("pageSize".toLowerCase()) && double.class.equals(arg)) {
                            method.invoke(obj, pageSize);
                        } else if (methodName.contains("threshold".toLowerCase()) && double.class.equals(arg)) {
                            method.invoke(obj, threshold);
                        } else if (methodName.contains("referrer".toLowerCase()) && String.class.equals(arg)) {
                            method.invoke(obj, referrer);
                        } else if (methodName.contains("allDividableNode".toLowerCase()) && Set.class.equals(arg)) {
                            method.invoke(obj, allDividableNode);
                        } else if (methodName.contains("context".toLowerCase()) && BrowserContext.class.equals(arg)) {
                            method.invoke(obj, context);
                        }
                    }
                }
            }
            rule = (DivideRule) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rule;
    }

    public Set<String> getAllDividableNode() {
        return allDividableNode;
    }

    public void setAllDividableNode(Set<String> allDividableNode) {
        this.allDividableNode = allDividableNode;
    }

    public double getPageSize() {
        return pageSize;
    }

    public void setPageSize(double pageSize) {
        this.pageSize = pageSize;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public BrowserContext getContext() {
        return context;
    }

    public void setContext(BrowserContext context) {
        this.context = context;
    }
}
