package jbomberman.net.pipe;

import java.io.IOException;

public class CompressFilter extends Filter {

    public CompressFilter(Pipe in, Pipe out) {
        super(in, out);
    }

    protected void transform() {
        try {
            int c = input_.read();
            while (c != -1) {
                output_.write(c + 1);
                c = input_.read();
            }
            output_.closeWriter();
        } catch (IOException exc) {
            exc.printStackTrace();
            System.err.println("Error: Could not compress.");
            System.exit(1);
        }
    }
}
