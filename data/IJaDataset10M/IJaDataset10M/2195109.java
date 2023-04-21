package org.ogre4j;

import org.xbig.base.*;

public interface IVertexElement extends INativeObject, org.ogre4j.IGeometryAllocatedObject {

    /** **/
    public int getSource();

    /** **/
    public int getOffset();

    /** **/
    public org.ogre4j.VertexElementType getType();

    /** **/
    public org.ogre4j.VertexElementSemantic getSemantic();

    /** **/
    public int getIndex();

    /** **/
    public int getSize();

    /** **/
    public boolean operatorEqual(org.ogre4j.IVertexElement rhs);

    /** 
    Adjusts a pointer to the base of a vertex to point at this element. **/
    public void baseVertexPointerToElement_a(VoidPointer pBase, NativeObjectPointer<VoidPointer> pElem);

    /** 
    Adjusts a pointer to the base of a vertex to point at this element. **/
    public void baseVertexPointerToElement_b(VoidPointer pBase, NativeObjectPointer<FloatPointer> pElem);

    /** 
    Adjusts a pointer to the base of a vertex to point at this element. **/
    public void baseVertexPointerToElement_c(VoidPointer pBase, NativeObjectPointer<LongPointer> pElem);

    /** 
    Adjusts a pointer to the base of a vertex to point at this element. **/
    public void baseVertexPointerToElement_d(VoidPointer pBase, NativeObjectPointer<ShortPointer> pElem);

    /** 
    Adjusts a pointer to the base of a vertex to point at this element. **/
    public void baseVertexPointerToElement_e(VoidPointer pBase, NativeObjectPointer<IntegerPointer> pElem);
}
