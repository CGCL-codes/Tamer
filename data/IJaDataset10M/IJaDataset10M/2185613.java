package astcentric.structure.validation.regex;

import astcentric.structure.regex.RegexTerm;
import astcentric.structure.regex.RegexTermCollection;
import astcentric.structure.regex.Sequence;
import astcentric.structure.tool.Compiler;

class RegexSequenceFactory<T> extends RegexTermCollectionFactory<T> {

    RegexSequenceFactory(Compiler<RegexTerm<T>, RegexCompilationContext> factory) {
        super(factory);
    }

    protected RegexTermCollection<T> createCollection() {
        return new Sequence<T>();
    }
}
