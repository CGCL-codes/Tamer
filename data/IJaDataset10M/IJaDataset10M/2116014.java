package net.sourceforge.hlm.semantics.traverse;

import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.formulae.*;
import net.sourceforge.hlm.library.objects.*;
import net.sourceforge.hlm.library.objects.constructions.*;
import net.sourceforge.hlm.library.objects.operators.*;
import net.sourceforge.hlm.library.objects.predicates.*;
import net.sourceforge.hlm.library.objects.theorems.*;
import net.sourceforge.hlm.library.parameters.*;
import net.sourceforge.hlm.library.parameters.arguments.*;
import net.sourceforge.hlm.library.proofs.*;
import net.sourceforge.hlm.library.terms.element.*;
import net.sourceforge.hlm.library.terms.set.*;
import net.sourceforge.hlm.library.terms.symbol.*;
import net.sourceforge.hlm.semantics.clone.*;

public abstract class ObjectTraverser {

    public void traverseObject(MathObject object) throws Exception {
        this.traverseParameters(object.getParameters());
        if (object instanceof Definition) {
            if (object instanceof Construction) {
                Construction construction = (Construction) object;
                for (Constructor constructor : construction.getConstructors()) {
                    this.traverseObject(constructor);
                }
                FixedPlaceholder<Construction.Embedding> embeddingPlaceholder = construction.getEmbedding();
                Construction.Embedding embedding = this.enterPlaceholder(embeddingPlaceholder);
                if (embedding != null) {
                    this.traverseEmbedding(embedding);
                }
                this.exitPlaceholder(embeddingPlaceholder);
            } else if (object instanceof Constructor) {
                Constructor constructor = (Constructor) object;
                SelectablePlaceholder<Constructor.EqualitySpecification> equalitySpecificationPlaceholder = constructor.getEqualitySpecification();
                Constructor.EqualitySpecification equalitySpecification = this.enterPlaceholder(equalitySpecificationPlaceholder);
                if (equalitySpecification != null) {
                    this.traverseEqualitySpecification(equalitySpecification);
                }
                this.exitPlaceholder(equalitySpecificationPlaceholder);
            } else if (object instanceof SetOperator) {
                SetOperator operator = (SetOperator) object;
                this.traverseEquivalenceList(operator.getTerms());
                FixedPlaceholder<OperatorSetRestriction> restrictionPlaceholder = operator.getSetRestriction();
                OperatorSetRestriction restriction = this.enterPlaceholder(restrictionPlaceholder);
                if (restriction != null) {
                    this.traverseOperatorSetRestriction(restriction);
                }
                this.exitPlaceholder(restrictionPlaceholder);
            } else if (object instanceof Operator) {
                SelectablePlaceholder<Operator.Definition> definitionPlaceholder = ((Operator) object).getDefinition();
                Operator.Definition definition = this.enterPlaceholder(definitionPlaceholder);
                if (definition != null) {
                    this.traverseOperatorDefinition(definition);
                }
                this.exitPlaceholder(definitionPlaceholder);
            } else if (object instanceof Predicate) {
                this.traverseEquivalenceList(((Predicate) object).getConditions());
            } else {
                throw new IllegalArgumentException();
            }
        } else if (object instanceof Theorem) {
            SelectablePlaceholder<Theorem.Contents> contentsPlaceholder = ((Theorem) object).getContents();
            Theorem.Contents contents = this.enterPlaceholder(contentsPlaceholder);
            if (contents != null) {
                this.traverseTheoremContents(contents);
            }
            this.exitPlaceholder(contentsPlaceholder);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseParameters(ParameterList parameters) throws Exception {
        for (Parameter parameter : parameters) {
            this.traverseParameter(parameter);
        }
    }

    public void traverseParameter(Parameter parameter) throws Exception {
        if (parameter instanceof ArbitrarySetParameter) {
        } else if (parameter instanceof SubsetParameter) {
            this.traverseSetTermPlaceholder(((SubsetParameter) parameter).getSuperset());
        } else if (parameter instanceof ElementParameter) {
            this.traverseElementParameter((ElementParameter) parameter);
        } else if (parameter instanceof ConstraintParameter) {
            this.traverseFormulaPlaceholder(((ConstraintParameter) parameter).getFormula());
        } else if (parameter instanceof BindingParameter) {
            BindingParameter bindingParameter = (BindingParameter) parameter;
            this.traverseParameter(bindingParameter.getElementParameter());
            this.traverseParameters(bindingParameter.getBoundParameters());
        } else if (parameter instanceof SymbolParameter) {
        } else if (parameter instanceof SetDefinition) {
            this.traverseSetTermPlaceholder(((SetDefinition) parameter).getTerm());
        } else if (parameter instanceof ElementDefinition) {
            this.traverseElementTermPlaceholder(((ElementDefinition) parameter).getTerm());
        } else if (parameter instanceof SymbolDefinition) {
            this.traverseSymbolTermPlaceholder(((SymbolDefinition) parameter).getTerm());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseElementParameter(ElementParameter parameter) throws Exception {
        this.traverseSetTermPlaceholder(parameter.getSet());
        FixedPlaceholder<ElementParameter.ShortCut> shortCutPlaceholder = parameter.getShortCut();
        ElementParameter.ShortCut shortCut = this.enterPlaceholder(shortCutPlaceholder);
        if (shortCut != null) {
            this.traverseMathObjectReference(shortCut.getConstruction(), null);
            this.traverseParameters(shortCut.getParameters());
        }
        this.exitPlaceholder(shortCutPlaceholder);
    }

    public Substituter traverseArguments(ArgumentList arguments, Substituter substituter) throws Exception {
        MapIterator<Parameter, Argument> iterator = arguments.iterator();
        while (iterator.hasNext()) {
            Parameter parameter = iterator.next();
            this.traverseArgument(parameter, iterator.getTarget(), substituter);
        }
        return substituter;
    }

    public void traverseArgument(Parameter parameter, Argument argument, Substituter substituter) throws Exception {
        if (argument instanceof VariableArgument<?>) {
            this.traverseContextPlaceholder(((VariableArgument<?>) argument).getTerm());
            if (argument instanceof SubsetArgument) {
                this.traverseProofPlaceholder(((SubsetArgument) argument).getSubsetProof());
            } else if (argument instanceof ElementArgument) {
                this.traverseProofPlaceholder(((ElementArgument) argument).getElementProof());
            }
        } else if (argument instanceof ConstraintArgument) {
            this.traverseProofPlaceholder(((ConstraintArgument) argument).getProof());
        } else if (argument instanceof BindingArgument) {
            BindingArgument bindingArgument = (BindingArgument) argument;
            this.traverseElementParameter(bindingArgument.getParameter());
            this.traverseArguments(bindingArgument.getBoundArguments(), substituter);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseFormula(Formula formula, ContextPlaceholder<?> correspondingPlaceholder) throws Exception {
        if (formula instanceof EnumerationFormula) {
            for (ContextPlaceholder<Formula> placeholder : ((EnumerationFormula) formula).getFormulae()) {
                this.traverseFormulaPlaceholder(placeholder);
            }
        } else if (formula instanceof QuantifiedFormula) {
            QuantifiedFormula quantifiedFormula = (QuantifiedFormula) formula;
            this.traverseParameters(quantifiedFormula.getParameters());
            ContextPlaceholder<Formula> placeholder = quantifiedFormula.getFormula();
            if (placeholder != null) {
                this.traverseFormulaPlaceholder(placeholder);
            }
        } else if (formula instanceof RelationFormula<?, ?>) {
            RelationFormula<?, ?> relationFormula = (RelationFormula<?, ?>) formula;
            this.traverseContextPlaceholder(relationFormula.getLeftTerm());
            this.traverseContextPlaceholder(relationFormula.getRightTerm());
        } else if (formula instanceof StructuralFormula) {
            this.traverseStructuralCases(((StructuralFormula) formula).getCases());
        } else if (formula instanceof PredicateFormula) {
            this.traverseMathObjectReference(((PredicateFormula) formula).getPredicate(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseSetTerm(SetTerm term, ContextPlaceholder<?> correspondingPlaceholder) throws Exception {
        if (term instanceof VariableSetTerm) {
            this.traverseContextItemReference(((VariableSetTerm) term).getVariable());
        } else if (term instanceof ConstructionTerm) {
            this.traverseMathObjectReference(((ConstructionTerm) term).getConstruction(), null);
        } else if (term instanceof EnumerationTerm) {
            for (ElementTerm innerTerm : ((EnumerationTerm) term).getTerms()) {
                this.traverseElementTerm(innerTerm, correspondingPlaceholder);
            }
        } else if (term instanceof SubsetTerm) {
            SubsetTerm subsetTerm = (SubsetTerm) term;
            this.traverseElementParameter(subsetTerm.getParameter());
            this.traverseFormulaPlaceholder(subsetTerm.getFormula());
        } else if (term instanceof ExtendedSubsetTerm) {
            ExtendedSubsetTerm extendedSubsetTerm = (ExtendedSubsetTerm) term;
            this.traverseParameters(extendedSubsetTerm.getParameters());
            this.traverseElementTermPlaceholder(extendedSubsetTerm.getTerm());
        } else if (term instanceof StructuralSetTerm) {
            this.traverseStructuralCases(((StructuralSetTerm) term).getCases());
        } else if (term instanceof SetOperatorTerm) {
            this.traverseMathObjectReference(((SetOperatorTerm) term).getOperator(), null);
        } else if (term instanceof PreviousSetTerm) {
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseElementTerm(ElementTerm term, ContextPlaceholder<?> correspondingPlaceholder) throws Exception {
        if (term instanceof VariableElementTerm) {
            this.traverseContextItemReference(((VariableElementTerm) term).getVariable());
        } else if (term instanceof ConstructorTerm) {
            ConstructorTerm constructorTerm = (ConstructorTerm) term;
            Substituter substituter = this.traverseMathObjectReference(constructorTerm.getConstruction(), null);
            this.traverseMathObjectReference(constructorTerm.getConstructor(), substituter);
        } else if (term instanceof CaseElementTerm) {
            this.traverseCases(((CaseElementTerm) term).getCases());
        } else if (term instanceof StructuralElementTerm) {
            this.traverseStructuralCases(((StructuralElementTerm) term).getCases());
        } else if (term instanceof OperatorTerm) {
            this.traverseMathObjectReference(((OperatorTerm) term).getOperator(), null);
        } else if (term instanceof PreviousElementTerm) {
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseSymbolTerm(SymbolTerm term, ContextPlaceholder<?> correspondingPlaceholder) throws Exception {
        if (term instanceof VariableSymbolTerm) {
            this.traverseContextItemReference(((VariableSymbolTerm) term).getVariable());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public <A> void traverseContextPlaceholder(ContextPlaceholder<A> placeholder) throws Exception {
        Class<? extends A> type = placeholder.getType();
        if (type == Formula.class) {
            this.traverseFormulaPlaceholder((ContextPlaceholder<Formula>) placeholder);
        } else if (type == SetTerm.class) {
            this.traverseSetTermPlaceholder((ContextPlaceholder<SetTerm>) placeholder);
        } else if (type == ElementTerm.class) {
            this.traverseElementTermPlaceholder((ContextPlaceholder<ElementTerm>) placeholder);
        } else if (type == SymbolTerm.class) {
            this.traverseSymbolTermPlaceholder((ContextPlaceholder<SymbolTerm>) placeholder);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseFormulaPlaceholder(ContextPlaceholder<Formula> placeholder) throws Exception {
        Formula formula = this.enterPlaceholder(placeholder);
        if (formula != null) {
            this.traverseFormula(formula, placeholder);
        }
        this.exitPlaceholder(placeholder);
    }

    public void traverseSetTermPlaceholder(ContextPlaceholder<SetTerm> placeholder) throws Exception {
        SetTerm term = this.enterPlaceholder(placeholder);
        if (term != null) {
            this.traverseSetTerm(term, placeholder);
        }
        this.exitPlaceholder(placeholder);
    }

    public void traverseElementTermPlaceholder(ContextPlaceholder<ElementTerm> placeholder) throws Exception {
        ElementTerm term = this.enterPlaceholder(placeholder);
        if (term != null) {
            this.traverseElementTerm(term, placeholder);
        }
        this.exitPlaceholder(placeholder);
    }

    public void traverseSymbolTermPlaceholder(ContextPlaceholder<SymbolTerm> placeholder) throws Exception {
        SymbolTerm term = this.enterPlaceholder(placeholder);
        if (term != null) {
            this.traverseSymbolTerm(term, placeholder);
        }
        this.exitPlaceholder(placeholder);
    }

    public <A> void traverseStructuralCases(StructuralCaseList<A> cases) throws Exception {
        this.traverseElementTermPlaceholder(cases.getSample());
        MapIterator<Constructor, StructuralCaseList.Case<A>> iterator = cases.iterator();
        while (iterator.hasNext()) {
            Constructor constructor = iterator.next();
            this.traverseStructuralCase(constructor, iterator.getTarget());
        }
    }

    public <A> void traverseStructuralCase(Constructor constructor, StructuralCaseList.Case<A> item) throws Exception {
        this.traverseParameters(item.getParameters());
        this.traverseContextPlaceholder(item.getContents());
        this.traverseProofPlaceholder(item.getWellDefinednessProof());
    }

    public <A> void traverseCases(CaseList<A> cases) throws Exception {
        for (CaseList.Case<A> item : cases) {
            this.traverseCase(item);
        }
    }

    public <A> void traverseCase(CaseList.Case<A> item) throws Exception {
        ContextPlaceholder<Formula> formula = item.getFormula();
        if (formula != null) {
            this.traverseFormulaPlaceholder(formula);
        }
        this.traverseContextPlaceholder(item.getContents());
        FixedPlaceholder<Proof> proof = item.getWellDefinednessProof();
        if (proof != null) {
            this.traverseProofPlaceholder(proof);
        }
    }

    public void traverseContextItemReference(ContextItemReference<?> reference) throws Exception {
        ContextItemReference.Binding binding = reference.getBinding();
        if (binding != null) {
            traverseBinding(binding);
        }
    }

    public void traverseBinding(ContextItemReference.Binding binding) throws Exception {
        ContextItemReference.Binding outerBinding = binding.getOuterBinding();
        if (outerBinding != null) {
            traverseBinding(outerBinding);
        }
        this.traverseElementTermPlaceholder(binding.getValue());
    }

    public <A> void traverseEquivalenceList(EquivalenceList<A> list) throws Exception {
        for (ContextPlaceholder<A> placeholder : list) {
            this.traverseContextPlaceholder(placeholder);
        }
        for (EquivalenceList.EquivalenceProof<A> proof : list.getEquivalenceProofs()) {
            this.traverseEquivalenceProof(proof);
        }
    }

    public <A> void traverseEquivalenceProof(EquivalenceList.EquivalenceProof<A> proof) throws Exception {
        this.traverseProofPlaceholder(proof);
    }

    public void traverseEmbedding(Construction.Embedding embedding) throws Exception {
        this.traverseElementParameter(embedding.getParameter());
        this.traverseMathObjectReference(embedding.getTarget(), null);
        this.traverseProofPlaceholder(embedding.getWellDefinednessProof());
    }

    public void traverseEqualitySpecification(Constructor.EqualitySpecification equalitySpecification) throws Exception {
        if (equalitySpecification instanceof Constructor.EqualityDefinition) {
            Constructor.EqualityDefinition equalityDefinition = (Constructor.EqualityDefinition) equalitySpecification;
            this.traverseEquivalenceList(equalityDefinition.getConditions());
            this.traverseProofPlaceholder(equalityDefinition.getReflexivityProof());
            this.traverseProofPlaceholder(equalityDefinition.getSymmetryProof());
            this.traverseProofPlaceholder(equalityDefinition.getTransitivityProof());
        } else if (equalitySpecification instanceof Constructor.IsomorphismDefinition) {
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseOperatorDefinition(Operator.Definition definition) throws Exception {
        if (definition instanceof Operator.ExplicitDefinition) {
            Operator.ExplicitDefinition explicitDefinition = (Operator.ExplicitDefinition) definition;
            this.traverseEquivalenceList(explicitDefinition.getTerms());
            FixedPlaceholder<OperatorSetRestriction> restrictionPlaceholder = explicitDefinition.getSetRestriction();
            OperatorSetRestriction restriction = this.enterPlaceholder(restrictionPlaceholder);
            if (restriction != null) {
                this.traverseOperatorSetRestriction(restriction);
            }
            this.exitPlaceholder(restrictionPlaceholder);
        } else if (definition instanceof Operator.ImplicitDefinition) {
            Operator.ImplicitDefinition implicitDefinition = (Operator.ImplicitDefinition) definition;
            this.traverseElementParameter(implicitDefinition.getParameter());
            this.traverseEquivalenceList(implicitDefinition.getConditions());
            this.traverseProofPlaceholder(implicitDefinition.getWellDefinednessProof());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void traverseOperatorSetRestriction(OperatorSetRestriction restriction) throws Exception {
        this.traverseSetTermPlaceholder(restriction.getSet());
        this.traverseProofPlaceholder(restriction.getProof());
    }

    public void traverseTheoremContents(Theorem.Contents contents) throws Exception {
        if (contents instanceof Theorem.StandardContents) {
            Theorem.StandardContents standardContents = (Theorem.StandardContents) contents;
            this.traverseFormulaPlaceholder(standardContents.getClaim());
            for (Proof proof : standardContents.getProofs()) {
                this.traverseProof(proof);
            }
        } else if (contents instanceof Theorem.EquivalenceContents) {
            this.traverseEquivalenceList(((Theorem.EquivalenceContents) contents).getConditions());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Substituter traverseMathObjectReference(MathObjectReference<?> reference, Substituter substituter) throws Exception {
        ArgumentList arguments = reference.getArguments();
        if (arguments != null) {
            substituter = this.traverseArguments(arguments, substituter);
        }
        return substituter;
    }

    public void traverseProofPlaceholder(FixedPlaceholder<Proof> placeholder) throws Exception {
        Proof proof = this.enterPlaceholder(placeholder);
        if (proof != null) {
            this.traverseProof(proof);
        }
        this.exitPlaceholder(placeholder);
    }

    public void traverseProof(Proof proof) throws Exception {
    }

    protected <A> A enterPlaceholder(GenericPlaceholder<A> placeholder) {
        return placeholder.get();
    }

    protected <A> void exitPlaceholder(GenericPlaceholder<A> placeholder) {
    }
}
