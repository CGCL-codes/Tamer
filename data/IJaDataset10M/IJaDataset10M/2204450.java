package takatuka.classreader.dataObjs.attribute;

import takatuka.classreader.dataObjs.*;

/**
 * <p>Title: </p>
 * <p>Description:
 * Based on section 4.7.6 at http://java.sun.com/docs/books/jvms/second_edition/html/ClassFile.doc.html
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class SyntheticAtt extends AttributeInfo {

    public SyntheticAtt() {
    }

    public SyntheticAtt(Un u2_attrNameIndex, Un u4_attributeLength) throws Exception {
        super(u2_attrNameIndex, u4_attributeLength);
    }
}
