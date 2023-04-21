package net.sf.doolin.app.sc.engine.support;

import java.util.concurrent.atomic.AtomicInteger;
import net.sf.doolin.app.sc.engine.NameGenerator;

public class StubNameGenerator implements NameGenerator {

    private final String stub;

    private final String separator;

    private final AtomicInteger count = new AtomicInteger(1);

    public StubNameGenerator(String stub) {
        this(stub, "-");
    }

    public StubNameGenerator(String stub, String separator) {
        this.stub = stub;
        this.separator = separator;
    }

    @Override
    public String generateName() {
        int i = this.count.getAndIncrement();
        return String.format("%s%s%d", this.stub, this.separator, i);
    }

    public String getStub() {
        return this.stub;
    }
}
