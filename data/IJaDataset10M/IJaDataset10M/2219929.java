package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.model.directions.inline.InlineInstructionRef;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class InlineInstructionRefVisitor extends AbstractValidationVisitor {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(InlineInstructionRefVisitor.class);

    public void visit(Object element, KnittingContext context) throws KnittingEngineException {
        InlineInstructionRef instructionRef = (InlineInstructionRef) element;
        PatternRepository repository = context.getPatternRepository();
        InlineInstruction instruction = repository.getInlineInstruction(instructionRef.getReferencedInstruction().getId());
        context.getPatternState().setReplayMode(true);
        try {
            visitChild(instruction, context);
        } finally {
            context.getPatternState().setReplayMode(false);
        }
    }
}
