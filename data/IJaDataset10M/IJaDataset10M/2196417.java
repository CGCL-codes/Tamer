package org.bionote.page.type;

/**
 * @author mbreese
 *
 */
public class GeneralType extends AbstractPageTypeWithAnnotation implements PageType {

    public String getTypeName() {
        return "General page";
    }

    public boolean isPublic() {
        return true;
    }
}
