package ch.bbv.mda;

import ch.bbv.mda.uml.*;

/**
 * The visitor interface to traverse the MDA model of data objects..
 * @author MarcelBaumann
 * @version $Revision: 1.9 $
 */
public interface Visitor {

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaModel item);

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaPackage item);

    /**
   * Visits view instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaView item);

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaClass item);

    /**
   * Visits use case instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaUseCase item);

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaProperty item);

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaIndexedProperty item);

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(MetaDatatype item);

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(Stereotype item);

    /**
   * Visits class instances of this type as part of the visitor pattern.
   * @param item item to visit
   */
    void visit(TaggedValue item);
}
