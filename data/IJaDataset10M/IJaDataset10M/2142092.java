package net.sourceforge.modelintegra.core.metamodel.extension.util;

import net.sourceforge.modelintegra.core.customizing.UseCaseTypes;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UC;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UCTypeEnum;
import org.apache.log4j.Logger;

/**
 * set of helper functions
 */
public class UCHelper {

    private static final Logger LOGGER = Logger.getLogger(UCHelper.class.getName());

    public static String getUCType(UC pUC) {
        String strUCType = null;
        if (pUC.getType() == UCTypeEnum.BUSINESS_LITERAL) {
            strUCType = UseCaseTypes.getBusinessStereotype();
        } else if (pUC.getType() == UCTypeEnum.SUMMARY_LITERAL) {
            strUCType = UseCaseTypes.getSummaryStereotype();
        } else if (pUC.getType() == UCTypeEnum.PRIMARY_LITERAL) {
            strUCType = UseCaseTypes.getPrimaryStereotype();
        } else if (pUC.getType() == UCTypeEnum.SECONDARY_LITERAL) {
            strUCType = UseCaseTypes.getSecondaryStereotype();
        } else if (pUC.getType() == UCTypeEnum.TECHNICAL_LITERAL) {
            strUCType = UseCaseTypes.getTechnicalStereotype();
        }
        return strUCType;
    }
}
