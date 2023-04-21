package pcgen.base.formula;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import pcgen.base.formula.compile.Formula;
import pcgen.base.formula.compile.FormulaCompiler;
import pcgen.base.formula.operation.CountOperation;
import pcgen.base.formula.operation.IfOperation;
import pcgen.base.formula.operation.NaryMethodOperation;
import pcgen.base.formula.operation.UnaryMethodOperation;
import pcgen.base.formula.operation.VarOperation;

/**
 * @author Thomas Parker
 */
public final class FormulaFileTest {

    private FormulaFileTest() {
    }

    public static void main(String[] args) {
        FormulaCompiler pt = new FormulaCompiler();
        try {
            pt.registerFunction("floor", new UnaryMethodOperation(Math.class, "floor"));
            pt.registerFunction("ceil", new UnaryMethodOperation(Math.class, "ceil"));
            pt.registerFunction("round", new UnaryMethodOperation(Math.class, "rint"));
            pt.registerFunction("abs", new UnaryMethodOperation(Math.class, "abs"));
            pt.registerFunction("min", new NaryMethodOperation(Math.class, "min"));
            pt.registerFunction("max", new NaryMethodOperation(Math.class, "max"));
            pt.registerFunction("var", new VarOperation());
            pt.registerFunction("count", new CountOperation());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        pt.registerFunction("if", new IfOperation());
        try {
            FileInputStream is = new FileInputStream(args[0]);
            File f = new File(args[0] + ".fail.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bir = new BufferedReader(isr);
            String line;
            FileWriter fis = new FileWriter(f);
            while ((line = bir.readLine()) != null) {
                Formula fm = pt.compile(line);
                if (fm == null) {
                    fis.write(line);
                    fis.write("\n");
                } else {
                    if (!fm.toString().equalsIgnoreCase(line)) System.err.println(line + " " + fm);
                }
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
