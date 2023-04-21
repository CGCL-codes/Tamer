package picounit.ruby;

import junit.framework.Test;
import picounit.API;

public class RubyImplementation implements API {

    public RubyImplementation() {
        this.implementation = (API) new RubyBSFManager("picounit.rb").eval("RubyPicoUnit.new");
    }

    public Test generateTestSuite() {
        return implementation.generateTestSuite();
    }

    public Test generateTestSuite(Class<?> caller) {
        return implementation.generateTestSuite(caller);
    }

    private final API implementation;
}
