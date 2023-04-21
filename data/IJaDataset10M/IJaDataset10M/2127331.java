package shu.cms.lcd.benchmark.auo;

import shu.cms.lcd.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public abstract class Benchmark {

    protected String filename;

    protected LCDTarget lcdTarget;

    /**
   *
   * @param filename String
   * @deprecated
   */
    public Benchmark(String filename) {
        this.filename = filename;
    }

    public Benchmark(LCDTarget lcdTarget) {
        this.lcdTarget = lcdTarget;
    }

    public abstract String report();
}
