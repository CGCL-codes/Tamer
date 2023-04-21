package org.mged.annotare.magetabtemplatemanager.designer.map;

import java.util.List;
import java.util.Set;
import org.mged.annotare.magetabtemplatemanager.designer.TemplateDesigner;
import org.mged.annotare.magetabtemplatemanager.designer.TemplateDesignerError;
import org.mged.annotare.magetabtemplatemanager.designer.TemplateDesignerAnswers.AnswersFormatSetter;
import org.mged.annotare.magetabtemplatemanager.designer.manager.AllTableDataManager;
import org.mged.annotare.magetabtemplatemanager.designer.table.Category;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.MAGETABInvestigation;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.HybridizationNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.CharacteristicsAttribute;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.FactorValueAttribute;

/**
 * @author mm
 *
 */
public interface MapUserAnswers {

    /**
	 * maps the information from setter into the IDF and SDRF members of investigation
	 * @param setter container for the information to map
	 * @param investigation containor of the IDF and SDRF information
	 * @param trm  table data information
	 */
    void map(AnswersFormatSetter setter, MAGETABInvestigation investigation, AllTableDataManager trm);

    public class MapUserAnswersHelper {

        /**
		 * Add an option to a dropdown comment
		 * @param ddName name of the column for the dropdown
		 * @param option the option to add
		 * @param deFault whether this option is the default
		 * @param investigation the container of the IDF to add the option to
		 */
        protected static void setDropdownOption(String ddName, String option, boolean deFault, MAGETABInvestigation investigation) {
            String optionDD = null;
            Set<String> dropdowns = investigation.IDF.getComments().get("DROPDOWN");
            if (null != dropdowns) {
                for (String dropdown : dropdowns) {
                    if (dropdown.startsWith(ddName)) {
                        optionDD = dropdown;
                        dropdowns.remove(optionDD);
                        break;
                    }
                }
            }
            if (null == optionDD) {
                optionDD = ddName + ":";
            } else {
                optionDD += ',';
            }
            optionDD += option + (deFault ? '*' : "");
            investigation.IDF.addComment("DROPDOWN", optionDD);
        }

        /**
		 * Add the categories as either Characteristics and/or Experimental Factors
		 * @param cats the list of categories
		 * @param investigation the containor of the IDF and SDRF
		 */
        protected static void addCategories(List<Category> cats, MAGETABInvestigation investigation) {
            for (Category category : cats) {
                if (!category.isDeleted()) {
                    if (category.isBMC()) {
                        addCharacteristic(category, investigation);
                    }
                    if (category.isFV()) {
                        addExperimentalFactor(category, investigation);
                    }
                }
            }
        }

        /**
		 * add a characteristic to the Source Name Node
		 * @param category information on the characteristic
		 * @param investigation container for the SDRF
		 */
        private static void addCharacteristic(Category category, MAGETABInvestigation investigation) {
            try {
                SourceNode node = investigation.SDRF.lookupNodes(SourceNode.class).iterator().next();
                CharacteristicsAttribute attr = new CharacteristicsAttribute();
                attr.type = category.getOntologyTerm();
                attr.termSourceREF = TemplateDesigner.MO_NAME;
                if (!characteristicsExists(node, attr)) {
                    node.characteristics.add(attr);
                }
            } catch (Exception e) {
                throw new TemplateDesignerError("Problem creating the characteristic for " + category.getDisplayLabel());
            }
        }

        /**
		 * add an experimental factor to the IDF and a factor value to the Hybridization Name Node
		 * @param category information on the Experimental Factor
		 * @param investigation container for the IDF and SDRF
		 */
        private static void addExperimentalFactor(Category category, MAGETABInvestigation investigation) {
            try {
                if (!investigation.IDF.experimentalFactorName.contains(category.getOntologyTerm())) {
                    investigation.IDF.experimentalFactorName.add(category.getOntologyTerm());
                    investigation.IDF.experimentalFactorType.add(category.getOntologyTerm());
                    investigation.IDF.experimentalFactorTermSourceREF.add(TemplateDesigner.MO_NAME);
                    HybridizationNode node = investigation.SDRF.lookupNodes(HybridizationNode.class).iterator().next();
                    FactorValueAttribute fv = new FactorValueAttribute();
                    fv.setNodeName(category.getOntologyTerm());
                    fv.setNodeType(category.getOntologyTerm());
                    fv.type = category.getOntologyTerm();
                    fv.termSourceREF = TemplateDesigner.MO_NAME;
                    if (!node.factorValues.contains(fv)) {
                        node.factorValues.add(fv);
                    }
                }
            } catch (Exception e) {
                throw new TemplateDesignerError("Problem creating the experimental factor for " + category.getDisplayLabel());
            }
        }

        /**
		 * checks to see if the given attribute is already on the characteristics list
		 * @param source the SourceNode to chack
		 * @param attr the attribute
		 * @return true if an equivalent attribute is on the characteristics list, false otherwise
		 */
        public static boolean characteristicsExists(SourceNode source, CharacteristicsAttribute attr) {
            try {
                for (CharacteristicsAttribute curAttr : source.characteristics) {
                    if (curAttr.type.equalsIgnoreCase(attr.type) && ((null == curAttr.termSourceREF && null == attr.termSourceREF) || (null != curAttr.termSourceREF && curAttr.termSourceREF.equalsIgnoreCase(attr.termSourceREF)))) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                throw new TemplateDesignerError("Failed check for an exisiting characteristic", e);
            }
        }
    }
}
