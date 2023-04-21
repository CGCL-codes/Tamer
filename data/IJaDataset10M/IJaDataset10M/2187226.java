package lcm;

import lcm.painters.DirtyMarkPainter;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.buffer.BufferListener;

public interface BufferHandler extends BufferListener {

    DirtyMarkPainter getDirtyMarkPainter(Buffer buffer, int physicalLine);

    void bufferSaved(Buffer buffer);

    void start();
}
