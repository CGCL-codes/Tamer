package se.oktad.permgencleaner.checkforleak.possibleleak;

import java.util.Random;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import se.oktad.permgencleaner.module.test.memoryleak.MemoryLeaker;

public class ModuleJdk4LoggerMemoryLeaker implements MemoryLeaker {

    public void leakMemory() throws ClassNotFoundException {
        String randomString = new Random(System.currentTimeMillis()).nextLong() + "";
        Logger logger = Logger.getLogger(randomString);
    }
}
