package org.argouml.language.csharp.importer.csparser.statements;

import org.argouml.language.csharp.importer.csparser.nodes.expressions.ExpressionNode;
import org.argouml.language.csharp.importer.csparser.collections.NodeCollection;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 18, 2008
 * Time: 11:42:36 AM
 */
public class CaseNode extends StatementNode {

    public boolean IsDefaultCase = false;

    public NodeCollection<ExpressionNode> Ranges = new NodeCollection<ExpressionNode>();

    public BlockStatement StatementBlock = new BlockStatement();

    public void ToSource(StringBuilder sb) {
        this.NewLine(sb);
        for (int i = 0; i < Ranges.size(); i++) {
            sb.append("case ");
            Ranges.get(i).ToSource(sb);
            sb.append(":");
            if (i == Ranges.size() - 1 && IsDefaultCase == false && StatementBlock.isHasBraces() == false && StatementBlock.Statements.size() != 1) {
                indent++;
            }
            this.NewLine(sb);
        }
        if (IsDefaultCase) {
            sb.append("default:");
            if (StatementBlock.isHasBraces() == false && StatementBlock.Statements.size() != 1) {
                indent++;
            }
            this.NewLine(sb);
        }
        StatementBlock.ToSource(sb);
    }
}
