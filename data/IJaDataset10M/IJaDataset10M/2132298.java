package net.sf.jcorrect.standard.validator.visitors;

import java.lang.reflect.Field;
import net.sf.jcorrect.standard.validator.ValidatorUtils;

public class LookForFieldWithConstraints extends LookForElementWithContraintHierarchyVisitor<Field> {

    /**
	 * @param propertyName a decapitalize property name
	 */
    public LookForFieldWithConstraints(String propertyName) {
        super(propertyName);
    }

    public boolean visit(Class<?> clazz) {
        try {
            member = clazz.getDeclaredField(memberName);
            constraints = ValidatorUtils.getConstraints(member);
            return true;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("No filed " + memberName + " at " + clazz.getName());
            }
        }
        return false;
    }
}
