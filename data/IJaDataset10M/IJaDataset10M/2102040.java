package com.google.gxp.compiler;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gxp.compiler.base.TemplateName;
import com.google.gxp.compiler.bind.Binder;
import com.google.gxp.compiler.bind.BoundTree;
import com.google.gxp.compiler.collapse.SpaceCollapsedTree;
import com.google.gxp.compiler.collapse.SpaceCollapser;
import com.google.gxp.compiler.escape.EscapedTree;
import com.google.gxp.compiler.escape.Escaper;
import com.google.gxp.compiler.flatten.ContentFlattenedTree;
import com.google.gxp.compiler.flatten.ContentFlattener;
import com.google.gxp.compiler.fs.FileRef;
import com.google.gxp.compiler.i18ncheck.I18nCheckedTree;
import com.google.gxp.compiler.i18ncheck.I18nChecker;
import com.google.gxp.compiler.ifexpand.IfExpandedTree;
import com.google.gxp.compiler.ifexpand.IfExpander;
import com.google.gxp.compiler.msgextract.MessageExtractedTree;
import com.google.gxp.compiler.msgextract.MessageExtractor;
import com.google.gxp.compiler.parser.ParseTree;
import com.google.gxp.compiler.parser.Parser;
import com.google.gxp.compiler.phinsert.PlaceholderInsertedTree;
import com.google.gxp.compiler.phinsert.PlaceholderInserter;
import com.google.gxp.compiler.phpivot.PlaceholderPivotedTree;
import com.google.gxp.compiler.phpivot.PlaceholderPivoter;
import com.google.gxp.compiler.reparent.ReparentedTree;
import com.google.gxp.compiler.reparent.Reparenter;
import com.google.gxp.compiler.servicedir.ServiceDirectory;
import com.google.gxp.compiler.validate.ValidatedTree;
import com.google.gxp.compiler.validate.Validator;
import java.io.IOException;

/**
 * A compilation unit. Represents a source file, and has (lazy) accessors for
 * the various things that it can be transformed into.
 */
public class CompilationUnit {

    private final ServiceDirectory serviceDirectory;

    private final Parser parser;

    private final FileRef sourceFileRef;

    private final String className;

    private final long compilationVersion;

    CompilationUnit(ServiceDirectory serviceDirectory, Parser parser, FileRef sourceFileRef, long compilationVersion) {
        this.serviceDirectory = Preconditions.checkNotNull(serviceDirectory);
        this.parser = Preconditions.checkNotNull(parser);
        this.sourceFileRef = Preconditions.checkNotNull(sourceFileRef);
        String fileName = sourceFileRef.removeExtension().getName();
        this.className = fileName.substring(1).replace('/', '.');
        this.compilationVersion = compilationVersion;
    }

    /**
   * @return The FileRef for this CompilationUnit's source file.
   */
    public FileRef getSourceFileRef() {
        return sourceFileRef;
    }

    public long getCompilationVersion() {
        return compilationVersion;
    }

    public TemplateName.FullyQualified getTemplateName() {
        return TemplateName.parseFullyQualifiedDottedName(className);
    }

    private <K, V> Supplier<V> memoCompose(Function<K, V> function, Supplier<? extends K> supplier) {
        return Suppliers.memoize(Suppliers.compose(function, supplier));
    }

    private final Supplier<ParseTree> parseTreeSupplier = Suppliers.memoize(new Supplier<ParseTree>() {

        public ParseTree get() {
            try {
                return parser.parse(sourceFileRef);
            } catch (IOException iox) {
                throw new RuntimeException(iox);
            }
        }
    });

    public ParseTree getParseTree() {
        return parseTreeSupplier.get();
    }

    private final Supplier<IfExpandedTree> ifExpandedTreeSupplier = memoCompose(new IfExpander(), parseTreeSupplier);

    public IfExpandedTree getIfExpandedTree() {
        return ifExpandedTreeSupplier.get();
    }

    private final Supplier<ReparentedTree> reparentedTreeSupplier = Suppliers.memoize(new Supplier<ReparentedTree>() {

        public ReparentedTree get() {
            return new Reparenter(parser.getSchemaFactory(), className).apply(getIfExpandedTree());
        }
    });

    public ReparentedTree getReparentedTree() {
        return reparentedTreeSupplier.get();
    }

    private final Supplier<BoundTree> boundTreeSupplier = Suppliers.memoize(new Supplier<BoundTree>() {

        public BoundTree get() {
            return new Binder(parser.getSchemaFactory(), serviceDirectory).apply(getReparentedTree());
        }
    });

    public BoundTree getBoundTree() {
        return boundTreeSupplier.get();
    }

    private final Supplier<SpaceCollapsedTree> spaceCollapsedTreeSupplier = memoCompose(new SpaceCollapser(), boundTreeSupplier);

    public SpaceCollapsedTree getSpaceCollapsedTree() {
        return spaceCollapsedTreeSupplier.get();
    }

    private final Supplier<PlaceholderInsertedTree> placeholderInsertedTreeSupplier = memoCompose(new PlaceholderInserter(), spaceCollapsedTreeSupplier);

    public PlaceholderInsertedTree getPlaceholderInsertedTree() {
        return placeholderInsertedTreeSupplier.get();
    }

    private final Supplier<EscapedTree> escapedTreeSupplier = memoCompose(new Escaper(), placeholderInsertedTreeSupplier);

    public EscapedTree getEscapedTree() {
        return escapedTreeSupplier.get();
    }

    private final Supplier<ValidatedTree> validatedTreeSupplier = memoCompose(new Validator(), escapedTreeSupplier);

    public ValidatedTree getValidatedTree() {
        return validatedTreeSupplier.get();
    }

    private final Supplier<ContentFlattenedTree> contentFlattenedTreeSupplier = memoCompose(new ContentFlattener(), validatedTreeSupplier);

    public ContentFlattenedTree getContentFlattenedTree() {
        return contentFlattenedTreeSupplier.get();
    }

    private final Supplier<PlaceholderPivotedTree> placeholderPivotedTreeSupplier = memoCompose(new PlaceholderPivoter(), contentFlattenedTreeSupplier);

    public PlaceholderPivotedTree getPlaceholderPivotedTree() {
        return placeholderPivotedTreeSupplier.get();
    }

    private final Supplier<I18nCheckedTree> i18nCheckedTreeSupplier = Suppliers.memoize(new Supplier<I18nCheckedTree>() {

        public I18nCheckedTree get() {
            return I18nChecker.INSTANCE.apply(getSpaceCollapsedTree(), getPlaceholderPivotedTree());
        }
    });

    public I18nCheckedTree getI18nCheckedTree() {
        return i18nCheckedTreeSupplier.get();
    }

    private final Supplier<MessageExtractedTree> messageExtractedTreeSupplier = memoCompose(new MessageExtractor(), i18nCheckedTreeSupplier);

    public MessageExtractedTree getMessageExtractedTree() {
        return messageExtractedTreeSupplier.get();
    }
}
