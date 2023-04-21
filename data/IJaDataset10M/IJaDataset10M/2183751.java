package gov.nist.atlas.util;

import gov.nist.atlas.*;
import gov.nist.atlas.impl.AnnotationInitializer;
import gov.nist.atlas.impl.MinimalImplementationDelegate;
import gov.nist.atlas.ref.SignalRef;
import gov.nist.atlas.spi.ImplementationDelegate;
import gov.nist.atlas.spi.TypeImplementationDelegate;
import gov.nist.atlas.type.ATLASType;
import gov.nist.atlas.type.ATLASTypeFactory;
import gov.nist.atlas.type.ChildrenType;
import java.net.URI;
import java.net.URL;

/**
 * <p>An ATLASImplementation provides access to a set of essential concrete
 * classes that implements some utility interfaces of the ATLAS framework.</p>
 *
 * <p>It is basically expected to get all the factories the user would need
 * to use the ATLAS framework.</p>
 *
 * <p>FIX-ME: Consider moving some functionality to SPI
 * Consider using type subclasses instead of ATLASType in methods...
 * </p>
 *
 * @version $Revision: 1.22 $
 * @author Christophe Laprun, Sylvain Pajot
 */
public abstract class ATLASImplementation {

    /**
   * Gets the implementation of the ATLASTypeFactory for this
   * ATLASImplementation
   *
   * @return the implementation of the ATLASTypeFactory for this
   * ATLASImplementation
   *
   * @deprecated Should use ATLASTypeFactory.getFactoryFor (not finished)
   */
    public abstract ATLASTypeFactory getATLASTypeFactory();

    /**
   * Gets the implementation of the ATLASElementSetFactory for this
   * ATLASImplementation
   *
   * @return the implementation of the ATLASElementSetFactory for this
   * ATLASImplementation
   *
   * @deprecated Do we really need such a method? See ATLASElementSetFactory
   */
    public abstract ATLASElementSetFactory getATLASElementSetFactory();

    /**
   * Gets the AIF version implemented by this ATLASImplementation
   *
   * @return the AIF version implemented by ATLASImplementation
   *
   * @deprecated Rename to getATLASVersion?
   */
    public abstract String getAIFVersion();

    /**
   * Returns a new Id for a given ATLASType.
   *
   * @param type The type to create the Id for
   * @return the new Id for the specified ATLASType
   */
    public abstract Id createNewIdFor(ATLASType type);

    /**
   * <p>Returns the Id corresponding to a given String. If such an Id exists
   * already, this method returns the existing Id.</p>
   *
   * @param stringId the String representation from which to create a new Id
   *
   * @return the Id created (or retrieved) from the specified String
   */
    public abstract Id createNewIdFor(String stringId);

    /**
   * Resolves and returns the Id object for a given name.
   *
   * @param stringId the Id as a string
   *
   * @return the Id for the specified name
   */
    public abstract Id resolveIdFor(String stringId);

    /**
   * Creates a new GUID.
   *
   * @return a new GUID
   * @since 2.0 Beta 6
   */
    public abstract Id createGUID();

    /**
   * Resolves and returns the Unit object for a given name.
   *
   * @param unitName the Unit as a string
   *
   * @return the Unit for the specified name
   */
    public abstract Unit resolveUnitFor(String unitName);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Analysis. This
   * Analysis must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Analysis
   *
   * @deprecated Move to SPI
   */
    public abstract Analysis newAnalysis(ATLASType type, ATLASElement parent, Id id);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Anchor. This
   * Anchor must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Anchor
   *
   * @deprecated Move to SPI
   */
    public abstract Anchor newAnchor(ATLASType type, ATLASElement parent, Id id, SignalRef signal);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Annotation. This
   * Annotation must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Annotation
   *
   * @deprecated Move to SPI
   */
    public abstract Annotation newAnnotation(ATLASType type, ATLASElement parent, Id id, AnnotationInitializer initializer);

    /**
   * Creates a new, empty Children. The specific type of the Children is decided
   * from the type of contained elements.
   *
   * @param type
   * @param parent
   * @return
   *
   * @since 2.0 beta 4
   */
    public abstract Children newChildren(ChildrenType type, ATLASElement parent);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Content. This
   * Content must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Content
   *
   * @deprecated Move to SPI
   */
    public abstract Content newContent(ATLASType type, ATLASElement parent);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Corpus. This
   * Corpus must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Corpus
   *
   * @deprecated Use newCorpus(ATLASType, URI) instead
   * @see #newCorpus(gov.nist.atlas.type.ATLASType, java.net.URI)
   */
    public Corpus newCorpus(ATLASType type, Id id, URL location) {
        return newCorpus(type, null);
    }

    /**
   *
   * @param type
   * @param uri
   * @return
   * @since 2.0 Beta 6
   */
    public abstract Corpus newCorpus(ATLASType type, URI uri);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Feature. This
   * Feature must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Feature
   *
   * @deprecated Move to SPI
   */
    public abstract Feature newFeature(ATLASType type, ATLASElement parent);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Metadata. This
   * Metadata must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Metadata
   *
   * @deprecated Move to SPI
   */
    public abstract Metadata newMetadata(ATLASType type, ATLASElement parent);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Parameter. This
   * Parameter must then be initialized in the context of an ATLASElementFactory
   * methods.           `
   *
   * @return a new, totally empty and <strong>invalid</strong> Parameter
   *
   * @deprecated Move to SPI
   */
    public abstract Parameter newParameter(ATLASType type, ATLASElement parent, Unit unit, String value);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Region. This
   * Region must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> Region
   *
   * @deprecated Move to SPI
   */
    public abstract Region newRegion(ATLASType type, ATLASElement parent, Id id);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> SimpleSignal. This
   * SimpleSignal must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> SimpleSignal
   *
   * @deprecated Move to SPI
   */
    public abstract SimpleSignal newSimpleSignal(ATLASType type, ATLASElement parent, Id id, URL location, String track);

    /**
   * Creates a new, totally empty and <strong>invalid</strong> Signalgroups. This
   * SignalGroup must then be initialized in the context of an ATLASElementFactory
   * methods.
   *
   * @return a new, totally empty and <strong>invalid</strong> SignalGroup
   *
   * @deprecated Move to SPI
   */
    public abstract SignalGroup newSignalGroup(ATLASType type, ATLASElement parent, Id id);

    /**
   *
   * @return
   *
   * @deprecated Move to SPI
   */
    public abstract TypeImplementationDelegate newTypeDelegate();

    /**
   *
   * @param type
   * @return
   *
   * @deprecated Move to SPI
   */
    public abstract ImplementationDelegate createImplementationDelegate(ATLASType type);

    /**
   *
   * @param type
   * @param parent
   * @param id
   * @return
   * @since 2.0 Beta 6
   */
    public abstract ReusableATLASElement newInvalidReusableATLASElement(ATLASType type, ATLASElement parent, Id id);

    /**
   *
   * @param type
   * @return
   *
   * @deprecated Move to SPI
   */
    protected abstract MinimalImplementationDelegate createMinimalImplementationDelegate(ATLASType type);
}
