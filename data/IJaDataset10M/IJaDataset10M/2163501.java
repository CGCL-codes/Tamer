package shake.log;

import java.util.concurrent.atomic.AtomicInteger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Sequence {

    @Inject
    AtomicInteger integer;

    public int get() {
        return integer.getAndIncrement();
    }
}
