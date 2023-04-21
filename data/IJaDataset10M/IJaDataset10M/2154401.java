package net.sourceforge.transumanza.task.load.info;

import net.sourceforge.transumanza.writer.BaseWriter;

public class WriterTreeWalkerDisplayHandler implements WriterTreeWalkerHandler {

    private StringBuffer buffer = new StringBuffer();

    public void handle(int level, BaseWriter writerInfo) {
        indent(level);
        buffer.append(WriterVisitHelper.getWriterName(writerInfo)).append("\n");
    }

    private void indent(int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("\t");
        }
    }

    public String getOutput() {
        return buffer.toString();
    }
}
