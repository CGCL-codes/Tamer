package pipe4j.pipe.compare;

import java.io.PrintWriter;
import pipe4j.core.Connections;
import pipe4j.core.connector.BlockingBuffer;
import pipe4j.pipe.AbstractPipe;

public class TextLineComparatorPipe extends AbstractPipe {

    public static final String INPUT_TWO = "inputTwo";

    public static final String INPUT_ONE = "inputOne";

    @Override
    public void run(Connections connections) throws Exception {
        BlockingBuffer inputBufferOne = connections.getNamedInputBuffer(INPUT_ONE);
        BlockingBuffer inputBufferTwo = connections.getNamedInputBuffer(INPUT_TWO);
        PrintWriter pw = new PrintWriter(connections.getOutputStream(), true);
        String lineOne = (String) inputBufferOne.take();
        String lineTwo = (String) inputBufferTwo.take();
        try {
            while (!cancelled() && lineOne != null && lineTwo != null) {
                if (!lineOne.equals(lineTwo)) {
                    pw.print("different");
                    return;
                }
                lineOne = (String) inputBufferOne.take();
                lineTwo = (String) inputBufferTwo.take();
            }
            pw.print("identical");
        } finally {
            pw.flush();
        }
    }
}
