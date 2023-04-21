package net.sourceforge.jnipp.peerGen;

import net.sourceforge.jnipp.common.ClassNode;
import net.sourceforge.jnipp.common.FormattedFileWriter;
import net.sourceforge.jnipp.common.MethodNode;
import net.sourceforge.jnipp.project.PeerGenSettings;
import java.io.File;
import java.util.Iterator;

public class JNIMappingCodeImplGenerator {

    private PeerGenSettings peerGenSettings = null;

    public JNIMappingCodeImplGenerator() {
    }

    public void generate(ClassNode root, PeerGenSettings peerGenSettings) throws java.io.IOException {
        this.peerGenSettings = peerGenSettings;
        String fullFileName = peerGenSettings.getProject().getCPPOutputDir() + File.separatorChar;
        if (root.getPackageName().equals("") == true) fullFileName += root.getCPPClassName() + "Mapping.cpp"; else fullFileName += root.getPackageName().replace('.', File.separatorChar) + File.separatorChar + root.getCPPClassName() + "Mapping.cpp";
        FormattedFileWriter writer = new FormattedFileWriter(fullFileName, true);
        writer.outputLine("#include \"net/sourceforge/jnipp/JVM.h\"");
        writer.outputLine("#include \"net/sourceforge/jnipp/JNIEnvHelper.h\"");
        writer.outputLine("#include \"net/sourceforge/jnipp/EnvironmentAlreadyInitializedException.h\"");
        writer.outputLine("#include \"" + root.getClassName() + "Mapping.h\"");
        writer.outputLine("#include \"" + root.getClassName() + "PeerFactory.h\"");
        writer.newLine(1);
        writer.outputLine("using namespace net::sourceforge::jnipp;");
        Iterator it = root.getNamespaceElements();
        if (it.hasNext() == true) {
            writer.output("using namespace " + (String) it.next());
            while (it.hasNext() == true) writer.output("::" + (String) it.next());
            writer.outputLine(";");
            writer.newLine(1);
        }
        writer.newLine(2);
        generateMethods(root, writer);
        writer.newLine(2);
        writer.flush();
        writer.close();
    }

    public void generateInitMethod(ClassNode root, FormattedFileWriter writer) throws java.io.IOException {
        String namespace = root.getPackageName().replace('.', '_');
        if (namespace == null || namespace.equals("") == true) namespace = "Java_"; else namespace = "Java_" + namespace + "_";
        writer.outputLine("JNIEXPORT void JNICALL " + namespace + root.getClassName() + "Proxy_init(JNIEnv* env, jclass cls)");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("// This method is called by the Java Proxy init() on the other side of the JNI to initialize the environment");
        writer.outputLine("try");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("JNIEnvHelper::init( env );");
        writer.outputLine("JNINativeMethod nativeMethods[] =");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("{ \"releasePeer\", \"()V\", (void*)" + namespace + root.getClassName() + "Proxy_releasePeer },");
        Iterator it = root.getMethods();
        while (it.hasNext() == true) {
            MethodNode node = (MethodNode) it.next();
            writer.output("{ \"" + node.getName() + "\", \"" + node.getJNISignature() + "\", (void*)" + namespace + root.getClassName() + "Proxy_");
            writer.output((peerGenSettings.getUseRichTypes() == true ? node.getCPPName() : node.getUniqueCPPName()) + " }");
            if (it.hasNext() == true) writer.output(",");
            writer.outputLine("");
        }
        writer.decTabLevel();
        writer.outputLine("};");
        writer.newLine(1);
        writer.outputLine("JNIEnvHelper::RegisterNatives( cls, nativeMethods, sizeof(nativeMethods)/sizeof(nativeMethods[0]) );");
        writer.decTabLevel();
        writer.outputLine("}");
        writer.outputLine("catch(EnvironmentAlreadyInitializedException&)");
        writer.outputLine("{");
        writer.outputLine("}");
        writer.decTabLevel();
        writer.outputLine("}");
    }

    public void generateMethods(ClassNode root, FormattedFileWriter writer) throws java.io.IOException {
        String namespace = root.getPackageName().replace('.', '_');
        if (namespace == null || namespace.equals("") == true) namespace = "Java_"; else namespace = "Java_" + namespace + "_";
        generateInitMethod(root, writer);
        writer.newLine(1);
        writer.outputLine(root.getCPPClassName() + "Peer* " + namespace + root.getClassName() + "Proxy_getPeerPtr(JNIEnv* env, jobject obj)");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("jclass cls = env->GetObjectClass( obj );");
        writer.outputLine("jfieldID fid = env->GetFieldID( cls, \"peerPtr\", \"J\" );");
        writer.outputLine("jlong peerPtr = env->GetLongField( obj, fid );");
        writer.outputLine(root.getCPPClassName() + "Peer* ptr = NULL;");
        writer.outputLine("if ( peerPtr == 0 )");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("ptr = " + root.getCPPClassName() + "PeerFactory::newPeer();");
        writer.outputLine("peerPtr = reinterpret_cast<jlong>( ptr );");
        writer.outputLine("env->SetLongField( obj, fid, peerPtr );");
        writer.decTabLevel();
        writer.outputLine("}");
        writer.newLine(1);
        writer.outputLine("return reinterpret_cast<" + root.getCPPClassName() + "Peer*>( peerPtr );");
        writer.decTabLevel();
        writer.outputLine("}");
        writer.newLine(1);
        writer.outputLine("JNIEXPORT void JNICALL " + namespace + root.getClassName() + "Proxy_releasePeer(JNIEnv* env, jobject obj)");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("// This method is called by the Java Proxy finalizer() on the other side of the JNI to free the peer");
        writer.outputLine("jclass cls = env->GetObjectClass( obj );");
        writer.outputLine("jfieldID fid = env->GetFieldID( cls, \"peerPtr\", \"J\" );");
        writer.outputLine("jlong peerPtr = env->GetLongField( obj, fid );");
        writer.outputLine("if ( peerPtr != 0 )");
        writer.incTabLevel();
        writer.outputLine("delete reinterpret_cast<" + root.getCPPClassName() + "Peer*>( peerPtr );");
        writer.decTabLevel();
        writer.decTabLevel();
        writer.outputLine("}");
        writer.newLine(1);
        writer.outputLine("// methods");
        Iterator it = root.getMethods();
        while (it.hasNext() == true) {
            int currentIndex = 0;
            MethodNode node = (MethodNode) it.next();
            writer.output("JNIEXPORT ");
            writer.output(node.getReturnType().getPlainJNITypeName() + " ");
            writer.output("JNICALL " + namespace + root.getClassName() + "Proxy_");
            writer.output((peerGenSettings.getUseRichTypes() == true ? node.getCPPName() : node.getUniqueCPPName()) + "(JNIEnv* env, jobject obj");
            Iterator params = node.getParameterList();
            while (params.hasNext() == true) {
                ClassNode currentParam = (ClassNode) params.next();
                writer.output(", " + currentParam.getPlainJNITypeName());
                writer.output(" p" + currentIndex++);
            }
            writer.outputLine(")");
            writer.outputLine("{");
            writer.incTabLevel();
            writer.outputLine("JNIEnvHelper::init( env );");
            if (node.getReturnType().getPlainJNITypeName().equals("void") == false) writer.outputLine(node.getReturnType().getPlainJNITypeName() + " rVal;\n");
            writer.outputLine("try");
            writer.outputLine("{");
            writer.incTabLevel();
            if (node.getReturnType().getPlainJNITypeName().equals("void") == false) writer.output("rVal = ");
            writer.output(namespace + root.getClassName() + "Proxy_getPeerPtr( env, obj )->");
            writer.output((peerGenSettings.getUseRichTypes() == true ? node.getCPPName() : node.getUniqueCPPName()) + "( env, obj");
            currentIndex = 0;
            params = node.getParameterList();
            while (params.hasNext() == true) {
                writer.output(", p" + currentIndex++);
                params.next();
            }
            writer.outputLine(" );");
            writer.decTabLevel();
            writer.outputLine("}");
            writer.outputLine("catch(jthrowable thr)");
            writer.outputLine("{");
            writer.incTabLevel();
            writer.outputLine("JNIEnvHelper::Throw( thr );");
            writer.decTabLevel();
            writer.outputLine("}");
            if (node.getReturnType().getPlainJNITypeName().equals("void") == false) {
                writer.newLine(1);
                writer.outputLine("return rVal;");
            }
            writer.decTabLevel();
            writer.outputLine("}");
            writer.newLine(1);
        }
        writer.newLine(1);
    }
}
