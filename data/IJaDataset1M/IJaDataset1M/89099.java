package whiteoak.tools.openjdk.compiler.jvm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.util.Context;

public class WhiteoakClassWriter extends ClassWriter {

    private ArrayList<String> classFiles;

    protected WhiteoakClassWriter(Context context) {
        super(context);
        classFiles = new ArrayList<String>();
    }

    public static void preRegister(final Context context) {
        context.put(classWriterKey, new Context.Factory<ClassWriter>() {

            public ClassWriter make() {
                return new WhiteoakClassWriter(context);
            }
        });
    }

    public static WhiteoakClassWriter instance(Context context) {
        return (WhiteoakClassWriter) ClassWriter.instance(context);
    }

    @Override
    public void writeClassFile(OutputStream out, ClassSymbol c) throws IOException, PoolOverflow, StringOverflow {
        super.writeClassFile(out, c);
        classFiles.add(c.name.toString());
    }

    public ArrayList<String> getClassFiles() {
        return classFiles;
    }
}
