package net.sourceforge.SwigXmlToCOM.output;

import net.sourceforge.SwigXmlToCOM.types.*;
import java.io.*;

/**
 * Summary description for ImplOutput.
 */
public class ImplHOutput {

    Project proj;

    public ImplHOutput(Project proj) {
        this.proj = proj;
    }

    public void output() throws Exception {
        Writer f = (Writer) new FileWriter(proj.module + "Impl.h");
        output(f);
        f.close();
    }

    public void output(Writer outwriter) throws Exception {
        PrintWriter out = new PrintWriter(outwriter);
        out.println("/* ----------------------------------------------------------------------------");
        out.println("* This file was automatically generated by SwigXmlToCOM");
        out.println("* and SWIG (http://www.swig.org).");
        out.println("*");
        out.println("* Do not make changes to this file unless you know what you are doing--modify");
        out.println("* the SWIG interface file instead.");
        out.println("* ----------------------------------------------------------------------------- */");
        out.println("");
        out.println("#ifndef __" + proj.module + "_h");
        out.println("#define __" + proj.module + "_h");
        out.println(proj.insertCode);
        java.util.Iterator itr = proj.getClasses().iterator();
        while (itr.hasNext()) {
            ClassObject cls = (ClassObject) itr.next();
            out.println("class C" + cls.getSymName() + ";");
        }
        out.println("");
        itr = proj.getClasses().iterator();
        while (itr.hasNext()) {
            ClassObject cls = (ClassObject) itr.next();
            outputClass(out, cls);
        }
        out.println("");
        out.println("#endif");
        out.println("");
    }

    private void outputClass(PrintWriter out, ClassObject cls) throws Exception {
        out.println("class C" + cls.getSymName() + ":");
        out.println("	public CLCOMBase<" + cls.getName() + ",C" + cls.getSymName() + ",I" + cls.getSymName() + ",&CLSID_" + cls.getSymName() + ",&IID_I" + cls.getSymName() + ">");
        out.println("{");
        out.println("public:");
        Typemap incl = proj.getTypemap(cls.getName(), "", cls.getName(), "axbody", true, false);
        if (incl != null && incl.code != null) out.println(cls.format(incl.code));
        if (!cls.isAbstract) {
            out.println("//constructors");
            java.util.Iterator cnstditr = cls.getConstructorsIterator();
            while (cnstditr.hasNext()) {
                Cdecl cdecl = (Cdecl) cnstditr.next();
                outputMethod(out, cls, cdecl);
            }
            if (!cls.isAbstract && cls.allocate_default_constructor && (!cls.allocate_has_constructor || !cls.getConstructorsIterator().hasNext())) {
                if (cls.getConstructorsIterator().hasNext()) {
                    System.out.println("Warning: " + cls.getName() + " uses default constructor AND has constructors");
                    System.out.println("	memory leaks may occur...");
                }
                out.println(" HRESULT FinalConstruct();");
            }
            out.println("");
        }
        if (cls.destructor != null) {
            out.println("//destructors");
            outputMethod(out, cls, cls.destructor);
            out.println("");
        } else System.out.println("Warning: no destructor for " + cls.getSymName());
        if (cls.baseClass != null) {
            out.println("//castors");
            out.println(" STDMETHOD(UpCast)(I" + cls.baseClass.getSymName() + "** ret);");
            out.println("");
        }
        out.println("//methods");
        java.util.Iterator mthditr = cls.getMethodsIterator();
        while (mthditr.hasNext()) {
            Cdecl cdecl = (Cdecl) mthditr.next();
            outputMethod(out, cls, cdecl);
        }
        out.println("//inherited methods");
        mthditr = cls.getInheritedMethodsIterator();
        while (mthditr.hasNext()) {
            Cdecl cdecl = (Cdecl) mthditr.next();
            outputMethod(out, cls, cdecl);
        }
        out.println("};");
        out.println("OBJECT_ENTRY_AUTO(CLSID_" + cls.getSymName() + ", C" + cls.getSymName() + ");");
        out.println("");
    }

    private void outputMethod(PrintWriter out, ClassObject cls, Cdecl cdecl) throws Exception {
        if (cdecl.isDestructor) {
            out.println(" void FinalRelease();");
        } else {
            out.print(" STDMETHOD(" + cdecl.getSymMethodNameExpanded() + ") (");
            out.print(OutputShared.OutputMethodParameters(proj, cls, cdecl, false, false));
            out.println(");");
        }
    }
}
