package ch.superj.jjtree.test;

import ch.superj.core.IEnv;
import ch.superj.core.IEnvBeans;

public class EnvBeansTest implements IEnvBeans {

    public IEnv getEnvBean(String beanName) {
        if (beanName.equals("Bean1")) return new Bean1();
        return null;
    }

    public static class Bean1 implements IEnv {

        public InnerBean1 meth() {
            return new InnerBean1("");
        }

        public InnerBean1 meth(String string) {
            return new InnerBean1(string);
        }

        public String meth(String test, String test2) {
            return test + " - " + test2;
        }
    }

    public static class InnerBean1 {

        private String _string;

        public InnerBean1(String test) {
            _string = test;
        }

        public InnerBean2 meth(String string) {
            return new InnerBean2(_string + string);
        }

        public InnerBean2 meth() {
            return new InnerBean2(_string);
        }

        public String meth(String test, String test2) {
            return test + " - " + test2;
        }
    }

    public static class InnerBean2 {

        private String _string;

        public InnerBean2(String test) {
            _string = test;
        }

        public String meth(String test, String test2) {
            return _string + test + " - " + test2;
        }

        public String meth() {
            return _string;
        }
    }
}
