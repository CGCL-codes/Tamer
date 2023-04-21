package jofc2;

import java.io.Writer;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class NullAwareJsonWriter extends JsonWriter {

    public NullAwareJsonWriter(Writer writer, char[] lineIndenter, String newLine, int mode) {
        super(writer, lineIndenter, newLine, mode);
    }

    public NullAwareJsonWriter(Writer writer, char[] lineIndenter, String newLine) {
        super(writer, lineIndenter, newLine);
    }

    public NullAwareJsonWriter(Writer writer, char[] lineIndenter) {
        super(writer, lineIndenter);
    }

    public NullAwareJsonWriter(Writer writer, int mode) {
        super(writer, mode);
    }

    public NullAwareJsonWriter(Writer writer, String lineIndenter, String newLine) {
        super(writer, lineIndenter, newLine);
    }

    public NullAwareJsonWriter(Writer writer, String lineIndenter) {
        super(writer, lineIndenter);
    }

    public NullAwareJsonWriter(Writer writer) {
        super(writer);
    }

    @Override
    protected void writeText(QuickWriter writer, String text) {
        if (text != null) {
            super.writeText(writer, text);
        } else {
            writer.write("null");
        }
    }
}
