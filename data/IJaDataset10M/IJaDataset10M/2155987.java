package darwevo.util.files;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

public class StreamDataOutputAdapter extends OutputStream {

    final DataOutput out;

    public StreamDataOutputAdapter(final DataOutput dataOutput) {
        super();
        out = dataOutput;
    }

    @Override
    public void write(final int b) throws IOException {
        out.write(b);
    }
}
