package clump.language.ast.definition.statements.impl;

import clump.language.ast.common.IVariableIdentifier;
import clump.language.ast.definition.expression.IExpression;
import clump.language.ast.definition.statements.IStatement;
import clump.language.ast.specification.expression.IEntityInstance;
import opala.lexing.ILocation;
import java.io.Serializable;
import java.util.List;

public class SwitchCase extends AbstractStatement {

    private static final long serialVersionUID = 4192696198450091913L;

    public static class Case implements Serializable {

        private static final long serialVersionUID = -8737684259882626561L;

        private final ILocation location;

        private final IEntityInstance type;

        private final IVariableIdentifier name;

        private final IStatement statement;

        private transient boolean usefullCatch;

        public Case(ILocation location, IEntityInstance type, IVariableIdentifier name, IStatement statement) {
            this.location = location;
            this.type = type;
            this.name = name;
            this.statement = statement;
            this.usefullCatch = false;
        }

        public boolean isUsefullCase() {
            return usefullCatch;
        }

        public void setUsefullCase() {
            this.usefullCatch = true;
        }

        public ILocation getLocation() {
            return location;
        }

        public IEntityInstance getType() {
            return type;
        }

        public IVariableIdentifier getVariable() {
            return name;
        }

        public IStatement getStatement() {
            return statement;
        }
    }

    private final IExpression switchExpression;

    private final List<Case> catches;

    private final IStatement defaultStatement;

    public SwitchCase(ILocation location, IExpression switchExpression, List<Case> catches, IStatement defaultStatement) {
        super(location);
        this.switchExpression = switchExpression;
        this.catches = catches;
        this.defaultStatement = defaultStatement;
    }

    public IExpression getExpression() {
        return switchExpression;
    }

    public List<Case> getCases() {
        return catches;
    }

    public IStatement getDefaultStatement() {
        return defaultStatement;
    }
}
