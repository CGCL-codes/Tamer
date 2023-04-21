package physicalc;

/** "break" statement
 *
 * @author Stuart Sierra, ss2806@columbia.edu
 */
public class Break extends Stmt {

    public Break() {
        ;
    }

    public Datum eval(SymbolTable globals, SymbolTable locals) {
        throw new BreakSignal();
    }
}
