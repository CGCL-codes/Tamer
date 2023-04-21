package org.t2framework.recl.visitor;

import org.t2framework.recl.ValidationContext;
import org.t2framework.recl.expression.And;
import org.t2framework.recl.expression.ConditionResult;
import org.t2framework.recl.expression.If;
import org.t2framework.recl.expression.Or;
import org.t2framework.recl.expression.message.MessageResult;
import org.t2framework.recl.expression.message.MessageStore;
import org.t2framework.recl.expression.validator.Required;
import org.t2framework.recl.expression.validator.ValidationResult;
import org.t2framework.recl.expression.value.BooleanValue;
import org.t2framework.recl.expression.value.None;
import org.t2framework.recl.type.BooleanType;

public interface Visitor<CTX extends ValidationContext> {

    void visit(And<CTX> and, CTX context);

    void visit(Or<CTX> or, CTX context);

    void visit(ConditionResult<CTX> result, CTX context);

    BooleanType visit(BooleanValue<CTX> value, CTX context);

    void visit(If<CTX> ifClause, CTX context);

    void visit(Required<CTX> required, CTX context);

    void visit(ValidationResult<CTX> result, CTX context);

    void visit(None none, CTX context);

    void visit(MessageStore<CTX> messageStore, CTX context);

    void visit(MessageResult<CTX> result, CTX context);
}
