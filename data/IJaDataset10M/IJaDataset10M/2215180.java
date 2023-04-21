package net.sf.jfta;

import net.sf.jfta.impl.JftaProperties;

public class TestClientFactory {

    private static TestClientFactory instance;

    private JftaProperties properties;

    private TestClientFactory() throws TestException {
        properties = JftaProperties.getInstance();
    }

    public static TestClientFactory getInstance() {
        if (instance == null) {
            instance = new TestClientFactory();
        }
        return instance;
    }

    public ITestClient createClient() throws TestException {
        String implName = properties.getHUImplName();
        if (implName == null || "".equals(implName)) {
            throw new TestException("implementation name is not set in properties");
        }
        try {
            Class<?> klass = Class.forName(implName);
            if (!ITestClient.class.isAssignableFrom(klass)) {
                throw new TestException("implementation class " + implName + " does not implement " + ITestClient.class.getName());
            }
            try {
                return (ITestClient) klass.newInstance();
            } catch (InstantiationException e) {
                throw new TestException("class " + implName + " cannot be instantiated");
            } catch (IllegalAccessException e) {
                throw new TestException("class " + implName + " cannot be instantiated");
            }
        } catch (ClassNotFoundException e) {
            throw new TestException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getInstance().createClient());
    }
}
