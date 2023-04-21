package helloworld.spring3ext;

/**
 *
 * @author mookiah
 */
class TestServiceImpl implements TestService {

    public TestServiceImpl() {
    }

    @Override
    public void setValue(String str) {
        System.out.println("Hello World Spring3" + str);
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
