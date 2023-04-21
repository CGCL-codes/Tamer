package ant.famix.cfplib;

import com.ibm.toad.cfparse.ClassFile;
import com.ibm.toad.cfparse.MethodInfo;
import com.ibm.toad.cfparse.attributes.CodeAttrInfo;
import com.ibm.toad.cfparse.instruction.ImmutableCodeSegment;
import com.ibm.toad.cfparse.instruction.Instruction;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import ant.famix.cdif.Output20;
import ant.famix.cdif.Output22;
import ant.famix.model.Access;

/**
 * @author mzeibig
 * @created 08.04.2004 22:00:00
 * @since
 * @version $Revision: 1.2 $
 * @deprecated
 */
final class CFPAccessWriter extends CFPAbstractMethodLevelWriter {

    private static final int minLevel = 3;

    CFPAccessWriter(FileWriter fw, Set ignoreSet) {
        super(fw, ignoreSet);
    }

    private static String rebuildTarget(String target) {
        int klammerpos = target.indexOf('(');
        if (klammerpos == -1) {
            klammerpos = target.length();
        }
        int leerpos = target.indexOf(' ');
        if ((leerpos < klammerpos) && (leerpos > -1)) {
            target = target.substring(leerpos + 1, target.length());
        }
        int numPoint = numOfPoints(target);
        for (int p = 0; p < numPoint - 1; p++) {
            target = target.replaceFirst("\\.", "::");
        }
        return target;
    }

    int write(ClassFile cf, MethodInfo method, String methodName, int counter) throws IOException {
        if (registry.getLevel() >= minLevel) {
            CodeAttrInfo code = (CodeAttrInfo) method.getAttrs().get("Code");
            if (code == null) return counter;
            ImmutableCodeSegment cs = new ImmutableCodeSegment(code.getCode());
            for (int csn = 0; csn < cs.getNumInstructions(); csn++) {
                byte[] inst = cs.getInstruction(csn);
                String name = Instruction.d_instrTable[inst[0] & 0x000000ff].name();
                if ((name.startsWith("get")) || (name.startsWith("put"))) {
                    String target = cf.getCP().getAsJava((inst[1] & 0x000000ff) * 256 + (inst[2] & 0x000000ff));
                    if (target.indexOf('$') > -1) continue;
                    target = rebuildTarget(target);
                    counter++;
                    accesscount++;
                    Access access;
                    if (registry.getFamixVersion() == 22) {
                        access = new ant.famix.model.model20.Access(target, replaceNamespaceSep(cf.getName()) + "." + getSignature(method, methodName));
                        fw.write(Output20.getAsString(access, counter));
                    } else {
                        access = new ant.famix.model.model22.Access(target, replaceNamespaceSep(cf.getName()) + "." + getSignature(method, methodName));
                        fw.write(Output22.getAsString(access, counter));
                    }
                }
            }
        }
        return counter;
    }
}
