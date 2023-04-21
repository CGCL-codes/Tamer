package net.sourceforge.refactor4pdt.core.variablescope;

import java.util.HashMap;
import org.eclipse.php.internal.core.ast.nodes.ASTNode;

public class VariablePhpBlock {

    public enum modifiers {

        PRIVATE, PROTECTED
    }

    public VariablePhpBlock(ASTNode node) {
        expressions = new HashMap();
        accesses = new HashMap();
        globals = new HashMap();
        type = node.getType();
        start = node.getStart();
        length = node.getLength();
        end = node.getEnd();
        block = node;
        parent = null;
        modifier = modifiers.PRIVATE;
        int ai[];
        int j = (ai = protectedBlocks).length;
        for (int i = 0; i < j; i++) {
            int blocktype = ai[i];
            if (blocktype == type) modifier = modifiers.PROTECTED;
        }
    }

    public static boolean isInArray(int lookfor, int lookin[]) {
        boolean result = false;
        int ai[];
        int j = (ai = lookin).length;
        for (int i = 0; i < j; i++) {
            int current = ai[i];
            if (current == lookfor) result = true;
        }
        return result;
    }

    private static int protectedBlocks[] = { 12 };

    public static int blockTypes[] = { 12, 29 };

    public static int blockHasParent[] = { 29 };

    public int start;

    public int length;

    public int end;

    public boolean curly;

    public ASTNode block;

    public int type;

    public modifiers modifier;

    public HashMap expressions;

    public HashMap accesses;

    public HashMap globals;

    public VariablePhpBlock parent;
}
