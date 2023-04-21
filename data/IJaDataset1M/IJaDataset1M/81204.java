package org.ozoneDB.tools.OPP;

import java.io.PrintWriter;
import java.util.Set;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.ClassGen;
import de.fub.bytecode.generic.ConstantPoolGen;
import org.ozoneDB.core.ObjectContainer;
import org.ozoneDB.tools.OPP.message.MessageWriter;

/**
 * Helper class that is responsible for all operations that are done on the
 * byte code of class files.
 *
 *
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @version $Revision: 1.8 $Date: 2004/11/20 10:41:32 $
 */
class ImplManipulator {

    protected ClassLoader loader;

    protected PrintWriter out;

    protected String outputDir;

    protected MessageWriter genListener;

    public ImplManipulator(String _outputDir, MessageWriter _genListener, ClassLoader _loader) throws Exception {
        loader = _loader;
        outputDir = _outputDir;
        genListener = _genListener;
    }

    protected String slashedClassName(String className) {
        StringBuffer sb = new StringBuffer(className);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '.') {
                sb.setCharAt(i, '/');
            }
        }
        return sb.toString();
    }

    /**
     * Renames the class and, (if classes not null) checks if the superclass.
     *  needs to be renamed too.
     *  @param classes
     *  @param fileName
     *  @param newClassName
     *  @throws Exception
     */
    public void changeClassFile(Set classes, String fileName, String newClassName) throws Exception {
        String newFileName = outputDir + OPPHelper.classFileBasename(newClassName) + ".class";
        genListener.info("   creating " + newFileName + " from " + fileName + " ...");
        ClassParser parser = new ClassParser(fileName);
        JavaClass jcl = parser.parse();
        String name = jcl.getClassName();
        genListener.info("class name: " + name);
        genListener.info("new class name: " + newClassName);
        ClassGen cg = new ClassGen(jcl);
        ConstantPoolGen cpg = new ConstantPoolGen(jcl.getConstantPool());
        int i = cpg.lookupClass(slashedClassName(name));
        if (i == -1) {
            throw new Exception("Unable to lookup class name in class.");
        }
        ConstantClass cc = (ConstantClass) cpg.getConstant(i);
        int x = cc.getNameIndex();
        ((ConstantUtf8) cpg.getConstant(x)).setBytes(slashedClassName(newClassName));
        cc.setNameIndex(cpg.addUtf8(slashedClassName(newClassName)));
        if (classes != null) {
            genListener.info("   checking super class... ");
            String sName = jcl.getSuperclassName();
            if (classes.contains(sName) || Class.forName("org.ozoneDB.OzoneProxy", true, loader).isAssignableFrom(Class.forName(sName, true, loader))) {
                String newSClassName = sName + ObjectContainer.IMPLNAME_POSTFIX;
                genListener.info(sName + " changed to " + newSClassName);
                i = cpg.lookupClass(slashedClassName(sName));
                cc = (ConstantClass) cpg.getConstant(i);
                x = cc.getNameIndex();
                ((ConstantUtf8) cpg.getConstant(x)).setBytes(slashedClassName(newSClassName));
                cc.setNameIndex(cpg.addUtf8(slashedClassName(newSClassName)));
            } else {
                genListener.info("nothing changed");
            }
        }
        jcl.dump(newFileName);
    }
}
