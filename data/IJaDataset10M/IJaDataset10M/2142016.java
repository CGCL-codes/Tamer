package org.pqt.mr2rib.mrutil;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import org.pqt.mr2rib.mrparser.MRPConstants;
import org.pqt.mr2rib.mrparser.ParseException;

/**
 *
 * @author Peter Quint  */
public class Camera extends NamedElement {

    public OptionRegister options = new OptionRegister();

    public Pass[] passes = null;

    public Output[] outputs = null;

    public ShaderDefinition[] volume = null;

    public ShaderDefinition[] environment = null;

    public ShaderDefinition[] lens = null;

    /** Creates a new instance of Camera */
    public Camera() {
        super(MRPConstants.CAMERA);
    }

    public void write(PrintStream out, String[] translator, int tabs) throws IOException {
        StringBuffer sb = new StringBuffer(tabs);
        for (int i = 0; i < tabs; i++) sb.append('\t');
        String s = sb.toString();
        out.print(s);
        out.print("camera \"" + getName() + "\"\n");
        for (int i = 0; i < passes.length; i++) {
            passes[i].write(out, translator, tabs + 1);
            out.print("\n");
        }
        for (int i = 0; i < outputs.length; i++) {
            outputs[i].write(out, translator, tabs + 1);
            out.print("\n");
        }
        options.write(out, translator, tabs + 1);
        if (lens != null) {
            out.print(s + "\tlens \n");
            for (int i = 0; i < lens.length; i++) {
                lens[i].write(out, tabs + 2);
            }
        }
        if (environment != null) {
            out.print(s + "\tenvironment \n");
            for (int i = 0; i < environment.length; i++) {
                environment[i].write(out, tabs + 2);
            }
        }
        if (volume != null) {
            out.print(s + "\tvolume \n");
            for (int i = 0; i < volume.length; i++) {
                volume[i].write(out, tabs + 2);
            }
        }
        out.print("end camera");
    }

    public void completeShaderAssignment() throws ParseException {
        Material.completeShaderAssignment(volume);
        Material.completeShaderAssignment(environment);
        Material.completeShaderAssignment(lens);
    }
}
