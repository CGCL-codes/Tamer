package com.loribel.commons.business.metamodel;

import com.loribel.commons.business.abstraction.GB_BusinessObject;
import com.loribel.commons.business.impl.bo.generated.GB_BOVisitor;

/**
 * Abstraction of business object of meta model.
 *
 * @author Gregory Borelli
 */
public interface GB_BOMetaModel extends GB_BusinessObject {

    Object accept(GB_BOVisitor a_visitor);
}
