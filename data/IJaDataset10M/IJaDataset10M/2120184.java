package net.sf.doolin.cli.support;

import java.io.PrintWriter;
import net.sf.doolin.cli.CLIOutput;

/**
 * Output on the standard output.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class SystemCLIOutput implements CLIOutput {

    private PrintWriter writer;

    /**
	 * @see System.out
	 * @see java.io.PrintStream#format(String, Object...)
	 * 
	 * {@inheritedDoc}
	 */
    @Override
    public void format(String format, Object... args) {
        System.out.format(format, args);
    }

    @Override
    public PrintWriter getPrintWriter() {
        if (this.writer != null) {
            return this.writer;
        } else {
            this.writer = new PrintWriter(System.out);
            return this.writer;
        }
    }
}
