package org.xmlfield.validation.test.newapi.range;

import org.xmlfield.annotations.FieldXPath;
import org.xmlfield.annotations.ResourceXPath;
import org.xmlfield.validation.annotations.Range;

/**
 * @author Nicolas Richeton <nicolas.richeton@capgemini.com>
 */
@ResourceXPath("/item")
public interface IIntegerModel {

    @Range(min = 2, max = 10)
    @FieldXPath("integer1")
    Integer getInteger1();

    void setInteger1(Integer i);

    @Range(min = 2, max = 10)
    @FieldXPath("integer0")
    int getInteger0();

    void setInteger0(int i);
}
