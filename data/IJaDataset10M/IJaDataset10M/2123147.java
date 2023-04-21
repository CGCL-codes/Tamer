package net.sourceforge.hlm.xml.write;

import java.io.*;
import java.net.*;
import java.util.*;
import net.sourceforge.hlm.generic.*;
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
import net.sourceforge.hlm.util.*;
import net.sourceforge.hlm.util.xml.write.*;

public class MathWriter extends ObjectWriter {

    public MathWriter(MessageHandler messageHandler, SimpleXMLWriter writer, Section section) {
        super(messageHandler, writer);
        this.section = section;
    }

    public void writeMathObject(MathObject object, File file, URI dtdURI) throws Exception {
        String elementName;
        if (object instanceof Construction) {
            elementName = "construction";
        } else if (object instanceof Predicate) {
            elementName = "predicate";
        } else if (object instanceof Operator) {
            elementName = "operator";
        } else if (object instanceof SetOperator) {
            elementName = "set-operator";
        } else if (object instanceof Theorem) {
            elementName = "theorem";
        } else {
            throw new XMLWriteException(Translator.format("unknown object encountered"));
        }
        XMLDocument document = this.startDocument(file, dtdURI, elementName);
        try {
            this.writeMathObject(object, this.createRootElement(document, elementName, object.getName().get()));
        } finally {
            this.finishDocument(document);
        }
    }

    public void writeMathObject(MathObject object, XMLContents element) throws Exception {
        element.addNewLine();
        if (this.writeParameters(object.getParameters(), element)) {
            element.addNewLine();
        }
        if (object instanceof Construction) {
            this.writeConstruction((Construction) object, element);
        } else if (object instanceof Predicate) {
            this.writePredicate((Predicate) object, element);
        } else if (object instanceof Operator) {
            this.writeOperator((Operator) object, element);
        } else if (object instanceof SetOperator) {
            this.writeSetOperator((SetOperator) object, element);
        } else if (object instanceof Theorem) {
            this.writeTheorem((Theorem) object, element);
        }
    }

    public void writeConstruction(Construction construction, XMLContents element) throws Exception {
        this.writeConstructors(construction.getConstructors(), element);
        this.writeEmbedding(construction.getEmbedding().get(), element);
    }

    public void writeConstructors(LibraryObjectList<Constructor> constructors, XMLContents element) throws Exception {
        if (!constructors.isEmpty()) {
            XMLElement ctors = element.addElement("constructors");
            for (Constructor constructor : constructors) {
                this.writeConstructor(constructor, ctors.addElement("constructor"));
            }
            element.addNewLine();
        }
    }

    public void writeConstructor(Constructor constructor, XMLElement element) throws Exception {
        String name = constructor.getName().get();
        if (name != null) {
            element.addAttribute("name", name);
        }
        this.writeParameters(constructor.getParameters(), element);
        this.writeEqualitySpecification(constructor, constructor.getEqualitySpecification().get(), element);
        this.writeRewriteRule(constructor.getRewriteRule().get(), element);
    }

    public void writeEqualitySpecification(Constructor constructor, Constructor.EqualitySpecification specification, XMLContents element) throws Exception {
        if (specification != null) {
            if (specification instanceof Constructor.EqualityDefinition) {
                Constructor.EqualityDefinition definition = (Constructor.EqualityDefinition) specification;
                XMLElement equality = element.addElement("equality-def");
                this.writeFormulaList(definition.getConditions(), equality.addElement("conditions"));
                this.writeEqualityDefinitionProof(definition.getReflexivityProof(), equality, "reflexivity");
                this.writeEqualityDefinitionProof(definition.getSymmetryProof(), equality, "symmetry");
                this.writeEqualityDefinitionProof(definition.getTransitivityProof(), equality, "transitivity");
            } else if (specification instanceof Constructor.IsomorphismDefinition) {
                Constructor.IsomorphismDefinition definition = (Constructor.IsomorphismDefinition) specification;
                XMLElement isomorphism = element.addElement("iso-equality-def");
                SetOperator setOperator = definition.getBijectionSet().get();
                if (setOperator != null) {
                    isomorphism.addAttribute("path", this.getLibraryObjectPath(setOperator));
                }
                for (Parameter parameter : constructor.getParameters()) {
                    Definition operator = null;
                    if (parameter instanceof SubsetParameter) {
                        operator = definition.getTransform((SubsetParameter) parameter).get();
                    } else if (parameter instanceof ElementParameter) {
                        operator = definition.getTransform((ElementParameter) parameter).get();
                    }
                    if (operator != null) {
                        XMLElement transform = isomorphism.addElement("iso-transform");
                        String name = parameter.getName().get();
                        if (name != null) {
                            transform.addAttribute("name", name);
                        }
                        transform.addAttribute("path", this.getLibraryObjectPath(operator));
                    }
                }
            }
        }
    }

    public void writeEqualityDefinitionProof(Indirection<Proof> placeholder, XMLContents element, String name) throws Exception {
        Proof proof = placeholder.get();
        if (proof != null) {
            this.writeProof(proof, element.addElement(name), true);
        }
    }

    public void writeRewriteRule(Constructor.RewriteRule rewriteRule, XMLContents element) throws Exception {
        if (rewriteRule != null) {
            XMLElement rule = element.addElement("rewrite");
            Theorem theorem = rewriteRule.getTheorem().get();
            if (theorem != null) {
                rule.addAttribute("path", this.getLibraryObjectPath(theorem));
            }
            this.writeElementTerm(rewriteRule.getTerm(), rule);
        }
    }

    public void writeEmbedding(Construction.Embedding embedding, XMLContents element) throws Exception {
        if (embedding != null) {
            XMLElement embed = element.addElement("embedding");
            this.writeParameter(embedding.getParameter(), embed);
            XMLElement target = embed.addElement("embedding-target");
            this.writeConstructorName(embedding.getTarget().get(), target);
            this.writeArgumentList(embedding.getTarget().getArguments(), target);
            Proof wellDefinednessProof = embedding.getWellDefinednessProof().get();
            if (wellDefinednessProof != null) {
                this.writeProof(wellDefinednessProof, embed.addElement("well-definedness"), true);
            }
            element.addNewLine();
        }
    }

    public void writePredicate(Predicate predicate, XMLContents element) throws Exception {
        this.writeFormulaList(predicate.getConditions(), element.addElement("conditions"));
        element.addNewLine();
    }

    public void writeOperator(Operator operator, XMLContents element) throws Exception {
        Operator.Definition definition = operator.getDefinition().get();
        if (definition != null) {
            if (definition instanceof Operator.ExplicitDefinition) {
                this.writeElementTermList(((Operator.ExplicitDefinition) definition).getTerms(), element.addElement("terms"));
            } else if (definition instanceof Operator.ImplicitDefinition) {
                Operator.ImplicitDefinition implicitDefinition = (Operator.ImplicitDefinition) definition;
                XMLElement implicit = element.addElement("implicit");
                this.writeParameter(implicitDefinition.getParameter(), implicit);
                this.writeFormulaList(implicitDefinition.getConditions(), implicit.addElement("conditions"));
                Proof wellDefinednessProof = implicitDefinition.getWellDefinednessProof().get();
                if (wellDefinednessProof != null) {
                    implicit.addNewLine();
                    this.writeProof(wellDefinednessProof, implicit.addElement("well-definedness"));
                }
            }
            element.addNewLine();
        }
    }

    public void writeSetOperator(SetOperator operator, XMLContents element) throws Exception {
        this.writeSetTermList(operator.getTerms(), element.addElement("set-terms"));
        element.addNewLine();
    }

    public void writeTheorem(Theorem theorem, XMLContents element) throws Exception {
        Theorem.Contents contents = theorem.getContents().get();
        if (contents != null) {
            if (contents instanceof Theorem.StandardContents) {
                Theorem.StandardContents standardContents = (Theorem.StandardContents) contents;
                this.writeFormula(standardContents.getClaim(), element.addElement("claim"));
                for (Proof proof : standardContents.getProofs()) {
                    element.addNewLine();
                    this.writeProof(proof, element.addElement("proof"));
                }
            } else if (contents instanceof Theorem.EquivalenceContents) {
                this.writeFormulaList(((Theorem.EquivalenceContents) contents).getConditions(), element.addElement("equivalence"));
            }
            element.addNewLine();
        }
    }

    private boolean writeParameters(ParameterList parameters, XMLContents element) throws Exception {
        if (parameters.isEmpty()) {
            return false;
        } else {
            XMLElement item = element.addElement("parameters");
            this.writeContextItemList(parameters, item);
            return true;
        }
    }

    public void writeContextItemList(Iterable<? extends ContextItem> list, XMLContents element) throws Exception {
        for (ContextItem contextItem : list) {
            if (contextItem instanceof Parameter) {
                this.writeParameter((Parameter) contextItem, element);
            } else if (contextItem instanceof IntermediateStep) {
                this.writeIntermediateStep((IntermediateStep) contextItem, element);
            } else if (contextItem instanceof FinalStep) {
                this.writeFinalStep((FinalStep) contextItem, element);
            } else {
                throw new XMLWriteException(Translator.format("unknown object encountered"));
            }
        }
    }

    public void writeParameter(Parameter parameter, XMLContents element) throws Exception {
        String elementName;
        if (parameter instanceof ArbitrarySetParameter) {
            elementName = "set-param";
        } else if (parameter instanceof SubsetParameter) {
            elementName = "subset-param";
        } else if (parameter instanceof ElementParameter) {
            elementName = "element-param";
        } else if (parameter instanceof ConstraintParameter) {
            elementName = "constraint-param";
        } else if (parameter instanceof BindingParameter) {
            elementName = "binding-param";
        } else if (parameter instanceof SymbolParameter) {
            elementName = "symbol-param";
        } else if (parameter instanceof ElementDefinition) {
            elementName = "define";
        } else if (parameter instanceof SetDefinition) {
            elementName = "set-define";
        } else if (parameter instanceof SymbolDefinition) {
            elementName = "symbol-define";
        } else {
            throw new XMLWriteException(Translator.format("unknown object encountered"));
        }
        XMLElement item = element.addElement(elementName);
        String name = parameter.getName().get();
        if (name != null) {
            item.addAttribute("name", name);
        }
        if (parameter.isAuto()) {
            item.addAttribute("auto", "yes");
        }
        this.writeParameterContents(parameter, item);
    }

    public void writeParameterContents(Parameter parameter, XMLElement item) throws Exception {
        if (parameter instanceof SubsetParameter) {
            this.writeSetTerm(((SubsetParameter) parameter).getSuperset(), item);
        } else if (parameter instanceof ElementParameter) {
            ElementParameter elementParameter = (ElementParameter) parameter;
            this.writeSetTerm(elementParameter.getSet(), item);
            this.writeShortCut(elementParameter.getShortCut().get(), item);
        } else if (parameter instanceof ConstraintParameter) {
            this.writeFormula(((ConstraintParameter) parameter).getFormula(), item);
        } else if (parameter instanceof BindingParameter) {
            BindingParameter bindingParameter = (BindingParameter) parameter;
            this.writeParameterContents(bindingParameter.getElementParameter(), item);
            this.writeContextItemList(bindingParameter.getBoundParameters(), item);
        } else if (parameter instanceof ElementDefinition) {
            this.writeElementTerm(((ElementDefinition) parameter).getTerm(), item);
        } else if (parameter instanceof SetDefinition) {
            this.writeSetTerm(((SetDefinition) parameter).getTerm(), item);
        } else if (parameter instanceof SymbolDefinition) {
            this.writeSymbolTerm(((SymbolDefinition) parameter).getTerm(), item);
        }
    }

    public void writeShortCut(ElementParameter.ShortCut shortCut, XMLContents element) throws Exception {
        if (shortCut != null) {
            XMLElement sc = element.addElement("shortcut");
            this.writeConstructorName(shortCut.getConstructor().get(), sc);
            if (shortCut.getRewrite()) {
                sc.addAttribute("rewrite", "yes");
            }
            if (shortCut.getOverride()) {
                sc.addAttribute("override", "yes");
            }
            this.writeMathObjectReference(shortCut.getConstruction(), sc.addElement("construction-ref"));
            this.writeContextItemList(shortCut.getParameters(), sc);
        }
    }

    public void writeIntermediateStep(IntermediateStep step, XMLContents element) throws Exception {
        String elementName;
        if (step instanceof RepeatStep) {
            elementName = "repeat";
        } else if (step instanceof StateFormulaStep) {
            elementName = "state";
        } else if (step instanceof EmbedStep) {
            elementName = "embed";
        } else if (step instanceof UseDefinitionStep) {
            elementName = "use-def";
        } else if (step instanceof UseForAllStep) {
            elementName = "use-forall";
        } else if (step instanceof SetExtendStep) {
            elementName = "set-extend";
        } else if (step instanceof ExtendStep) {
            elementName = "extend";
        } else if (step instanceof SubstituteStep) {
            elementName = "substitute";
        } else if (step instanceof ResolveStep) {
            elementName = "resolve";
        } else if (step instanceof TheoremStep) {
            elementName = "theorem-ref";
        } else {
            throw new XMLWriteException(Translator.format("unknown object encountered"));
        }
        XMLElement item = element.addElement(elementName);
        String name = step.getName().get();
        if (name != null) {
            item.addAttribute("name", name);
        }
        if (step instanceof RepeatStep) {
            RepeatStep repeatStep = (RepeatStep) step;
            this.writeContextItemReference(repeatStep.getItem(), item);
        } else if (step instanceof EmbedStep) {
            EmbedStep embedStep = (EmbedStep) step;
            this.writeMathObjectPath(embedStep.getConstruction().get(), item);
            this.writeElementTerm(embedStep.getInput(), item);
            this.writeElementTerm(embedStep.getOutput(), item);
        } else if (step instanceof UseDefinitionStep) {
            this.writeRelationSide(((UseDefinitionStep) step).getRelationSide().get(), item);
        } else if (step instanceof UseForAllStep) {
            this.writeArgumentList(((UseForAllStep) step).getArguments(), item);
        } else if (step instanceof SetExtendStep) {
            this.writeSetTerm(((SetExtendStep) step).getTerm(), item);
        } else if (step instanceof ExtendStep) {
            this.writeElementTerm(((ExtendStep) step).getTerm(), item);
        } else if (step instanceof SubstituteStep) {
            this.writeRelationSide(((SubstituteStep) step).getRelationSide().get(), item);
            this.writeIntermediateStep(((SubstituteStep) step).getSource().get(), item);
        } else if (step instanceof TheoremStep) {
            this.writeMathObjectReference(((TheoremStep) step).getTheorem(), item);
            this.writeFormula(step.getResult(), item.addElement("result"));
        }
        if (step instanceof StateFormulaStep || step instanceof UseDefinitionStep || step instanceof ResolveStep || step instanceof SubstituteStep) {
            this.writeFormula(step.getResult(), item);
        }
        if (step instanceof StateFormulaStep) {
            Proof proof = ((StateFormulaStep) step).getSubProof().get();
            if (proof != null) {
                this.writeProof(proof, item.addElement("proof"));
            }
        }
    }

    public void writeFinalStep(FinalStep step, XMLContents element) throws Exception {
        String elementName;
        if (step instanceof UseDisjunctionStep) {
            elementName = "use-or";
        } else if (step instanceof UseCasesStep) {
            elementName = "use-cases";
        } else if (step instanceof ProveDefinitionStep) {
            elementName = "prove-def";
        } else if (step instanceof ProveNegatedStep) {
            elementName = "prove-neg";
        } else if (step instanceof ProveForAllStep) {
            elementName = "prove-forall";
        } else if (step instanceof ProveExistsStep) {
            elementName = "prove-exists";
        } else if (step instanceof ProveSetEqualityStep) {
            elementName = "prove-set-equals";
        } else if (step instanceof ProveCasesStep) {
            elementName = "prove-cases";
        } else {
            throw new XMLWriteException(Translator.format("unknown object encountered"));
        }
        XMLElement item = element.addElement(elementName);
        if (step instanceof ProveSingleStep) {
            if (step instanceof ProveDefinitionStep) {
                this.writeRelationSide(((ProveDefinitionStep) step).getRelationSide().get(), item);
            } else if (step instanceof ProveForAllStep) {
            } else if (step instanceof ProveExistsStep) {
                this.writeArgumentList(((ProveExistsStep) step).getArguments(), item);
            }
            Proof subProof = ((ProveSingleStep) step).getSubProof().get();
            if (subProof != null) {
                if (step instanceof ProveNegatedStep) {
                    Iterator<Parameter> iterator = subProof.getParameters().iterator();
                    if (iterator.hasNext()) {
                        String paramName = iterator.next().getName().get();
                        if (paramName != null) {
                            item.addAttribute("name", paramName);
                        }
                    }
                }
                XMLElement proofElement = step instanceof ProveExistsStep ? item.addElement("proof") : item;
                this.writeProof(subProof, proofElement);
            }
        } else if (step instanceof UseDisjunctionStep) {
            UseDisjunctionStep useDisjunctionStep = (UseDisjunctionStep) step;
            GenericMap<Formula, Proof> cases = useDisjunctionStep.getCases();
            for (Indirection<Formula> placeholder : ((DisjunctionFormula) useDisjunctionStep.getPremiseFormula()).getFormulae()) {
                Formula formula = placeholder.get();
                if (formula == null) {
                    break;
                }
                Proof subProof = cases.get(formula);
                if (subProof == null) {
                    break;
                }
                XMLElement caseElement = item.addElement("case");
                boolean allAssumptions = false;
                Parameter parameter = null;
                Iterator<Parameter> iterator = subProof.getParameters().iterator();
                while (iterator.hasNext()) {
                    if (parameter != null) {
                        allAssumptions = true;
                    }
                    parameter = iterator.next();
                }
                if (parameter != null) {
                    String parameterName = parameter.getName().get();
                    if (parameterName != null) {
                        caseElement.addAttribute("name", parameterName);
                    }
                }
                if (allAssumptions) {
                    caseElement.addAttribute("assumptions", "all");
                }
                this.writeProof(subProof, caseElement);
            }
        } else if (step instanceof ProveSetEqualityStep) {
            ProveSetEqualityStep proveSetEqualityStep = (ProveSetEqualityStep) step;
            Proof subsetProof = proveSetEqualityStep.getSubsetProof().get();
            if (subsetProof != null) {
                XMLElement subsetElement = item.addElement("direction");
                subsetElement.addAttribute("source", "left");
                this.writeContextItemList(subsetProof.getSteps(), subsetElement);
            }
            Proof supersetProof = proveSetEqualityStep.getSupersetProof().get();
            if (supersetProof != null) {
                XMLElement supersetElement = item.addElement("direction");
                supersetElement.addAttribute("source", "right");
                this.writeContextItemList(supersetProof.getSteps(), supersetElement);
            }
        } else if (step instanceof CasesStep) {
            CasesStep casesStep = (CasesStep) step;
            this.writeRelationSide(casesStep.getRelationSide().get(), item);
            MapIterator<Constructor, Proof> proofs = casesStep.getProofs().iterator();
            while (proofs.hasNext()) {
                Constructor constructor = proofs.next();
                Proof caseProof = proofs.getTarget();
                XMLElement caseElement = item.addElement("case");
                Iterator<Parameter> constructorIterator = constructor.getParameters().iterator();
                Iterator<Parameter> iterator = caseProof.getParameters().iterator();
                while (constructorIterator.hasNext()) {
                    constructorIterator.next();
                    iterator.next();
                }
                if (iterator.hasNext()) {
                    Parameter parameter = iterator.next();
                    if (parameter != null) {
                        String name = parameter.getName().get();
                        if (name != null) {
                            caseElement.addAttribute("name", name);
                        }
                    }
                }
                this.writeProof(caseProof, caseElement);
            }
        }
    }

    public void writeRelationSide(RelationSide side, XMLElement element) throws Exception {
        if (side != null) {
            element.addAttribute("side", side == RelationSide.LEFT ? "left" : "right");
        }
    }

    public void writeArgumentList(ArgumentList arguments, XMLContents element) throws Exception {
        if (arguments != null) {
            MapIterator<Parameter, Argument> iterator = arguments.iterator();
            while (iterator.hasNext()) {
                Parameter parameter = iterator.next();
                Argument argument = iterator.getTarget();
                this.writeArgument(parameter, argument, element);
            }
        }
    }

    public void writeArgument(Parameter parameter, Argument argument, XMLContents element) throws Exception {
        String elementName;
        if (argument instanceof ArbitrarySetArgument) {
            elementName = "set-arg";
        } else if (argument instanceof SubsetArgument) {
            elementName = "subset-arg";
        } else if (argument instanceof ElementArgument) {
            elementName = "element-arg";
        } else if (argument instanceof ConstraintArgument) {
            elementName = "constraint-arg";
        } else if (argument instanceof BindingArgument) {
            elementName = "binding-arg";
        } else if (argument instanceof SymbolArgument) {
            elementName = "symbol-arg";
        } else {
            throw new XMLWriteException(Translator.format("unknown object encountered"));
        }
        XMLElement arg = element.addElement(elementName);
        String name = parameter.getName().get();
        if (name == null) {
            throw new XMLWriteException(Translator.format("referenced parameter lacks a name"));
        }
        arg.addAttribute("name", name);
        if (argument instanceof ArbitrarySetArgument) {
            this.writeSetTerm(((ArbitrarySetArgument) argument).getTerm(), arg);
        } else if (argument instanceof SubsetArgument) {
            SubsetArgument subsetArgument = (SubsetArgument) argument;
            this.writeSetTerm(subsetArgument.getTerm(), arg);
            Proof subsetProof = subsetArgument.getSubsetProof().get();
            if (subsetProof != null) {
                this.writeProof(subsetProof, arg.addElement("well-definedness"));
            }
        } else if (argument instanceof ElementArgument) {
            ElementArgument elementArgument = (ElementArgument) argument;
            this.writeElementTerm(elementArgument.getTerm(), arg);
            Proof elementProof = elementArgument.getElementProof().get();
            if (elementProof != null) {
                this.writeProof(elementProof, arg.addElement("well-definedness"));
            }
        } else if (argument instanceof ConstraintArgument) {
            this.writeProof(((ConstraintArgument) arg).getProof().get(), arg);
        } else if (argument instanceof BindingArgument) {
            BindingArgument bindingArgument = (BindingArgument) argument;
            this.writeParameter(bindingArgument.getParameter(), arg);
            this.writeArgumentList(bindingArgument.getBoundArguments(), arg);
        } else if (argument instanceof SymbolArgument) {
            this.writeSymbolTerm(((SymbolArgument) argument).getTerm(), arg);
        }
    }

    public void writeFormula(Indirection<Formula> formula, XMLContents element) throws Exception {
        this.writeFormula(formula.get(), element);
    }

    public void writeFormula(Formula formula, XMLContents element) throws Exception {
        if (formula instanceof EnumerationFormula) {
            EnumerationFormula enumerationFormula = (EnumerationFormula) formula;
            XMLElement enumeration;
            if (enumerationFormula instanceof ConjunctionFormula) {
                enumeration = element.addElement("and");
            } else if (enumerationFormula instanceof DisjunctionFormula) {
                enumeration = element.addElement("or");
            } else {
                throw new XMLWriteException(Translator.format("unknown object encountered"));
            }
            this.writeFormulaAttributes(formula, enumeration);
            for (Indirection<Formula> item : enumerationFormula.getFormulae()) {
                this.writeFormula(item, enumeration);
            }
        } else if (formula instanceof QuantifiedFormula) {
            QuantifiedFormula quantifiedFormula = (QuantifiedFormula) formula;
            XMLElement inner;
            if (quantifiedFormula instanceof ForAllFormula) {
                inner = element.addElement("forall");
            } else if (quantifiedFormula instanceof ExistsFormula) {
                inner = element.addElement("exists");
            } else if (quantifiedFormula instanceof ExistsUniqueFormula) {
                inner = element.addElement("exists-unique");
            } else {
                throw new XMLWriteException(Translator.format("unknown object encountered"));
            }
            this.writeFormulaAttributes(formula, inner);
            this.writeParameters(quantifiedFormula.getParameters(), inner);
            Indirection<Formula> innerFormula = quantifiedFormula.getFormula();
            if (innerFormula != null) {
                this.writeFormula(innerFormula, inner);
            }
        } else if (formula instanceof RelationFormula<?, ?>) {
            if (formula instanceof ElementFormula) {
                ElementFormula elementFormula = (ElementFormula) formula;
                XMLElement in = element.addElement("in");
                this.writeFormulaAttributes(formula, in);
                this.writeElementTerm(elementFormula.getLeftTerm(), in);
                this.writeSetTerm(elementFormula.getRightTerm(), in);
            } else if (formula instanceof SubsetFormula) {
                SubsetFormula subsetFormula = (SubsetFormula) formula;
                XMLElement sub = element.addElement("sub");
                this.writeFormulaAttributes(formula, sub);
                this.writeSetTerm(subsetFormula.getLeftTerm(), sub);
                this.writeSetTerm(subsetFormula.getRightTerm(), sub);
            } else if (formula instanceof EqualityFormula) {
                EqualityFormula equalityFormula = (EqualityFormula) formula;
                XMLElement equals = element.addElement("equals");
                this.writeFormulaAttributes(formula, equals);
                this.writeElementTerm(equalityFormula.getLeftTerm(), equals);
                this.writeElementTerm(equalityFormula.getRightTerm(), equals);
            } else if (formula instanceof SetEqualityFormula) {
                SetEqualityFormula equalityFormula = (SetEqualityFormula) formula;
                XMLElement equals = element.addElement("set-equals");
                this.writeFormulaAttributes(formula, equals);
                this.writeSetTerm(equalityFormula.getLeftTerm(), equals);
                this.writeSetTerm(equalityFormula.getRightTerm(), equals);
            } else {
                throw new XMLWriteException(Translator.format("unknown object encountered"));
            }
        } else if (formula instanceof StructuralFormula) {
            StructuralFormula structuralFormula = (StructuralFormula) formula;
            XMLElement structural = element.addElement("structural");
            this.writeFormulaAttributes(formula, structural);
            StructuralCaseList<Formula> cases = structuralFormula.getCases();
            this.writeStructuralCaseListHeader(cases, structural);
            MapIterator<Constructor, StructuralCaseList.Case<Formula>> iterator = cases.iterator();
            while (iterator.hasNext()) {
                Constructor constructor = iterator.next();
                StructuralCaseList.Case<Formula> item = iterator.getTarget();
                XMLElement structuralCase = structural.addElement("structural-item");
                this.writeStructuralCaseHeader(constructor, item, structuralCase);
                this.writeFormula(item.getContents(), structuralCase);
                Proof wellDefinednessProof = item.getWellDefinednessProof().get();
                if (wellDefinednessProof != null) {
                    this.writeProof(wellDefinednessProof, structuralCase.addElement("well-definedness"));
                }
            }
        } else if (formula instanceof PredicateFormula) {
            XMLElement ref = element.addElement("predicate-ref");
            MathObjectReference<Predicate> predicate = ((PredicateFormula) formula).getPredicate();
            this.writeMathObjectPath(predicate.get(), ref);
            this.writeFormulaAttributes(formula, ref);
            this.writeArgumentList(predicate.getArguments(), ref);
        } else {
            element.addElement("empty");
        }
    }

    public void writeFormulaAttributes(Formula formula, XMLAttributes element) throws IOException {
        int negationCount = formula.getNegationCount();
        if (negationCount > 0) {
            element.addAttribute("negated", Integer.toString(negationCount));
        }
    }

    public void writeSetTerm(Indirection<SetTerm> term, XMLContents element) throws Exception {
        this.writeSetTerm(term.get(), element);
    }

    public void writeSetTerm(SetTerm term, XMLContents element) throws Exception {
        if (term instanceof VariableSetTerm) {
            this.writeContextItemReference(((VariableSetTerm) term).getVariable(), element.addElement("set-var-ref"));
        } else if (term instanceof ConstructionTerm) {
            this.writeMathObjectReference(((ConstructionTerm) term).getConstruction(), element.addElement("construction-ref"));
        } else if (term instanceof EnumerationTerm) {
            XMLElement enumeration = element.addElement("enumeration");
            for (ElementTerm item : ((EnumerationTerm) term).getTerms()) {
                this.writeElementTerm(item, enumeration);
            }
        } else if (term instanceof SubsetTerm) {
            SubsetTerm subsetTerm = (SubsetTerm) term;
            XMLElement subset = element.addElement("subset");
            this.writeParameter(subsetTerm.getParameter(), subset);
            this.writeFormula(subsetTerm.getFormula(), subset);
        } else if (term instanceof ExtendedSubsetTerm) {
            ExtendedSubsetTerm extendedSubsetTerm = (ExtendedSubsetTerm) term;
            XMLElement extendedSubset = element.addElement("extended-subset");
            this.writeParameters(extendedSubsetTerm.getParameters(), extendedSubset);
            this.writeElementTerm(extendedSubsetTerm.getTerm(), extendedSubset);
        } else if (term instanceof StructuralSetTerm) {
            StructuralSetTerm structuralSetTerm = (StructuralSetTerm) term;
            XMLElement structural = element.addElement("set-structural-cases");
            StructuralCaseList<SetTerm> cases = structuralSetTerm.getCases();
            this.writeStructuralCaseListHeader(cases, structural);
            MapIterator<Constructor, StructuralCaseList.Case<SetTerm>> iterator = cases.iterator();
            while (iterator.hasNext()) {
                Constructor constructor = iterator.next();
                StructuralCaseList.Case<SetTerm> item = iterator.getTarget();
                XMLElement structuralCase = structural.addElement("set-structural-case");
                this.writeStructuralCaseHeader(constructor, item, structuralCase);
                this.writeSetTerm(item.getContents(), structuralCase);
                Proof wellDefinednessProof = item.getWellDefinednessProof().get();
                if (wellDefinednessProof != null) {
                    this.writeProof(wellDefinednessProof, structuralCase.addElement("well-definedness"));
                }
            }
        } else if (term instanceof SetOperatorTerm) {
            this.writeMathObjectReference(((SetOperatorTerm) term).getOperator(), element.addElement("set-operator-ref"));
        } else if (term instanceof PreviousSetTerm) {
            element.addElement("set-previous");
        } else {
            element.addElement("empty");
        }
    }

    public void writeElementTerm(Indirection<ElementTerm> term, XMLContents element) throws Exception {
        this.writeElementTerm(term.get(), element);
    }

    public void writeElementTerm(ElementTerm term, XMLContents element) throws Exception {
        if (term instanceof VariableElementTerm) {
            this.writeContextItemReference(((VariableElementTerm) term).getVariable(), element.addElement("var-ref"));
        } else if (term instanceof ConstructorTerm) {
            ConstructorTerm constructorTerm = (ConstructorTerm) term;
            XMLElement ctor = element.addElement("constructor-ref");
            MathObjectReference<Constructor> constructor = constructorTerm.getConstructor();
            this.writeConstructorName(constructor.get(), ctor);
            this.writeMathObjectReference(constructorTerm.getConstruction(), ctor.addElement("construction-ref"));
            this.writeArgumentList(constructor.getArguments(), ctor);
        } else if (term instanceof CaseElementTerm) {
            CaseElementTerm caseTerm = (CaseElementTerm) term;
            XMLElement cases = element.addElement("cases");
            for (CaseList.Case<ElementTerm> item : caseTerm.getCases()) {
                this.writeElementTerm(item.getContents(), cases);
                Indirection<Formula> formulaPlaceholder = item.getFormula();
                if (formulaPlaceholder != null) {
                    this.writeFormula(formulaPlaceholder, cases);
                    Indirection<Proof> proofPlaceholder = item.getWellDefinednessProof();
                    if (proofPlaceholder != null) {
                        Proof proof = proofPlaceholder.get();
                        if (proof != null) {
                            this.writeProof(proof, cases.addElement("well-definedness"));
                        }
                    }
                }
            }
        } else if (term instanceof StructuralElementTerm) {
            StructuralElementTerm structuralElementTerm = (StructuralElementTerm) term;
            XMLElement structural = element.addElement("structural-cases");
            StructuralCaseList<ElementTerm> cases = structuralElementTerm.getCases();
            this.writeStructuralCaseListHeader(cases, structural);
            MapIterator<Constructor, StructuralCaseList.Case<ElementTerm>> iterator = cases.iterator();
            while (iterator.hasNext()) {
                Constructor constructor = iterator.next();
                StructuralCaseList.Case<ElementTerm> item = iterator.getTarget();
                XMLElement structuralCase = structural.addElement("structural-case");
                this.writeStructuralCaseHeader(constructor, item, structuralCase);
                this.writeElementTerm(item.getContents(), structuralCase);
                Proof proof = item.getWellDefinednessProof().get();
                if (proof != null) {
                    this.writeProof(proof, structuralCase.addElement("well-definedness"));
                }
            }
        } else if (term instanceof OperatorTerm) {
            this.writeMathObjectReference(((OperatorTerm) term).getOperator(), element.addElement("operator-ref"));
        } else if (term instanceof PreviousElementTerm) {
            element.addElement("previous");
        } else {
            element.addElement("empty");
        }
    }

    public void writeSymbolTerm(Indirection<SymbolTerm> term, XMLContents element) throws Exception {
        this.writeSymbolTerm(term.get(), element);
    }

    public void writeSymbolTerm(SymbolTerm term, XMLContents element) throws Exception {
        if (term instanceof VariableSymbolTerm) {
            this.writeContextItemReference(((VariableSymbolTerm) term).getVariable(), element.addElement("var-ref"));
        } else {
            element.addElement("empty");
        }
    }

    public void writeContextItemReference(ContextItemReference<?> reference, XMLElement element) throws Exception {
        ContextItem contextItem = reference.get();
        if (contextItem != null) {
            String name = contextItem.getName().get();
            if (name != null) {
                element.addAttribute("name", name);
            }
        }
        this.writeBinding(reference.getBinding(), element);
    }

    public void writeBinding(ContextItemReference.Binding binding, XMLContents element) throws Exception {
        if (binding == null) {
            return;
        }
        this.writeBinding(binding.getOuterBinding(), element);
        this.writeElementTerm(binding.getValue(), element);
    }

    public void writeConstructorName(Constructor constructor, XMLAttributes element) throws Exception {
        if (constructor != null) {
            String name = constructor.getName().get();
            if (name != null) {
                element.addAttribute("name", name);
            }
        }
    }

    public void writeMathObjectPath(MathObject object, XMLAttributes element) throws Exception {
        if (object != null) {
            element.addAttribute("path", this.getLibraryObjectPath(object));
        }
    }

    private String getLibraryObjectPath(LibraryObject object) throws Exception {
        return this.getLibraryObjectPath(object, this.section);
    }

    public void writeMathObjectReference(MathObjectReference<?> reference, XMLElement element) throws Exception {
        this.writeMathObjectPath(reference.get(), element);
        this.writeArgumentList(reference.getArguments(), element);
    }

    public void writeStructuralCaseListHeader(StructuralCaseList<?> cases, XMLElement element) throws Exception {
        this.writeMathObjectPath(cases.getConstruction().get(), element);
        this.writeElementTerm(cases.getSample(), element);
    }

    public void writeStructuralCaseHeader(Constructor constructor, StructuralCaseList.Case<?> item, XMLElement element) throws Exception {
        this.writeConstructorName(constructor, element);
        if (item.getRewrite()) {
            element.addAttribute("rewrite", "yes");
        }
        this.writeParameters(item.getParameters(), element);
    }

    public void writeFormulaList(EquivalenceList<Formula> formulae, XMLContents element) throws Exception {
        for (Indirection<Formula> formula : formulae) {
            this.writeFormula(formula, element);
        }
        this.writeEquivalenceProofs(formulae, element);
    }

    public void writeSetTermList(EquivalenceList<SetTerm> terms, XMLContents element) throws Exception {
        for (Indirection<SetTerm> term : terms) {
            this.writeSetTerm(term, element);
        }
        this.writeEquivalenceProofs(terms, element);
    }

    public void writeElementTermList(EquivalenceList<ElementTerm> terms, XMLContents element) throws Exception {
        for (Indirection<ElementTerm> term : terms) {
            this.writeElementTerm(term, element);
        }
        this.writeEquivalenceProofs(terms, element);
    }

    private <A> void writeEquivalenceProofs(EquivalenceList<A> list, XMLContents element) throws Exception {
        GenericList<EquivalenceList.EquivalenceProof<A>> proofs = list.getEquivalenceProofs();
        if (!proofs.isEmpty()) {
            element.addNewLine();
            for (EquivalenceList.EquivalenceProof<A> proof : proofs) {
                XMLElement proofElement = element.addElement("equivalence-proof");
                proofElement.addAttribute("from", Integer.toString(proof.getSourceIndex() + 1));
                proofElement.addAttribute("to", Integer.toString(proof.getTargetIndex() + 1));
                this.writeProof(proof.get(), proofElement);
            }
        }
    }

    public void writeProof(Proof proof, XMLContents element) throws Exception {
        this.writeProof(proof, element, false);
    }

    public void writeProof(Proof proof, XMLContents element, boolean includeParameters) throws Exception {
        if (includeParameters) {
            this.writeParameters(proof.getParameters(), element);
        }
        Formula goal = proof.getExplicitGoal().get();
        if (goal != null) {
            this.writeFormula(goal, element.addElement("goal"));
        }
        this.writeContextItemList(proof.getSteps(), element);
    }

    private Section section;
}
