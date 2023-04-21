package net.sourceforge.hlm.xml.read;

import java.util.*;
import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.generic.exceptions.*;
import net.sourceforge.hlm.helpers.adder.*;
import net.sourceforge.hlm.library.*;
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
import net.sourceforge.hlm.semantics.*;
import net.sourceforge.hlm.semantics.clone.*;
import net.sourceforge.hlm.util.*;
import net.sourceforge.hlm.util.iterator.*;
import org.xml.sax.*;

public class MathHandler extends Handler {

    public MathHandler(MathObject object, RelationManager relationManager) throws Exception {
        if (relationManager.isObjectFinished(object)) {
            throw new XMLReadException(Translator.format("\"%s\" was included multiple times", object.getName().get()));
        }
        this.object = object;
        this.relationManager = relationManager;
    }

    @Override
    public Object startElement(String name, Attributes attr, Object current, Object placeholder) throws Exception {
        if (placeholder != null) {
            return this.startExpression((SelectablePlaceholder<?>) placeholder, name, attr);
        } else if (current == null) {
            return this.readMainObject(name, attr);
        } else if (current instanceof MathObject) {
            return this.readMathObject(name, attr, (MathObject) current);
        } else if (current instanceof Constructor.EqualitySpecification) {
            return this.readEqualitySpecification(name, attr, (Constructor.EqualitySpecification) current);
        } else if (current instanceof Constructor.RewriteRule) {
            return this.readRewriteRule(name, attr, (Constructor.RewriteRule) current);
        } else if (current instanceof Construction.Embedding) {
            return this.readEmbedding(name, attr, (Construction.Embedding) current);
        } else if (current instanceof Operator.ImplicitDefinition) {
            return this.readImplicitDefinition(name, attr, (Operator.ImplicitDefinition) current);
        } else if (current instanceof Theorem.Contents) {
            return this.readTheoremContents(name, attr, (Theorem.Contents) current);
        } else if (current instanceof ParameterList) {
            return this.readParameterList(name, attr, (ParameterList) current);
        } else if (current instanceof Parameter) {
            return this.readParameter(name, attr, (Parameter) current);
        } else if (current instanceof ElementParameter.ShortCut) {
            return this.readShortCut(name, attr, (ElementParameter.ShortCut) current);
        } else if (current instanceof ArgumentList) {
            return this.readArgumentList(name, attr, (ArgumentList) current);
        } else if (current instanceof Argument) {
            return this.readArgument(name, attr, (Argument) current);
        } else if (current instanceof MathObjectReference<?>) {
            return this.readMathObjectReference(name, attr, (MathObjectReference<?>) current);
        } else if (current instanceof Formula) {
            return this.readFormula(name, attr, (Formula) current);
        } else if (current instanceof SetTerm) {
            return this.readSetTerm(name, attr, (SetTerm) current);
        } else if (current instanceof ElementTerm) {
            return this.readElementTerm(name, attr, (ElementTerm) current);
        } else if (current instanceof SymbolTerm) {
            return this.readSymbolTerm(name, attr, (SymbolTerm) current);
        } else if (current instanceof ContextItemReference<?>) {
            return this.readContextItemReference(name, attr, (ContextItemReference<?>) current);
        } else if (current instanceof ContextItemReference.Binding) {
            return this.readBinding(name, attr, (ContextItemReference.Binding) current);
        } else if (current instanceof StructuralCaseList<?>) {
            return this.readStructuralCaseList(name, attr, (StructuralCaseList<?>) current);
        } else if (current instanceof StructuralCaseList.Case<?>) {
            return this.readStructuralCase(name, attr, (StructuralCaseList.Case<?>) current);
        } else if (current instanceof EquivalenceList<?>) {
            return this.readEquivalenceList(name, attr, (EquivalenceList<?>) current);
        } else if (current instanceof ProofWithParameters) {
            return this.readProofWithParameters(name, attr, (ProofWithParameters) current);
        } else if (current instanceof Proof) {
            return this.readProof(name, attr, (Proof) current);
        } else if (current instanceof ProofStep) {
            return this.readProofStep(name, attr, (ProofStep) current);
        }
        return null;
    }

    private Object readMainObject(String name, Attributes attr) throws Exception {
        if (this.object instanceof Construction) {
            if (!name.equals("construction")) {
                throw new XMLReadException(Translator.format("construction expected"));
            }
        } else if (this.object instanceof Predicate) {
            if (!name.equals("predicate")) {
                throw new XMLReadException(Translator.format("predicate expected"));
            }
        } else if (this.object instanceof Operator) {
            if (!name.equals("operator")) {
                throw new XMLReadException(Translator.format("operator expected"));
            }
        } else if (this.object instanceof SetOperator) {
            if (!name.equals("set-operator")) {
                throw new XMLReadException(Translator.format("set operator expected"));
            }
        } else if (this.object instanceof Theorem) {
            if (!name.equals("theorem")) {
                throw new XMLReadException(Translator.format("theorem expected"));
            }
        }
        String objectName = attr.getValue("name");
        if (objectName == null) {
            throw new XMLReadException(Translator.format("attribute 'name' missing"));
        }
        String expectedName = this.object.getName().get();
        if (!expectedName.equals(objectName)) {
            this.error(new XMLReadException(Translator.format("object name mismatch: expected \"%s\", got \"%s\"", expectedName, objectName)));
        }
        return this.object;
    }

    private Object readMathObject(String name, Attributes attr, MathObject object) throws Exception {
        if (name.equals("parameters")) {
            return object.getParameters();
        }
        if (object instanceof Construction) {
            return this.readConstruction(name, attr, (Construction) object);
        } else if (object instanceof Constructor) {
            return this.readConstructor(name, attr, (Constructor) object);
        } else if (object instanceof Predicate) {
            return this.readPredicate(name, attr, (Predicate) object);
        } else if (object instanceof Operator) {
            return this.readOperator(name, attr, (Operator) object);
        } else if (object instanceof SetOperator) {
            return this.readSetOperator(name, attr, (SetOperator) object);
        } else if (object instanceof Theorem) {
            return this.readTheorem(name, attr, (Theorem) object);
        }
        return null;
    }

    private Object readConstruction(String name, Attributes attr, Construction construction) throws Exception {
        if (name.equals("constructors")) {
            return construction;
        } else if (name.equals("constructor")) {
            String constructorName = attr.getValue("name");
            if (constructorName == null) {
                throw new XMLReadException(Translator.format("attribute 'name' missing"));
            }
            LibraryObjectList<Constructor> constructors = construction.getConstructors();
            Constructor constructor = constructors.find(constructorName);
            if (constructor == null) {
                constructor = constructors.add(Constructor.class, constructorName, null);
            } else {
                if (this.relationManager.isObjectFinished(constructor)) {
                    throw new XMLReadException(Translator.format("duplicate constructor \"%s\"", constructorName));
                }
                constructor.move(constructors, null);
            }
            return constructor;
        } else if (name.equals("embedding")) {
            return construction.getEmbedding().fill();
        }
        return null;
    }

    private Object readConstructor(String name, Attributes attr, Constructor constructor) throws Exception {
        if (name.equals("equality-def")) {
            return constructor.getEqualitySpecification().fill(Constructor.EqualityDefinition.class);
        } else if (name.equals("iso-equality-def")) {
            String path = attr.getValue("path");
            if (path == null) {
                throw new XMLReadException(Translator.format("attribute 'path' missing"));
            }
            SetOperator operator = this.relationManager.getObject(SetOperator.class, path);
            Constructor.IsomorphismDefinition definition = constructor.getEqualitySpecification().fill(Constructor.IsomorphismDefinition.class);
            definition.getBijectionSet().set(operator);
            return definition;
        } else if (name.equals("rewrite")) {
            String path = attr.getValue("path");
            if (path == null) {
                throw new XMLReadException(Translator.format("attribute 'path' missing"));
            }
            Theorem theorem = this.relationManager.getObject(Theorem.class, path);
            Constructor.RewriteRule rewriteRule = constructor.getRewriteRule().fill();
            rewriteRule.getTheorem().set(theorem);
            return rewriteRule;
        }
        return null;
    }

    private Object readEqualitySpecification(String name, Attributes attr, Constructor.EqualitySpecification specification) throws Exception {
        if (specification instanceof Constructor.EqualityDefinition) {
            return this.readEqualityDefinition(name, attr, (Constructor.EqualityDefinition) specification);
        } else if (specification instanceof Constructor.IsomorphismDefinition) {
            return this.readIsomorphismDefinition(name, attr, (Constructor.IsomorphismDefinition) specification);
        }
        return null;
    }

    private Object readEqualityDefinition(String name, Attributes attr, Constructor.EqualityDefinition definition) throws Exception {
        if (name.equals("conditions")) {
            return definition.getConditions();
        } else {
            if (name.equals("reflexivity")) {
                return new ProofWithParameters(definition.getReflexivityProof().fill());
            } else if (name.equals("symmetry")) {
                return new ProofWithParameters(definition.getSymmetryProof().fill());
            } else if (name.equals("transitivity")) {
                return new ProofWithParameters(definition.getTransitivityProof().fill());
            }
        }
        return null;
    }

    private Object readIsomorphismDefinition(String name, Attributes attr, Constructor.IsomorphismDefinition definition) throws Exception {
        Constructor constructor = this.findMostRecent(Constructor.class);
        if (name.equals("iso-transform")) {
            String parameterName = attr.getValue("name");
            if (parameterName == null) {
                throw new XMLReadException(Translator.format("attribute 'name' missing"));
            }
            Parameter parameter = constructor.getParameters().get(Parameter.class, parameterName, false);
            String path = attr.getValue("path");
            if (path == null) {
                throw new XMLReadException(Translator.format("attribute 'path' missing"));
            }
            if (parameter instanceof SubsetParameter) {
                SetOperator operator = this.relationManager.getObject(SetOperator.class, path);
                definition.getTransform((SubsetParameter) parameter).set(operator);
                return this;
            } else if (parameter instanceof ElementParameter) {
                Operator operator = this.relationManager.getObject(Operator.class, path);
                definition.getTransform((ElementParameter) parameter).set(operator);
                return this;
            }
        }
        return null;
    }

    private Object readRewriteRule(String name, Attributes attr, Constructor.RewriteRule rewriteRule) throws Exception {
        return this.startElementTerm(rewriteRule.getTerm(), name, attr);
    }

    private Object readEmbedding(String name, Attributes attr, Construction.Embedding embedding) throws Exception {
        if (name.equals("element-param")) {
            return this.fillParameter(embedding.getParameter(), attr);
        } else if (name.equals("embedding-target")) {
            Construction construction = this.findMostRecent(Construction.class);
            String constructorName = attr.getValue("name");
            if (constructorName == null) {
                throw new XMLReadException(Translator.format("attribute 'name' missing"));
            }
            MathObjectReference<Constructor> target = embedding.getTarget();
            target.set(construction.getConstructors().get(Constructor.class, constructorName));
            return target.getArguments();
        } else if (name.equals("well-definedness")) {
            return new ProofWithParameters(embedding.getWellDefinednessProof().fill());
        }
        return null;
    }

    private Object readPredicate(String name, Attributes attr, Predicate predicate) throws Exception {
        if (name.equals("conditions")) {
            return predicate.getConditions();
        }
        return null;
    }

    private Object readOperator(String name, Attributes attr, Operator operator) throws Exception {
        if (name.equals("terms")) {
            Operator.ExplicitDefinition definition = operator.getDefinition().fill(Operator.ExplicitDefinition.class);
            return definition.getTerms();
        } else if (name.equals("implicit")) {
            return operator.getDefinition().fill(Operator.ImplicitDefinition.class);
        }
        return null;
    }

    private Object readImplicitDefinition(String name, Attributes attr, Operator.ImplicitDefinition definition) throws Exception {
        if (name.equals("element-param")) {
            return this.fillParameter(definition.getParameter(), attr);
        } else if (name.equals("conditions")) {
            return definition.getConditions();
        } else if (name.equals("well-definedness")) {
            return definition.getWellDefinednessProof().fill();
        }
        return null;
    }

    private Object readSetOperator(String name, Attributes attr, SetOperator operator) throws Exception {
        if (name.equals("set-terms")) {
            return operator.getTerms();
        }
        return null;
    }

    private Object readTheorem(String name, Attributes attr, Theorem theorem) throws Exception {
        if (name.equals("claim")) {
            Theorem.StandardContents contents = theorem.getContents().fill(Theorem.StandardContents.class);
            this.addPlaceholder(contents.getClaim());
            return contents;
        } else if (name.equals("equivalence")) {
            return theorem.getContents().fill(Theorem.EquivalenceContents.class);
        } else if (name.equals("proof")) {
            Theorem.Contents contents = theorem.getContents().get();
            if (contents instanceof Theorem.StandardContents) {
                FixedList<Proof> proofs = ((Theorem.StandardContents) contents).getProofs();
                return proofs.add(null);
            }
        }
        return null;
    }

    private Object readTheoremContents(String name, Attributes attr, Theorem.Contents contents) throws Exception {
        if (contents instanceof Theorem.EquivalenceContents) {
            return this.readEquivalenceList(name, attr, ((Theorem.EquivalenceContents) contents).getConditions());
        }
        return null;
    }

    private Object readParameterList(String name, Attributes attr, ParameterList parameters) throws Exception {
        String parameterName = attr.getValue("name");
        if (parameterName == null) {
            throw new XMLReadException(Translator.format("attribute 'name' missing"));
        }
        Class<? extends Parameter> parameterClass;
        if (name.equals("set-param")) {
            parameterClass = ArbitrarySetParameter.class;
        } else if (name.equals("subset-param")) {
            parameterClass = SubsetParameter.class;
        } else if (name.equals("element-param")) {
            parameterClass = ElementParameter.class;
        } else if (name.equals("constraint-param")) {
            parameterClass = ConstraintParameter.class;
        } else if (name.equals("binding-param")) {
            parameterClass = BindingParameter.class;
        } else if (name.equals("symbol-param")) {
            parameterClass = SymbolParameter.class;
        } else if (name.equals("define")) {
            parameterClass = ElementDefinition.class;
        } else if (name.equals("set-define")) {
            parameterClass = SetDefinition.class;
        } else if (name.equals("symbol-define")) {
            parameterClass = SymbolDefinition.class;
        } else {
            return null;
        }
        Parameter parameter = parameters.find(parameterClass, parameterName, false);
        if (parameter == null) {
            parameter = parameters.add(parameterClass, null);
            parameter.getName().set(parameterName);
        } else {
            parameters.move(parameter, null);
        }
        parameter.setAuto("yes".equals(attr.getValue("auto")));
        if (parameterClass == BindingParameter.class) {
            this.addPlaceholder(((BindingParameter) parameter).getSet());
        }
        return parameter;
    }

    private Object fillParameter(Parameter parameter, Attributes attr) throws Exception {
        String parameterName = attr.getValue("name");
        if (parameterName == null) {
            throw new XMLReadException(Translator.format("attribute 'name' missing"));
        }
        parameter.getName().set(parameterName);
        return parameter;
    }

    private Object readParameter(String name, Attributes attr, Parameter parameter) throws Exception {
        if (parameter instanceof SubsetParameter) {
            return this.readSubsetParameter(name, attr, (SubsetParameter) parameter);
        } else if (parameter instanceof ElementParameter) {
            return this.readElementParameter(name, attr, (ElementParameter) parameter);
        } else if (parameter instanceof ConstraintParameter) {
            return this.readConstraintParameter(name, attr, (ConstraintParameter) parameter);
        } else if (parameter instanceof BindingParameter) {
            return this.readBindingParameter(name, attr, (BindingParameter) parameter);
        } else if (parameter instanceof SetDefinition) {
            return this.readSetDefinition(name, attr, (SetDefinition) parameter);
        } else if (parameter instanceof ElementDefinition) {
            return this.readElementDefinition(name, attr, (ElementDefinition) parameter);
        } else if (parameter instanceof SymbolDefinition) {
            return this.readSymbolDefinition(name, attr, (SymbolDefinition) parameter);
        }
        return null;
    }

    private Object readSubsetParameter(String name, Attributes attr, SubsetParameter parameter) throws Exception {
        return this.startSetTerm(parameter.getSuperset(), name, attr);
    }

    private Object readElementParameter(String name, Attributes attr, ElementParameter parameter) throws Exception {
        if (name.equals("shortcut")) {
            return this.fillShortCut(parameter.getShortCut(), attr);
        } else {
            return this.startSetTerm(parameter.getSet(), name, attr);
        }
    }

    private Object fillShortCut(FixedPlaceholder<ElementParameter.ShortCut> placeholder, Attributes attr) throws Exception {
        String constructorName = attr.getValue("name");
        if (constructorName == null) {
            throw new XMLReadException(Translator.format("attribute 'name' missing"));
        }
        ElementParameter.ShortCut shortCut = placeholder.fill();
        shortCut.setRewrite("yes".equals(attr.getValue("rewrite")));
        shortCut.setOverride("yes".equals(attr.getValue("override")));
        this.constructorNames.push(constructorName);
        return shortCut;
    }

    private Object readShortCut(String name, Attributes attr, ElementParameter.ShortCut shortCut) throws Exception {
        if (name.equals("construction-ref")) {
            return this.fillSectionObjectReference(Construction.class, shortCut.getConstruction(), attr);
        } else {
            this.fillConstructorReference(shortCut.getConstructor(), shortCut.getConstruction());
            return this.redirect(name, attr, shortCut.getParameters());
        }
    }

    private void fillConstructorReference(Reference<Constructor> reference, MathObjectReference<Construction> construction) throws Exception {
        if (reference.get() == null) {
            reference.set(this.findConstructor(construction.get(), this.constructorNames.peek()));
        }
    }

    private Object readConstraintParameter(String name, Attributes attr, ConstraintParameter parameter) throws Exception {
        return this.startFormula(parameter.getFormula(), name, attr);
    }

    private Object readBindingParameter(String name, Attributes attr, BindingParameter parameter) throws Exception {
        if (name.equals("shortcut")) {
            return this.fillShortCut(parameter.getElementParameter().getShortCut(), attr);
        } else {
            return this.redirect(name, attr, parameter.getBoundParameters());
        }
    }

    private Object readSetDefinition(String name, Attributes attr, SetDefinition definition) throws Exception {
        return this.startSetTerm(definition.getTerm(), name, attr);
    }

    private Object readElementDefinition(String name, Attributes attr, ElementDefinition definition) throws Exception {
        return this.startElementTerm(definition.getTerm(), name, attr);
    }

    private Object readSymbolDefinition(String name, Attributes attr, SymbolDefinition definition) throws Exception {
        return this.startSymbolTerm(definition.getTerm(), name, attr);
    }

    private Object readArgumentList(String name, Attributes attr, ArgumentList arguments) throws Exception {
        String parameterName = attr.getValue("name");
        if (parameterName == null) {
            throw new XMLReadException(Translator.format("attribute 'name' missing"));
        }
        Class<? extends Parameter> parameterClass;
        if (name.equals("set-arg")) {
            parameterClass = ArbitrarySetParameter.class;
        } else if (name.equals("subset-arg")) {
            parameterClass = SubsetParameter.class;
        } else if (name.equals("element-arg")) {
            parameterClass = ElementParameter.class;
        } else if (name.equals("constraint-arg")) {
            parameterClass = ConstraintParameter.class;
        } else if (name.equals("binding-arg")) {
            parameterClass = BindingParameter.class;
        } else if (name.equals("symbol-arg")) {
            parameterClass = SymbolParameter.class;
        } else {
            return null;
        }
        ParameterList parameters = arguments.getParameters();
        Parameter parameter = parameters.find(parameterClass, parameterName, false);
        if (parameter == null) {
            MathObjectReference<?> reference = null;
            for (Object object : this.getObjects()) {
                if (object instanceof MathObjectReference<?>) {
                    reference = (MathObjectReference<?>) object;
                } else if (!(object instanceof ArgumentList || object instanceof BindingArgument)) {
                    break;
                }
            }
            if (reference == null || this.relationManager.isObjectFinished(reference.get())) {
                throw new ItemNotFoundException(Translator.format("parameter \"%s\" not found", parameterName));
            } else {
                parameter = parameters.add(parameterClass, null);
                parameter.getName().set(parameterName);
            }
        }
        return arguments.add(parameter, null);
    }

    private Object readArgument(String name, Attributes attr, Argument argument) throws Exception {
        if (argument instanceof ArbitrarySetArgument) {
            return this.readArbitrarySetArgument(name, attr, (ArbitrarySetArgument) argument);
        } else if (argument instanceof SubsetArgument) {
            return this.readSubsetArgument(name, attr, (SubsetArgument) argument);
        } else if (argument instanceof ElementArgument) {
            return this.readElementArgument(name, attr, (ElementArgument) argument);
        } else if (argument instanceof ConstraintArgument) {
            return this.readConstraintArgument(name, attr, (ConstraintArgument) argument);
        } else if (argument instanceof BindingArgument) {
            return this.readBindingArgument(name, attr, (BindingArgument) argument);
        } else if (argument instanceof SymbolArgument) {
            return this.readSymbolArgument(name, attr, (SymbolArgument) argument);
        }
        return null;
    }

    private Object readArbitrarySetArgument(String name, Attributes attr, ArbitrarySetArgument argument) throws Exception {
        return this.startSetTerm(argument.getTerm(), name, attr);
    }

    private Object readSubsetArgument(String name, Attributes attr, SubsetArgument argument) throws Exception {
        if (name.equals("well-definedness")) {
            return argument.getSubsetProof().fill();
        } else {
            return this.startSetTerm(argument.getTerm(), name, attr);
        }
    }

    private Object readElementArgument(String name, Attributes attr, ElementArgument argument) throws Exception {
        if (name.equals("well-definedness")) {
            return argument.getElementProof().fill();
        } else {
            return this.startElementTerm(argument.getTerm(), name, attr);
        }
    }

    private Object readConstraintArgument(String name, Attributes attr, ConstraintArgument argument) throws Exception {
        return argument.getProof().fill();
    }

    private Object readBindingArgument(String name, Attributes attr, BindingArgument argument) throws Exception {
        if (name.equals("element-param")) {
            return this.fillParameter(argument.getParameter(), attr);
        } else {
            return this.redirect(name, attr, argument.getBoundArguments());
        }
    }

    private Object readSymbolArgument(String name, Attributes attr, SymbolArgument argument) throws Exception {
        return this.startSymbolTerm(argument.getTerm(), name, attr);
    }

    private <A extends SectionObject> Object fillSectionObjectReference(Class<A> type, Reference<A> reference, Attributes attr) throws Exception {
        String path = attr.getValue("path");
        if (path == null) {
            throw new XMLReadException(Translator.format("attribute 'path' missing"));
        }
        try {
            reference.set(this.relationManager.getObject(type, path));
        } catch (ItemNotFoundException e) {
            this.error(new XMLReadException(e));
        }
        return reference;
    }

    private Object readMathObjectReference(String name, Attributes attr, MathObjectReference<?> reference) throws Exception {
        return this.redirect(name, attr, reference.getArguments());
    }

    private Object startFormula(SelectablePlaceholder<Formula> placeholder, String name, Attributes attr) throws Exception {
        Formula formula;
        if (name.equals("empty")) {
            return this;
        } else if (name.equals("and")) {
            formula = placeholder.fill(ConjunctionFormula.class);
        } else if (name.equals("or")) {
            formula = placeholder.fill(DisjunctionFormula.class);
        } else if (name.equals("forall")) {
            formula = placeholder.fill(ForAllFormula.class);
        } else if (name.equals("exists")) {
            formula = placeholder.fill(ExistsFormula.class);
        } else if (name.equals("exists-unique")) {
            formula = placeholder.fill(ExistsUniqueFormula.class);
        } else if (name.equals("in")) {
            formula = placeholder.fill(ElementFormula.class);
        } else if (name.equals("sub")) {
            formula = placeholder.fill(SubsetFormula.class);
        } else if (name.equals("equals")) {
            formula = placeholder.fill(EqualityFormula.class);
        } else if (name.equals("set-equals")) {
            formula = placeholder.fill(SetEqualityFormula.class);
        } else if (name.equals("structural")) {
            StructuralFormula structuralFormula = placeholder.fill(StructuralFormula.class);
            this.fillStructuralCaseList(structuralFormula.getCases(), attr);
            formula = structuralFormula;
        } else if (name.equals("predicate-ref")) {
            PredicateFormula predicateFormula = placeholder.fill(PredicateFormula.class);
            this.fillSectionObjectReference(Predicate.class, predicateFormula.getPredicate(), attr);
            formula = predicateFormula;
        } else {
            return null;
        }
        String negated = attr.getValue("negated");
        if (negated != null) {
            int negationCount = Integer.parseInt(negated);
            if (negationCount < 0) {
                throw new InvalidValueException(Translator.format("negation count cannot be negative"));
            }
            formula.setNegationCount(negationCount);
        }
        if (formula instanceof QuantifiedFormula) {
            ((QuantifiedFormula) formula).setHasFormula(false);
        } else if (formula instanceof RelationFormula<?, ?>) {
            RelationFormula<?, ?> relationFormula = (RelationFormula<?, ?>) formula;
            this.addPlaceholder(relationFormula.getLeftTerm());
            this.addPlaceholder(relationFormula.getRightTerm());
        }
        return formula;
    }

    private Object readFormula(String name, Attributes attr, Formula formula) throws Exception {
        if (formula instanceof EnumerationFormula) {
            return this.readEnumerationFormula(name, attr, (EnumerationFormula) formula);
        } else if (formula instanceof QuantifiedFormula) {
            return this.readQuantifiedFormula(name, attr, (QuantifiedFormula) formula);
        } else if (formula instanceof StructuralFormula) {
            return this.readStructuralFormula(name, attr, (StructuralFormula) formula);
        } else if (formula instanceof PredicateFormula) {
            return this.readPredicateFormula(name, attr, (PredicateFormula) formula);
        }
        return null;
    }

    private Object readEnumerationFormula(String name, Attributes attr, EnumerationFormula formula) throws Exception {
        return this.startFormula(formula.getFormulae().add(null), name, attr);
    }

    private Object readQuantifiedFormula(String name, Attributes attr, QuantifiedFormula formula) throws Exception {
        if (name.equals("parameters")) {
            return formula.getParameters();
        } else {
            formula.setHasFormula(true);
            return this.startFormula(formula.getFormula(), name, attr);
        }
    }

    private Object readStructuralFormula(String name, Attributes attr, StructuralFormula formula) throws Exception {
        return this.redirect(name, attr, formula.getCases());
    }

    private Object readPredicateFormula(String name, Attributes attr, PredicateFormula formula) throws Exception {
        return this.redirect(name, attr, formula.getPredicate());
    }

    private Object startSetTerm(SelectablePlaceholder<SetTerm> placeholder, String name, Attributes attr) throws Exception {
        if (name.equals("empty")) {
            return this;
        } else if (name.equals("set-var-ref")) {
            VariableSetTerm term = placeholder.fill(VariableSetTerm.class);
            this.fillContextItemReference(term.getVariable(), attr);
            return term;
        } else if (name.equals("construction-ref")) {
            ConstructionTerm term = placeholder.fill(ConstructionTerm.class);
            this.fillSectionObjectReference(Construction.class, term.getConstruction(), attr);
            return term;
        } else if (name.equals("enumeration")) {
            return placeholder.fill(EnumerationTerm.class);
        } else if (name.equals("subset")) {
            return placeholder.fill(SubsetTerm.class);
        } else if (name.equals("extended-subset")) {
            return placeholder.fill(ExtendedSubsetTerm.class);
        } else if (name.equals("set-structural-cases")) {
            StructuralSetTerm term = placeholder.fill(StructuralSetTerm.class);
            this.fillStructuralCaseList(term.getCases(), attr);
            return term;
        } else if (name.equals("set-operator-ref")) {
            SetOperatorTerm term = placeholder.fill(SetOperatorTerm.class);
            this.fillSectionObjectReference(SetOperator.class, term.getOperator(), attr);
            return term;
        } else if (name.equals("set-previous")) {
            return placeholder.fill(PreviousSetTerm.class);
        }
        return null;
    }

    private Object readSetTerm(String name, Attributes attr, SetTerm term) throws Exception {
        if (term instanceof VariableSetTerm) {
            return this.readSetVariableTerm(name, attr, (VariableSetTerm) term);
        } else if (term instanceof ConstructionTerm) {
            return this.readConstructionTerm(name, attr, (ConstructionTerm) term);
        } else if (term instanceof EnumerationTerm) {
            return this.readEnumerationTerm(name, attr, (EnumerationTerm) term);
        } else if (term instanceof SubsetTerm) {
            return this.readSubsetTerm(name, attr, (SubsetTerm) term);
        } else if (term instanceof ExtendedSubsetTerm) {
            return this.readExtendedSubsetTerm(name, attr, (ExtendedSubsetTerm) term);
        } else if (term instanceof StructuralSetTerm) {
            return this.readStructuralSetTerm(name, attr, (StructuralSetTerm) term);
        } else if (term instanceof SetOperatorTerm) {
            return this.readSetOperatorTerm(name, attr, (SetOperatorTerm) term);
        }
        return null;
    }

    private Object readSetVariableTerm(String name, Attributes attr, VariableSetTerm term) throws Exception {
        return this.redirect(name, attr, term.getVariable());
    }

    private Object readConstructionTerm(String name, Attributes attr, ConstructionTerm term) throws Exception {
        return this.redirect(name, attr, term.getConstruction());
    }

    private Object readEnumerationTerm(String name, Attributes attr, EnumerationTerm term) throws Exception {
        return this.startElementTerm(new ContextListAdder<ElementTerm>(term.getTerms()), name, attr);
    }

    private Object readSubsetTerm(String name, Attributes attr, SubsetTerm term) throws Exception {
        if (name.equals("element-param")) {
            return this.fillParameter(term.getParameter(), attr);
        } else {
            return this.startFormula(term.getFormula(), name, attr);
        }
    }

    private Object readExtendedSubsetTerm(String name, Attributes attr, ExtendedSubsetTerm term) throws Exception {
        if (name.equals("parameters")) {
            return term.getParameters();
        } else {
            return this.startElementTerm(term.getTerm(), name, attr);
        }
    }

    private Object readStructuralSetTerm(String name, Attributes attr, StructuralSetTerm term) throws Exception {
        return this.redirect(name, attr, term.getCases());
    }

    private Object readSetOperatorTerm(String name, Attributes attr, SetOperatorTerm term) throws Exception {
        return this.redirect(name, attr, term.getOperator());
    }

    private Object startElementTerm(SelectablePlaceholder<ElementTerm> placeholder, String name, Attributes attr) throws Exception {
        if (name.equals("empty")) {
            return this;
        } else if (name.equals("var-ref")) {
            VariableElementTerm term = placeholder.fill(VariableElementTerm.class);
            this.fillContextItemReference(term.getVariable(), attr);
            return term;
        } else if (name.equals("constructor-ref")) {
            String constructorName = attr.getValue("name");
            if (constructorName == null) {
                throw new XMLReadException(Translator.format("attribute 'name' missing"));
            }
            ConstructorTerm term = placeholder.fill(ConstructorTerm.class);
            this.constructorNames.push(constructorName);
            return term;
        } else if (name.equals("cases")) {
            return placeholder.fill(CaseElementTerm.class);
        } else if (name.equals("structural-cases")) {
            StructuralElementTerm term = placeholder.fill(StructuralElementTerm.class);
            this.fillStructuralCaseList(term.getCases(), attr);
            return term;
        } else if (name.equals("operator-ref")) {
            OperatorTerm term = placeholder.fill(OperatorTerm.class);
            this.fillSectionObjectReference(Operator.class, term.getOperator(), attr);
            return term;
        } else if (name.equals("previous")) {
            return placeholder.fill(PreviousElementTerm.class);
        }
        return null;
    }

    private Object readElementTerm(String name, Attributes attr, ElementTerm term) throws Exception {
        if (term instanceof VariableElementTerm) {
            return this.readElementVariableTerm(name, attr, (VariableElementTerm) term);
        } else if (term instanceof ConstructorTerm) {
            return this.readConstructorTerm(name, attr, (ConstructorTerm) term);
        } else if (term instanceof CaseElementTerm) {
            return this.readCaseElementTerm(name, attr, (CaseElementTerm) term);
        } else if (term instanceof StructuralElementTerm) {
            return this.readStructuralElementTerm(name, attr, (StructuralElementTerm) term);
        } else if (term instanceof OperatorTerm) {
            return this.readOperatorTerm(name, attr, (OperatorTerm) term);
        }
        return null;
    }

    private Object readElementVariableTerm(String name, Attributes attr, VariableElementTerm term) throws Exception {
        return this.redirect(name, attr, term.getVariable());
    }

    private Object readConstructorTerm(String name, Attributes attr, ConstructorTerm term) throws Exception {
        if (name.equals("construction-ref")) {
            return this.fillSectionObjectReference(Construction.class, term.getConstruction(), attr);
        } else {
            this.fillConstructorReference(term.getConstructor(), term.getConstruction());
            return this.redirect(name, attr, term.getConstructor());
        }
    }

    private Object readCaseElementTerm(String name, Attributes attr, CaseElementTerm term) throws Exception {
        CaseList<ElementTerm> cases = term.getCases();
        int count = cases.getCount();
        if (count > 2 && name.equals("well-definedness")) {
            return cases.get(count - 2).getWellDefinednessProof().fill();
        }
        this.placeholders.set(this.placeholders.size() - 1, new SingleItemIterator<Object>(new CaseListAdder(cases)));
        return this.startElementTerm(cases.get(count - 1).getContents(), name, attr);
    }

    private Object readStructuralElementTerm(String name, Attributes attr, StructuralElementTerm term) throws Exception {
        return this.redirect(name, attr, term.getCases());
    }

    private Object readOperatorTerm(String name, Attributes attr, OperatorTerm term) throws Exception {
        return this.redirect(name, attr, term.getOperator());
    }

    private Object startSymbolTerm(SelectablePlaceholder<SymbolTerm> placeholder, String name, Attributes attr) throws Exception {
        if (name.equals("empty")) {
            return this;
        } else if (name.equals("symbol-var-ref")) {
            VariableSymbolTerm term = placeholder.fill(VariableSymbolTerm.class);
            this.fillContextItemReference(term.getVariable(), attr);
            return term;
        }
        return null;
    }

    private Object readSymbolTerm(String name, Attributes attr, SymbolTerm term) throws Exception {
        if (term instanceof VariableSymbolTerm) {
            return this.readSymbolVariableTerm(name, attr, (VariableSymbolTerm) term);
        }
        return null;
    }

    private Object readSymbolVariableTerm(String name, Attributes attr, VariableSymbolTerm term) throws Exception {
        return this.redirect(name, attr, term.getVariable());
    }

    private Object startExpression(SelectablePlaceholder<?> placeholder, String name, Attributes attr) throws Exception {
        Class<?> type = placeholder.getType();
        if (type == Formula.class) {
            return this.startFormula((SelectablePlaceholder<Formula>) placeholder, name, attr);
        } else if (type == SetTerm.class) {
            return this.startSetTerm((SelectablePlaceholder<SetTerm>) placeholder, name, attr);
        } else if (type == ElementTerm.class) {
            return this.startElementTerm((SelectablePlaceholder<ElementTerm>) placeholder, name, attr);
        } else if (type == SymbolTerm.class) {
            return this.startSymbolTerm((SelectablePlaceholder<SymbolTerm>) placeholder, name, attr);
        }
        return null;
    }

    private <A extends ContextItem> void fillContextItemReference(ContextItemReference<A> reference, Attributes attr) throws Exception {
        String variableName = attr.getValue("name");
        if (variableName == null) {
            throw new XMLReadException(Translator.format("attribute 'name' missing"));
        }
        try {
            reference.set(reference.getContext().get(variableName));
        } catch (ItemNotFoundException e) {
            this.error(new XMLReadException(e));
        }
    }

    private Object readContextItemReference(String name, Attributes attr, ContextItemReference<?> reference) throws Exception {
        return this.redirect(name, attr, reference.getBinding());
    }

    private Object readBinding(String name, Attributes attr, ContextItemReference.Binding binding) throws Exception {
        if (binding != null && binding.getValue().get() == null) {
            ContextItemReference.Binding outerBinding;
            while ((outerBinding = binding.getOuterBinding()) != null && outerBinding.getValue().get() == null) {
                binding = outerBinding;
            }
            return this.startElementTerm(binding.getValue(), name, attr);
        }
        return null;
    }

    private void fillStructuralCaseList(StructuralCaseList<?> structuralCases, Attributes attr) throws Exception {
        String path = attr.getValue("path");
        if (path == null) {
            throw new XMLReadException(Translator.format("attribute 'path' missing"));
        }
        Construction construction = this.relationManager.getObject(Construction.class, path);
        structuralCases.getConstruction().set(construction);
    }

    private Object readStructuralCaseList(String name, Attributes attr, StructuralCaseList<?> structuralCases) throws Exception {
        if (name.equals("structural-item") || name.equals("set-structural-case") || name.equals("structural-case")) {
            String constructorName = attr.getValue("name");
            if (constructorName == null) {
                throw new XMLReadException(Translator.format("attribute 'name' missing"));
            }
            Constructor constructor = this.findConstructor(structuralCases.getConstruction().get(), constructorName);
            StructuralCaseList.Case<?> item = structuralCases.add(constructor, null);
            item.setRewrite("yes".equals(attr.getValue("rewrite")));
            return item;
        } else {
            return this.startElementTerm(structuralCases.getSample(), name, attr);
        }
    }

    private <A> Object readStructuralCase(String name, Attributes attr, StructuralCaseList.Case<A> item) throws Exception {
        if (name.equals("parameters")) {
            return item.getParameters();
        } else if (name.equals("well-definedness")) {
            return item.getWellDefinednessProof().fill();
        } else {
            return this.startExpression(item.getContents(), name, attr);
        }
    }

    private <A> Object readEquivalenceList(String name, Attributes attr, EquivalenceList<A> list) throws Exception {
        if (name.equals("equivalence-proof") || name.equals("subset-proof") || name.equals("equality-proof")) {
            EquivalenceList.EquivalenceProof<A> equivalenceProof = list.getEquivalenceProofs().add(null);
            A source = this.getEquivalenceListItem(list, attr.getValue("from"));
            A target = this.getEquivalenceListItem(list, attr.getValue("to"));
            equivalenceProof.getSource().set(source);
            equivalenceProof.getTarget().set(target);
            Proof proof = equivalenceProof.fill();
            if (list.getContentType() == Formula.class) {
                ParameterList parameters = proof.getParameters();
                ConstraintParameter premise = parameters.add(ConstraintParameter.class, null);
                premise.getName().set("@");
                ObjectCloner.getInstance().cloneFormula((Formula) source, premise.getFormula());
            }
            return proof;
        } else {
            return this.startExpression(list.add(null), name, attr);
        }
    }

    private <A> A getEquivalenceListItem(EquivalenceList<A> list, String indexString) throws Exception {
        int index = this.parseIndex(indexString);
        for (Indirection<A> item : list) {
            if (index-- == 0) {
                return item.get();
            }
        }
        return null;
    }

    private Object readProofWithParameters(String name, Attributes attr, ProofWithParameters proofWithParameters) throws Exception {
        Proof proof = proofWithParameters.proof;
        if (name.equals("parameters")) {
            ParameterList parameters = proof.getParameters();
            parameters.clear();
            return parameters;
        } else {
            return this.redirect(name, attr, proof);
        }
    }

    private Object readProof(String name, Attributes attr, Proof proof) throws Exception {
        ContextList<ProofStep> steps = proof.getSteps();
        if (steps.isEmpty() && name.equals("goal")) {
            this.addPlaceholder(proof.getExplicitGoal());
            return this;
        } else {
            return this.startProofStep(new ContextListAdder<ProofStep>(steps), name, attr);
        }
    }

    private ProofStep startProofStep(SelectablePlaceholder<ProofStep> placeholder, String name, Attributes attr) throws Exception {
        ProofStep result = this.startIndependentIntermediateStep(placeholder, name, attr);
        if (result != null) {
            return result;
        }
        result = this.startDependentIntermediateStep(placeholder, name, attr);
        if (result != null) {
            return result;
        }
        result = this.startIndependentFinalStep(placeholder, name, attr);
        if (result != null) {
            return result;
        }
        result = this.startDependentFinalStep(placeholder, name, attr);
        if (result != null) {
            return result;
        }
        return this.startDefinitionStep(placeholder, name, attr);
    }

    private IndependentIntermediateStep startIndependentIntermediateStep(SelectablePlaceholder<? super IndependentIntermediateStep> placeholder, String name, Attributes attr) throws Exception {
        IndependentIntermediateStep result;
        if (name.equals("repeat")) {
            RepeatStep step = placeholder.fill(RepeatStep.class);
            String itemName = attr.getValue("name");
            if (itemName == null) {
                throw new XMLReadException(Translator.format("attribute 'name' missing"));
            }
            ContextItemReference<ContextItem> item = step.getItem();
            item.set(item.getContext().get(itemName));
            return step;
        } else if (name.equals("state")) {
            result = placeholder.fill(StateFormulaStep.class);
        } else if (name.equals("embed")) {
            EmbedStep step = placeholder.fill(EmbedStep.class);
            String path = attr.getValue("path");
            if (path == null) {
                throw new XMLReadException(Translator.format("attribute 'path' missing"));
            }
            step.getConstruction().set(this.relationManager.getObject(Construction.class, path));
            this.addPlaceholder(step.getInput());
            this.addPlaceholder(step.getOutput());
            result = step;
        } else if (name.equals("theorem-ref")) {
            TheoremStep step = placeholder.fill(TheoremStep.class);
            this.fillSectionObjectReference(Theorem.class, step.getTheorem(), attr);
            result = step;
        } else {
            return null;
        }
        this.fillOptionalName(result.getName(), attr);
        return result;
    }

    private DependentIntermediateStep startDependentIntermediateStep(SelectablePlaceholder<? super DependentIntermediateStep> placeholder, String name, Attributes attr) throws Exception {
        DependentIntermediateStep result;
        if (name.equals("use-def")) {
            result = placeholder.fill(UseDefinitionStep.class);
        } else if (name.equals("use-forall")) {
            result = placeholder.fill(UseForAllStep.class);
        } else if (name.equals("extend")) {
            ExtendStep step = placeholder.fill(ExtendStep.class);
            this.addPlaceholder(step.getTerm());
            result = step;
        } else if (name.equals("set-extend")) {
            SetExtendStep step = placeholder.fill(SetExtendStep.class);
            this.addPlaceholder(step.getTerm());
            result = step;
        } else if (name.equals("substitute")) {
            SubstituteStep step = placeholder.fill(SubstituteStep.class);
            this.fillRelationSide(step.getRelationSide(), attr);
            result = step;
        } else if (name.equals("resolve")) {
            result = placeholder.fill(ResolveStep.class);
        } else {
            return null;
        }
        this.fillOptionalName(result.getName(), attr);
        return result;
    }

    private IndependentFinalStep startIndependentFinalStep(SelectablePlaceholder<? super IndependentFinalStep> placeholder, String name, Attributes attr) throws Exception {
        if (name.equals("prove-def")) {
            ProveDefinitionStep step = placeholder.fill(ProveDefinitionStep.class);
            this.fillRelationSide(step.getRelationSide(), attr);
            return step;
        } else if (name.equals("prove-neg")) {
            ProveNegatedStep step = placeholder.fill(ProveNegatedStep.class);
            Proof subProof = step.getSubProof().fill();
            ConstraintParameter parameter = subProof.getParameters().add(ConstraintParameter.class, null);
            String parameterName = attr.getValue("name");
            if (parameterName != null) {
                parameter.getName().set(parameterName);
            }
            FormulaSemantics.negate(this.findMostRecent(Proof.class).getGoal(), parameter.getFormula());
            return step;
        } else if (name.equals("prove-forall")) {
            ProveForAllStep step = placeholder.fill(ProveForAllStep.class);
            ForAllFormula goal = (ForAllFormula) this.findMostRecent(Proof.class).getGoal();
            ObjectCloner.getInstance().copyParameterList(goal.getParameters(), step.getSubProof().fill().getParameters(), null);
            return step;
        } else if (name.equals("prove-exists")) {
            return placeholder.fill(ProveExistsStep.class);
        } else if (name.equals("prove-set-equals")) {
            return placeholder.fill(ProveSetEqualityStep.class);
        } else if (name.equals("prove-cases")) {
            ProveCasesStep step = placeholder.fill(ProveCasesStep.class);
            this.fillRelationSide(step.getRelationSide(), attr);
            StructuralCaseList<?> structuralCases = ProofSemantics.getCaseList(step);
            if (structuralCases == null) {
                return null;
            }
            step.getConstruction().set(structuralCases.getConstruction().get());
            return step;
        }
        return null;
    }

    private DependentFinalStep startDependentFinalStep(SelectablePlaceholder<? super DependentFinalStep> placeholder, String name, Attributes attr) throws Exception {
        if (name.equals("use-or")) {
            return placeholder.fill(UseDisjunctionStep.class);
        } else if (name.equals("use-cases")) {
            UseCasesStep step = placeholder.fill(UseCasesStep.class);
            this.fillRelationSide(step.getRelationSide(), attr);
            StructuralCaseList<?> structuralCases = ProofSemantics.getCaseList(step);
            if (structuralCases == null) {
                return null;
            }
            step.getConstruction().set(structuralCases.getConstruction().get());
            return step;
        } else {
            return null;
        }
    }

    private VariableDefinition<?> startDefinitionStep(SelectablePlaceholder<? super VariableDefinition<?>> placeholder, String name, Attributes attr) throws Exception {
        VariableDefinition<?> result;
        if (name.equals("define")) {
            result = placeholder.fill(ElementDefinition.class);
        } else if (name.equals("set-define")) {
            result = placeholder.fill(SetDefinition.class);
        } else if (name.equals("symbol-define")) {
            result = placeholder.fill(SymbolDefinition.class);
        } else {
            return null;
        }
        String variableName = attr.getValue("name");
        if (variableName == null) {
            throw new XMLReadException(Translator.format("attribute 'name' missing"));
        }
        result.getName().set(variableName);
        return result;
    }

    private void fillOptionalName(Reference<String> name, Attributes attr) throws Exception {
        String nameString = attr.getValue("name");
        if (nameString != null) {
            name.set(nameString);
        }
    }

    private void fillRelationSide(Reference<RelationSide> side, Attributes attr) throws Exception {
        String sideString = attr.getValue("side");
        if (sideString != null) {
            if (sideString.equals("left")) {
                side.set(RelationSide.LEFT);
            } else if (sideString.equals("right")) {
                side.set(RelationSide.RIGHT);
            } else {
                throw new XMLReadException(Translator.format("attribute 'side' must be \"left\" or \"right\""));
            }
        }
    }

    private Object readProofStep(String name, Attributes attr, ProofStep step) throws Exception {
        if (step instanceof IntermediateStep) {
            return this.readIntermediateStep(name, attr, (IntermediateStep) step);
        } else if (step instanceof FinalStep) {
            return this.readFinalStep(name, attr, (FinalStep) step);
        }
        return null;
    }

    private Object readIntermediateStep(String name, Attributes attr, IntermediateStep step) throws Exception {
        if (step instanceof RepeatStep) {
            return this.redirect(name, attr, ((RepeatStep) step).getItem());
        } else if (step instanceof UseForAllStep) {
            return this.redirect(name, attr, ((UseForAllStep) step).getArguments());
        } else if (step instanceof SubstituteStep) {
            SelectablePlaceholder<IndependentIntermediateStep> source = ((SubstituteStep) step).getSource();
            if (source.get() == null) {
                return this.startIndependentIntermediateStep(source, name, attr);
            }
        } else if (step instanceof TheoremStep) {
            TheoremStep theoremStep = (TheoremStep) step;
            if (name.equals("result")) {
                this.addPlaceholder(theoremStep.getResult());
                return this;
            } else {
                return this.redirect(name, attr, theoremStep.getTheorem());
            }
        }
        if (step instanceof StateFormulaStep || step instanceof UseDefinitionStep || step instanceof ResolveStep || step instanceof SubstituteStep) {
            SelectablePlaceholder<Formula> result = step.getResult();
            if (result.get() == null) {
                return this.startFormula(result, name, attr);
            }
        }
        if (step instanceof StateFormulaStep && name.equals("proof")) {
            return ((StateFormulaStep) step).getSubProof().fill();
        }
        return null;
    }

    private Object readFinalStep(String name, Attributes attr, FinalStep step) throws Exception {
        if (step instanceof UseDisjunctionStep) {
            if (name.equals("case")) {
                UseDisjunctionStep useDisjunctionStep = (UseDisjunctionStep) step;
                FixedMap<Formula, Proof> cases = useDisjunctionStep.getCases();
                DisjunctionFormula premise = (DisjunctionFormula) useDisjunctionStep.getPremiseFormula();
                for (Indirection<Formula> formulaPlaceholder : premise.getFormulae()) {
                    Formula formula = formulaPlaceholder.get();
                    if (formula != null && cases.get(formula) == null) {
                        Proof subProof = cases.add(formula, null);
                        ParameterList parameters = subProof.getParameters();
                        if ("all".equals(attr.getValue("assumptions"))) {
                            for (Indirection<Formula> assumptionPlaceholder : premise.getFormulae()) {
                                Formula assumption = assumptionPlaceholder.get();
                                if (assumption == null) {
                                    continue;
                                }
                                if (formula.equals(assumption)) {
                                    break;
                                }
                                ConstraintParameter constraintParameter = parameters.add(ConstraintParameter.class, null);
                                Proof assumptionProof = cases.get(assumption);
                                Parameter parameter = null;
                                Iterator<Parameter> iterator = assumptionProof.getParameters().iterator();
                                while (iterator.hasNext()) {
                                    parameter = iterator.next();
                                }
                                if (parameter != null) {
                                    constraintParameter.getName().set(parameter.getName().get());
                                }
                                FormulaSemantics.negate(assumption, constraintParameter.getFormula());
                            }
                        }
                        ConstraintParameter constraintParameter = parameters.add(ConstraintParameter.class, null);
                        constraintParameter.getName().set(attr.getValue("name"));
                        ObjectCloner.getInstance().cloneFormula(formulaPlaceholder.get(), constraintParameter.getFormula());
                        return subProof;
                    }
                }
            }
        } else if (step instanceof CasesStep) {
            if (name.equals("case")) {
                CasesStep casesStep = (CasesStep) step;
                return this.readCase(attr, casesStep, ProofSemantics.getCaseList(casesStep));
            }
        } else if (step instanceof ProveExistsStep) {
            ProveExistsStep existsStep = (ProveExistsStep) step;
            if (name.equals("proof")) {
                Proof subProof = existsStep.getSubProof().fill();
                ArgumentList arguments = existsStep.getArguments();
                ProofSemantics.insertArgumentDefinitions(arguments, subProof.getParameters());
                return subProof;
            } else {
                return this.redirect(name, attr, existsStep.getArguments());
            }
        } else if (step instanceof ProveSetEqualityStep) {
            if (name.equals("direction")) {
                ProveSetEqualityStep setEqualityStep = (ProveSetEqualityStep) step;
                String source = attr.getValue("source");
                boolean superset;
                if ("left".equals(source)) {
                    superset = false;
                } else if ("right".equals(source)) {
                    superset = true;
                } else {
                    throw new XMLReadException(Translator.format("attribute 'source' must be either \"left\" or \"right,\" each at most once"));
                }
                return ProofSemantics.fillSetEqualityProof(setEqualityStep, superset);
            }
        } else if (step instanceof ProveSingleStep) {
            FixedPlaceholder<Proof> subProofPlaceholder = ((ProveSingleStep) step).getSubProof();
            Proof subProof = subProofPlaceholder.get();
            if (subProof == null) {
                subProof = subProofPlaceholder.fill();
            }
            if (step instanceof ProveForAllStep) {
                return this.redirect(name, attr, new ProofWithParameters(subProof));
            } else {
                return this.redirect(name, attr, subProof);
            }
        }
        return null;
    }

    private <A> Object readCase(Attributes attr, CasesStep step, StructuralCaseList<A> structuralCases) throws Exception {
        MapIterator<Constructor, StructuralCaseList.Case<A>> iterator = structuralCases.iterator();
        while (iterator.hasNext()) {
            Constructor constructor = iterator.next();
            StructuralCaseList.Case<A> structuralCase = iterator.getTarget();
            if (step.getProofs().get(constructor) == null) {
                Proof subProof = step.getProofs().add(constructor, null);
                ParameterList parameters = subProof.getParameters();
                ProofSemantics.fillCaseParameters(structuralCases, constructor, structuralCase, parameters, attr.getValue("name"));
                if (step instanceof UseCasesStep) {
                    ConstraintParameter parameter = parameters.add(ConstraintParameter.class, null);
                    parameter.getName().set(attr.getValue("name"));
                    ProofSemantics.fillCaseFormula(((UseCasesStep) step).getPremiseFormula(), step.getRelationSide().get(), structuralCase, parameter.getFormula());
                }
                return subProof;
            }
        }
        return null;
    }

    @Override
    public void endElement(Object current) throws Exception {
        if (current instanceof MathObject) {
            this.endMathObject((MathObject) current);
        } else if (current instanceof ElementParameter.ShortCut) {
            this.endShortCut((ElementParameter.ShortCut) current);
        } else if (current instanceof ElementTerm) {
            this.endElementTerm((ElementTerm) current);
        } else if (current instanceof IntermediateStep) {
            this.endIntermediateStep((IntermediateStep) current);
        }
    }

    private void endMathObject(MathObject object) throws Exception {
        this.relationManager.setObjectFinished(object);
    }

    private void endShortCut(ElementParameter.ShortCut shortCut) throws Exception {
        this.fillConstructorReference(shortCut.getConstructor(), shortCut.getConstruction());
        this.constructorNames.pop();
    }

    private void endElementTerm(ElementTerm term) throws Exception {
        if (term instanceof ConstructorTerm) {
            ConstructorTerm constructorTerm = (ConstructorTerm) term;
            this.fillConstructorReference(constructorTerm.getConstructor(), constructorTerm.getConstruction());
            this.constructorNames.pop();
        }
    }

    private void endIntermediateStep(IntermediateStep step) throws Exception {
        ProofSemantics.fillIntermediateStep(step);
    }

    private Constructor findConstructor(Construction construction, String constructorName) throws Exception {
        if (construction == null) {
            throw new XMLReadException(Translator.format("construction not set"));
        }
        LibraryObjectList<Constructor> constructors = construction.getConstructors();
        Constructor constructor = constructors.find(constructorName);
        if (constructor == null) {
            if (this.relationManager.isObjectFinished(construction)) {
                throw new ItemNotFoundException(Translator.format("constructor \"%s\" not found", constructorName));
            } else {
                constructor = constructors.add(Constructor.class, constructorName, null);
            }
        }
        return constructor;
    }

    private int parseIndex(String s) throws Exception {
        if (s == null) {
            throw new XMLReadException(Translator.format("missing index"));
        }
        try {
            return Integer.parseInt(s) - 1;
        } catch (NumberFormatException e) {
            throw new XMLReadException(Translator.format("\"%s\" is not a valid index", s));
        }
    }

    private MathObject object;

    private RelationManager relationManager;

    private java.util.Stack<String> constructorNames = new java.util.Stack<String>();

    private class ProofWithParameters {

        public ProofWithParameters(Proof proof) {
            this.proof = proof;
        }

        public Proof proof;
    }

    private class CaseListAdder implements SelectablePlaceholder<Formula> {

        public CaseListAdder(CaseList<ElementTerm> cases) {
            this.cases = cases;
        }

        public Class<Formula> getType() {
            return Formula.class;
        }

        public <A extends Formula> A fill(Class<A> type) throws DependencyException, AlreadyFilledException {
            int count = this.cases.getCount();
            this.cases.add(null);
            return this.cases.get(count - 1).getFormula().fill(type);
        }

        public void clear() throws DependencyException {
            throw new UnsupportedOperationException();
        }

        public Formula get() {
            throw new UnsupportedOperationException();
        }

        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        private CaseList<ElementTerm> cases;
    }
}
