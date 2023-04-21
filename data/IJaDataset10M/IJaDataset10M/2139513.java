package org.fernwood.jbasic.opcodes;

import org.fernwood.jbasic.JBasic;
import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.SymbolTable;
import org.fernwood.jbasic.value.Value;

/**
 * @author cole
 * 
 */
public class OpPACKAGE extends AbstractOpcode {

    static String PACKAGE_RESTORE = "SYS$PACKAGES_RESTORE";

    /**
	 * Add or delete an entry from the SYS$PACKAGES list used to qualify class
	 * names for user-written statements and functions, and for the NEW()
	 * function that creates Java objects by class name.
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        env.session.checkPermission("JAVA");
        Value packagePath = null;
        if (env.instruction.stringValid) packagePath = new Value(env.instruction.stringOperand); else if (env.instruction.integerOperand != 2) packagePath = env.pop();
        if (packagePath == null) processPackagePath(env.instruction.integerOperand, env.session.globals(), null); else if (packagePath.getType() == Value.ARRAY) {
            for (int idx = 1; idx <= packagePath.size(); idx++) {
                Value pathElement = packagePath.getElement(idx);
                if (pathElement.getType() != Value.STRING) throw new JBasicException(Status.INVTYPE);
                processPackagePath(env.instruction.integerOperand, env.session.globals(), pathElement);
            }
        } else if (packagePath.getType() == Value.STRING) processPackagePath(env.instruction.integerOperand, env.session.globals(), packagePath); else throw new JBasicException(Status.ARGTYPE);
        return;
    }

    /**
	 * Process a single string path item to be inserted or deleted from the 
	 * package list
	 * @param mode the mode 0 - add, 1 - delete, 2 - restore
	 * @param symbols the symbol table to find SYS$PACKAGES
	 * @param packagePath
	 * @throws JBasicException
	 */
    private void processPackagePath(int mode, SymbolTable symbols, Value packagePath) throws JBasicException {
        String s = null;
        if (packagePath != null) {
            s = packagePath.getString();
            while (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        Value packageList = symbols.findReference(JBasic.PACKAGES, false);
        if (packageList == null) {
            packageList = new Value(Value.ARRAY, null);
            symbols.insertReadOnly(JBasic.PACKAGES, packageList);
        }
        Value backupPackageList = symbols.findReference(PACKAGE_RESTORE, false);
        if (backupPackageList == null) {
            backupPackageList = packageList.copy();
            symbols.insertReadOnly(PACKAGE_RESTORE, backupPackageList);
        }
        int index = 0;
        switch(mode) {
            case 0:
                if (packagePath == null) throw new JBasicException(Status.INVOPARG, mode);
                for (index = 1; index <= packageList.size(); index++) if (packageList.getString(index).equals(s)) return;
                index = packageList.size() + 1;
                packageList.setElementOverride(new Value(s), index);
                break;
            case 1:
                if (packagePath == null) throw new JBasicException(Status.INVOPARG, mode);
                for (index = 1; index <= packageList.size(); index++) if (packageList.getString(index).equals(s)) break;
                if (index > packageList.size()) return;
                packageList.removeArrayElement(index);
                break;
            case 2:
                packageList.fReadonly = false;
                symbols.insertReadOnly(JBasic.PACKAGES, backupPackageList);
                symbols.deleteAlways(PACKAGE_RESTORE);
                break;
            default:
                throw new JBasicException(Status.INVOPARG, mode);
        }
    }
}
