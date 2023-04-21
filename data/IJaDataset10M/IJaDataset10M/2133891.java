package edu.neu.ccs.demeterf.demfgen.dgp.traversals;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.demfgen.*;
import edu.neu.ccs.demeterf.demfgen.dgp.DGPFunc;
import edu.neu.ccs.demeterf.demfgen.traversals.Travs;
import edu.neu.ccs.demeterf.demfgen.pcdgp.PCDGPFunc;
import edu.neu.ccs.demeterf.dispatch.*;
import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.demfgen.ClassHier.InhrtPair;
import java.io.*;
import java.io.FileInputStream;
import edu.neu.ccs.demeterf.util.Util;
import edu.neu.ccs.demeterf.util.CLI;
import edu.neu.ccs.demeterf.demfgen.classes.Package;
import edu.neu.ccs.demeterf.demfgen.classes.CDFile;
import edu.neu.ccs.demeterf.demfgen.dgp.DGPFunc.Trav;
import edu.neu.ccs.demeterf.demfgen.classes.*;

public class PrintTrav implements DGPFunc.Trav {

    private edu.neu.ccs.demeterf.demfgen.dgp.Print func;

    public PrintTrav(edu.neu.ccs.demeterf.demfgen.dgp.Print f) {
        func = f;
    }

    public String traverse(final Option<List<TypeDef>> _h) {
        return traverseOption_List_TypeDef__(_h);
    }

    public TypeUseParams traverseUseParams(final UseParams _h) {
        NETypeUseList _types = traverseNETypeUseList(_h.getTypes());
        return func.combine((TypeUseParams) _h);
    }

    public TypeUseParams traverseTypeUseParams(final TypeUseParams _h) {
        if (_h instanceof UseParams) return traverseUseParams((UseParams) _h);
        if (_h instanceof EmptyUseParams) return traverseEmptyUseParams((EmptyUseParams) _h); else throw new RuntimeException("Unknown TypeUseParams Variant");
    }

    public TypeUseList traverseTypeUseList(final TypeUseList _h) {
        if (_h instanceof TypeUseCons) return traverseTypeUseCons((TypeUseCons) _h);
        if (_h instanceof TypeUseEmpty) return traverseTypeUseEmpty((TypeUseEmpty) _h); else throw new RuntimeException("Unknown TypeUseList Variant");
    }

    public TypeUseList traverseTypeUseEmpty(final TypeUseEmpty _h) {
        return func.combine((TypeUseList) _h);
    }

    public TypeUseList traverseTypeUseCons(final TypeUseCons _h) {
        TypeUse _first = traverseTypeUse(_h.getFirst());
        TypeUseList _rest = traverseTypeUseList(_h.getRest());
        return func.combine((TypeUseList) _h);
    }

    public TypeUse traverseTypeUse(final TypeUse _h) {
        ident _name = _h.getName();
        TypeUseParams _params = traverseTypeUseParams(_h.getTparams());
        return func.combine((TypeUse) _h);
    }

    public String traverseTypeDef(final TypeDef _h) {
        if (_h instanceof IntfcDef) return traverseIntfcDef((IntfcDef) _h);
        if (_h instanceof ClassDef) return traverseClassDef((ClassDef) _h); else throw new RuntimeException("Unknown TypeDef Variant");
    }

    public Syntax traverseTheEOF(final TheEOF _h) {
        return func.combine((Syntax) _h);
    }

    public Syntax traverseSyntax(final Syntax _h) {
        if (_h instanceof AddSpace) return traverseAddSpace((AddSpace) _h);
        if (_h instanceof AddTab) return traverseAddTab((AddTab) _h);
        if (_h instanceof AddLine) return traverseAddLine((AddLine) _h);
        if (_h instanceof AddReturn) return traverseAddReturn((AddReturn) _h);
        if (_h instanceof Plus) return traversePlus((Plus) _h);
        if (_h instanceof Minus) return traverseMinus((Minus) _h);
        if (_h instanceof AddToken) return traverseAddToken((AddToken) _h);
        if (_h instanceof TheEOF) return traverseTheEOF((TheEOF) _h); else throw new RuntimeException("Unknown Syntax Variant");
    }

    public List<String> traverseSubtypeList(final SubtypeList _h) {
        if (_h instanceof SubtypeCons) return traverseSubtypeCons((SubtypeCons) _h);
        if (_h instanceof SubtypeEmpty) return traverseSubtypeEmpty((SubtypeEmpty) _h); else throw new RuntimeException("Unknown SubtypeList Variant");
    }

    public List<String> traverseSubtypeEmpty(final SubtypeEmpty _h) {
        return func.combine((SubtypeList) _h);
    }

    public List<String> traverseSubtypeCons(final SubtypeCons _h) {
        TypeUse _first = traverseTypeUse(_h.getFirst());
        List<String> _rest = traverseSubtypeList(_h.getRest());
        if ((_first instanceof TypeUse)) {
            if ((_rest instanceof List)) {
                return func.combine((SubtypeList) _h, (TypeUse) _first, (List<String>) _rest);
            } else {
                return func.combine((SubtypeList) _h);
            }
        } else {
            return func.combine((SubtypeList) _h);
        }
    }

    public String traverseSome_List_TypeDef__(final Some<List<TypeDef>> _h) {
        String _just = traverseList_TypeDef_(_h.getJust());
        return func.combine((Some<List<TypeDef>>) _h, (String) _just);
    }

    public Syntax traversePlus(final Plus _h) {
        return func.combine((Syntax) _h);
    }

    public List<String> traversePESubtypeList(final PESubtypeList _h) {
        if (_h instanceof NESubtypeList) return traverseNESubtypeList((NESubtypeList) _h);
        if (_h instanceof SubtypeEmpty) return traverseSubtypeEmpty((SubtypeEmpty) _h); else throw new RuntimeException("Unknown PESubtypeList Variant");
    }

    public String traverseOption_List_TypeDef__(final Option<List<TypeDef>> _h) {
        if (_h instanceof Some) return traverseSome_List_TypeDef__((Some<List<TypeDef>>) _h);
        if (_h instanceof None) return traverseNone_List_TypeDef__((None<List<TypeDef>>) _h); else throw new RuntimeException("Unknown Option Variant");
    }

    public String traverseNone_List_TypeDef__(final None<List<TypeDef>> _h) {
        return func.combine((None<List<TypeDef>>) _h);
    }

    public NETypeUseList traverseNETypeUseList(final NETypeUseList _h) {
        TypeUse _first = traverseTypeUse(_h.getFirst());
        TypeUseList _rest = traverseTypeUseList(_h.getRest());
        return func.combine((NETypeUseList) _h);
    }

    public List<String> traverseNESubtypeList(final NESubtypeList _h) {
        TypeUse _first = traverseTypeUse(_h.getFirst());
        List<String> _rest = traverseSubtypeList(_h.getRest());
        return func.combine((NESubtypeList) _h, (TypeUse) _first, (List<String>) _rest);
    }

    public Syntax traverseMinus(final Minus _h) {
        return func.combine((Syntax) _h);
    }

    public String traverseList_TypeDef_(final List<TypeDef> _h) {
        if (_h instanceof Cons) return traverseCons_TypeDef_((Cons<TypeDef>) _h);
        if (_h instanceof Empty) return traverseEmpty_TypeDef_((Empty<TypeDef>) _h); else throw new RuntimeException("Unknown List Variant");
    }

    public String traverseIntfcDef(final IntfcDef _h) {
        DoGen _gen = _h.getGen();
        ident _name = _h.getName();
        TypeDefParams _params = _h.getTparams();
        List<String> _subtypes = traversePESubtypeList(_h.getSubtypes());
        return func.combine((IntfcDef) _h, (DoGen) _gen);
    }

    public Syntax traverseFieldOrSyntax(final FieldOrSyntax _h) {
        if (_h instanceof Field) return traverseField((Field) _h);
        if (_h instanceof Syntax) return traverseSyntax((Syntax) _h); else throw new RuntimeException("Unknown FieldOrSyntax Variant");
    }

    public List<?> traverseFieldList(final FieldList _h) {
        if (_h instanceof FieldCons) return traverseFieldCons((FieldCons) _h);
        if (_h instanceof FieldEmpty) return traverseFieldEmpty((FieldEmpty) _h); else throw new RuntimeException("Unknown FieldList Variant");
    }

    public List<?> traverseFieldEmpty(final FieldEmpty _h) {
        return func.combine((FieldEmpty) _h);
    }

    public List<?> traverseFieldCons(final FieldCons _h) {
        Syntax _first = traverseFieldOrSyntax(_h.getFirst());
        List<?> _rest = traverseFieldList(_h.getRest());
        return func.combine((FieldCons) _h, (Object) _first, (List) _rest);
    }

    public Syntax traverseField(final Field _h) {
        ident _name = _h.getName();
        TypeUse _type = traverseTypeUse(_h.getType());
        return func.combine((Field) _h, (ident) _name, (TypeUse) _type);
    }

    public TypeUseParams traverseEmptyUseParams(final EmptyUseParams _h) {
        return func.combine((TypeUseParams) _h);
    }

    public String traverseEmpty_TypeDef_(final Empty<TypeDef> _h) {
        return func.combine((Empty<TypeDef>) _h);
    }

    public String traverseCons_TypeDef_(final Cons<TypeDef> _h) {
        String _first = traverseTypeDef(_h.getFirst());
        String _rest = traverseList_TypeDef_(_h.getRest());
        return func.combine((Cons<TypeDef>) _h, (String) _first, (String) _rest);
    }

    public String traverseClassDef(final ClassDef _h) {
        DoGen _gen = _h.getGen();
        ident _name = _h.getName();
        TypeDefParams _params = _h.getTparams();
        List<String> _subtypes = traversePESubtypeList(_h.getSubtypes());
        List<?> _fields = traverseFieldList(_h.getFields());
        Impl _ext = _h.getExt();
        return func.combine((ClassDef) _h, (DoGen) _gen, (ident) _name, (TypeDefParams) _params, (List<String>) _subtypes, (List<Syntax>) _fields);
    }

    public Syntax traverseAddToken(final AddToken _h) {
        String _str = _h.getStr();
        return func.combine((Syntax) _h);
    }

    public Syntax traverseAddTab(final AddTab _h) {
        return func.combine((Syntax) _h);
    }

    public Syntax traverseAddSpace(final AddSpace _h) {
        return func.combine((Syntax) _h);
    }

    public Syntax traverseAddReturn(final AddReturn _h) {
        return func.combine((Syntax) _h);
    }

    public Syntax traverseAddLine(final AddLine _h) {
        return func.combine((Syntax) _h);
    }
}
