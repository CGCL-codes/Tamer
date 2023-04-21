package org.expasy.jpl.msident.format.pepxml.io;

import java.text.ParseException;
import org.apache.commons.collections15.Transformer;
import org.expasy.jpl.core.mol.chem.MassCalculator;
import org.expasy.jpl.core.mol.polymer.pept.rule.EditionRule;
import org.expasy.jpl.msident.format.pepxml.SearchSummaryElement;
import org.expasy.jpl.msident.format.pepxml.SearchSummaryElement.AminoacidModification;
import org.expasy.jpl.msident.format.pepxml.SearchSummaryElement.SearchDatabase;
import org.expasy.jpl.msident.format.pepxml.SearchSummaryElement.SearchSummaryChildElement;
import org.expasy.jpl.msident.model.impl.query.DatabaseSearchSettings;

public class SearchSummaryElement2DatabaseSearchSettingsTransformer implements Transformer<SearchSummaryElement, DatabaseSearchSettings> {

    private static final AAMod2RuleTransformer MOD2RULE_TRANSFORMER = new AAMod2RuleTransformer();

    @Override
    public DatabaseSearchSettings transform(SearchSummaryElement element) {
        DatabaseSearchSettings settings = new DatabaseSearchSettings();
        settings.setSearchId(element.getSearchId());
        for (SearchSummaryChildElement child : element.getElements()) {
            if (child instanceof SearchDatabase) {
                SearchDatabase sdb = (SearchDatabase) child;
                settings.setDb(sdb.getLocalPath(), sdb.getType());
            } else if (child instanceof AminoacidModification) {
                AminoacidModification aaMod = (AminoacidModification) child;
                settings.addModifAAMassToModifMass(String.valueOf(aaMod.getMass()), aaMod.getMassDiff());
                settings.addModificationRule(MOD2RULE_TRANSFORMER.transform(aaMod));
            }
        }
        settings.setSearchEngine(element.getSearchEngine());
        try {
            settings.setPrecursorMassCalc(MassCalculator.valueOf(element.getPrecursorMassType()));
        } catch (ParseException e) {
            System.err.println("Warning: " + e.getMessage() + " (set precursor mass calc to monoisotopic accuracy)");
            settings.setPrecursorMassCalc(MassCalculator.getMonoAccuracyInstance());
        }
        try {
            settings.setFragmentMassCalc(MassCalculator.valueOf(element.getFragmentMassType()));
        } catch (ParseException e) {
            System.err.println("Warning: " + e.getMessage() + " (set fragment mass calc to monoisotopic accuracy)");
            settings.setFragmentMassCalc(MassCalculator.getMonoAccuracyInstance());
        }
        return settings;
    }

    public static class AAMod2RuleTransformer implements Transformer<AminoacidModification, EditionRule> {

        @Override
        public EditionRule transform(AminoacidModification aaMod) {
            return DatabaseSearchSettings.createRule(aaMod.getAminoAcid(), aaMod.getMass(), aaMod.getMassDiff(), aaMod.isVariable());
        }
    }
}
